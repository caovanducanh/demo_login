package com.example.demologin.graphql.user;
import com.example.demologin.dto.response.ResponseObject;
import com.example.demologin.mapper.UserMapper;

import com.example.demologin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class UserQueryResolver {
    private final UserService userService;
    private final UserMapper userMapper;

    @QueryMapping
    public ResponseObject users(@Argument(name = "page") Integer page, @Argument(name = "size") Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        var pageResult = userService.getAllUsers(pageNum, pageSize);
        var users = pageResult.getContent();
        return new ResponseObject(200, "Success", users);
    }


    @QueryMapping
    public ResponseObject userById(@Argument Long id) {
        var member = userService.findById(id)
            .map(userMapper::toUserResponse)
            .orElse(null);
        return new ResponseObject(200, member != null ? "Success" : "Not found", member);
    }


}
