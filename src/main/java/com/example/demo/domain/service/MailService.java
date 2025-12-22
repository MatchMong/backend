package com.example.demo.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final Map<String, String> verificationCodes = new HashMap<>(); // 실제로는 Redis 권장

    public void sendVerificationCode(String email) {
        String code = String.valueOf((int)(Math.random() * 899999) + 100000); // 6자리 생성
        verificationCodes.put(email, code); // 메모리에 임시 저장

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("회원가입 인증 번호입니다.");
        message.setText("인증 번호: " + code);
        mailSender.send(message);
    }

    public boolean verifyCode(String email, String code) {
        return code.equals(verificationCodes.get(email));
    }
}
