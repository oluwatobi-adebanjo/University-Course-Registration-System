package com.unilag.course_registration_system.exception;

import lombok.Getter;
import static com.unilag.course_registration_system.utils.ResponseCodes.VALIDATION_FAILED_CODE;

@Getter
public class NotFoundException extends RuntimeException{
    private final int errorCode;

    public NotFoundException(String message) {
        super(message);
        this.errorCode = VALIDATION_FAILED_CODE;
    }
}
