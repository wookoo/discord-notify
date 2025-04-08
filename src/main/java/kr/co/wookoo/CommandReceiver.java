package kr.co.wookoo;

import kr.co.wookoo.http.Client;
import kr.co.wookoo.http.HttpClient;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class CommandReceiver extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        if (event.getGuild() == null) {
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setImage("https://cdn.discordapp.com/attachments/1343911144431026187/1358747559627194399/2vM_UXCLnU8R-qzGDZuwOtnsl4Im-ljoeXCuP62X4doxngeYjHIKABvB1As8BEKLEYatuMwspQLIDTgXE8Busg.webp?ex=67f4f7c6&is=67f3a646&hm=d73f4978857307a025363a9564db726b4f11490645c60479763dca19243149fc&");


        JDA jda = event.getJDA();
        switch (event.getName()) {

            case "알림발송":
                Guild guild = event.getGuild();
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
                event.reply("메시지 발송 완료").queue();
                break;
            case "상태변경":
                String status = event.getOption("상태").getAsString();
                jda.getPresence().setActivity(Activity.of(Activity.ActivityType.COMPETING, status));
                event.reply("상태를 " + status + "로 업데이트 완료").queue();

            case "상인시간":
                Client client = new HttpClient();
                try {
                    List<TraderResetTime> traderResetTimes = client.fetchTraderResetTimes();
                    StringBuilder sb = new StringBuilder();
                    Instant now = Instant.now();
                    for (TraderResetTime traderResetTime : traderResetTimes) {
                        long minutesLeft = Duration.between(now, traderResetTime.getResetTime()).toMinutes();

                        sb.append(traderResetTime.getName())
                                .append(" : ");

                        if (minutesLeft > 0) {
                            sb.append(minutesLeft)
                                    .append("분 남음\n");
                        } else {
                            sb.append("방금 초기화됨\n");
                        }

                    }
                    event.reply(sb.toString()).queue();
                } catch (IOException e) {
                    event.reply("서버가 이상함").queue();
                }

        }
    }
}
