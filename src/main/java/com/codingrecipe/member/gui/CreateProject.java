package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.service.IssueCommentService;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.ProjectService;
import com.codingrecipe.member.service.MemberService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CreateProject {
    private JFrame frame;
    private JTextField textFieldProjectName;
    private JTextField textFieldProjectDescription;
    private JComboBox<String> comboBoxPL;
    private JComboBox<String> comboBoxTester;
    private JComboBox<String> comboBoxDev;
    private JButton btnCreate;

    private final IssueService issueService;
    private final IssueCommentService issueCommentService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final String username;
    private final String password;

    public CreateProject(IssueService issueService, IssueCommentService issueCommentService, ProjectService projectService, MemberService memberService, String username, String password) {
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
        frame.setBounds(100, 100, 450, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblProjectName = new JLabel("Project Name:");
        lblProjectName.setBounds(50, 30, 100, 20);
        frame.getContentPane().add(lblProjectName);

        textFieldProjectName = new JTextField();
        textFieldProjectName.setBounds(150, 30, 250, 25);
        frame.getContentPane().add(textFieldProjectName);

        JLabel lblProjectDescription = new JLabel("Description:");
        lblProjectDescription.setBounds(50, 70, 100, 20);
        frame.getContentPane().add(lblProjectDescription);

        textFieldProjectDescription = new JTextField();
        textFieldProjectDescription.setBounds(150, 70, 250, 25);
        frame.getContentPane().add(textFieldProjectDescription);

        JLabel lblPL = new JLabel("Project Leader:");
        lblPL.setBounds(50, 110, 100, 20);
        frame.getContentPane().add(lblPL);

        comboBoxPL = new JComboBox<>();
        comboBoxPL.setBounds(150, 110, 250, 25);
        frame.getContentPane().add(comboBoxPL);

        JLabel lblTester = new JLabel("Tester:");
        lblTester.setBounds(50, 150, 100, 20);
        frame.getContentPane().add(lblTester);

        comboBoxTester = new JComboBox<>();
        comboBoxTester.setBounds(150, 150, 250, 25);
        frame.getContentPane().add(comboBoxTester);

        JLabel lblDev = new JLabel("Developer:");
        lblDev.setBounds(50, 190, 100, 20);
        frame.getContentPane().add(lblDev);

        comboBoxDev = new JComboBox<>();
        comboBoxDev.setBounds(150, 190, 250, 25);
        frame.getContentPane().add(comboBoxDev);

        loadMembersIntoComboBoxes();

        btnCreate = new JButton("Create Project");
        btnCreate.setBounds(150, 230, 150, 30);
        frame.getContentPane().add(btnCreate);

        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if ("admin".equals(username) && "0000".equals(password)) {
                        String projectName = textFieldProjectName.getText();
                        String projectDescription = textFieldProjectDescription.getText();
                        String projectPL = (String) comboBoxPL.getSelectedItem();
                        String projectTester = (String) comboBoxTester.getSelectedItem();
                        String projectDev = (String) comboBoxDev.getSelectedItem();

                        // 프로젝트 이름 중복 확인
                        if (projectService.isExistProjectName(projectName)) {
                            JOptionPane.showMessageDialog(frame, "Project name already exists. Please choose a different name.");
                            return;
                        }

                        String projectCreatedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        String projectStatus = "Active";

                        // 프로젝트 생성
                        ProjectDTO projectDTO = new ProjectDTO();
                        projectDTO.setProjectname(projectName);
                        projectDTO.setProjectdescription(projectDescription);
                        projectDTO.setProjectcreatedtime(projectCreatedTime);
                        projectDTO.setProjectstatus(projectStatus);
                        projectService.register(projectDTO);

                        // 생성된 프로젝트로 이동
                        AdminPage adminPage = new AdminPage(issueService,issueCommentService, projectService, memberService, username, password);
                        adminPage.showFrame();
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Can only be accessed by admin.");
                        UserPage userPage = new UserPage(issueService, issueCommentService,projectService, memberService, username, password);
                        userPage.showFrame();
                        frame.dispose();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void loadMembersIntoComboBoxes() {
        List<MemberDTO> members = memberService.findAll();
        for (MemberDTO member : members) {
            comboBoxPL.addItem(member.getUserid());
            comboBoxTester.addItem(member.getUserid());
            comboBoxDev.addItem(member.getUserid());
        }
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
