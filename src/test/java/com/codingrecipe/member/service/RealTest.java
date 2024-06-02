package com.codingrecipe.member.service;

import com.codingrecipe.member.entity.IssueCommentEntity;
import com.codingrecipe.member.entity.IssueEntity;
import com.codingrecipe.member.entity.ProjectEntity;
import com.codingrecipe.member.repository.IssueRepository;
import com.codingrecipe.member.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class RealTest {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
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
        projectEntity1.setProjectname("test");
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

        issueRepository.saveAll(issueEntityList);
        projectRepository.save(projectEntity1);
    }

    @Test
    public void 이슈추가테스트(){
        System.out.println(issueRepository.count());
    }
}
