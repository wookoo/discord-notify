package kr.co.wookoo.discord.events;

import kr.co.wookoo.discord.constant.BotConstant;
import kr.co.wookoo.discord.service.ItemService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ButtonEvent extends ListenerAdapter {

    private final ItemService itemService;
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String id = event.getButton().getId();
        if(id.startsWith(BotConstant.ITEM_BUTTON_PREFIX)){
            long itemId = Long.parseLong(id.substring(BotConstant.ITEM_BUTTON_PREFIX.length()));
            MessageEmbed messageEmbed = itemService.findById(itemId);
            if(messageEmbed == null){
                event.reply("오류가 발생했다 다시시도!").setEphemeral(true).queue();
                return;
            }
            event.replyEmbeds(messageEmbed).setEphemeral(true).queue();

        }
    }
}
