package com.takumagamestore.demo.repository;

import com.takumagamestore.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Method sakti tambahan buat nyari user berdasarkan email pas login Google OAuth nanti
    Optional<User> findByEmail(String email);
    
    // Method tambahan kalau lo mau mastiin google_id nya udah terdaftar atau belum
    Optional<User> findByGoogleId(String googleId);
}