package com.schedulehelper.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.schedulehelper.api.entity.ShiftRole;
import com.schedulehelper.api.exception.IdGenerationFailedException;
import com.schedulehelper.api.exception.ShiftRoleNotFoundException;
import com.schedulehelper.api.repository.ShiftRoleRepository;

@ExtendWith(MockitoExtension.class)
public class ShiftRoleServiceTest {
    @Mock
    private ShiftRoleRepository shiftRoleRepository;

    @InjectMocks
    private ShiftRoleService shiftRoleService;

    // --- findById ---

    @Test
    public void testFindById_notFound() {
        when(this.shiftRoleRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ShiftRoleNotFoundException.class, () -> this.shiftRoleService.findById(99));
    }

    @Test
    public void testFindById() {
        final ShiftRole shiftRole = mock(ShiftRole.class);
        when(this.shiftRoleRepository.findById(1)).thenReturn(Optional.of(shiftRole));

        final ShiftRole result = this.shiftRoleService.findById(1);

        assertEquals(shiftRole, result);
    }

    // --- createNew ---

    @Test
    public void testCreateNew_withId_throwsException() {
        final ShiftRole shiftRole = mock(ShiftRole.class);
        when(shiftRole.getId()).thenReturn(1);

        assertThrows(IllegalArgumentException.class, () -> this.shiftRoleService.createNew(shiftRole));
    }

    @Test
    public void testCreateNew_idGenerationFailed_throwsException() {
        final ShiftRole shiftRole = mock(ShiftRole.class);
        when(shiftRole.getId()).thenReturn(null);
        final ShiftRole savedShiftRole = mock(ShiftRole.class);
        when(savedShiftRole.getId()).thenReturn(null);
        when(this.shiftRoleRepository.save(shiftRole)).thenReturn(savedShiftRole);

        assertThrows(IdGenerationFailedException.class, () -> this.shiftRoleService.createNew(shiftRole));
    }
    
    @Test
    public void testCreateNew() {
        final ShiftRole shiftRole = mock(ShiftRole.class);
        when(shiftRole.getId()).thenReturn(null);
        final ShiftRole savedShiftRole = mock(ShiftRole.class);
        when(savedShiftRole.getId()).thenReturn(1);
        when(this.shiftRoleRepository.save(shiftRole)).thenReturn(savedShiftRole);

        assertEquals(savedShiftRole, this.shiftRoleService.createNew(shiftRole));
    }

    // --- deleteById ---

    @Test
    public void testDeleteById_doesNotExist_throwsException() {
        when(this.shiftRoleRepository.existsById(1)).thenReturn(false);

        assertThrows(ShiftRoleNotFoundException.class, () -> this.shiftRoleService.deleteById(1));
    }

    @Test
    public void testDeleteById() {
        when(this.shiftRoleRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> this.shiftRoleService.deleteById(1));
    }

    // --- updateById ---

    @Test
    public void testUpdateById_nullId_throwsException() {
        final ShiftRole shiftRole = mock(ShiftRole.class);
        when(shiftRole.getId()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> this.shiftRoleService.updateById(shiftRole));
    }

    @Test
    public void testUpdateById_doesNotExist_throwsException() {
        final ShiftRole shiftRole = mock(ShiftRole.class);
        when(shiftRole.getId()).thenReturn(1);
        when(this.shiftRoleRepository.existsById(1)).thenReturn(false);

        assertThrows(ShiftRoleNotFoundException.class, () -> this.shiftRoleService.updateById(shiftRole));
    }

    @Test
    public void testUpdateById() {
        final ShiftRole shiftRole = mock(ShiftRole.class);
        when(shiftRole.getId()).thenReturn(1);
        when(this.shiftRoleRepository.existsById(1)).thenReturn(true);
        when(this.shiftRoleRepository.save(shiftRole)).thenReturn(shiftRole);

        assertEquals(shiftRole, this.shiftRoleService.updateById(shiftRole));
    }
}
