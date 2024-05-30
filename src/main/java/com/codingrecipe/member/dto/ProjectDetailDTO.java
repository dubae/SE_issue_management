package com.codingrecipe.member.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailDTO {
    private ProjectDTO project;
    private List<MemberDTOSecure> PL;
    private List<MemberDTOSecure> DEV;
    private List<MemberDTOSecure> PM;
    private List<MemberDTOSecure> TESTER;
}
