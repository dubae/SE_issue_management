package com.codingrecipe.member.dto;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectInfoDTO {
    public Long projectid;
    public String projectname;
    public String projectdescription;
    public String projectcreatedtime;
    public String status;
    public List<MemberDTOSecure> members;

}
