package kr.co.wookoo.discord.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "disord_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long memberId;

    @OneToOne(mappedBy = "user")
    private Spectate spectate;
    private User(Long memberId) {
        this.memberId = memberId;
    }

    public static User from(Member member) {
        return new User(member.getIdLong());
    }
}
