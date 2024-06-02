package com.codingrecipe.member.repository;

import com.codingrecipe.member.entity.IssueCommentEntity;
import com.codingrecipe.member.entity.IssueEntity;
import com.codingrecipe.member.entity.ProjectEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class IssueRepositoryTest {

    @Autowired
    private IssueRepository issueRepository;

    @AfterEach
    public void tearDown() {
        //issueRepository.deleteAll();

    }

    @Test
    public void findAll() {

    }

    @Test
    public void testSave() {
        ProjectEntity projectEntity1 = new ProjectEntity();
        double random= Math.random();
        projectEntity1.setProjectname(String.valueOf(random));
        projectEntity1.setProjectdescription("test2L");
        projectEntity1.setProjectstatus("Not Started");
        projectEntity1.setProjectcreatedtime("test2L");


        List<IssueCommentEntity> issueCommentEntityList = new ArrayList<>();
        List<IssueEntity> list = new ArrayList<>();
        IssueEntity issueEntity1 = IssueEntity.builder()
                .title("title").writerId("1L").description("test")
                .status("new").projectEntity(projectEntity1)
                .comments(issueCommentEntityList).build();

        IssueEntity issueEntity2 = IssueEntity.builder()
                .title("title").writerId("1L").description("test")
                .status("new").projectEntity(projectEntity1)
                .comments(issueCommentEntityList).build();

        list.add(issueEntity1);
        list.add(issueEntity2);

        projectEntity1.setIssueEntityList(list);

        issueRepository.saveAll(list);
    }

}
