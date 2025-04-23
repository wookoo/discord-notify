package kr.co.wookoo.discord.service;

import kr.co.wookoo.discord.entity.NotificationImage;
import kr.co.wookoo.discord.repository.NotificationImageRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MessageEmbed embed = new EmbedBuilder()
            .setImage("https://cdn.discordapp.com/attachments/1343911144431026187/1358747559627194399/2vM_UXCLnU8R-qzGDZuwOtnsl4Im-ljoeXCuP62X4doxngeYjHIKABvB1As8BEKLEYatuMwspQLIDTgXE8Busg.webp?ex=67f4f7c6&is=67f3a646&hm=d73f4978857307a025363a9564db726b4f11490645c60479763dca19243149fc&")
            .build();

    private final NotificationImageRepository notificationImageRepository;

    public void notify(Guild guild) {
        guild.getVoiceChannels().forEach(channel -> {
            StringBuilder sb = new StringBuilder();
            channel.getMembers().forEach(member -> {
                sb.append(member.getAsMention());
            });

            //TODO : DB 연동 구현
            if (!sb.toString().isEmpty()) { //멤버가 있으면
                channel.sendMessage(sb.toString() + "밥똥탈 그만하고 스트레칭도 좀 하고 물도 좀 먹어라").queue();
                channel.sendMessageEmbeds(embed).queue();
            }
        });

    }


    public int uploadImage(List<Message.Attachment> attachmentList) {

        int result = 0;
        if (!attachmentList.isEmpty()) {
            for (Message.Attachment attachment : attachmentList) {
                if (attachment.isImage()) {
                    NotificationImage notificationImage = NotificationImage.from(attachment);
                    notificationImageRepository.save(notificationImage);
                    result++;
                }
            }
        }

        return result;
    }
}
