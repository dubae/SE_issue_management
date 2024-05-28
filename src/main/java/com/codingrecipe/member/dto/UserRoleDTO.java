package com.codingrecipe.member.dto;

import com.codingrecipe.member.entity.UserRoleEntity;
import lombok.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRoleDTO {
    private Long id;
    private Long projectid;
    private String userid;
    private String role;

    public static UserRoleDTO toUserRoleDTO(UserRoleEntity userRoleEntity) {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setProjectid(userRoleEntity.getProject().getProjectid());
        userRoleDTO.setUserid(userRoleEntity.getMember().getUserid());
        userRoleDTO.setRole(userRoleEntity.getRole());
        return userRoleDTO;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleDTO that = (UserRoleDTO) o;
        return Objects.equals(projectid, that.projectid) &&
                Objects.equals(userid, that.userid) &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectid, userid, role);
    }

}
