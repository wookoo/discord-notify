package kr.co.wookoo.receiver;

import kr.co.wookoo.dto.MemberTemp;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class ChannelJoinReceiver extends ListenerAdapter {

    public static HashMap<String, MemberTemp> joinedMember = new HashMap<>();


    private final String TARGET_NICKNAME = "";

    private static final String PREFIX = "〔관전〕";


    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Member member = event.getMember();
        AudioChannel oldChannel = event.getOldValue();
        AudioChannel newChannel = event.getNewValue();

        if (oldChannel == null && newChannel != null) {
            onMemberJoinVoiceChannel(event);
            return;
        }
        if (oldChannel != null && newChannel == null) {
            onMemberLeaveVoiceChannel(event);
            return;
        }
        onMemberMoveVoiceChannel(event);
    }

    private void onMemberMoveVoiceChannel(GuildVoiceUpdateEvent event) {

    }

    private void onMemberJoinVoiceChannel(GuildVoiceUpdateEvent event) {

//        if (event.getMember().getId().equals(TARGET_NICKNAME)) {
//
////            addMemberPrefix(event.getMember());
//        }
    }

    private void onMemberLeaveVoiceChannel(GuildVoiceUpdateEvent event) {
        removeMemberPrefix(event.getMember());

    }

    public static void addMemberPrefix(Member member) {

        String nickName = member.getEffectiveName();
        System.out.println(nickName);
        if (nickName.startsWith(PREFIX)) {
            return;
        }
        member.modifyNickname(PREFIX + nickName).queue();

    }

    public static void removeMemberPrefix(Member member) {

        String oldNickName = member.getEffectiveName();
        if (oldNickName.startsWith(PREFIX)) {
            String newNickName = oldNickName.substring(PREFIX.length());
            member.modifyNickname(newNickName).queue();
        }
    }
}
