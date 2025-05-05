package org.example.finsight.transaction.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO dto,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.createTransaction(dto, userDetails.getUsername()));
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<TransactionDTO>> getUserTransactions(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getTransactionsByUser(userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id,
                                                            @RequestBody TransactionDTO dto,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.updateTransaction(id, dto, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        service.deleteTransaction(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}