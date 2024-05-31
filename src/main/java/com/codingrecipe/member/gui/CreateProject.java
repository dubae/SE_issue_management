package com.codingrecipe.member.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateProject {
    private JFrame frame;
    private JTextField textFieldProjectName;
    private JTextArea textAreaProjectDescription;

    public CreateProject() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblProjectName = new JLabel("Project Name:");
        lblProjectName.setBounds(50, 30, 100, 25);
        frame.getContentPane().add(lblProjectName);

        textFieldProjectName = new JTextField();
        textFieldProjectName.setBounds(150, 30, 200, 25);
        frame.getContentPane().add(textFieldProjectName);

        JLabel lblProjectDescription = new JLabel("Description:");
        lblProjectDescription.setBounds(50, 70, 100, 25);
        frame.getContentPane().add(lblProjectDescription);

        textAreaProjectDescription = new JTextArea();
        textAreaProjectDescription.setBounds(150, 70, 200, 100);
        frame.getContentPane().add(textAreaProjectDescription);

        JButton btnCreate = new JButton("Create");
        btnCreate.setBounds(150, 200, 100, 30);
        frame.getContentPane().add(btnCreate);

        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String projectName = textFieldProjectName.getText();
                String projectDescription = textAreaProjectDescription.getText();

                // 프로젝트 생성 로직 추가 (DB 저장 등)
                JOptionPane.showMessageDialog(frame, "Project Created: " + projectName);
                // 프로젝트 생성 후 관리자 모드로 돌아가기
                Admin admin = new Admin();
                admin.showFrame();
                frame.dispose();
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(270, 200, 80, 30);
        frame.getContentPane().add(btnBack);

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Admin admin = new Admin();
                admin.showFrame();
                frame.dispose();
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
