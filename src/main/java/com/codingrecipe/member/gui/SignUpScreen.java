package com.codingrecipe.member.gui;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpScreen {
    private JFrame frame;
    private JTextField textFieldUserId;
    private JTextField textFieldEmail;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private final MemberService memberService;
    private final IssueService issueService;
    private final ProjectService projectService;

    public SignUpScreen(MemberService memberService, IssueService issueService, ProjectService projectService) {
        this.memberService = memberService;
        this.issueService = issueService;
        this.projectService = projectService;
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

        JLabel lblUserId = new JLabel("User ID:"); // Added userid label
        lblUserId.setBounds(50, 60, 80, 25);
        frame.getContentPane().add(lblUserId);

        textFieldUserId = new JTextField(); // Added userid text field
        textFieldUserId.setBounds(150, 60, 180, 25);
        frame.getContentPane().add(textFieldUserId);
        textFieldUserId.setColumns(10);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 100, 80, 25);
        frame.getContentPane().add(lblEmail);

        textFieldEmail = new JTextField();
        textFieldEmail.setBounds(150, 100, 180, 25);
        frame.getContentPane().add(textFieldEmail);
        textFieldEmail.setColumns(10);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 140, 80, 25);
        frame.getContentPane().add(lblUsername);

        textFieldUsername = new JTextField();
        textFieldUsername.setBounds(150, 140, 180, 25);
        frame.getContentPane().add(textFieldUsername);
        textFieldUsername.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 180, 80, 25);
        frame.getContentPane().add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 180, 180, 25);
        frame.getContentPane().add(passwordField);

        JButton btnSignUp = new JButton("가입");
        btnSignUp.setBounds(150, 220, 180, 30);
        frame.getContentPane().add(btnSignUp);

        btnSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userid = textFieldUserId.getText();
                String email = textFieldEmail.getText();
                String username = textFieldUsername.getText();
                String password = new String(passwordField.getPassword());

                if (validateInputs(userid, email, username, password)) {
                    MemberDTO memberDTO = new MemberDTO();
                    memberDTO.setUserid(userid);
                    memberDTO.setEmail(email);
                    memberDTO.setUsername(username);
                    memberDTO.setPassword(password);
                    memberService.register(memberDTO);
                    JOptionPane.showMessageDialog(frame, "회원 가입 성공!");
                    LoginScreen loginScreen = new LoginScreen(memberService, issueService, projectService);
                    loginScreen.showFrame();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                }
            }
        });
    }

    private boolean validateInputs(String userid, String email, String username, String password) {
        return !(userid.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty());
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}