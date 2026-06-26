package com.takumagamestore.demo.repository;

import com.takumagamestore.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Cari transaksi berdasarkan Nomor Invoice (buat fitur cek resi/status top-up)
    Optional<Transaction> findByInvoiceNumber(String invoiceNumber);
    
    // Cari semua riwayat transaksi milik satu user tertentu (buat halaman riwayat user)
    List<Transaction> findByUserId(Long userId);
}