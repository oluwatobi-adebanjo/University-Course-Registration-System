package com.unilag.course_registration_system.dto.request;

import com.unilag.course_registration_system.entity.Department;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CreateDepartment {
    private String departmentName;
    private String departmentCode;
    private Long facultyId;
}
