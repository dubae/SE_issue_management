package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class IssueController {

    private final IssueService issueService;

    @Autowired
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    /**
    프로젝트 별 이슈 리스트 반환(Json-IssueDto)
     */

    @GetMapping("/project/{projectId}/issue")
    @ResponseBody
    public List<IssueDTO> issue(@PathVariable("projectId") Long projectId, Model model) {
        return issueService.findAllIssue();
    }

    /**
     * 프로젝트 별 이슈 추가 사이트 구현(필요x)
     */
    @GetMapping("/project/{projectId}/issue/new")
    public String newIssue(@PathVariable("projectId") Long projectId, Model model) {
        return "addissue";

    }

    /**
     * 프로젝트 별 이슈 추가(Json-IssueDto 형태)
     */
    @ModelAttribute
    @PostMapping("/project/{projectId}/issue/new")
    public String addIssue(@ModelAttribute IssueDTO issueDTO, Model model) {
        issueService.addNewIssue(issueDTO);
        return "redirect:/";
    }

}
