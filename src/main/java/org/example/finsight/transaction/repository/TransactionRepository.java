package org.example.finsight.transaction.repository;

import org.example.finsight.auth.model.User;
import org.example.finsight.stats.dto.CategorySummaryProjection;
import org.example.finsight.stats.dto.MonthlySummaryProjection;
import org.example.finsight.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);

    @Query("""
    SELECT 
        FUNCTION('to_char', t.date, 'YYYY-MM') as month,
        SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) as income,
        SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) as expense
    FROM Transaction t
    WHERE t.user = :user
    GROUP BY FUNCTION('to_char', t.date, 'YYYY-MM')
    ORDER BY 1
""")
    List<MonthlySummaryProjection> getMonthlySummaryByUser(@Param("user") User user);

    @Query("""
    SELECT t.category AS category, SUM(t.amount) AS total
    FROM Transaction t
    WHERE t.user = :user AND t.type = 'EXPENSE'
    GROUP BY t.category
    ORDER BY total DESC
""")
    List<CategorySummaryProjection> getExpenseSummaryByCategory(@Param("user") User user);
}