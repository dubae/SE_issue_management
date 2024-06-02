package com.codingrecipe.member.controller;

import antlr.StringUtils;
import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.dto.MemberDTOSecure;
import com.codingrecipe.member.dto.UserRoleDTO;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.UserRoleService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.codingrecipe.member.session.SessionManager;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final UserRoleService userRoleService;

    @PostMapping("/api/register")
    public ResponseEntity<String> register(@RequestBody MemberDTO memberDTO) {
        if (memberService.isExistId(memberDTO.getUserid()) || memberService.isExistEmail(memberDTO.getEmail())){
            System.out.println("회원가입 실패");
            // 에러 메시지
            return ResponseEntity.badRequest().body("이미 존재하는 아이디 또는 이메일입니다.");

        } else {
            memberService.register(memberDTO);
            System.out.println("회원가입 성공");
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        }
    }


    @PostMapping("/api/check_userid")
    public ResponseEntity<String> checkUserId(@RequestBody MemberDTO memberDTO) {
        boolean isExist = memberService.isExistId(memberDTO.getUserid());
        if (isExist) {
            // 에러 메시지
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }
        // 에러 메시지
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }


    @RequestMapping(value = "/api/login_status", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> login_get(HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        System.out.println(sessionid);
        if (SessionManager.getSession(sessionid) != null){
            // 에러 메시지
            return ResponseEntity.ok("이미 로그인 되어있습니다..!");
        }
        // 에러 메시지
        return ResponseEntity.badRequest().body("로그인을 해야 합니다..!");
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO memberDTO, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        System.out.println(memberDTO.getUserid());
        System.out.println(memberDTO.getPassword());
        System.out.println(sessionid);
        if (SessionManager.getSession(sessionid) != null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        MemberDTO findMember = memberService.findByUserId(memberDTO.getUserid(), true);
        if (findMember == null) {
            System.out.println("로그인 실패");
            // 에러 메시지
            return ResponseEntity.badRequest().body("아이디가 존재하지 않습니다.");
        } else {
            if (findMember.getPassword().equals(memberDTO.getPassword())) {
                System.out.println("로그인 성공");
                if (sessionid != null && SessionManager.getSession(sessionid) != null) {
                    SessionManager.removeSession(sessionid);
                }
                String key = SessionManager.generateKey();
                SessionManager.setSession(key, findMember.getUserid());
                System.out.println("key: "+key);
                return ResponseEntity.status(HttpStatus.OK).body(key);
            } else {
                System.out.println("로그인 실패");
                // 에러 메시지
                return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
            }
        }
    }

    @GetMapping("/api/member/{userid}")
    public ResponseEntity<MemberDTOSecure> getMember(@PathVariable String userid, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
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
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        SessionManager.removeSession(sessionid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/user/{userid}/delete")
    public ResponseEntity<String> delete(@PathVariable String userid, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!SessionManager.getSession(sessionid).equals(userid) && !SessionManager.getSession(sessionid).equals("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (memberService.findByUserId(userid) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (memberService.deleteByUserId(userid)) {
            if (SessionManager.getSession(sessionid).equals(userid)){
                SessionManager.removeSession(sessionid);
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PostMapping("/api/user_list")
    public ResponseEntity<List<MemberDTOSecure>> addProjectGet(HttpServletRequest request) {
        System.out.println("user list~");
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<MemberDTO> memberDTOList = memberService.findAll();

        List<MemberDTOSecure> memberDTOSecureList = MemberDTOSecure.toMemberDTOSecureList(memberDTOList);
        return ResponseEntity.status(HttpStatus.OK).body(memberDTOSecureList);
    }

    @GetMapping("/api/myinfo")
    public ResponseEntity<MemberDTOSecure> get_my_info(HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userid = (String) SessionManager.getSession(sessionid);
        MemberDTO member = memberService.findByUserId(userid);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        MemberDTOSecure memberDTOSecure = MemberDTOSecure.toMemberDTOSecure(member);
        return ResponseEntity.ok(memberDTOSecure);
    }
}
