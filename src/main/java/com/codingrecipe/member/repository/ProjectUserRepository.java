package com.codingrecipe.member.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.codingrecipe.member.entity.ProjectUserEntity;
import java.util.List;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUserEntity, Long>{
    Optional<ProjectUserEntity> findByProjectidAndUserid(Long projectid, Long userid);
    Optional<List<ProjectUserEntity>> findByProjectid(Long projectid);
    Optional<List<ProjectUserEntity>> findByUserid(Long userid);
    List<ProjectUserEntity> findAll();
    //void deleteByProjectidAndUserid(Long projectid, Long userid);
    //void deleteByProjectid(Long projectid);
    //void deleteByUserid(Long userid);

}
