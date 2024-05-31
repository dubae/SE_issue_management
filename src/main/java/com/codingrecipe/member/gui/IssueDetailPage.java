package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IssueDetailPage {
    private JFrame frame;
    private final IssueService issueService;
    private final IssueDTO issue;

    public IssueDetailPage(IssueService issueService, IssueDTO issue) {
        this.issueService = issueService;
        this.issue = issue;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Issue Detail");
        lblTitle.setBounds(180, 20, 100, 30);
        frame.getContentPane().add(lblTitle);

        JLabel lblIssueTitle = new JLabel("Title:");
        lblIssueTitle.setBounds(50, 70, 100, 20);
        frame.getContentPane().add(lblIssueTitle);

        JTextField textFieldTitle = new JTextField(issue.getTitle());
        textFieldTitle.setBounds(150, 70, 250, 25);
        textFieldTitle.setEditable(false);
        frame.getContentPane().add(textFieldTitle);

        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setBounds(50, 110, 100, 20);
        frame.getContentPane().add(lblDescription);

        JTextArea textAreaDescription = new JTextArea(issue.getDescription());
        textAreaDescription.setBounds(150, 110, 250, 100);
        textAreaDescription.setEditable(false);
        frame.getContentPane().add(textAreaDescription);

        JLabel lblPriority = new JLabel("Priority:");
        lblPriority.setBounds(50, 230, 100, 20);
        frame.getContentPane().add(lblPriority);

        JTextField textFieldPriority = new JTextField(issue.getPriority());
        textFieldPriority.setBounds(150, 230, 150, 25);
        textFieldPriority.setEditable(false);
        frame.getContentPane().add(textFieldPriority);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(50, 270, 100, 20);
        frame.getContentPane().add(lblStatus);

        JTextField textFieldStatus = new JTextField(issue.getStatus());
        textFieldStatus.setBounds(150, 270, 150, 25);
        textFieldStatus.setEditable(false);
        frame.getContentPane().add(textFieldStatus);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(150, 320, 100, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserPage userPage = new UserPage(null, issueService, issue.getWriterId().toString());
                userPage.showFrame();
                frame.dispose();
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
