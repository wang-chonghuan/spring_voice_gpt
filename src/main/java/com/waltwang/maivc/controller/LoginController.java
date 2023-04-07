package com.waltwang.maivc.controller;

import com.waltwang.maivc.security.AccountCredentials;
import com.waltwang.maivc.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {
        UsernamePasswordAuthenticationToken creds =
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        Authentication auth = authenticationManager.authenticate(creds);
        // generate token
        String jwtToken = jwtService.getToken(auth.getName());
        log.info("login getToken: {}", jwtToken);

        // build response with generated token and JSON content
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

}
