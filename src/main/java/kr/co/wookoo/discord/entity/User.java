package kr.co.wookoo.discord.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Builder(access = AccessLevel.PUBLIC)
    private User(Long memberId,Role role) {
        this.memberId = memberId;
        this.role = role;
    }

    public static User from(Member member) {
        return new User(member.getIdLong(),Role.DEFAULT);
    }
}
