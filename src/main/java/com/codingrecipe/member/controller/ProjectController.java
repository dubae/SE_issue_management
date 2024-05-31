package com.codingrecipe.member.controller;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.codingrecipe.member.dto.AddProjectDTO;
import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.dto.MemberDTOSecure;
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
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import com.codingrecipe.member.session.SessionManager;

import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final MemberService memberService;
    private final UserRoleService userRoleService;
    private final TransactionTemplate transactionTemplate;


    @PostMapping("/api/projects")
    public ResponseEntity<List<ProjectInfoDTO>> project_get(HttpServletRequest request){
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getAttribute(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ProjectDTO> projects;
        if (SessionManager.getAttribute(sessionid).equals("admin")){
            projects = projectService.findAll();
        }
        else{
            String userId = (String) SessionManager.getAttribute(sessionid);
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
        List<MemberDTOSecure> members_secure;
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
            members_secure = new ArrayList<>();
            for (UserRoleDTO projectUser: projectUserDTO){
                members_secure.add(MemberDTOSecure.toMemberDTOSecure(memberService.findByUserId(projectUser.getUserid())));
            }
            Set<MemberDTOSecure> members_secureSet = new HashSet<>(members_secure);
            members_secure = new ArrayList<>(members_secureSet);
            projectinfoDTO.setMembers(members_secure);
            projects_info.add(projectinfoDTO);
        }
        return ResponseEntity.ok(projects_info);
    }

    @Transactional
    @PostMapping("/api/addproject")
    public ResponseEntity<String> add_project_post(@RequestBody AddProjectDTO addProjectDTO, HttpSession session) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ProjectDTO projectDTO = addProjectDTO.getProjectDTO();
        if (projectService.isExistProjectName(projectDTO.getProjectname())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
                        List<String> user_list = new ArrayList<>();
            user_list.addAll(addProjectDTO.getPl());
            user_list.addAll(addProjectDTO.getDev());
            user_list.addAll(addProjectDTO.getTester());
            user_list.addAll(addProjectDTO.getPm());

            Set<String> user_set = new HashSet<>(user_list);
            List<String> user_list_no_dup = new ArrayList<>(user_set);
            for (String userid : user_list_no_dup) {
                if (memberService.findByUserId(userid) == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userid);
                }
            }
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

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }
    //프로젝트 상태 변경 API (스트링만 받음) -> 변경 가능 상태 : Not Started, In Progress, Completed, Paused, Cancelled
    @Transactional
    @PostMapping("/api/project/{projectname}/update_status")
    public ResponseEntity<String> update_status_post(HttpSession session, @PathVariable String projectname, @RequestBody String status) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!status.equals("Not Started") && !status.equals("In Progress") && !status.equals("Completed") && !status.equals("Paused") && !status.equals("Cancelled")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
        }
        ProjectDTO existingProjectDTO = projectService.findByProjectName(projectname);
        if (existingProjectDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        projectService.update_status(existingProjectDTO, status);
        return ResponseEntity.status(HttpStatus.OK).build();
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
        List<MemberDTOSecure> PL = new ArrayList<>();
        List<MemberDTOSecure> DEV = new ArrayList<>();
        List<MemberDTOSecure> PM = new ArrayList<>();
        List<MemberDTOSecure> TESTER = new ArrayList<>();

        for (UserRoleDTO userRole : userRoleDTOList) {
            MemberDTO memberDTO = memberService.findByUserId(userRole.getUserid());
            switch (userRole.getRole()) {
                case "PL":
                    PL.add(MemberDTOSecure.toMemberDTOSecure(memberDTO));
                    break;
                case "DEV":
                    DEV.add(MemberDTOSecure.toMemberDTOSecure(memberDTO));
                    break;
                case "PM":
                    PM.add(MemberDTOSecure.toMemberDTOSecure(memberDTO));
                    break;
                case "TESTER":
                    TESTER.add(MemberDTOSecure.toMemberDTOSecure(memberDTO));
                    break;
            }
        }

        ProjectDetailDTO projectDetailDTO = new ProjectDetailDTO(projectDTO, PL, DEV, PM, TESTER);
        return ResponseEntity.status(HttpStatus.OK).body(projectDetailDTO);
    }

    @GetMapping("/api/project/{projectname}/{role}/list_addable_user")
    public ResponseEntity<List<MemberDTOSecure>> role_addable_user_list(HttpSession session, @PathVariable String projectname, @PathVariable String role) {
        System.out.println(role);
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!role.equals("PL") && !role.equals("DEV") && !role.equals("PM") && !role.equals("TESTER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<MemberDTO> memberDTOs = memberService.findAll();
        Set<MemberDTO> memberDTOSet = new HashSet<>(memberDTOs);
        List<UserRoleDTO> userRoleDTOs = userRoleService.findByRole(role);
        List<MemberDTO> projectMembers = new ArrayList<>();
        for (UserRoleDTO userRoleDTO : userRoleDTOs) {
            MemberDTO memberDTO = memberService.findByUserId(userRoleDTO.getUserid());
            projectMembers.add(memberDTO);
        }
        Set<MemberDTO> projectMemberSet = new HashSet<>(projectMembers);
        memberDTOSet.removeAll(projectMemberSet);
        List<MemberDTOSecure> memberDTOSecures = MemberDTOSecure.toMemberDTOSecureList(new ArrayList<>(memberDTOSet));
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(memberDTOSecures));
    }

    @GetMapping("/api/project/{projectname}/{role}/list_user")
    public ResponseEntity<List<MemberDTOSecure>> role_user_list(HttpSession session, @PathVariable String projectname, @PathVariable String role) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!role.equals("PL") && !role.equals("DEV") && !role.equals("PM") && !role.equals("TESTER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<UserRoleDTO> userRoleDTOs = userRoleService.findByProjectId(projectService.findByProjectName(projectname).getProjectid());
        List<MemberDTOSecure> memberDTOSecures = new ArrayList<>();
        for (UserRoleDTO userRoleDTO : userRoleDTOs) {
            if (userRoleDTO.getRole().equals(role)) {
                MemberDTOSecure memberDTOSecure = MemberDTOSecure.toMemberDTOSecure(memberService.findByUserId(userRoleDTO.getUserid()));
                memberDTOSecures.add(memberDTOSecure);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(memberDTOSecures);
    }
    @Transactional
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userRole.getUserid());
            }
            MemberEntity memberEntity = MemberEntity.toMemberEntity(memberService.findByUserId(userRole.getUserid()));
            UserRoleEntity existingUserRoleEntity = userRoleService.findByProjectAndMemberAndRole(ProjectEntity.toProjectEntity(projectService.findByProjectName(projectname)), memberEntity, userRole.getRole());
            if (existingUserRoleEntity != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(userRole.getUserid()+":"+userRole.getRole());
            }
        }
        for (UserRoleDTO userRole : userRoleDTO) {
            userRoleService.add_user_role(userRole, memberService.findByUserId(userRole.getUserid()), projectid);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Transactional
    @GetMapping("/api/project/{projectname}/delete")
    public ResponseEntity<String> delete_project_get(HttpSession session, @PathVariable String projectname) {
        if (session.getAttribute("userid") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ProjectDTO projectDTO = projectService.findByProjectName(projectname);
        if (projectDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<UserRoleDTO> userRoleDTOList = userRoleService.findByProjectId(projectDTO.getProjectid());
        List<MemberDTO> PL = new ArrayList<>();
        for (UserRoleDTO userRole : userRoleDTOList) {
            MemberDTO memberDTO = memberService.findByUserId(userRole.getUserid());
            if (userRole.getRole().equals("PL")) PL.add(memberDTO);
        }
        if (!session.getAttribute("userid").equals("admin") && !PL.contains(memberService.findByUserId((String) session.getAttribute("userid")))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (projectService.deleteByProjectName(projectname)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
