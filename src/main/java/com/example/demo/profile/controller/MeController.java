package com.example.demo.profile.controller;

import com.example.demo.profile.dto.MeDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class MeController {

    @GetMapping("/me")
    public List<MeDto> me(@AuthenticationPrincipal com.example.demo.domain.entity.User user) {
        String major = user.getMajor();
        if (major == null || major.isBlank()) major = "무전공";

        return List.of(
                new MeDto(user.getId(), major, user.getEmail())
        );
    }
}