package com.codingrecipe.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.codingrecipe.member.session.SessionManager;

@SpringBootApplication
public class MemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemberApplication.class, args);
		SessionManager.test();
	}

}
