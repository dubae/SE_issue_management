package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
        frame.setBounds(100, 100, 500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("User Page");
        lblTitle.setBounds(200, 20, 100, 30);
        frame.getContentPane().add(lblTitle);

        JButton btnCreateIssue = new JButton("Create Issue");
        btnCreateIssue.setBounds(50, 60, 150, 30);
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