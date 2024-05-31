package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.entity.IssueCommentEntity;
import com.codingrecipe.member.entity.IssueEntity;
import com.codingrecipe.member.entity.ProjectEntity;
import com.codingrecipe.member.repository.IssueRepository;
import com.codingrecipe.member.repository.ProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private IssueService issueService;

    private ProjectEntity projectEntity1;
    List<IssueEntity> issueEntityList;
    List<IssueCommentEntity> issueCommentEntityList = new ArrayList<>();
    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        projectEntity1 = new ProjectEntity();
        double random= Math.random();
        projectEntity1.setProjectid(1L);
        projectEntity1.setProjectname(String.valueOf(random));
        projectEntity1.setProjectdescription("test2L");
        projectEntity1.setProjectstatus("Not Started");
        projectEntity1.setProjectcreatedtime("test2L");




        issueEntityList =new ArrayList<>();
        IssueEntity issueEntity1 = IssueEntity.builder()
                .id(1L).title("title1").writerId(1L).description("test")
                .status("new").projectEntity(projectEntity1)
                .comments(issueCommentEntityList).build();

        IssueEntity issueEntity2 = IssueEntity.builder()
                .id(2L).title("title2").writerId(1L).description("test")
                .status("new").projectEntity(projectEntity1)
                .comments(issueCommentEntityList).build();

        issueEntityList.add(issueEntity1);
        issueEntityList.add(issueEntity2);

        projectEntity1.setIssueEntityList(issueEntityList);
    }

    @AfterEach
    public void tearDown() {
        issueEntityList.clear();
        projectEntity1=null;
    }

    @Test
    void testFindAllIssue() {
        when(issueRepository.findAll()).thenReturn(issueEntityList);

        List<IssueDTO> expectedIssueDTOList = new ArrayList<>();

        //issueEntity->dto
        for(IssueEntity issueEntity : issueRepository.findAll()){
            expectedIssueDTOList.add(new IssueDTO(issueEntity));
//            System.out.println(issueEntity.getId());

        }
//        System.out.println(issueDTOList.toString());

        //테스트 할 메소드.
        List<IssueDTO> issueDTOList = issueService.findAllIssue();

        //실제 값과 비교.
        Assertions.assertEquals(expectedIssueDTOList, issueDTOList);

    }

    @Test
    void testFindByTitle() {
        when(issueRepository.findAll()).thenReturn(issueEntityList);

        List<IssueDTO> issueDTOs = issueService.findByTitle("title1");
        assertEquals("title1", issueDTOs.get(0).getTitle());
    }

    @Test
    void testAddNewIssue() {
        //새 이슈 객체 리스트에 추가.
        IssueEntity issueEntity3 = IssueEntity.builder()
                .id(3L).title("title3").writerId(1L).description("test")
                .status("new").projectEntity(projectEntity1)
                .comments(issueCommentEntityList).build();
        issueEntityList.add(issueEntity3);

        when(issueRepository.findAll()).thenReturn(issueEntityList);


        issueService.addNewIssue(new IssueDTO(issueEntity3));
        assertEquals(issueEntity3.getId(), issueService.findByTitle("title3").get(0).getId());
        //verify(issueRepository, times(1)).save(any(IssueEntity.class));
    }

    @Test
    void testDeleteIssue() {
        IssueDTO issueDTO = new IssueDTO();
        IssueEntity issueEntity = new IssueEntity();
        when(issueRepository.save(any(IssueEntity.class))).thenReturn(issueEntity);

        issueService.deleteIssue(issueDTO);
        verify(issueRepository, times(1)).delete(any(IssueEntity.class));
    }

    @Test
    void testFindByProjectId() {
        when(issueRepository.findAll()).thenReturn(issueEntityList);

        List<IssueDTO> issueDTOs = issueService.findByProjectId(1L);
        assertEquals(1L, issueDTOs.get(0).getProjectId());
    }

    @Test
    void testFindById() {
        when(issueRepository.findById(1L)).thenReturn(Optional.ofNullable(issueEntityList.get(0)));

        IssueDTO issueDTO = issueService.findById(1L);
        assertEquals(1L, issueDTO.getId());
    }

    @Test
    void testChangeStatus() {
        when(issueRepository.findById(1L)).thenReturn(Optional.ofNullable(issueEntityList.get(0)));

        issueService.changeStatus(1L, "new status");
        assertEquals("new status", issueService.findById(1L).getStatus());

    }

    @Test
    void testChangeDevId() {
        IssueEntity issueEntity = new IssueEntity();
        when(issueRepository.findById(anyLong())).thenReturn(Optional.of(issueEntity));

        issueService.changeDevId(1L, 2L);
        assertEquals(2L, issueEntity.getDevId());
        verify(issueRepository, times(1)).save(issueEntity);
    }

    @Test
    void testFindByStatus() {
        List<IssueEntity> issueEntities = new ArrayList<>();
        IssueEntity issueEntity = new IssueEntity();
        issueEntity.setStatus("open");
        issueEntities.add(issueEntity);
        when(issueRepository.findAll()).thenReturn(issueEntities);

        List<IssueDTO> issueDTOs = issueService.findByStatus("open");
        assertEquals(1, issueDTOs.size());
    }

    @Test
    void testFindByComponent() {
        List<IssueEntity> issueEntities = new ArrayList<>();
        IssueEntity issueEntity = new IssueEntity();
        issueEntity.setComponent("backend");
        issueEntities.add(issueEntity);
        when(issueRepository.findAll()).thenReturn(issueEntities);

        List<IssueDTO> issueDTOs = issueService.findByComponent("backend");
        assertEquals(1, issueDTOs.size());
    }

    @Test
    void testFindByWriterId() {
        List<IssueEntity> issueEntities = new ArrayList<>();
        IssueEntity issueEntity = new IssueEntity();
        issueEntity.setWriterId(1L);
        issueEntities.add(issueEntity);
        when(issueRepository.findAll()).thenReturn(issueEntities);

        List<IssueDTO> issueDTOs = issueService.findByWriterId(1L);
        assertEquals(1, issueDTOs.size());
    }

    @Test
    void testFindIssuesByDate() {
        List<IssueEntity> issueEntities = new ArrayList<>();
        IssueEntity issueEntity = new IssueEntity();
        issueEntity.setCreatedAt(LocalDate.now());
        issueEntities.add(issueEntity);
        when(issueRepository.findAll()).thenReturn(issueEntities);

        List<IssueDTO> issueDTOs = issueService.findIssuesByDate(LocalDate.now());
        assertEquals(1, issueDTOs.size());
    }

    @Test
    void testCountIssuesByDate() {
        List<IssueEntity> issueEntities = new ArrayList<>();
        IssueEntity issueEntity = new IssueEntity();
        issueEntity.setCreatedAt(LocalDate.now());
        issueEntities.add(issueEntity);
        when(issueRepository.findAll()).thenReturn(issueEntities);

        int count = issueService.countIssuesByDate(LocalDate.now());
        assertEquals(1, count);
    }

    @Test
    void testSuggestDev() {
        List<IssueDTO> closedIssues = new ArrayList<>();
        IssueDTO issueDTO1 = new IssueDTO();
        issueDTO1.setComponent("backend");
        issueDTO1.setDevId(1L);
        closedIssues.add(issueDTO1);

        when(issueService.findByStatus("closed")).thenReturn(closedIssues);

        IssueDTO newIssue = new IssueDTO();
        newIssue.setComponent("backend");

        Long suggestedDev = issueService.suggestDev(newIssue);
        assertEquals(1L, suggestedDev);
    }
}










//package com.codingrecipe.member.service;
//
//import com.codingrecipe.member.dto.IssueDTO;
//import com.codingrecipe.member.entity.IssueCommentEntity;
//import com.codingrecipe.member.entity.IssueEntity;
//import com.codingrecipe.member.entity.ProjectEntity;
//import com.codingrecipe.member.repository.IssueRepository;
//import com.codingrecipe.member.repository.ProjectRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.Mockito.when;
//
//
//@SpringBootTest
//@Transactional
//public class IssueServiceTest{
//
//    @Autowired
//    private IssueRepository issueRepository;
//
//    @Autowired
//    private ProjectRepository projectRepository;
//
//    @Autowired
//    private IssueService issueService=new IssueService(issueRepository,projectRepository);
//
//    @BeforeEach
//    public void setUp(){
//
//         ProjectEntity projectEntity1 = new ProjectEntity();
//         double random= Math.random();
//         projectEntity1.setProjectname(String.valueOf(random));
//         projectEntity1.setProjectdescription("test2L");
//         projectEntity1.setProjectstatus("Not Started");
//         projectEntity1.setProjectcreatedtime("test2L");
//
//
//         List<IssueCommentEntity> issueCommentEntityList = new ArrayList<>();
//         List<IssueEntity> list = new ArrayList<>();
//         IssueEntity issueEntity1 = IssueEntity.builder()
//         .title("title").writerId(1L).description("test")
//         .status("new").projectEntity(projectEntity1)
//         .comments(issueCommentEntityList).build();
//
//         IssueEntity issueEntity2 = IssueEntity.builder()
//         .title("title").writerId(1L).description("test")
//         .status("new").projectEntity(projectEntity1)
//         .comments(issueCommentEntityList).build();
//
//         list.add(issueEntity1);
//         list.add(issueEntity2);
//
//         projectEntity1.setIssueEntityList(list);
//
//      //  projectRepository.save(projectEntity1);
//         issueRepository.saveAll(list);
//    }
//
//    public List<IssueEntity> makeIssueEntityList(ProjectEntity projectEntity) {
//        List<IssueEntity> list = new ArrayList<>();
//        List<IssueCommentEntity> issueCommentEntityList = new ArrayList<>();
//
//        IssueEntity issueEntity1 = IssueEntity.builder()
//                .title("title").writerId(1L).description("test")
//                .status("new").projectEntity(projectEntity)
//                .comments(issueCommentEntityList).build();
//
//        IssueEntity issueEntity2 = IssueEntity.builder()
//                .title("title").writerId(1L).description("test")
//                .status("new").projectEntity(projectEntity)
//                .comments(issueCommentEntityList).build();
//
//        IssueEntity issueEntity3 = IssueEntity.builder()
//                .title("title").writerId(1L).description("test")
//                .status("new").projectEntity(projectEntity)
//                .comments(issueCommentEntityList).build();
//
//        list.add(issueEntity1);
//        list.add(issueEntity2);
//        list.add(issueEntity3);
//
//        return list;
//    }
//
//    @AfterEach
//    public void tearDown() {
//       // projectRepository.deleteAll();
//
//    }
//
//    @Test
//    public void common(){
//        System.out.println("pj rps size:" + projectRepository.findAll().get(0).getProjectid());
//    }
//
//    @Test
//    public void testFindAllIssue() {
//        /**
//         * findAllIssue()와 실제 DB의 entity를 비교함.
//         */
//        List<IssueDTO> issueDTOList = issueService.findAllIssue();
//        List<IssueDTO> expectedIssueDTOList = new ArrayList<>();
//        for(IssueEntity issueEntity : issueRepository.findAll()){
//            expectedIssueDTOList.add(new IssueDTO(issueEntity));
//            System.out.println(issueEntity.getId());
//
//        }
//        System.out.println(issueDTOList.toString());
//        Assertions.assertEquals(expectedIssueDTOList, issueDTOList);
//    }
//
//    @Test
//    public void testCountByDate() {
//        // Add logic to mock countIssuesByDate method
//    }
//
//    @Test
//    public void testSuggestDev() {
//        // Add logic to mock suggestDev method
//    }
//
//    @Test
//    public void testFindByTitle() {
//        System.out.println(issueService.findByTitle("title").size());
//    }
//}