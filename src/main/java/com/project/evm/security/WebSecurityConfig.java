package com.project.evm.security;

import org.hibernate.Session;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfiguration{
    
    public void filterChain(HttpSecurity http)throws Exception{
    }
}
