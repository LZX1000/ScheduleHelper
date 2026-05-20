package com.schedulehelper.api.schedulehelperapi.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schedulehelper.api.schedulehelperapi.entity.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    /**
     * Finds all shifts starting within a given time range.
     * Used to retrieve shifts for scheduling display.
     *
     * @param start the start of the time range (inclusive)
     * @param end   the end of the time range (inclusive)
     * @return all shifts with a start time within the given range
     */
    List<Shift> findByStartTimeBetween(OffsetDateTime start, OffsetDateTime end);
}
