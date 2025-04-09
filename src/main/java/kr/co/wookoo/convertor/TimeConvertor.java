package kr.co.wookoo.convertor;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class TimeConvertor {

    private static final int TARKOV_RATIO = 7;
    private static final long ONE_DAY_MILLIS = 1000L * 60 * 60 * 24;
    private static final long RUSSIA_OFFSET_MILLIS = 1000L * 60 * 60 * 3; // GMT+3

    public static LocalTime realTimeToTarkovTime(Instant realTime, boolean left) {
        long utcMillis = realTime.toEpochMilli();
        long offset = RUSSIA_OFFSET_MILLIS + (left ? 0 : ONE_DAY_MILLIS / 2);

        long tarkovMillis = (offset + (utcMillis * TARKOV_RATIO)) % ONE_DAY_MILLIS;

        return Instant.ofEpochMilli(tarkovMillis)
                .atZone(ZoneOffset.UTC)
                .toLocalTime();
    }
}

