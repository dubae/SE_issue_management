package com.codingrecipe.member.dto;

import com.codingrecipe.member.entity.ProjectEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectDTO {
    public Long projectid;
    public String projectname;
    public String projectdescription;
    public String projectcreatedtime;


    public static ProjectDTO toProjectDTO(ProjectEntity projectEntity) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectid(projectEntity.getProjectid());
        projectDTO.setProjectname(projectEntity.getProjectname());
        projectDTO.setProjectdescription(projectEntity.getProjectdescription());
        projectDTO.setProjectcreatedtime(projectEntity.getProjectcreatedtime());
        return projectDTO;
    }
}