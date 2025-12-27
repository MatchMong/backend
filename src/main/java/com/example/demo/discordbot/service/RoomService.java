package com.example.demo.discordbot.service;

import com.example.demo.discordbot.repository.ParticipantRepository; // 경로 확인 필요
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

    @Transactional
    public ROOM update(Long roomId, RoomRequest request, String loginUserId) {
        ROOM room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다."));

        if (!room.getOwnerId().equals(loginUserId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        room.setRoomtitle(request.getRoomtitle());
        room.setRoomwrite(request.getRoomwrite());
        // 엔티티에 세터(Setter)나 수정 메서드가 있어야 합니다.
        return room;
    }

    @Transactional
    public void delete(Long roomId, String loginUserId) {
        ROOM room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다."));

        if (!room.getOwnerId().equals(loginUserId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        roomRepository.delete(room);
    }
    // RoomService.java 안의 joinRoom 메서드 (하나만 남기세요!)

    @Transactional
    public void joinRoom(Long roomId, User loginUser) {
        // 1. 방 존재 여부 확인
        ROOM room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));

        if (participantRepository.existsByRoom_RoomIdAndUserDiscordId(roomId, loginUser.getDiscordId())) {
            throw new RuntimeException("이미 이 방에 참여 신청을 하셨습니다!");
        }
        // 2. 현재 참여 인원수 체크 (새로 추가한 로직)
        long currentCount = participantRepository.countByRoom_RoomId(roomId);

        // 3. 인원 제한 확인
        if (currentCount >= room.getMaxParticipants()) {
            throw new RuntimeException("정원이 초과되었습니다. (최대 인원: " + room.getMaxParticipants() + "명)");
        }

        // 4. 참여 정보 저장
        RoomParticipant participant = new RoomParticipant();
        participant.setRoom(room);
        participant.setUserNickname(loginUser.getNickname());
        participant.setUserDiscordId(loginUser.getDiscordId());

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