package kr.co.wookoo.dto;

import net.dv8tion.jda.api.entities.channel.Channel;

import java.time.Instant;

public class MemberTemp {

    private final String id;
    private final Instant createdAt;
    private final Channel channel;


    public MemberTemp(String id, Channel channel) {
        this.id = id;
        this.channel = channel;
        this.createdAt = Instant.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public Channel getChannel() {
        return channel;
    }
}
