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

        btnCreateIssue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateIssue createIssue = new CreateIssue(issueService, username);
                createIssue.showFrame();
                frame.dispose();
            }
        });

        try {
            Long writerId = Long.parseLong(username); // Convert username to Long
            List<IssueDTO> issues = issueService.findByWriterId(writerId);

            // 디버그 메시지 추가
            System.out.println("Found issues for user " + username + ": " + issues.size());

            int yPosition = 100;
            for (IssueDTO issue : issues) {
                JButton issueButton = new JButton(issue.getTitle());
                issueButton.setBounds(50, yPosition, 400, 30);
                issueButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        IssueDetailPage detailPage = new IssueDetailPage(issueService, issue);
                        detailPage.showFrame();
                        frame.dispose();
                    }
                });
                frame.getContentPane().add(issueButton);
                yPosition += 40;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid user ID format.");
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(frame, "IssueService is null.");
        }
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
