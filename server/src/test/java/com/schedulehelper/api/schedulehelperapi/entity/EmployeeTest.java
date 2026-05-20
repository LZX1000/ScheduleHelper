package com.schedulehelper.api.schedulehelperapi.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Employee}.
 */
public class EmployeeTest {
    private final List<Employee> employees = new ArrayList<>();

    private Employee firstNameOnly;

    @BeforeEach
    void setUp() {
        this.firstNameOnly = new Employee("Alex");

        this.employees.add(new Employee("John", "Smith"));
        this.employees.add(new Employee("John", "Smith"));
        this.employees.add(new Employee("Jane", "Doe"));
        this.employees.add(this.firstNameOnly);
    }

    // --- getId ---

    @Test
    void getId_returnsEmpty() {
        final Integer actual = employees.getFirst().getId();
        assertThat(actual).isNull();
    }

    // --- getFirstName / getLastName ---

    @Test
    void getFirstName_returnsFirstName() {
        final String actual = employees.getFirst().getFirstName();
        final String expected = "John";

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getLastName_returnsLastName() {
        final String actual = employees.getFirst().getLastName();
        final String expected = "Smith";

        assertThat(actual).isEqualTo(expected);
    }

    // --- default last name ---

    @Test
    void defaultConstructor_lastNameDefaultsToEmptyString() {
        final String actual = this.firstNameOnly.getLastName();
        final String expected = "";

        assertThat(actual).isEqualTo(expected);
    }

    // --- setters ---

    @Test
    void setFirstName_updatesFirstName() {
        final String newName = "Bob";

        employees.getFirst().setFirstName(newName);
        final String actual = employees.getFirst().getFirstName();

        assertThat(actual).isEqualTo(newName);
    }

    @Test
    void setLastName_updatesLastName() {
        final String newName = "Jones";

        employees.getFirst().setLastName(newName);
        final String actual = employees.getFirst().getLastName();

        assertThat(actual).isEqualTo(newName);
    }
}
