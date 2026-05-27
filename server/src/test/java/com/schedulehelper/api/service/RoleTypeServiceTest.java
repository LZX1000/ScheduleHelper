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

import com.schedulehelper.api.entity.RoleType;
import com.schedulehelper.api.exception.RoleTypeNotFoundException;
import com.schedulehelper.api.repository.RoleTypeRepository;

@ExtendWith(MockitoExtension.class)
public class RoleTypeServiceTest {
    @Mock
    private RoleTypeRepository roleTypeRepository;

    @InjectMocks
    private RoleTypeService roleTypeService;
    
    // --- findByTitle ---

    @Test
    public void testFindByTitle_notFound() {
        when(this.roleTypeRepository.findByTitle("test")).thenReturn(Optional.empty());

        assertThrows(RoleTypeNotFoundException.class, () -> this.roleTypeService.findByTitle("test"));
    }

    @Test
    public void testFindByTitle() {
        final RoleType roleType = mock(RoleType.class);
        when(this.roleTypeRepository.findByTitle("test")).thenReturn(Optional.of(roleType));

        final RoleType result = this.roleTypeService.findByTitle("test");
        assertEquals(roleType, result);
    }

    // --- findByPartialTitle ---

    @Test
    public void testFindByPartialTitle_empty() {
        final List<RoleType> expected = List.of();
        when(this.roleTypeRepository.findByPartialTitle("test")).thenReturn(expected);

        final List<RoleType> result = this.roleTypeService.findByPartialTitle("test");
        assertEquals(result, expected);
    }

    @Test
    public void testFindByPartialTitle_multipleMatches() {
        final List<RoleType> expected = List.of(mock(RoleType.class), mock(RoleType.class));
        when(this.roleTypeRepository.findByPartialTitle("test")).thenReturn(expected);

        final List<RoleType> result = this.roleTypeService.findByPartialTitle("test");
        assertEquals(expected, result);
    }

    @Test
    public void testFindByPartialTitle_singleMatch() {
        final List<RoleType> expected = List.of(mock(RoleType.class));
        when(this.roleTypeRepository.findByPartialTitle("test")).thenReturn(expected);

        final List<RoleType> result = this.roleTypeService.findByPartialTitle("test");
        assertEquals(expected, result);
    }

    // --- findById ---

    @Test
    public void testFindById_notFound() {
        when(this.roleTypeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RoleTypeNotFoundException.class, () -> this.roleTypeService.findById(99));
    }

    @Test
    public void testFindById() {
        final RoleType roleType = mock(RoleType.class);
        when(this.roleTypeRepository.findById(1)).thenReturn(Optional.of(roleType));

        final RoleType result = this.roleTypeService.findById(1);

        assertEquals(roleType, result);
    }

    // --- createNew ---

    @Test
    public void testCreateNew_withId_throwsException() {
        final RoleType roleType = mock(RoleType.class);
        when(roleType.getId()).thenReturn(1);

        assertThrows(IllegalArgumentException.class, () -> this.roleTypeService.createNew(roleType));
    }

    @Test
    public void testCreateNew() {
        final RoleType roleType = mock(RoleType.class);
        when(roleType.getId()).thenReturn(null);
        when(this.roleTypeRepository.save(roleType)).thenReturn(roleType);

        assertDoesNotThrow(() -> this.roleTypeService.createNew(roleType));
    }

    // --- deleteById ---

    @Test
    public void testDeleteById_doesNotExist_throwsException() {
        when(this.roleTypeRepository.existsById(1)).thenReturn(false);

        assertThrows(RoleTypeNotFoundException.class, () -> this.roleTypeService.deleteById(1));
    }

    @Test
    public void testDeleteById() {
        when(this.roleTypeRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> this.roleTypeService.deleteById(1));
    }

    // --- updateById ---

    @Test
    public void testUpdateById_nullId_throwsException() {
        final RoleType roleType = mock(RoleType.class);
        when(roleType.getId()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> this.roleTypeService.updateById(roleType));
    }

    @Test
    public void testUpdateById_doesNotExist_throwsException() {
        final RoleType roleType = mock(RoleType.class);
        when(roleType.getId()).thenReturn(1);
        when(this.roleTypeRepository.existsById(1)).thenReturn(false);

        assertThrows(RoleTypeNotFoundException.class, () -> this.roleTypeService.updateById(roleType));
    }

    @Test
    public void testUpdateById() {
        final RoleType roleType = mock(RoleType.class);
        when(roleType.getId()).thenReturn(1);
        when(this.roleTypeRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> this.roleTypeService.updateById(roleType));
    }
}
