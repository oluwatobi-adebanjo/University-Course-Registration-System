package com.unilag.course_registration_system.service;

import com.unilag.course_registration_system.dto.request.CreateStudent;
import com.unilag.course_registration_system.dto.response.Response;
import com.unilag.course_registration_system.model.StudentModel;

public interface StudentService {
    Response<Void> registerStudent(CreateStudent request);

    Response<StudentModel> getStudent(String studentId);
}
