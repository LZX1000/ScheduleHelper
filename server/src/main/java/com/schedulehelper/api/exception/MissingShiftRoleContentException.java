package com.schedulehelper.api.exception;

public class MissingShiftRoleContentException extends ScheduleHelperException {
    public MissingShiftRoleContentException() {
        super("ShiftRole has no content.");
    }
}
