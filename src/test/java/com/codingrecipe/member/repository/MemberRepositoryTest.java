package com.codingrecipe.member.repository;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codingrecipe.member.entity.MemberEntity;


@SpringBootTest
public class MemberRepositoryTest {
        @Autowired
        private MemberRepository memberRepository;
    
        @AfterEach
        public void tearDown() {
    
        }
    
        @Test
        public void findAll() {
    
        }
    
        @Test
        public void testRegister() {
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.setUsername("name");
            memberEntity.setPassword("password");
            memberEntity.setUserid("id");
            memberEntity.setEmail("email@email.com");
            memberRepository.save(memberEntity);
        }



}
