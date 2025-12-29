package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest {
    private String email;
    private String password;
    private String major;
    private String discordId;
    private String verificationCode;
    private String selfwrite;
    private String nickname;
}


