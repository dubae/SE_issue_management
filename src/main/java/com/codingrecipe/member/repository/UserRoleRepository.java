package com.codingrecipe.member.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.codingrecipe.member.entity.UserRoleEntity;

import java.util.List;
@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long>{
    Optional<UserRoleEntity> findByProjectidAndUserid(Long projectid, String userid);
    Optional<List<UserRoleEntity>> findByProjectid(Long projectid);
    Optional<List<UserRoleEntity>> findByUserid(String userid);
    Optional<UserRoleEntity> findByProjectidAndUseridAndRole(Long projectid, String userid, String role);
    List<UserRoleEntity> findAll();
    //void deleteByProjectidAndUserid(Long projectid, Long userid);
    //void deleteByProjectid(Long projectid);
    //void deleteByUserid(Long userid);
}
