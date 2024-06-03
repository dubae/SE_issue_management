package com.codingrecipe.member.gui;

import com.codingrecipe.member.service.IssueCommentService;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserPage {
    private JFrame frame;
    private final IssueService issueService;
    private final IssueCommentService issueCommentService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final String username;
    private final String password;

    public UserPage(IssueService issueService, IssueCommentService issueCommentService, ProjectService projectService, MemberService memberService, String username, String password) {
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
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnCreateProject = new JButton("Create Project");
        btnCreateProject.setBounds(150, 50, 150, 30);
        frame.getContentPane().add(btnCreateProject);

        btnCreateProject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateProject createProject = new CreateProject(issueService, issueCommentService, projectService, memberService, username, password);
                createProject.showFrame();
                frame.dispose();
            }
        });

        JButton btnCreateIssue = new JButton("Create Issue");
        btnCreateIssue.setBounds(150, 100, 150, 30);
        frame.getContentPane().add(btnCreateIssue);

        btnCreateIssue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateIssue createIssue = new CreateIssue(issueService, projectService, memberService, username, password);
                createIssue.showFrame();
                frame.dispose();
            }
        });

        JButton btnViewIssues = new JButton("View Issues");
        btnViewIssues.setBounds(150, 150, 150, 30);
        frame.getContentPane().add(btnViewIssues);

        btnViewIssues.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewIssue viewIssue = new ViewIssue(issueService, issueCommentService, projectService, memberService, username, password);
                viewIssue.showFrame();
                frame.dispose();
            }
        });

        JButton btnViewStatistics = new JButton("View Statistics");
        btnViewStatistics.setBounds(150, 200, 150, 30);
        frame.getContentPane().add(btnViewStatistics);

        btnViewStatistics.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IssueStatisticsPage issueStatisticsPage = new IssueStatisticsPage(issueService, issueCommentService,projectService, memberService, username, password);
                issueStatisticsPage.showFrame();
                frame.dispose();
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
