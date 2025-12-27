package com.example.demo.discordbot;

import com.example.demo.discordbot.entity.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<RoomParticipant, Long> {
    List<RoomParticipant> findByRoom_RoomId(Long roomId);
}
