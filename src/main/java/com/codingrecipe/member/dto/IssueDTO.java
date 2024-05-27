package com.codingrecipe.member.dto;


import com.codingrecipe.member.entity.IssueCommentEntity;
import com.codingrecipe.member.entity.IssueEntity;
import lombok.*;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IssueDTO extends BaseDTO{
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

    private List<IssueCommentDTO> issueCommentDTOList=new ArrayList<>();

    /**
     * 생성자 방식
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
        this.createdAt=issueEntity.getCreatedAt();
        this.updatedAt=issueEntity.getUpdatedAt();
        for(IssueCommentEntity issueCommentEntity:issueEntity.getComments()){
            issueCommentDTOList.add(new IssueCommentDTO(issueCommentEntity));
        }
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
        for(IssueCommentEntity issueCommentEntity:issueEntity.getComments()){
            issueCommentDTOList.add(new IssueCommentDTO(issueCommentEntity));
        }

        return issueDTO;
    }
}
