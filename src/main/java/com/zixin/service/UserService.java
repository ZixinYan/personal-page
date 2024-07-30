package com.zixin.service;

import com.zixin.pojo.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User findByUserName(String userName);

    void register(String userName, String password);

    void update(User user);

    void updateAvatar(String avatarUrl);

    void updatePassword(String newPwd);

    User findByEmail(String email);

    void updatePasswordByEmail(String email, String newPwd);
}
