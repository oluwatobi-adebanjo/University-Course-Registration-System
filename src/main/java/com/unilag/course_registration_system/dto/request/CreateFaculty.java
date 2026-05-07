package com.unilag.course_registration_system.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateFaculty {
    private String facultyName;
    private String facultyCode;
}
