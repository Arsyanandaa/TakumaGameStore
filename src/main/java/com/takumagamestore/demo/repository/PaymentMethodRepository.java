package com.takumagamestore.demo.repository;

import com.takumagamestore.demo.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    
    // Ambil semua metode pembayaran yang statusnya aktif aja (buat ditampilin ke user)
    List<PaymentMethod> findByIsActiveTrue();
    
    // Cari berdasarkan kode unik pembayaran
    Optional<PaymentMethod> findByCode(String code);
}