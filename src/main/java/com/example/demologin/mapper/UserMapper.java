package com.example.demologin.mapper;
import com.example.demologin.entity.Role;

import com.example.demologin.dto.request.user.UserRequest;
import com.example.demologin.dto.response.LoginResponse;
import com.example.demologin.dto.response.MemberResponse;
import com.example.demologin.dto.response.UserResponse;
import com.example.demologin.entity.User;
import com.example.demologin.enums.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public MemberResponse toUserResponse(User user) {
    java.util.List<String> roleNames = user.getRoles().stream()
        .map(Role::getName)
        .collect(java.util.stream.Collectors.toList());
    return new MemberResponse(
        user.getUserId(),
        user.getUsername(),
        user.getEmail(),
        user.getIdentityCard(),
        user.getFullName(),
        user.getPhone(),
        user.getAddress(),
        user.getDateOfBirth(),
        user.getStatus(),
        roleNames
    );
    }
    public static User toEntity(UserRequest userRequest) {
        User user = new User(
                userRequest.getUsername(),
                null, // password will be set elsewhere
                userRequest.getFullname(),
                userRequest.getEmail(),
                userRequest.getPhone(),
                userRequest.getAddress()
        );
        
        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setIdentityCard(userRequest.getIdentity_Card());
        user.setStatus(UserStatus.ACTIVE); // Default status
        
        return user;
    }

    // Convert User -> UserResponse (cho login) - chỉ trả về token và refreshToken  
    public static UserResponse toResponse(User user, String token, String refreshToken) {
        return UserResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    // Convert User -> LoginResponse (cho login) - chỉ trả về token và refreshToken
    public static LoginResponse toLoginResponse(User user, String token, String refreshToken) {
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}
