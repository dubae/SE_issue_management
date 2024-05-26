package com.codingrecipe.member.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codingrecipe.member.dto.UserRoleDTO;
import com.codingrecipe.member.entity.UserRoleEntity;
import com.codingrecipe.member.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    public final UserRoleRepository userRoleRepository;
    public void add_user_role(UserRoleDTO userRoleDTO) {
        System.out.println("유저 권한 추가");
        userRoleRepository.save(UserRoleEntity.toUserRoleEntity(userRoleDTO));
    }
    public void update_user_role(UserRoleDTO userRoleDTO) {
        Optional<UserRoleEntity> userRoleEntity = userRoleRepository.findByProjectidAndUserid(userRoleDTO.getProjectid(), userRoleDTO.getUserid());
        if (userRoleRepository.findByProjectidAndUserid(userRoleDTO.getProjectid(), userRoleDTO.getUserid()).isPresent()){
            System.out.println(userRoleEntity.get().toString()+"->"+userRoleDTO.toString()+" 유저 권한 수정");
            userRoleRepository.save(UserRoleEntity.toUserRoleEntity(userRoleDTO));
        }
        else{
            System.out.println("해당 유저가 존재하지 않습니다.");
        }
        
    }

    public List<UserRoleDTO> findByProjectId(Long projectid) {
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findByProjectid(projectid).orElse(null);
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

    public List<UserRoleDTO> findByUserId(String userid) {
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findByUserid(userid).orElse(null);
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

    public UserRoleDTO findByProjectidAndUseridAndRole(Long projectid, String userid, String role) {
        UserRoleEntity userRoleEntity = userRoleRepository.findByProjectidAndUseridAndRole(projectid, userid, role).orElse(null);
        if (userRoleEntity != null) {
            return UserRoleDTO.toUserRoleDTO(userRoleEntity);
        } else {
            return null;
        }
    }

}
