package kr.co.wookoo.discord.service;

import kr.co.wookoo.discord.entity.Spectate;
import kr.co.wookoo.discord.entity.User;
import kr.co.wookoo.discord.repository.SpectateRepository;
import kr.co.wookoo.discord.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpectateService {

    private final SpectateRepository spectateRepository;
    private final NickNameService nickNameService;
    private final UserRepository userRepository;

    public void autoSpectate(Member member) {
        long id = Long.parseLong(member.getId());

        userRepository.findByMemberId(id).ifPresent(user -> {

            log.info("User Login? ={}",user.getSpectate());
            if (user.getSpectate() != null && user.getSpectate().getEnabled()) {
                nickNameService.addPrefix(member);
            }
        });
    }

    public void activate(Member member) {
        create(member, true);
    }

    public void deActivate(Member member) {
        create(member, false);
    }

    private void create(Member member, boolean enable) {


        User user = userRepository.findByMemberId(member.getIdLong()).orElse(null);
        if (user == null) {
            user = User.from(member);
            userRepository.save(user);
        }

        Spectate spectate = spectateRepository.findByUser(user).orElse(null);
        if (spectate == null) {
            spectate = Spectate.from(user, enable);
        }
        spectate.setEnabled(enable);
        spectateRepository.save(spectate);
    }


}
