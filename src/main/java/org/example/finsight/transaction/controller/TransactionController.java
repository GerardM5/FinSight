package org.example.finsight.transaction.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.finsight.auth.model.User;
import org.example.finsight.transaction.dto.TransactionDTO;
import org.example.finsight.transaction.model.TransactionType;
import org.example.finsight.transaction.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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

    @GetMapping("/search")
    public ResponseEntity<List<TransactionDTO>> searchTransactions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String description,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.search(userDetails.getUsername(), category, type, startDate, endDate, description));
    }

    @PostMapping(value = "/import/csv", consumes = "multipart/form-data")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> importCsv(
            @Parameter(description = "Archivo CSV", required = true)
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            service.importCsv(file, userDetails.getUsername());
            return ResponseEntity.ok("Importación completada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error durante la importación: " + e.getMessage());
        }
    }
}