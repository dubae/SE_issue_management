package com.codingrecipe.member.gui;

import com.codingrecipe.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.codingrecipe.member.service"})
public class Main implements CommandLineRunner {

    @Autowired
    private MemberService memberService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        MainScreen mainScreen = new MainScreen(memberService);
        mainScreen.showFrame();
    }
}
