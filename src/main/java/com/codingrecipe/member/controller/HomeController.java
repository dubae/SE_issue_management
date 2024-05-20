package com.codingrecipe.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.service.ProjectService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProjectService projectService;
    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/projects")
    public String project_get(HttpSession session, Model model){
        if (session.getAttribute("userid") == null) {
            return "redirect:/login";
        }
        model.addAttribute("userid", session.getAttribute("userid"));
        model.addAttribute("projects", projectService.findAll());
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
    
}
