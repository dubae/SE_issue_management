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

public class ViewIssue {
    private JFrame frame;
    private final IssueService issueService;
    private final IssueCommentService issueCommentService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final String username;
    private final String password;

    public ViewIssue(IssueService issueService, IssueCommentService issueCommentService, ProjectService projectService, MemberService memberService, String username, String password) {
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

        List<IssueDTO> issues = issueService.findAllIssue();

        // 각 이슈를 버튼으로 생성 -> 이슈 저장이 하나밖에 안되는 것 같음
        int y = 30;
        for (IssueDTO issue : issues) {
            JButton btnIssue = new JButton(issue.getTitle());
            btnIssue.setBounds(50, y, 250, 25);
            frame.getContentPane().add(btnIssue);

            btnIssue.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // 팝업 메뉴 생성
                    JPopupMenu popupMenu = new JPopupMenu();

                    JMenuItem viewDetails = new JMenuItem("View Details");
                    viewDetails.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            IssueDetailsPage issueDetailsPage = new IssueDetailsPage(issueService, issueCommentService, projectService, memberService, username, password, issue);
                            issueDetailsPage.showFrame();
                            frame.dispose();
                        }
                    });
                    popupMenu.add(viewDetails);

                    JMenuItem editIssue = new JMenuItem("Edit Issue");
                    editIssue.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            EditIssue editIssuePage = new EditIssue(issueService, issueCommentService, projectService, memberService, Long.parseLong(username), username, password, issue);
                            editIssuePage.showFrame();
                            frame.dispose();
                        }
                    });
                    popupMenu.add(editIssue);

                    popupMenu.show(btnIssue, btnIssue.getWidth() / 2, btnIssue.getHeight() / 2);
                }
            });

            y += 30;
        }

        JButton btnSearch = new JButton("Search Issues");
        btnSearch.setBounds(50, y, 150, 25);
        frame.getContentPane().add(btnSearch);

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SearchIssue searchIssue = new SearchIssue(issueService, issueCommentService, projectService, memberService, username, password);
                searchIssue.showFrame();
                frame.dispose();
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(50, y + 40, 100, 25);
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
