package ru.itmentor.spring.boot_security.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {
@GetMapping("/login")
    public ResponseEntity<Object> loginPage() {
        return ResponseEntity.status(401).body("Please login");
    }
    }
