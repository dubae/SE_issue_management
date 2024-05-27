package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Transactional
public class IssueController {

    private final IssueService issueService;

    @Autowired
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    /**
    프로젝트 별 이슈 리스트 반환(Json-IssueDto)
     */
//    @GetMapping("/project/{projectId}/issue")
//    @ResponseBody
//    public List<IssueDTO> issue(@PathVariable("projectId") Long projectId, Model model) {
//        return issueService.findByProjectId(projectId);
//    }
    //json형태로 받을 거면 위에 선택, 인터페이스는 아래 선택.
    @GetMapping("/project/{projectId}/issue")
    public String issue(@PathVariable("projectId") Long projectId, Model model) {
        List<IssueDTO> issues = issueService.findByProjectId(projectId);
        model.addAttribute("issues", issues);
        return "issues";
    }


    /**
     * 프로젝트 별 이슈 추가 사이트 구현(필요x)
     */
    @GetMapping("/project/{projectId}/issue/new")
    public String newIssue(@PathVariable("projectId") Long projectId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("issueDTO", new IssueDTO());
        return "addissue";
    }



    /**
     * 프로젝트 별 이슈 추가(Json-IssueDto 형태)
     */
    @ModelAttribute
    @PostMapping("/project/{projectId}/issue/new")
    public String addIssue(@ModelAttribute IssueDTO issueDTO, Model model) {
        issueService.addNewIssue(issueDTO);
        return "redirect:/addissue";
    }

    /**
     * 하나의 이슈 자세히 보기.
     * IssueDto객체를 Json형태로 반환함.
     */
    @GetMapping("/project/{projectId}/issue/{issueId}")
    @ResponseBody
    public IssueDTO issue_info(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueID, Model model) {
        return issueService.findById(issueID);

    }

    /**
     * 이름으로 이슈 찾기. issueDto의 리스트로 반환되며, 댓글 역시 같이 반환됨. fetch속도 ?
     */
    @GetMapping("/project/{projectId}/issue/find")
    @ResponseBody
    public List<IssueDTO> findByTitle(@PathVariable("projectId") Long projectId, @RequestParam String title) {
        return issueService.findByTitle(title);
    }

    /**
     * 이슈의 상태 변경하기(new->assigned ...)
     */
    @ModelAttribute
    @PostMapping("/project/{projectId}/issue/{issueId}/status")
    public void changeStatus(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId,@RequestParam String status, Model model) {
        issueService.changeStatus(issueId,status);
    }

    /**
     * 이슈의 개발자 지정(변경)하기. 개발자 id를 req.param으로 넣기.
     */
    @ModelAttribute
    @PostMapping("/project/{projectId}/issue/{issueId}/devId")
    public void changeDevId(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId,@RequestParam Long devId, Model model){
        issueService.changeDevId(issueId,devId);
    }

}