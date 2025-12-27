package com.example.demo.discordbot.dto;

import com.example.demo.discordbot.entity.ROOM;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomResponse {
    private String roomtitle;
    private String roomwrite;

    public RoomResponse(ROOM room) {
        this.roomtitle = room.getRoomtitle();
        this.roomwrite = room.getRoomwrite();
    }
}