package com.codingrecipe.member.service;

import java.util.ArrayList;
import java.util.List;

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
}
