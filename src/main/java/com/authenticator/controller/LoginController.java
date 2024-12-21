package com.authenticator.controller;

import com.authenticator.model.dto.CredentialsDTO;
import com.authenticator.model.dto.JwtTokenDTO;
import com.authenticator.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {
    private final LoginService loginService;

    @PostMapping
    public JwtTokenDTO login(@RequestBody CredentialsDTO credentialsDTO) {
        return loginService.logIn(credentialsDTO.getEmail(), credentialsDTO.getPassword());
    }
}
