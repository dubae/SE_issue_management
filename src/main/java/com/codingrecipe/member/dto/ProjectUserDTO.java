package com.codingrecipe.member.dto;
import com.codingrecipe.member.entity.ProjectUserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectUserDTO {
    Long projectid;
    String userid;

    public static ProjectUserDTO toProjectUserDTO(ProjectUserEntity projectUserEntity) {
        ProjectUserDTO projectUserDTO = new ProjectUserDTO();
        projectUserDTO.setProjectid(projectUserEntity.getProjectid());
        projectUserDTO.setUserid(projectUserEntity.getUserid());
        return projectUserDTO;
    }
}
