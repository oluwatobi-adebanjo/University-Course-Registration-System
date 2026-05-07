package com.unilag.course_registration_system.service.impl;

import com.unilag.course_registration_system.dto.request.CreateFaculty;
import com.unilag.course_registration_system.dto.response.Response;
import com.unilag.course_registration_system.entity.Faculty;
import com.unilag.course_registration_system.repository.FacultyRepository;
import com.unilag.course_registration_system.service.FacultyService;
import com.unilag.course_registration_system.utils.ResponseCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.unilag.course_registration_system.utils.ResponseCodes.GENERAL_SUCCESS_CODE;
import static com.unilag.course_registration_system.utils.ResponseCodes.VALIDATION_FAILED_CODE;

@RequiredArgsConstructor
@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    @Override
    public Response<Void> createFaculty(CreateFaculty faculty) {
        if (faculty.getFacultyName() == null || faculty.getFacultyName().isEmpty()) {
            return new Response<>(VALIDATION_FAILED_CODE, "Faculty name is required");
        }
        if (faculty.getFacultyCode() == null || faculty.getFacultyCode().isEmpty()) {
            return new Response<>(VALIDATION_FAILED_CODE, "Faculty code is required");
        }
        if (facultyRepository.existsByFacultyNameOrFacultyCode(faculty.getFacultyName(), faculty.getFacultyCode())) {
            return new Response<>(VALIDATION_FAILED_CODE, "Faculty already exists");
        }
        Faculty facultyObj = new Faculty(faculty.getFacultyName(),faculty.getFacultyCode());
        facultyRepository.save(facultyObj);

        return new Response<>(GENERAL_SUCCESS_CODE, "Faculty created successfully");
    }

    @Override
    public Response<List<Faculty>> fetchAllFaculties() {
        List<Faculty> faculties = facultyRepository.findAll();
        return new Response<>(GENERAL_SUCCESS_CODE, "Faculties retrieved successfully", faculties);
    }
}
