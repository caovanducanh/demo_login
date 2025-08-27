package com.example.demologin.serviceImpl;

import com.example.demologin.dto.response.MemberResponse;
import com.example.demologin.entity.User;
import com.example.demologin.repository.UserRepository;
import com.example.demologin.mapper.UserMapper;
import com.example.demologin.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<MemberResponse> getAllUsers(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toUserResponse);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

} 
