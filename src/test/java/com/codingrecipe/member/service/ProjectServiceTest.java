package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.entity.ProjectEntity;
import com.codingrecipe.member.repository.ProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private List<ProjectEntity> projectEntityList;
    private ProjectEntity projectEntity1, projectEntity2;
    private ProjectDTO projectDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        projectEntityList = new ArrayList<>();
        projectEntity1 = ProjectEntity.builder()
                .projectid(1L)
                .projectname("name1")
                .projectcreatedtime("2025-05-31")
                .projectstatus("Not Started")
                .build();

        projectEntity2 = ProjectEntity.builder()
                .projectid(2L)
                .projectname("name2")
                .projectcreatedtime("2025-05-31")
                .projectstatus("Not Started")
                .build();

        projectEntityList.add(projectEntity1);
        projectEntityList.add(projectEntity2);

        when(projectRepository.findByProjectid(1L)).thenReturn(Optional.of(projectEntity1));
        when(projectRepository.findByProjectname("name1")).thenReturn(Optional.of(projectEntity1));
        when(projectRepository.findByProjectname("name2")).thenReturn(Optional.of(projectEntity2));
        when(projectRepository.findAll()).thenReturn(projectEntityList);
    }

    @AfterEach
    void tearDown() {
        reset(projectRepository);
    }

    @Test
    void testRegister() {
        projectDTO = new ProjectDTO();
        projectDTO.setProjectname("Test Project");
        projectDTO.setProjectstatus("Not Started");

        projectService.register(projectDTO);
        verify(projectRepository, times(1)).save(any(ProjectEntity.class));
    }

    @Test
    void testUpdateStatus() {
        projectDTO = new ProjectDTO();
        projectDTO.setProjectid(1L);

        projectService.update_status(projectDTO, "In Progress");
        assertEquals("In Progress", projectEntity1.getProjectstatus());
        verify(projectRepository, times(1)).save(projectEntity1);
    }

    @Test
    void testFindByProjectName() {
        ProjectDTO foundProjectDTO = projectService.findByProjectName("name1");
        assertNotNull(foundProjectDTO);
        assertEquals("name1", foundProjectDTO.getProjectname());
    }

    @Test
    void testFindByProjectNameEntity() {
        ProjectEntity foundProjectEntity = projectService.findByProjectNameEntity("name1");
        assertNotNull(foundProjectEntity);
        assertEquals("name1", foundProjectEntity.getProjectname());
    }

    @Test
    void testFindByProjectId() {
        ProjectDTO foundProjectDTO = projectService.findByProjectId(1L);
        assertNotNull(foundProjectDTO);
        assertEquals(1L, foundProjectDTO.getProjectid());
    }

    @Test
    void testIsExistProjectName() {
        boolean exists = projectService.isExistProjectName("name1");
        assertTrue(exists);

        boolean notExists = projectService.isExistProjectName("nonexistent");
        assertFalse(notExists);
    }

    @Test
    void testFindAll() {
        List<ProjectDTO> projectDTOs = projectService.findAll();
        assertEquals(2, projectDTOs.size());
        assertEquals("name1", projectDTOs.get(0).getProjectname());
        assertEquals("name2", projectDTOs.get(1).getProjectname());
    }

    @Test
    void testDeleteByProjectName() {
        when(projectRepository.count()).thenReturn(2L, 1L); // Before delete and after delete counts

        boolean isDeleted = projectService.deleteByProjectName("name1");
        verify(projectRepository, times(1)).deleteByProjectname("name1");
        assertTrue(isDeleted);
    }
}
