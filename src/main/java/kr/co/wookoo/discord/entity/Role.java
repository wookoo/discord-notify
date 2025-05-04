package kr.co.wookoo.discord.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    DEVELOPER(2),
    ADMIN(1),
    DEFAULT(0);
    private final int priority;
    public boolean hasRole(Role role) {
        return this.priority >= role.getPriority();
    }
}
