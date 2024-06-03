package com.codingrecipe.member.gui;

import com.codingrecipe.member.service.IssueCommentService;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.codingrecipe.member"})
public class Main implements CommandLineRunner {

    @Autowired
    private MemberService memberService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private final IssueCommentService issueCommentService;

    public Main(IssueCommentService issueCommentService) {
        this.issueCommentService = issueCommentService;
    }


    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        MainScreen mainScreen = new MainScreen(memberService, issueService, projectService, issueCommentService); // ProjectService 인스턴스 전달
        mainScreen.showFrame();
    }
}