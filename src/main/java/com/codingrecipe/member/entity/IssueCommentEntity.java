package com.codingrecipe.member.entity;

import lombok.*;

import javax.persistence.*;
import javax.swing.*;
import java.util.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

}
