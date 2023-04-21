package com.example.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.feign.dto.UserListDTO;
import com.example.user.dto.*;
import com.example.user.entity.User;
import com.example.user.entity.UserInfo;
import com.example.user.mapper.UserMapper;
import com.example.user.service.SendMailService;
import com.example.user.service.UserInfoService;
import com.example.user.service.UserService;
import com.example.user.utils.RegexUtils;
import com.example.user.utils.SystemConstants;
import com.example.user.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.user.utils.RedisConstants.*;
import static com.example.user.utils.SystemConstants.USER_NICK_NAME_PREFIX;


@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String tokenKey;

    @Autowired
    private UserInfoService userInfoService;


//    @Autowired
//    private ShopClient shopClient;

    @Autowired
    private SendMailService sendMailService;

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
//        Map<String, String> map = new HashMap<>();
//        map.put("email", eamil);
//        map.put("code", code);
//        rabbitTemplate.convertAndSend("code.direct","code.email", map);
        sendMailService.sendMail(eamil, code);
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
                tokenKey = UUID.randomUUID().toString(true);
                saveUserMsg(user, tokenKey);
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
        //        7.1、随机生成token，作为登录令牌
        tokenKey = UUID.randomUUID().toString(true);
        saveUserMsg(user, tokenKey);
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
        //        7.1、随机生成token，作为登录令牌
        tokenKey = UUID.randomUUID().toString(true);
        saveUserMsg(user, tokenKey);
        return Result.ok(tokenKey);
    }

    private void saveUserMsg(User user, String tokenKey) {
        //        7、保存用户信息到redis中
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
        log.info("查询id:" + userId);
        UserInfo info = userInfoService.getById(userId);
        log.info("查询结果:" + JSONUtil.toJsonStr(info));
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
        return null;
    }

//    @Override
//    public Result queryVoucherByUser() {
//        Long userId = UserHolder.getUser().getId();
//        VoucherOrder voucherOrder = shopClient.findVoucherByUser(userId);
//        return Result.ok(voucherOrder);
//    }

    @Override
    public Result edit(User user, HttpServletRequest request) {
        boolean update = updateById(user);
        tokenKey = request.getHeader("authorization");
        saveUserMsg(user, tokenKey);
        String key = CHAT_LIST_KEY + user.getId();
        stringRedisTemplate.delete(key);
        return Result.ok(update);
    }

    @Override
    public Result infoEdit(UserInfo userInfo) {
        boolean update = false;
        if(null == userInfo.getUserId()){
            userInfo.setUserId(UserHolder.getUser().getId());
            log.info("新增数据:" + userInfo);
        }
        update = userInfoService.saveOrUpdate(userInfo);
        return Result.ok(update);
    }

    @Override
    public Result findPassword(LoginFormDTO loginForm) {
        String phone = loginForm.getPhone();
        String email = loginForm.getEmail();
        String code = loginForm.getCode();
        String password = loginForm.getPassword();
        Object cacheCode = null;
        if(phone != null){
            cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        }else
            cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + email);
        if(cacheCode == null || !cacheCode.toString().equals(code)){
            //        3、不一致，报错
            return Result.fail("验证码错误！");
        }
        boolean update = false;
        if(phone != null)
            update = update().set("password", password).eq("phone", phone).update();
        else
            update = update().set("password", password).eq("email", email).update();
        return Result.ok(update);
    }

    @Override
    public Result queryUserList(UserListDTO userListDTO) {
        List<Long> ids = userListDTO.getIds();
        String idStr = userListDTO.getIdStr();
        log.info("转换结果:" + ids);
        if(idStr.isEmpty()){
            List<UserDTO> userDTOS = listByIds(ids)
                    .stream()
                    .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                    .collect(Collectors.toList());
            return Result.ok(userDTOS);
        }
        List<UserDTO> userDTOS = query().in("id", ids).last("order by field(id, " + idStr + ")")
                .list()
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                .collect(Collectors.toList());
        return Result.ok(userDTOS);
    }

    @Override
    public Result queryUserByCondition(String condition) {
        log.info("查询条件:" + condition);
        List<User> users = null;
        if(!RegexUtils.isPhoneInvalid(condition)){
            log.info("手机号");
            users = query().eq("phone", condition).list();
        }else if(!RegexUtils.isEmailInvalid(condition)){
            log.info("邮箱号");
            users = query().eq("email", condition).list();
        }else{
            log.info("昵称");
            users = query().like("nick_name", condition).list();
        }
        log.info("查询结果:" + users);
        List<UserDTO> userDTOs = new LinkedList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            BeanUtil.copyProperties(user, userDTO);
            userDTOs.add(userDTO);
        }
        return Result.ok(userDTOs);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean followUser(Long id, Long followId) {
        UserInfo userInfo = userInfoService.getById(id);
        boolean flag1 = false;
        boolean flag2 = false;
        if(null == userInfo){
            UserInfo userInfo1 = new UserInfo();
            userInfo1.setUserId(id);
            userInfo1.setFollowee(1);
            flag1 = userInfoService.save(userInfo1);
        }else {
            userInfo.setFollowee(userInfo.getFollowee() + 1);
            flag1 = userInfoService.updateById(userInfo);
        }
        UserInfo userInfo1 = userInfoService.getById(followId);
        if(null == userInfo1){
            UserInfo userInfo2 = new UserInfo();
            userInfo2.setUserId(followId);
            userInfo2.setFans(1);
            flag2 = userInfoService.save(userInfo2);
        }else {
            userInfo1.setFans(userInfo1.getFans() + 1);
            flag2 = userInfoService.updateById(userInfo1);
        }

        return flag1 && flag2;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean notFollowUser(Long id, Long followId) {
        UserInfo userInfo = userInfoService.getById(id);
        boolean flag1 = false;
        boolean flag2 = false;
        userInfo.setFollowee(userInfo.getFollowee() - 1);
        flag1 = userInfoService.updateById(userInfo);
        UserInfo userInfo1 = userInfoService.getById(followId);
        userInfo1.setFans(userInfo1.getFans() - 1);
        flag2 = userInfoService.updateById(userInfo1);
        return flag1 && flag2;
    }

    @Override
    public List<OptionsDTO> queryCity() {
        CountryDTO countryDTOs = JSONObject.parseObject(SystemConstants.CITY_JSON, CountryDTO.class);
        List<OptionsDTO> res = new LinkedList<>();
        for (ProvinceDTO province : countryDTOs.getProvinces()) {
            OptionsDTO optionsDTO = new OptionsDTO();
            optionsDTO.setValue(province.getProvinceName());
            optionsDTO.setLabel(province.getProvinceName());
            List<OptionsDTO> children = new LinkedList<>();
            for (CityDTO city : province.getCitys()) {
                OptionsDTO optionsDTO1 = new OptionsDTO();
                optionsDTO1.setValue(city.getCityName());
                optionsDTO1.setLabel(city.getCityName());
                children.add(optionsDTO1);
            }
            optionsDTO.setChildren(children);
            res.add(optionsDTO);
        }
        return res;
    }

    private User createUserWithPhone(String phone) {
//        1、创建用户
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
//        2、保存用户
        save(user);
        log.info("新增的id：" + user.getId());
        Boolean res = createUserInfo(user.getId());
        if(!res)
            log.info("创建失败:" + user.getId());
        return user;
    }

    private User createUserWithEmail(String email) {
//        1、创建用户
        User user = new User();
        user.setEmail(email);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
//        2、保存用户
        save(user);
        Boolean res = createUserInfo(user.getId());
        if(!res)
            log.info("创建失败:" + user.getId());
        return user;
    }

    private Boolean createUserInfo(Long Id){
        log.info("拿到的Id：" + Id);
        boolean save = userInfoService.saveUserInfo(Id);
        return save;
    }
}
