package com.codingrecipe.member.gui;

import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.*;

public class MainScreen {
    private JFrame frame;
    private final MemberService memberService;
    private final IssueService issueService;
    private final ProjectService projectService;

    public MainScreen(MemberService memberService, IssueService issueService, ProjectService projectService) {
        this.memberService = memberService;
        this.issueService = issueService;
        this.projectService = projectService; // ProjectService 인스턴스 초기화
        if (!GraphicsEnvironment.isHeadless()) {
            initialize();
        } else {
            System.out.println("헤드리스 모드에서 실행중");
        }
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnLogin = new JButton("로그인");
        btnLogin.setBounds(150, 80, 100, 30);
        frame.getContentPane().add(btnLogin);

        JButton btnSignUp = new JButton("회원가입");
        btnSignUp.setBounds(150, 130, 100, 30);
        frame.getContentPane().add(btnSignUp);

        btnLogin.addActionListener(e -> {
            LoginScreen loginScreen = new LoginScreen(memberService, issueService, projectService);
            loginScreen.showFrame();
            frame.dispose();
        });

        btnSignUp.addActionListener(e -> {
            SignUpScreen signUpScreen = new SignUpScreen(memberService, issueService, projectService);
            signUpScreen.showFrame();
            frame.dispose();
        });
    }

    public void showFrame() {
        if (!GraphicsEnvironment.isHeadless()) {
            frame.setVisible(true);
        } else {
            System.out.println("헤드리스 모드에서 실행중");
        }
    }
}
