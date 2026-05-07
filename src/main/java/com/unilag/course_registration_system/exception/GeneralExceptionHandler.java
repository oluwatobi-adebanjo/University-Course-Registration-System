package com.unilag.course_registration_system.exception;

import com.unilag.course_registration_system.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static com.unilag.course_registration_system.utils.ResponseCodes.INTERNAL_ERROR_MSG;

@RestControllerAdvice
@Slf4j
public class GeneralExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public Response<Void> handleNotFound(NotFoundException ex) {
        log.error("Exception occurred: {}",ex.getMessage(), ex);
       return new Response<>(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response<Void> handleException(Exception ex) {
        log.error("General Exception occurred: {}",ex.getMessage(), ex);
        return new Response<>(10, INTERNAL_ERROR_MSG);
    }
}
