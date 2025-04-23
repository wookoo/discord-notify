package kr.co.wookoo.discord.events;

import kr.co.wookoo.discord.service.NotificationService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GuildJoinEvent extends ListenerAdapter {

    private final NotificationService notificationService;
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        notificationService.sendWelcomeMessage(event.getGuild(), event.getMember());
    }
}