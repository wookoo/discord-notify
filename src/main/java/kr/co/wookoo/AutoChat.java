package kr.co.wookoo;

import kr.co.wookoo.dto.TraderResetTime;
import kr.co.wookoo.http.HttpClient;
import kr.co.wookoo.http.Client;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AutoChat extends ListenerAdapter {


    private final ScheduledThreadPoolExecutor scheduler;


    public AutoChat() {
        scheduler = new ScheduledThreadPoolExecutor(3);
    }

    @Override
    public void onReady(ReadyEvent event) {

        System.out.println("ON READY");
        String TARGET_GUILD_ID = System.getenv("TARGET_GUILD_ID");
        if (TARGET_GUILD_ID == null) {
            System.out.println("길드 아이디 없음");
            return;
        }
        Guild guild = event.getJDA().getGuildById(TARGET_GUILD_ID);
        if (guild == null) {
            System.out.println("채널이 없음");
            return;
        }

        initDiskMessage(guild);

        try {
            initTraderMessage(guild);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private void initTraderMessage(Guild guild) {
        Runnable scheduleNextReset = new Runnable() {
            @Override
            public void run() {
                try {
                    Client client = new HttpClient();
                    List<TraderResetTime> traderResetTimes = client.fetchTraderResetTimes();
                    Instant now = Instant.now();
                    TraderResetTime nextReset = traderResetTimes.stream()
                            .filter(t -> t.getResetTime().isAfter(now)) // 현재 시간 이후만 필터
                            .min(Comparator.comparing(TraderResetTime::getResetTime))
                            .orElse(null);

                    if (nextReset == null) return;

                    Instant resetTime = nextReset.getResetTime();
                    long delayMillis = Duration.between(now, resetTime).toMillis();

                    Runnable notifyAndReschedule = () -> {
                        guild.getVoiceChannels().forEach(channel -> {
                            if (!channel.getMembers().isEmpty()) {
                                channel.sendMessage(nextReset.getName() + " 상인 초기화됨 구매 ㄱㄱ").queue();
                            }
                        });
                        // 다음 리셋을 위한 재귀 예약
                        scheduler.execute(this);
                    };
                    scheduler.schedule(notifyAndReschedule, delayMillis, TimeUnit.MILLISECONDS);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        scheduler.execute(scheduleNextReset);
    }


    private void initDiskMessage(Guild guild) {


        EmbedBuilder embed = new EmbedBuilder()
                .setImage("https://cdn.discordapp.com/attachments/1343911144431026187/1358747559627194399/2vM_UXCLnU8R-qzGDZuwOtnsl4Im-ljoeXCuP62X4doxngeYjHIKABvB1As8BEKLEYatuMwspQLIDTgXE8Busg.webp?ex=67f4f7c6&is=67f3a646&hm=d73f4978857307a025363a9564db726b4f11490645c60479763dca19243149fc&");


        long delayUntilNextHour = Duration.between(
                LocalDateTime.now(),
                LocalDateTime.now().withMinute(0).withSecond(0).plusHours(1)
        ).getSeconds();

        Runnable notifyByDisk = () -> {
            guild.getVoiceChannels().forEach(channel -> {

                StringBuilder sb = new StringBuilder();
                channel.getMembers().forEach(member -> {

                    sb.append(member.getAsMention());
                });
                if (!sb.toString().isEmpty()) { //멤버가 있으면
                    channel.sendMessage(sb.toString() + "밥똥탈 그만하고 스트레칭도 좀 하고 물도 좀 마셔라").queue();
                    channel.sendMessageEmbeds(embed.build()).queue();
                }
            });
        };
        scheduler.scheduleAtFixedRate(notifyByDisk, delayUntilNextHour, 3600, TimeUnit.SECONDS);
    }
}
