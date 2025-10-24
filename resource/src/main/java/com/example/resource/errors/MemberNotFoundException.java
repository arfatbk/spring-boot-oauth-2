package com.example.resource.errors;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException() {
        super(Errors.membershipNotFound.getCode(), Errors.membershipNotFound.getMessage());
    }
}
