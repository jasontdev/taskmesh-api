package dev.jasont.taskmesh.api.util;

public class UnauthorizedException extends Exception {
    public UnauthorizedException(String errorMessage) {
        super(errorMessage);
    }
}