package com.schedulehelper.api.schedulehelperapi.entity;

import jakarta.persistence.*;

/**
 * Entity representing the {@code role_type} table.
 *
 * @see <a href="db/schedulehelper.sql">schedulehelper.sql</a>
 */
@Entity
@Table(name = "role_type")
public class RoleType {

    /**
     * Auto-generated primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Shift's start time. Required.
     */
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    // CONSTRUCTOR

    public RoleType() {}

    // GETTERS

    public Integer getId() { return this.id; }
    public String getTitle() { return this.title; }

    // SETTERS

    public void setStartTime(final String newTitle) { this.title = newTitle; }
}
