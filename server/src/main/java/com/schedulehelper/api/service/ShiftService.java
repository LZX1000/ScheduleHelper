package com.schedulehelper.api.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedulehelper.api.entity.Shift;
import com.schedulehelper.api.exception.IdGenerationFailedException;
import com.schedulehelper.api.exception.ShiftNotFoundException;
import com.schedulehelper.api.repository.ShiftRepository;

/**
 * Service layer for managing {@link Shift} entities.
 *
 * <p>This service provides create, update, lookup, and delete operations.
 * IDs are required for modification actions, such as updates and deletes,
 * but are rejected when creating new entries.
 * 
 * <p>This service throws {@link ShiftNotFoundException} when a shift cannot be found,
 * and {@link IdGenerationFailedException}
 *  if the persistence layer fails to assign an ID during creation.
 *
 * <p>All write operations are executed within transactional boundaries.
 */
@Service
public class ShiftService {
    private static final Logger LOG = LoggerFactory.getLogger(ShiftService.class);

    private final ShiftRepository shiftRepository;

    @Autowired
    public ShiftService(final ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    /**
     * Finds matching {@link Shift Shifts} by a start time range.
     * 
     * @param start beginning of the range
     * @param end end of the range
     * 
     * @return matching shifts
     */
    @Transactional(readOnly = true)
    public List<Shift> findByStartTimeBetween(
        final OffsetDateTime start, final OffsetDateTime end
    ) {
        return this.shiftRepository.findByStartTimeBetween(start, end);
    }

    /**
     * Finds a {@link Shift} in the persistence layer by ID.
     *
     * @param shiftId the ID of the shift to find
     * 
     * @return the matching shift
     *
     * @throws ShiftNotFoundException if the ID is not in the persistence layer
     */
    @Transactional(readOnly = true)
    public Shift findById(final Integer shiftId) {
        return this.shiftRepository.findById(shiftId)
            .orElseThrow(() -> new ShiftNotFoundException(shiftId));
    }

    /**
     * Creates a new {@link Shif} in the database.
     *
     * <p>This method requires that the provided shift has no predefined ID.
     * If an ID is present, the creation attempt is rejected. After creating
     * the shift, the method verifies that an ID was successfully generated.
     *
     * @param shift the shift entity to create; must not have an ID
     * 
     * @return created shift
     *
     * @throws IllegalArgumentException if the shift already has an ID
     * @throws IdGenerationFailedException if the persistence layer fails to generate an ID
     */
    @Transactional
    public Shift createNew(final Shift shift) {
        final Integer shiftId = shift.getId();
        if (shiftId != null) {
            LOG.warn("Attempted to create shift with predefined id {}", shiftId);
            throw new IllegalArgumentException("New Shift must not have an ID.");
        }

        final Shift savedShift = this.shiftRepository.save(shift);
        final Integer savedShiftId = savedShift.getId();

        if (savedShiftId == null) {
            LOG.warn("Id generation failed for new shift.");
            throw new IdGenerationFailedException("Shift");
        }

        LOG.info("Created new shift with id {}", savedShiftId);
        return savedShift;
    }

    /**
     * Deletes a {@link Shift} from the persistence layer by ID.
     *
     * <p>If no shift exists with the given ID, the deletion attempt is rejected.
     *
     * @param shiftRoleId the ID of the shift to delete
     *
     * @throws ShiftNotFoundException if no shift exists with the given ID
     */
    @Transactional
    public void deleteById(final Integer shiftId) {
        if (!this.shiftRepository.existsById(shiftId)) {
            LOG.warn("Attempted to delete non-existent shift with id {}", shiftId);
            throw new ShiftNotFoundException(shiftId);
        }
        
        this.shiftRepository.deleteById(shiftId);
        LOG.info("Deleted shift with id {}", shiftId);
    }

    /**
     * Updates a given {@link Shift} in the persistence layer.
     *
     * <p>This method requires that the provided shift has an ID.
     * If the ID is missing or does not correspond to an existing shift,
     * the update attempt is rejected.
     *
     * @param shift the updated shift entity; must have an ID
     * 
     * @return updated shift
     *
     * @throws IllegalArgumentException if the shift does not have an ID
     * @throws ShiftNotFoundException if the ID is not in the persistence layer
     */
    @Transactional
    public Shift updateById(final Shift shift) {
        final Integer shiftId = shift.getId();
        
        if (shiftId == null) {
            LOG.warn("Attempted to update shift with null id");
            throw new IllegalArgumentException("Shift must have an ID to be updated.");
        }
        if (!this.shiftRepository.existsById(shiftId)) {
            LOG.warn("Attempted to update non-existent shift with id {}", shiftId);
            throw new ShiftNotFoundException(shiftId);
        }

        final Shift updatedShift = this.shiftRepository.save(shift);
        LOG.info("Updated shift with id {}", shiftId);
        return updatedShift;
    }
}
