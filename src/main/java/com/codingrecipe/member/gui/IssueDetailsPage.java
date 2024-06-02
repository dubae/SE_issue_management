package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.entity.IssueEntity;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class IssueDetailsPage {
    private JFrame frame;
    private final IssueService issueService;
    private final ProjectService projectService;
    private final String username;
    private final String password;
    private final IssueDTO issueDTO;

    public IssueDetailsPage(IssueService issueService, ProjectService projectService, String username, String password, IssueDTO issueDTO) {
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
        lblDescription.setBounds(50, 180, 100, 20);
        frame.getContentPane().add(lblDescription);

        JTextArea textAreaDescription = new JTextArea();
        textAreaDescription.setText(issueDTO.getDescription());
        textAreaDescription.setBounds(50, 210, 350, 150);
        frame.getContentPane().add(textAreaDescription);

        JButton btnBack = new JButton("뒤로가기");
        btnBack.setBounds(50, 420, 100, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewIssue viewIssue = new ViewIssue(issueService, projectService, username, password);
                viewIssue.showFrame();
                frame.dispose();
            }
        });

        addSearchButton("Title", issueDTO.getTitle(), 100, 90, 100, 30);
        addSearchButton("Status", issueDTO.getStatus(), 100, 120, 100, 30);
        addSearchButton("Component", issueDTO.getStatus(), 100, 150, 100, 30);
        addSearchButton("Priority", issueDTO.getStatus(), 100, 180, 100, 30);

    }

    private void addSearchButton(String fieldName, String fieldValue, int x, int y, int width, int height) {
        JButton btnSearch = new JButton("Search " + fieldName);
        btnSearch.setBounds(x, y, width, height);
        frame.getContentPane().add(btnSearch);

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // issueService에 findByPriority 추가함
                List<IssueDTO> searchResults = null;
                switch (fieldName) {
                    case "Title":
                        searchResults = issueService.findByTitle(fieldValue);
                        break;
                    case "Status":
                        searchResults = issueService.findByStatus(fieldValue);
                        break;
                    case "Component":
                        searchResults = issueService.findByComponent(fieldValue);
                        break;
                    case "Priority":
                        searchResults = issueService.findByPriority(fieldValue);
                        break;
                }
                if (searchResults != null) {
                    // Display the search results in a new window
                    displaySearchResults(searchResults);
                } else {
                    JOptionPane.showMessageDialog(frame, "No search results found for " + fieldName + ": " + fieldValue);
                }
            }
        });
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
