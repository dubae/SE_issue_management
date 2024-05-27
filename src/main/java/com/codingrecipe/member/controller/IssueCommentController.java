package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.IssueCommentDTO;
import com.codingrecipe.member.entity.IssueCommentEntity;
import com.codingrecipe.member.service.IssueCommentService;
import com.codingrecipe.member.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Transactional
public class IssueCommentController {

    private IssueCommentService issueCommentService;
    private IssueService issueService;
    @Autowired
    public void setIssueCommentService(IssueCommentService issueCommentService) {
        this.issueCommentService = issueCommentService;
    }
    @Autowired
    public void setIssueService(IssueService issueService) {
        this.issueService = issueService;
    }

    /**
     * 새로운 댓글 추가
     */
    @ModelAttribute
    @PostMapping("/project/{projectId}/issue/{issueId}/comment/new")
    public void newIssueComment(@PathVariable("projectId") Long projectId, @PathVariable Long issueId,
                                  @ModelAttribute IssueCommentDTO issueCommentDTO, Model model) {
        issueCommentDTO.setIssueId(issueId);
        issueCommentService.save(issueCommentDTO);
    }

    /**
     * 댓글 id로 하나의 댓글 조회.
     */
    @GetMapping("/project/{projectId}/issue/{issueId}/comment/{commentId}")
    @ResponseBody
    public IssueCommentDTO findById(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId, @PathVariable("commentId") Long commentId) {
       return issueCommentService.findById(commentId);
    }

    /**
     * 한 이슈에 대한 댓글 리스트 조회
     */
    @GetMapping("/project/{projectId}/issue/{issueId}/comment")
    @ResponseBody
    public List<IssueCommentDTO> findByIssueId(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId) {
        return issueCommentService.findByIssueId(issueId);
    }

    /**
     * (테스트용) 모든 댓글 조회
     */
    @GetMapping("/test/comment")
    @ResponseBody
    public List<IssueCommentDTO> findAll(){
        return issueCommentService.findAll();
    }




}
