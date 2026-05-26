package com.schedulehelper.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.schedulehelper.api.entity.Employee;
import com.schedulehelper.api.exception.EmployeeNotFoundException;
import com.schedulehelper.api.repository.EmployeeRepository;

@Service
public class EmployeeService {
    final EmployeeRepository employeeRepository;

    public EmployeeService(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void createNew(final Employee employee) {
        if (employee.getId() != null) {
            throw new IllegalArgumentException("New employee must not have an ID.");
        }
        this.employeeRepository.save(employee);
    }

    public List<Employee> findByPartialName(
        final Optional<String> first, final Optional<String> last
    ) {
        // TODO: rename findByName in employeeRepository to findByPartialName
        //       create string findByName method for employeeRepository
        return this.employeeRepository.findByName(first.orElse(null), last.orElse(null));
    }

    public void deleteById(final Integer id) {
        if (!this.employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        this.employeeRepository.deleteById(id);
    }

    public Employee findById(final Integer id) {
        return this.employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
}
