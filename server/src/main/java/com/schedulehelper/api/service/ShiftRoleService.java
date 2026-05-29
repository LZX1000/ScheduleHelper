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

@Service
public class ShiftRoleService {
    private static final Logger LOG = LoggerFactory.getLogger(ShiftRoleService.class);

    private final ShiftRoleRepository shiftRoleRepository;

    @Autowired
    public ShiftRoleService(final ShiftRoleRepository shiftRoleRepository) {
        this.shiftRoleRepository = shiftRoleRepository;
    }

    @Transactional(readOnly = true)
    public ShiftRole findById(final Integer id) {
        return this.shiftRoleRepository.findById(id)
            .orElseThrow(() -> new ShiftRoleNotFoundException(id));
    }

    @Transactional
    public void createNew(final ShiftRole shiftRole) {
        final Integer shiftRoleId = shiftRole.getId();

        if (shiftRoleId != null) {
            LOG.warn("Attempted to create shift_role with predefined id {}", shiftRoleId);
            throw new IllegalArgumentException("New ShiftRole must not have an ID.");
        }

        final Integer savedShiftRoleId = this.shiftRoleRepository.save(shiftRole).getId();

        if (savedShiftRoleId == null) {
            LOG.warn("Id generation failed for new shift_role.");
            throw new IdGenerationFailedException("ShiftRole");
        }
    
        LOG.info("Created new shift with id {}", savedShiftRoleId);
    }


    @Transactional
    public void deleteById(final Integer shiftRoleId) {
        if (!this.shiftRoleRepository.existsById(shiftRoleId)) {
            LOG.warn("Attempted to delete non-existent shift_role with id {}", shiftRoleId);
            throw new ShiftRoleNotFoundException(shiftRoleId);
        }
        
        this.shiftRoleRepository.deleteById(shiftRoleId);
        LOG.info("Deleted shift with id {}", shiftRoleId);
    }

    @Transactional
    public void updateById(final ShiftRole shiftRole) {
        final Integer shiftRoleId = shiftRole.getId();
        
        if (shiftRoleId == null) {
            LOG.warn("Attempted to update shift_role with null id");
            throw new IllegalArgumentException("ShiftRole must have an ID to be updated.");
        }
        if (!this.shiftRoleRepository.existsById(shiftRoleId)) {
            LOG.warn("Attempted to update non-existent shift_role with id {}", shiftRoleId);
            throw new ShiftRoleNotFoundException(shiftRoleId);
        }

        this.shiftRoleRepository.save(shiftRole);
        LOG.info("Updated shift with id {}", shiftRoleId);
    }
}
