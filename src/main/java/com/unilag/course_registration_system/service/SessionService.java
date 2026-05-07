package com.unilag.course_registration_system.service;

import com.unilag.course_registration_system.dto.request.CreateSession;
import com.unilag.course_registration_system.dto.response.Response;
import com.unilag.course_registration_system.dto.response.TokenResponse;

public interface SessionService {
    Response<TokenResponse> createSession(CreateSession request);
}
