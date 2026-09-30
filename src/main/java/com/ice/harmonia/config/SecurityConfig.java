package com.ice.harmonia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabled for stateless REST APIs
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // Protects every endpoint, including Swagger
                )
                .httpBasic(Customizer.withDefaults()); // Enforces the username/password prompt

        return http.build();
    }

    /*@Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Allows text credentials evaluation for testing
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(SecurityProperties securityProperties, PasswordEncoder passwordEncoder) {
        SecurityProperties.User userProperties = securityProperties.getUser();

        String username = (userProperties.getName() != null) ? userProperties.getName() : "user";
        String password = (userProperties.getPassword() != null) ? userProperties.getPassword() : "harmonia";

        UserDetails testUser = User.withUsername(username)
                // REMOVE passwordEncoder.encode() here
                .password(password)
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(testUser);
    }*/

}
