package kr.co.wookoo;

import kr.co.wookoo.receiver.ChannelJoinReceiver;
import kr.co.wookoo.receiver.CommandReceiver;
import kr.co.wookoo.receiver.GuildJoinReceiver;
import kr.co.wookoo.receiver.MessageReceiver;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import static java.lang.System.exit;


public class Main {
    public static void main(String[] args) throws Exception {

        String token = System.getenv("TOKEN");
        if (token == null || token.isEmpty()) {
            System.out.println("토큰이 없습니다!");
            exit(1);
        }


        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new MessageReceiver(), new AutoChat(), new CommandReceiver(), new ChannelJoinReceiver(),
                        new GuildJoinReceiver())
                .setActivity(Activity.playing("허리수술 2000만원"))
                .build();

        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                Commands.slash("알림발송", "분탕 알림을 발송합니다.")
                        .setContexts(InteractionContextType.GUILD)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );
        commands.addCommands(
                Commands.slash("상태변경", "봇의 상태를 변경합니다.")
                        .addOption(OptionType.STRING, "상태", "바꿀 상태명을 적어주세요.")
                        .setContexts(InteractionContextType.GUILD)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );
        commands.addCommands(
                Commands.slash("상인시간", "남은 상인시간을 표시합니다.")
                        .setContexts(InteractionContextType.GUILD)
        );
        commands.addCommands(
                Commands.slash("플리가격", "아이템 플리 가격을 대충 표시합니다.")
                        .addOption(OptionType.STRING, "아이템-이름", "아이템 이름을 적어주세요.")
                        .setContexts(InteractionContextType.GUILD)

        );
        commands.addCommands(
                Commands.slash("인게임시간", "인게임 시간을 표기합니다")
                        .setContexts(InteractionContextType.GUILD)
        );
        commands.addCommands(
                Commands.slash("비트코인", "비트코인 가격을 표기합니다")
                        .setContexts(InteractionContextType.GUILD)
        );

        commands.addCommands(
                Commands.slash("관전취소", "달려있는 관전을 떼버립니다")
                        .setContexts(InteractionContextType.GUILD)
        );
        commands.addCommands(
                Commands.slash("관전등록", "관전을 등록합니다")
                        .setContexts(InteractionContextType.GUILD)
        );
//        commands.addCommands(
//                Commands.slash("자동관전", "채팅방 입장시 자동으로 관전을 달아버립니다.")
//                        .setContexts(InteractionContextType.GUILD)
//        );

        commands.queue();


    }

}
