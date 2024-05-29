package com.codingrecipe.member.entity;

import com.codingrecipe.member.dto.IssueCommentDTO;
import com.codingrecipe.member.repository.IssueCommentRepository;
import com.codingrecipe.member.repository.IssueRepository;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Setter
/**
 * 하나의 티켓 내에 다수의 댓글(comment)을 담당하는 개체.
 */
public class IssueCommentEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private Long writerId;

    @Column(nullable = false, length = 20)
    private String content;

//    @Column(nullable = false, length = 20)
//    private Long issueId;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "issue_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private IssueEntity issueEntity;

    public static IssueCommentEntity toIssueCommentEntity(IssueCommentDTO issueCommentDTO) {
        IssueCommentEntity issueCommentEntity = new IssueCommentEntity();

       //
        issueCommentEntity.setId(issueCommentDTO.getId());
        issueCommentEntity.setContent(issueCommentDTO.getContent());
        issueCommentEntity.setWriterId(issueCommentDTO.getWriterId());

        return issueCommentEntity;
    }
    

}
