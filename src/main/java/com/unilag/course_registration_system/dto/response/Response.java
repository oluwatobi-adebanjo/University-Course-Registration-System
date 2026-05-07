package com.unilag.course_registration_system.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response <T>{
    private String message;
    private int code;
    private T data;

    public Response( int code,String message, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public Response(int code,String message) {
        this.message = message;
        this.code = code;
    }

}
