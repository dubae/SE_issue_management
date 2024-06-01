package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateProject {
    private JFrame frame;
    private JTextField textFieldProjectName;
    private JButton btnCreate;

    private final IssueService issueService;
    private final ProjectService projectService;
    private final String username;
    private final String password;

    public CreateProject(IssueService issueService, ProjectService projectService, String username, String password) {
        this.issueService = issueService;
        this.projectService = projectService;
        this.username = username;
        this.password = password;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblProjectName = new JLabel("Project Name:");
        lblProjectName.setBounds(50, 30, 100, 20);
        frame.getContentPane().add(lblProjectName);

        textFieldProjectName = new JTextField();
        textFieldProjectName.setBounds(150, 30, 250, 25);
        frame.getContentPane().add(textFieldProjectName);

        btnCreate = new JButton("Create Project");
        btnCreate.setBounds(150, 80, 150, 30);
        frame.getContentPane().add(btnCreate);

        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if ("admin".equals(username) && "0000".equals(password)) {
                        String projectName = textFieldProjectName.getText();

                        // 프로젝트 생성
                        ProjectDTO projectDTO = new ProjectDTO();
                        projectDTO.setProjectname(projectName);
                        projectService.register(projectDTO);

                        // 생성된 프로젝트로 이동
                        UserPage userPage = new UserPage(issueService, projectService, username, password);
                        userPage.showFrame();
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Can only be accessed by admin.");
                        UserPage userPage = new UserPage(issueService, projectService, username, password);
                        userPage.showFrame();
                        frame.dispose();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
