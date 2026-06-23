package com.schedulehelper.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.schedulehelper.api.entity.Employee;
import com.schedulehelper.api.exception.EmployeeNotFoundException;
import com.schedulehelper.api.exception.IdGenerationFailedException;
import com.schedulehelper.api.exception.MissingEmployeeContent;
import com.schedulehelper.api.service.EmployeeService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    // --- createNew ---

    @Test
    public void testCreateNew_illegalArgument_exceptionHandlerIntercept() throws Exception {
        when(this.employeeService.createNew(any(Employee.class)))
            .thenThrow(new IllegalArgumentException("New employee must not have an ID."));

        mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(Employee.class))))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateNew_missingEmployeeContent_exceptionHandlerIntercept() throws Exception {
        when (this.employeeService.createNew(null))
            .thenThrow(new MissingEmployeeContent());

        mockMvc.perform(post("/api/employee"))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testCreateNew_idGenerationFailed_exceptionHandlerIntercept() throws Exception {
        when(this.employeeService.createNew(any(Employee.class)))
            .thenThrow(new IdGenerationFailedException("Id generation failed for new employee."));

        mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(Employee.class))))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateNew_success() throws Exception {
        final Employee employee = mock(Employee.class);
        final String employeeJson = new ObjectMapper().writeValueAsString(employee);

        when(this.employeeService.createNew(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
            .andExpect(status().isCreated())
            .andExpect(content().json(employeeJson));
    }

    // --- updateById ---

    @Test
    public void testUpdateById_missingEmployeeContent_exceptionHandlerIntercept() throws Exception {
        when(this.employeeService.updateById(null))
            .thenThrow(new MissingEmployeeContent());

        mockMvc.perform(put("/api/employee"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateById_illegalArgument_exceptionHandlerIntercept() throws Exception {
        when(this.employeeService.updateById(any(Employee.class)))
            .thenThrow(new IllegalArgumentException("Employee must have an ID to be updated."));

        mockMvc.perform(put("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(Employee.class))))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateById_employeeNotFound_exceptionHandlerIntercept() throws Exception {
        when(this.employeeService.updateById(any(Employee.class)))
            .thenThrow(new EmployeeNotFoundException(0));

        mockMvc.perform(put("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(Employee.class))))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateById_success() throws Exception {
        final Employee employee = mock(Employee.class);
        final String employeeJson = new ObjectMapper().writeValueAsString(employee);

        when(this.employeeService.updateById(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(put("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
            .andExpect(status().isOk())
            .andExpect(content().json(employeeJson));
    }

    // --- findByPartialName ---

    @Test
    public void testFindByPartialName_nullSearch_success() throws Exception {
        final List<Employee> expected = List.of(mock(Employee.class));
        final String expectedJson = new ObjectMapper().writeValueAsString(expected);

        when(this.employeeService.findByPartialName(Optional.empty(), Optional.empty()))
            .thenReturn(expected);

        mockMvc.perform(get("/api/employee/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(mock(Employee.class)))))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    @Test
    public void testFindByPartialName_success() throws Exception {
        final List<Employee> expected = List.of(mock(Employee.class));
        final String expectedJson = new ObjectMapper().writeValueAsString(expected);

        when(this.employeeService.findByPartialName(Optional.of("John"), Optional.of("Smith")))
            .thenReturn(expected);

        mockMvc.perform(get("/api/employee/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(mock(Employee.class))))
                .param("partialFirst", "John")
                .param("partialLast", "Smith"))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    // --- deleteById ---

    @Test
    public void testDeleteById_employeeNotFound_exceptionHandlerIntercept() throws Exception {
        final Integer test_id = 0;

        doThrow(new EmployeeNotFoundException(0)).when(this.employeeService).deleteById(any(Integer.class));

        mockMvc.perform(delete("/api/employee/{employeeId}", test_id))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteById_success() throws Exception {
        final Integer test_id = 0;

        doNothing().when(this.employeeService).deleteById(test_id);

        mockMvc.perform(delete("/api/employee/{employeeId}", test_id))
            .andExpect(status().isNoContent());
    }

    // --- getById ---

    @Test
    public void testGetById_employeeNotFound_exceptionHandlerIntercept() throws Exception {
        final Integer testId = 0;

        when(this.employeeService.findById(testId)).thenThrow(EmployeeNotFoundException.class);

        mockMvc.perform(get("/api/employee/{employeeId}", testId))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testGetById_success() throws Exception {
        final Employee expected = mock(Employee.class);
        final String expectedJson = new ObjectMapper().writeValueAsString(expected);
        final Integer testId = 0;

        when(this.employeeService.findById(testId)).thenReturn(expected);

        mockMvc.perform(get("/api/employee/{employeeId}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(Employee.class))))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }
}
