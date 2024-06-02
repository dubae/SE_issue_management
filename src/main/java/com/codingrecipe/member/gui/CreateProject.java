package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateProject {
    private JFrame frame;
    private JTextField textFieldProjectId;
    private JTextField textFieldProjectName;
    private JTextField textFieldProjectDescription;
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
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblProjectId = new JLabel("Project ID:");
        lblProjectId.setBounds(50, 30, 100, 20);
        frame.getContentPane().add(lblProjectId);

        textFieldProjectId = new JTextField();
        textFieldProjectId.setBounds(150, 30, 250, 25);
        frame.getContentPane().add(textFieldProjectId);

        JLabel lblProjectName = new JLabel("Project Name:");
        lblProjectName.setBounds(50, 70, 100, 20);
        frame.getContentPane().add(lblProjectName);

        textFieldProjectName = new JTextField();
        textFieldProjectName.setBounds(150, 70, 250, 25);
        frame.getContentPane().add(textFieldProjectName);

        JLabel lblProjectDescription = new JLabel("Description:");
        lblProjectDescription.setBounds(50, 110, 100, 20);
        frame.getContentPane().add(lblProjectDescription);

        textFieldProjectDescription = new JTextField();
        textFieldProjectDescription.setBounds(150, 110, 250, 25);
        frame.getContentPane().add(textFieldProjectDescription);

        btnCreate = new JButton("Create Project");
        btnCreate.setBounds(150, 160, 150, 30);
        frame.getContentPane().add(btnCreate);

        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if ("admin".equals(username) && "0000".equals(password)) {
                        Long projectId;
                        try {
                            projectId = Long.parseLong(textFieldProjectId.getText());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid Project ID. Please enter a valid number.");
                            return;
                        }

                        String projectName = textFieldProjectName.getText();
                        String projectDescription = textFieldProjectDescription.getText();

                        // 프로젝트 이름 중복 확인
                        if (projectService.isExistProjectName(projectName)) {
                            JOptionPane.showMessageDialog(frame, "Project name already exists. Please choose a different name.");
                            return;
                        }

                        String projectCreatedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        String projectStatus = "Active"; // 프로젝트 상태를 기본값으로 설정

                        // 프로젝트 생성
                        ProjectDTO projectDTO = new ProjectDTO();
                        projectDTO.setProjectid(projectId);
                        projectDTO.setProjectname(projectName);
                        projectDTO.setProjectdescription(projectDescription);
                        projectDTO.setProjectcreatedtime(projectCreatedTime);
                        projectDTO.setProjectstatus(projectStatus);
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