package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailResponse {
    private String nickname;
    private String major;
    private String email;
    private String discordId;
    private String selfwrite; // 유저가 작성한 자기소개
}