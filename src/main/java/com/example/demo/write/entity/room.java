package com.example.demo.write.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table (name = "room")
public class room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(length = 45)
    private String title;

    @Column(length = 1000)
    private String content;

}
