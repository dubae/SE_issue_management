package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.ProjectService;
import com.codingrecipe.member.service.MemberService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EditProject {
    private JFrame frame;
    private JTextField textFieldProjectName;
    private JTextField textFieldProjectDescription;
    private JComboBox<String> comboBoxPL;
    private JComboBox<String> comboBoxTester;
    private JComboBox<String> comboBoxDev;
    private JButton btnUpdate;

    private final IssueService issueService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final Long projectId;
    private final String username;
    private final String password;

    public EditProject(IssueService issueService, ProjectService projectService, MemberService memberService, Long projectId, String username, String password) {
        this.issueService = issueService;
        this.projectService = projectService;
        this.memberService = memberService;
        this.projectId = projectId;
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

        // Load members into combo boxes
        loadMembersIntoComboBoxes();

        btnUpdate = new JButton("Update Project");
        btnUpdate.setBounds(150, 230, 150, 30);
        frame.getContentPane().add(btnUpdate);

        ProjectDTO projectDTO = projectService.findByProjectId(projectId);
        if (projectDTO != null) {
            textFieldProjectName.setText(projectDTO.getProjectname());
            textFieldProjectDescription.setText(projectDTO.getProjectdescription());

            comboBoxPL.setSelectedItem(projectDTO.getProjectname());
            comboBoxTester.setSelectedItem(projectDTO.getProjectdescription());
            comboBoxDev.setSelectedItem(projectDTO.getProjectname());
        }

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String projectName = textFieldProjectName.getText();
                String projectDescription = textFieldProjectDescription.getText();
                String projectPL = (String) comboBoxPL.getSelectedItem();
                String projectTester = (String) comboBoxTester.getSelectedItem();
                String projectDev = (String) comboBoxDev.getSelectedItem();

                if (!projectDTO.getProjectname().equals(projectName) && projectService.isExistProjectName(projectName)) {
                    JOptionPane.showMessageDialog(frame, "Project name already exists. Please choose a different name.");
                    return;
                }

                projectDTO.setProjectname(projectName);
                projectDTO.setProjectdescription(projectDescription);
                projectService.register(projectDTO);

                AdminPage adminPage = new AdminPage(issueService, projectService, memberService, username, password);
                adminPage.showFrame();
                frame.dispose();
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(50, 230, 100, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChooseProject chooseProject = new ChooseProject(issueService, projectService, memberService, username, password);
                chooseProject.showFrame();
                frame.dispose();
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
