package com.dppojo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dppojo.dto.LoginFormDTO;
import com.dppojo.dto.Result;
import com.dppojo.dto.UserDTO;
import com.dppojo.entity.User;
import com.dppojo.mapper.UserMapper;
import com.dppojo.service.IUserService;
import com.dppojo.utils.RegexUtils;
import com.dppojo.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import static com.dppojo.utils.RedisConstants.*;
import static com.dppojo.utils.SystemConstants.USER_NICK_NAME_PREFIX;


@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String tokenKey;

    @Override
    public Result sendCode(String phone, HttpSession session) {
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
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
//        1、校验手机号
        String phone = loginForm.getPhone();
        if(RegexUtils.isPhoneInvalid(phone)){
            //        2、不符合返回错误信息
            return Result.fail("手机号格式错误!");
        }
//        2、校验验证码
//        Object cacheCode = session.getAttribute("code");
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
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
        return Result.ok(tokenKey);
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
    public Result logout() {
        if(tokenKey == null || tokenKey.isEmpty())
            return Result.fail("未登录无法退出！");
        Boolean isSuccess = stringRedisTemplate.delete(LOGIN_USER_KEY + tokenKey);
        if(!isSuccess)
            return Result.fail("退出失败！");
        UserHolder.removeUser();
        return Result.ok();
    }

    private User createUserWithPhone(String phone) {
//        1、创建用户
        User user = new User();
        user.setPassword(phone);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
//        2、保存用户
        save(user);
        return user;
    }
}
