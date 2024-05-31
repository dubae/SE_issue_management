package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.MemberService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpScreen {
    private JFrame frame;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private final MemberService memberService;

    public SignUpScreen(MemberService memberService) {
        this.memberService = memberService;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("회원 가입");
        lblTitle.setBounds(150, 20, 100, 30);
        frame.getContentPane().add(lblTitle);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 80, 80, 25);
        frame.getContentPane().add(lblUsername);

        textFieldUsername = new JTextField();
        textFieldUsername.setBounds(150, 80, 180, 25);
        frame.getContentPane().add(textFieldUsername);
        textFieldUsername.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 120, 80, 25);
        frame.getContentPane().add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 120, 180, 25);
        frame.getContentPane().add(passwordField);

        JButton btnSignUp = new JButton("가입");
        btnSignUp.setBounds(150, 160, 180, 30);
        frame.getContentPane().add(btnSignUp);

        JButton btnBack = new JButton("뒤로가기");
        btnBack.setBounds(150, 200, 180, 30);
        frame.getContentPane().add(btnBack);

        btnSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = textFieldUsername.getText();
                String password = new String(passwordField.getPassword());

                if (validateInputs(username, password)) {
                    MemberDTO memberDTO = new MemberDTO();
                    memberDTO.setUserid(username);
                    memberDTO.setPassword(password);
                    memberService.register(memberDTO);
                    JOptionPane.showMessageDialog(frame, "회원 가입 성공!");
                    LoginScreen loginScreen = new LoginScreen(memberService);
                    loginScreen.showFrame();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "다시 입력해주세요.");
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainScreen mainScreen = new MainScreen(memberService);
                mainScreen.showFrame();
                frame.dispose();
            }
        });
    }

    private boolean validateInputs(String username, String password) {
        return !(username.isEmpty() || password.isEmpty());
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}

