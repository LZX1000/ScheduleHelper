package com.schedulehelper.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schedulehelper.api.entity.Employee;
import com.schedulehelper.api.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    final private EmployeeService employeeService;

    EmployeeController(final EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("")
    // required = false for custom exception in service layer
    public ResponseEntity<Employee> createNew(@RequestBody(required = false) Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.employeeService.createNew(employee));
    }

    @PutMapping("")
    // required = false for custom exception in service layer
    public ResponseEntity<Employee> updateById(@RequestBody(required = false) final Employee employee) {
        return ResponseEntity.status(HttpStatus.OK).body(this.employeeService.updateById(employee));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Employee>> findByPartialName(
        @RequestParam final Optional<String> partialFirst,
        @RequestParam final Optional<String> partialLast
    ) {
        return ResponseEntity.ok(this.employeeService.findByPartialName(partialFirst, partialLast));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteById(@PathVariable final Integer employeeId) {
        this.employeeService.deleteById(employeeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getById(@PathVariable final Integer employeeId) {
        return ResponseEntity.ok(this.employeeService.findById(employeeId));
    }
}
