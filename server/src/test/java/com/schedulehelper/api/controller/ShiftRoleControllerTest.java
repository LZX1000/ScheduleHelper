package com.schedulehelper.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.schedulehelper.api.entity.ShiftRole;
import com.schedulehelper.api.exception.EmployeeNotFoundException;
import com.schedulehelper.api.exception.IdGenerationFailedException;
import com.schedulehelper.api.exception.MissingShiftRoleContentException;
import com.schedulehelper.api.exception.RoleTypeNotFoundException;
import com.schedulehelper.api.exception.ShiftRoleNotFoundException;
import com.schedulehelper.api.service.ShiftRoleService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(ShiftRoleController.class)
public class ShiftRoleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ShiftRoleService shiftRoleService;

    // --- Create ---

    // -- createNew --

    @Test
    public void testCreateNew_illegalArgument_exceptionHandlerIntercept() throws Exception {
        when(this.shiftRoleService.createNew(any(ShiftRole.class)))
            .thenThrow(new IllegalArgumentException("New shift role must not have an ID."));

        mockMvc.perform(post("/api/shift-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(ShiftRole.class))))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateNew_missingShiftRoleContent_exceptionHandlerIntercept() throws Exception {
        when (this.shiftRoleService.createNew(null))
            .thenThrow(new MissingShiftRoleContentException());

        mockMvc.perform(post("/api/shift-role"))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testCreateNew_idGenerationFailed_exceptionHandlerIntercept() throws Exception {
        when(this.shiftRoleService.createNew(any(ShiftRole.class)))
            .thenThrow(new IdGenerationFailedException("Id generation failed for new shiftRole."));

        mockMvc.perform(post("/api/shift-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(ShiftRole.class))))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateNew_success() throws Exception {
        final ShiftRole shiftRole = mock(ShiftRole.class);
        final String roleTypeJson = this.objectMapper.writeValueAsString(shiftRole);

        when(this.shiftRoleService.createNew(any(ShiftRole.class))).thenReturn(shiftRole);

        mockMvc.perform(post("/api/shift-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shiftRole)))
            .andExpect(status().isCreated())
            .andExpect(content().json(roleTypeJson));
    }

    // --- Read ---

    // -- getById --

    @Test
    public void testGetById_shiftRoleNotFound_exceptionHandlerIntercept() throws Exception {
        final Integer testId = 0;

        when(this.shiftRoleService.findById(testId)).thenThrow(ShiftRoleNotFoundException.class);

        mockMvc.perform(get("/api/shift-role/{shiftRoleId}", testId))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testGetById_success() throws Exception {
        final ShiftRole expected = mock(ShiftRole.class);
        final String expectedJson = new ObjectMapper().writeValueAsString(expected);
        final Integer testId = 0;

        when(this.shiftRoleService.findById(testId)).thenReturn(expected);

        mockMvc.perform(get("/api/shift-role/{shiftRoleId}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(ShiftRole.class))))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    // --- Update ---

    // -- updateById --

    @Test
    public void testUpdateById_missingShiftRoleContent_exceptionHandlerIntercept() throws Exception {
        when(this.shiftRoleService.updateById(null))
            .thenThrow(new MissingShiftRoleContentException());

        mockMvc.perform(put("/api/shift-role"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateById_illegalArgument_exceptionHandlerIntercept() throws Exception {
        when(this.shiftRoleService.updateById(any(ShiftRole.class)))
            .thenThrow(new IllegalArgumentException("ShiftRole must have an ID to be updated."));

        mockMvc.perform(put("/api/shift-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mock(ShiftRole.class))))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateById_shiftRoleNotFound_exceptionHandlerIntercept() throws Exception {
        when(this.shiftRoleService.updateById(any(ShiftRole.class)))
            .thenThrow(new RoleTypeNotFoundException(0));

        mockMvc.perform(put("/api/shift-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mock(ShiftRole.class))))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateById_success() throws Exception {
        final ShiftRole shiftRole = mock(ShiftRole.class);
        final String shiftRoleJson = this.objectMapper.writeValueAsString(shiftRole);

        when(this.shiftRoleService.updateById(any(ShiftRole.class))).thenReturn(shiftRole);

        mockMvc.perform(put("/api/shift-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(shiftRole)))
            .andExpect(status().isOk())
            .andExpect(content().json(shiftRoleJson));
    }

    // --- Delete ---

    // -- deleteById --

    @Test
    public void testDeleteById_shiftRoleNotFound_exceptionHandlerIntercept() throws Exception {
        final Integer testId = 0;

        doThrow(new EmployeeNotFoundException(0)).when(this.shiftRoleService).deleteById(any(Integer.class));

        mockMvc.perform(delete("/api/shift-role/{shiftRoleId}", testId))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteById_success() throws Exception {
        final Integer testId = 0;

        doNothing().when(this.shiftRoleService).deleteById(testId);

        mockMvc.perform(delete("/api/shift-role/{shiftRoleId}", testId))
            .andExpect(status().isNoContent());
    }
}
