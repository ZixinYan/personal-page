package com.zixin.service;

import com.zixin.pojo.FindPasswordInfo;
import com.zixin.pojo.Result;

public interface VerificationCodeService {
    /**
     * 获取请求权限码
     * 使用权限码校验防止故意重复使用该接口
     * @param info (邮箱)
     * @return
     */
    Result getRequestPermissionCode(FindPasswordInfo info);
    /**
     * 发送邮箱验证码
     * @param info （邮箱和权限码）
     * @return
     */
    Result sendEmailCode(FindPasswordInfo info);
}