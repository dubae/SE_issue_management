package com.codingrecipe.member.gui;

import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.statistics.IssueStatistics;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;

public class IssueStatisticsPage {
    private JFrame frame;
    private IssueStatistics issueStatistics;

    public IssueStatisticsPage(IssueService issueService) {
        this.issueStatistics = new IssueStatistics(issueService);
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton btnShowDailyTrend = new JButton("Show Daily Trend");
        btnShowDailyTrend.addActionListener(e -> {
            Map<LocalDate, Integer> dailyTrend = issueStatistics.getDailyIssueTrend(LocalDate.now().minusDays(30), LocalDate.now());
            StringBuilder sb = new StringBuilder("Daily Issue Trend:\n");
            for (Map.Entry<LocalDate, Integer> entry : dailyTrend.entrySet()) {
                if (entry.getValue() > 0) { // 0 이슈 제외
                    sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" issues\n");
                }
            }
            textArea.setText(sb.toString());
        });

        JButton btnShowMonthlyTrend = new JButton("Show Monthly Trend");
        btnShowMonthlyTrend.addActionListener(e -> {
            Map<Month, Integer> monthlyTrend = issueStatistics.getMonthlyIssueTrend(LocalDate.now().getYear());
            StringBuilder sb = new StringBuilder("Monthly Issue Trend:\n");
            for (Month month : Month.values()) {
                if (monthlyTrend.containsKey(month) && monthlyTrend.get(month) > 0) { // 0 이슈 제외
                    sb.append(month).append(": ").append(monthlyTrend.get(month)).append(" issues\n");
                }
            }
            textArea.setText(sb.toString());
        });

        JPanel panel = new JPanel();
        panel.add(btnShowDailyTrend);
        panel.add(btnShowMonthlyTrend);

        frame.getContentPane().add(panel, BorderLayout.SOUTH);
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
