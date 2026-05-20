package com.schedulehelper.api.schedulehelperapi.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Shift}.
 */
public class ShiftTest {
    private final List<Shift> shifts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.shifts.add(new Shift(
            OffsetDateTime.of(2026, 5, 19, 9, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 19, 17, 0, 0, 0, ZoneOffset.ofHours(0))
        ));
    }

    // --- getId ---

    @Test
    void getId_returnsEmpty() {
        final Integer actual = shifts.getFirst().getId();
        assertThat(actual).isNull();
    }

    // --- getStartTime / getEndTime ---

    @Test
    void getStartTime_returnsStartTime() {
        final OffsetDateTime actual = shifts.getFirst().getStartTime();
        final OffsetDateTime expected = OffsetDateTime.of(
            2026, 5, 19, 9, 0, 0, 0, ZoneOffset.ofHours(0)
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getEndTime_returnsEndTime() {
        final OffsetDateTime actual = shifts.getFirst().getEndTime();
        final OffsetDateTime expected = OffsetDateTime.of(
            2026, 5, 19, 17, 0, 0, 0, ZoneOffset.ofHours(0)
        );

        assertThat(actual).isEqualTo(expected);
    }

    // --- setStartTime / setEndTime ---

    @Test
    void setStartTime_updatesStartTime() {
        final OffsetDateTime newStartTime = OffsetDateTime.of(
            2026, 5, 19, 12, 0, 0, 0, ZoneOffset.ofHours(0)
        );

        shifts.getFirst().setStartTime(newStartTime);
        final OffsetDateTime actual = shifts.getFirst().getStartTime();

        assertThat(actual).isEqualTo(newStartTime);
    }

    @Test
    void setEndTime_updatesEndTime() {
        final OffsetDateTime newEndTime = OffsetDateTime.of(
            2026, 5, 19, 12, 0, 0, 0, ZoneOffset.ofHours(0)
        );

        shifts.getFirst().setEndTime(newEndTime);
        final OffsetDateTime actual = shifts.getFirst().getEndTime();

        assertThat(actual).isEqualTo(newEndTime);
    }
}
