package com.codingrecipe.member.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.dto.UserRoleDTO;
import com.codingrecipe.member.entity.MemberEntity;
import com.codingrecipe.member.entity.ProjectEntity;
import com.codingrecipe.member.entity.UserRoleEntity;
import com.codingrecipe.member.repository.MemberRepository;
import com.codingrecipe.member.repository.ProjectRepository;
import com.codingrecipe.member.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    @PersistenceContext
    private EntityManager entityManager;
    public final UserRoleRepository userRoleRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    @Transactional
    public void add_user_role(UserRoleDTO userRoleDTO, MemberDTO memberDTO, Long projectid) {
        System.out.println("유저 권한 추가: "+ projectid);
        ProjectEntity projectEntity = null;
        try {
            projectEntity = projectRepository.findByProjectid(projectid).orElseGet(() -> null);
            System.out.println("projectEntity: "+projectEntity);
            System.out.println(projectEntity.getProjectdescription());
            projectEntity = projectRepository.save(projectEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(projectEntity == null) return;
        System.out.println("null 아님.");

        // Check if the member already exists
        Optional<MemberEntity> existingMember = memberRepository.findByUserid(memberDTO.getUserid());
        MemberEntity memberEntity;
        if (existingMember.isPresent()) {
            // If the member already exists, use the existing member
            memberEntity = existingMember.get();
        } else {
            // If the member does not exist, create a new member
            memberEntity = MemberEntity.toMemberEntity(memberDTO);
        }

        UserRoleEntity userRoleEntity = UserRoleEntity.toUserRoleEntity(userRoleDTO, memberEntity, projectEntity);
        userRoleRepository.save(userRoleEntity);
    }

    // public void update_user_role(UserRoleDTO userRoleDTO) {
    //     ProjectEntity projectEntity = projectRepository.findByProjectid(userRoleDTO.getProjectid()).orElse(null);
    //     MemberEntity memberEntity = memberRepository.findByUserid(userRoleDTO.getUserid()).orElse(null);
    //     Optional<UserRoleEntity> userRoleEntity = userRoleRepository.findByProjectAndMember(projectEntity, memberEntity);
    //     if (userRoleRepository.findByProjectAndMember(projectEntity, memberEntity).isPresent()){
    //         System.out.println(userRoleEntity.get().toString()+"->"+userRoleDTO.toString()+" 유저 권한 수정");
    //         userRoleRepository.save(UserRoleEntity.toUserRoleEntity(userRoleDTO, memberEntity, projectEntity));
    //     }
    //     else{
    //         System.out.println("해당 유저가 존재하지 않습니다.");
    //     }

    // }
    // 다른 작업하다가 왜 존재하는지 이유를 찾지 못 했음.
    @Transactional
    public List<UserRoleDTO> findByProjectId(Long projectid) {
        ProjectEntity projectEntity = projectRepository.findById(projectid).orElse(null);
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findByProject(projectEntity).orElse(null);
        if (userRoleEntities != null) {
            List<UserRoleDTO> userRoleDTOs = new ArrayList<>();
            for (UserRoleEntity userRoleEntity : userRoleEntities) {
                userRoleDTOs.add(UserRoleDTO.toUserRoleDTO(userRoleEntity));
            }
            return userRoleDTOs;
        } else {
            return null;
        }
    }
    @Transactional
    public List<UserRoleDTO> findByUserId(String userid) {
        MemberEntity memberEntity = memberRepository.findById(userid).orElse(null);
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findByMember(memberEntity).orElse(null);
        if (userRoleEntities != null) {
            List<UserRoleDTO> userRoleDTOs = new ArrayList<>();
            for (UserRoleEntity userRoleEntity : userRoleEntities) {
                userRoleDTOs.add(UserRoleDTO.toUserRoleDTO(userRoleEntity));
            }
            return userRoleDTOs;
        } else {
            return null;
        }
    }
    @Transactional
    public List<UserRoleDTO> findByRole(String role) {
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findByRole(role).orElse(null);
        if (userRoleEntities != null) {
            List<UserRoleDTO> userRoleDTOs = new ArrayList<>();
            for (UserRoleEntity userRoleEntity : userRoleEntities) {
                userRoleDTOs.add(UserRoleDTO.toUserRoleDTO(userRoleEntity));
            }
            return userRoleDTOs;
        } else {
            return null;
        }
    }
    @Transactional
    public ProjectEntity findProject(Long projectid) {
        ProjectEntity projectEntity = projectRepository.findById(projectid).orElse(null);
        if (projectEntity != null) {
            return projectEntity;
        } else {
            return null;
        }
    }
    @Transactional
    public MemberEntity findMember(String userid) {
        MemberEntity memberEntity = memberRepository.findById(userid).orElse(null);
        if (memberEntity != null) {
            return memberEntity;
        } else {
            return null;
        }
    }

    public UserRoleEntity findByProjectAndMemberAndRole(ProjectEntity project, MemberEntity member, String role) {
        return userRoleRepository.findByProjectAndMemberAndRole(project, member, role).orElse(null);
    }

    public UserRoleEntity findByProjectAndRole(ProjectEntity project, String role) {
        return userRoleRepository.findByProjectAndRole(project, role).orElse(null);
    }

    public List<UserRoleDTO> findByProjectAndMember(ProjectEntity project, MemberEntity member) {
        Optional<List<UserRoleEntity>> userRoleEntities = userRoleRepository.findByProjectAndMember(project, member);
        if (userRoleEntities != null) {
            List<UserRoleDTO> userRoleDTOs = new ArrayList<>();
            for (UserRoleEntity userRoleEntity : userRoleEntities.get()) {
                userRoleDTOs.add(UserRoleDTO.toUserRoleDTO(userRoleEntity));
            }
            return userRoleDTOs;
        } else {
            return null;
        }
    }

}
