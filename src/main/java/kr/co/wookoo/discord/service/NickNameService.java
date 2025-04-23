package kr.co.wookoo.discord.service;


import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Service;

@Service
public class NickNameService {
    private static final String PREFIX = "〔관전〕";
    public void addPrefix(Member member) {
        String nickName = member.getEffectiveName();
        if (nickName.startsWith(PREFIX)) {
            return;
        }
        member.modifyNickname(PREFIX + nickName).queue();
    }

    public void removePrefix(Member member) {
        String oldNickName = member.getEffectiveName();
        if (oldNickName.startsWith(PREFIX)) {
            String newNickName = oldNickName.substring(PREFIX.length());
            member.modifyNickname(newNickName).queue();
        }
    }
}
