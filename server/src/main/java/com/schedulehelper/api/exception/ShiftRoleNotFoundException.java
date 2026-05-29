package com.schedulehelper.api.exception;

public class ShiftRoleNotFoundException extends ScheduleHelperException {
    public ShiftRoleNotFoundException(final Integer id) {
        super("Shift with id " + id + " not found.");
    }
}
