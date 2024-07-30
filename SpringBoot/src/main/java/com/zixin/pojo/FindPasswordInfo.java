package com.zixin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindPasswordInfo {
    private String email; // 邮箱
    private String password; // 密码
    private String passwordConfirm; // 确认密码
    private String code; // 验证码
}
