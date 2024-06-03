package com.codingrecipe.member.dto;


import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueUpdateDTO {
    private String assignee;
    private String priority;
    private String status;
    //private String fixer;
    //private String Reporter;
}
