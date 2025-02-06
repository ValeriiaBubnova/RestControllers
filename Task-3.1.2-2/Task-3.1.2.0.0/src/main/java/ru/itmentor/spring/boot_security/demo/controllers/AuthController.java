package ru.itmentor.spring.boot_security.demo.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmentor.spring.boot_security.demo.model.User;


@Controller
@RequestMapping("/auth")
public class AuthController {
@GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
    }
