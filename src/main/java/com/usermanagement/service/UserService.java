package com.usermanagement.service;

import com.usermanagement.dto.CreateUserRequest;
import com.usermanagement.dto.UpdateUserRequest;
import com.usermanagement.dto.UserResponseDTO;
import com.usermanagement.entity.User;
import com.usermanagement.entity.enums.Role;
import com.usermanagement.exception.EmailAlreadyExistsException;
import com.usermanagement.mapper.UserMapper;
import com.usermanagement.mapper.UserNotFoundException;
import com.usermanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    @Transactional
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findByActiveTrue().stream().map(userMapper::toResponseDTO).toList();
    }

    @Transactional
    public UserResponseDTO getUserById(UUID id){
        User user = userRepository.findById(id).orElseThrow(()->
                new UserNotFoundException("utilisateur introuvé"));

        if (!user.isActive()) {
            throw new UserNotFoundException("utilisateur desactivé");
        }
        return userMapper.toResponseDTO(user);
    }


    @Transactional
    public UserResponseDTO updateUser(UUID id , UpdateUserRequest request){
        User user = userRepository.findById(id).orElseThrow(()->
                new UserNotFoundException("utilisateur introuvé"));
        if(!user.isActive()){
            throw new UserNotFoundException("utilisateur desactivé");
        }

        if (!user.getEmail().equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email déjà existant");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        User userUpdate = userRepository.save(user);
        return userMapper.toResponseDTO(userUpdate);
    }

    @Transactional
    public void deactivateUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("utilisateur introuvé"));

        if (!user.isActive()) {
            throw new UserNotFoundException("Utilisateur déjà desactivé");
        }
        user.setActive(false);
        userRepository.save(user);
    }

}
