package com.schedulehelper.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schedulehelper.api.entity.Employee;
import com.schedulehelper.api.exception.EmployeeNotFoundException;
import com.schedulehelper.api.repository.EmployeeRepository;

@Service
public class EmployeeService {
    final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void createNew(final Employee employee) {
        if (employee.getId() != null) {
            throw new IllegalArgumentException("New employee must not have an ID.");
        }
        this.employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByPartialName(
        final Optional<String> first, final Optional<String> last
    ) {
        // TODO: rename findByName in employeeRepository to findByPartialName
        //       create string findByName method for employeeRepository
        return this.employeeRepository.findByName(first.orElse(null), last.orElse(null));
    }

    @Transactional
    public void deleteById(final Integer id) {
        if (!this.employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        this.employeeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Employee findById(final Integer id) {
        return this.employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
}
