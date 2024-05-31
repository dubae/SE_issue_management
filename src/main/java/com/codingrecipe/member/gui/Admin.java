package com.codingrecipe.member.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//관리자 모드에서 프로젝트 생성 부분 구현
public class Admin {
    private JFrame frame;

    public Admin() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnCreateProject = new JButton("Create Project");
        btnCreateProject.setBounds(100, 100, 200, 30);
        frame.getContentPane().add(btnCreateProject);

        btnCreateProject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateProject createProject = new CreateProject();
                createProject.showFrame();
                frame.dispose();
            }
        });
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}
