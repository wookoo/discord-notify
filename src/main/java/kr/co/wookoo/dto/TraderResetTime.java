package kr.co.wookoo.dto;

import java.time.Instant;

public class TraderResetTime {
    private String name;
    private Instant resetTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getResetTime() {
        return resetTime;
    }

    public void setResetTime(Instant resetTime) {
        this.resetTime = resetTime;
    }

    @Override
    public String toString() {
        return "TraderResetTime{" +
                "name='" + name + '\'' +
                ", resetTime=" + resetTime +
                '}';
    }
}