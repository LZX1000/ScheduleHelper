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

    // --- findByTitle ---

    static Stream<Arguments> matchTitleCases() {
        return Stream.of(
            Arguments.of("match case",       "Director",  List.of("Director") ),
            Arguments.of("includes special", "Camera #1", List.of("Camera #1")),
            Arguments.of("upper",            "PA",        List.of("PA")       )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchTitleCases")
    void findByTitle_returnsRoleType(
        final String description,
        final String title,
        final List<String> contains
    ) {
        assertThat(this.roleTypeRepository.findByPartialTitle(title))
            .extracting(RoleType::getTitle)
            .containsExactlyInAnyOrderElementsOf(contains);
    }

    static Stream<Arguments> noMatchTitleCases() {
        return Stream.of(
            Arguments.of("invalid entry", "xyz"     ),
            Arguments.of("lowered",       "pa"      ),
            Arguments.of("mixed case",    "dirECtoR")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("noMatchTitleCases")
    void findByTitle_noMatch_returnsEmpty(
        final String description,
        final String title
    ) {
        assertThat(this.roleTypeRepository.findByTitle(title)).isEmpty();
    }

    // --- findByPartialTitle ---

    static Stream<Arguments> matchPartialTitleCases() {
        return Stream.of(
            Arguments.of("partial lower",      "p",        List.of("PA")                   ),
            Arguments.of("partial upper",      "P",        List.of("PA")                   ),
            Arguments.of("partial multiple",   "c",        List.of("Director", "Camera #1")),
            Arguments.of("multiple with case", "a",        List.of("PA", "Camera #1")      ),
            Arguments.of("full match",         "Director", List.of("Director")             )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchPartialTitleCases")
    void findByPartialTitle_returnsRoleType(
        final String description,
        final String title,
        final List<String> contains
    ) {
        assertThat(this.roleTypeRepository.findByPartialTitle(title))
            .extracting(RoleType::getTitle)
            .containsExactlyInAnyOrderElementsOf(contains);
    }

    static Stream<Arguments> noMatchPartialTitleCases() {
        return Stream.of(
            Arguments.of("no match", "xyz")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("noMatchPartialTitleCases")
    void findByPartialTitle_noMatch_returnsEmpty(
        final String description,
        final String title
    ) {
        assertThat(this.roleTypeRepository.findByPartialTitle(title)).isEmpty();
    }
}
