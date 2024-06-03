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
import java.time.format.DateTimeFormatter;

public class IssueDetailsPage {
    private JFrame frame;
    private final IssueService issueService;
    private final IssueCommentService issueCommentService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final String username;
    private final String password;
    private final IssueDTO issueDTO;
    private JTextArea textAreaComments;
    private JTextField textFieldComment;

    public IssueDetailsPage(IssueService issueService, IssueCommentService issueCommentService, ProjectService projectService, MemberService memberService, String username, String password, IssueDTO issueDTO) {
        this.issueService = issueService;
        this.issueCommentService = issueCommentService;
        this.projectService = projectService;
        this.memberService = memberService;
        this.username = username;
        this.password = password;
        this.issueDTO = issueDTO;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Title:");
        lblTitle.setBounds(50, 30, 100, 20);
        frame.getContentPane().add(lblTitle);

        JLabel lblTitleValue = new JLabel(issueDTO.getTitle());
        lblTitleValue.setBounds(150, 30, 250, 20);
        frame.getContentPane().add(lblTitleValue);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(50, 60, 100, 20);
        frame.getContentPane().add(lblStatus);

        JLabel lblStatusValue = new JLabel(issueDTO.getStatus());
        lblStatusValue.setBounds(150, 60, 250, 20);
        frame.getContentPane().add(lblStatusValue);

        JLabel lblReporter = new JLabel("Reporter:");
        lblReporter.setBounds(50, 90, 100, 20);
        frame.getContentPane().add(lblReporter);

        // Reporter ID로 사용자 이름 가져오기
        MemberDTO reporter = memberService.findByUserId(issueDTO.getWriterId(), false);
        JLabel lblReporterValue = new JLabel(reporter != null ? reporter.getUsername() : "Unknown");
        lblReporterValue.setBounds(150, 90, 250, 20);
        frame.getContentPane().add(lblReporterValue);

        JLabel lblComponent = new JLabel("Component:");
        lblComponent.setBounds(50, 120, 100, 20);
        frame.getContentPane().add(lblComponent);

        JLabel lblComponentValue = new JLabel(issueDTO.getComponent());
        lblComponentValue.setBounds(150, 120, 250, 20);
        frame.getContentPane().add(lblComponentValue);

        JLabel lblPriority = new JLabel("Priority:");
        lblPriority.setBounds(50, 150, 100, 20);
        frame.getContentPane().add(lblPriority);

        JLabel lblPriorityValue = new JLabel(issueDTO.getPriority());
        lblPriorityValue.setBounds(150, 150, 250, 20);
        frame.getContentPane().add(lblPriorityValue);

        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setBounds(50, 180, 100, 20);
        frame.getContentPane().add(lblDescription);

        JTextArea textAreaDescription = new JTextArea();
        textAreaDescription.setText(issueDTO.getDescription());
        textAreaDescription.setBounds(50, 210, 350, 100);
        frame.getContentPane().add(textAreaDescription);

        JLabel lblComments = new JLabel("Comments:");
        lblComments.setBounds(50, 320, 100, 20);
        frame.getContentPane().add(lblComments);

        textAreaComments = new JTextArea();
        textAreaComments.setBounds(50, 350, 350, 100);
        textAreaComments.setEditable(false);
        frame.getContentPane().add(textAreaComments);

        textFieldComment = new JTextField();
        textFieldComment.setBounds(50, 460, 250, 25);
        frame.getContentPane().add(textFieldComment);

        JButton btnAddComment = new JButton("Add Comment");
        btnAddComment.setBounds(310, 460, 120, 25);
        frame.getContentPane().add(btnAddComment);

        btnAddComment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String commentText = textFieldComment.getText();
                if (!commentText.isEmpty()) {
                    IssueCommentDTO newComment = new IssueCommentDTO();
                    newComment.setContent(commentText);
                    MemberDTO commenter = memberService.findByUserId(username, false); //
                    if (commenter != null) {
                        newComment.setWriterId(String.valueOf(Long.parseLong(commenter.getUserid())));
                    }
                    newComment.setIssueId(issueDTO.getId());
                    newComment.setCreatedAt(LocalDate.now());
                    issueCommentService.save(newComment);
                    issueDTO.getIssueCommentDTOList().add(newComment);
                    updateComments();
                    textFieldComment.setText("");
                }
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(50, 520, 100, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewIssue viewIssue = new ViewIssue(issueService, issueCommentService, projectService, memberService, username, password);
                viewIssue.showFrame();
                frame.dispose();
            }
        });

        updateComments();
    }

    private void updateComments() {
        textAreaComments.setText("");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (IssueCommentDTO comment : issueDTO.getIssueCommentDTOList()) {
            MemberDTO writer = memberService.findByUserId(String.valueOf(comment.getWriterId()), false);
            textAreaComments.append((writer != null ? writer.getUsername() : "Unknown") + " (" + comment.getCreatedAt().format(formatter) + "): " + comment.getContent() + "\n");
        }
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
