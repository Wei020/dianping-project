package com.example.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.feign.clients.ShopClient;
import com.example.feign.entity.Voucher;
import com.example.user.dto.LoginFormDTO;
import com.example.user.dto.Result;
import com.example.user.dto.UserDTO;
import com.example.user.entity.User;
import com.example.user.entity.UserInfo;
import com.example.user.mapper.UserMapper;
import com.example.user.service.IUserInfoService;
import com.example.user.service.IUserService;
import com.example.user.utils.RegexUtils;
import com.example.user.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static com.example.user.utils.RedisConstants.*;
import static com.example.user.utils.SystemConstants.USER_NICK_NAME_PREFIX;


@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String tokenKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ShopClient shopClient;

    @Override
    public Result sendPhoneCode(String phone) {
//        1、校验手机号
        if(RegexUtils.isPhoneInvalid(phone)){
            //        2、不符合返回错误信息
            return Result.fail("手机号格式错误");
        }
//        3、符合，生成验证码
        String code = RandomUtil.randomNumbers(6);
//        4、保存验证码到session
//        session.setAttribute("code",code);
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
//        5、发送验证码
        log.info("发送短信验证码成功，验证码:"+code);
        return Result.ok("验证码已发送！");
    }

    @Override
    public Result sendEmailCode(String eamil) {
        //        1、校验邮箱
        if(RegexUtils.isEmailInvalid(eamil)){
            //        2、不符合返回错误信息
            return Result.fail("邮箱格式错误!");
        }
//        3、符合，生成验证码
        String code = RandomUtil.randomNumbers(6);
//        4、保存验证码到session
//        session.setAttribute("code",code);
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + eamil, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
//        5、发送验证码
//        log.info("发送邮箱验证码成功，验证码:"+code);
        Map<String, String> map = new HashMap<>();
        map.put("email", eamil);
        map.put("code", code);
        rabbitTemplate.convertAndSend("code.direct","code.email", map);
        return Result.ok("验证码已发送！");
    }

    @Override
    public Result login(LoginFormDTO loginForm) {
//        1、校验手机号
        String phone = loginForm.getPhone();
        if(phone != null)
            return loginByPhone(loginForm, phone);
        String email = loginForm.getEmail();
        if(RegexUtils.isEmailInvalid(email)){
            //        2、不符合返回错误信息
            return Result.fail("邮箱格式错误!");
        }
        return LoginByEmailCode(loginForm, email);
    }

    private Result loginByPhone(LoginFormDTO loginForm, String phone) {
        if(RegexUtils.isPhoneInvalid(phone)){
            //        2、不符合返回错误信息
            return Result.fail("手机号格式错误!");
        }
//        判断是否密码登录
        String pwd = loginForm.getPassword();
        if(pwd == null || pwd.isEmpty()){
            return LoginByPhoneCode(loginForm, phone);
        }else{
            User user = query().eq("phone", phone).eq("password", pwd).one();
            if(user == null)
                return Result.fail("密码错误！");
            else{
                saveUserMsg(user);
                return Result.ok(tokenKey);
            }

        }
    }

    private Result LoginByEmailCode(LoginFormDTO loginForm, String email) {
        //        2、校验验证码
        Object cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + email);
        String code = loginForm.getCode();
        if(cacheCode == null || !cacheCode.toString().equals(code)){
            //        3、不一致，报错
            return Result.fail("验证码错误！");
        }
//        4、一致，根据手机号查询用户
        User user = query().eq("email", email).one();
//        5、判断用户是否存在
        if (user == null) {
            //        6、不存在，创建新用户并保存
            user = createUserWithEmail(email);
        }
//        保存用户信息
        saveUserMsg(user);
        return Result.ok(tokenKey);
    }

    private Result LoginByPhoneCode(LoginFormDTO loginForm, String phone) {
        //        2、校验验证码
        Object cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        String code = loginForm.getCode();
        if(cacheCode == null || !cacheCode.toString().equals(code)){
            //        3、不一致，报错
            return Result.fail("验证码错误！");
        }
//        4、一致，根据手机号查询用户
        User user = query().eq("phone", phone).one();
//        5、判断用户是否存在
        if (user == null) {
            //        6、不存在，创建新用户并保存
            user = createUserWithPhone(phone);
        }
//        保存用户信息
        saveUserMsg(user);
        return Result.ok(tokenKey);
    }

    private void saveUserMsg(User user) {
        //        7、保存用户信息到redis中
//        7.1、随机生成token，作为登录令牌
        tokenKey = UUID.randomUUID().toString(true);
//        7.2、将user对象转为hash存储
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
//        session.setAttribute("user", BeanUtil.copyProperties(user, UserDTO.class));
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                    CopyOptions.create().setIgnoreNullValue(true).setFieldValueEditor(
                            (fieldName, fieldValue) -> fieldValue.toString()
                    )
                );
//        7.3、存储
        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY + tokenKey,userMap);
//        7.4、设置token有效期
        stringRedisTemplate.expire(LOGIN_USER_KEY + tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
    }

    //    签到功能
    @Override
    public Result sign() {
        Long userId = UserHolder.getUser().getId();
        LocalDateTime now = LocalDateTime.now();
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = USER_SIGN_KEY + userId + keySuffix;
        int dayOfMonth = now.getDayOfMonth();
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
        return Result.ok();
    }

    @Override
    public Result signCount() {
        Long userId = UserHolder.getUser().getId();
        LocalDateTime now = LocalDateTime.now();
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = USER_SIGN_KEY + userId + keySuffix;
        int dayOfMonth = now.getDayOfMonth();
        List<Long> result = stringRedisTemplate.opsForValue().bitField(
                key, BitFieldSubCommands.create()
                        .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth))
                        .valueAt(0)
        );
        if(result == null || result.isEmpty()){
            return Result.ok(0);
        }
        Long num = result.get(0);
        if(num == null || num == 0){
            return Result.ok(0);
        }
        int count = 0;
        while (true){
            if((num & 1) == 0){
                break;
            }else {
                count++;
            }
            num >>>= 1;
        }
        return Result.ok(count);
    }

    @Override
    public Result logout(HttpServletRequest request) {
        tokenKey = request.getHeader("authorization");
        if(tokenKey == null || tokenKey.isEmpty())
            return Result.fail("未登录无法退出！");
        Boolean isSuccess = stringRedisTemplate.delete(LOGIN_USER_KEY + tokenKey);
        if(!isSuccess)
            return Result.fail("退出失败！");
        return Result.ok();
    }

    @Override
    public Result queryUserById(Long userId) {
        // 查询详情
        User user = getById(userId);
        if (user == null) {
            return Result.ok();
        }
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        // 返回
        return Result.ok(userDTO);
    }

    @Override
    public Result info(Long userId) {
        // 查询详情
        UserInfo info = userInfoService.getById(userId);
        if (info == null) {
            // 没有详情，应该是第一次查看详情
            return Result.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);
        // 返回
        return Result.ok(info);
    }

    @Override
    public Result queryVoucherByUser() {
        Long userId = UserHolder.getUser().getId();
        List<Voucher> voucherByUser = shopClient.findVoucherByUser(userId);
        System.out.println("有没有" + voucherByUser.toString());
        return Result.ok(voucherByUser);
    }

    private User createUserWithPhone(String phone) {
//        1、创建用户
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
//        2、保存用户
        save(user);
        return user;
    }

    private User createUserWithEmail(String email) {
//        1、创建用户
        User user = new User();
        user.setEmail(email);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
//        2、保存用户
        save(user);
        return user;
    }
}
