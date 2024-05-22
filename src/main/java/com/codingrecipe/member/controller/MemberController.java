package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.MemberService;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/register")
    public String register_get() {
        System.out.println("회원가입 페이지");
        return "register";
    }

    @ModelAttribute
    @PostMapping("/register")
    public String register(@ModelAttribute MemberDTO memberDTO) {
        if (memberService.isExistId(memberDTO.getUserid()) || memberService.isExistEmail(memberDTO.getEmail())){
            System.out.println("회원가입 실패");
            return "redirect:/login";
        } else {
            memberService.register(memberDTO);
            System.out.println("회원가입 성공");
            return "redirect:/login";
        }
    }
        

    @GetMapping("/login")
    public String login_get() {
        System.out.println("로그인 페이지");
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO findMember = memberService.findByUserId(memberDTO.getUserid());
        if (findMember == null) {
            System.out.println("로그인 실패");
            return "login";
        } else {
            if (findMember.getPassword().equals(memberDTO.getPassword())) {
                System.out.println("로그인 성공");
                session.setAttribute("userid", findMember.getUserid());
                return "redirect:/projects";
            } else {
                System.out.println("로그인 실패");
                return "login";
            }
        }
    }

    @GetMapping("/member/{userid}")
    public String member_detail(@PathVariable String userid, Model model, HttpSession session){
        if (session.getAttribute("userid") == null) {
            return "redirect:/login";
        }
        System.out.println(session.getAttribute("userid"));
        MemberDTO memberDTO = memberService.findByUserId(userid);
        model.addAttribute("member", memberDTO);
        System.out.println(memberDTO.getUserid());
        if (session.getAttribute("userid").equals(memberDTO.getUserid())) {
            System.out.println("내 정보");
            return "redirect:/myinfo";
        }
        else {
            return "memberdetail";
        }
    }

    @GetMapping("/myinfo")
    public String myinfo(HttpSession session, Model model) {
        if (session.getAttribute("userid") == null) {
            return "redirect:/login";
        }
        MemberDTO memberDTO = memberService.findByUserId((String)session.getAttribute("userid"));
        model.addAttribute("member", memberDTO);
        return "myinfo";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
    
        return "redirect:/login";
    }
}
