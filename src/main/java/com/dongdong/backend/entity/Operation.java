package com.dongdong.backend.entity;

public enum Operation {
    DELETE(0),
    ADD(1);

    private final int value;

    Operation(int value) {
        this.value = value;
    }

    public static Operation valueOf(int value) {
        return switch (value) {
            case 0 -> DELETE;
            case 1 -> ADD;
            default -> null;
        };
    }

    public int value() {
        return this.value;
    }
}
