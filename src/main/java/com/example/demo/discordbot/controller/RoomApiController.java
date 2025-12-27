package com.example.demo.discordbot.controller;

import com.example.demo.discordbot.dto.ParticipantResponse;
import com.example.demo.discordbot.service.DiscordBot;
import com.example.demo.discordbot.dto.RoomRequest;
import com.example.demo.discordbot.dto.RoomResponse;
import com.example.demo.discordbot.service.RoomService;
import com.example.demo.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.demo.discordbot.entity.ROOM;
import com.example.demo.discordbot.dto.discordmessageRequest;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomApiController {
    private final RoomService roomService;
    private final DiscordBot discordBot;

    @PostMapping
    public ResponseEntity<ROOM> createRoom(
            @AuthenticationPrincipal User loginUser,
            @RequestBody RoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.save(request, loginUser.getDiscordId()));
    }
    @GetMapping
    public ResponseEntity<List<ROOM>> getAllRooms() {
        List<ROOM> rooms = roomService.findAll();
        return ResponseEntity.ok(rooms);
    }
    @PostMapping("/{roomId}/join")
    public ResponseEntity<String> joinRoom(
            @AuthenticationPrincipal User loginUser,
            @PathVariable Long roomId) {
        try {
            roomService.joinRoom(roomId, loginUser);

        // 2. 방장 정보 가져오기
        ROOM room = roomService.findById(roomId);

        // 3. 방장에게 DM 보내기 (고유 ID 사용)
        String message = String.format("🔔 [%s] 방에 '%s'님이 입장하셨습니다.",
                room.getRoomtitle(), loginUser.getNickname());
        discordBot.sendDM(room.getOwnerId(), message);

            return ResponseEntity.ok("방 참여 및 방장 알림 전송 완료");
        } catch (RuntimeException e) {
            // "정원이 가득 찼습니다" 같은 에러 메시지를 클라이언트에 보냄
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{roomId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable Long roomId) {
        List<ParticipantResponse> result = roomService.getParticipants(roomId).stream()
                .map(p -> new ParticipantResponse(p.getUserNickname(), p.getUserDiscordId()))
                .toList();
        return ResponseEntity.ok(result);
    }
    // 2. 메시지 보내기: 요청자와 방장이 일치하는지 확인
    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(
            @AuthenticationPrincipal User loginUser,
            @RequestBody discordmessageRequest request) { // DTO를 사용하여 roomId와 message를 모두 받음

        // 1. 방 정보 조회 (request에서 roomId를 꺼냄)
        ROOM room = roomService.findById(request.getRoomid());

        // 2. 메시지 조립 (보낸 사람의 정보를 하단에 추가)
        String senderInfo = String.format("\n\n(보낸 이: %s [%s])",
                loginUser.getNickname(), loginUser.getDiscordId());
        String finalMessage = request.getMessage() + senderInfo;

        // 3. 방장(ownerId)에게 DM 전송
        discordBot.sendDM(room.getOwnerId(), finalMessage);

        return ResponseEntity.ok("방장에게 메시지를 성공적으로 보냈습니다!");
    }
    //방 수정
    @PutMapping("/{roomId}")
    public ResponseEntity<ROOM> updateRoom(
            @AuthenticationPrincipal User loginUser,
            @PathVariable Long roomId,
            @RequestBody RoomRequest request) {

        ROOM updatedRoom = roomService.update(roomId, request, loginUser.getDiscordId());
        return ResponseEntity.ok(updatedRoom);
    }

    //방 삭제
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(
            @AuthenticationPrincipal User loginUser,
            @PathVariable Long roomId) {

        roomService.delete(roomId, loginUser.getDiscordId());
        return ResponseEntity.noContent().build(); // 성공 시 데이터 없이 204 응답
    }
}