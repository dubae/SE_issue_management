package com.codingrecipe.member.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import com.codingrecipe.member.dto.ProjectUserDTO;
import com.codingrecipe.member.entity.ProjectUserEntity;
import com.codingrecipe.member.repository.ProjectUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectUserService {
    private final ProjectUserRepository projectUserRepository;
    public void register(ProjectUserDTO projectUserDTO) {
        System.out.println("프로젝트에 유저 추가");
        projectUserRepository.save(ProjectUserEntity.toProjectUserEntity(projectUserDTO));
    }
    public List<ProjectUserDTO> findByProjectId(Long projectid) {
        List<ProjectUserEntity> projectUserEntitys = projectUserRepository.findByProjectid(projectid).orElse(null);
        if (projectUserEntitys != null) {
            List<ProjectUserDTO> projectUserDTOs = new ArrayList<>();
            for (ProjectUserEntity projectUserEntity : projectUserEntitys) {
                projectUserDTOs.add(ProjectUserDTO.toProjectUserDTO(projectUserEntity));
            }
            return projectUserDTOs;
        } else {
            return null;
        }
    }

    public List<ProjectUserDTO> findByUserId(Long userid) {
        List<ProjectUserEntity> projectUserEntitys = projectUserRepository.findByUserid(userid).orElse(null);
        if (projectUserEntitys != null) {
            List<ProjectUserDTO> projectUserDTOs = new ArrayList<>();
            for (ProjectUserEntity projectUserEntity : projectUserEntitys) {
                projectUserDTOs.add(ProjectUserDTO.toProjectUserDTO(projectUserEntity));
            }
            return projectUserDTOs;
        } else {
            return null;
        }
    }
    
}
