package com.schedulehelper.api.exception;

public class EmployeeNotFoundException extends ScheduleHelperException {
    public EmployeeNotFoundException(final Integer id) {
        super("Employee with id " + id + " not found.");
    }

    public EmployeeNotFoundException(final String message) {
        super(message);
    }
}
