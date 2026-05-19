package com.schedulehelper.api.schedulehelperapi.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

/**
 * Entity representing the {@code shift} table.
 *
 * @see <a href="db/schedulehelper.sql">schedulehelper.sql</a>
 */
@Entity
@Table(name = "shift")
public class Shift {

    /**
     * Auto-generated primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Shift's start time. Required.
     */
    @Column(name = "start_time", nullable = false)
    private OffsetDateTime startTime;

    /**
     * Shift's end time. Required.
     */
    @Column(name = "end_time", nullable = false)
    private OffsetDateTime endTime;

    // CONSTRUCTOR

    public Shift() {}

    // GETTERS

    public Integer getId() { return this.id; }
    public OffsetDateTime getStartTime() { return this.startTime; }
    public OffsetDateTime getEndTime() { return this.endTime; }

    // SETTERS

    public void setStartTime(final OffsetDateTime newStartTime) { this.startTime = newStartTime; }
    public void setEndTime(final OffsetDateTime newEndTime) { this.endTime = newEndTime; }
}
