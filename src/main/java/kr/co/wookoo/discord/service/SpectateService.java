package kr.co.wookoo.discord.service;

import kr.co.wookoo.discord.entity.Spectate;
import kr.co.wookoo.discord.repository.SpectateRepository;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpectateService {

    private final SpectateRepository spectateRepository;
    private final NickNameService nickNameService;

    public void autoSpectate(Member member) {
        long id = Long.parseLong(member.getId());
        spectateRepository.findByMemberId(id).ifPresent(spectate -> {
            if (spectate.getEnabled()) {
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
        Spectate spectate = spectateRepository.findByMemberId(member.getIdLong()).orElse(null);
        if (spectate == null) {
            spectate = Spectate.from(member, enable);
        }
        spectate.setEnabled(enable);
        spectateRepository.save(spectate);
    }


}
