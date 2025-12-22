package com.example.demo.domain.controller;

import com.example.demo.global.config.jwt.TokenProvider;
import com.example.demo.domain.dto.AddUserRequest;
import com.example.demo.domain.dto.LoginRequest;
import com.example.demo.domain.dto.LoginResponse;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.service.MailService;
import com.example.demo.domain.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final MailService mailService;



    @PostMapping("/signup/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody AddUserRequest request) {
        boolean isVerified = mailService.verifyCode(request.getEmail(), request.getVerificationCode());

        if (isVerified) {
            return ResponseEntity.ok("인증에 성공했습니다. 비밀번호를 설정해주세요.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 번호가 틀렸습니다.");
        }
    }

    @PostMapping("/signup/send-code")
    public ResponseEntity<String> sendCode(@RequestBody AddUserRequest request) {
        // 1. 이메일 주소 확인
        String email = request.getEmail();
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("이메일을 입력해주세요.");
        }

        // 2. 메일 발송
        mailService.sendVerificationCode(email);

        return ResponseEntity.ok("인증 번호가 발송되었습니다.");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AddUserRequest request) {
        // 마지막으로 한 번 더 확인 (보안상 중요!)
        if (!mailService.verifyCode(request.getEmail(), request.getVerificationCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 세션이 만료되었거나 번호가 틀립니다.");
        }

        userService.save(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // 1. 유저 검증 (UserService에서 처리 권장)
        User user = userService.login(request.getEmail(), request.getPassword());

        // 2. 토큰 생성
        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));
        String refreshToken = tokenProvider.generateToken(user, Duration.ofDays(14));

        // 3. 리프레시 토큰 DB 저장 (이 로직이 꼭 들어가야 합니다!)
        userService.updateRefreshToken(user.getId(), refreshToken);

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }

    @PostMapping("/api/logout") // [수정] Get -> Post, React 방식 응답
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok().build();
    }
}
