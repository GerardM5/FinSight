package org.example.finsight.transaction.mapper;

import org.example.finsight.transaction.dto.TransactionDTO;
import org.example.finsight.transaction.model.Transaction;

public class TransactionMapper {

    public static TransactionDTO toDTO(Transaction entity) {
        return new TransactionDTO(
            entity.getId(),
            entity.getDescription(),
            entity.getAmount(),
            entity.getType(),
            entity.getCategory(),
            entity.getDate()
        );
    }

    public static Transaction toEntity(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setId(dto.getId());
        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setCategory(dto.getCategory());
        transaction.setDate(dto.getDate());
        return transaction;
    }
}