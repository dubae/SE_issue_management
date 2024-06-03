package com.codingrecipe.member.gui;

import com.codingrecipe.member.service.IssueCommentService;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;
import com.codingrecipe.member.gui.IssueStatistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

public class IssueStatisticsPage {
    private JFrame frame;
    private IssueStatistics issueStatistics;

    private MemberService memberService;
    private IssueService issueService;
    private ProjectService projectService;
    private IssueCommentService issueCommentService;
    private String username;
    private String password;

    public IssueStatisticsPage(IssueService issueService, IssueCommentService issueCommentService, ProjectService projectService, MemberService memberService, String username, String password) {
        this.issueStatistics = new IssueStatistics(issueService);
        this.issueService = issueService;
        this.issueCommentService = issueCommentService;
        this.projectService = projectService;
        this.memberService = memberService;
        this.username = username;
        this.password = password;
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

        JButton btnBack = new JButton("뒤로가기");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserPage userPage = new UserPage(issueService, issueCommentService, projectService, memberService, username, password);
                userPage.showFrame();
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.add(btnShowDailyTrend);
        panel.add(btnShowMonthlyTrend);
        panel.add(btnBack);

        frame.getContentPane().add(panel, BorderLayout.SOUTH);
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
