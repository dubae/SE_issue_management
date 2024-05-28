package com.codingrecipe.member.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailDTO {
    private ProjectDTO project;
    private List<MemberDTO> PL;
    private List<MemberDTO> DEV;
    private List<MemberDTO> PM;
    private List<MemberDTO> TESTER;
}
