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
    private String identity_Card;
    private String fullName;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private UserStatus status;
    private java.util.List<String> roles;

    public MemberResponse(Long userId, String username, String email, String identity_Card, String fullName, String phone, String address, LocalDate dateOfBirth, UserStatus status, java.util.List<String> roles) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.identity_Card = identity_Card;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.roles = roles;
    }
}

