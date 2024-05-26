package com.codingrecipe.member.controller;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.dto.ProjectInfoDTO;
import com.codingrecipe.member.dto.UserRoleDTO;
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


    @GetMapping("/projects")
    public String project_get(HttpSession session, Model model){
        if (session.getAttribute("userid") == null) {
            return "redirect:/login";
        }
        List<ProjectInfoDTO> projects_info = new ArrayList<>();
        List<ProjectDTO> projects = new ArrayList<>();
        projects = projectService.findAll();
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
        System.out.println(projects_info);
        model.addAttribute("userid", session.getAttribute("userid"));
        model.addAttribute("projects", projects_info);
        return "projects";
    }
    @GetMapping("/addproject")
    public String add_project_get(HttpSession session, Model model){
        if (session.getAttribute("userid") == null) {
            return "redirect:/login";
        }
        model.addAttribute("userid", session.getAttribute("userid"));
        model.addAttribute("users", memberService.findAll());
        return "addproject";
    }
    @PostMapping("/addproject")
    public String add_project_post(@ModelAttribute ProjectDTO projectDTO,@RequestParam List<String> pl,@RequestParam List<String> dev, @RequestParam List<String> tester,@RequestParam List<String> pm, HttpSession session, Model model) {
        if (session.getAttribute("userid") == null) {
            return "redirect:/login";
        }
        else {
            if (projectService.isExistProjectName(projectDTO.getProjectname())) {
                model.addAttribute("errorMessage", "프로젝트 이름이 이미 존재합니다.");
                return "addproject";
            }
            else{
                projectService.register(projectDTO);
                Long projectid = projectService.findByProjectName(projectDTO.getProjectname()).getProjectid();
                UserRoleDTO userRoleDTO = new UserRoleDTO();
                userRoleDTO.setProjectid(projectid);
                for (String userid : pl) {
                    userRoleDTO.setUserid(userid);
                    userRoleDTO.setRole("PL");
                    userRoleService.add_user_role(userRoleDTO);
                }
                for (String userid : dev) {
                    userRoleDTO.setUserid(userid);
                    userRoleDTO.setRole("DEV");
                    userRoleService.add_user_role(userRoleDTO);
                }
                for (String userid : tester) {
                    userRoleDTO.setUserid(userid);
                    userRoleDTO.setRole("TESTER");
                    userRoleService.add_user_role(userRoleDTO);
                }
                for (String userid : pm) {
                    userRoleDTO.setUserid(userid);
                    userRoleDTO.setRole("PM");
                    userRoleService.add_user_role(userRoleDTO);
                }
                return "redirect:/projects";
            }
        }
    }

    @GetMapping("/project/{projectname}")
    public String project_get (@PathVariable String projectname, Model model, HttpSession session){
        if (session.getAttribute("userid") == null) {
            return "redirect:/login";
        }
        model.addAttribute("userid", session.getAttribute("userid"));
        model.addAttribute("project", projectService.findByProjectName(projectname));
        List<UserRoleDTO> userRoleDTO = new ArrayList<>();
        ProjectDTO projectDTO = projectService.findByProjectName(projectname);
        userRoleDTO = userRoleService.findByProjectId(projectDTO.getProjectid());
        List<MemberDTO> PL = new ArrayList<>();
        List<MemberDTO> DEV = new ArrayList<>();
        List<MemberDTO> PM = new ArrayList<>();
        List<MemberDTO> TESTER = new ArrayList<>();

        for (UserRoleDTO userRole : userRoleDTO) {
            MemberDTO memberDTO = memberService.findByUserid(userRole.getUserid());
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
        model.addAttribute("PL", PL);
        model.addAttribute("DEV", DEV);
        model.addAttribute("PM", PM);
        model.addAttribute("TESTER", TESTER);
        return "projectdetail";
    }

    @GetMapping("/adduser")
    public String add_user_get(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("userid") == null) {
            return "redirect:/login";
        }
        if (session.getAttribute("userid").equals("admin")) {
            model.addAttribute("projects", projectService.findAll());
            model.addAttribute("users", memberService.findAll());
            return "adduser";
        }
        String userId = (String) session.getAttribute("userid");
        Set<UserRoleDTO> roles = new HashSet<UserRoleDTO>();
        List<ProjectDTO> projects = new ArrayList<>();
        List<UserRoleDTO> userRoleDTO = userRoleService.findByUserId(userId);
        for (UserRoleDTO userRole : userRoleDTO) {//현재 어떤 역할인지 알 수 없음
            userRole.setRole(null);
            roles.add(userRole);
        }
        for (UserRoleDTO userRole : new ArrayList<>(roles)){
            projects.add(projectService.findByProjectId(userRole.getProjectid()));
        }
        
        if (projects.size() == 0) {
            redirectAttributes.addFlashAttribute("Error", "참여중인 프로젝트가 없습니다.");
            return "redirect:/projects";
        }
        model.addAttribute("projects", new ArrayList<>(projects));//projectService.findAll()
        model.addAttribute("users", memberService.findAll());
        return "adduser";
    }

    @PostMapping("/adduser")
    public String add_user_post(HttpSession session, @ModelAttribute UserRoleDTO userRoleDTO, Model model, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("userid") == null) {
            return "redirect:/login";
        }
        
        // 사용자 ID와 역할로 이미 존재하는 UserRole을 찾습니다.
        UserRoleDTO existingUserRoleDTO = userRoleService.findByProjectidAndUseridAndRole(userRoleDTO.getProjectid(), userRoleDTO.getUserid(), userRoleDTO.getRole());
        
        // 이미 존재하는 경우, 예외를 발생시킵니다.
        if (existingUserRoleDTO != null) {
            redirectAttributes.addFlashAttribute("Error", "이미 같은 역할을 가진 사용자가 존재합니다.");
            return "redirect:/adduser";
        }
        else{
            userRoleService.add_user_role(userRoleDTO);
            return "redirect:/projects";
        }
        
        
    }
}
