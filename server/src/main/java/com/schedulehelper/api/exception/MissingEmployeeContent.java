package com.schedulehelper.api.exception;

public class MissingEmployeeContent extends ScheduleHelperException {
    public MissingEmployeeContent() {
        super("Employee has no content.");
    }
}
