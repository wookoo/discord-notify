package kr.co.wookoo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceiver extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }


        Member sender = event.getMember(); // 채팅 친 유저의 Member 객체

        EmbedBuilder embed = new EmbedBuilder()
                .setImage("https://cdn.discordapp.com/attachments/1343911144431026187/1358747559627194399/2vM_UXCLnU8R-qzGDZuwOtnsl4Im-ljoeXCuP62X4doxngeYjHIKABvB1As8BEKLEYatuMwspQLIDTgXE8Busg.webp?ex=67f4f7c6&is=67f3a646&hm=d73f4978857307a025363a9564db726b4f11490645c60479763dca19243149fc&");

        if (sender != null && sender.hasPermission(Permission.ADMINISTRATOR)) {
            String message = event.getMessage().getContentRaw();
            if (!message.strip().equals("알림 전송")) {
                return;
            }
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


        }
    }
}
