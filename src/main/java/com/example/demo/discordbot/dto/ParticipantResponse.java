package com.example.demo.discordbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipantResponse {
    private String nickname;
    private String discordId;
}
