package com.example.demo.discordbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class discordmessageRequest {
    private Long roomid;
    private String message;
}