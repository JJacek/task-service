package com.company.taskservice.infrastructure.exception;

public class MyEntityNotFoundException extends RuntimeException {

    public MyEntityNotFoundException(String message) {
        super(message);
    }

}
