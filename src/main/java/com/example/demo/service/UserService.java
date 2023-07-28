package com.example.demo.service;

import com.example.demo.model.user.User;
import com.example.demo.model.user.dto.LoginRequestDto;
import com.example.demo.model.user.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    void registerUser(UserDto dto);

    UserDetails login(LoginRequestDto dto);
}
