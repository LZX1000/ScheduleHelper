package com.schedulehelper.api.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedulehelper.api.entity.Employee;
import com.schedulehelper.api.exception.EmployeeNotFoundException;
import com.schedulehelper.api.exception.IdGenerationFailedException;
import com.schedulehelper.api.exception.MissingEmployeeContentException;
import com.schedulehelper.api.repository.EmployeeRepository;

/**
 * Service layer for managing {@link Employee} entities.
 *
 * <p>This service provides create, update, lookup, and delete operations.
 * IDs are required for modification actions, such as updates and deletes,
 * but are rejected when creating new entries.
 * 
 * <p>This service throws {@link EmployeeNotFoundException} when an employee cannot be found,
 * and {@link IdGenerationFailedException}
 *  if the persistence layer fails to assign an ID during creation.
 *
 * <p>All write operations are executed within transactional boundaries.
 */
@Service
public class EmployeeService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // --- Create ---

    /**
     * Creates a new {@link Employee} in the database.
     *
     * <p>This method requires that the provided employee has no predefined ID.
     * If an ID is present, the creation attempt is rejected. After creating
     * the employee, the method verifies that an ID was successfully generated.
     *
     * @param employee the employee entity to create; must not have an ID
     *
     * @return created employee
     * 
     * @throws MissingEmployeeContentException if the employee object is missing
     * @throws IllegalArgumentException if the employee already has an ID
     * @throws IdGenerationFailedException if the persistence layer fails to generate an ID
     */
    @Transactional
    public Employee createNew(final Employee employee) {
        if (employee == null) {
            LOG.warn("Attempted to create employee with no content");
            throw new MissingEmployeeContentException();
        }

        final Integer employeeId = employee.getId();

        if (employeeId != null) {
            LOG.warn("Attempted to create employee with predefined id {}", employeeId);
            throw new IllegalArgumentException("New employee must not have an ID.");
        }

        final Employee savedEmployee = this.employeeRepository.save(employee);
        final Integer savedEmployeeId = savedEmployee.getId();

        if (savedEmployeeId == null) {
            LOG.warn("Id generation failed for new employee.");
            throw new IdGenerationFailedException("employee");
        }

        LOG.info("Created new employee with id {}", savedEmployeeId);
        return savedEmployee;
    }

    // --- Read ---

    /**
     * Finds matching {@link Employee Employees} by partial first and/or last name.
     *
     * <p>Empty optionals are treated as null values,
     * meaning that name component is not used to filter results.
     * 
     * @param first optional first name fragment
     * @param last optional last name fragment
     * 
     * @return matching employees
     */
    @Transactional(readOnly = true)
    public List<Employee> findByPartialName(
        final Optional<String> first, final Optional<String> last
    ) {
        return this.employeeRepository.findByPartialName(first.orElse(null), last.orElse(null));
    }

    /**
     * Finds an {@link Employee} in the persistence layer by ID.
     *
     * @param employeeId the ID of the employee to find
     * 
     * @return the matching employee
     *
     * @throws EmployeeNotFoundException if the ID is not in the persistence layer
     */
    @Transactional(readOnly = true)
    public Employee findById(final Integer employeeId) {
        return this.employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    // --- Update ---

    /**
     * Updates a given {@link Employee} in the persistence layer.
     *
     * <p>This method requires that the provided employee has an ID.
     * If the ID is missing or does not correspond to an existing employee,
     * the update attempt is rejected.
     *
     * @param employee the updated employee entity; must have an ID
     * 
     * @return updated employee
     * 
     * @throws MissingEmployeeContentException if the employee object is missing
     * @throws IllegalArgumentException if the employee does not have an ID
     * @throws EmployeeNotFoundException if the ID is not in the persistence layer
     */
    @Transactional
    public Employee updateById(final Employee employee) {
        if (employee == null) {
            LOG.warn("Attempted to create employee with no content");
            throw new MissingEmployeeContentException();
        }
    
        final Integer employeeId = employee.getId();

        if (employeeId == null) {
            LOG.warn("Attempted to update employee with null id");
            throw new IllegalArgumentException("Employee must have an ID to be updated.");
        }
        if (!this.employeeRepository.existsById(employeeId)) {
            LOG.warn("Attempted to update non-existent employee with id {}", employeeId);
            throw new EmployeeNotFoundException(employeeId);
        }

        final Employee savedEmployee = this.employeeRepository.save(employee);
        LOG.info("Updated employee with id {}", employeeId);
        return savedEmployee;
    }

    // --- Delete ---

    /**
     * Deletes an {@link Employee} from the persistence layer by ID.
     *
     * <p>If no employee exists with the given ID, the deletion attempt is rejected.
     *
     * @param employeeId the ID of the employee to delete
     *
     * @throws EmployeeNotFoundException if no employee exists with the given ID
     */
    @Transactional
    public void deleteById(final Integer employeeId) {
        if (!this.employeeRepository.existsById(employeeId)) {
            LOG.warn("Attempted to delete non-existent employee with id {}", employeeId);
            throw new EmployeeNotFoundException(employeeId);
        }
        
        this.employeeRepository.deleteById(employeeId);
        LOG.info("Deleted employee with id {}", employeeId);
    }
}
