package com.schedulehelper.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
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

import com.schedulehelper.api.entity.Shift;
import com.schedulehelper.api.exception.EmployeeNotFoundException;
import com.schedulehelper.api.exception.IdGenerationFailedException;
import com.schedulehelper.api.exception.MissingRoleTypeContentException;
import com.schedulehelper.api.exception.ShiftNotFoundException;
import com.schedulehelper.api.service.ShiftService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(ShiftController.class)
public class ShiftControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ShiftService shiftService;
    
    // --- Create ---

    // -- createNew --

    @Test
    public void testCreateNew_illegalArgument_exceptionHandlerIntercept() throws Exception {
        when(this.shiftService.createNew(any(Shift.class)))
            .thenThrow(new IllegalArgumentException("New shift must not have an ID."));

        mockMvc.perform(post("/api/shift")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(Shift.class))))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateNew_missingShiftContent_exceptionHandlerIntercept() throws Exception {
        when (this.shiftService.createNew(null))
            .thenThrow(new MissingRoleTypeContentException());

        mockMvc.perform(post("/api/shift"))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testCreateNew_idGenerationFailed_exceptionHandlerIntercept() throws Exception {
        when(this.shiftService.createNew(any(Shift.class)))
            .thenThrow(new IdGenerationFailedException("Id generation failed for new shift."));

        mockMvc.perform(post("/api/shift")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(Shift.class))))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateNew_success() throws Exception {
        final Shift shift = mock(Shift.class);
        final String shiftJson = this.objectMapper.writeValueAsString(shift);

        when(this.shiftService.createNew(any(Shift.class))).thenReturn(shift);

        mockMvc.perform(post("/api/shift")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shift)))
            .andExpect(status().isCreated())
            .andExpect(content().json(shiftJson));
    }

    // --- Read ---

    // -- getByStartTimeBetween --

    @Test
    public void testGetByStartTimeBetween_success() throws Exception {
        final List<Shift> expected = List.of(mock(Shift.class));
        final OffsetDateTime start = OffsetDateTime.now();
        final OffsetDateTime end = OffsetDateTime.now().plusHours(8);

        when(this.shiftService.findByStartTimeBetween(Optional.of(start), Optional.of(end)))
            .thenReturn(expected);

        mockMvc.perform(get("/api/shift/between")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(List.of(mock(Shift.class))))
                .param("start", start.toString())
                .param("end", end.toString()))
            .andExpect(status().isOk())
            .andExpect(content().json(this.objectMapper.writeValueAsString(expected)));
    }

    // -- getById --

    @Test
    public void testGetById_shiftNotFound_exceptionHandlerIntercept() throws Exception {
        final Integer testId = 0;

        when(this.shiftService.findById(testId)).thenThrow(ShiftNotFoundException.class);

        mockMvc.perform(get("/api/shift/{shiftId}", testId))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testGetById_success() throws Exception {
        final Shift expected = mock(Shift.class);
        final Integer testId = 0;

        when(this.shiftService.findById(testId)).thenReturn(expected);

        mockMvc.perform(get("/api/shift/{shiftId}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(Shift.class))))
            .andExpect(status().isOk())
            .andExpect(content().json(this.objectMapper.writeValueAsString(expected)));
    }

    // --- Update ---

    // -- updateById --

    @Test
    public void testUpdateById_missingShiftContent_exceptionHandlerIntercept() throws Exception {
        when(this.shiftService.updateById(null))
            .thenThrow(new MissingRoleTypeContentException());

        mockMvc.perform(put("/api/shift"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateById_illegalArgument_exceptionHandlerIntercept() throws Exception {
        when(this.shiftService.updateById(any(Shift.class)))
            .thenThrow(new IllegalArgumentException("Shift must have an ID to be updated."));

        mockMvc.perform(put("/api/shift")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mock(Shift.class))))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateById_shiftNotFound_exceptionHandlerIntercept() throws Exception {
        when(this.shiftService.updateById(any(Shift.class)))
            .thenThrow(new ShiftNotFoundException(0));

        mockMvc.perform(put("/api/shift")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mock(Shift.class))))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateById_success() throws Exception {
        final Shift shift = mock(Shift.class);

        when(this.shiftService.updateById(any(Shift.class))).thenReturn(shift);

        mockMvc.perform(put("/api/shift")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(shift)))
            .andExpect(status().isOk())
            .andExpect(content().json(this.objectMapper.writeValueAsString(shift)));
    }

    // --- Delete ---

    // -- deleteById --

    @Test
    public void testDeleteById_shiftNotFound_exceptionHandlerIntercept() throws Exception {
        final Integer testId = 0;

        doThrow(new EmployeeNotFoundException(0)).when(this.shiftService).deleteById(any(Integer.class));

        mockMvc.perform(delete("/api/shift/{shiftId}", testId))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteById_success() throws Exception {
        final Integer testId = 0;

        doNothing().when(this.shiftService).deleteById(testId);

        mockMvc.perform(delete("/api/shift/{shiftId}", testId))
            .andExpect(status().isNoContent());
    }
}
