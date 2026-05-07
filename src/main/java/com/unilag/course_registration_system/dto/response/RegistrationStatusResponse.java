package com.unilag.course_registration_system.dto.response;

import com.unilag.course_registration_system.model.RegistrationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationStatusResponse {
    private String summary;
    private String message;
    private RegistrationStatus status;
    private boolean registered;
    private String semesterName;
    private String academicSession;


}
