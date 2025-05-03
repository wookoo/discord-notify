package kr.co.wookoo.discord.events;

import kr.co.wookoo.discord.entity.Spectate;
import kr.co.wookoo.discord.service.NickNameService;
import kr.co.wookoo.discord.service.NotificationService;
import kr.co.wookoo.discord.service.SpectateService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class VoiceChannelEvent extends ListenerAdapter {
    private final NickNameService nickNameService;
    private final SpectateService spectateService;
    private final NotificationService notificationService;

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
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


        AudioChannel oldChannel = event.getOldValue();
        AudioChannel newChannel = event.getNewValue();

        Member member = event.getMember();




        notificationService.sendVoiceChannelJoinMessage(member,newChannel);
        notificationService.sendVoiceChannelLeaveMessage(member,oldChannel);


    }

    private void onMemberJoinVoiceChannel(GuildVoiceUpdateEvent event) {
        Member member = event.getMember();
        if(member.getUser().isBot()){
            return;
        }
        spectateService.autoSpectate(member);
        notificationService.sendVoiceChannelJoinMessage(member,event.getNewValue());
    }

    private void onMemberLeaveVoiceChannel(GuildVoiceUpdateEvent event) {
        if(event.getMember().getUser().isBot()){
            return;
        }
        nickNameService.removePrefix(event.getMember());
        notificationService.sendVoiceChannelLeaveMessage(event.getMember(),event.getOldValue());
    }
}
