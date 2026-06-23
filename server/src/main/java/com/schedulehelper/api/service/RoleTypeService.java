package com.schedulehelper.api.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedulehelper.api.entity.RoleType;
import com.schedulehelper.api.exception.IdGenerationFailedException;
import com.schedulehelper.api.exception.MissingRoleTypeContentException;
import com.schedulehelper.api.exception.RoleTypeNotFoundException;
import com.schedulehelper.api.repository.RoleTypeRepository;

/**
 * Service layer for managing {@link RoleType} entities.
 *
 * <p>This service provides create, update, lookup, and delete operations.
 * IDs are required for modification actions, such as updates and deletes,
 * but are rejected when creating new entries.
 * 
 * <p>This service throws {@link RoleTypeNotFoundException} when a role type cannot be found,
 * and {@link IdGenerationFailedException}
 *  if the persistence layer fails to assign an ID during creation.
 *
 * <p>All write operations are executed within transactional boundaries.
 */
@Service
public class RoleTypeService {
    private static final Logger LOG = LoggerFactory.getLogger(RoleTypeService.class);

    private final RoleTypeRepository roleTypeRepository;

    @Autowired
    public RoleTypeService(final RoleTypeRepository roleTypeRepository) {
        this.roleTypeRepository = roleTypeRepository;
    }

    /**
     * Finds a {@link RoleType} in the persistence layer by unique title.
     *
     * @param title the title of the role type to find
     * 
     * @return the matching role type
     *
     * @throws RoleTypeNotFoundException if the title does not match any existing role type
     */
    @Transactional(readOnly = true)
    public RoleType findByTitle(final String title) {
        return this.roleTypeRepository.findByTitle(title).orElseThrow(() -> {
            LOG.warn("Attempted to find non-existent RoleType with title {}", title);
            throw new RoleTypeNotFoundException(
                String.format("RoleType with title {} not found", title)
            );
        });
    }

    /**
     * Finds matching {@link RoleType RoleTypes} by partial title.
     * 
     * @param title title fragment
     * 
     * @return matching role types
     */
    @Transactional(readOnly = true)
    public List<RoleType> findByPartialTitle(final Optional<String> title) {
        return this.roleTypeRepository.findByPartialTitle(title.orElse(null));
    }

    /**
     * Finds a {@link RoleType} in the persistence layer by ID.
     *
     * @param roleTypeId the ID of the role type to find
     * 
     * @return the matching role type
     *
     * @throws RoleTypeNotFoundException if the ID is not in the persistence layer
     */
    @Transactional(readOnly = true)
    public RoleType findById(final Integer roleTypeId) {
        return this.roleTypeRepository.findById(roleTypeId)
            .orElseThrow(() -> new RoleTypeNotFoundException(roleTypeId));
    }

    /**
     * Creates a new {@link RoleType} in the database.
     *
     * <p>This method requires that the provided role type has no predefined ID.
     * If an ID is present, the creation attempt is rejected. After creating
     * the role type, the method verifies that an ID was successfully generated.
     *
     * @param roleType the role type entity to create; must not have an ID
     *
     * @return created role type
     * 
     * @throws MissingRoleTypeContentException if the role type object is missing
     * @throws IllegalArgumentException if the role type already has an ID
     * @throws IdGenerationFailedException if the persistence layer fails to generate an ID
     */
    @Transactional
    public RoleType createNew(final RoleType roleType) {
        if (roleType == null) {
            LOG.warn("Attempted to create role_type with no content");
            throw new MissingRoleTypeContentException();
        }

        final Integer roleTypeId = roleType.getId();

        if (roleTypeId != null) {
            LOG.warn("Attempted to create role_type with predefined id {}", roleTypeId);
            throw new IllegalArgumentException("New RoleType must not have an ID.");
        }

        final RoleType savedRoleType = this.roleTypeRepository.save(roleType);
        final Integer savedRoleTypeId = savedRoleType.getId();

        if (savedRoleTypeId == null) {
            LOG.warn("Id generation failed for new role_type.");
            throw new IdGenerationFailedException("RoleType");
        }

        LOG.info("Created new employee with id {}", savedRoleTypeId);
        return savedRoleType;
    }

    /**
     * Deletes a {@link RoleType} from the persistence layer by ID.
     *
     * <p>If no role type exists with the given ID, the deletion attempt is rejected.
     *
     * @param roleTypeId the ID of the role type to delete
     *
     * @throws RoleTypeNotFoundException if no role type exists with the given ID
     */
    @Transactional
    public void deleteById(final Integer roleTypeId) {
        if (!this.roleTypeRepository.existsById(roleTypeId)) {
            LOG.warn("Attempted to delete non-existent role_type with id {}", roleTypeId);
            throw new RoleTypeNotFoundException(roleTypeId);
        }
        
        this.roleTypeRepository.deleteById(roleTypeId);
        LOG.info("Deleted role_type with id {}", roleTypeId);
    }

    /**
     * Updates a given {@link RoleType} in the persistence layer.
     *
     * <p>This method requires that the provided role type has an ID.
     * If the ID is missing or does not correspond to an existing role type,
     * the update attempt is rejected.
     *
     * @param roleType the updated role type entity; must have an ID
     *
     * @return updated role type
     * 
     * @throws MissingRoleTypeContentException if the role type object is missing
     * @throws IllegalArgumentException if the role type does not have an ID
     * @throws RoleTypeNotFoundException if the ID is not in the persistence layer
     */
    @Transactional
    public RoleType updateById(final RoleType roleType) {
        if (roleType == null) {
            LOG.warn("Attempted to create role_type with no content");
            throw new MissingRoleTypeContentException();
        }

        final Integer roleTypeId = roleType.getId();

        if (roleTypeId == null) {
            LOG.warn("Attempted to update role_type with null id");
            throw new IllegalArgumentException("RoleType must have an ID to be updated.");
        }
        if (!this.roleTypeRepository.existsById(roleTypeId)) {
            LOG.warn("Attempted to update non-existent role_type with id {}", roleTypeId);
            throw new RoleTypeNotFoundException(roleTypeId);
        }

        final RoleType savedRoleType = this.roleTypeRepository.save(roleType);
        LOG.info("Updated role_type with id {}", roleTypeId);
        return savedRoleType;
    }
}
