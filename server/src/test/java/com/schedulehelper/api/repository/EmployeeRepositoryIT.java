package com.schedulehelper.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.schedulehelper.api.entity.Employee;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link EmployeeRepository}.
 */
@Tag("repository")
@DataJpaTest
public class EmployeeRepositoryIT {
    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.save(new Employee("John", "Smith"));
        employeeRepository.save(new Employee("Jane", "Doe"));
        employeeRepository.save(new Employee("Alex"));
    }

    // --- findByPartialName ---

    static Stream<Arguments> matchingNameCases() {
        return Stream.of(
            Arguments.of("full name",             "John", "Smith", Arrays.asList("John")),
            Arguments.of("partial first",         "Jo",   "Smith", Arrays.asList("John")),
            Arguments.of("partial last",          "John", "Smi",   Arrays.asList("John")),
            Arguments.of("partial first and last","Jo",   "Sm",    Arrays.asList("John")),
            Arguments.of("uppercase first",       "JoHN", "Smith", Arrays.asList("John")),
            Arguments.of("uppercase last",        "John", "SmITH", Arrays.asList("John")),
            Arguments.of("empty first real last", "",     "Smith", Arrays.asList("John")),
            Arguments.of("empty last full first", "John", "",      Arrays.asList("John")),
            Arguments.of("empty last employee"  , "Alex", "",      Arrays.asList("Alex"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("matchingNameCases")
    void findByPartialName_returnsEmployee(
        final String description,
        final String first, final String last,
        final List<String> contains
    ) {
        assertThat(this.employeeRepository.findByPartialName(first, last))
            .extracting(Employee::getFirstName)
            .containsAll(contains);
    }

    static Stream<Arguments> noMatchNameCases() {
        return Stream.of(
            Arguments.of("full name",                       "xyz",  "xyz"  ),
            Arguments.of("invalid partial combination",     "Jo",   "D"    ),
            Arguments.of("invalid full combination",        "John", "Doe"  ),
            Arguments.of("failing real last partial first", "Jo",   "Doe"  ),
            Arguments.of("failing real first partial last", "Ja",   "Smith"),
            Arguments.of("failing fake last full first",    "John", "xyz"  ),
            Arguments.of("failing fake first full last",    "xyz",  "Smith"),
            Arguments.of("failing first empty last",        "xyz",  ""     )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("noMatchNameCases")
    void findByPartialName_noMatch_returnsEmpty(
        final String description,
        final String first, final String last
    ) {
        assertThat(this.employeeRepository.findByPartialName(first, last)).isEmpty();
    }
}
