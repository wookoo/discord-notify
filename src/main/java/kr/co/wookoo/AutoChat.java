package kr.co.wookoo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoChat extends ListenerAdapter {

    private static final String TARGET_GUILD_ID = "1358736723659456562";

    @Override
    public void onReady(ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(TARGET_GUILD_ID);
        if (guild == null) {
            System.out.println("채널이 없음");
            return;
        }
        EmbedBuilder embed = new EmbedBuilder()
                .setImage("https://cdn.discordapp.com/attachments/1343911144431026187/1358747559627194399/2vM_UXCLnU8R-qzGDZuwOtnsl4Im-ljoeXCuP62X4doxngeYjHIKABvB1As8BEKLEYatuMwspQLIDTgXE8Busg.webp?ex=67f4f7c6&is=67f3a646&hm=d73f4978857307a025363a9564db726b4f11490645c60479763dca19243149fc&");


        long delayUntilNextHour = Duration.between(
                LocalDateTime.now(),
                LocalDateTime.now().withMinute(0).withSecond(0).plusHours(1)
        ).getSeconds();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


        scheduler.scheduleAtFixedRate(
                () -> {
                    guild.getVoiceChannels().forEach(channel -> {

                        StringBuilder sb = new StringBuilder();
                        channel.getMembers().forEach(member -> {

                            sb.append(member.getAsMention());
                        });
                        if (!sb.toString().isEmpty()) { //멤버가 있으면
                            channel.sendMessage(sb.toString() + "밥똥탈 그만하고 스트레칭도 좀 하고 물도 좀 먹어라").queue();
                            channel.sendMessageEmbeds(embed.build()).queue();
                        }
                    });

                }
                , delayUntilNextHour, 3600, TimeUnit.SECONDS);

    }
}
