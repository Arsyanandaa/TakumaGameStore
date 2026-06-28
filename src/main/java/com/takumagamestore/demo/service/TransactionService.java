package com.takumagamestore.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    // 1. ISI SERVER KEY SANDBOX LO DI SINI
    private final String MIDTRANS_SERVER_KEY = "Mid-server-ANMaJns4Ks1uTE6Tayj7ywFM";
    private final String MIDTRANS_URL = "https://app.sandbox.midtrans.com/snap/v1/transactions";

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
        transaction.setGameUserId(dto.getGameUserId());
        transaction.setZoneId(dto.getZoneId());
        transaction.setPaymentMethod(dto.getPaymentMethod());
        transaction.setTotalPrice(product.getPrice()); 
        transaction.setStatus("PENDING");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        // 5. Generate Invoice Number unik di awal (karena mau dikirim ke Midtrans)
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uniqueId = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        String invoiceNumber = "TKM-" + datePrefix + "-" + uniqueId;
        transaction.setInvoiceNumber(invoiceNumber);

        // 4. LOGIC REAL REQUEST QRIS VIA MIDTRANS API (HTTP POST REST-TEMPLATE)
        if (dto.getPaymentMethod().equalsIgnoreCase("QRIS")) {
            try {
                RestTemplate restTemplate = new RestTemplate();

                // Setup Headers HTTP Basic Auth untuk Midtrans
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                
                // Server key di-encode ke Base64 sebagai syarat Header Authorization dari Midtrans
                String auth = MIDTRANS_SERVER_KEY + ":";
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
                headers.set("Authorization", "Basic " + encodedAuth);

                // Buat Payload JSON
                Map<String, Object> body = new HashMap<>();
                
                Map<String, String> transactionDetails = new HashMap<>();
                transactionDetails.put("order_id", invoiceNumber);
                transactionDetails.put("gross_amount", String.valueOf(product.getPrice().longValue()));
                body.put("transaction_details", transactionDetails);

                // Aktifkan pembayaran Gopay & ShopeePay untuk memicu QRIS universal Midtrans
                List<String> enabledPayments = new ArrayList<>();
                enabledPayments.add("gopay");
                enabledPayments.add("shopeepay");
                body.put("enabled_payments", enabledPayments);

                // Kirim request POST ke API Midtrans Sandbox
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
                ResponseEntity<Map> response = restTemplate.postForEntity(MIDTRANS_URL, entity, Map.class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    Map<String, Object> responseBody = response.getBody();
                    String snapToken = (String) responseBody.get("token");
                    String redirectUrl = (String) responseBody.get("redirect_url");

                    // Simpan token & URL pembayaran real dari Midtrans
                    transaction.setSnapToken(snapToken);
                    transaction.setQrCodeUrl(redirectUrl); 
                } else {
                    throw new RuntimeException("Gagal mendapatkan respons valid dari Midtrans");
                }

            } catch (Exception e) {
                throw new RuntimeException("Gagal menghubungkan pembayaran ke Midtrans: " + e.getMessage());
            }
        }

        // 6. Simpan ke database PostgreSQL lo
        return transactionRepository.save(transaction);
    }
}