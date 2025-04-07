package kr.co.wookoo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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

    private void initTraderMessage(Guild guild) throws Exception {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    GraphQLClient graphQLClient = new GraphQLClient();
                    List<TraderResetTime> traderResetTimes = graphQLClient.fetchTraderResetTimes();
                    TraderResetTime nextReset = traderResetTimes.stream()
                            .min(Comparator.comparing(TraderResetTime::getResetTimestamp))
                            .orElse(null);

                    if (nextReset != null) {
                        guild.getVoiceChannels().forEach(channel -> {
                            if (!channel.getMembers().isEmpty()) {
                                channel.sendMessage(nextReset.getName() + " 상인 초기화됨 구매 ㄱㄱ").queue();
                            }
                        });
                        Instant now = Instant.now();

                        System.out.println(now);
                        System.out.println(nextReset.getName());
                        System.out.println(nextReset.getResetTimestamp());


                        Duration delay = Duration.between(now, nextReset.getResetTimestamp());
                        long delayMillis = delay.toMillis();

                        scheduler.schedule(this, delayMillis, TimeUnit.MILLISECONDS); // 재귀 등록
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        scheduler.execute(task);

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
