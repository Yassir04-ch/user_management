
package com.usermanagement.repository;

import com.usermanagement.config.RootConfig;
import com.usermanagement.entity.enums.Role;
import com.usermanagement.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Test
    void SaveAndFindUserByEmail() {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class)) {

            UserRepository userRepository = context.getBean(UserRepository.class);

            String email = "yassir" + UUID.randomUUID() + "@gmail.com";

            User user = new User();
            user.setFirstName("Yassir");
            user.setLastName("Cherqui");
            user.setEmail(email);
            user.setPassword("password");
            user.setRole(Role.USER);
            user.setActive(true);

            userRepository.save(user);

            assertTrue(userRepository.existsByEmail(email));

            assertTrue(userRepository.findByEmail(email).isPresent());
        }
    }
}
