package com.example.demo.discordbot.controller;

import com.example.demo.discordbot.service.DiscordBot;
import com.example.demo.discordbot.entity.ROOM;
import com.example.demo.domain.entity.User;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.demo.discordbot.repository.RoomRepository;
import com.example.demo.discordbot.dto.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(
        origins = "http://localhost:3000",
        allowedHeaders = {"Content-Type", "ngrok-skip-browser-warning", "Authorization"}, // 명시적으로 추가
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)

@RestController
public class WebController {
    private final DiscordBot discordBot;
    private final RoomRepository roomRepository;

    public WebController(DiscordBot discordBot, RoomRepository roomRepository) {
        this.discordBot = discordBot;
        this.roomRepository = roomRepository;
    }

    @PostMapping("/send-message-to-owner")
    public String sendMessageToOwner(
            @AuthenticationPrincipal User user, // 1. 토큰에서 "누가 보냈는지" 정보를 바로 가져옴
            @RequestBody discordmessageRequest request
    ) {
        // 2. DB에서 방 정보를 가져옴
        ROOM room = roomRepository.findById(request.getRoomid())
                .orElseThrow(() -> new RuntimeException("방을 찾을 수 없습니다."));

        // 3. (선택사항) 보내는 사람의 정보를 메시지에 추가 (누가 보냈는지 방장이 알아야 하니까요)
        String senderInfo = "보낸 사람: " + user.getEmail() + " (" + user.getDiscordId() + ")\n";
        String fullMessage = senderInfo + "내용: " + request.getMessage();

        // 4. 방에 저장된 '진짜 방장 ID'로 메시지 전송
        discordBot.sendDM(room.getOwnerId(), fullMessage);

        return "방장(" + room.getOwnerId() + ")에게 메시지 전송 완료!";
    }





    @PostMapping("/send-dm")
    public String sendDM(@RequestBody Map<String,String> body) {
        String userId = body.get("userId");
        discordBot.sendDM(userId, "왜 안됨?"+" ");
        return "DM 전송 요청 완료!";
    }

    @GetMapping("/api/members")
    public List<Map<String, String>> getMembersme() {
        List<Member> members = discordBot.getMemberList();

        return members.stream()
                .map(m -> Map.of(
                        "username", m.getUser().getName(),           // 닉네임
                        "discriminator", m.getUser().getDiscriminator(), // 태그
                        "id", m.getId()                              // 고유 ID
                ))
                .collect(Collectors.toList());
    }

    // 🔍 서버 멤버 목록 확인용 (선택사항)
    @GetMapping("/members")
    public Object getMembers() {
        return discordBot.getMemberList()
                .stream()
                .map(m -> m.getUser().getName() + " (" + m.getId() + ")")
                .toList();
    }

    @GetMapping("/send-dm-all")
    public String sendDMToAll() {
        discordBot.sendDMToAll("📢순우 : 너나\n" +
                "공부해\n" +
                "하ㅏ하하하하하\n" +
                "하하하하ㅏ하ㅏ하하\n" +
                "ㅎ\n" +
                "ㅎ\n" +
                "ㅏ하핳\n" +
                "ㅏ;\n" +
                "나는야\n" +
                "뉴로우 회고왕\n" +
                "깔깔까라까라깔");
        return "전체 DM 전송 완료!";
    }
}
