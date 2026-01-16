package com.dotoryteam.dotory.domain.auth.service;

import com.dotoryteam.dotory.domain.auth.exception.CustomAuthException;
import com.dotoryteam.dotory.global.redis.service.SecurityRedisService;
import com.dotoryteam.dotory.global.security.enums.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.service.SecurityService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {
    private final JavaMailSender mailSender;
    private final SecurityRedisService securityRedisService;

    private static final String CODE_PREFIX = "CODE: ";
    private static final long CODE_EXPIRATION = 300000L;

    public void sendVerificationCode(String email) {
        String code = createCode();

        try {
            MimeMessage message = createEmailForm(email , "도토리 인증 메일입니다." , code);
            mailSender.send(message);

            securityRedisService.setValue(
                    CODE_PREFIX + email ,
                    code ,
                    CODE_EXPIRATION
            );
        } catch (MessagingException e) {
            throw new CustomAuthException(ErrorCode.MAIL_SEND_ERROR);
        }
    }

    private String createCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            key.append(random.nextInt(10));
        }

        return key.toString();
    }

    private MimeMessage createEmailForm(String email, String title, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message , true , "UTF-8");

        helper.setTo(email);
        helper.setSubject(title);
        helper.setText("<div><h1>Dotory 인증번호 안내입니다.</h1>" +
                "<h3>아래 인증번호를 입력해주세요.</h3>" +
                "<h1>" + code + "</h1>" +
                "</div>", true);

        return message;
    }
}
