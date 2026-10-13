package com.usermanagement.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"com.usermanagement.service", "com.usermanagement.mapper"})
@Import(PersistenceConfig.class)
public class RootConfig {
}
