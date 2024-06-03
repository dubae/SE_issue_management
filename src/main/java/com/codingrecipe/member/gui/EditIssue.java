package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueCommentDTO;
import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.IssueCommentService;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class EditIssue {
    private JFrame frame;
    private JTextField textFieldTitle;
    private JTextArea textAreaDescription;
    private JComboBox<String> comboBoxPriority;
    private JComboBox<String> comboBoxStatus;
    private JComboBox<String> comboBoxAssignee;

    private final IssueService issueService;
    private final IssueCommentService issueCommentService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final String username;
    private final String password;
    private final Long userid;
    private final IssueDTO issueDTO;

    public EditIssue(IssueService issueService, IssueCommentService issueCommentService, ProjectService projectService, MemberService memberService, Long userid, String username, String password, IssueDTO issueDTO) {
        this.issueService = issueService;
        this.issueCommentService = issueCommentService;
        this.projectService = projectService;
        this.memberService = memberService;
        this.username = username;
        this.password = password;
        this.userid = userid;
        this.issueDTO = issueDTO;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 600);
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
        comboBoxStatus.addItem("assigned");
        comboBoxStatus.addItem("resolved");
        comboBoxStatus.addItem("closed");
        comboBoxStatus.setSelectedItem(issueDTO.getStatus());
        frame.getContentPane().add(comboBoxStatus);

        JLabel lblAssignee = new JLabel("Assignee:");
        lblAssignee.setBounds(50, 280, 100, 20);
        frame.getContentPane().add(lblAssignee);

        comboBoxAssignee = new JComboBox<>();
        comboBoxAssignee.setBounds(150, 280, 150, 25);
        comboBoxAssignee.addItem("No Assignee"); // No Assignee 옵션 추가
        List<MemberDTO> members = memberService.findAll();
        for (MemberDTO member : members) {
            comboBoxAssignee.addItem(member.getUserid());
        }
        comboBoxAssignee.setSelectedItem(issueDTO.getDevId() == null ? "No Assignee" : issueDTO.getDevId());
        frame.getContentPane().add(comboBoxAssignee);

        JButton btnUpdateIssue = new JButton("Update Issue");
        btnUpdateIssue.setBounds(200, 420, 150, 30);
        frame.getContentPane().add(btnUpdateIssue);

        btnUpdateIssue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String title = textFieldTitle.getText();
                    String description = textAreaDescription.getText();
                    String priority = (String) comboBoxPriority.getSelectedItem();
                    String assignee = (String) comboBoxAssignee.getSelectedItem();

                    issueDTO.setTitle(title);
                    issueDTO.setDescription(description);
                    issueDTO.setPriority(priority);
                    issueDTO.setStatus("fixed"); // 상태를 fixed로 설정
                    issueDTO.setDevId(assignee.equals("No Assignee") ? null : assignee); // devId 설정

                    issueService.addNewIssue(issueDTO);

                    // 이슈 수정에 대한 코멘트 추가
                    if (userid != null && issueDTO.getId() != null) {
                        IssueCommentDTO comment = new IssueCommentDTO();
                        comment.setWriterId(userid);
                        comment.setIssueId(issueDTO.getId());
                        comment.setContent("Issue updated by " + username);
                        comment.setCreatedAt(LocalDate.now());
                        issueCommentService.save(comment);

                        issueDTO.getIssueCommentDTOList().add(comment);
                    } else {
                        System.out.println("Error: userid or issueDTO.getId() is null");
                    }

                    ViewIssue viewIssue = new ViewIssue(issueService, issueCommentService, projectService, memberService, username, password);
                    viewIssue.showFrame();
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
                ViewIssue viewIssue = new ViewIssue(issueService, issueCommentService, projectService, memberService, username, password);
                viewIssue.showFrame();
                frame.dispose();
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
