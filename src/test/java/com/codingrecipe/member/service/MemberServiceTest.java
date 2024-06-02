package com.codingrecipe.member.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.entity.MemberEntity;
import com.codingrecipe.member.repository.MemberRepository;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;

import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;

    private MemberDTO memberDTO;
    @BeforeEach
    public void setUp() {
        memberDTO = new MemberDTO();
        memberDTO.setUsername("name");
        memberDTO.setPassword("password");
        memberDTO.setUserid("id");
        memberDTO.setEmail("email@email.com");

    }

    @AfterEach
    public void tearDown() {
        memberDTO = null;
    }

    @Test
    public void testRegister() {
        memberDTO = new MemberDTO();
        memberDTO.setUsername("name");
        memberDTO.setPassword("password");
        memberDTO.setUserid("id");
        memberDTO.setEmail("email@email.com");
        memberService.register(memberDTO);
        when(memberRepository.findByUserid("id")).thenReturn(Optional.of(MemberEntity.toMemberEntity(memberDTO)));
        MemberDTO expected = memberService.findByUserId("id");
        Assertions.assertEquals(memberDTO, expected);
    }
    @Test
    public void testFindByUserId() {
        memberDTO = new MemberDTO();
        memberDTO.setUsername("name");
        memberDTO.setPassword("password");
        memberDTO.setUserid("id");
        memberDTO.setEmail("email@email.com");
        when(memberRepository.findByUserid("id")).thenReturn(Optional.of(MemberEntity.toMemberEntity(memberDTO)));
        MemberDTO expected = memberService.findByUserId("id");
        Assertions.assertEquals(memberDTO, expected);
    }

    @Test
    public void testIsExistEmail() {
        memberDTO = new MemberDTO();
        memberDTO.setUsername("name");
        memberDTO.setPassword("password");
        memberDTO.setUserid("id");
        memberDTO.setEmail("email@email.com");
        when(memberRepository.findByEmail("email")).thenReturn(Optional.of(MemberEntity.toMemberEntity(memberDTO)));
        boolean expected = memberService.isExistEmail("email");
        Assertions.assertTrue(expected);
    }

    @Test
    public void testIsExistId() {
        memberDTO = new MemberDTO();
        memberDTO.setUsername("name");
        memberDTO.setPassword("password");
        memberDTO.setUserid("id");
        memberDTO.setEmail("email@email.com");
        when(memberRepository.findByUserid("id")).thenReturn(Optional.of(MemberEntity.toMemberEntity(memberDTO)));
        boolean expected = memberService.isExistId("id");
        Assertions.assertTrue(expected);
    }

    @Test
    public void testFindAll() {
        memberDTO = new MemberDTO();
        memberDTO.setUsername("name");
        memberDTO.setPassword("password");
        memberDTO.setUserid("id");
        memberDTO.setEmail("email@email.com");
        when(memberRepository.findAll()).thenReturn(List.of(MemberEntity.toMemberEntity(memberDTO)));
        List<MemberDTO> expected = memberService.findAll();
        Assertions.assertEquals(List.of(memberDTO), expected);
    }

    @Test
    public void testDeleteByUserId() {
        MemberService memberService = new MemberService(memberRepository);
        memberDTO = new MemberDTO();
        memberDTO.setUsername("name");
        memberDTO.setPassword("password");
        memberDTO.setUserid("id");
        memberDTO.setEmail("email@email.com");
        memberService.register(memberDTO);
        when(memberRepository.findByUserid("id")).thenReturn(Optional.of(MemberEntity.toMemberEntity(memberDTO)));
        boolean expected = memberService.deleteByUserId("id");
        MemberDTO memberDTO = memberService.findByUserId("id");
        Assertions.assertTrue(memberDTO == null && expected);
    }
}
