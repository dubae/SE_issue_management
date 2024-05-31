// UserPage.java
package com.codingrecipe.member.gui;

import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserPage {
    private JFrame frame;
    private final IssueService issueService;
    private final ProjectService projectService;
    private final String username;
    private final String password;

    public UserPage(IssueService issueService, ProjectService projectService, String username, String password) {
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

        JButton btnCreateProject = new JButton("Create Project");
        btnCreateProject.setBounds(150, 100, 150, 30);
        frame.getContentPane().add(btnCreateProject);

        btnCreateProject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateProject createProject = new CreateProject(issueService, projectService, username, password);
                createProject.showFrame();
                frame.dispose();
            }
        });

        JButton btnCreateIssue = new JButton("Create Issue");
        btnCreateIssue.setBounds(150, 150, 150, 30);
        frame.getContentPane().add(btnCreateIssue);

        btnCreateIssue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateIssue createIssue = new CreateIssue(issueService, projectService, username);
                createIssue.showFrame();
                frame.dispose();
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
