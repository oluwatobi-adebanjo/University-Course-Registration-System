package com.unilag.course_registration_system.service;

import com.unilag.course_registration_system.dto.request.CreateCourse;
import com.unilag.course_registration_system.dto.response.Response;
import com.unilag.course_registration_system.model.CourseModel;
import java.util.List;

public interface CourseService {
    Response<Void> createCourse(CreateCourse request);
    Response<List<CourseModel>> fetchCourses();
}
