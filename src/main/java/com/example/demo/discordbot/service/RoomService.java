package com.example.demo.discordbot.service;

import com.example.demo.discordbot.ROOM;
import com.example.demo.discordbot.dto.RoomRequest;
import com.example.demo.discordbot.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional
    public ROOM save(RoomRequest dto) {
        return roomRepository.save(ROOM.builder()
                .roomtitle(dto.getRoomtitle())
                .roomwrite(dto.getRoomwrite())
                .ownerId(dto.getDiscordid())
                .build());
    }

    @Transactional(readOnly = true)
    public List<ROOM> findAll() {
        return roomRepository.findAll();
    }
}