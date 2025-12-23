package com.example.demo.profile.service;

import com.example.demo.domain.dto.AddUserRequest;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // 생성자 주입 자동화
@Transactional // 데이터 변경을 안전하게 보장
public class MeService {

    private final UserRepository userRepository;

    public Long saveOrUpdate(AddUserRequest dto) {
        // 1. 기존 유저가 있는지 확인 (이메일 기준)
        return userRepository.findByEmail(dto.getEmail())
                .map(user -> {
                    // 2. 있다면 정보 업데이트 (더티 체킹 발생)
                    user.updateMajor(dto.getMajor());
                    // 추가적으로 이름이나 디코드ID 업데이트 메서드를 엔티티에 만들어 쓰면 좋습니다.
                    return user.getId();
                })
                .orElseGet(() -> {
                    // 3. 없다면 신규 저장
                    User newUser = User.builder()
                            .email(dto.getEmail())
                            .major(dto.getMajor())
                            .discordId(dto.getDiscordId())
                            .selfwrite(dto.getSelfwrite())
                            .password("temp_pwd") // 필수값인 경우 초기값 설정
                            .build();
                    return userRepository.save(newUser).getId();
                });
    }
}