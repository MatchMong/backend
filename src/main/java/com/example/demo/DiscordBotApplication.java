package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.security.auth.login.LoginException;

@SpringBootApplication
public class DiscordBotApplication {
    public static void main(String[] args) throws LoginException {
        // Spring Boot 실행
        ApplicationContext context = SpringApplication.run(DiscordBotApplication.class, args);

    }
}
