package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditIssue {
    private JFrame frame;
    private JTextField textFieldTitle;
    private JTextArea textAreaDescription;
    private JComboBox<String> comboBoxPriority;
    private JComboBox<String> comboBoxStatus;

    private final IssueService issueService;
    private final ProjectService projectService;
    private final String username;
    private final String password;
    private final IssueDTO issueDTO;

    public EditIssue(IssueService issueService, ProjectService projectService, String username, String password, IssueDTO issueDTO) {
        this.issueService = issueService;
        this.projectService = projectService;
        this.username = username;
        this.password = password;
        this.issueDTO = issueDTO;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Issue Title:");
        lblTitle.setBounds(50, 30, 100, 20);
        frame.getContentPane().add(lblTitle);

        textFieldTitle = new JTextField(issueDTO.getTitle());
        textFieldTitle.setBounds(150, 30, 250, 25);
        frame.getContentPane().add(textFieldTitle);

        JLabel lblDescription = new JLabel("Issue Description:");
        lblDescription.setBounds(50, 70, 150, 20);
        frame.getContentPane().add(lblDescription);

        textAreaDescription = new JTextArea(issueDTO.getDescription());
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
        comboBoxPriority.setSelectedItem(issueDTO.getPriority());
        frame.getContentPane().add(comboBoxPriority);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(50, 240, 100, 20);
        frame.getContentPane().add(lblStatus);

        comboBoxStatus = new JComboBox<>();
        comboBoxStatus.setBounds(150, 240, 150, 25);
        comboBoxStatus.addItem("new");
        comboBoxStatus.addItem("in progress");
        comboBoxStatus.addItem("resolved");
        comboBoxStatus.addItem("closed");
        comboBoxStatus.setSelectedItem(issueDTO.getStatus());
        frame.getContentPane().add(comboBoxStatus);

        JButton btnUpdateIssue = new JButton("Update Issue");
        btnUpdateIssue.setBounds(200, 420, 150, 30);
        frame.getContentPane().add(btnUpdateIssue);

        btnUpdateIssue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = textFieldTitle.getText();
                String description = textAreaDescription.getText();
                String priority = (String) comboBoxPriority.getSelectedItem();
                String status = (String) comboBoxStatus.getSelectedItem();

                issueDTO.setTitle(title);
                issueDTO.setDescription(description);
                issueDTO.setPriority(priority);
                issueDTO.setStatus(status);
                issueDTO.setFixerId(issueDTO.getWriterId()); // 수정한 사람의 userid를 fixerid로 설정

                // 이슈 업데이트
                issueService.changeStatus(issueDTO.getId(), status);

                // 이전 페이지로 이동
                ViewIssue viewIssue = new ViewIssue(issueService, projectService, username, password);
                viewIssue.showFrame();
                frame.dispose();
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(50, 420, 100, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 이전 페이지로 이동
                ViewIssue viewIssue = new ViewIssue(issueService, projectService, username, password);
                viewIssue.showFrame();
                frame.dispose();
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}