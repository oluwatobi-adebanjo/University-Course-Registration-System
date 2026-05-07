package com.unilag.course_registration_system.service.impl;

import com.unilag.course_registration_system.dto.request.CreateDepartment;
import com.unilag.course_registration_system.dto.response.Response;
import com.unilag.course_registration_system.entity.Department;
import com.unilag.course_registration_system.entity.Faculty;
import com.unilag.course_registration_system.exception.NotFoundException;
import com.unilag.course_registration_system.repository.DepartmentRepository;
import com.unilag.course_registration_system.repository.FacultyRepository;
import com.unilag.course_registration_system.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.unilag.course_registration_system.utils.ResponseCodes.GENERAL_ERROR_CODE;
import static com.unilag.course_registration_system.utils.ResponseCodes.GENERAL_SUCCESS_CODE;
import static com.unilag.course_registration_system.utils.ResponseCodes.VALIDATION_FAILED_CODE;

@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;


    @Override
    public Response<Void>
    createDepartment(CreateDepartment request) {
        try {
            if (request.getDepartmentName() == null || request.getDepartmentName().isEmpty()) {
                return new Response<>(VALIDATION_FAILED_CODE, "Department name is required");
            }
            if (request.getDepartmentCode() == null || request.getDepartmentCode().isEmpty()) {
                return new Response<>(VALIDATION_FAILED_CODE, "Department code is required");
            }
            if (departmentRepository.existsByDepartmentNameOrDepartmentCode(request.getDepartmentName(), request.getDepartmentCode())) {
                return new Response<>(VALIDATION_FAILED_CODE, "Department already exists");
            }
            Faculty faculty = facultyRepository.findById(request.getFacultyId()).orElseThrow(() -> new NotFoundException("Faculty does not exist"));
            Department department = new Department(request.getDepartmentName(), request.getDepartmentCode(), faculty);
            departmentRepository.save(department);
            System.out.println("Department created successfully");
            return new Response<>(GENERAL_SUCCESS_CODE, "Department created successfully");
        }catch (Exception e){
            System.out.println("Department creation failed with exception: "+ e.getMessage());
            return new Response<>(GENERAL_ERROR_CODE, "Department creation failed");
        }
    }

    @Override
    public Response<List<Department>> fetchDepartments() {
        try {
            List<Department> departments = departmentRepository.findAll();
            System.out.println("Department list Size: " + departments.size());
            System.out.println("Department retrieved successfully");
            return new Response<>(GENERAL_SUCCESS_CODE, "Department fetch successfully", departments);
        }catch (Exception e){
            System.out.println("Department retrieval failed with exception: "+ e.getMessage());
            return new Response<>(GENERAL_ERROR_CODE, "Department retrieval failed");
        }
    }
}
