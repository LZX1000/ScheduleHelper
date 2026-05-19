package com.schedulehelper.api.schedulehelperapi.entity;

import jakarta.persistence.*;

/**
 * Entity representing the {@code employee} table.
 *
 * @see <a href="db/schedulehelper.sql">schedulehelper.sql</a>
 */
@Entity
@Table(name = "employee")
public class Employee {

    /**
     * Auto-generated primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Employee's first name. Required.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Employee's last name. Optional.
     */
    @Column(name = "last_name")
    private String lastName;

    // CONSTRUCTOR

    public Employee() {}

    // GETTERS

    public Integer getId() { return this.id; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }

    // SETTERS

    public void setFirstName(final String newFirstName) { this.firstName = newFirstName; }
    public void setLastName(final String newLastName) { this.lastName = newLastName; }
}
