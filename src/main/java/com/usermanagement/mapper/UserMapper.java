package com.usermanagement.mapper;

import com.usermanagement.dto.CreateUserRequest;
import com.usermanagement.dto.UserResponseDTO;
import com.usermanagement.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        return  user;
    }

    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.isActive()
        );
    }
}
