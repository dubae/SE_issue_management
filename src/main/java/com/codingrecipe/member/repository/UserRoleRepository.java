package com.codingrecipe.member.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codingrecipe.member.entity.MemberEntity;
import com.codingrecipe.member.entity.ProjectEntity;
import com.codingrecipe.member.entity.UserRoleEntity;

import java.util.List;
@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long>{
    Optional<List<UserRoleEntity>> findByProjectAndMember(ProjectEntity project, MemberEntity member);
    Optional<List<UserRoleEntity>> findByProject(ProjectEntity project);
    Optional<List<UserRoleEntity>> findByMember(MemberEntity member);
    Optional<UserRoleEntity> findByProjectAndMemberAndRole(ProjectEntity project, MemberEntity member, String role);
    Optional<List<UserRoleEntity>> findByRole(String role);
    Optional<UserRoleEntity> findByProjectAndRole(ProjectEntity project, String role);
    List<UserRoleEntity> findAll();
    //void deleteByProjectidAndUserid(Long projectid, Long userid);
    //void deleteByProjectid(Long projectid);
    //void deleteByUserid(Long userid);
}
