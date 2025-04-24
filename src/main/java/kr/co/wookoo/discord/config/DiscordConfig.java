package kr.co.wookoo.discord.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.wookoo.discord.events.*;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class DiscordConfig {

    private final String DISCORD_BOT_TOKEN;
    private final CommandEvent commandEvent;
    private final VoiceChannelEvent voiceChannelEvent;
    private final ChatEvent chatEvent;
    private final GuildJoinEvent guildJoinEvent;
    private final ButtonEvent buttonEvent;

    public DiscordConfig(@Value("${token}") String token, CommandEvent commandEvent, VoiceChannelEvent voiceChannelEvent, ChatEvent chatEvent, GuildJoinEvent guildJoinEvent, ButtonEvent buttonEvent) {
        this.DISCORD_BOT_TOKEN = token;
        this.commandEvent = commandEvent;
        this.voiceChannelEvent = voiceChannelEvent;
        this.chatEvent = chatEvent;
        this.guildJoinEvent = guildJoinEvent;
        this.buttonEvent = buttonEvent;
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public OkHttpClient httpClient() {
        return new OkHttpClient(); // 커넥션 풀 공유 가능
    }

    @Bean
    public JDA jda() {
        return JDABuilder.createDefault(DISCORD_BOT_TOKEN)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(commandEvent, voiceChannelEvent, chatEvent, guildJoinEvent, buttonEvent)
                .setActivity(Activity.playing("허리수술 2000만원"))
                .build();

    }
}
