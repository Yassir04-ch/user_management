package com.usermanagement.service;

import com.usermanagement.dto.CreateUserRequest;
import com.usermanagement.dto.UpdateUserRequest;
import com.usermanagement.dto.UserResponseDTO;
import com.usermanagement.entity.User;
import com.usermanagement.entity.enums.Role;
import com.usermanagement.exception.EmailAlreadyExistsException;
import com.usermanagement.exception.UserNotFoundException;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    CreateUserRequest createRequest;
    User user;

    @BeforeEach
    void setUp(){
        createRequest = new CreateUserRequest();
        createRequest.setFirstName("yassir");
        createRequest.setLastName("cherqui");
        createRequest.setEmail("yassir1@gmail.com");
        createRequest.setPassword("yassir123");

        user = new User();
        user.setFirstName("yassir");
        user.setLastName("cherqui");
        user.setEmail("yassir1@gmail.com");
    }


    @Test
    void CreateUser() {
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(userMapper.toEntity(createRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(createRequest);

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
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,()->userService.createUser(createRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void UpdateUser() {
        UUID id = UUID.randomUUID();
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFirstName("Yassir");
        updateUserRequest.setLastName("Cherqui");
        updateUserRequest.setEmail("yassir@gmail.com");

        UserResponseDTO responseDTO = mock(UserResponseDTO.class);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        when(userRepository.existsByEmail(updateUserRequest.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.updateUser(id,updateUserRequest);

        assertNotNull(result);
        assertEquals("Yassir", user.getFirstName());
        assertEquals("Cherqui",user.getLastName());
        assertEquals("yassir@gmail.com", user.getEmail());

        verify(userRepository).save(user);
        verify(userMapper).toResponseDTO(user);
    }
    @Test
    void getUserById(){
        UUID id = UUID.randomUUID();

        UserResponseDTO responseDTO = new UserResponseDTO();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.getUserById(id);
        assertNotNull(result);
        assertSame(responseDTO,result);

        verify(userRepository).findById(id);
        verify(userMapper).toResponseDTO(user);
    }

    @Test
    void getUserByIdThrowsExceptionWhenNotFound(){
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,() -> userService.getUserById(id));
        verify(userMapper,never()).toResponseDTO(any(User.class));
    }

    @Test
    void getAllUsers(){
       UserResponseDTO responseDTO = mock(UserResponseDTO.class);
       when(userRepository.findByActiveTrue()).thenReturn(List.of(user));
       when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(responseDTO, result.get(0));

        verify(userRepository).findByActiveTrue();
        verify(userMapper).toResponseDTO(user);
    }

    @Test
    void getAllUsersEmptyList() {
        when(userRepository.findByActiveTrue()).thenReturn(List.of());
        List<UserResponseDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void deactivateUser(){
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        userService.deactivateUser(id);
        assertFalse(user.isActive());
        verify(userRepository).findById(id);
    }

    @Test
    void deactivateUserThrowsExceptionWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deactivateUser(id));
        verify(userRepository, never()).save(any(User.class));
    }
}
