package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ViewIssue {
    private JFrame frame;
    private final IssueService issueService;
    private final ProjectService projectService;
    private final String username;
    private final String password;

    public ViewIssue(IssueService issueService, ProjectService projectService, String username, String password) {
        this.issueService = issueService;
        this.projectService = projectService;
        this.username = username;
        this.password = password;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        List<IssueDTO> issues = issueService.findAllIssue();

        // 각 이슈를 버튼으로 생성
        int y = 30;
        for (IssueDTO issue : issues) {
            JButton btnIssue = new JButton(issue.getTitle());
            btnIssue.setBounds(50, y, 250, 25);
            frame.getContentPane().add(btnIssue);

            btnIssue.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    IssueDetailsPage issueDetailsPage = new IssueDetailsPage(issueService, projectService, username, password, issue);
                    issueDetailsPage.showFrame();
                    frame.dispose();
                }
            });

            y += 30;
        }
        JButton btnBack = new JButton("뒤로가기");
        btnBack.setBounds(50, y, 100, 25);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserPage userPage = new UserPage(issueService, projectService, username, password);
                userPage.showFrame();
                frame.dispose();
            }
        });
    }


    public void showFrame() {
        frame.setVisible(true);
    }
}