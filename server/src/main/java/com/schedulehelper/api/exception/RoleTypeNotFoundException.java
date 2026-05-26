package com.schedulehelper.api.exception;

public class RoleTypeNotFoundException extends ScheduleHelperException {
    public RoleTypeNotFoundException(final Integer id) {
        super("RoleType with id " + id + " not found.");
    }

    public RoleTypeNotFoundException(final String message) {
        super(message);
    }
}
