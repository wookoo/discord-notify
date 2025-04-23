package kr.co.wookoo.receiver;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class GuildJoinReceiver extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        Member member = event.getMember();
        String memberName = member.getAsMention();
        List<TextChannel> channelList = event.getGuild().getTextChannels();
        TextChannel channel = channelList.get(0);
        for (TextChannel c : channelList) {
            if (c.getName().contains("자유")) {
                channel = c;
                break;
            }
        }

    channel.sendMessage(memberName + "공지 스근하게 읽고 닉네임 바꿔주셔~").queue();


    }
}
