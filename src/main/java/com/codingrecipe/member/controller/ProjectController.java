package com.codingrecipe.member.controller;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.codingrecipe.member.dto.AddProjectDTO;
import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.dto.ProjectDetailDTO;
import com.codingrecipe.member.dto.ProjectInfoDTO;
import com.codingrecipe.member.dto.UserRoleDTO;
import com.codingrecipe.member.entity.MemberEntity;
import com.codingrecipe.member.entity.ProjectEntity;
import com.codingrecipe.member.entity.UserRoleEntity;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;
import com.codingrecipe.member.service.UserRoleService;

import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final MemberService memberService;
    private final UserRoleService userRoleService;


    @GetMapping("/api/projects")
    public ResponseEntity<List<ProjectInfoDTO>> project_get(HttpSession session){
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ProjectDTO> projects;
        if (session.getAttribute("userid").equals("admin")) {
            projects = projectService.findAll();
        }
        else{
            String userId = (String) session.getAttribute("userid");
            Set<UserRoleDTO> roles = new HashSet<UserRoleDTO>();
            projects = new ArrayList<>();
            List<UserRoleDTO> userRoleDTO = userRoleService.findByUserId(userId);
            for (UserRoleDTO userRole : userRoleDTO) {//현재 어떤 역할인지 알 수 없음
                userRole.setRole(null);
                roles.add(userRole);
            }
            for (UserRoleDTO userRole : new ArrayList<>(roles)){
                projects.add(projectService.findByProjectId(userRole.getProjectid()));
            }
        }
        
        List<ProjectInfoDTO> projects_info = new ArrayList<>();
        List<UserRoleDTO> projectUserDTO;
        List<MemberDTO> members;
        ProjectInfoDTO projectinfoDTO;
        
        for (ProjectDTO project : projects) {
            projectinfoDTO = new ProjectInfoDTO();
            projectinfoDTO.setProjectid(project.getProjectid());
            projectinfoDTO.setProjectname(project.getProjectname());
            projectinfoDTO.setProjectdescription(project.getProjectdescription());
            projectinfoDTO.setProjectcreatedtime(project.getProjectcreatedtime());
            projectUserDTO = new ArrayList<>();
            projectUserDTO = userRoleService.findByProjectId(project.getProjectid());
            System.out.println(projectUserDTO);
            members = new ArrayList<>();
            for (UserRoleDTO projectUser: projectUserDTO){
                members.add(memberService.findByUserId(projectUser.getUserid()));
            }
            Set<MemberDTO> memberSet = new HashSet<>(members);
            members = new ArrayList<>(memberSet);
            projectinfoDTO.setMembers(members);
            projects_info.add(projectinfoDTO);
        }
        return ResponseEntity.ok(projects_info);
    }
    @GetMapping("/api/addproject")
    public ResponseEntity<List<MemberDTO>> addProjectGet(HttpSession session) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(memberService.findAll());
    }

    @PostMapping("/api/addproject")
    public ResponseEntity<String> add_project_post(@RequestBody AddProjectDTO addProjectDTO, HttpSession session) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ProjectDTO projectDTO = addProjectDTO.getProjectDTO();
        if (projectService.isExistProjectName(projectDTO.getProjectname())) {
            return ResponseEntity.badRequest().body("이미 존재하는 프로젝트 이름입니다.");
        } else {
            projectService.register(projectDTO);
            Long projectid = projectService.findByProjectName(projectDTO.getProjectname()).getProjectid();
            UserRoleDTO userRoleDTO = new UserRoleDTO();
            userRoleDTO.setProjectid(projectid);

            for (String userid : addProjectDTO.getPl()) {
                userRoleDTO.setUserid(userid);
                userRoleDTO.setRole("PL");
                MemberDTO memberDTO = memberService.findByUserId(userid, true);
                userRoleService.add_user_role(userRoleDTO, memberDTO, projectid);
            }
            for (String userid : addProjectDTO.getDev()) {
                userRoleDTO.setUserid(userid);
                userRoleDTO.setRole("DEV");
                MemberDTO memberDTO = memberService.findByUserId(userid);
                userRoleService.add_user_role(userRoleDTO, memberDTO, projectid);
            }
            for (String userid : addProjectDTO.getTester()) {
                userRoleDTO.setUserid(userid);
                userRoleDTO.setRole("TESTER");
                MemberDTO memberDTO = memberService.findByUserId(userid);
                userRoleService.add_user_role(userRoleDTO, memberDTO, projectid);
            }
            for (String userid : addProjectDTO.getPm()) {
                userRoleDTO.setUserid(userid);
                userRoleDTO.setRole("PM");
                MemberDTO memberDTO = memberService.findByUserId(userid);
                userRoleService.add_user_role(userRoleDTO, memberDTO, projectid);
            }

            return ResponseEntity.ok("프로젝트 등록 성공");
        }
    }
    //프로젝트 상태 변경 API (스트링만 받음) -> 변경 가능 상태 : Not Started, In Progress, Completed, Paused, Cancelled
    @PostMapping("/api/project/{projectname}/update_status")
    public ResponseEntity<String> update_status_post(HttpSession session, @PathVariable String projectname, @RequestBody String status) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!status.equals("Not Started") && !status.equals("In Progress") && !status.equals("Completed") && !status.equals("Paused") && !status.equals("Cancelled")) {
            return ResponseEntity.badRequest().body("올바르지 않은 프로젝트 상태입니다.");
        }
        ProjectDTO existingProjectDTO = projectService.findByProjectName(projectname);
        if (existingProjectDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String past_status = existingProjectDTO.getProjectstatus();
        projectService.update_status(existingProjectDTO, status);
        return ResponseEntity.ok("프로젝트 상태 변경 성공 "+past_status+" -> "+status);
    }

    @GetMapping("/api/project/{projectname}")
    public ResponseEntity<ProjectDetailDTO> project_get(@PathVariable String projectname, HttpSession session) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProjectDTO projectDTO = projectService.findByProjectName(projectname);
        if (projectDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<UserRoleDTO> userRoleDTOList = userRoleService.findByProjectId(projectDTO.getProjectid());
        List<MemberDTO> PL = new ArrayList<>();
        List<MemberDTO> DEV = new ArrayList<>();
        List<MemberDTO> PM = new ArrayList<>();
        List<MemberDTO> TESTER = new ArrayList<>();

        for (UserRoleDTO userRole : userRoleDTOList) {
            MemberDTO memberDTO = memberService.findByUserId(userRole.getUserid());
            switch (userRole.getRole()) {
                case "PL":
                    PL.add(memberDTO);
                    break;
                case "DEV":
                    DEV.add(memberDTO);
                    break;
                case "PM":
                    PM.add(memberDTO);
                    break;
                case "TESTER":
                    TESTER.add(memberDTO);
                    break;
            }
        }

        ProjectDetailDTO projectDetailDTO = new ProjectDetailDTO(projectDTO, PL, DEV, PM, TESTER);
        return ResponseEntity.ok(projectDetailDTO);
    }

    @GetMapping("/api/project/{projectname}/{role}/list_user")
    public ResponseEntity<List<MemberDTO>> role_user_list(HttpSession session, @PathVariable String projectname, @PathVariable String role) {
        System.out.println(role);
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!role.equals("PL") && !role.equals("DEV") && !role.equals("PM") && !role.equals("TESTER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<MemberDTO> memberDTOs = memberService.findAll();
        for (MemberDTO memberDTO : memberDTOs) {
            memberDTO.setPassword(null);
        }
        Set<MemberDTO> memberDTOSet = new HashSet<>(memberDTOs);
        List<UserRoleDTO> userRoleDTOs = userRoleService.findByRole(role);
        List<MemberDTO> roleMembers = new ArrayList<>();
        for (UserRoleDTO userRoleDTO : userRoleDTOs) {
            MemberDTO memberDTO = memberService.findByUserId(userRoleDTO.getUserid());
            memberDTO.setPassword(null);
            roleMembers.add(memberDTO);
        }
        Set<MemberDTO> roleMemberSet = new HashSet<>(roleMembers);
        memberDTOSet.removeAll(roleMemberSet);
        return ResponseEntity.ok(new ArrayList<>(memberDTOSet));
    }

    @PostMapping("/api/project/{projectname}/adduser")
    public ResponseEntity<String> add_user_post(HttpSession session, @RequestBody List<UserRoleDTO> userRoleDTO, @PathVariable String projectname) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // 사용자 ID와 역할로 이미 존재하는 UserRole을 찾습니다.
        Long projectid = projectService.findByProjectName(projectname).getProjectid();
        for (UserRoleDTO userRole : userRoleDTO) {
            userRole.setProjectid(projectid);
            
            if (memberService.findByUserId(userRole.getUserid()) == null) {
                return ResponseEntity.badRequest().body(userRole.getUserid()+"는 존재하지 않는 사용자입니다.");
            }
            MemberEntity memberEntity = MemberEntity.toMemberEntity(memberService.findByUserId(userRole.getUserid()));
            UserRoleEntity existingUserRoleEntity = userRoleService.findByProjectAndMemberAndRole(ProjectEntity.toProjectEntity(projectService.findByProjectName(projectname)), memberEntity, userRole.getRole());
            if (existingUserRoleEntity != null) {
                return ResponseEntity.badRequest().body(userRole.getUserid()+"는 이미 "+userRole.getRole()+"입니다.");
            }
        }
        for (UserRoleDTO userRole : userRoleDTO) {
            userRoleService.add_user_role(userRole, memberService.findByUserId(userRole.getUserid()), projectid);
        }
        return ResponseEntity.ok("사용자 추가 성공");
    }
}
