package com.prodapt.learningspring.controller;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prodapt.learningspring.business.LoginBody;
import com.prodapt.learningspring.dto.TokenDTO;
import com.prodapt.learningspring.service.UserService;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class APIAuthController {
    
    @Autowired
    JwtEncoder jwtEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @PostMapping("/token")
    public TokenDTO token(@RequestBody LoginBody loginBody) {
        Instant now = Instant.now();
        long expiry = 3600L;
        var username = loginBody.getUsername();
        var password = loginBody.getPassword();
        System.out.println(username);
        System.out.println(password);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String scope = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(expiry))
				.subject(authentication.getName())
				.claim("scope", scope)
				.build();


        TokenDTO t = new TokenDTO();
        t.setToken(this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
        t.setUsername(authentication.getName());
        return t;
    }

}