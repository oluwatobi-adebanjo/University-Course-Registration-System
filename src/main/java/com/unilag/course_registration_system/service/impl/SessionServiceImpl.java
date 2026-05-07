package com.unilag.course_registration_system.service.impl;

import com.unilag.course_registration_system.dto.request.CreateSession;
import com.unilag.course_registration_system.dto.response.Response;
import com.unilag.course_registration_system.dto.response.TokenResponse;
import com.unilag.course_registration_system.entity.Student;
import com.unilag.course_registration_system.exception.NotFoundException;
import com.unilag.course_registration_system.repository.StudentRepository;
import com.unilag.course_registration_system.service.SessionService;
import com.unilag.course_registration_system.session.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.unilag.course_registration_system.utils.ResponseCodes.GENERAL_SUCCESS_CODE;
import static com.unilag.course_registration_system.utils.ResponseCodes.GENERAL_SUCCESS_MESSAGE;
import static com.unilag.course_registration_system.utils.ResponseCodes.VALIDATION_FAILED_CODE;

@RequiredArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {
    private final StudentRepository studentRepository;
    private final TokenService tokenService;

    @Override
    public Response<TokenResponse> createSession(CreateSession request) {
        if (request.getStudentId() == null || request.getStudentId().isEmpty()) {
            return new Response<>(VALIDATION_FAILED_CODE, "Student Id is required.");
        }
        Student student = studentRepository.findByStudentId(request.getStudentId()).orElseThrow(()-> new NotFoundException("Invalid Student Id."));
        TokenResponse  tokenValidationResponse = tokenService.generateToken(student);
        return new Response<>(GENERAL_SUCCESS_CODE, GENERAL_SUCCESS_MESSAGE, tokenValidationResponse);
    }
}
