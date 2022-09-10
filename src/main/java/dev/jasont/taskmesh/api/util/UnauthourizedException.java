package dev.jasont.taskmesh.api.util;

public class UnauthourizedException extends Exception {
    public UnauthourizedException(String errorMessage) {
        super(errorMessage);
    }

    public UnauthourizedException() {
        super();
    }
}
