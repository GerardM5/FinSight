package org.example.finsight.stats.service;

import org.example.finsight.auth.model.User;
import org.example.finsight.auth.service.UserService;
import org.example.finsight.stats.dto.CategorySummaryProjection;
import org.example.finsight.stats.dto.MonthlySummaryDTO;
import org.example.finsight.stats.dto.MonthlySummaryProjection;
import org.example.finsight.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsService {

    private final TransactionRepository repository;
    private final UserService userService;

    public StatsService(TransactionRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public List<MonthlySummaryDTO> getMonthlySummary(String email) {
        User user = userService.findByEmail(email);
        List<MonthlySummaryProjection> projections = repository.getMonthlySummaryByUser(user);
        return projections.stream()
                .map(p -> new MonthlySummaryDTO(
                        p.getMonth(),
                        p.getIncome(),
                        p.getExpense()))
                .toList();
    }

    public List<CategorySummaryProjection> getExpensesByCategory(String email) {
        User user = userService.findByEmail(email);
        return repository.getExpenseSummaryByCategory(user);
    }
}