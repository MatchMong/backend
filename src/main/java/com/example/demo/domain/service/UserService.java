package com.example.demo.domain.service;

import com.example.demo.discordbot.service.DiscordBot;
import com.example.demo.domain.RefreshToken;
import com.example.demo.domain.dto.AddUserRequest;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.RefreshTokenRepository;
import com.example.demo.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    private final RefreshTokenRepository refreshTokenRepository;
    private final DiscordBot discordBot;

    public Long save(AddUserRequest dto) {

        String snowflakeId = discordBot.getUserIdByName(dto.getDiscordId());
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .discordId(dto.getDiscordId())
                .nickname(dto.getNickname())
                .major(dto.getMajor())
                .nicknameid(snowflakeId)
                .build()).getId();
    }

    public User findById(Long userid) {
        return userRepository.findById(userid)
                .orElseThrow(()->new IllegalArgumentException("Unexpected user"));
    }

    @Transactional
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    @Transactional
    public void updateRefreshToken(Long userId, String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(refreshToken))
                .orElse(new RefreshToken(userId, refreshToken));

        refreshTokenRepository.save(token);
    }

    @Transactional
    public void deleteRefreshTokenByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        refreshTokenRepository.deleteByUserId(user.getId());
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
