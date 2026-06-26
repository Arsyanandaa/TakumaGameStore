package com.takumagamestore.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takumagamestore.demo.dto.TransactionDTO;
import com.takumagamestore.demo.model.Product;
import com.takumagamestore.demo.model.Transaction;
import com.takumagamestore.demo.model.User;
import com.takumagamestore.demo.repository.ProductRepository;
import com.takumagamestore.demo.repository.TransactionRepository;
import com.takumagamestore.demo.repository.UserRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Transaction processTransaction(TransactionDTO dto) {
    // 1. Cari User aslinya di DB
    User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User tidak ditemukan dengan ID: " + dto.getUserId()));

    // 2. Cari Product aslinya di DB
    Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new RuntimeException("Product tidak ditemukan dengan ID: " + dto.getProductId()));

    // 3. Rakit objek Transaction baru
    Transaction transaction = new Transaction();
    transaction.setUser(user);
    transaction.setProduct(product);
    transaction.setTargetId(dto.getTargetId());
    transaction.setZoneId(dto.getZoneId());
    transaction.setPaymentMethod(dto.getPaymentMethod());
    transaction.setTotalPrice(product.getPrice()); 
    transaction.setStatus("PENDING");
    transaction.setCreatedAt(LocalDateTime.now());
    transaction.setUpdatedAt(LocalDateTime.now());

    // 4. LOGIC QRIS SIMULATOR GRATISAN
    // Kalau user milih QRIS, kita kasih gambar QRIS dummy/testing biar di-render frontend
    if (dto.getPaymentMethod().equalsIgnoreCase("QRIS")) {
        // Lo bisa ganti pake link gambar QRIS asli e-wallet lo, atau pake QR dummy ini dulu buat tes
        transaction.setQrCodeUrl("https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=TAKUMA-STORE-PAYMENT-DUMMY");
    }

    // 5. Generate Invoice Number unik
    String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String uniqueId = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    transaction.setInvoiceNumber("TKM-" + datePrefix + "-" + uniqueId);
    
    // 6. Simpan ke database
    return transactionRepository.save(transaction);
}
}