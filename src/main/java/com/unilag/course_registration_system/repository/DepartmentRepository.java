package com.unilag.course_registration_system.repository;

import com.unilag.course_registration_system.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    boolean existsByDepartmentNameOrDepartmentCode(String departmentName, String departmentCode);
}
