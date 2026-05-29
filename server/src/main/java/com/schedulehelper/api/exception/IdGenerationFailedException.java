package com.schedulehelper.api.exception;

public class IdGenerationFailedException extends ScheduleHelperException {
    public IdGenerationFailedException(final String entity) {
        super(String.format("Id generation failed for {}.", entity));
    }
}
