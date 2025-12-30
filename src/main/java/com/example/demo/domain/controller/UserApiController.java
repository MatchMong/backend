package com.example.demo.domain.controller;

import com.example.demo.domain.dto.*;
import com.example.demo.global.config.jwt.TokenProvider;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.service.MailService;
import com.example.demo.domain.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import com.example.demo.discordbot.service.DiscordBot;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

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
        String email = request.getEmail();
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("이메일을 입력해주세요.");
        }
        mailService.sendVerificationCode(email);
        return ResponseEntity.ok("인증 번호가 발송되었습니다.");
    }

    @PostMapping("/find-password/send-code")
    public ResponseEntity<String> findPasswordSendCode(@RequestBody AddUserRequest request) {
        try {
            mailService.sendVerificationCode(request.getEmail());
            return ResponseEntity.ok("인증 번호가 발송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/find-password/verify")
    public ResponseEntity<String> findPasswordVerify(@RequestBody PasswordVerifyRequest request) {
        System.out.println("인증 요청 발생: " + request.getEmail());

        try {
            mailService.verifyTemporaryPassword(request.getEmail(), request.getVerificationCode());
            return ResponseEntity.ok("인증 성공! 새로운 임시 비밀번호가 메일로 발송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AddUserRequest request) {
        System.out.println("저장하려는 이메일: " + request.getEmail());
        if (!mailService.verifyCode(request.getEmail(), request.getVerificationCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 정보가 유효하지 않습니다.");
        }
        userService.save(request);
        return ResponseEntity.ok("회원가입 완료");
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

    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.deleteRefreshTokenByEmail(email);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/users/profiles")
    public ResponseEntity<List<UserProfileResponse>> getAllUserProfiles() {
                List<UserProfileResponse> profiles = userService.findAllUsers().stream()
                .map(user -> new UserProfileResponse(
                        user.getId(),       // 1. ID
                        user.getNickname(), // 2. 닉네임
                        user.getMajor() != null ? user.getMajor() : "미등록" // 3. 전공
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(profiles);
    }
    @GetMapping("/api/users/{userId}")
    public ResponseEntity<UserDetailResponse> getUserDetail(@PathVariable Long userId) {
        // 1. ID로 유저 정보 조회
        User user = userService.findById(userId);

        // 2. 필요한 정보만 DTO에 담아서 반환
        UserDetailResponse response = new UserDetailResponse(
                user.getNickname(),
                user.getMajor(),
                user.getEmail(),
                user.getDiscordId(),
                user.getSelfwrite()
        );

        return ResponseEntity.ok(response);
    }
}
