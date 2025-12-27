package com.example.demo.discordbot.service;

import com.example.demo.discordbot.ParticipantRepository; // 경로 확인 필요
import com.example.demo.discordbot.entity.ROOM;
import com.example.demo.discordbot.dto.RoomRequest;
import com.example.demo.discordbot.entity.RoomParticipant;
import com.example.demo.discordbot.repository.RoomRepository;
import com.example.demo.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;

    // 1. 방 생성 (토큰의 Discord ID 저장)
    @Transactional
    public ROOM save(RoomRequest dto, String ownerDiscordId) {
        return roomRepository.save(ROOM.builder()
                .ownerId(ownerDiscordId)
                .roomtitle(dto.getRoomtitle())
                .roomwrite(dto.getRoomwrite())
                .build());
    }

    // 2. 방 참가 로직
    @Transactional
    public void joinRoom(Long roomId, User user) {
        ROOM room = findById(roomId);

        RoomParticipant participant = RoomParticipant.builder()
                .room(room)
                .userDiscordId(user.getDiscordId())
                .userNickname(user.getNickname()) // User 엔티티에 nickname 필드가 있어야 함
                .build();

        participantRepository.save(participant);
    }

    // 3. 참여자 목록 가져오기 (중복 제거됨)
    public List<RoomParticipant> getParticipants(Long roomId) {
        return participantRepository.findByRoom_RoomId(roomId);
    }

    // 4. ID로 방 찾기 (Long 타입으로 통일)
    public ROOM findById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다. ID: " + roomId));
    }

    // 5. 모든 방 조회 (매개변수 제거)
    @Transactional(readOnly = true)
    public List<ROOM> findAll() {
        return roomRepository.findAll();
    }
}