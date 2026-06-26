package com.takumagamestore.demo.controller;

import com.takumagamestore.demo.model.PaymentMethod;
import com.takumagamestore.demo.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    // 1. GET ALL: Nampilin semua tanpa terkecuali (Fitur Dashboard Admin)
    @GetMapping
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    // 2. GET ACTIVE ONLY: Cuma nampilin yang aktif (Penting buat form checkout User)
    @GetMapping("/active")
    public List<PaymentMethod> getActivePaymentMethods() {
        return paymentMethodRepository.findByIsActiveTrue();
    }

    // 3. CREATE: Nambahin metode pembayaran baru (Fitur Admin)
    @PostMapping
    public PaymentMethod createPaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    // 4. GET BY ID: Cari detail metode pembayaran
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethodById(@PathVariable Long id) {
        return paymentMethodRepository.findById(id)
                .map(pm -> ResponseEntity.ok().body(pm))
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. UPDATE: Edit data/status metode pembayaran (Fitur Admin)
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethod> updatePaymentMethod(@PathVariable Long id, @RequestBody PaymentMethod pmDetails) {
        return paymentMethodRepository.findById(id)
                .map(pm -> {
                    pm.setName(pmDetails.getName());
                    pm.setCode(pmDetails.getCode());
                    pm.setType(pmDetails.getType());
                    pm.setImageUrl(pmDetails.getImageUrl());
                    pm.setIsActive(pmDetails.getIsActive());
                    
                    PaymentMethod updatedPm = paymentMethodRepository.save(pm);
                    return ResponseEntity.ok().body(updatedPm);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 6. DELETE: Hapus metode pembayaran (Fitur Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePaymentMethod(@PathVariable Long id) {
        return paymentMethodRepository.findById(id)
                .map(pm -> {
                    paymentMethodRepository.delete(pm);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}