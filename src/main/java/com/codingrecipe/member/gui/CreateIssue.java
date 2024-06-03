package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.IssueCommentService;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

public class CreateIssue {
    private JFrame frame;
    private JTextField textFieldTitle;
    private JTextArea textAreaDescription;
    private JComboBox<String> comboBoxPriority;
    private JComboBox<String> comboBoxAssignee;

    private MemberService memberService;
    private IssueService issueService;
    private ProjectService projectService;
    private IssueCommentService issueCommentService;
    private String username;
    private String password;

    private Long selectedProjectId;

    public CreateIssue(IssueService issueService, ProjectService projectService, MemberService memberService, String username, String password) {
        this.issueService = issueService;
        this.projectService = projectService;
        this.memberService = memberService;
        this.username = username;
        this.password = password;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 550);
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

        JLabel lblAssignee = new JLabel("Assignee:");
        lblAssignee.setBounds(50, 240, 100, 20);
        frame.getContentPane().add(lblAssignee);

        comboBoxAssignee = new JComboBox<>();
        comboBoxAssignee.setBounds(150, 240, 250, 25);
        frame.getContentPane().add(comboBoxAssignee);

        comboBoxAssignee.addItem("No Assignee"); // 이슈추가만 하고 assignee 등록 안하는 경우
        List<MemberDTO> members = memberService.findAll();
        for (MemberDTO member : members) {
            comboBoxAssignee.addItem(member.getUserid());
        }

        JLabel lblSelectProject = new JLabel("Select Project:");
        lblSelectProject.setBounds(50, 280, 100, 20);
        frame.getContentPane().add(lblSelectProject);

        JPanel projectPanel = new JPanel();
        projectPanel.setBounds(50, 310, 350, 100);
        frame.getContentPane().add(projectPanel);

        List<ProjectDTO> projectList = projectService.findAll();
        ButtonGroup projectButtonGroup = new ButtonGroup();
        for (ProjectDTO project : projectList) {
            JRadioButton projectButton = new JRadioButton(project.getProjectname());
            projectButton.setActionCommand(project.getProjectid().toString());
            projectButtonGroup.add(projectButton);
            projectPanel.add(projectButton);
        }

        JButton btnCreateIssue = new JButton("Create Issue");
        btnCreateIssue.setBounds(200, 420, 150, 30);
        frame.getContentPane().add(btnCreateIssue);

        btnCreateIssue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String title = textFieldTitle.getText();
                    String description = textAreaDescription.getText();
                    String priority = (String) comboBoxPriority.getSelectedItem();
                    String assignee = (String) comboBoxAssignee.getSelectedItem();
                    LocalDate createdAt = LocalDate.now();

                    // 프로젝트 선택
                    if (projectButtonGroup.getSelection() == null) {
                        JOptionPane.showMessageDialog(frame, "Please select a project.");
                        return;
                    }
                    selectedProjectId = Long.valueOf(projectButtonGroup.getSelection().getActionCommand());
                    if (selectedProjectId == null) {
                        JOptionPane.showMessageDialog(frame, "No project selected. Please create a project first.");
                        return;
                    }

                    IssueDTO issueDTO = new IssueDTO();
                    issueDTO.setTitle(title);
                    issueDTO.setDescription(description);
                    issueDTO.setPriority(priority);
                    issueDTO.setStatus(assignee.equals("No Assignee") ? "new" : "assigned"); // 상태 설정
                    issueDTO.setProjectId(selectedProjectId); // projectId
                    issueDTO.setWriterId("1L"); // writerId
                    issueDTO.setDevId(assignee.equals("No Assignee") ? null : assignee); // devId
                    issueDTO.setFixerId(null); // fixerId
                    issueDTO.setComponent(null); // component
                    issueDTO.setCreatedAt(createdAt); // 생성 시간 설정

                    issueDTO.setProjectDTO(projectService.findByProjectId(selectedProjectId));

                    // IssueService를 통해 이슈 추가
                    issueService.addNewIssue(issueDTO);

                    // 생성된 이슈 페이지로 이동
                    UserPage userPage = new UserPage(issueService, issueCommentService, projectService, memberService, username, password);
                    userPage.showFrame();
                    frame.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(50, 420, 100, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserPage userPage = new UserPage(issueService, issueCommentService, projectService, memberService, username, password);
                userPage.showFrame();
                frame.dispose();
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
