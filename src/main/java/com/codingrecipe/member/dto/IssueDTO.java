package com.codingrecipe.member.dto;


import com.codingrecipe.member.entity.IssueEntity;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IssueDTO {
    private Long id;
    private Long writerId;
    private Long projectId;
    private Long devId;
    private String title;
    private String status; //enum?
    private String component;
    private String priority;
    private String significance;
    private String description;

    /**
     * 생성자
     */
    public IssueDTO(IssueEntity issueEntity){
        this.id=issueEntity.getId();
        this.writerId=issueEntity.getWriterId();
        this.projectId=issueEntity.getProjectId();
        this.devId=issueEntity.getDevId();
        this.title=issueEntity.getTitle();
        this.status= issueEntity.getStatus(); //enum?
        this.component= issueEntity.getComponent();
        this.priority= issueEntity.getPriority();
        this.significance= issueEntity.getSignificance();
        this.description=issueEntity.getDescription();
    }

    /**
     * setter방식
     */
    public IssueDTO toIssueDTO(IssueEntity issueEntity){
        IssueDTO issueDTO=new IssueDTO();

        issueDTO.setId(issueEntity.getId());
        issueDTO.setWriterId(issueEntity.getWriterId());
        issueDTO.setProjectId(issueEntity.getProjectId());
        issueDTO.setDevId(issueEntity.getDevId());
        issueDTO.setTitle(issueEntity.getTitle());
        issueDTO.setStatus(issueEntity.getStatus());
        issueDTO.setComponent(issueEntity.getComponent());
        issueDTO.setPriority(issueEntity.getPriority());
        issueDTO.setSignificance(issueEntity.getSignificance());
        issueDTO.setDescription(issueEntity.getDescription());

        return issueDTO;
    }
}
