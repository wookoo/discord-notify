package kr.co.wookoo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.time.LocalDateTime;

public class TraderResetTime {
    private String name;
    private Instant resetTimestamp;

    public Instant getResetTimestamp() {
        return resetTimestamp;
    }

    public void setResetTimestamp(Instant resetTimestamp) {
        this.resetTimestamp = resetTimestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TraderResetTime{" +
                "name='" + name + '\'' +
                ", resetTimestamp=" + resetTimestamp +
                '}';
    }
}