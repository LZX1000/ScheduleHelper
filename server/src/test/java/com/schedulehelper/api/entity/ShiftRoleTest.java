package com.schedulehelper.api.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link ShiftRole}.
 */
@Tag("entity")
@ExtendWith(MockitoExtension.class)
class ShiftRoleTest {
    private final List<ShiftRole> shiftRoles = new ArrayList<>();

    private Shift shift;
    private Employee employee;
    private RoleType roleType;

    @BeforeEach
    void setUp() {
        this.shift    = mock(Shift.class   );
        this.employee = mock(Employee.class);
        this.roleType = mock(RoleType.class);

        this.shiftRoles.add(new ShiftRole(shift, employee, roleType));
        this.shiftRoles.add(new ShiftRole(shift, employee, roleType, (short) 50));
    }

    // --- getId ---

    @Test
    void getId_returnsEmpty() {
        final Integer actual = this.shiftRoles.get(0).getId();
        assertThat(actual).isNull();
    }

    // --- getShift ---

    @Test
    void getShift_returnsShift() {
        assertThat(this.shiftRoles.get(0).getShift()).isEqualTo(this.shift);
    }

    // --- getEmployee ---

    @Test
    void getEmployee_returnsEmployee() {
        assertThat(this.shiftRoles.get(0).getEmployee()).isEqualTo(this.employee);
    }

    // --- getRoleType ---

    @Test
    void getRoleType_returnsRole() {
        assertThat(this.shiftRoles.get(0).getRole()).isEqualTo(this.roleType);
    }

    // --- getPerformance ---

    @Test
    void getPerformance_returnsPerformance() {
        assertThat(this.shiftRoles.get(1).getPerformance()).isEqualTo((short) 50);
    }

    @Test
    void getPerformance_defaultNull_returnsPerformance() {
        assertThat(this.shiftRoles.get(0).getPerformance()).isNull();
    }

    // --- setPerformance ---

    @Test
    void setPerformance_updatesPerformance() {
        final short newPerformance = 25;

        shiftRoles.get(0).setPerformance(newPerformance);

        assertThat(shiftRoles.get(0).getPerformance()).isEqualTo(newPerformance);
    }
}
