package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class CreateIssue {
    private JFrame frame;
    private JTextField textFieldTitle;
    private JTextArea textAreaDescription;
    private JComboBox<String> comboBoxPriority;
    private JComboBox<String> comboBoxStatus;
    private JComboBox<String> comboBoxComponent; // Component ComboBox 추가
    private final IssueService issueService; // 이슈 서비스 객체
    private final ProjectService projectService;
    private final String username;

    // 생성자 수정 - IssueService 객체를 전달받도록 변경
    public CreateIssue(IssueService issueService, ProjectService projectService, String username) {
        this.issueService = issueService; // 이슈 서비스 객체 설정
        this.projectService = projectService; // 프로젝트 서비스 객체 설정
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

        JLabel lblComponent = new JLabel("Component:"); // Component 레이블 추가
        lblComponent.setBounds(50, 280, 100, 20);
        frame.getContentPane().add(lblComponent);

        comboBoxComponent = new JComboBox<>(); // Component ComboBox 추가
        comboBoxComponent.setBounds(150, 280, 150, 25);
        comboBoxComponent.addItem("UI");
        comboBoxComponent.addItem("Backend");
        comboBoxComponent.addItem("Database");
        // 필요한 Component 추가
        frame.getContentPane().add(comboBoxComponent);

        JButton btnCreateIssue = new JButton("Create Issue");
        btnCreateIssue.setBounds(200, 330, 150, 30);
        frame.getContentPane().add(btnCreateIssue);

        btnCreateIssue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String title = textFieldTitle.getText();
                    String description = textAreaDescription.getText();
                    String priority = (String) comboBoxPriority.getSelectedItem();
                    String status = (String) comboBoxStatus.getSelectedItem();
                    String component = (String) comboBoxComponent.getSelectedItem(); // 선택된 Component 값 가져오기
                    LocalDate reportedDate = LocalDate.now();

                    // IssueDTO 생성 및 설정
                    IssueDTO issueDTO = new IssueDTO();
                    issueDTO.setTitle(title);
                    issueDTO.setDescription(description);
                    issueDTO.setPriority(priority);
                    issueDTO.setStatus(status);
                    issueDTO.setComponent(component); // Component 설정
                    issueDTO.setProjectId(projectService.createProjectAndGetId("Sample Project")); // 프로젝트 생성 및 ID 가져오기
                    issueDTO.setWriterId(1L); // writerId

                    // IssueService를 통해 이슈 추가
                    issueService.addNewIssue(issueDTO);

                    // 생성된 이슈 페이지로 이동
                    UserPage userPage = new UserPage(null, issueService, username);
                    userPage.showFrame();
                    frame.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton btnBack = new JButton("뒤로가기");
        btnBack.setBounds(50, 330, 100, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserPage userPage = new UserPage(null, issueService, username);
                userPage.showFrame();
                frame.dispose();
            }
        });
    }


    public void showFrame() {
        frame.setVisible(true);
    }
}
