package com.usermanagement.controller;

import com.usermanagement.dto.CreateUserRequest;
import com.usermanagement.dto.UserResponseDTO;
import com.usermanagement.entity.User;
import com.usermanagement.mapper.UserMapper;
import com.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService,UserMapper userMapper){
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserRequest request){
        User user = userService.createUser(request);
        UserResponseDTO response = userMapper.toResponseDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
