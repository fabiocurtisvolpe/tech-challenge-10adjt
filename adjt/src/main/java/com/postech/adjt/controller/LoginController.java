package com.postech.adjt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.adjt.jwt.model.AuthRequest;
import com.postech.adjt.jwt.model.AuthResponse;
import com.postech.adjt.jwt.service.AppUserDetailsService;
import com.postech.adjt.jwt.service.JwtService;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final AppUserDetailsService userDetailsService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public LoginController(AuthenticationManager authManager, JwtService jwtService,
            AppUserDetailsService userDetailsService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public AuthResponse login(@RequestBody AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.senha()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.login());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }
}
