package kr.co.wookoo;

import kr.co.wookoo.http.HttpClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.Command;
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
                .enableIntents(GatewayIntent.GUILD_MESSAGES,GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MessageReceiver(),new AutoChat(),new CommandReceiver())
                .setActivity(Activity.playing("허리수술 2000만원"))
                .build();

        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(
                Commands.slash("알림발송", "분탕 알림을 발송합니다.")
                        .setContexts(InteractionContextType.GUILD)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );
        commands.addCommands(
                Commands.slash("상태변경","봇의 상태를 변경합니다.")
                        .addOption(OptionType.STRING,"상태","바꿀 상태명을 적어주세요.")
                        .setContexts(InteractionContextType.GUILD)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );
        commands.addCommands(
                Commands.slash("상인시간", "남은 상인시간을 표시합니다.")
                        .setContexts(InteractionContextType.GUILD)
        );

        commands.queue();




    }

}
