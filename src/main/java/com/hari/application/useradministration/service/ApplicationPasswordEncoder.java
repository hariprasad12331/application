package com.hari.application.useradministration.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ApplicationPasswordEncoder {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12); 
    
    public String encode(String password){
        return  this.passwordEncoder.encode(password);

    }

    public Boolean match(String passwordSentForLogin,String actualPassword){
        return this.passwordEncoder.matches(passwordSentForLogin, actualPassword);
    }

    
}
