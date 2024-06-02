package com.codingrecipe.member.controller;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final MemberService memberService;
    private final UserRoleService userRoleService;


    @GetMapping("/api/projects")
    public ResponseEntity<List<ProjectInfoDTO>> project_get(HttpServletRequest request){
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ProjectDTO> projects;
        if (SessionManager.getSession(sessionid).equals("admin")){
            projects = projectService.findAll();
        }
        else{
            String userId = (String) SessionManager.getSession(sessionid);
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
            projectinfoDTO.setProjectstatus(project.getProjectstatus());
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
    @ResponseBody
    @PostMapping(value = "/api/addproject", produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> add_project_post(@RequestBody AddProjectDTO addProjectDTO, HttpServletRequest request){
        System.out.println("hi~~");
        String sessionid = request.getHeader("sessionid");
        System.out.println("sessionid: "+ sessionid);
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ProjectDTO projectDTO = addProjectDTO.getProjectDTO();
        if (projectService.isExistProjectName(projectDTO.getProjectname())) {
            return ResponseEntity.badRequest().body("이미 존재하는 프로젝트 이름입니다.");
        } else {
            List<String> user_list = new ArrayList<>();
            user_list.addAll(addProjectDTO.getPl());
            user_list.addAll(addProjectDTO.getDev());
            user_list.addAll(addProjectDTO.getTester());

//            Set<String> user_set = new HashSet<>(user_list);
//            List<String> user_list_no_dup = user_set.stream().collect(Collectors.toList());
            List<String> user_list_no_dup = user_list.stream().distinct().collect(Collectors.toList());
            System.out.print("user_list_no_dup: \n");
            user_list_no_dup.forEach(System.out::println);
            String notUserId = "";
            for (String userid : user_list_no_dup) {
                if (memberService.findByUserId(userid) == null) {
                    notUserId = userid;
                    break;
                }
            }
            if(notUserId.length() > 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("'"+notUserId+"' 해당 유저의 정보를 찾을 수 없습니다.");

            // 그리고 이미 생성된 Id 값을 집어넣고 생성하니 오류 발생.
            if(!addProjectDTO.isEditingProject()) addProjectDTO.getProjectDTO().setProjectid(null);
            System.out.println("finish check dup~");
            System.out.println("projectDTO.getProjectname(): "+ projectDTO.getProjectname());
            // 여기 로직에 문제가 있었음. 생성 직후 조회하는 과정에 문제가 발생해서, 생성된 값을 바로 가져오도록 조회.
            Long projectid = projectService.register(projectDTO).getProjectid();
            System.out.println("projectid: "+projectid);
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

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }
    //프로젝트 상태 변경 API (스트링만 받음) -> 변경 가능 상태 : Not Started, In Progress, Completed, Paused, Cancelled
    @Transactional
    @PostMapping("/api/project/{projectname}/update_status")
    public ResponseEntity<String> update_status_post(@PathVariable String projectname, @RequestBody String status, HttpServletRequest request){
        System.out.println("status: "+status);
        String sessionid = request.getHeader("sessionid");
        System.out.println("sessionid: "+sessionid);
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!status.equals("Not Started") && !status.equals("In Progress") && !status.equals("Completed") && !status.equals("Paused") && !status.equals("Cancelled")) {
            System.out.println("올바르지 않은~");
            return ResponseEntity.badRequest().body("올바르지 않은 프로젝트 상태입니다.");
        }
        ProjectDTO existingProjectDTO = projectService.findByProjectName(projectname);
        System.out.println("existingProjectDTO: "+existingProjectDTO);
        if (existingProjectDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String past_status = existingProjectDTO.getProjectstatus();
        System.out.println("past_status:" + past_status);
        projectService.update_status(existingProjectDTO, status);
        return ResponseEntity.ok("프로젝트 상태 변경에 성공 하였습니다." + past_status + " -> " + status);
    }

    @GetMapping("/api/project/{projectname}")
    public ResponseEntity<ProjectDetailDTO> project_get(@PathVariable String projectname, HttpServletRequest request){
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProjectDTO projectDTO = projectService.findByProjectName(projectname);
        if (projectDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<UserRoleDTO> userRoleDTOList = userRoleService.findByProjectId(projectDTO.getProjectid());
        List<MemberDTOSecure> PL = new ArrayList<>();
        List<MemberDTOSecure> DEV = new ArrayList<>();
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
                case "TESTER":
                    TESTER.add(MemberDTOSecure.toMemberDTOSecure(memberDTO));
                    break;
            }
        }

        ProjectDetailDTO projectDetailDTO = new ProjectDetailDTO(projectDTO, PL, DEV, TESTER);
        return ResponseEntity.status(HttpStatus.OK).body(projectDetailDTO);
    }

    @GetMapping("/api/project/{projectname}/{role}/list_addable_user")
    public ResponseEntity<List<MemberDTOSecure>> role_addable_user_list(@PathVariable String projectname, @PathVariable String role, HttpServletRequest request){
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!role.equals("PL") && !role.equals("DEV") && !role.equals("TESTER")) {
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
    public ResponseEntity<List<MemberDTOSecure>> role_user_list(@PathVariable String projectname, @PathVariable String role, HttpServletRequest request){
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!role.equals("PL") && !role.equals("DEV") && !role.equals("TESTER")) {
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
    public ResponseEntity<String> add_user_post(@RequestBody List<UserRoleDTO> userRoleDTO, @PathVariable String projectname, HttpServletRequest request){
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
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
    public ResponseEntity<String> delete_project_get(@PathVariable String projectname, HttpServletRequest request){
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        System.out.println(projectname);
        ProjectDTO projectDTO = projectService.findByProjectName(projectname);
        System.out.println(projectDTO);
        if (projectDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<UserRoleDTO> userRoleDTOList = userRoleService.findByProjectId(projectDTO.getProjectid());
        List<MemberDTO> PL = new ArrayList<>();
        for (UserRoleDTO userRole : userRoleDTOList) {
            MemberDTO memberDTO = memberService.findByUserId(userRole.getUserid());
            if (userRole.getRole().equals("PL")) PL.add(memberDTO);
        }
        if (!SessionManager.getSession(sessionid).equals("admin") && !PL.contains(memberService.findByUserId((String) SessionManager.getSession(sessionid)))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (projectService.deleteByProjectName(projectname)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Transactional
    @GetMapping("/api/project/{projectname}/{userid}/getrole")
    public ResponseEntity<List<String>> user_project_role(@PathVariable String projectname, @PathVariable String userid, HttpServletRequest request){
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ProjectEntity projectEntity = projectService.findByProjectNameEntity(projectname);
        if (SessionManager.getSession(sessionid).equals("admin")){
            
        }
        else if (projectEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<UserRoleDTO> userRoleDTO = userRoleService.findByProjectAndMember(projectEntity, MemberEntity.toMemberEntity(memberService.findByUserId(userid)));
        if (SessionManager.getSession(sessionid).equals("admin") && userRoleDTO.size() == 0) {
            List<String> data = new ArrayList<>();
            data.add("admin");
            return ResponseEntity.status(HttpStatus.OK).body(data);
        }
        if (userRoleDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<String> roles = new ArrayList<>();
        for (UserRoleDTO userRole : userRoleDTO) {
            roles.add(userRole.getRole());
        }
        System.out.println(roles.toString());
        return ResponseEntity.status(HttpStatus.OK).body(roles);
        
    }
    
}