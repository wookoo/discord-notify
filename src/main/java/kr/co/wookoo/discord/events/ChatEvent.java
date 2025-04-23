package kr.co.wookoo.discord.events;

import kr.co.wookoo.discord.constant.BotConstant;
import kr.co.wookoo.discord.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatEvent extends ListenerAdapter {

    private final NotificationService notificationService;
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        Member member = event.getMember();
        if(member == null) return;
        if(! (member.hasPermission(Permission.ADMINISTRATOR) || member.getId().equals("241922585623789571") ) ){
            return;
        }

        String message = event.getMessage().getContentRaw();
        if(!message.startsWith(BotConstant.CMD_IMAGE_ADD)){
            return;
        }
        List<Message.Attachment> attachments = event.getMessage().getAttachments();

        int count = notificationService.uploadImage(attachments);
        event.getChannel().sendMessage("이미지 총 " +count+ "개 완료").queue();

    }
}
