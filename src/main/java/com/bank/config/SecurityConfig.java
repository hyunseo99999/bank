package com.bank.config;

import com.bank.common.ResponseUtil;
import com.bank.domain.user.UserEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.csrf().disable();
        http.formLogin().disable();
        http.cors().configurationSource(null);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.httpBasic().disable();

        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            ResponseUtil.unAuthentication(response, "로그인을 진행해 주세요.");
        });

        http.authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/api/admin/**").hasRole(String.valueOf(UserEnum.ADMIN))
                .anyRequest()
                .permitAll();

        return http.build();
    }

}
