package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueCommentService;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SearchIssue {
    private JFrame frame;
    private final IssueService issueService;
    private final IssueCommentService issueCommentService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final String username;
    private final String password;
    private JComboBox<String> comboBoxSearchBy;
    private JTextField textFieldSearch;

    public SearchIssue(IssueService issueService, IssueCommentService issueCommentService, ProjectService projectService, MemberService memberService, String username, String password) {
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
        frame.setBounds(100, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblSearchBy = new JLabel("Search by:");
        lblSearchBy.setBounds(50, 30, 100, 20);
        frame.getContentPane().add(lblSearchBy);

        comboBoxSearchBy = new JComboBox<>(new String[]{"Title", "Status", "Component"});
        comboBoxSearchBy.setBounds(150, 30, 150, 25);
        frame.getContentPane().add(comboBoxSearchBy);

        textFieldSearch = new JTextField();
        textFieldSearch.setBounds(150, 70, 150, 25);
        frame.getContentPane().add(textFieldSearch);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(310, 70, 90, 25);
        frame.getContentPane().add(btnSearch);

        JPanel searchResultsPanel = new JPanel();
        searchResultsPanel.setBounds(50, 110, 500, 300);
        frame.getContentPane().add(searchResultsPanel);
        searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.Y_AXIS));

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchBy = (String) comboBoxSearchBy.getSelectedItem();
                String searchValue = textFieldSearch.getText();
                searchIssues(searchBy, searchValue, searchResultsPanel);
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(50, 420, 100, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewIssue viewIssue = new ViewIssue(issueService, issueCommentService,projectService, memberService, username, password);
                viewIssue.showFrame();
                frame.dispose();
            }
        });
    }

    private void searchIssues(String searchBy, String searchValue, JPanel searchResultsPanel) {
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
            displaySearchResults(searchResults, searchResultsPanel);
        } else {
            JOptionPane.showMessageDialog(frame, "No search results found for " + searchBy + ": " + searchValue);
        }
    }

    private void displaySearchResults(List<IssueDTO> searchResults, JPanel searchResultsPanel) {
        searchResultsPanel.removeAll();
        for (IssueDTO issue : searchResults) {
            JButton btnIssue = new JButton(issue.getTitle());
            searchResultsPanel.add(btnIssue);

            btnIssue.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    IssueDetailsPage issueDetailsPage = new IssueDetailsPage(issueService, issueCommentService, projectService, memberService, username, password, issue);
                    issueDetailsPage.showFrame();
                    frame.dispose();
                }
            });
        }
        searchResultsPanel.revalidate();
        searchResultsPanel.repaint();
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
