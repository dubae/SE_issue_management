package com.codingrecipe.member.controller;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.dto.ProjectInfoDTO;
import com.codingrecipe.member.dto.ProjectUserDTO;
import com.codingrecipe.member.service.MemberService;
import com.codingrecipe.member.service.ProjectService;
import com.codingrecipe.member.service.ProjectUserService;

import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final MemberService memberService;
    private final ProjectUserService projectUserService;

    @GetMapping("/projects")
    public String project_get(HttpSession session, Model model){
        if (session.getAttribute("userid") == null) {
            return "redirect:/login";
        }
        List<ProjectInfoDTO> projects_info = new ArrayList<>();
        List<ProjectDTO> projects = new ArrayList<>();
        projects = projectService.findAll();
        List<ProjectUserDTO> projectUserDTO;
        List<MemberDTO> members;
        ProjectInfoDTO projectinfoDTO;
        
        for (ProjectDTO project : projects) {
            projectinfoDTO = new ProjectInfoDTO();
            projectinfoDTO.setProjectid(project.getProjectid());
            projectinfoDTO.setProjectname(project.getProjectname());
            projectinfoDTO.setProjectdescription(project.getProjectdescription());
            projectinfoDTO.setProjectcreatedtime(project.getProjectcreatedtime());
            projectUserDTO = new ArrayList<>();
            projectUserDTO = projectUserService.findByProjectId(project.getProjectid());
            System.out.println(projectUserDTO);
            members = new ArrayList<>();
            for (ProjectUserDTO projectUser: projectUserDTO){
                members.add(memberService.findByUserId(projectUser.getUserid()));
            }
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
        return "addproject";
    }
    @PostMapping("/addproject")
    public String add_project_post(@ModelAttribute ProjectDTO projectDTO, HttpSession session) {
        if (session.getAttribute("userid") == null) {
            return "redirect:/login";
        }
        else {
            if (projectService.isExistProjectName(projectDTO.getProjectname())) {
                return "addproject";
            }
            else{
                projectService.register(projectDTO);
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
        return "projectdetail";
    }

    @GetMapping("/adduser")
    public String add_user_get(Model model) {
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("users", memberService.findAll());
        return "adduser";
    }

    @PostMapping("/adduser")
    public String add_user_post(@ModelAttribute ProjectUserDTO projectUserDTO) {
        System.out.println(projectUserDTO);
        projectUserService.register(projectUserDTO);
        return "redirect:/projects";
    }
}
