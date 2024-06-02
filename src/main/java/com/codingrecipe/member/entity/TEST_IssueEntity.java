package com.codingrecipe.member.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TEST_IssueEntity extends IssueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String writerId;

    @Column
    private String devId;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 20)
    private String status;

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
//
//    @OneToMany(mappedBy = "testIssueEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<IssueCommentEntity> comments = new ArrayList<>();
//
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "project_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private ProjectEntity projectEntity;

    // Additional methods or overrides specific to TEST_IssueEntity can be added here
}
