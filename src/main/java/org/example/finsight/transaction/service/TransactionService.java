package org.example.finsight.transaction.service;

import org.example.finsight.auth.model.User;
import org.example.finsight.transaction.model.Transaction;
import org.example.finsight.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.example.finsight.transaction.dto.TransactionDTO;
import org.example.finsight.transaction.mapper.TransactionMapper;
import org.example.finsight.auth.service.UserService;

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
}