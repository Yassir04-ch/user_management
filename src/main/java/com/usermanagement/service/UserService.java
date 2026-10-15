package com.usermanagement.service;

import com.usermanagement.dto.CreateUserRequest;
import com.usermanagement.entity.User;
import com.usermanagement.entity.enums.Role;
import com.usermanagement.exception.EmailAlreadyExistsException;
import com.usermanagement.mapper.UserMapper;
import com.usermanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder , UserMapper userMapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public User createUser(CreateUserRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email déja Exist");
        }

        User user = userMapper.toEntity(request);

        user.setRole(Role.USER);
        user.setActive(true);

        String  password = passwordEncoder.encode(request.getPassword());
        user.setPassword(password);

        return userRepository.save(user);
    }

}
