package com.takumagamestore.demo.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.takumagamestore.demo.model.User;
import com.takumagamestore.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    // TODO: Ganti pake Google Client ID asli lo nanti dari Google Cloud Console
    private final String GOOGLE_CLIENT_ID = "YOUR_GOOGLE_CLIENT_ID_DISINI.apps.googleusercontent.com";

    public User loginWithGoogle(String idTokenString) throws Exception {
        // 1. Setup verifier resmi dari Google
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

        // 2. Verifikasi tokennya
        GoogleIdToken idToken = verifier.verify(idTokenString);
        
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Get data user dari payload Google
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String googleId = payload.getSubject(); // Unique Google ID

            // 3. Cek di DB, apakah user sudah pernah daftar?
            Optional<User> existingUser = userRepository.findByEmail(email);

            if (existingUser.isPresent()) {
                // User sudah ada, langsung return datanya (Login Berhasil)
                return existingUser.get();
            } else {
                // User belum ada, daftarin otomatis ke PostgreSQL (Register Berhasil)
                User newUser = new User();
                newUser.setGoogleId(googleId);
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setAvatarUrl(pictureUrl);
                newUser.setCreatedAt(LocalDateTime.now());
                newUser.setUpdatedAt(LocalDateTime.now());

                return userRepository.save(newUser);
            }
        } else {
            throw new RuntimeException("Token Google tidak valid atau sudah kadaluwarsa!");
        }
    }
}