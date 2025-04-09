package kr.co.wookoo.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.wookoo.dto.ItemInfo;
import kr.co.wookoo.dto.ItemPrice;
import kr.co.wookoo.dto.TraderResetTime;
import kr.co.wookoo.http.Client;
import kr.co.wookoo.http.GraphQLClient;
import kr.co.wookoo.http.HttpClient;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandReceiver extends ListenerAdapter {


    private List<ItemInfo> itemInfoList = null;

    private final Map<String, ItemInfo> itemInfoMap = new HashMap<>();

    public CommandReceiver() {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("item.json")) {
            if (in == null) {
                return;
            }
            JsonNode items = mapper.readTree(in).get("data").get("items");
            this.itemInfoList = mapper.readerForListOf(ItemInfo.class).readValue(items.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (ItemInfo itemInfo : this.itemInfoList) {
            itemInfoMap.put(itemInfo.getId(), itemInfo);
        }
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        if (event.getGuild() == null) {
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setImage("https://cdn.discordapp.com/attachments/1343911144431026187/1358747559627194399/2vM_UXCLnU8R-qzGDZuwOtnsl4Im-ljoeXCuP62X4doxngeYjHIKABvB1As8BEKLEYatuMwspQLIDTgXE8Busg.webp?ex=67f4f7c6&is=67f3a646&hm=d73f4978857307a025363a9564db726b4f11490645c60479763dca19243149fc&");


        JDA jda = event.getJDA();
        switch (event.getName()) {

            case "알림발송":
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
                event.reply("메시지 발송 완료").setEphemeral(true).queue();
                break;
            case "상태변경":
                String status = event.getOption("상태").getAsString();
                jda.getPresence().setActivity(Activity.of(Activity.ActivityType.COMPETING, status));
                event.reply("상태를 " + status + "로 업데이트 완료").setEphemeral(true).queue();

            case "상인시간":
                Client client = new HttpClient();
                try {
                    List<TraderResetTime> traderResetTimes = client.fetchTraderResetTimes();
                    StringBuilder sb = new StringBuilder();
                    Instant now = Instant.now();
                    for (TraderResetTime traderResetTime : traderResetTimes) {
                        long minutesLeft = Duration.between(now, traderResetTime.getResetTime()).toMinutes();

                        sb.append(traderResetTime.getName())
                                .append(" : ");

                        if (minutesLeft > 0) {
                            sb.append(minutesLeft)
                                    .append("분 남음\n");
                        } else {
                            sb.append("방금 초기화됨\n");
                        }

                    }
                    event.reply(sb.toString()).setEphemeral(true).queue();
                } catch (IOException e) {
                    event.reply("서버가 이상함").setEphemeral(true).queue();
                }
                break;
            case "플리가격":
                itemSearchAction(event);
            default:
                event.reply("명령어가 없는데??").queue();
        }
    }

    private void itemSearchAction(SlashCommandInteractionEvent event) {

        String name = event.getOption("아이템-이름").getAsString().toLowerCase();
        if (itemInfoList == null) {
            event.reply("뭔가 오류가 발생함").queue();
            return;
        }

        List<ItemInfo> choicedList = new ArrayList<>();
        for (ItemInfo itemInfo : itemInfoList) {
            if (itemInfo.getName().toLowerCase().contains(name)) {
                choicedList.add(itemInfo);
            }
        }
        if (choicedList.isEmpty()) {
            event.reply("일치하는 아이템이 한개도 없다! 한국어 이름으로 ㄱㄱ").queue();
            return;
        }

        int limit = Math.min(choicedList.size(), 5);
        List<Button> buttons = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            ItemInfo itemInfo = choicedList.get(i);
            buttons.add(
                    Button.secondary("item:" + itemInfo.getId(), itemInfo.getName())
            );
        }
        event.reply("어떤 아이템인가?").addActionRow(buttons).setEphemeral(true).queue();
    }


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String id = event.getButton().getId().replace("item:", "");
        GraphQLClient graphQLClient = new GraphQLClient();

        ItemInfo itemInfo = itemInfoMap.get(id);
        try {
            ItemPrice itemPrice = graphQLClient.fetchItemFleaMarketPrice(id);
            Instant instant = Instant.ofEpochMilli(itemPrice.getTimestamp());
            Instant now = Instant.now();
            Duration duration = Duration.between(instant,now);
            long minutes = duration.toMinutes();
            ZonedDateTime kstTime = instant.atZone(ZoneId.of("Asia/Seoul"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String formattedTime = kstTime.format(formatter);
            EmbedBuilder embed = new EmbedBuilder()
                    .setImage(itemInfo.getImageLink())
                    .setTitle(itemInfo.getName())
                            .setDescription("가격 : " +itemPrice.getPrice()+"\n업데이트 시각 : " + formattedTime + " (" + minutes +"분 전 업데이트 )");
            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
        } catch (IOException e) {
            e.printStackTrace();
            event.reply("탈콥 데브서버 이상함").setEphemeral(true).queue();
        }
        event.getMessage().delete().queue();
    }
}
