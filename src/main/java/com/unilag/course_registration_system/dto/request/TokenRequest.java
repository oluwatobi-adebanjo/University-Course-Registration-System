package com.unilag.course_registration_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {
    private String studentId;
    private String email;
    private String firstName;
    private String lastName;
}
