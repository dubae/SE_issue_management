package com.codingrecipe.member.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import com.codingrecipe.member.dto.ProjectDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "projects")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectid")
    private Long projectid;
    @Column(name = "projectname", unique = true, nullable = false)
    private String projectname;

    @Column(name = "description")
    private String projectdescription;

    @Column(name = "createdtime", nullable = false)
    private String projectcreatedtime;

    @Column(name = "status", nullable = false)
    private String projectstatus;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueEntity> issueEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRoleEntity> userRoles = new HashSet<>();

    public static ProjectEntity toProjectEntity(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setProjectid(projectDTO.getProjectid());
        projectEntity.setProjectname(projectDTO.getProjectname());
        projectEntity.setProjectdescription(projectDTO.getProjectdescription());
        projectEntity.setProjectcreatedtime(projectDTO.getProjectcreatedtime());
        projectEntity.setProjectstatus(projectDTO.getProjectstatus());
        return projectEntity;
    }
}