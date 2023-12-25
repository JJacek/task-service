package com.company.taskservice.util;

public final class Preconditions {

    private Preconditions() {
    }

    public static void greaterOrEqualsThen(int input, int target) {
        if (input < target) {
            throw new IllegalArgumentException(input + " must be greater or equal then " + target);
        }
    }


}
