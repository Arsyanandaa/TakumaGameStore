package com.takumagamestore.demo.controller;

import com.takumagamestore.demo.model.User;
import com.takumagamestore.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 1. GET ALL: Ngambil SEMUA data user dari database
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 2. CREATE: Nyimpen/register user baru ke database
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // 3. GET BY ID: Nyari satu user spesifik berdasarkan ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. UPDATE: Ngubah data user yang udah ada berdasarkan ID (Penting buat Admin)
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    // Update data yang diizinkan diubah oleh admin/sistem
                    user.setName(userDetails.getName());
                    user.setEmail(userDetails.getEmail());
                    user.setAvatarUrl(userDetails.getAvatarUrl());
                    user.setUpdatedAt(LocalDateTime.now()); // Update otomatis waktu perubahannya
                    
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok().body(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. DELETE: Menghapus user berdasarkan ID dari database (Penting buat Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build(); // Return HTTP 200 OK kalau sukses hapus
                })
                .orElse(ResponseEntity.notFound().build()); // Return HTTP 404 kalau ID gak ketemu
    }
}