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
import com.schedulehelper.api.repository.EmployeeRepository;

@Service
public class EmployeeService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void createNew(final Employee employee) {
        final Integer employeeId = employee.getId();

        if (employeeId != null) {
            LOG.warn("Attempted to create employee with defined id {}", employeeId);
            throw new IllegalArgumentException("New employee must not have an ID.");
        }

        this.employeeRepository.save(employee);
        LOG.info("Created new employee with id {}", employeeId);
    }

    @Transactional
    public void updateById(final Employee employee) {
        final Integer employeeId = employee.getId();
        
        if (employeeId == null) {
            LOG.warn("Attempted to update employee with null id");
            throw new IllegalArgumentException("Employee must have an ID to be updated.");
        }
        if (!this.employeeRepository.existsById(employeeId)) {
            LOG.warn("Attempted to update non-existent employee with id {}", employeeId);
            throw new EmployeeNotFoundException(employeeId);
        }

        this.employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByPartialName(
        final Optional<String> first, final Optional<String> last
    ) {
        return this.employeeRepository.findByPartialName(first.orElse(null), last.orElse(null));
    }

    @Transactional
    public void deleteById(final Integer employeeId) {
        if (!this.employeeRepository.existsById(employeeId)) {
            LOG.warn("Attempted to delete non-existent employee with id {}", employeeId);
            throw new EmployeeNotFoundException(employeeId);
        }
        
        this.employeeRepository.deleteById(employeeId);
        LOG.info("Deleted employee with id {}", employeeId);
    }

    @Transactional(readOnly = true)
    public Employee findById(final Integer id) {
        return this.employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
}
