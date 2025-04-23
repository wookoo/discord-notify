package kr.co.wookoo.discord.events;


import kr.co.wookoo.discord.constant.BotConstant;
import kr.co.wookoo.discord.dto.ItemDto;
import kr.co.wookoo.discord.service.ItemService;
import kr.co.wookoo.discord.service.NickNameService;
import kr.co.wookoo.discord.service.NotificationService;
import kr.co.wookoo.discord.service.SpectateService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static ch.qos.logback.core.joran.spi.ConsoleTarget.findByName;

@Component
@RequiredArgsConstructor
public class CommandEvent extends ListenerAdapter {


    private final NickNameService nickNameService;
    private final NotificationService notificationService;
    private final SpectateService spectateService;
    private final ItemService itemService;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getMember() == null) {
            return;
        }
        if (event.getUser().isBot()) {
            return;
        }
        if (event.getGuild() == null) {
            return;
        }


        switch (event.getName()) {
            case BotConstant.CMD_SPECTATE_ON -> {
                nickNameService.addPrefix(event.getMember());
                event.reply("관전 등록~").setEphemeral(true).queue();
            }
            case BotConstant.CMD_SPECTATE_OFF -> {
                nickNameService.removePrefix(event.getMember());
                event.reply("관전 취소~").setEphemeral(true).queue();
            }
            case BotConstant.CMD_NOTIFY -> {
                notificationService.notify(event.getGuild());
                event.reply("발송 완료").setEphemeral(true).queue();
            }
            case BotConstant.CMD_AUTO_ON -> {
                spectateService.activate(event.getMember());
                event.reply("등록 완료~").setEphemeral(true).queue();
            }
            case BotConstant.CMD_AUTO_OFF -> {
                spectateService.deActivate(event.getMember());
                event.reply("등록 취소~").setEphemeral(true).queue();
            }
            case BotConstant.CMD_PRICE -> {
                OptionMapping option = event.getOption("아이템-이름");
                if (option == null) {
                    event.reply("아이템 이름이 없는거 같은데~").setEphemeral(true).queue();
                    return;
                }
                String name = option.getAsString();
                List<ItemDto> itemDtoList = itemService.findByName(name);
                if (itemDtoList.isEmpty()) {
                    event.reply("음 아이템이 플리 벤이거나 없는걸 검색한거같은데").setEphemeral(true).queue();
                    return;
                }
                List<ActionRow> rows = new ArrayList<>();
                List<Button> buffer = new ArrayList<>();
                for (int i = 0; i < itemDtoList.size(); i++) {
                    ItemDto dto = itemDtoList.get(i);
                    Button btn = Button.secondary(BotConstant.ITEM_BUTTON_PREFIX + dto.getId(), dto.getKorean());
                    buffer.add(btn);
                    if (buffer.size() == 5 || i == itemDtoList.size() - 1) {
                        rows.add(ActionRow.of(buffer));
                        buffer = new ArrayList<>();
                    }
                    if (rows.size() == 5) break;
                }
                event.reply("자네가 고른 아이템은 뭔가?")
                        .addComponents(rows)
                        .setEphemeral(true)
                        .queue();
            }
            default -> {
                event.reply("코드 갈아 엎어서 아직 구현이 안됬음")
                        .setEphemeral(true)
                        .queue();
            }

        }


//
//        JDA jda = event.getJDA();
//        switch (event.getName()) {
//
//            case "알림발송":
//                Guild guild = event.getGuild();
//                guild.getVoiceChannels().forEach(channel -> {
//
//                    StringBuilder sb = new StringBuilder();
//                    channel.getMembers().forEach(member -> {
//
//                        sb.append(member.getAsMention());
//                    });
//                    if (!sb.toString().isEmpty()) { //멤버가 있으면
//                        channel.sendMessage(sb.toString() + "밥똥탈 그만하고 스트레칭도 좀 하고 물도 좀 먹어라").queue();
//                        channel.sendMessageEmbeds(embed.build()).queue();
//                    }
//                });
//                event.reply("메시지 발송 완료").setEphemeral(true).queue();
//                break;
//            case "상태변경":
//                String status = event.getOption("상태").getAsString();
//                jda.getPresence().setActivity(Activity.of(Activity.ActivityType.COMPETING, status));
//                event.reply("상태를 " + status + "로 업데이트 완료").setEphemeral(true).queue();
//
//            case "상인시간":
//                Client client = new HttpClient();
//                try {
//                    List<TraderResetTime> traderResetTimes = client.fetchTraderResetTimes();
//                    StringBuilder sb = new StringBuilder();
//                    Instant now = Instant.now();
//                    for (TraderResetTime traderResetTime : traderResetTimes) {
//                        long minutesLeft = Duration.between(now, traderResetTime.getResetTime()).toMinutes();
//
//                        sb.append(traderResetTime.getName())
//                                .append(" : ");
//
//                        if (minutesLeft > 0) {
//                            sb.append(minutesLeft)
//                                    .append("분 남음\n");
//                        } else {
//                            sb.append("방금 초기화됨\n");
//                        }
//
//                    }
//                    event.reply(sb.toString()).setEphemeral(true).queue();
//                } catch (IOException e) {
//                    event.reply("서버가 이상함").setEphemeral(true).queue();
//                }
//                break;
//            case "플리가격":
//                itemSearchAction(event);
//
//                break;
//
//            case "관전취소":
//                ChannelJoinReceiver.removeMemberPrefix(event.getMember());
//                event.reply("관전 취소~").setEphemeral(true).queue();
//                break;
//            case "관전등록":
////                System.out.println(event.getMember());
//                ChannelJoinReceiver.addMemberPrefix(event.getMember());
//                event.reply("관전 등록~").setEphemeral(true).queue();
//            case "비트코인":
//                GraphQLClient graphQLClient = new GraphQLClient();
//                try {
//                    graphQLClient.getBitcoinPrice();
//                    event.reply("현재 가격 : " + graphQLClient.getBitcoinPrice()).setEphemeral(true).queue();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    event.reply("뭔가 오류 나중에 다시 시도").setEphemeral(true).queue();
//                }
//                break;
//
//            case "인게임시간":
//                Instant now = Instant.now();
//                LocalTime leftTime = TimeConvertor.realTimeToTarkovTime(now, true);
//                LocalTime rightTime = TimeConvertor.realTimeToTarkovTime(now, false);
//                event.reply("좌측 : " + leftTime.getHour() + "시" + leftTime.getMinute() + "분\n우측 : " + rightTime.getHour() + "시" + rightTime.getMinute() + "분").setEphemeral(true).queue();
//                break;
//            default:
//                event.reply("명령어가 없는데??").queue();
//        }
//    }

//    private void itemSearchAction(SlashCommandInteractionEvent event) {
//
//        String name = event.getOption("아이템-이름").getAsString().toLowerCase();
//        if (itemInfoList == null) {
//            event.reply("뭔가 오류가 발생함").queue();
//            return;
//        }
//
//        List<ItemInfo> choicedList = new ArrayList<>();
//        for (ItemInfo itemInfo : itemInfoList) {
//            if (itemInfo.getName().toLowerCase().contains(name)) {
//                choicedList.add(itemInfo);
//            }
//        }
//        if (choicedList.isEmpty()) {
//            event.reply("일치하는 아이템이 한개도 없다! 한국어 이름으로 ㄱㄱ").setEphemeral(true).queue();
//            return;
//        }
//
//        int limit = Math.min(choicedList.size(), 5);
//        List<Button> buttons = new ArrayList<>();
//        for (int i = 0; i < limit; i++) {
//            ItemInfo itemInfo = choicedList.get(i);
//            buttons.add(
//                    Button.secondary("item:" + itemInfo.getId(), itemInfo.getName())
//            );
//        }
//        event.reply("어떤 아이템인가?").addActionRow(buttons).setEphemeral(true).queue();
//    }
//
//
//    @Override
//    public void onButtonInteraction(ButtonInteractionEvent event) {
//        String id = event.getButton().getId().replace("item:", "");
//        GraphQLClient graphQLClient = new GraphQLClient();
//
//        ItemInfo itemInfo = itemInfoMap.get(id);
//        try {
//            List<ItemPrice> itemPriceList = graphQLClient.fetchItemFleaMarketPriceList(id);
//            if (itemPriceList.isEmpty()) {
//                event.reply(itemInfo.getName() + "은 플리 벤임!").setEphemeral(true).queue();
//                return;
//            }
//            int sum = itemPriceList.stream()
//                    .mapToInt(ItemPrice::getPrice)
//                    .sum();
//            int avg = (int) ((double) sum / itemPriceList.size());
//            Instant instant = Instant.ofEpochMilli(itemPriceList.get(0).getTimestamp());
//            ZonedDateTime kstTime = instant.atZone(ZoneId.of("Asia/Seoul"));
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            String formattedTime = kstTime.format(formatter);
//            EmbedBuilder embed = new EmbedBuilder()
//                    .setImage(itemInfo.getImageLink())
//                    .setTitle(itemInfo.getName())
//                    .setDescription("1주간 평균 가격 : " + avg + "\n(" + formattedTime + " ~ " + Instant.ofEpochMilli(itemPriceList.get(itemPriceList.size() - 1).getTimestamp()).atZone(ZoneId.of("Asia/Seoul")).format(formatter) + ")");
//            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
//        } catch (IOException e) {
//            e.printStackTrace();
//            event.reply("탈콥 데브서버 이상함").setEphemeral(true).queue();
//        }
//        event.getMessage().delete().queue();
//    }
    }
}