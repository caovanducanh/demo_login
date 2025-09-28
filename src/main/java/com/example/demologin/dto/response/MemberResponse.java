package com.example.demologin.dto.response;

import com.example.demologin.enums.UserStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MemberResponse {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private UserStatus status;
    private String role;

    public MemberResponse(Long userId, String username, String email, String fullName, UserStatus status, String role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.status = status;
        this.role = role;
    }
}

