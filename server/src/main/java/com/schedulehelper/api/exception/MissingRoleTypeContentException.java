package com.schedulehelper.api.exception;

public class MissingRoleTypeContentException extends ScheduleHelperException {
    public MissingRoleTypeContentException() {
        super("RoleType has no content.");
    }
}
