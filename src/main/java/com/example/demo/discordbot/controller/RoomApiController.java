package com.example.demo.discordbot.controller;

import com.example.demo.discordbot.dto.RoomRequest;
import com.example.demo.discordbot.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.discordbot.ROOM;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomApiController {
    private final RoomService roomService; // Service와 Repository는 기존 방식대로 생성

    @PostMapping
    public ResponseEntity<ROOM> createRoom(@RequestBody RoomRequest request) {
        ROOM savedRoom = roomService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    @GetMapping
    public ResponseEntity<List<ROOM>> findAllRooms() {
        return ResponseEntity.ok(roomService.findAll());
    }
}