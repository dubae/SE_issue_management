package com.codingrecipe.member.dto;
import java.util.List;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddProjectDTO {
    private ProjectDTO projectDTO;
    private List<String> pl;
    private List<String> dev;
    private List<String> tester;
    private List<String> pm;
}