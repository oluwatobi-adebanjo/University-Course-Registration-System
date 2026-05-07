package com.unilag.course_registration_system.controller;

import com.unilag.course_registration_system.dto.request.CreateStudent;
import com.unilag.course_registration_system.dto.response.Response;
import com.unilag.course_registration_system.model.StudentModel;
import com.unilag.course_registration_system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping("register")
    public Response<Void> registerStudent(@RequestBody CreateStudent request) {
        return studentService.registerStudent(request);
    }

    @GetMapping("/{studentId}")
    public Response<StudentModel> getStudent(@PathVariable("studentId") String studentId) {
        return studentService.getStudent(studentId);
    }
}
