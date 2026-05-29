package com.schedulehelper.api.exception;

public class ShiftNotFoundException extends ScheduleHelperException {
    public ShiftNotFoundException(final Integer id) {
        super("Shift with id " + id + " not found.");
    }
}
