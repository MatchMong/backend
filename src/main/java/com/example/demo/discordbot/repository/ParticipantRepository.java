package com.example.demo.discordbot.repository;

import com.example.demo.discordbot.entity.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<RoomParticipant, Long> {
    List<RoomParticipant> findByRoom_RoomId(Long roomId);

    long countByRoom_RoomId(Long roomRoomId);

    boolean existsByRoom_RoomIdAndUserDiscordId(Long roomRoomId, String userDiscordId);
}
