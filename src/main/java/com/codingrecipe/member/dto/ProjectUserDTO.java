package com.codingrecipe.member.dto;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUserDTO {
    private List<ProjectDTO> projects;
    private List<MemberDTO> users;
}