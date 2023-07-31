package com.example.demo.controller;

import com.example.demo.model.user.User;
import com.example.demo.model.user.dto.LoginRequestDto;
import com.example.demo.model.user.dto.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Autowired
    public UserController(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto dto) {
        userService.registerUser(dto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto) {
        UserDetails user = userService.login(dto);
        return new ResponseEntity<>(jwtTokenUtil.generateToken(user), HttpStatus.OK);
    }

    @PostMapping("/token")
    public ResponseEntity<String> refreshToken(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(jwtTokenUtil.generateToken(userDetails), HttpStatus.OK);
    }
}
