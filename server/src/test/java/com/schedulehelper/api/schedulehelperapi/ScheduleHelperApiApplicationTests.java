package com.schedulehelper.api.schedulehelperapi;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

@SpringBootTest
class ScheduleHelperApiApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    void main_doesNotThrow() {
        ScheduleHelperApiApplication.main(new String[]{});
    }
}