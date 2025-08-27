package com.example.demologin.service;

import com.example.demologin.dto.request.user.UpdateUserRequest;
import com.example.demologin.dto.response.MemberResponse;
import org.springframework.data.domain.Page;

import com.example.demologin.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Page<MemberResponse> getAllUsers(int page, int size);

    List<User> findAll();
    Optional<User> findById(Long id);
}
