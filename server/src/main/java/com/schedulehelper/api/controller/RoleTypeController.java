package com.schedulehelper.api.controller;

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

import com.schedulehelper.api.entity.RoleType;
import com.schedulehelper.api.service.RoleTypeService;

@RestController
@RequestMapping("/api/role-type")
public class RoleTypeController {
    final private RoleTypeService roleTypeService;

    RoleTypeController(final RoleTypeService roleTypeService) {
        this.roleTypeService = roleTypeService;
    }
    
    // --- Create ---

    @PostMapping("")
    // required = false for custom exception in service layer
    public ResponseEntity<RoleType> createNew(@RequestBody(required = false) final RoleType roleType) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleTypeService.createNew(roleType));
    }

    // --- Read ---

    @GetMapping("/title/{title}")
    public ResponseEntity<RoleType> getByTitle(@PathVariable final String title) {
        return ResponseEntity.ok(this.roleTypeService.findByTitle(title));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RoleType>> getByPartialTitle(@RequestParam(required = false) final Optional<String> partialTitle) {
        return ResponseEntity.ok(this.roleTypeService.findByPartialTitle(partialTitle));
    }

    @GetMapping("/{roleTypeId}")
    public ResponseEntity<RoleType> getById(@PathVariable final Integer roleTypeId) {
        return ResponseEntity.ok(this.roleTypeService.findById(roleTypeId));
    }

    // --- Update ---

    @PutMapping("")
    // required = false for custom exception in service layer
    public ResponseEntity<RoleType> updateById(@RequestBody(required = false) final RoleType roleType) {
        return ResponseEntity.ok(this.roleTypeService.updateById(roleType));
    }

    // --- Delete ---

    @DeleteMapping("/{roleTypeId}")
    public ResponseEntity<Void> deleteById(@PathVariable final Integer roleTypeId) {
        this.roleTypeService.deleteById(roleTypeId);
        return ResponseEntity.noContent().build();
    }
}
