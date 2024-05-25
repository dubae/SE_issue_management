package com.codingrecipe.member.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Column(nullable = false, length = 20)
    private Long issueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issueId", insertable = false, updatable = false)
    private IssueEntity issueEntity;

}
