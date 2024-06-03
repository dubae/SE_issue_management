package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.dto.UserRoleDTO;
import com.codingrecipe.member.entity.MemberEntity;
import com.codingrecipe.member.entity.ProjectEntity;
import com.codingrecipe.member.entity.UserRoleEntity;
import com.codingrecipe.member.repository.MemberRepository;
import com.codingrecipe.member.repository.ProjectRepository;
import com.codingrecipe.member.repository.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRoleServiceTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserRoleService userRoleService;

    private ProjectEntity projectEntity;
    private MemberEntity memberEntity;
    private UserRoleEntity userRoleEntity;
    private UserRoleDTO userRoleDTO;
    private MemberDTO memberDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        projectEntity = ProjectEntity.builder()
                .projectid(1L)
                .projectname("Test Project")
                .projectcreatedtime("2025-05-31")
                .projectstatus("Not Started")
                .build();

        memberEntity = MemberEntity.builder()
                .userid("testuser")
                .username("Test User")
                .password("password")
                .email("testuser@example.com")
                .build();

        userRoleEntity = UserRoleEntity.builder()
                .id(1L)
                .role("DEV")
                .member(memberEntity)
                .project(projectEntity)
                .build();

        userRoleDTO = UserRoleDTO.builder()
                .id(1L)
                .role("DEV")
                .userid("testuser")
                .projectid(1L)
                .build();

        memberDTO = MemberDTO.builder()
                .userid("testuser")
                .username("Test User")
                .password("password")
                .email("testuser@example.com")
                .build();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(projectEntity));
        when(memberRepository.findByUserid("testuser")).thenReturn(Optional.of(memberEntity));
        when(userRoleRepository.save(any(UserRoleEntity.class))).thenReturn(userRoleEntity);
        when(entityManager.merge(any(ProjectEntity.class))).thenReturn(projectEntity);
    }

    @AfterEach
    void tearDown() {
        reset(userRoleRepository, memberRepository, projectRepository, entityManager);
    }

    @Test
    void testAddUserRole() {
        when(projectRepository.findByProjectid(1L)).thenReturn(Optional.of(projectEntity));

        userRoleService.add_user_role(userRoleDTO, memberDTO, 1L);

        verify(userRoleRepository, times(0)).save(any(UserRoleEntity.class));
    }

    @Test
    void testFindByProjectId() {
        List<UserRoleEntity> userRoleEntities = new ArrayList<>();
        userRoleEntities.add(userRoleEntity);

        when(userRoleRepository.findByProject(projectEntity)).thenReturn(Optional.of(userRoleEntities));

        List<UserRoleDTO> result = userRoleService.findByProjectId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DEV", result.get(0).getRole());
    }

    @Test
    void testFindByUserId() {
        when(memberRepository.findById("testuser")).thenReturn(Optional.ofNullable(memberEntity));
        List<UserRoleEntity> userRoleEntities = new ArrayList<>();
        userRoleEntities.add(userRoleEntity);

        when(userRoleRepository.findByMember(memberEntity)).thenReturn(Optional.of(userRoleEntities));

        List<UserRoleDTO> result = userRoleService.findByUserId("testuser");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DEV", result.get(0).getRole());
    }


    @Test
    void testFindByRole() {
        List<UserRoleEntity> userRoleEntities = new ArrayList<>();
        userRoleEntities.add(userRoleEntity);

        when(userRoleRepository.findByRole("DEV")).thenReturn(Optional.of(userRoleEntities));

        List<UserRoleDTO> result = userRoleService.findByRole("DEV");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DEV", result.get(0).getRole());
    }

    @Test
    void testFindProject() {
        ProjectEntity result = userRoleService.findProject(1L);

        assertNotNull(result);
        assertEquals(1L, result.getProjectid());
    }

    @Test
    void testFindMember() {
        when(memberRepository.findById("testuser")).thenReturn(Optional.ofNullable(memberEntity));

        MemberEntity result = userRoleService.findMember("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUserid());
    }

    @Test
    void testFindByProjectAndMemberAndRole() {
        when(userRoleRepository.findByProjectAndMemberAndRole(projectEntity, memberEntity, "DEV"))
                .thenReturn(Optional.of(userRoleEntity));

        UserRoleEntity result = userRoleService.findByProjectAndMemberAndRole(projectEntity, memberEntity, "DEV");

        assertNotNull(result);
        assertEquals("DEV", result.getRole());
    }

    @Test
    void testFindByProjectAndRole() {
        when(userRoleRepository.findByProjectAndRole(projectEntity, "DEV")).thenReturn(Optional.of(userRoleEntity));

        UserRoleEntity result = userRoleService.findByProjectAndRole(projectEntity, "DEV");

        assertNotNull(result);
        assertEquals("DEV", result.getRole());
    }
}
