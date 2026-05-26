package com.schedulehelper.api.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedulehelper.api.entity.Shift;
import com.schedulehelper.api.exception.ShiftNotFoundException;
import com.schedulehelper.api.repository.ShiftRepository;

@Service
public class ShiftService {
    private static final Logger LOG = LoggerFactory.getLogger(ShiftService.class);

    private final ShiftRepository shiftRepository;

    @Autowired
    public ShiftService(final ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Transactional(readOnly = true)
    public List<Shift> findByStartTimeBetween(
        final OffsetDateTime start, final OffsetDateTime end
    ) {
        return this.shiftRepository.findByStartTimeBetween(start, end);
    }

    @Transactional(readOnly = true)
    public Shift findById(final Integer id) {
        return this.shiftRepository.findById(id)
            .orElseThrow(() -> new ShiftNotFoundException(id));
    }

    @Transactional
    public void createNew(final Shift shift) {
        final Integer shiftId = shift.getId();

        if (shiftId != null) {
            LOG.warn("Attempted to create shift with predefined id {}", shiftId);
            throw new IllegalArgumentException("New shift must not have an ID.");
        }

        final Shift savedShift = this.shiftRepository.save(shift);
        LOG.info("Created new shift with id {}", savedShift.getId());
    }


    @Transactional
    public void deleteById(final Integer shiftId) {
        if (!this.shiftRepository.existsById(shiftId)) {
            LOG.warn("Attempted to delete non-existent shift with id {}", shiftId);
            throw new ShiftNotFoundException(shiftId);
        }
        
        this.shiftRepository.deleteById(shiftId);
        LOG.info("Deleted shift with id {}", shiftId);
    }

    @Transactional
    public void updateById(final Shift shift) {
        final Integer shiftId = shift.getId();
        
        if (shiftId == null) {
            LOG.warn("Attempted to update shift with null id");
            throw new IllegalArgumentException("Shift must have an ID to be updated.");
        }
        if (!this.shiftRepository.existsById(shiftId)) {
            LOG.warn("Attempted to update non-existent shift with id {}", shiftId);
            throw new ShiftNotFoundException(shiftId);
        }

        this.shiftRepository.save(shift);
        LOG.info("Updated shift with id {}", shiftId);
    }
}
