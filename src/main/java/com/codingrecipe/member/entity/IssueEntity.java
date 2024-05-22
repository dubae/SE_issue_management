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
public class IssueEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 20)
    private Long writerId;

    @Column(nullable = false, length = 20)
    private Long projectId;

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



}
