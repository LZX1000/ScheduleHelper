package com.schedulehelper.api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedulehelper.api.entity.RoleType;
import com.schedulehelper.api.exception.RoleTypeNotFoundException;
import com.schedulehelper.api.repository.RoleTypeRepository;

@Service
public class RoleTypeService {
    private static final Logger LOG = LoggerFactory.getLogger(RoleTypeService.class);

    private final RoleTypeRepository roleTypeRepository;

    @Autowired
    public RoleTypeService(final RoleTypeRepository roleTypeRepository) {
        this. roleTypeRepository = roleTypeRepository;
    }

    @Transactional(readOnly = true)
    public RoleType findByTitle(final String title) {
        return this.roleTypeRepository.findByTitle(title).orElseThrow(() -> {
            LOG.warn("Attempted to find non-existent RoleType with title {}", title);
            throw new RoleTypeNotFoundException(
                String.format("RoleType with title {} not found", title)
            );
        });
    }

    @Transactional(readOnly = true)
    public List<RoleType> findByPartialTitle(final String title) {
        return this.roleTypeRepository.findByPartialTitle(title);
    }

    @Transactional(readOnly = true)
    public RoleType findById(final Integer id) {
        return this.roleTypeRepository.findById(id)
            .orElseThrow(() -> new RoleTypeNotFoundException(id));
    }

    @Transactional
    public void createNew(final RoleType roleType) {
        final Integer roleTypeId = roleType.getId();

        if (roleTypeId != null) {
            LOG.warn("Attempted to create role_type with predefined id {}", roleTypeId);
            throw new IllegalArgumentException("New RoleType must not have an ID.");
        }

        final RoleType savedRoleType = this.roleTypeRepository.save(roleType);
        LOG.info("Created new employee with id {}", savedRoleType.getId());
    }

    @Transactional
    public void deleteById(final Integer roleTypeId) {
        if (!this.roleTypeRepository.existsById(roleTypeId)) {
            LOG.warn("Attempted to delete non-existent role_type with id {}", roleTypeId);
            throw new RoleTypeNotFoundException(roleTypeId);
        }
        
        this.roleTypeRepository.deleteById(roleTypeId);
        LOG.info("Deleted role_type with id {}", roleTypeId);
    }

    @Transactional
    public void updateById(final RoleType roleType) {
        final Integer roleTypeId = roleType.getId();
        
        if (roleTypeId == null) {
            LOG.warn("Attempted to update role_type with null id");
            throw new IllegalArgumentException("RoleType must have an ID to be updated.");
        }
        if (!this.roleTypeRepository.existsById(roleTypeId)) {
            LOG.warn("Attempted to update non-existent role_type with id {}", roleTypeId);
            throw new RoleTypeNotFoundException(roleTypeId);
        }

        this.roleTypeRepository.save(roleType);
        LOG.info("Updated role_type with id {}", roleTypeId);
    }
}
