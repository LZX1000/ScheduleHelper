package com.schedulehelper.api.schedulehelperapi.entity;

import jakarta.persistence.*;

/**
 * Entity representing the {@code shift_role} table.
 *
 * @see <a href="db/schedulehelper.sql">schedulehelper.sql</a>
 */
@Entity
@Table(name = "shift_role")
public class ShiftRole {

    /**
     * Auto-generated primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * FK to shift. Required.
     */
    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    /**
     * FK to employee. Required.
     */
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    /**
     * FK to role_type. Required.
     */
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleType roleType;

    /**
     * Recorded score. Optional.
     */
    @Column(name = "performance")
    private Short performance;

    // CONSTRUCTOR

    /**
     * Required by JPA. Not for direct use.
     */
    protected ShiftRole() {}

    public ShiftRole(final Shift shift, final Employee employee, final RoleType roleType) {
        this(shift, employee, roleType, null);
    }

    public ShiftRole(
        final Shift shift,
        final Employee employee,
        final RoleType roleType,
        final Short performance
    ) {
        this.shift = shift;
        this.employee = employee;
        this.roleType = roleType;
        this.performance = performance;
    }

    // GETTERS

    public Integer getId() { return this.id; }
    public Shift getShift() { return this.shift; }
    public Employee getEmployee() { return this.employee; }
    public RoleType getRole() { return this.roleType; }

    public Short getPerformance() { return this.performance; }

    // SETTERS

    public void setPerformance(final short newPerformance) { this.performance = newPerformance; }
}
