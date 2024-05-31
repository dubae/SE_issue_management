// CreateIssue.java
package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class CreateIssue {
    private JFrame frame;
    private JTextField textFieldTitle;
    private JTextArea textAreaDescription;
    private JComboBox<String> comboBoxPriority;
    private JComboBox<String> comboBoxStatus;

    private IssueService issueService;
    private ProjectService projectService;
    private String username;
    private String password;

    public CreateIssue(IssueService issueService, ProjectService projectService, String username) {
        this.issueService = issueService;
        this.projectService = projectService;
        this.username = username;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Issue Title:");
        lblTitle.setBounds(50, 30, 100, 20);
        frame.getContentPane().add(lblTitle);

        textFieldTitle = new JTextField();
        textFieldTitle.setBounds(150, 30, 250, 25);
        frame.getContentPane().add(textFieldTitle);

        JLabel lblDescription = new JLabel("Issue Description:");
        lblDescription.setBounds(50, 70, 150, 20);
        frame.getContentPane().add(lblDescription);

        textAreaDescription = new JTextArea();
        textAreaDescription.setBounds(150, 70, 250, 100);
        frame.getContentPane().add(textAreaDescription);

        JLabel lblPriority = new JLabel("Priority:");
        lblPriority.setBounds(50, 200, 100, 20);
        frame.getContentPane().add(lblPriority);

        comboBoxPriority = new JComboBox<>();
        comboBoxPriority.setBounds(150, 200, 150, 25);
        comboBoxPriority.addItem("major");
        comboBoxPriority.addItem("critical");
        comboBoxPriority.addItem("blocker");
        comboBoxPriority.addItem("minor");
        comboBoxPriority.addItem("trivial");
        frame.getContentPane().add(comboBoxPriority);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(50, 240, 100, 20);
        frame.getContentPane().add(lblStatus);

        comboBoxStatus = new JComboBox<>();
        comboBoxStatus.setBounds(150, 240, 150, 25);
        comboBoxStatus.addItem("new");
        comboBoxStatus.addItem("assigned");
        comboBoxStatus.addItem("resolved");
        comboBoxStatus.addItem("closed");
        comboBoxStatus.addItem("reopened");
        frame.getContentPane().add(comboBoxStatus);

        JButton btnCreateIssue = new JButton("Create Issue");
        btnCreateIssue.setBounds(200, 300, 150, 30);
        frame.getContentPane().add(btnCreateIssue);

        btnCreateIssue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String title = textFieldTitle.getText();
                    String description = textAreaDescription.getText();
                    String priority = (String) comboBoxPriority.getSelectedItem();
                    String status = (String) comboBoxStatus.getSelectedItem();
                    LocalDate reportedDate = LocalDate.now();

                    // 프로젝트 선택
                    Long projectId = selectProject();
                    if (projectId == null) {
                        JOptionPane.showMessageDialog(frame, "No project selected. Please create a project first.");
                        return;
                    }

                    // IssueDTO 생성 및 설정
                    IssueDTO issueDTO = new IssueDTO();
                    issueDTO.setTitle(title);
                    issueDTO.setDescription(description);
                    issueDTO.setPriority(priority);
                    issueDTO.setStatus(status);
                    issueDTO.setProjectId(projectId); // projectId
                    issueDTO.setWriterId(1L); // writerId
                    issueDTO.setDevId(null); // devId
                    issueDTO.setFixerId(null); // fixerId
                    issueDTO.setComponent(null); // component

                    // IssueService를 통해 이슈 추가
                    issueService.addNewIssue(issueDTO);

                    // 생성된 이슈 페이지로 이동
                    UserPage userPage = new UserPage(issueService, projectService, username, password);
                    userPage.showFrame();
                    frame.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton btnBack = new JButton("뒤로가기");
        btnBack.setBounds(50, 300, 100, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserPage userPage = new UserPage(issueService, projectService, username, password);
                userPage.showFrame();
                frame.dispose();
            }
        });
    }

    private Long selectProject() {
        // 프로젝트 선택 로직 구현
        // 예를 들어, 프로젝트 목록을 가져와서 사용자에게 선택하도록 할 수 있습니다.
        // 이 부분은 필요에 따라 구현하세요.
        List<ProjectDTO> projectList = projectService.findAll();
        if (projectList.isEmpty()) {
            return null;
        }

        // 간단한 예로 첫 번째 프로젝트를 선택하도록 하겠습니다.
        return projectList.get(0).getProjectid();
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
