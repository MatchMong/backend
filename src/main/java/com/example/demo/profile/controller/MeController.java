package com.example.demo.profile.controller;

import com.example.demo.domain.dto.AddUserRequest;
import com.example.demo.profile.dto.MeDto;
import com.example.demo.profile.service.MeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile") // API 경로 구조화
@RequiredArgsConstructor
public class MeController {

    private final MeService meService;

    @GetMapping("/me")

    public List<MeDto> me(@AuthenticationPrincipal com.example.demo.domain.entity.User user) {
        String major = user.getMajor();
        if (major == null || major.isBlank()) major = "무전공";
        return List.of(
                new MeDto(user.getId(), major, user.getEmail(), user.getDiscordId(), user.getSelfwrite())
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Long> saveMe(@RequestBody AddUserRequest request) {
        // 성공 시 ID 반환, 에러 처리는 GlobalExceptionHandler에 맡기면 더 깔끔합니다.
        Long savedId = meService.saveOrUpdate(request);
        return ResponseEntity.ok(savedId);
    }
}