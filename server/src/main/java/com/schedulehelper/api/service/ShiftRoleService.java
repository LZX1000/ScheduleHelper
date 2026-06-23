package com.schedulehelper.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedulehelper.api.entity.ShiftRole;
import com.schedulehelper.api.exception.IdGenerationFailedException;
import com.schedulehelper.api.exception.ShiftRoleNotFoundException;
import com.schedulehelper.api.repository.ShiftRoleRepository;

/**
 * Service layer for managing {@link ShiftRole} entities.
 *
 * <p>This service provides create, update, lookup, and delete operations.
 * IDs are required for modification actions, such as updates and deletes,
 * but are rejected when creating new entries.
 * 
 * <p>This service throws {@link ShiftRoleNotFoundException} when a shift role cannot be found,
 * and {@link IdGenerationFailedException}
 *  if the persistence layer fails to assign an ID during creation.
 *
 * <p>All write operations are executed within transactional boundaries.
 */
@Service
public class ShiftRoleService {
    private static final Logger LOG = LoggerFactory.getLogger(ShiftRoleService.class);

    private final ShiftRoleRepository shiftRoleRepository;

    @Autowired
    public ShiftRoleService(final ShiftRoleRepository shiftRoleRepository) {
        this.shiftRoleRepository = shiftRoleRepository;
    }

    // --- Create ---

    /**
     * Creates a new {@link ShifRole} in the database.
     *
     * <p>This method requires that the provided shift role has no predefined ID.
     * If an ID is present, the creation attempt is rejected. After creating
     * the shift role, the method verifies that an ID was successfully generated.
     *
     * @param shiftRole the shift role entity to create; must not have an ID
     *
     * @return created shift role
     * 
     * @throws IllegalArgumentException if the shift role already has an ID
     * @throws IdGenerationFailedException if the persistence layer fails to generate an ID
     */
    @Transactional
    public ShiftRole createNew(final ShiftRole shiftRole) {
        final Integer shiftRoleId = shiftRole.getId();
        if (shiftRoleId != null) {
            LOG.warn("Attempted to create shift_role with predefined id {}", shiftRoleId);
            throw new IllegalArgumentException("New ShiftRole must not have an ID.");
        }

        final ShiftRole savedShiftRole = this.shiftRoleRepository.save(shiftRole);
        final Integer savedShiftRoleId = savedShiftRole.getId();

        if (savedShiftRoleId == null) {
            LOG.warn("Id generation failed for new shift_role.");
            throw new IdGenerationFailedException("ShiftRole");
        }
    
        LOG.info("Created new shift with id {}", savedShiftRoleId);
        return savedShiftRole;
    }

    // --- Read ---

    /**
     * Finds a {@link ShiftRole} in the persistence layer by ID.
     *
     * @param shiftRoleId the ID of the shift role to find
     * 
     * @return the matching shift role
     *
     * @throws ShiftRoleNotFoundException if the ID is not in the persistence layer
     */
    @Transactional(readOnly = true)
    public ShiftRole findById(final Integer shiftRoleId) {
        return this.shiftRoleRepository.findById(shiftRoleId)
            .orElseThrow(() -> new ShiftRoleNotFoundException(shiftRoleId));
    }

    // --- Update ---

    /**
     * Updates a given {@link ShiftRole} in the persistence layer.
     *
     * <p>This method requires that the provided shift role has an ID.
     * If the ID is missing or does not correspond to an existing shift role,
     * the update attempt is rejected.
     *
     * @param shiftRole the updated shift role entity; must have an ID
     *
     * @return updated shift role
     * 
     * @throws IllegalArgumentException if the shift role does not have an ID
     * @throws ShiftRoleNotFoundException if the ID is not in the persistence layer
     */
    @Transactional
    public ShiftRole updateById(final ShiftRole shiftRole) {
        final Integer shiftRoleId = shiftRole.getId();

        if (shiftRoleId == null) {
            LOG.warn("Attempted to update shift_role with null id");
            throw new IllegalArgumentException("ShiftRole must have an ID to be updated.");
        }
        if (!this.shiftRoleRepository.existsById(shiftRoleId)) {
            LOG.warn("Attempted to update non-existent shift_role with id {}", shiftRoleId);
            throw new ShiftRoleNotFoundException(shiftRoleId);
        }

        final ShiftRole savedShiftRole = this.shiftRoleRepository.save(shiftRole);
        LOG.info("Updated shift with id {}", shiftRoleId);
        return savedShiftRole;
    }

    // --- Delete ---

    /**
     * Deletes a {@link ShiftRole} from the persistence layer by ID.
     *
     * <p>If no shift role exists with the given ID, the deletion attempt is rejected.
     *
     * @param shiftRoleId the ID of the shift role to delete
     *
     * @throws ShiftRoleNotFoundException if no shift role exists with the given ID
     */
    @Transactional
    public void deleteById(final Integer shiftRoleId) {
        if (!this.shiftRoleRepository.existsById(shiftRoleId)) {
            LOG.warn("Attempted to delete non-existent shift_role with id {}", shiftRoleId);
            throw new ShiftRoleNotFoundException(shiftRoleId);
        }
        
        this.shiftRoleRepository.deleteById(shiftRoleId);
        LOG.info("Deleted shift with id {}", shiftRoleId);
    }
}
