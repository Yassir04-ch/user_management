package com.usermanagement.controller;

import com.usermanagement.dto.CreateUserRequest;
import com.usermanagement.dto.UpdateUserRequest;
import com.usermanagement.dto.UserResponseDTO;
import com.usermanagement.entity.User;
import com.usermanagement.mapper.UserMapper;
import com.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") UUID id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") UUID id, @Valid @RequestBody UpdateUserRequest request) {
        UserResponseDTO updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable("id") UUID id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }


}
