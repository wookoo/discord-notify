package kr.co.wookoo;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import static java.lang.System.exit;


public class Main {
    public static void main(String[] args) {

        String token = System.getenv("TOKEN");
        if (token == null || token.isEmpty()) {
            System.out.println("토큰이 없습니다!");
            exit(1);
        }
        JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES,GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MessageReceiver())
                .setActivity(Activity.playing("허리수술 2000만원"))
                .build();
    }

}
