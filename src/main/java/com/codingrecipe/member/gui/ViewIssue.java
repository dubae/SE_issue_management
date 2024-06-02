package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueCommentService;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ViewIssue {
    private JFrame frame;
    private final IssueService issueService;
    private final IssueCommentService issueCommentService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final String username;
    private final String password;

    public ViewIssue(IssueService issueService, IssueCommentService issueCommentService, ProjectService projectService, MemberService memberService, String username, String password) {
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
        frame.setBounds(100, 100, 450, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBounds(50, 30, 350, 250);
        frame.getContentPane().add(scrollPane);

        List<IssueDTO> issues = issueService.findAllIssue();

        // 각 이슈를 버튼으로 생성
        int y = 10;
        for (IssueDTO issue : issues) {
            JButton btnIssue = new JButton(issue.getTitle());
            btnIssue.setBounds(10, y, 300, 25);
            panel.add(btnIssue);

            btnIssue.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    IssueDetailsPage issueDetailsPage = new IssueDetailsPage(issueService, issueCommentService, projectService, memberService, username, password, issue);
                    issueDetailsPage.showFrame();
                    frame.dispose();
                }
            });

            y += 30;
        }

        panel.setPreferredSize(new java.awt.Dimension(350, y));

        JButton btnSearch = new JButton("Search Issues");
        btnSearch.setBounds(50, y + 20, 150, 25);
        frame.getContentPane().add(btnSearch);

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SearchIssue searchIssue = new SearchIssue(issueService, issueCommentService,projectService, memberService, username, password);
                searchIssue.showFrame();
                frame.dispose();
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(50, y + 50, 100, 25);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserPage userPage = new UserPage(issueService, issueCommentService,projectService, memberService, username, password);
                userPage.showFrame();
                frame.dispose();
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
