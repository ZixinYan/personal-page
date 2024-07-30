package com.zixin.service;

import com.zixin.pojo.FindPasswordInfo;
import com.zixin.pojo.Result;

public interface LoginService {
    /**
     * 找回密码
     * @param info (邮箱、验证码、新密码、确认密码)
     * @return
     */
    Result findPassword(FindPasswordInfo info);
}