package com.unilag.course_registration_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenValidationResponse {
    private boolean valid;
    private String studentId;
    private Long departmentId;
    private Long facultyId;
    private String email;
    private String firstName;
    private Instant expiresAt;
    private String message;

    @Override
    public String toString() {
        return "TokenValidationResponse{" +
                "valid=" + valid +
                ", studentId='" + studentId + '\'' +
                ", departmentId=" + departmentId +
                ", facultyId=" + facultyId +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", expiresAt=" + expiresAt +
                ", message='" + message + '\'' +
                '}';
    }
}
