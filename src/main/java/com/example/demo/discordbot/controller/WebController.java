package com.example.demo.discordbot.controller;

import com.example.demo.discordbot.DiscordBot;
import com.example.demo.discordbot.ROOM;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.web.bind.annotation.*;
import com.example.demo.discordbot.repository.RoomRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
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


    @PostMapping("/create-room")
    public String createRoom(@RequestParam String roomId, @RequestParam String ownerId) {
        ROOM room = new ROOM(roomId, ownerId);
        roomRepository.save(room); //
        return "방 생성 완료!";
    }
    @PostMapping("/send-message-to-owner")
    public String sendMessageToOwner(@RequestParam String roomId, @RequestParam String message) {
        ROOM room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return "해당 방을 찾을 수 없습니다.";
        }

        discordBot.sendDM(room.getOwnerId(), message);
        return "방장에게 메시지 전송 완료!";
    }





    @PostMapping("/send-dm")
    public String sendDM(@RequestParam String userId) {
        discordBot.sendDM(userId, "코드 왜 안됨??" +" ");
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
