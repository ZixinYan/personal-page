package com.zixin.service.impl;

import com.zixin.pojo.FindPasswordInfo;
import com.zixin.pojo.Result;
import com.zixin.service.ThreadService;
import com.zixin.service.VerificationCodeService;
import com.zixin.utils.StringUtil;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ThreadService threadService;
    @Override
    public Result getRequestPermissionCode(FindPasswordInfo info) {
        // 非空校验
        if (StringUtils.isBlank(info.getEmail()))
            return Result.error("参数格式不正确");
        //邮箱校验
        if (!StringUtil.checkEmail(info.getEmail())) {
            return Result.error("邮箱格式不正确");
        }
        // 随机生成权限码
        String permissionCode = UUID.randomUUID().toString();
        System.out.println(permissionCode);
        // 存入redis，缓存10s
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisTemplate.getConnectionFactory());
        stringRedisTemplate.opsForValue().set("EMAIL_REQUEST_VERIFY_" + info.getEmail(), permissionCode, Duration.ofSeconds(20));
        return permissionCode != null ? Result.success(permissionCode) : Result.error("获取权限码失败");
    }
    @Override
    public Result sendEmailCode(FindPasswordInfo info) {
        if (info == null)
            return Result.error("参数格式不正确");
        //获取邮箱和权限码
        String email = info.getEmail();
        String permissionCode = info.getCode();
        // 参数校验
        if (StringUtils.isBlank(email) && StringUtils.isBlank(permissionCode)) {
            return Result.error("参数格式不正确");
        }else if (!StringUtil.checkEmail(email)) {
            // 邮箱校验
            return Result.error("邮箱格式不正确");
        }else {
            // 权限码比对
            StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisTemplate.getConnectionFactory());
            String rightCode = stringRedisTemplate.opsForValue().get("EMAIL_REQUEST_VERIFY_" + info.getEmail());
            System.out.println(email);
            System.out.println(rightCode);
            System.out.println(permissionCode);
            if (!permissionCode.equals(rightCode)) {
                // 不通过
                return Result.error("非法操作");
            }
        }
        // 全部通过
        // 随机生成6位数字验证码
        String code = StringUtil.randomSixCode();
        // 正文内容
        String content = "忘记密码的杂鱼大哥哥：\n" +
                "你的验证码是：\n\n" +
                code + "\n\n" +
                "要好好在五分钟内使用哦\n" +
                "真是的，连密码都能忘记，杂鱼杂鱼！";
        // 发送验证码
        threadService.sendSimpleMail(email, "心怀感激的收下吧：" + code, content);
        // 丢入缓存，设置5分钟过期
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisTemplate.getConnectionFactory());
        stringRedisTemplate.opsForValue().set("EMAIL_" + email, code, Duration.ofMinutes(5));

        return Result.success();
    }
}