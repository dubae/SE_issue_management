package com.codingrecipe.member.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.codingrecipe.member.dto.UserRoleDTO;
import com.codingrecipe.member.service.UserRoleService;


@Entity
@Getter
@Setter
@Table(name = "member_role")
@NoArgsConstructor
public class UserRoleEntity {
    @Transient
    @Autowired
    private UserRoleService userRoleService; // 생성자 주입

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}) // 많은 UserRoleEntity가 하나의 MemberEntity를 참조할 수 있음
    @JoinColumn(name = "userid") // user_id 컬럼을 FK로 지정
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}) // 많은 UserRoleEntity가 하나의 ProjectEntity를 참조할 수 있음
    @JoinColumn(name = "projectid") // project_id 컬럼을 FK로 지정
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
