package com.unilag.course_registration_system.repository;

import com.unilag.course_registration_system.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {
    boolean existsByFacultyNameOrFacultyCode(String facultyName, String facultyCode);
}
