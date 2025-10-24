package com.example.resource.errors;

public class NotFoundException extends RuntimeException {

    private final String code;
    private final String message;
    public NotFoundException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    public String getCode() {
        return code;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
