package com.takumagamestore.demo.controller;

import com.takumagamestore.demo.dto.TransactionDTO;
import com.takumagamestore.demo.model.Transaction;
import com.takumagamestore.demo.repository.TransactionRepository;
import com.takumagamestore.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService; // <-- Kita inject Service-nya di sini bro

    // 1. GET ALL: Menampilkan semua transaksi masuk (Fitur Admin Dashboard)
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // 2. CREATE / SUBMIT TOP-UP: Sekarang pake TransactionDTO, bukan Model mentahan lagi!
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        // Panggil logic sakti dari Service layer buat memproses DTO-nya
        Transaction newTransaction = transactionService.processTransaction(transactionDTO);
        return ResponseEntity.ok(newTransaction);
    }

    // 3. TRACK INVOICE: Cek status transaksi berdasarkan nomor invoice (Fitur cek resi umum)
    @GetMapping("/track/{invoiceNumber}")
    public ResponseEntity<Transaction> getTransactionByInvoice(@PathVariable String invoiceNumber) {
        return transactionRepository.findByInvoiceNumber(invoiceNumber)
                .map(tx -> ResponseEntity.ok().body(tx))
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. GET BY USER ID: Ambil riwayat transaksi milik 1 user spesifik
    @GetMapping("/user/{userId}")
    public List<Transaction> getTransactionsByUserId(@PathVariable Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    // 5. UPDATE STATUS: Dipakai Admin atau Webhook Payment Gateway buat ngubah status (PENDING -> SUCCESS)
    @PutMapping("/{id}/status")
    public ResponseEntity<Transaction> updateTransactionStatus(@PathVariable Long id, @RequestParam String status) {
        return transactionRepository.findById(id)
                .map(tx -> {
                    tx.setStatus(status.toUpperCase());
                    tx.setUpdatedAt(LocalDateTime.now());
                    Transaction updatedTx = transactionRepository.save(tx);
                    return ResponseEntity.ok().body(updatedTx);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}