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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ROOM room;

    private String userDiscordId;
    private String userNickname;
}
