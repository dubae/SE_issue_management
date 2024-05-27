package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.MemberService;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;




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
    public RedirectView register(@ModelAttribute MemberDTO memberDTO, RedirectAttributes redirectAttributes) {
        if (memberService.isExistId(memberDTO.getUserid()) || memberService.isExistEmail(memberDTO.getEmail())){
            System.out.println("회원가입 실패");
            redirectAttributes.addFlashAttribute("Error", "이미 존재하는 아이디 또는 이메일입니다.");
            return new RedirectView("register");
        } else {
            memberService.register(memberDTO);
            System.out.println("회원가입 성공");
            redirectAttributes.addFlashAttribute("Error", "회원가입이 완료되었습니다.");
            return new RedirectView("login");
        }
    }

    @GetMapping("registerdone")
    public String register_done() {
        System.out.println("회원가입 완료 페이지");
        return "registerdone";
    }
    

    @PostMapping("/check_userid")
    @ResponseBody
    public boolean checkUserId(@RequestBody MemberDTO memberDTO) {
        return memberService.isExistId(memberDTO.getUserid());
    }
        
    @GetMapping("/login")
    public String login_get(HttpSession session) {
        if (session.getAttribute("userid") != null) {
            return "redirect:/projects";
        }
        System.out.println("로그인 페이지");
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model) {
        if (session.getAttribute("userid") != null) {
            return "redirect:/projects";
        }
        MemberDTO findMember = memberService.findByUserId(memberDTO.getUserid());
        if (findMember == null) {
            System.out.println("로그인 실패");
            model.addAttribute("Error", "아이디가 존재하지 않습니다.");
            return "login";
        } else {
            if (findMember.getPassword().equals(memberDTO.getPassword())) {
                System.out.println("로그인 성공");
                if (session.getAttribute("userid") != null) {
                    session.invalidate();
                }
                session.setAttribute("userid", findMember.getUserid());
                return "redirect:/projects";
            } else {
                System.out.println("로그인 실패");
                model.addAttribute("Error", "비밀번호가 일치하지 않습니다.");
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
