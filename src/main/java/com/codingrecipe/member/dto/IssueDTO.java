package com.codingrecipe.member.dto;


import com.codingrecipe.member.entity.IssueCommentEntity;
import com.codingrecipe.member.entity.IssueEntity;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IssueDTO extends BaseDTO{
    private Long id;
    private String writerId;
    private Long projectId;
    private Long devId;
    private Long fixerId;
    private String title;
    private String status; //enum?
    private String component;
    private String priority;
    private String significance;
    private String description;
    private ProjectDTO projectDTO;
    private String userId;

    private List<IssueCommentDTO> issueCommentDTOList=new ArrayList<>();

    /**
     * 생성자 방식
     */

    public IssueDTO(IssueEntity issueEntity){
        this.id=issueEntity.getId();
        this.writerId=issueEntity.getWriterId();
        this.projectId=issueEntity.getProjectEntity().getProjectid();
        this.devId=issueEntity.getDevId();
        this.title=issueEntity.getTitle();
        this.status= issueEntity.getStatus(); //enum?
        this.component= issueEntity.getComponent();
        this.priority= issueEntity.getPriority();
        this.significance= issueEntity.getSignificance();
        this.description=issueEntity.getDescription();
        this.createdAt=issueEntity.getCreatedAt();
        this.updatedAt=issueEntity.getUpdatedAt();
        this.fixerId=issueEntity.getFixerId();

        this.projectDTO=ProjectDTO.toProjectDTO(issueEntity.getProjectEntity());
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
        issueDTO.setProjectId(issueEntity.getProjectEntity().getProjectid());
        issueDTO.setDevId(issueEntity.getDevId());
        issueDTO.setTitle(issueEntity.getTitle());
        issueDTO.setStatus(issueEntity.getStatus());
        issueDTO.setComponent(issueEntity.getComponent());
        issueDTO.setPriority(issueEntity.getPriority());
        issueDTO.setSignificance(issueEntity.getSignificance());
        issueDTO.setDescription(issueEntity.getDescription());
        issueDTO.setCreatedAt(issueEntity.getCreatedAt());
        issueDTO.setUpdatedAt(issueEntity.getUpdatedAt());
        issueDTO.setFixerId(issueEntity.getFixerId());

        issueDTO.setProjectDTO(ProjectDTO.toProjectDTO(issueEntity.getProjectEntity()));

        for(IssueCommentEntity issueCommentEntity:issueEntity.getComments()){
            issueCommentDTOList.add(new IssueCommentDTO(issueCommentEntity));
        }

        return issueDTO;
    }

    @Override
    public boolean equals(Object o) {
        return Objects.equals(this.id, ((IssueDTO) o).getId());
    }
}
