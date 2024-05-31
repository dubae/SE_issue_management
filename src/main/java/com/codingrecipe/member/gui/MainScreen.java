package com.codingrecipe.member.gui;

import com.codingrecipe.member.service.MemberService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen {
    private JFrame frame;
    private final MemberService memberService;

    public MainScreen(MemberService memberService) {
        this.memberService = memberService;
        initialize();
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

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginScreen loginScreen = new LoginScreen(memberService);
                loginScreen.showFrame();
                frame.dispose();
            }
        });

        btnSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SignUpScreen signUpScreen = new SignUpScreen(memberService);
                signUpScreen.showFrame();
                frame.dispose();
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}

