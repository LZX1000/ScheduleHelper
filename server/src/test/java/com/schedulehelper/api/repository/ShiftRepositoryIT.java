package com.schedulehelper.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.schedulehelper.api.entity.Shift;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ShiftRepository}.
 */
@Tag("repository")
@DataJpaTest
public class ShiftRepositoryIT {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ShiftRepository shiftRepository;

    @BeforeEach
    void setUp() {
        shiftRepository.deleteAll();
        jdbcTemplate.execute("ALTER TABLE shift ALTER COLUMN id RESTART WITH 1");

        shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 19, 9, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 19, 17, 0, 0, 0, ZoneOffset.ofHours(0))
        ));
        shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 20, 9, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 20, 17, 0, 0, 0, ZoneOffset.ofHours(0))
        ));
        shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 21, 12, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 21, 20, 0, 0, 0, ZoneOffset.ofHours(0))
        ));
        shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 22, 7, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 22, 15, 0, 0, 0, ZoneOffset.ofHours(0))
        ));
        shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 23, 9, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 23, 17, 0, 0, 0, ZoneOffset.ofHours(0))
        ));

        shiftRepository.save(new Shift(
            OffsetDateTime.of(2026, 5, 24, 9, 0, 0, 0, ZoneOffset.ofHours(0)),
            OffsetDateTime.of(2026, 5, 24, 17, 0, 0, 0, ZoneOffset.ofHours(0))
        ));
    }

    // --- findByStartTimeBetween ---

    static Stream<Arguments> matchStartTimeBetween() {
        return Stream.of(
            Arguments.of(
                "all month",
                OffsetDateTime.of(2026, 5, 1, 0, 0, 0, 0, ZoneOffset.ofHours(0)),
                OffsetDateTime.of(2026, 5, 31, 23, 59, 59, 0, ZoneOffset.ofHours(0)),
                Arrays.asList(1, 2, 3, 4, 5, 6)
            ),
            Arguments.of(
                "one week weekdays",
                OffsetDateTime.of(2026, 5, 18, 0, 0, 0, 0, ZoneOffset.ofHours(0)),
                OffsetDateTime.of(2026, 5, 22, 23, 59, 59, 0, ZoneOffset.ofHours(0)),
                Arrays.asList(1, 2, 3, 4)
            ),
            Arguments.of(
                "one week weekend",
                OffsetDateTime.of(2026, 5, 23, 0, 0, 0, 0, ZoneOffset.ofHours(0)),
                OffsetDateTime.of(2026, 5, 24, 23, 59, 59, 0, ZoneOffset.ofHours(0)),
                Arrays.asList(5, 6)
            )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchStartTimeBetween")
    void findByStartTimeBetween_returnsShift(
        final String description,
        final OffsetDateTime start, final OffsetDateTime end,
        final List<Integer> contains
    ) {
        assertThat(this.shiftRepository.findByStartTimeBetween(start, end))
            .extracting(Shift::getId)
            .containsAll(contains);
    }

    static Stream<Arguments> noMatchStartTimeBetween() {
        return Stream.of(
            Arguments.of(
                "next month",
                OffsetDateTime.of(2026, 6, 1, 0, 0, 0, 0, ZoneOffset.ofHours(0)),
                OffsetDateTime.of(2026, 6, 30, 23, 59, 59, 0, ZoneOffset.ofHours(0))
            ),
            Arguments.of(
                "previous week",
                OffsetDateTime.of(2026, 5, 10, 0, 0, 0, 0, ZoneOffset.ofHours(0)),
                OffsetDateTime.of(2026, 5, 16, 23, 59, 59, 0, ZoneOffset.ofHours(0))
            )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("noMatchStartTimeBetween")
    void findByStartTimeBetween_noMatch_returnsEmpty(
        final String description,
        final OffsetDateTime start, final OffsetDateTime end
    ) {
        assertThat(this.shiftRepository.findByStartTimeBetween(start, end)).isEmpty();
    }
}
