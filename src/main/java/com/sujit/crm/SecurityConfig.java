package com.sujit.crm;

import com.sujit.crm.service.SpringDataUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

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
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers("/").hasAnyRole("ADMIN", "OWNER", "EMPLOYEE")
                .requestMatchers("/client/**").hasAnyRole("OWNER", "EMPLOYEE", "MANAGER")
                .requestMatchers("/contract/**").hasAnyRole("OWNER", "EMPLOYEE", "MANAGER")
                .requestMatchers("/event/**").hasAnyRole("OWNER", "EMPLOYEE", "MANAGER")
                .requestMatchers("/employeeSearch/**").hasAnyRole("OWNER", "EMPLOYEE", "MANAGER")
                .requestMatchers("/managerSearch/**").hasAnyRole("OWNER", "EMPLOYEE", "MANAGER")
                .requestMatchers("/import/**").hasAnyRole("OWNER", "EMPLOYEE", "MANAGER")
                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                .anyRequest().permitAll()).formLogin(withDefaults()).httpBasic(withDefaults());

        return http.build();
    }
}
