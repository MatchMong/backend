package com.example.demo.discordbot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 이 줄이 반드시 추가되어야 합니다!
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ROOM room;

    private String userDiscordId;
    private String userNickname;
}
