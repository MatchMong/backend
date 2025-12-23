package com.example.demo.discordbot;

import com.example.demo.discordbot.dto.RoomRequest;
import com.example.demo.discordbot.repository.RoomRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    @GeneratedValue
    @Column(name = "id",updatable = false)
    private Long roomId;

    @Column(name = "ownerId", nullable = false)
    private String ownerId;

    @Column(name = "roomtitle", nullable = false)
    private String roomtitle;

    @Column(name = "roomwrite",nullable = false)
    private String roomwrite;

    public ROOM(Long roomId, String ownerId) {
        this.roomId = roomId;
        this.ownerId = ownerId;
    }
}
