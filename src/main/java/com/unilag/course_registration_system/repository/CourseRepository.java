package com.unilag.course_registration_system.repository;

import com.unilag.course_registration_system.entity.Course;
import com.unilag.course_registration_system.entity.Department;
import com.unilag.course_registration_system.model.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course> findByLevelAndDepartment(String level, Department department);
    List<CourseModel> findByDepartmentAndLevel(Department department, String courseCode);
}
