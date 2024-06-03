package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueService;

import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IssueStatistics {
    private final IssueService issueService;

    public IssueStatistics(IssueService issueService) {
        this.issueService = issueService;
    }

    public Map<LocalDate, Integer> getDailyIssueTrend(LocalDate startDate, LocalDate endDate) {
        List<IssueDTO> issues = issueService.findAllIssue();
        Map<LocalDate, Integer> dailyTrend = issues.stream()
                .filter(issue -> {
                    LocalDate createdAt = issue.getCreatedAt();
                    return (createdAt.isEqual(startDate) || createdAt.isAfter(startDate)) &&
                            (createdAt.isEqual(endDate) || createdAt.isBefore(endDate));
                })
                .collect(Collectors.groupingBy(IssueDTO::getCreatedAt, Collectors.summingInt(e -> 1)));
        return dailyTrend;
    }

    public Map<Month, Integer> getMonthlyIssueTrend(int year) {
        List<IssueDTO> issues = issueService.findAllIssue();
        Map<Month, Integer> monthlyTrend = new EnumMap<>(Month.class);
        for (Month month : Month.values()) {
            int issueCount = (int) issues.stream()
                    .filter(issue -> {
                        LocalDate createdAt = issue.getCreatedAt();
                        return createdAt.getYear() == year && createdAt.getMonth() == month;
                    })
                    .count();
            monthlyTrend.put(month, issueCount);
        }

        Map<Month, Integer> sortedMonthlyTrend = new LinkedHashMap<>(); //1월~12월 순
        for (Month month : Month.values()) {
            if (monthlyTrend.containsKey(month) && monthlyTrend.get(month) > 0) {
                sortedMonthlyTrend.put(month, monthlyTrend.get(month));
            }
        }
        return sortedMonthlyTrend;
    }
}
