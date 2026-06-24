package com.schedulehelper.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schedulehelper.api.entity.ShiftRole;
import com.schedulehelper.api.service.ShiftRoleService;

@RestController
@RequestMapping("/api/shift-role")
public class ShiftRoleController {
    private final ShiftRoleService shiftRoleService;

    ShiftRoleController(final ShiftRoleService shiftRoleService) {
        this.shiftRoleService = shiftRoleService;
    }

    // --- Create ---

    @PostMapping("")
    public ResponseEntity<ShiftRole> createNew(@RequestBody(required = false) final ShiftRole shiftRole) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.shiftRoleService.createNew(shiftRole));
    }

    // --- Read ---

    @GetMapping("/{shiftRoleId}")
    public ResponseEntity<ShiftRole> getById(@PathVariable final Integer shiftRoleId) {
        return ResponseEntity.ok(this.shiftRoleService.findById(shiftRoleId));
    }

    // --- Update ---

    @PutMapping("")
    public ResponseEntity<ShiftRole> updateById(@RequestBody(required = false) final ShiftRole shiftRole) {
        return ResponseEntity.ok(this.shiftRoleService.updateById(shiftRole));
    }

    // --- Delete

    @DeleteMapping("/{shiftRoleId}")
    public ResponseEntity<Void> deleteById(@PathVariable final Integer shiftRoleId) {
        this.shiftRoleService.deleteById(shiftRoleId);
        return ResponseEntity.noContent().build();
    }
}
