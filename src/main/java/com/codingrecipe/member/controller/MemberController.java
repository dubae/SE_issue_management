package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.MemberService;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/api/register")
    public ResponseEntity<String> register(@RequestBody MemberDTO memberDTO) {
        if (memberService.isExistId(memberDTO.getUserid()) || memberService.isExistEmail(memberDTO.getEmail())){
            System.out.println("회원가입 실패");
            return ResponseEntity.badRequest().body("이미 존재하는 아이디 또는 이메일입니다.");
        } else {
            memberService.register(memberDTO);
            System.out.println("회원가입 성공");
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        }
    }
    

    @PostMapping("/api/check_userid")
    @ResponseBody
    public ResponseEntity<String> checkUserId(@RequestBody MemberDTO memberDTO) {
        boolean isExist = memberService.isExistId(memberDTO.getUserid());
        if (isExist) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }
    
        
    @PostMapping("/api/login_status")
    public ResponseEntity<String> login_get(HttpSession session) {
        if (session.getAttribute("userid") != null) {
            return ResponseEntity.ok("이미 로그인 되어있습니다..");
        }
        return ResponseEntity.badRequest().body("로그인 해야합니다..");
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO memberDTO, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            return ResponseEntity.badRequest().body("이미 로그인 되어있습니다.");
        }
        MemberDTO findMember = memberService.findByUserId(memberDTO.getUserid(), true);
        System.out.println(memberDTO.getUserid());
        if (findMember == null) {
            System.out.println("로그인 실패");
            return ResponseEntity.badRequest().body("아이디가 존재하지 않습니다.");
        } else {
            if (findMember.getPassword().equals(memberDTO.getPassword())) {
                System.out.println("로그인 성공");
                if (session.getAttribute("userid") != null) {
                    session.invalidate();
                }
                session.setAttribute("userid", findMember.getUserid());
                return ResponseEntity.ok("로그인 성공");
            } else {
                System.out.println("로그인 실패");
                return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
            }
        }
    }

    @GetMapping("/api/member/{userid}")
    public ResponseEntity<MemberDTO> getMember(@PathVariable String userid, HttpSession session) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        MemberDTO member = memberService.findByUserId(userid);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/api/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.badRequest().body("로그인 되어있지 않습니다.");
        }
        session.invalidate();
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
