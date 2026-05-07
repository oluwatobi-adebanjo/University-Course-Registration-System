package com.unilag.course_registration_system.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@Embeddable
public class Semester {
    private String semesterName;
    private String academicSession;

    /**
     * Maps to the read-only displayLabel in the API.
     * Used verbatim in notification messages.
     * Example: "First Semester, 2025/2026 session"
     */
    public String getDisplayLabel() {
        return String.format("%s, %s session", semesterName, academicSession);
    }
}
