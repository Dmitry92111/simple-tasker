package com.karfidov.simpletasker.backend.support;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

public final class TestTime {
    private TestTime() {
    }

    public static final Instant DEFAULT_INSTANT = Instant.parse("2026-04-01T12:00:00Z");
    public static final Clock DEFAULT_CLOCK = Clock.fixed(DEFAULT_INSTANT, ZoneOffset.UTC);

    public static Clock fixedClockAt(String instant) {
        return Clock.fixed(Instant.parse(instant), ZoneOffset.UTC);
    }
}
