package com.usermanagement.controller;

import com.usermanagement.dto.CreateUserRequest;
import com.usermanagement.dto.ExceptionResponse;
import com.usermanagement.dto.UpdateUserRequest;
import com.usermanagement.dto.UserResponseDTO;
import com.usermanagement.entity.User;
import com.usermanagement.exception.EmailAlreadyExistsException;
import com.usermanagement.exception.UserNotFoundException;
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
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request){
       try{
        User user = userService.createUser(request);
        UserResponseDTO response = userMapper.toResponseDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
       }catch (EmailAlreadyExistsException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
       }
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") UUID id) {
        try{
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        }catch (UserNotFoundException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e.getMessage()));
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") UUID id, @Valid @RequestBody UpdateUserRequest request) {
       try{
           UserResponseDTO updatedUser = userService.updateUser(id, request);
           return ResponseEntity.ok(updatedUser);
       }catch (EmailAlreadyExistsException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
       }catch (UserNotFoundException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e.getMessage()));
       }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable("id") UUID id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.noContent().build();

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e.getMessage()));
        }
    }

}
