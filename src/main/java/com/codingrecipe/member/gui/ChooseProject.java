package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.ProjectService;
import com.codingrecipe.member.service.MemberService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChooseProject {
    private JFrame frame;
    private final IssueService issueService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final String username;
    private final String password;

    public ChooseProject(IssueService issueService, ProjectService projectService, MemberService memberService, String username, String password) {
        this.issueService = issueService;
        this.projectService = projectService;
        this.memberService = memberService;
        this.username = username;
        this.password = password;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblSelectProject = new JLabel("Select Project:");
        lblSelectProject.setBounds(50, 30, 100, 20);
        frame.getContentPane().add(lblSelectProject);

        JPanel projectPanel = new JPanel();
        projectPanel.setBounds(50, 60, 350, 200);
        frame.getContentPane().add(projectPanel);

        List<ProjectDTO> projectList = projectService.findAll();
        ButtonGroup projectButtonGroup = new ButtonGroup();
        for (ProjectDTO project : projectList) {
            JRadioButton projectButton = new JRadioButton(project.getProjectname());
            projectButton.setActionCommand(project.getProjectid().toString());
            projectButtonGroup.add(projectButton);
            projectPanel.add(projectButton);
        }

        JButton btnEdit = new JButton("Edit");
        btnEdit.setBounds(150, 270, 150, 30);
        frame.getContentPane().add(btnEdit);

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (projectButtonGroup.getSelection() == null) {
                    JOptionPane.showMessageDialog(frame, "Please select a project.");
                    return;
                }
                Long selectedProjectId = Long.valueOf(projectButtonGroup.getSelection().getActionCommand());
                EditProject editProject = new EditProject(issueService, projectService, memberService, selectedProjectId, username, password);
                editProject.showFrame();
                frame.dispose();
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(150, 310, 150, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminPage adminPage = new AdminPage(issueService, projectService, memberService, username, password);
                adminPage.showFrame();
                frame.dispose();
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
