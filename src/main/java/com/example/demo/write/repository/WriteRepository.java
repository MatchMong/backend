package com.example.demo.write.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.write.entity.room;

@Repository
public interface WriteRepository extends JpaRepository<room, Long> {

}
