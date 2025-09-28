package com.example.demologin.mapper;

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
        // Lấy role đầu tiên nếu có
        String roleName = user.getRoles().stream().findFirst().map(r -> r.getName()).orElse("");
        return new MemberResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getStatus(),
                roleName
        );
    }
    public static User toEntity(UserRequest userRequest) {
        User user = new User(
                userRequest.getUsername(),
                null, // password will be set elsewhere
                userRequest.getFullname(),
                userRequest.getEmail()
        );
        user.setStatus(UserStatus.ACTIVE); // Default status
        
        return user;
    }

    // Convert User -> LoginResponse (cho login) - chỉ trả về token và refreshToken
    public static LoginResponse toLoginResponse(User user, String token, String refreshToken) {
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}
