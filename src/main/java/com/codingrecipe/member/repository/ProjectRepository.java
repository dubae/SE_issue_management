package com.codingrecipe.member.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.codingrecipe.member.entity.ProjectEntity;
import java.util.List;

@Repository
public interface ProjectRepository  extends JpaRepository<ProjectEntity, Long> {

    ProjectEntity save(ProjectEntity projectEntity);
    Optional<ProjectEntity> findByProjectname(String projectname);
    Optional<ProjectEntity> findByProjectid(Long projectid);
    List<ProjectEntity> findAll();
    void deleteByProjectname(String projectname);
}
