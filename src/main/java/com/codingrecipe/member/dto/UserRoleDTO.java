package com.codingrecipe.member.dto;
import lombok.*;
import com.codingrecipe.member.entity.UserRoleEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRoleDTO {
    private Long projectid;
    private String userid;
    private String role;

    public static UserRoleDTO toUserRoleDTO(UserRoleEntity userRoleEntity) {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setProjectid(userRoleEntity.getProjectid());
        userRoleDTO.setUserid(userRoleEntity.getUserid());
        userRoleDTO.setRole(userRoleEntity.getRole());
        return userRoleDTO;
    }

}
