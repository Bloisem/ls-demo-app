package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String activeProfile = System.getenv("SPRING_PROFILES_ACTIVE"); // Get active profile

        if ("dev".equals(activeProfile)) {
            // 🔹 Development Mode (No Security)
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        } else {
            // 🔹 Staging & Production Security Rules
            http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/public/**").permitAll() // Open routes
                    .requestMatchers("/admin/**").hasRole("ADMIN") // Admin-only routes
                    .anyRequest().authenticated()) // Secure all other routes
                .httpBasic(httpBasic -> {}) // Use Basic Authentication
                .csrf(csrf -> csrf.disable()); // Disable CSRF for APIs
        }

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        String password = System.getenv("USER_PASSWORD"); // Fetch password from env

        if (password == null || password.isBlank()) {
            password = "defaultPassword"; // Fallback password for testing
        }

        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder.encode(password))
            .roles("ADMIN") // Admin role
            .build();

        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder.encode("user123"))
            .roles("USER") // Regular user role
            .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 🔒 Use BCrypt for hashing passwords
    }
}