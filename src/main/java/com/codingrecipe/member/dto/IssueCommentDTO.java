package com.codingrecipe.member.dto;


import com.codingrecipe.member.entity.IssueCommentEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IssueCommentDTO extends  BaseDTO{
    private Long id;
    private String writerId;
    private String content;
    private Long issueId;


    /**
     * 생성자 방식
     */
    public IssueCommentDTO(IssueCommentEntity issueCommentEntity) {
        this.id=issueCommentEntity.getId();
        this.writerId=issueCommentEntity.getWriterId();
        this.content=issueCommentEntity.getContent();
        this.issueId=issueCommentEntity.getIssueEntity().getId();
        this.createdAt=issueCommentEntity.getCreatedAt();
        this.updatedAt=issueCommentEntity.getUpdatedAt();
    }

    /**
     * setter방식
     */
    public IssueCommentDTO toIssueCommentDTO(IssueCommentEntity issueCommentEntity){
        IssueCommentDTO issueCommentDTO = new IssueCommentDTO();

        issueCommentDTO.setId(issueCommentEntity.getId());
        issueCommentDTO.setWriterId(issueCommentEntity.getWriterId());
       issueCommentDTO.setIssueId(issueCommentEntity.getIssueEntity().getId());
        issueCommentDTO.setContent(issueCommentEntity.getContent());

        return issueCommentDTO;
    }
}
