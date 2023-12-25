package com.company.taskservice.domain.task.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void buildTaskIsValid() {
        Task.builder().input("ABCD").pattern("BCD").build();
    }
    @Test
    void buildTaskThrowsNullPointerException_nullInput() {
        assertThrows(NullPointerException.class, () -> {
            Task.builder().pattern("BCD").build();
        });
    }

    @Test
    void buildTaskThrowsNullPointerException_nullPattern() {
        assertThrows(NullPointerException.class, () -> {
            Task.builder().input("ABCD").build();
        });
    }

}