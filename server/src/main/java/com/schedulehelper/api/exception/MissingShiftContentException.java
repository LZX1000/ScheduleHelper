package com.schedulehelper.api.exception;

public class MissingShiftContentException extends ScheduleHelperException {
    public MissingShiftContentException() {
        super("Shift has no content.");
    }
}
