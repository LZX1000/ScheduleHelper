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
import com.schedulehelper.api.exception.MissingShiftContentException;
import com.schedulehelper.api.exception.ShiftNotFoundException;
import com.schedulehelper.api.repository.ShiftRepository;

@ExtendWith(MockitoExtension.class)
class ShiftServiceTest {
    @Mock
    private ShiftRepository shiftRepository;

    @InjectMocks
    private ShiftService shiftService;

    // --- findByStartTimeBetween ---

    @Test
    void testFindByStartTimeBetween_noResults() {
        final Optional<OffsetDateTime> start = Optional.of(mock(OffsetDateTime.class));
        final Optional<OffsetDateTime> end   = Optional.of(mock(OffsetDateTime.class));
        final List<Shift> expected = List.of();

        when(this.shiftRepository.findByStartTimeBetween(start.get(), end.get())).thenReturn(expected);

        assertEquals(expected, this.shiftService.findByStartTimeBetween(start, end));
    }

    @Test
    void testFindByStartTimeBetween() {
        final Optional<OffsetDateTime> start = Optional.of(mock(OffsetDateTime.class));
        final Optional<OffsetDateTime> end   = Optional.of(mock(OffsetDateTime.class));
        final List<Shift> expected = List.of(mock(Shift.class), mock(Shift.class), mock(Shift.class));

        when(this.shiftRepository.findByStartTimeBetween(start.get(), end.get())).thenReturn(expected);

        assertEquals(expected, this.shiftService.findByStartTimeBetween(start, end));
    }

    // --- findById ---

    @Test
    void testFindById_notFound() {
        when(this.shiftRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ShiftNotFoundException.class, () -> this.shiftService.findById(99));
    }

    @Test
    void testFindById() {
        final Shift shiftRole = mock(Shift.class);

        when(this.shiftRepository.findById(1)).thenReturn(Optional.of(shiftRole));

        final Shift result = this.shiftService.findById(1);

        assertEquals(shiftRole, result);
    }

    // --- createNew ---

    @Test
    void testCreateNew_withId_throwsException() {
        final Shift shift = mock(Shift.class);

        when(shift.getId()).thenReturn(1);

        assertThrows(IllegalArgumentException.class, () -> this.shiftService.createNew(shift));
    }

    @Test
    void testCreateNew_missingShiftContent_throwsException() {
        assertThrows(MissingShiftContentException.class, () -> this.shiftService.createNew(null));
    }

    @Test
    void testCreateNew_idGenerationFailed_throwsException() {
        final Shift shift = mock(Shift.class);
        final Shift savedshift = mock(Shift.class);

        when(shift.getId()).thenReturn(null);
        when(savedshift.getId()).thenReturn(null);
        when(this.shiftRepository.save(shift)).thenReturn(savedshift);

        assertThrows(IdGenerationFailedException.class, () -> this.shiftService.createNew(shift));
    }

    @Test
    void testCreateNew() {
        final Shift shift = mock(Shift.class);
        final Shift savedshift = mock(Shift.class);

        when(shift.getId()).thenReturn(null);
        when(savedshift.getId()).thenReturn(1);
        when(this.shiftRepository.save(shift)).thenReturn(savedshift);

        assertEquals(savedshift, this.shiftService.createNew(shift));
    }

    // --- deleteById ---

    @Test
    void testDeleteById_doesNotExist_throwsException() {
        when(this.shiftRepository.existsById(1)).thenReturn(false);

        assertThrows(ShiftNotFoundException.class, () -> this.shiftService.deleteById(1));
    }

    @Test
    void testDeleteById() {
        when(this.shiftRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> this.shiftService.deleteById(1));
    }

    // --- updateById ---

    @Test
    void testUpdateById_nullId_throwsException() {
        final Shift shift = mock(Shift.class);

        when(shift.getId()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> this.shiftService.updateById(shift));
    }

    @Test
    void testUpdateById_missingShiftContent_throwsException() {
        assertThrows(MissingShiftContentException.class, () -> this.shiftService.updateById(null));
    }

    @Test
    void testUpdateById_doesNotExist_throwsException() {
        final Shift shift = mock(Shift.class);

        when(shift.getId()).thenReturn(1);
        when(this.shiftRepository.existsById(1)).thenReturn(false);

        assertThrows(ShiftNotFoundException.class, () -> this.shiftService.updateById(shift));
    }

    @Test
    void testUpdateById() {
        final Shift shift = mock(Shift.class);

        when(shift.getId()).thenReturn(1);
        when(this.shiftRepository.existsById(1)).thenReturn(true);
        when(this.shiftRepository.save(shift)).thenReturn(shift);

        assertEquals(shift, this.shiftService.updateById(shift));
    }
}
