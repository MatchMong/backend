package com.example.demo.repository;

import com.example.demo.ROOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<ROOM, String> {
    // JpaRepository<ENTITY, PK타입>
}