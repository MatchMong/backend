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
public class ROOM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id",updatable = false)
    private Long roomId;

    @Column
    private String nickname;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "roomtitle", nullable = false)
    private String roomtitle;

    @Column(name = "roomwrite",nullable = false)
    private String roomwrite;

    @Column(nullable = false)
    private int maxParticipants;


    public String getOwnerId() {
        return this.ownerId;
    }
    public void setRoomtitle(String roomtitle) {
        this.roomtitle = roomtitle;
    }

    public void setRoomwrite(String roomwrite) {
        this.roomwrite = roomwrite;
    }

}
