package com.codingrecipe.member.gui;

import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPage {
    private JFrame frame;
    private final IssueService issueService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final String username;
    private final String password;

    public AdminPage(IssueService issueService, ProjectService projectService, MemberService memberService, String username, String password) {
        this.issueService = issueService;
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
        btnCreateProject.setBounds(150, 100, 150, 30);
        frame.getContentPane().add(btnCreateProject);

        btnCreateProject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateProject createProject = new CreateProject(issueService, projectService, memberService, username, password);
                createProject.showFrame();
                frame.dispose();
            }
        });

        JButton btnEditProject = new JButton("Edit Project");
        btnEditProject.setBounds(150, 150, 150, 30);
        frame.getContentPane().add(btnEditProject);

        btnEditProject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChooseProject chooseProject = new ChooseProject(issueService, projectService, memberService, username, password);
                chooseProject.showFrame();
                frame.dispose();
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(150, 200, 150, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginScreen loginScreen = new LoginScreen(memberService, issueService, projectService);
                loginScreen.showFrame();
                frame.dispose();
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
