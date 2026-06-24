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

import com.schedulehelper.api.entity.RoleType;
import com.schedulehelper.api.exception.EmployeeNotFoundException;
import com.schedulehelper.api.exception.IdGenerationFailedException;
import com.schedulehelper.api.exception.MissingRoleTypeContentException;
import com.schedulehelper.api.exception.RoleTypeNotFoundException;
import com.schedulehelper.api.service.RoleTypeService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(RoleTypeController.class)
class RoleTypeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoleTypeService roleTypeService;

    // --- Create ---

    // -- createNew --

    @Test
    void testCreateNew_illegalArgument_exceptionHandlerIntercept() throws Exception {
        when(this.roleTypeService.createNew(any(RoleType.class)))
            .thenThrow(new IllegalArgumentException("New shift role must not have an ID."));

        mockMvc.perform(post("/api/role-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(RoleType.class))))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateNew_missingRoleTypeContent_exceptionHandlerIntercept() throws Exception {
        when (this.roleTypeService.createNew(null))
            .thenThrow(new MissingRoleTypeContentException());

        mockMvc.perform(post("/api/role-type"))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCreateNew_idGenerationFailed_exceptionHandlerIntercept() throws Exception {
        when(this.roleTypeService.createNew(any(RoleType.class)))
            .thenThrow(new IdGenerationFailedException("Id generation failed for new roleType."));

        mockMvc.perform(post("/api/role-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(RoleType.class))))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateNew_success() throws Exception {
        final RoleType roleType = mock(RoleType.class);
        final String roleTypeJson = this.objectMapper.writeValueAsString(roleType);

        when(this.roleTypeService.createNew(any(RoleType.class))).thenReturn(roleType);

        mockMvc.perform(post("/api/role-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleType)))
            .andExpect(status().isCreated())
            .andExpect(content().json(roleTypeJson));
    }

    // --- Read ---

    // -- getByTitle --

    @Test
    void testGetByTitle_roleTypeNotFound() throws Exception {
        final String testTitle = "test";

        when(this.roleTypeService.findByTitle(testTitle)).thenThrow(RoleTypeNotFoundException.class);

        mockMvc.perform(get("/api/role-type/title/{title}", testTitle))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetByTitle_success() throws Exception {
        final String testTitle = "test";
        final RoleType expected = mock(RoleType.class);
        final String expectedJson = this.objectMapper.writeValueAsString(expected);

        when(this.roleTypeService.findByTitle(testTitle)).thenReturn(expected);

        mockMvc.perform(get("/api/role-type/title/{title}", testTitle))
            .andExpect(content().json(expectedJson));
    }

    // -- getByPartialTitle --

    @Test
    void testFindByPartialTitle_nullSearch_success() throws Exception {
        final List<RoleType> expected = List.of(mock(RoleType.class));
        final String expectedJson = new ObjectMapper().writeValueAsString(expected);

        when(this.roleTypeService.findByPartialTitle(Optional.empty()))
            .thenReturn(expected);

        mockMvc.perform(get("/api/role-type/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(List.of(mock(RoleType.class)))))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    @Test
    void testFindByPartialTitle_success() throws Exception {
        final String testPartialTitle = "test";
        final List<RoleType> expected = List.of(mock(RoleType.class));
        final String expectedJson = new ObjectMapper().writeValueAsString(expected);

        when(this.roleTypeService.findByPartialTitle(Optional.of(testPartialTitle)))
            .thenReturn(expected);

        mockMvc.perform(get("/api/role-type/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(List.of(mock(RoleType.class))))
                .param("partialTitle", testPartialTitle))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    // -- getById --

    @Test
    void testGetById_roleTypeNotFound_exceptionHandlerIntercept() throws Exception {
        final Integer testId = 0;

        when(this.roleTypeService.findById(testId)).thenThrow(RoleTypeNotFoundException.class);

        mockMvc.perform(get("/api/role-type/{roleTypeId}", testId))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetById_success() throws Exception {
        final RoleType expected = mock(RoleType.class);
        final String expectedJson = new ObjectMapper().writeValueAsString(expected);
        final Integer testId = 0;

        when(this.roleTypeService.findById(testId)).thenReturn(expected);

        mockMvc.perform(get("/api/role-type/{roleTypeId}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock(RoleType.class))))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    // --- Update ---

    // -- updateById --

    @Test
    void testUpdateById_missingRoleTypeContent_exceptionHandlerIntercept() throws Exception {
        when(this.roleTypeService.updateById(null))
            .thenThrow(new MissingRoleTypeContentException());

        mockMvc.perform(put("/api/role-type"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateById_illegalArgument_exceptionHandlerIntercept() throws Exception {
        when(this.roleTypeService.updateById(any(RoleType.class)))
            .thenThrow(new IllegalArgumentException("RoleType must have an ID to be updated."));

        mockMvc.perform(put("/api/role-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mock(RoleType.class))))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateById_roleTypeNotFound_exceptionHandlerIntercept() throws Exception {
        when(this.roleTypeService.updateById(any(RoleType.class)))
            .thenThrow(new RoleTypeNotFoundException(0));

        mockMvc.perform(put("/api/role-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mock(RoleType.class))))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateById_success() throws Exception {
        final RoleType roleType = mock(RoleType.class);
        final String roleTypeJson = this.objectMapper.writeValueAsString(roleType);

        when(this.roleTypeService.updateById(any(RoleType.class))).thenReturn(roleType);

        mockMvc.perform(put("/api/role-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(roleType)))
            .andExpect(status().isOk())
            .andExpect(content().json(roleTypeJson));
    }

    // --- Delete ---

    // -- deleteById --

    @Test
    void testDeleteById_roleTypeNotFound_exceptionHandlerIntercept() throws Exception {
        final Integer testId = 0;

        doThrow(new EmployeeNotFoundException(0)).when(this.roleTypeService).deleteById(any(Integer.class));

        mockMvc.perform(delete("/api/role-type/{roleTypeId}", testId))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteById_success() throws Exception {
        final Integer testId = 0;

        doNothing().when(this.roleTypeService).deleteById(testId);

        mockMvc.perform(delete("/api/role-type/{roleTypeId}", testId))
            .andExpect(status().isNoContent());
    }
}
