package org.example.finsight.transaction.controller;

import org.example.finsight.transaction.dto.TransactionDTO;
import org.example.finsight.transaction.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO dto,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.createTransaction(dto, userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getUserTransactions(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getTransactionsByUser(userDetails.getUsername()));
    }
}