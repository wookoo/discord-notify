package kr.co.wookoo.discord.events;

import kr.co.wookoo.discord.constant.BotConstant;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class ButtonEvent extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String id = event.getButton().getId();
        if(id.startsWith(BotConstant.ITEM_BUTTON_PREFIX)){
            long itemId = Long.parseLong(id.substring(BotConstant.ITEM_BUTTON_PREFIX.length()));
        }
    }
}
