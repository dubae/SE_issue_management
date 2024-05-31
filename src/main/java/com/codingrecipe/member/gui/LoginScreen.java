package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen {
    private JFrame frame;
    private JTextField textFieldEmail;
    private JPasswordField passwordField;
    private final MemberService memberService;
    private final IssueService issueService;

    public LoginScreen(MemberService memberService, IssueService issueService) {
        this.memberService = memberService;
        this.issueService = issueService;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("로그인");
        lblTitle.setBounds(150, 20, 100, 30);
        frame.getContentPane().add(lblTitle);

        JLabel lblEmail = new JLabel("UserID:");
        lblEmail.setBounds(50, 80, 80, 25);
        frame.getContentPane().add(lblEmail);

        textFieldEmail = new JTextField();
        textFieldEmail.setBounds(150, 80, 180, 25);
        frame.getContentPane().add(textFieldEmail);
        textFieldEmail.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 120, 80, 25);
        frame.getContentPane().add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 120, 180, 25);
        frame.getContentPane().add(passwordField);

        JButton btnLogin = new JButton("로그인");
        btnLogin.setBounds(150, 160, 180, 30);
        frame.getContentPane().add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = textFieldEmail.getText();
                String password = new String(passwordField.getPassword());

                if (validateInputs(email, password)) {
                    // Retrieve user data from the MemberService
                    MemberDTO memberDTO = memberService.findByUserId(email, true);
                    if (memberDTO != null && memberDTO.getPassword().equals(password)) {
                        // Login successful
                        JOptionPane.showMessageDialog(frame, "로그인 성공!");

                        UserPage userPage = new UserPage(memberService, issueService, email);
                        userPage.showFrame();

                        frame.dispose();

                    } else {
                        JOptionPane.showMessageDialog(frame, "로그인 실패. 아이디 또는 비밀번호 오류!");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "아이디와 비밀번호를 입력하세요.");
                }
            }
        });

        // 뒤로가기 같은 로직
        JButton btnBack = new JButton("뒤로가기");
        btnBack.setBounds(50, 160, 90, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainScreen mainScreen = new MainScreen(memberService);
                mainScreen.showFrame();
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        return !(email.isEmpty() || password.isEmpty());
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}