package com.zixin.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendSimpleMail(String to, String theme, String content, String... cc);

    void sendHtmlMail(String to, String subject, String content, String... cc) throws Exception;
}
