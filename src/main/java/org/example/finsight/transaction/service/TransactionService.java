package org.example.finsight.transaction.service;

import org.example.finsight.auth.model.User;
import org.example.finsight.transaction.model.Transaction;
import org.example.finsight.transaction.model.TransactionType;
import org.example.finsight.transaction.repository.TransactionRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.example.finsight.transaction.dto.TransactionDTO;
import org.example.finsight.transaction.mapper.TransactionMapper;
import org.example.finsight.auth.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final UserService userService;

    public TransactionService(TransactionRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public TransactionDTO createTransaction(TransactionDTO dto, String userEmail) {
        User user = userService.findByEmail(userEmail);
        Transaction transaction = TransactionMapper.toEntity(dto);
        transaction.setUser(user);
        return TransactionMapper.toDTO(repository.save(transaction));
    }

    public List<TransactionDTO> getTransactionsByUser(String userEmail) {
        User user = userService.findByEmail(userEmail);
        return repository.findAllByUser(user).stream()
                .map(TransactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return TransactionMapper.toDTO(transaction);
    }

    public void deleteTransaction(Long id, String userEmail) {
        User user = userService.findByEmail(userEmail);
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar esta transacción.");
        }

        repository.delete(transaction);
    }

    public TransactionDTO updateTransaction(Long id, TransactionDTO dto, String userEmail) {
        User user = userService.findByEmail(userEmail);
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso para modificar esta transacción.");
        }

        transaction.setAmount(dto.getAmount());
        transaction.setDescription(dto.getDescription());
        transaction.setDate(dto.getDate());
        transaction.setCategory(dto.getCategory());
        transaction.setType(dto.getType());

        return TransactionMapper.toDTO(repository.save(transaction));
    }

    public List<TransactionDTO> search(String username, String category, TransactionType type,
                                       LocalDate startDate, LocalDate endDate, String description) {

        User user = userService.findByEmail(username);
        List<Transaction> transactions = repository.searchTransactions(user, category, type, startDate, endDate, description);
        return transactions.stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }

    public void importCsv(MultipartFile file, String username) throws IOException {
        User user = userService.findByEmail(username);

        List<Transaction> transactions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length < 4) continue;
                if (parts[0].equals("Concepto")) continue;
                String description = parts[0].trim();
                LocalDate date = LocalDate.parse(parts[1].trim(), formatter);
                BigDecimal amount = new BigDecimal(parts[2].trim().replace("EUR", "")
                        .replace(".", "")
                        .replace(",", "."));
                TransactionType type = amount.compareTo(BigDecimal.ZERO) >= 0 ? TransactionType.INCOME : TransactionType.EXPENSE;

                Transaction t = new Transaction();
                t.setDescription(description);
                t.setDate(date);
                t.setAmount(amount.abs());
                t.setType(type);
                t.setUser(user);
                t.setCategory("Uncategorized");

                transactions.add(t);
            }
        }

        repository.saveAll(transactions);
    }

}