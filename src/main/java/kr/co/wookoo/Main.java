package kr.co.wookoo;

import kr.co.wookoo.http.HttpClient;

import static java.lang.System.exit;


public class Main {
    public static void main(String[] args) throws Exception {

        String token = System.getenv("TOKEN");
        if (token == null || token.isEmpty()) {
            System.out.println("토큰이 없습니다!");
            exit(1);
        }
//        JDABuilder.createDefault(token)
//                .enableIntents(GatewayIntent.GUILD_MESSAGES,GatewayIntent.MESSAGE_CONTENT)
//                .addEventListeners(new MessageReceiver(),new AutoChat())
//                .setActivity(Activity.playing("허리수술 2000만원"))
//                .build();

//        GraphQLClient graphQLClient = new GraphQLClient();
//
//        System.out.println(graphQLClient.fetchTraderResetTimes());

        HttpClient httpHttpClient = new HttpClient();

        System.out.println(httpHttpClient.fetchTraderResetTimes());
    }

}
