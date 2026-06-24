package com.schedulehelper.api.controller;

import java.time.OffsetDateTime;
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

import com.schedulehelper.api.entity.Shift;
import com.schedulehelper.api.service.ShiftService;

@RestController
@RequestMapping("/api/shift")
public class ShiftController {
    private final ShiftService shiftService; 

    ShiftController(final ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    // --- Create ---

    @PostMapping("")
    public ResponseEntity<Shift> createNew(@RequestBody(required = false) final Shift shift) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.shiftService.createNew(shift));
    }

    // --- Read ---

    @GetMapping("/between")
    public ResponseEntity<List<Shift>> getByStartTimeBetween(
        @RequestParam(required = false) final Optional<OffsetDateTime> start,
        @RequestParam(required = false) final Optional<OffsetDateTime> end
    ) {
        // return ResponseEntity.status(HttpStatus.OK).body(this.shiftService.findByStartTimeBetween(start, end));
        return ResponseEntity.ok(this.shiftService.findByStartTimeBetween(start, end));
    }

    @GetMapping("/{shiftId}")
    public ResponseEntity<Shift> getById(@PathVariable final Integer shiftId) {
        return ResponseEntity.ok(this.shiftService.findById(shiftId));
    }

    // --- Update ---

    @PutMapping("")
    public ResponseEntity<Shift> updateById(@RequestBody(required = false) final Shift shift) {
        return ResponseEntity.ok(this.shiftService.updateById(shift));
    }

    // --- Delete ---

    @DeleteMapping("/{shiftId}")
    public ResponseEntity<Void> deleteById(@PathVariable final Integer shiftId) {
        this.shiftService.deleteById(shiftId);
        return ResponseEntity.noContent().build();
    }
}
