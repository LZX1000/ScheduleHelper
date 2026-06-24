package com.schedulehelper.api.exception;

public class MissingEmployeeContentException extends ScheduleHelperException {
    public MissingEmployeeContentException() {
        super("Employee has no content.");
    }
}
