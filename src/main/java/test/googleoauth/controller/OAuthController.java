package test.googleoauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.googleoauth.service.GoogleOAuthService;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final GoogleOAuthService googleOAuthService;

    @PostMapping("/oauth/google")
    public void performGoogleEmail(@RequestParam String code) {
        googleOAuthService.performGoogleEmail(code);
    }

    @PostMapping("/oauth/google/token")
    public void getGoogleEmail(@RequestParam String code) {
        googleOAuthService.getGoogleEmail(code);
    }
}
