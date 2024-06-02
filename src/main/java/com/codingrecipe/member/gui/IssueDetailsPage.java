package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueCommentDTO;
import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueCommentService;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class IssueDetailsPage {
    private JFrame frame;
    private final IssueService issueService;
    private final IssueCommentService issueCommentService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final String username;
    private final String password;
    private final IssueDTO issueDTO;
    private JComboBox<String> comboBoxSearchBy;
    private JTextField textFieldSearch;
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

        JLabel lblComponent = new JLabel("Component:");
        lblComponent.setBounds(50, 90, 100, 20);
        frame.getContentPane().add(lblComponent);

        JLabel lblComponentValue = new JLabel(issueDTO.getComponent());
        lblComponentValue.setBounds(150, 90, 250, 20);
        frame.getContentPane().add(lblComponentValue);

        JLabel lblPriority = new JLabel("Priority:");
        lblPriority.setBounds(50, 120, 100, 20);
        frame.getContentPane().add(lblPriority);

        JLabel lblPriorityValue = new JLabel(issueDTO.getPriority());
        lblPriorityValue.setBounds(150, 120, 250, 20);
        frame.getContentPane().add(lblPriorityValue);

        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setBounds(50, 150, 100, 20);
        frame.getContentPane().add(lblDescription);

        JTextArea textAreaDescription = new JTextArea();
        textAreaDescription.setText(issueDTO.getDescription());
        textAreaDescription.setBounds(50, 180, 350, 100);
        frame.getContentPane().add(textAreaDescription);

        JLabel lblComments = new JLabel("Comments:");
        lblComments.setBounds(50, 290, 100, 20);
        frame.getContentPane().add(lblComments);

        textAreaComments = new JTextArea();
        textAreaComments.setBounds(50, 320, 350, 100);
        textAreaComments.setEditable(false);
        frame.getContentPane().add(textAreaComments);

        textFieldComment = new JTextField();
        textFieldComment.setBounds(50, 430, 250, 25);
        frame.getContentPane().add(textFieldComment);

        JButton btnAddComment = new JButton("Add Comment");
        btnAddComment.setBounds(310, 430, 120, 25);
        frame.getContentPane().add(btnAddComment);

        btnAddComment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String commentText = textFieldComment.getText();
                if (!commentText.isEmpty()) {
                    IssueCommentDTO newComment = new IssueCommentDTO();
                    newComment.setContent(commentText);
                    newComment.setWriterId(Long.parseLong(username)); // Assuming username is the user ID
                    newComment.setIssueId(issueDTO.getId());
                    newComment.setCreatedAt(LocalDateTime.now());
                    issueCommentService.save(newComment);
                    issueDTO.getIssueCommentDTOList().add(newComment);
                    updateComments();
                    textFieldComment.setText("");
                }
            }
        });

        JLabel lblSearchBy = new JLabel("Search by:");
        lblSearchBy.setBounds(50, 460, 100, 20);
        frame.getContentPane().add(lblSearchBy);

        comboBoxSearchBy = new JComboBox<>(new String[]{"Title", "Status", "Component"});
        comboBoxSearchBy.setBounds(150, 460, 150, 25);
        frame.getContentPane().add(comboBoxSearchBy);

        textFieldSearch = new JTextField();
        textFieldSearch.setBounds(150, 490, 150, 25);
        frame.getContentPane().add(textFieldSearch);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(310, 490, 90, 25);
        frame.getContentPane().add(btnSearch);

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchBy = (String) comboBoxSearchBy.getSelectedItem();
                String searchValue = textFieldSearch.getText();
                searchIssues(searchBy, searchValue);
            }
        });

        JButton btnBack = new JButton("뒤로가기");
        btnBack.setBounds(50, 530, 100, 30);
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
            textAreaComments.append(comment.getWriterId() + " (" + comment.getCreatedAt().format(formatter) + "): " + comment.getContent() + "\n");
        }
    }

    private void searchIssues(String searchBy, String searchValue) {
        List<IssueDTO> searchResults = null;
        switch (searchBy) {
            case "Title":
                searchResults = issueService.findByTitle(searchValue);
                break;
            case "Status":
                searchResults = issueService.findByStatus(searchValue);
                break;
            case "Component":
                searchResults = issueService.findByComponent(searchValue);
                break;
        }
        if (searchResults != null) {
            displaySearchResults(searchResults);
        } else {
            JOptionPane.showMessageDialog(frame, "No search results found for " + searchBy + ": " + searchValue);
        }
    }

    private void displaySearchResults(List<IssueDTO> searchResults) {
        JFrame searchResultsFrame = new JFrame();
        searchResultsFrame.setBounds(200, 200, 600, 400);
        searchResultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchResultsFrame.getContentPane().setLayout(null);

        JList<String> resultList = new JList<>(searchResults.stream().map(IssueDTO::getTitle).toArray(String[]::new));
        JScrollPane scrollPane = new JScrollPane(resultList);
        scrollPane.setBounds(50, 50, 500, 250);
        searchResultsFrame.getContentPane().add(scrollPane);

        searchResultsFrame.setVisible(true);
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
