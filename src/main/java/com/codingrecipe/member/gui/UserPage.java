package com.codingrecipe.member.gui;

import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;

import javax.swing.*;

public class UserPage {
    private JFrame frame;
    private final MemberService memberService;
    private final IssueService issueService;
    private final String username;

    public UserPage(MemberService memberService, IssueService issueService, String username) {
        this.memberService = memberService;
        this.issueService = issueService;
        this.username = username;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("User Page");
        lblTitle.setBounds(150, 20, 100, 30);
        frame.getContentPane().add(lblTitle);

        JButton btnCreateIssue = new JButton("Create Issue");
        btnCreateIssue.setBounds(150, 80, 120, 30);
        frame.getContentPane().add(btnCreateIssue);

        btnCreateIssue.addActionListener(e -> {
            CreateIssue createIssue = new CreateIssue(issueService, username);
            createIssue.showFrame();
            frame.dispose();
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}