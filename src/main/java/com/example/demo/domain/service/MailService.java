package com.example.demo.domain.service;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, String> verificationCodes = new HashMap<>(); // 실제로는 Redis 권장

    public void sendVerificationCode(String email) {
        if (email == null) {
            System.out.println("에러: 이메일 값이 null입니다!");
            return;
        }
        String code = String.valueOf((int)(Math.random() * 899999) + 100000); // 6자리 생성
        verificationCodes.put(email, code); // 메모리에 임시 저장

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("매치몽 인증 번호입니다.");
        message.setText("인증 번호: " + code);
        mailSender.send(message);

        System.out.println("--- 인증번호 생성 완료 ---");
        System.out.println("저장 이메일: [" + email + "]");
        System.out.println("저장 코드: [" + code + "]");
        System.out.println("현재 저장된 총 개수: " + verificationCodes.size());
        System.out.println("------------------------");
    }

    public boolean verifyCode(String email, String verificationCode) {
        String savedCode = verificationCodes.get(email);
        System.out.println("--- 인증번호 검증 시작 ---");
        System.out.println("입력된 이메일: [" + email + "]");
        System.out.println("입력된 코드: [" + verificationCode + "]");
        System.out.println("저장된 코드: [" + savedCode + "]");
        System.out.println("-----------------------");
        if (savedCode == null) return false;
        return Objects.equals(verificationCode, savedCode);
    }

    public void sendTemporaryPassword(String email, String tempPassword) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("임시 비밀번호 안내입니다.");
        message.setText("새로운 임시 비밀번호: " + tempPassword);
        mailSender.send(message);
    }

    public void verifyTemporaryPassword(String email, String code) {
        if (!verifyCode(email, code)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        user.updatePassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        sendTemporaryPassword(email, tempPassword);
    }

}
