package kr.co.wookoo.discord.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Spectate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private Long memberId;

    @Setter
    private Boolean enabled;

    private Spectate(Long memberId,boolean enabled) {
        this.memberId = memberId;
        this.enabled = enabled;
    }

    public static Spectate from(Member member, boolean enabled) {
        long memberId = member.getIdLong();
        return new Spectate(memberId,enabled);
    }


}
