package com.helloworld.okta;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @GetMapping("/")
    public String index(@AuthenticationPrincipal OidcUser principal) {
        return "Hello " + principal.getName();
    }

    @GetMapping("/greet")
    public String greet() {
        return "Hello World!";
    }
}
