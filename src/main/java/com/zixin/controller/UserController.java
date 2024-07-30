package com.zixin.controller;

import com.zixin.pojo.Result;
import com.zixin.pojo.User;
import com.zixin.service.UserService;
import com.zixin.utils.JwtUtil;
import com.zixin.utils.Md5Util;
import com.zixin.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

import java.util.HashMap;
import java.util.Map;


@RestController
@Component
@Validated
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$")String password){
        //check(use validation)

        //search username if exist
            User u = userService.findByUserName(username);
        if(u == null){
            //register
            userService.register(username,password);
            return Result.success();
        }else{
            return Result.error("username has existed");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$")String password){
        //search username
        User u = userService.findByUserName(username);
        //if exist
        if(u==null){
            return Result.error("username or password wrong");
        }
        //check password
        if(Md5Util.getMD5String(password).equals(u.getPassword())){
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",u.getId());
            claims.put("username",u.getUsername());
            String token = JwtUtil.genToken(claims);
            StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisTemplate.getConnectionFactory());
            stringRedisTemplate.opsForValue().set(token, token, Duration.ofHours(1));
            return Result.success(token);
        }else{
            return Result.error("username or password wrong");
        }
    }
    @GetMapping("/userinfo")
    public Result<User> userInfo(@RequestHeader(name = "Authorization")String token){
        //Map<String,Object> map= JwtUtil.parseToken(token);
        //String username = (String)map.get("username");
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String)map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token){
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");
        if (StringUtils.hasLength(oldPwd)||StringUtils.hasLength(newPwd)||StringUtils.hasLength(rePwd)) {
            Map<String,Object> map = ThreadLocalUtil.get();
            User user = userService.findByUserName((String)map.get("username"));
            if(user.getPassword().equals(Md5Util.getMD5String(oldPwd))){
                if(newPwd.equals(rePwd)){
                    userService.updatePassword(newPwd);
                    ValueOperations<String, String> ops = redisTemplate.opsForValue();
                    ops.getOperations().delete(token);
                    return Result.success();
                }else{
                    return Result.error("Two passwords you typed don't match");
                }
            }else{
                return Result.error("Old password is wrong");
            }
        }else{
            return Result.error("Required parameter missing");
        }
    }
}
