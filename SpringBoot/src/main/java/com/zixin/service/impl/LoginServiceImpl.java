package com.zixin.service.impl;

import com.zixin.pojo.FindPasswordInfo;
import com.zixin.pojo.Result;
import com.zixin.pojo.User;
import com.zixin.service.LoginService;
import com.zixin.service.UserService;
import com.zixin.utils.Md5Util;
import com.zixin.utils.StringUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserService userService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result findPassword(FindPasswordInfo info) {
        if (info == null)
            return Result.error("参数格式不规范");

        //获取参数
        String email = info.getEmail();
        String code = info.getCode();
        String password = info.getPassword();
        String passwordConfirm = info.getPasswordConfirm();

        if (StringUtils.isBlank(email) && StringUtils.isBlank(code) && StringUtils.isBlank(password) && StringUtils.isBlank(passwordConfirm)){
            return Result.error("参数不合法");
        }
        else if (code.length() != 6) {
            // 验证码长度校验
            return Result.error("验证码格式不正确");
        }
        else if (!passwordConfirm.equals(password)) {
            // 密码与确认密码校验
            return Result.error("两次输入密码不相同");
        }

        // 查询用户，是否存在
        User user = userService.findByEmail(email);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // 获取正确的验证码
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisTemplate.getConnectionFactory());
        String rightCode = stringRedisTemplate.opsForValue().get("EMAIL_" + email);
        if (!code.equals(rightCode)) {
            // 验证码比对
            return Result.error("验证码错误");
        }

        // 删除验证码
        redisTemplate.delete("EMAIL_" + email);

        // 修改密码
        User user1 = new User();
        user1.setId(user.getId());
        //对密码进行加密
        String pwd = Md5Util.getMD5String(password);


        // 修改
        userService.updatePasswordByEmail(email, pwd);
        return Result.success();
    }
}