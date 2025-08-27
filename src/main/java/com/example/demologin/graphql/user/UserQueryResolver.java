package com.example.demologin.graphql.user;

import com.example.demologin.entity.User;
import com.example.demologin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserQueryResolver {

    private final UserService userService;

    @QueryMapping
    public List<UserDto> users() {
        return userService.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @QueryMapping
    public UserDto userById(@Argument Long id) {
        return userService.findById(id).map(this::toDto).orElse(null);
    }


    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
        dto.setRoles(user.getRoles() != null ? user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList()) : null);
        return dto;
    }
}
