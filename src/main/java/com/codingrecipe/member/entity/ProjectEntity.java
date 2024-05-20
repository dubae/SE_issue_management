package com.codingrecipe.member.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

import com.codingrecipe.member.dto.ProjectDTO;

@Entity
@Getter
@Setter
@Table(name = "projects")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectid;

    @Column(name = "projectname")
    private String projectname;

    @Column(name = "description")
    private String projectdescription;

    @Column(name = "createdtime")
    private String projectcreatedtime;

    public static ProjectEntity toProjectEntity(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setProjectname(projectDTO.getProjectname());
        projectEntity.setProjectdescription(projectDTO.getProjectdescription());
        projectEntity.setProjectcreatedtime(projectDTO.getProjectcreatedtime());
        return projectEntity;
    }
}
