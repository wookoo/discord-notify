package kr.co.wookoo.discord.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Spectate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    private Boolean enabled;

    private Spectate(User user,boolean enabled) {
        this.user = user;
        this.enabled = enabled;
    }

    public static Spectate from(User user, boolean enabled) {
        return new Spectate(user,enabled);
    }

}
