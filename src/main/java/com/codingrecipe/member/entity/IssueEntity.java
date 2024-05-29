package com.codingrecipe.member.entity;

import com.codingrecipe.member.dto.IssueDTO;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.swing.*;
import java.util.*;

import java.time.LocalDate;

@Entity
@Getter
//@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setter
/**
 * 한 프로젝트 내 다수의 이슈 존재.
 * 1:n관계 설정 필요.
 */
public class IssueEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 20)
    private Long writerId;

    //@Column(nullable = false, length = 20)
    //private Long projectId;

    @Column(length = 20)
    private Long devId;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 20)
    private String status; //enum?

    @Column
    private String component;


    @Column
    private String priority;

    @Column
    private String significance;

    @Column(nullable = false, length = 20)
    private String description;

    @Column
    private Long fixerId;

    @OneToMany(mappedBy = "issueEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueCommentEntity> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectEntity projectEntity;

    public static IssueEntity toIssueEntity(IssueDTO issueDTO){
        IssueEntity issueEntity=new IssueEntity();

        issueEntity.setId(issueDTO.getId());
        issueEntity.setWriterId(issueDTO.getWriterId());
        issueEntity.getProjectEntity().setProjectid(issueDTO.getProjectId());
        issueEntity.setDevId(issueDTO.getDevId());
        issueEntity.setTitle(issueDTO.getTitle());
        issueEntity.setStatus(issueDTO.getStatus());
        issueEntity.setComponent(issueDTO.getComponent());
        issueEntity.setPriority(issueDTO.getPriority());
        issueEntity.setSignificance(issueDTO.getSignificance());
        issueEntity.setDescription(issueDTO.getDescription());
        issueEntity.setCreatedAt(LocalDate.now());
        issueEntity.setFixerId(issueDTO.getFixerId());

        return issueEntity;
    }

    /**
     *  여기서부터 고민이 필요.
     */
    public void addComment(IssueCommentEntity commentEntity){
        comments.add(commentEntity);
        commentEntity.setIssueEntity(this);
    }

}
