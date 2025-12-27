package com.example.demo.discordbot.repository;

import com.example.demo.discordbot.entity.ROOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<ROOM, Long> {
    // JpaRepository<ENTITY, PK타입>
}