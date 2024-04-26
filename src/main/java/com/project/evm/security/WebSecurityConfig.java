package com.project.evm.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfiguration{
    
    public void filterChain(HttpSecurity http)throws Exception{
    }
}
