package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.dto.MemberDTOSecure;
import com.codingrecipe.member.service.MemberService;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        } else {
            memberService.register(memberDTO);
            System.out.println("회원가입 성공");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }
    

    @PostMapping("/api/check_userid")
    @ResponseBody
    public ResponseEntity<String> checkUserId(@RequestBody MemberDTO memberDTO) {
        boolean isExist = memberService.isExistId(memberDTO.getUserid());
        if (isExist) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
        
    @GetMapping("/api/login_status")
    public ResponseEntity<String> login_get(HttpSession session) {
        if (session.getAttribute("userid") != null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO memberDTO, HttpSession session) {
        if (session.getAttribute("userid") != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        MemberDTO findMember = memberService.findByUserId(memberDTO.getUserid(), true);
        if (findMember == null) {
            System.out.println("로그인 실패");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            if (findMember.getPassword().equals(memberDTO.getPassword())) {
                System.out.println("로그인 성공");
                if (session.getAttribute("userid") != null) {
                    session.invalidate();
                }
                session.setAttribute("userid", findMember.getUserid());
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                System.out.println("로그인 실패");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
    }

    @GetMapping("/api/member/{userid}")
    public ResponseEntity<MemberDTOSecure> getMember(@PathVariable String userid, HttpSession session) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        MemberDTO member = memberService.findByUserId(userid);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        MemberDTOSecure memberDTOSecure = MemberDTOSecure.toMemberDTOSecure(member);
        return ResponseEntity.ok(memberDTOSecure);
    }

    @GetMapping("/api/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/user/{userid}/delete")
    public ResponseEntity<String> delete(@PathVariable String userid, HttpSession session) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!session.getAttribute("userid").equals(userid) && !session.getAttribute("userid").equals("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (memberService.findByUserId(userid) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (memberService.deleteByUserId(userid)) {
            if (session.getAttribute("userid").equals(userid)){
                session.invalidate();
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @GetMapping("/api/user_list")
    public ResponseEntity<List<MemberDTOSecure>> addProjectGet(HttpSession session) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<MemberDTO> memberDTOList = memberService.findAll();

        List<MemberDTOSecure> memberDTOSecureList = MemberDTOSecure.toMemberDTOSecureList(memberDTOList);
        return ResponseEntity.status(HttpStatus.OK).body(memberDTOSecureList);
    }
}
