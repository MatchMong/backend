package com.example.demo.discordbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomParticipant {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ROOM room;

    private String userDiscordId;
    private String userNickname;
}
