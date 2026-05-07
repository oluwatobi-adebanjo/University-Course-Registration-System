package com.unilag.course_registration_system.service;

import com.unilag.course_registration_system.dto.request.CreateFaculty;
import com.unilag.course_registration_system.dto.response.Response;
import com.unilag.course_registration_system.entity.Faculty;
import java.util.List;

public interface FacultyService {
    Response<Void> createFaculty(CreateFaculty faculty);

    Response<List<Faculty>> fetchAllFaculties();
}
