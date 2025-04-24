package kr.co.wookoo.discord.scheduler;

import kr.co.wookoo.discord.service.NotificationService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiskScheduler {


    @Value("${TARGET_GUILD_ID}")
    private String TARGET_GUILD_ID;

    private final JDA jda;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(fixedRate = 3000)
    private void hourNotify() {
        Guild guild = jda.getGuildById(TARGET_GUILD_ID);
        if (guild != null) {
            notificationService.notify(guild);
        }
    }
}
