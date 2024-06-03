package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueCommentService;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.*;
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
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel issuePanel = new JPanel();
        issuePanel.setLayout(new GridLayout(0, 1));

        List<IssueDTO> issueList = issueService.findAllIssue();
        for (IssueDTO issue : issueList) {
            JButton issueButton = new JButton(issue.getTitle());
            issueButton.addActionListener(e -> {
                IssueDetailsPage issueDetailsPage = new IssueDetailsPage(issueService, issueCommentService, projectService, memberService, username, password, issue);
                issueDetailsPage.showFrame();
                frame.dispose();
            });
            issuePanel.add(issueButton);
        }

        JScrollPane scrollPane = new JScrollPane(issuePanel);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton btnBack = new JButton("뒤로가기");
        btnBack.addActionListener(e -> {
            UserPage userPage = new UserPage(issueService, issueCommentService, projectService, memberService, username, password);
            userPage.showFrame();
            frame.dispose();
        });

        frame.getContentPane().add(btnBack, BorderLayout.SOUTH);
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
