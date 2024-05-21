package com.codingrecipe.member.entity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

import com.codingrecipe.member.dto.ProjectUserDTO;


@Entity
@Getter
@Setter
@Table(name = "project_user")
public class ProjectUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "projectid")
    private Long projectid;

    @Column(name = "userid")
    private String userid;

    public static ProjectUserEntity toProjectUserEntity(ProjectUserDTO projectUserDTO) {
        ProjectUserEntity projectUserEntity = new ProjectUserEntity();
        projectUserEntity.setProjectid(projectUserDTO.getProjectid());
        projectUserEntity.setUserid(projectUserDTO.getUserid());
        return projectUserEntity;
    }
}
