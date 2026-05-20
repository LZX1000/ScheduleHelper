package com.schedulehelper.api.schedulehelperapi.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RoleType}.
 */
public class RoleTypeTest {
    private final List<RoleType> roleTypes = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.roleTypes.add(new RoleType("PA"));
        this.roleTypes.add(new RoleType("Director"));
        this.roleTypes.add(new RoleType("Camera #1"));
    }

    // --- getId ---

    @Test
    void getId_returnsEmpty() {
        final Integer actual = roleTypes.getFirst().getId();
        assertThat(actual).isNull();
    }

    // --- getTitle ---

    @Test
    void getTitle_returnsTitle() {
        final String actual = roleTypes.getFirst().getTitle();
        final String expected = "PA";

        assertThat(actual).isEqualTo(expected);
    }

    // --- setTitle ---

    @Test
    void setTitle_updatesTitle() {
        final String newTitle = "Bob";

        roleTypes.getFirst().setTitle(newTitle);
        final String actual = roleTypes.getFirst().getTitle();

        assertThat(actual).isEqualTo(newTitle);
    }
}
