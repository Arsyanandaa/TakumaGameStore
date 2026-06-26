package com.takumagamestore.demo.controller;

import com.takumagamestore.demo.dto.AuthDTO;
import com.takumagamestore.demo.model.User;
import com.takumagamestore.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody AuthDTO authDTO) {
        try {
            User user = authService.loginWithGoogle(authDTO.getIdToken());
            // Berhasil login/register, return data user-nya ke frontend
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // Jika token palsu atau error sistem, kirim status 400 Bad Request
            return ResponseEntity.badRequest().body("Login Gagal: " + e.getMessage());
        }
    }
}