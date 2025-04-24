package kr.co.wookoo.discord.service;

import kr.co.wookoo.discord.entity.Notification;
import kr.co.wookoo.discord.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {


    private final NotificationRepository notificationRepository;

    public void notify(Guild guild) {
        guild.getVoiceChannels().forEach(channel -> {
            StringBuilder sb = new StringBuilder();
            channel.getMembers().forEach(member -> {
                sb.append(member.getAsMention());
            });


            if (!sb.toString().isEmpty()) {
                Notification notification = notificationRepository.getRandom().orElse(
                        new Notification("밥똥탈 그만하고 스트레칭도 좀 하고 물도 좀 마셔라", "https://cdn.discordapp.com/attachments/1343911144431026187/1358747559627194399/2vM_UXCLnU8R-qzGDZuwOtnsl4Im-ljoeXCuP62X4doxngeYjHIKABvB1As8BEKLEYatuMwspQLIDTgXE8Busg.webp?ex=67f4f7c6&is=67f3a646&hm=d73f4978857307a025363a9564db726b4f11490645c60479763dca19243149fc&"
                        )
                );
                MessageEmbed embed = new EmbedBuilder().setImage(notification.getImage()).build();
                channel.sendMessage(sb.toString() + notification.getText()).queue();
                channel.sendMessageEmbeds(embed).queue();
            }
        });

    }


    public Notification upload(String text, List<Message.Attachment> attachmentList) {

        if (!attachmentList.isEmpty()) {
            for (Message.Attachment attachment : attachmentList) {
                if (attachment.isImage()) {
                    Notification notification = Notification.of(text, attachment);
                    notificationRepository.save(notification);
                    return notification;
                }
            }
        }

        return null;
    }

    public void sendWelcomeMessage(Guild guild, Member member) {
        String memberName = member.getAsMention();
        List<TextChannel> channelList = guild.getTextChannels();
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
