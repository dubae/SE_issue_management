package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.entity.IssueCommentEntity;
import com.codingrecipe.member.entity.IssueEntity;
import com.codingrecipe.member.entity.ProjectEntity;
import com.codingrecipe.member.repository.IssueRepository;
import com.codingrecipe.member.repository.ProjectRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
@SpringJUnitConfig
public class IssueServiceTest {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private IssueService issueService=new IssueService(issueRepository,projectRepository);

    @BeforeEach
    public void setUp(){
        /**
         * 프로젝트: id-2, 3개의 이슈 포함됨.
         * 각 이슈: id(1,2,3), 0개의 댓글.
         */

        tearDown();

        List<IssueEntity> issueEntityList;
        ProjectEntity projectEntity1 = new ProjectEntity();
        projectEntity1.setProjectid(2L);
        projectEntity1.setProjectname("test2L");
        projectEntity1.setProjectdescription("test2L");
        projectEntity1.setProjectstatus("Not Started");
        projectEntity1.setProjectcreatedtime("test2L");

        // Mock the saveAll and save methods to return the input data
//        when(issueRepository.saveAll(anyList())).thenReturn(issueEntityList);
//        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(projectEntity1);

        issueEntityList = makeIssueEntityList(projectEntity1);
        projectEntity1.setIssueEntityList(issueEntityList);

        projectRepository.save(projectEntity1);
        issueRepository.saveAll(issueEntityList);

    }

    public List<IssueEntity> makeIssueEntityList(ProjectEntity projectEntity) {
        List<IssueEntity> list = new ArrayList<>();
        List<IssueCommentEntity> issueCommentEntityList = new ArrayList<>();

        IssueEntity issueEntity1 = IssueEntity.builder()
                .id(1L).title("title1").writerId(1L).description("test1")
                .status("new").projectEntity(projectEntity)
                .comments(issueCommentEntityList).build();

        IssueEntity issueEntity2 = IssueEntity.builder()
                .id(2L).title("title2").writerId(1L).description("test2")
                .status("new").projectEntity(projectEntity)
                .comments(issueCommentEntityList).build();

        IssueEntity issueEntity3 = IssueEntity.builder()
                .id(3L).title("title3").writerId(1L).description("test3")
                .status("new").projectEntity(projectEntity)
                .comments(issueCommentEntityList).build();

        list.add(issueEntity1);
        list.add(issueEntity2);
        list.add(issueEntity3);

        return list;
    }

    @AfterEach
    public void tearDown() {
        projectRepository.deleteAll();
        issueRepository.deleteAll();
    }

    @Test
    public void testFindAllIssue() {
        List<IssueDTO> issues = issueService.findAllIssue();
        System.out.println(issues);
    }

    @Test
    public void testCountByDate() {
        // Add logic to mock countIssuesByDate method
    }

    @Test
    public void testSuggestDev() {
        // Add logic to mock suggestDev method
    }

    @Test
    public void testFindByTitle() {
       System.out.println(issueService.findByTitle("title").size());
    }
}
