package com.codingrecipe.member.entity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import com.codingrecipe.member.dto.UserRoleDTO;


@Entity
@Setter
@Getter
@Table(name = "member_role")
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "projectid")
    private Long projectid;
    @Column(name = "userid")
    private String userid;
    @Column(name = "role")
    private String role;
    
    public static UserRoleEntity toUserRoleEntity(UserRoleDTO userRoleDTO) {
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setProjectid(userRoleDTO.getProjectid());
        userRole.setUserid(userRoleDTO.getUserid());
        userRole.setRole(userRoleDTO.getRole());
        return userRole;
    }
    

}
