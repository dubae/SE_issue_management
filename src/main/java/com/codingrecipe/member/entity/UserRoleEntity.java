package com.codingrecipe.member.entity;
import lombok.*;


import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;

import com.codingrecipe.member.dto.UserRoleDTO;


@Entity
@Getter
@Setter
@Table(name = "member_role")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 많은 UserRoleEntity가 하나의 MemberEntity를 참조할 수 있음
    @JoinColumn(name = "userid") // user_id 컬럼을 FK로 지정
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY) // 많은 UserRoleEntity가 하나의 ProjectEntity를 참조할 수 있음
    @JoinColumn(name = "projectid") // project_id 컬럼을 FK로 지정
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectEntity project;

    @Column(nullable = false) // null을 허용하지 않음
    private String role;

    public static UserRoleEntity toUserRoleEntity(UserRoleDTO userRoleDTO, MemberEntity memberEntity, ProjectEntity projectEntity) {
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setMember(memberEntity); // MemberEntity를 설정
        userRoleEntity.setProject(projectEntity); // ProjectEntity를 설정
        userRoleEntity.setRole(userRoleDTO.getRole()); // Role을 설정
        return userRoleEntity;
    }

}
