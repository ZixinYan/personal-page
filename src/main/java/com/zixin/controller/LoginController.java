package com.zixin.controller;

import com.zixin.pojo.FindPasswordInfo;
import com.zixin.pojo.Result;
import com.zixin.service.LoginService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @ApiOperation("找回密码接口")
    @PostMapping("/findPassword")
    public Result findPassword(@RequestBody FindPasswordInfo info){
        return loginService.findPassword(info);
    }
}