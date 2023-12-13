package com.example.ProducerConsumer.config;

import com.example.ProducerConsumer.ConsumerSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerSocketHandler.class);

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        System.out.println(encoder.encode("password"));
        UserDetails user = User.withUsername("user")
                .password("password")
                .roles("USER")
                .build();
        logger.info("UserDetails user: username = " + user.getUsername() + ", password = " + user.getPassword());
        return new InMemoryUserDetailsManager(user);
    }
}
