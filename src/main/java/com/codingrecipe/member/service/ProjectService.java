package com.codingrecipe.member.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.entity.ProjectEntity;
import com.codingrecipe.member.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    public void register(ProjectDTO projectDTO) {
        System.out.println("프로젝트 추가");
        projectDTO.setProjectstatus("Not Started");
        projectRepository.save(ProjectEntity.toProjectEntity(projectDTO));
    }

    public void update_status(ProjectDTO projectDTO, String status) {
        ProjectEntity projectEntity = projectRepository.findByProjectid(projectDTO.getProjectid()).orElse(null);
        if (projectEntity != null) {
            projectEntity.setProjectstatus(status);
            projectRepository.save(projectEntity);
        }
    }
    
    public ProjectDTO findByProjectName(String project_name) {
        ProjectEntity projectEntity = projectRepository.findByProjectname(project_name).orElse(null);
        if (projectEntity != null) {
            return ProjectDTO.toProjectDTO(projectEntity);
        } else {
            return null;
        }
    }

    public ProjectEntity findByProjectNameEntity(String project_name) {
        ProjectEntity projectEntity = projectRepository.findByProjectname(project_name).orElse(null);
        if (projectEntity != null) {
            return projectEntity;
        } else {
            return null;
        }
    }
    public ProjectDTO findByProjectId(Long projectid) {
        ProjectEntity projectEntity = projectRepository.findByProjectid(projectid).orElse(null);
        if (projectEntity != null) {
            return ProjectDTO.toProjectDTO(projectEntity);
        } else {
            return null;
        }
    }
    public boolean isExistProjectName(String projectname) {
        return projectRepository.findByProjectname(projectname).isPresent();
    }
    public List<ProjectDTO> findAll() {
        List<ProjectEntity> projectEntities = projectRepository.findAll();
        List<ProjectDTO> projectDTOs = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntities) {
            projectDTOs.add(ProjectDTO.toProjectDTO(projectEntity));
        }
        return projectDTOs;
    }
    @Transactional
    public boolean deleteByProjectName(String projectname) {
        long deletedCountBefore = projectRepository.count(); // 삭제 작업 전 레코드 수
        projectRepository.deleteByProjectname(projectname);
        long deletedCountAfter = projectRepository.count(); // 삭제 작업 후 레코드 수
        // 삭제 작업 전후 레코드 수가 다르면 삭제가 이루어진 것으로 간주
        if (deletedCountBefore > deletedCountAfter) {
            return true;
        }
        else{
            return false;
        }
    }
}
