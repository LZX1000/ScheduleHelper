package com.schedulehelper.api.schedulehelperapi.repository;

import com.schedulehelper.api.schedulehelperapi.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EmployeeRepository}.
 */
@Tag("repository")
@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.save(new Employee("John", "Smith"));
        employeeRepository.save(new Employee("Jane", "Doe"));
    }

    // --- findByName ---

    static Stream<Arguments> matchingNameCases() {
        return Stream.of(
            Arguments.of("full name",             "John",  "Smith"),
            Arguments.of("partial first",         "Jo",    "Smith"),
            Arguments.of("partial last",          "John",  "Smi"  ),
            Arguments.of("partial first and last","Jo",    "Sm"   ),
            Arguments.of("uppercase first",       "JoHN",  "Smith"),
            Arguments.of("uppercase last",        "John",  "SmITH"),
            Arguments.of("empty first",           "",      "Smith"),
            Arguments.of("empty last",            "John",  ""     )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchingNameCases")
    void findByName_returnsEmployee(
        final String description,
        final String first, final String last
    ) {
        assertThat(this.employeeRepository.findByName(first, last)).isPresent();
    }

    static Stream<Arguments> noMatchNameCases() {
        return Stream.of(
            Arguments.of("full name",                       "xyz",  "xyz"  ),
            Arguments.of("invalid partial combination",     "Jo",   "D"    ),
            Arguments.of("invalid full combination",        "John", "Doe"  ),
            Arguments.of("failing real last partial first", "Jo",   "Doe"  ),
            Arguments.of("failing real first partial last", "Ja",   "Smith"),
            Arguments.of("failing fake last full first",    "John", "xyz"  ),
            Arguments.of("failing fake first full last",    "xyz",  "Smith")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("noMatchNameCases")
    void findByName_noMatch_returnsEmpty(
        final String description,
        final String first, final String last
    ) {
        assertThat(this.employeeRepository.findByName(first, last)).isEmpty();
    }
}
