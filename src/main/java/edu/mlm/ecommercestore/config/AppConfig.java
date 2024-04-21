package edu.mlm.ecommercestore.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Application-wide configuration class.
 * <p>
 * Defines beans that are used across the application to ensure that core functionalities such as
 * object mapping and password encoding are configured correctly and available for autowiring into other components.
 * Utilizing this centralized configuration approach facilitates maintenance and promotes consistency
 * throughout the application.
 */
@Configuration
public class AppConfig {

    /**
     * Configures and provides a {@link ModelMapper} bean for the application.
     * <p>
     * {@link ModelMapper} is utilized for object mapping purposes, simplifying the conversion between different
     * object models within the application. This is particularly useful for transforming data transfer objects (DTOs)
     * to entities and vice versa, ensuring a clean separation of concerns and reducing boilerplate code.
     *
     * @return a configured {@link ModelMapper} instance
     */
    @Bean
    ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    /**
     * Configures and provides a {@link PasswordEncoder} bean, specifically a {@link BCryptPasswordEncoder},
     * for the application.
     * <p>
     * The {@link PasswordEncoder} is essential for encoding passwords securely, utilizing the BCrypt strong hashing function.
     * This approach enhances security by ensuring that passwords are stored in a hashed form, making them resistant
     * to various attack vectors such as brute force attacks.
     *
     * @return a configured {@link PasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

