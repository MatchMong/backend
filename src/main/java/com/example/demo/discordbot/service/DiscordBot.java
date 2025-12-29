package com.example.demo.discordbot.service;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DiscordBot extends ListenerAdapter {
    private JDA jda;
    private final List<Member> memberList = new ArrayList<>();

    @Value("${discord.bot.token}")
    private String bottoken;

    @PostConstruct
    public void startBot() throws LoginException {
        jda = JDABuilder.createDefault(bottoken,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(this)
                .build();

        System.out.println("Discord 봇 실행 중...");
    }



    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("봇이 준비되었습니다!");
        System.out.println("봇이 속한 서버 목록:");

        for (Guild g : event.getJDA().getGuilds()) {
            System.out.println("- " + g.getName() + " | ID: " + g.getId());
        }

        String GUILD_ID = "1414584088878387260";
        Guild guild = event.getJDA().getGuildById(GUILD_ID);

        if (guild != null) {
            guild.loadMembers().onSuccess(members -> {
                memberList.clear();
                memberList.addAll(members);
                System.out.println("서버 멤버 불러오기 완료 (" + members.size() + "명)");
                for (Member m : members) {
                    System.out.println(m.getUser().getName() + " | " + m.getId());

                }
            });
        } else {
            System.out.println("서버를 찾을 수 없습니다. GUILD_ID 확인하세요.");
        }
    }
    // DiscordBot.java
    public String getUserIdByName(String name) {
        // 서버(Guild) 멤버 목록에서 이름이 일치하는 사람을 찾음
        return jda.getGuilds().get(0).getMembers().stream()
                .filter(m -> m.getUser().getName().equals(name) || m.getEffectiveName().equals(name))
                .map(m -> m.getUser().getId()) // 숫자 ID 추출
                .findFirst()
                .orElse("0"); // 못 찾으면 0 반환 (이후 에러 처리 가능)
    }

    public void sendDM(String userId, String message) {
        if (userId == null || userId.isBlank()) {
            System.out.println("에러: 유저 ID가 비어있습니다.");
            return;
        }

        // retrieveUserById를 사용해야 캐시에 없어도 디스코드 서버에서 유저를 찾아옵니다.
        jda.retrieveUserById(userId).queue(user -> {
            // 유저를 찾은 경우
            user.openPrivateChannel().queue(channel -> {
                channel.sendMessage(message).queue(
                        success -> System.out.println(user.getName() + "님에게 DM 발송 성공!"),
                        error -> System.err.println("DM 발송 실패 (상대방이 DM을 차단했을 수 있음): " + error.getMessage())
                );
            });
        }, error -> {
            // 유저 ID 자체가 잘못되었거나 찾을 수 없는 경우
            System.err.println("디스코드에서 유저를 찾을 수 없습니다. ID 확인 필요: " + userId);
        });
    }

    public List<Member> getMemberList() {
        return memberList;
    }



    public void sendDMToAll(String message) {
        if (memberList.isEmpty()) {
            System.out.println("멤버 목록이 비어 있습니다. 봇이 아직 서버 멤버를 불러오지 못했을 수 있습니다.");
            return;
        }

        for (Member member : memberList) {
            User user = member.getUser();
            if (!user.isBot()) { // 봇 계정 제외
                user.openPrivateChannel().queue(channel -> {
                    channel.sendMessage(message).queue();
                    System.out.println(user.getName() + " 에게 DM 보냄");
                });
            }
        }

        System.out.println("모든 유저에게 DM 전송 완료!");
    }

    public String getNicknameByDiscordId(String discordId) {
        String cleanId = discordId.trim();

        // 1. 숫자인지 확인 (디스코드 ID는 숫자 형태의 Snowflake여야 함)
        if (!cleanId.matches("\\d+")) {
            // 숫자가 아니라면(사용자명이라면) memberList에서 이름으로 직접 찾기 시도
            return memberList.stream()
                    .filter(m -> m.getUser().getName().equals(cleanId) || m.getEffectiveName().equals(cleanId))
                    .findFirst()
                    .map(Member::getEffectiveName)
                    .orElse(cleanId); // 이름도 못 찾으면 그냥 들어온 값 그대로 반환
        }

        try {
            String GUILD_ID = "1414584088878387260";
            Guild guild = jda.getGuildById(GUILD_ID);

            if (guild != null) {
                Member member = guild.getMemberById(cleanId);
                if (member != null) {
                    return member.getEffectiveName();
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("잘못된 형식의 ID가 입력됨: " + cleanId);
        }

        // 최종적으로 못 찾으면 memberList에서 ID로 다시 확인
        return memberList.stream()
                .filter(m -> m.getId().equals(cleanId))
                .findFirst()
                .map(Member::getEffectiveName)
                .orElse(cleanId); // 마지막 보루: 입력받은 ID나 이름 그대로 반환
    }

}
