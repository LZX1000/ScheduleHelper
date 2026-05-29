package com.schedulehelper.api;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

@SpringBootTest
class ScheduleHelperApiApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    void main_doesNotThrow() {
        assertDoesNotThrow(() -> ScheduleHelperApiApplication.main(new String[]{}));
    }
}