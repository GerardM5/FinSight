package org.example.finsight.transaction.repository;

import org.example.finsight.transaction.model.Transaction;
import org.example.finsight.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);
}