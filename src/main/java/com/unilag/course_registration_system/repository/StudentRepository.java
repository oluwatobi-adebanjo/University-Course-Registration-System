package com.unilag.course_registration_system.repository;

import com.unilag.course_registration_system.entity.Student;
import com.unilag.course_registration_system.model.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    Optional<StudentModel> findByStudentIdAndFirstName(String studentId, String firstName);

    long countByStudentIdStartingWith(String s);

    boolean existsByStudentId(String newId);
}
