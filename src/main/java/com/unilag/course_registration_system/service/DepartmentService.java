package com.unilag.course_registration_system.service;

import com.unilag.course_registration_system.dto.request.CreateDepartment;
import com.unilag.course_registration_system.dto.response.Response;
import com.unilag.course_registration_system.entity.Department;
import java.util.List;

public interface DepartmentService {
    Response<Void> createDepartment(CreateDepartment request);

    Response<List<Department>> fetchDepartments();
}
