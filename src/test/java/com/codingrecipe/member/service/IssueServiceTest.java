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
import java.time.Month;
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
                .id(1L).title("title1").writerId("1L").description("test")
                .status("new").projectEntity(projectEntity1).component("comp1")
                .comments(issueCommentEntityList).build();

        IssueEntity issueEntity2 = IssueEntity.builder()
                .id(2L).title("title2").writerId("2L").description("test").fixerId("1L")
                .status("closed").projectEntity(projectEntity1).component("comp1")
                .comments(issueCommentEntityList).build();

        IssueEntity issueEntity3 = IssueEntity.builder()
                .id(3L).title("title2").writerId("2L").description("test").fixerId("2L")
                .status("closed").projectEntity(projectEntity1).component("comp1")
                .comments(issueCommentEntityList).build();

        IssueEntity issueEntity4 = IssueEntity.builder()
                .id(4L).title("title2").writerId("2L").description("test").fixerId("2L")
                .status("closed").projectEntity(projectEntity1).component("comp1")
                .comments(issueCommentEntityList).build();

        issueEntity1.setCreatedAt(LocalDate.now());
        issueEntity2.setCreatedAt(LocalDate.now());
        issueEntity3.setCreatedAt(LocalDate.now());
        issueEntity4.setCreatedAt(LocalDate.now());
        issueEntityList.add(issueEntity1);
        issueEntityList.add(issueEntity2);
        issueEntityList.add(issueEntity3);
        issueEntityList.add(issueEntity4);

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
        String title = "title1";
        List<IssueDTO> issueDTOs = issueService.findByTitle(title);
        assertEquals(issueDTOs.size(), issueDTOs.stream().filter(issueDTO -> issueDTO.getTitle().equals(title)).toList().size());
    }

    @Test
    void testAddNewIssue() {
        //새 이슈 객체 리스트에 추가.
        IssueEntity issueEntity3 = IssueEntity.builder()
                .id(3L).title("title3").writerId("1L").description("test")
                .status("new").projectEntity(projectEntity1)
                .comments(issueCommentEntityList).build();
        issueEntityList.add(issueEntity3);

        when(issueRepository.findAll()).thenReturn(issueEntityList);
        when(projectRepository.findByProjectid(1L)).thenReturn(Optional.of(projectEntity1));

        issueService.addNewIssue(new IssueDTO(issueEntity3));
        assertEquals(issueEntity3.getId(), issueService.findByTitle("title3").get(0).getId());
        //verify(issueRepository, times(1)).save(any(IssueEntity.class));
    }

//    @Test
//    void testDeleteIssue() {
//        IssueDTO issueDTO = new IssueDTO();
//        IssueEntity issueEntity = new IssueEntity();
//        when(issueRepository.save(any(IssueEntity.class))).thenReturn(issueEntity);
//
//        issueService.deleteIssue(issueDTO);
//        verify(issueRepository, times(1)).delete(any(IssueEntity.class));
//    }

    @Test
    void testFindByProjectId() {
        when(issueRepository.findAll()).thenReturn(issueEntityList);
        Long projectId = 1L;
        List<IssueDTO> issueDTOs = issueService.findByProjectId(projectId);
        assertEquals(issueDTOs.size(), issueDTOs.stream().filter(issueDTO -> issueDTO.getProjectId().equals(projectId)).toList().size());
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
        when(issueRepository.findById(1L)).thenReturn(Optional.ofNullable(issueEntityList.get(0)));
        String devId = "99L"; Long id=1L;
        issueService.changeDevId(id, devId);
        assertEquals(devId, issueService.findById(id).getDevId());
    }

    @Test
    void testFindByStatus() {
        when(issueRepository.findAll()).thenReturn(issueEntityList);
        String status="assigned";
        List<IssueDTO> issueDTOs = issueService.findByStatus(status);
        assertEquals(issueDTOs.size(), issueDTOs.stream().filter(issueDTO -> issueDTO.getStatus().equals(status)).toList().size());
    }

    @Test
    void testFindByComponent() {
        when(issueRepository.findAll()).thenReturn(issueEntityList);
        String component="comp1";
        List<IssueDTO> issueDTOs = issueService.findByComponent(component);
        assertEquals(issueDTOs.size(), issueDTOs.stream().filter(issueDTO -> issueDTO.getComponent().equals(component)).toList().size());
    }

    @Test
    void testFindByWriterId() {
        when(issueRepository.findAll()).thenReturn(issueEntityList);

        List<IssueDTO> issueDTOs = issueService.findByWriterId("1L");

        assertEquals(issueDTOs.size(), issueDTOs.stream().filter(issueDTO -> issueDTO.getWriterId().equals("1L")).toList().size());
    }

    @Test
    void testFindIssuesByDate() {
        when(issueRepository.findAll()).thenReturn(issueEntityList);
        LocalDate date=LocalDate.now();
        List<IssueDTO> issueDTOs = issueService.findIssuesByDate(date);
        // findIssueByDate() 메소드로 가져온 모든 이슈의 createdAt 값이 찾으려는 값과 같으면 테스트 통과.
        assertEquals(issueDTOs.size(), issueDTOs.stream().filter(issueDTO -> issueDTO.getCreatedAt().equals(date)).toList().size());
    }

    @Test
    void testCountIssuesByDate() {
        when(issueRepository.findAll()).thenReturn(issueEntityList);
        LocalDate date=LocalDate.now();
        int count = issueService.countIssuesByDate(date);
        // findIssueByDate() 메소드로 가져온 모든 이슈의 createdAt 값이 찾으려는 값과 같으면 테스트 통과.
        assertEquals(count, issueEntityList.stream().filter(issueEntity -> issueEntity.getCreatedAt().equals(date)).toList().size());

    }

    @Test
    void testFindIssuesByMonth(){
        when(issueRepository.findAll()).thenReturn(issueEntityList);
        Month month=LocalDate.now().getMonth();
        List<IssueDTO> issueDTOList=issueService.findIssuesByMonth(month);
        // FindIssuesByMonth() 메소드로 가져온 모든 이슈의 createdAt 값이 찾으려는 값과 같으면 테스트 통과.
        assertEquals(issueDTOList.size(), issueDTOList.stream().filter(issueEntity -> issueEntity.getCreatedAt().getMonth().equals(month)).toList().size());

    }

    @Test
    void testSuggestDev(){
        when(issueRepository.findAll()).thenReturn(issueEntityList);
        when(issueRepository.findById(1L)).thenReturn(Optional.ofNullable(issueEntityList.get(0)));

        assertEquals(issueService.suggestDev(1L).get(0),"2L");
        assertEquals(issueService.suggestDev(1L).get(1),"1L");

        System.out.println(issueService.suggestDev(1L));
    }




//    @Test
//    void testSuggestDev() {
//        List<IssueDTO> closedIssues = new ArrayList<>();
//        IssueDTO issueDTO1 = new IssueDTO();
//        issueDTO1.setComponent("backend");
//        issueDTO1.setDevId(1L);
//        closedIssues.add(issueDTO1);
//
//        when(issueService.findByStatus("closed")).thenReturn(closedIssues);
//
//        IssueDTO newIssue = new IssueDTO();
//        newIssue.setComponent("backend");
//
//        Long suggestedDev = issueService.suggestDev(newIssue);
//        assertEquals(1L, suggestedDev);
//    }
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
//         .title("title").writerId("1L").description("test")
//         .status("new").projectEntity(projectEntity1)
//         .comments(issueCommentEntityList).build();
//
//         IssueEntity issueEntity2 = IssueEntity.builder()
//         .title("title").writerId("1L").description("test")
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
//                .title("title").writerId("1L").description("test")
//                .status("new").projectEntity(projectEntity)
//                .comments(issueCommentEntityList).build();
//
//        IssueEntity issueEntity2 = IssueEntity.builder()
//                .title("title").writerId("1L").description("test")
//                .status("new").projectEntity(projectEntity)
//                .comments(issueCommentEntityList).build();
//
//        IssueEntity issueEntity3 = IssueEntity.builder()
//                .title("title").writerId("1L").description("test")
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