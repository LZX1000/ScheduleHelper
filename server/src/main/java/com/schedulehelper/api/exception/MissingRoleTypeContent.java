package com.schedulehelper.api.exception;

public class MissingRoleTypeContent extends ScheduleHelperException {
    public MissingRoleTypeContent() {
        super("RoleType has no content.");
    }
}
