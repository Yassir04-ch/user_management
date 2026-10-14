
package com.usermanagement.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RootConfigTest {

    @Test
    void shouldLoadRootConfiguration() {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class)) {
            assertNotNull(context);
        }
    }
}
