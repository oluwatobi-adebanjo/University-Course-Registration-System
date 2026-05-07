package com.unilag.course_registration_system.entity;

import com.unilag.course_registration_system.model.RegistrationStatus;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "registrations")
@RequiredArgsConstructor
@Getter
@Setter
public class Registration extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student students;

    @ManyToMany
    @JoinTable(
            name = "registration_courses",
            joinColumns = @JoinColumn(name = "registration_id"),
            inverseJoinColumns = @JoinColumn(name = "course_code")
    )
    private List<Course> courses;

    @Embedded
    private Semester semester;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;
}
