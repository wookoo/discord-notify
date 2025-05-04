package kr.co.wookoo.discord.initializer;

//import kr.co.wookoo.discord.events.AutoChat;

import kr.co.wookoo.discord.constant.BotConstant;
import kr.co.wookoo.discord.entity.Role;
import kr.co.wookoo.discord.entity.User;
import kr.co.wookoo.discord.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {
        Long memberId = 241922585623789571L;

        userRepository.findByMemberId(memberId)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .memberId(memberId)
                                .role(Role.DEVELOPER)
                                .build()
                ));
    }
}