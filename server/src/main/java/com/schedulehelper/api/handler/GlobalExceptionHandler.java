package com.schedulehelper.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.schedulehelper.api.exception.EmployeeNotFoundException;
import com.schedulehelper.api.exception.IdGenerationFailedException;
import com.schedulehelper.api.exception.MissingEmployeeContent;
import com.schedulehelper.api.exception.RoleTypeNotFoundException;
import com.schedulehelper.api.exception.ScheduleHelperException;
import com.schedulehelper.api.exception.ShiftNotFoundException;
import com.schedulehelper.api.exception.ShiftRoleNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // --- Not Found

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EmployeeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(RoleTypeNotFoundException.class)
    public ResponseEntity<String> handleNotFound(RoleTypeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ShiftNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ShiftNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ShiftRoleNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ShiftRoleNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // --- Content

    @ExceptionHandler(MissingEmployeeContent.class)
    public ResponseEntity<String> handleMissingContent(MissingEmployeeContent e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // --- Internal

    @ExceptionHandler(IdGenerationFailedException.class)
    public ResponseEntity<String> handleGenerationFailed(IdGenerationFailedException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(ScheduleHelperException.class)
    public ResponseEntity<String> handleAppError(ScheduleHelperException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
