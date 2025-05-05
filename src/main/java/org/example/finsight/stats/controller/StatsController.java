package org.example.finsight.stats.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.finsight.stats.dto.CategorySummaryProjection;
import org.example.finsight.stats.dto.MonthlySummaryDTO;
import org.example.finsight.stats.dto.MonthlySummaryProjection;
import org.example.finsight.stats.service.StatsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stats")
@SecurityRequirement(name = "bearerAuth")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/monthly-summary")
    public List<MonthlySummaryDTO> getMonthlySummary(@AuthenticationPrincipal UserDetails userDetails) {
        return statsService.getMonthlySummary(userDetails.getUsername());
    }

    @GetMapping("/expenses-by-category")
    public List<CategorySummaryProjection> getExpensesByCategory(@AuthenticationPrincipal UserDetails userDetails) {
        return statsService.getExpensesByCategory(userDetails.getUsername());
    }
}