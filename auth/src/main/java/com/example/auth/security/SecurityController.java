package com.example.auth.security;

import com.example.auth.security.otpauthentication.OTPConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * @author Arfat A. Chaus
 * since 2025-10-23
 */
@Controller
final class SecurityController {

    @GetMapping("/form-login")
    String login() {
        return "login";
    }

    @GetMapping("/logout")
    String logout() {
        return "logout";
    }

    @GetMapping(OTPConstants.OTP_URL)
    String otp(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "otp";
    }


    @GetMapping("/error")
    String error() {
        return "error";
    }

}
