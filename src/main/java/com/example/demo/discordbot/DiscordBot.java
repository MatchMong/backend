package com.example.demo.discordbot;

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

    public void sendDM(String userId, String message) {
        User user = jda.getUserById(userId);
        if (user != null) {
            user.openPrivateChannel().queue(ch -> ch.sendMessage(message).queue());
            System.out.println(user.getName() + " 에게 DM 보냄: " + message);
        } else {
            System.out.println("유저를 찾을 수 없습니다: " + userId);
        }
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

}
