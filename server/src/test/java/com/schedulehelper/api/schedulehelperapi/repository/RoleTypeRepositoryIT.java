package com.schedulehelper.api.schedulehelperapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.schedulehelper.api.schedulehelperapi.entity.RoleType;

/**
 * Integration tests for {@link RoleType}.
 */
@Tag("repository")
@DataJpaTest
public class RoleTypeRepositoryIT {
    @Autowired
    private RoleTypeRepository roleTypeRepository;

    @BeforeEach
    void setUp() {
        roleTypeRepository.save(new RoleType("PA")       );
        roleTypeRepository.save(new RoleType("Director") );
        roleTypeRepository.save(new RoleType("Camera #1"));
    }

    // --- findByPartialTitle ---

    static Stream<Arguments> matchingPartialTitleCases() {
        return Stream.of(
            Arguments.of("partial lower",      "p",        List.of("PA")                   ),
            Arguments.of("partial upper",      "P",        List.of("PA")                   ),
            Arguments.of("partial multiple",   "c",        List.of("Director", "Camera #1")),
            Arguments.of("multiple with case", "a",        List.of("PA", "Camera #1")      ),
            Arguments.of("full match",         "Director", List.of("Director")             )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchingPartialTitleCases")
    void findByPartialTitle_returnsRoleType(
        final String description,
        final String title,
        final List<String> contains
    ) {
        assertThat(this.roleTypeRepository.findByPartialTitle(title))
            .extracting(RoleType::getTitle)
            .containsAll(contains);
    }

    static Stream<Arguments> noMatchPartialTitleCases() {
        return Stream.of(
            Arguments.of("no match", "xyz", List.of())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("noMatchPartialTitleCases")
    void findByPartialTitle_noMatch_returnsEmpty(
        final String description,
        final String title,
        final List<String> contains
    ) {
        assertThat(this.roleTypeRepository.findByPartialTitle(title)).isEmpty();
    }
}
