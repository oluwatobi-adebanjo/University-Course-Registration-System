package com.unilag.course_registration_system.service.impl;

import com.unilag.course_registration_system.dto.request.RegistrationRequest;
import com.unilag.course_registration_system.dto.response.RegistrationStatusResponse;
import com.unilag.course_registration_system.dto.response.Response;
import com.unilag.course_registration_system.dto.response.TokenValidationResponse;
import com.unilag.course_registration_system.entity.Course;
import com.unilag.course_registration_system.entity.Department;
import com.unilag.course_registration_system.entity.Registration;
import com.unilag.course_registration_system.entity.Student;
import com.unilag.course_registration_system.exception.NotFoundException;
import com.unilag.course_registration_system.model.RegistrationStatus;
import com.unilag.course_registration_system.repository.CourseRepository;
import com.unilag.course_registration_system.repository.DepartmentRepository;
import com.unilag.course_registration_system.repository.RegistrationRepository;
import com.unilag.course_registration_system.repository.StudentRepository;
import com.unilag.course_registration_system.service.RegistrationService;
import com.unilag.course_registration_system.session.TokenService;
import com.unilag.course_registration_system.utils.ResponseCodes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static com.unilag.course_registration_system.model.RegistrationStatus.COMPLETED;
import static com.unilag.course_registration_system.model.RegistrationStatus.PENDING;

@RequiredArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final TokenService tokenService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final HttpServletRequest servletRequest;

    @Override
    @Transactional
    public Response<Void> courseRegistration(RegistrationRequest request) {
        // 1. Authenticate and Fetch Student
        String token = tokenService.getToken(servletRequest);
        TokenValidationResponse session = tokenService.validateToken(token);

        Student student = studentRepository.findByStudentId(session.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        // 2. Check if already registered for this semester (Conflict 409)
        boolean alreadyRegistered = registrationRepository.existsByStudentsAndSemesterAndStatus(
                student, request.getSemester(), COMPLETED);

        if (alreadyRegistered) {
            throw new NotFoundException(
                    "Your courses are already registered for the " + request.getSemester().getDisplayLabel());
        }
        Department department = departmentRepository.findById(student.getDepartments().getId()).orElseThrow(()-> new NotFoundException("Department not found"));

        // 3. Fetch and Validate Courses
        List<Course> departmentalCourses = courseRepository.findByLevelAndDepartment(student.getCurrentLevel(),department);
        System.out.println("Courses "+ departmentalCourses);
        System.out.println("Departmental Courses "+ departmentalCourses.size());
        System.out.println("Submitted Courses "+ request.getCourseCodes().size());

        if (departmentalCourses.size() != request.getCourseCodes().size()) {
            throw new NotFoundException("Selected courses does not equal to required courses");
        }

        // 4. OOP Business Validations
        validateBusinessRules(student, departmentalCourses);

        // 5. Create Registration Record
        Registration registration = new Registration();
        registration.setStudents(student);
        registration.setCourses(departmentalCourses);
        registration.setSemester(request.getSemester());
        registration.setStatus(COMPLETED);

        // 6. Update Course Slots (Decrement)
        departmentalCourses.forEach(course -> {
            course.setAvailableSlots(course.getAvailableSlots() - 1);
            courseRepository.save(course);
        });
        registrationRepository.save(registration);
        System.out.println("Courses registered successfully");

        return new Response<>(ResponseCodes.GENERAL_SUCCESS_CODE,"Registration successful for the " + request.getSemester().getDisplayLabel());
    }

    @Override
    public Response<RegistrationStatusResponse> checkRegistrationStatus() {
        String token = tokenService.getToken(servletRequest);
        TokenValidationResponse session = tokenService.validateToken(token);

        Student student = studentRepository.findByStudentId(session.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));
        Optional<Registration> reg = registrationRepository.findByStudentsAndSemester(student, student.getSemester());
        if (reg.isEmpty()) {
            RegistrationStatusResponse registration = getRegistrationStatusResponse(student);
            return new Response<>(ResponseCodes.GENERAL_SUCCESS_CODE,"Registration status retried successfully",registration);
        }
        if (reg.get().getStatus() == COMPLETED) {
            RegistrationStatusResponse registration = getStatusResponse(student);
            return new Response<>(ResponseCodes.GENERAL_SUCCESS_CODE,"Registration status retrieved successfully",registration);
        }else{
            RegistrationStatusResponse registration = new RegistrationStatusResponse();
            registration.setStatus(PENDING);
            registration.setMessage("Your courses registration is in progress.");
            registration.setRegistered(false);
            registration.setSemesterName(student.getSemester().getSemesterName());
            registration.setAcademicSession(student.getSemester().getAcademicSession());
            registration.setSummary("Course registration in progress");
            return new Response<>(ResponseCodes.GENERAL_SUCCESS_CODE,"Registration status successfully",registration);
        }
    }

    private static RegistrationStatusResponse getStatusResponse(Student student) {
        RegistrationStatusResponse registration = new RegistrationStatusResponse();
        registration.setStatus(COMPLETED);
        registration.setMessage("Your courses are already registered for the selected Academic year and Semester.");
        registration.setRegistered(true);
        registration.setSemesterName(student.getSemester().getSemesterName());
        registration.setAcademicSession(student.getSemester().getAcademicSession());
        registration.setSummary("Student has completed registration");
        return registration;
    }

    private static RegistrationStatusResponse getRegistrationStatusResponse(Student student) {
        RegistrationStatusResponse registration = new RegistrationStatusResponse();
        registration.setStatus(RegistrationStatus.PENDING);
        registration.setMessage("Student has not yet registered");
        registration.setRegistered(false);
        registration.setSemesterName(student.getSemester().getSemesterName());
        registration.setAcademicSession(student.getSemester().getAcademicSession());
        registration.setSummary("Student has not yet registered");
        return registration;
    }

    private void validateBusinessRules(Student student, List<Course> courses) {
        // Rule: Credit Units must be between 12 and 24
        int totalCredits = courses.stream().mapToInt(Course::getCreditUnit).sum();
        if (totalCredits < 12 || totalCredits > 24) {
            throw new NotFoundException("Total credits is either less than 12 or more than 24");
        }

        // Rule: Slot Availability
        for (Course course : courses) {
            if (course.getAvailableSlots() <= 0) {
                throw new NotFoundException("Course " + course.getCourseCode() + " has no remaining slots.");
            }
        }

    }
}
