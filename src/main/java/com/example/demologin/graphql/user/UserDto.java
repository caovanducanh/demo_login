package com.example.demologin.graphql.user;

import lombok.Data;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String status;
    private List<String> roles;
}
