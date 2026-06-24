package com.schedulehelper.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.schedulehelper.api.entity.Employee;
import com.schedulehelper.api.exception.EmployeeNotFoundException;
import com.schedulehelper.api.exception.IdGenerationFailedException;
import com.schedulehelper.api.exception.MissingEmployeeContentException;
import com.schedulehelper.api.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    // --- createNew ---

    @Test
    void testCreateNew_withId_throwsException() {
        final Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1);

        assertThrows(IllegalArgumentException.class, () -> this.employeeService.createNew(employee));
    }

    @Test
    void testCreateNew_missingEmployeeContent_throwsException() {
        assertThrows(MissingEmployeeContentException.class, () -> this.employeeService.createNew(null));
    }

    @Test
    void testCreateNew_idGenerationFailed_throwsException() {
        final Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(null);
        final Employee savedEmployee = mock(Employee.class);
        when(savedEmployee.getId()).thenReturn(null);
        when(this.employeeRepository.save(employee)).thenReturn(savedEmployee);

        assertThrows(IdGenerationFailedException.class, () -> this.employeeService.createNew(employee));
    }

    @Test
    void testCreateNew_success() {
        final Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(null);
        final Employee savedEmployee = mock(Employee.class);
        when(savedEmployee.getId()).thenReturn(1);
        when(this.employeeRepository.save(employee)).thenReturn(savedEmployee);

        assertEquals(savedEmployee, this.employeeService.createNew(employee));
    }

    // --- updateById ---

    @Test
    void testUpdateById_nullId_throwsException() {
        final Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> this.employeeService.updateById(employee));
    }

    @Test
    void testUpdateById_missingEmployeeContent_throwsException() {
        assertThrows(MissingEmployeeContentException.class, () -> this.employeeService.updateById(null));
    }

    @Test
    void testUpdateById_doesNotExist_throwsException() {
        final Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1);
        when(this.employeeRepository.existsById(1)).thenReturn(false);

        assertThrows(EmployeeNotFoundException.class, () -> this.employeeService.updateById(employee));
    }

    @Test
    void testUpdateById_success() {
        final Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1);
        when(this.employeeRepository.existsById(1)).thenReturn(true);
        when(this.employeeRepository.save(employee)).thenReturn(employee);

        assertEquals(employee, this.employeeService.updateById(employee));
    }

    // --- findByPartialName ---

    @Test
    void testFindByPartialName_bothNames() {
        final List<Employee> expected = List.of(mock(Employee.class));
        when(this.employeeRepository.findByPartialName("John", "Smith")).thenReturn(expected);

        final List<Employee> result = this.employeeService.findByPartialName(
            Optional.of("John"), Optional.of("Smith")
        );

        assertEquals(expected, result);
    }

    @Test void testFindByPartialName_firstName() {
        final List<Employee> expected = List.of(mock(Employee.class));
        when(this.employeeRepository.findByPartialName("John", null)).thenReturn(expected);

        final List<Employee> result = this.employeeService.findByPartialName(
            Optional.of("John"), Optional.empty()
        );

        assertEquals(expected, result);
    }

    @Test void testFindByPartialName_lastName() {
        final List<Employee> expected = List.of(mock(Employee.class));
        when(this.employeeRepository.findByPartialName(null, "Smith")).thenReturn(expected);

        final List<Employee> result = this.employeeService.findByPartialName(
            Optional.empty(), Optional.of("Smith")
        );

        assertEquals(expected, result);
    }

    @Test
    void testFindByPartialName_nullNames() {
        final List<Employee> expected = List.of(mock(Employee.class));
        when(this.employeeRepository.findByPartialName(null, null)).thenReturn(expected);

        final List<Employee> result = this.employeeService.findByPartialName(
            Optional.empty(), Optional.empty()
        );

        assertEquals(expected, result);
    }

    // --- deleteById ---

    @Test
    void testDeleteById_doesNotExist_throwsException() {
        when(this.employeeRepository.existsById(1)).thenReturn(false);

        assertThrows(EmployeeNotFoundException.class, () -> this.employeeService.deleteById(1));
    }

    @Test
    void testDeleteById_success() {
        when(this.employeeRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> this.employeeService.deleteById(1));
    }

    // --- findById ---

    @Test
    void testFindById_success() {
        final Employee employee = mock(Employee.class);
        when(this.employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        final Employee result = this.employeeService.findById(1);

        assertEquals(employee, result);
    }

    @Test
    void testFindById_notFound() {
        when(this.employeeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> this.employeeService.findById(99));
    }
}
