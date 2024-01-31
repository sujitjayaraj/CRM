package com.sujit.crm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SpringDataUserDetailsService customUserDetailsService() {
        return new SpringDataUserDetailsService();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests((authorize) -> authorize.requestMatchers("/").hasAnyRole("ADMIN", "OWNER", "EMPLOYEE", "MANAGER")
                        .requestMatchers("/client/**").hasAnyRole("OWNER", "EMPLOYEE", "MANAGER")
                        .requestMatchers("/contract/**").hasAnyRole("OWNER", "EMPLOYEE", "MANAGER")
                        .requestMatchers("/employeeSearch/**").hasAnyRole("OWNER", "EMPLOYEE", "MANAGER")
                        .requestMatchers(("event/**")).hasAnyRole("OWNER", "EMPLOYEE", "MANAGER")
                        .requestMatchers(("/import/**")).hasAnyRole("OWNER", "EMPLOYEE", "MANAGER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()).formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults()).build();
    }
}