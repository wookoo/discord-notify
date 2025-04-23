package kr.co.wookoo.discord.initializer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ItemInitializer  implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing Item");
    }
}
