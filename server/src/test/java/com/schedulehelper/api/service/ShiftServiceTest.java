package com.schedulehelper.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.schedulehelper.api.entity.Shift;
import com.schedulehelper.api.exception.IdGenerationFailedException;
import com.schedulehelper.api.exception.ShiftNotFoundException;
import com.schedulehelper.api.repository.ShiftRepository;

@ExtendWith(MockitoExtension.class)
public class ShiftServiceTest {
    @Mock
    private ShiftRepository shiftRepository;

    @InjectMocks
    private ShiftService shiftService;

    // --- findByStartTimeBetween ---

    @Test
    public void testFindByStartTimeBetween_noResults() {
        final OffsetDateTime start = mock(OffsetDateTime.class);
        final OffsetDateTime end   = mock(OffsetDateTime.class);
        final List<Shift> expected = List.of();
        when(this.shiftRepository.findByStartTimeBetween(start, end)).thenReturn(expected);

        assertEquals(expected, this.shiftService.findByStartTimeBetween(start, end));
    }

    @Test
    public void testFindByStartTimeBetween() {
        final OffsetDateTime start = mock(OffsetDateTime.class);
        final OffsetDateTime end   = mock(OffsetDateTime.class);
        final List<Shift> expected = List.of(mock(Shift.class), mock(Shift.class), mock(Shift.class));
        when(this.shiftRepository.findByStartTimeBetween(start, end)).thenReturn(expected);

        assertEquals(expected, this.shiftService.findByStartTimeBetween(start, end));
    }

    // --- findById ---

    @Test
    public void testFindById_notFound() {
        when(this.shiftRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ShiftNotFoundException.class, () -> this.shiftService.findById(99));
    }

    @Test
    public void testFindById() {
        final Shift shiftRole = mock(Shift.class);
        when(this.shiftRepository.findById(1)).thenReturn(Optional.of(shiftRole));

        final Shift result = this.shiftService.findById(1);

        assertEquals(shiftRole, result);
    }

    // --- createNew ---

    @Test
    public void testCreateNew_withId_throwsException() {
        final Shift shift = mock(Shift.class);
        when(shift.getId()).thenReturn(1);

        assertThrows(IllegalArgumentException.class, () -> this.shiftService.createNew(shift));
    }

    @Test
    public void testCreateNew_idGenerationFailed_throwsException() {
        final Shift shift = mock(Shift.class);
        when(shift.getId()).thenReturn(null);
        final Shift savedshift = mock(Shift.class);
        when(savedshift.getId()).thenReturn(null);
        when(this.shiftRepository.save(shift)).thenReturn(savedshift);

        assertThrows(IdGenerationFailedException.class, () -> this.shiftService.createNew(shift));
    }

    @Test
    public void testCreateNew() {
        final Shift shift = mock(Shift.class);
        when(shift.getId()).thenReturn(null);
        final Shift savedshift = mock(Shift.class);
        when(savedshift.getId()).thenReturn(1);
        when(this.shiftRepository.save(shift)).thenReturn(savedshift);

        assertDoesNotThrow(() -> this.shiftService.createNew(shift));
    }

    // --- deleteById ---

    @Test
    public void testDeleteById_doesNotExist_throwsException() {
        when(this.shiftRepository.existsById(1)).thenReturn(false);

        assertThrows(ShiftNotFoundException.class, () -> this.shiftService.deleteById(1));
    }

    @Test
    public void testDeleteById() {
        when(this.shiftRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> this.shiftService.deleteById(1));
    }

    // --- updateById ---

    @Test
    public void testUpdateById_nullId_throwsException() {
        final Shift shift = mock(Shift.class);
        when(shift.getId()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> this.shiftService.updateById(shift));
    }

    @Test
    public void testUpdateById_doesNotExist_throwsException() {
        final Shift shift = mock(Shift.class);
        when(shift.getId()).thenReturn(1);
        when(this.shiftRepository.existsById(1)).thenReturn(false);

        assertThrows(ShiftNotFoundException.class, () -> this.shiftService.updateById(shift));
    }

    @Test
    public void testUpdateById() {
        final Shift shift = mock(Shift.class);
        when(shift.getId()).thenReturn(1);
        when(this.shiftRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> this.shiftService.updateById(shift));
    }
}
