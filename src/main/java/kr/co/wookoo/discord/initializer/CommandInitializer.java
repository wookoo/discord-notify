package kr.co.wookoo.discord.initializer;

//import kr.co.wookoo.discord.events.AutoChat;

import kr.co.wookoo.discord.constant.BotConstant;
import kr.co.wookoo.discord.events.*;
import lombok.RequiredArgsConstructor;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandInitializer implements CommandLineRunner {

    private final JDA jda;


    @Override
    public void run(String... args) throws Exception {


        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                Commands.slash(BotConstant.CMD_NOTIFY, "분탕 알림을 발송합니다.")
                        .setContexts(InteractionContextType.GUILD)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("상태변경", "봇의 상태를 변경합니다.")
                        .addOption(OptionType.STRING, "상태", "바꿀 상태명을 적어주세요.")
                        .setContexts(InteractionContextType.GUILD)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("상인시간", "남은 상인시간을 표시합니다.")
                        .setContexts(InteractionContextType.GUILD),
                Commands.slash(BotConstant.CMD_PRICE, "아이템 플리 가격을 대충 표시합니다.")
                        .addOption(OptionType.STRING, "아이템-이름", "아이템 이름을 적어주세요.")
                        .setContexts(InteractionContextType.GUILD),
                Commands.slash("인게임시간", "인게임 시간을 표기합니다")
                        .setContexts(InteractionContextType.GUILD),
                Commands.slash("비트코인", "비트코인 가격을 표기합니다")
                        .setContexts(InteractionContextType.GUILD),
                Commands.slash(BotConstant.CMD_SPECTATE_OFF, "달려있는 관전을 떼버립니다")
                        .setContexts(InteractionContextType.GUILD),
                Commands.slash(BotConstant.CMD_SPECTATE_ON, "관전을 등록합니다")
                        .setContexts(InteractionContextType.GUILD),
                Commands.slash(BotConstant.CMD_AUTO_ON, "통화방 입장시 자동으로 관전이 등록됩니다")
                        .setContexts(InteractionContextType.GUILD),
                Commands.slash(BotConstant.CMD_AUTO_OFF, "통화방 입장시 자동 관전을 해제합니다")
                        .setContexts(InteractionContextType.GUILD),
                Commands.slash(BotConstant.CMD_MESSAGE,"분탕 알림에 보낼 내용을 추가합니다")
                        .addOption(OptionType.STRING,"내용","추가할 내용")
                        .setContexts(InteractionContextType.GUILD)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );
        commands.queue();
    }
}