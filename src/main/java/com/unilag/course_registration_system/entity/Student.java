package com.unilag.course_registration_system.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "students")
@Entity
@Builder
public class Student extends BaseEntity {
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String currentLevel;

    // Many Students belong to one Department
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department departments;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "semesterName", column = @Column(name = "enrolled_semester_name")),
            @AttributeOverride(name = "academicSession", column = @Column(name = "enrolled_academic_session"))
    })
    private Semester semester;


    public Student(String studentId, String firstName, String lastName, String email, String phoneNumber, String address, String currentLevel, Department departments, Semester semester) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.currentLevel = currentLevel;
        this.departments = departments;
        this.semester = semester;
    }
}
