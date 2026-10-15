package com.usermanagement.service;

import com.usermanagement.dto.CreateUserRequest;
import com.usermanagement.entity.User;
import com.usermanagement.entity.enums.Role;
import com.usermanagement.exception.EmailAlreadyExistsException;
import com.usermanagement.mapper.UserMapper;
import com.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @org.mockito.Spy
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    UserService userService;
    CreateUserRequest request;
    User user;

    @BeforeEach
    void setUp(){
        request = new CreateUserRequest();
        request.setFirstName("yassir");
        request.setLastName("cherqui");
        request.setEmail("yassir1@gmail.com");
        request.setPassword("yassir123");

        user = new User();
        user.setFirstName("yassir");
        user.setLastName("cherqui");
        user.setEmail("yassir1@gmail.com");
    }


    @Test
    void CreateUser() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("yassir1@gmail.com", result.getEmail());
        assertEquals(Role.USER, result.getRole());
        assertTrue(result.isActive());
        assertNotEquals("yassir123", result.getPassword());
        assertTrue(passwordEncoder.matches("yassir123", result.getPassword()));
        verify(passwordEncoder).encode("yassir123");
        verify(userRepository).save(user);
    }

    @Test
    void ThrowExceptionWhenEmailAlreadyExists(){
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,()->userService.createUser(request));

        verify(userRepository, never()).save(any(User.class));
    }
}
