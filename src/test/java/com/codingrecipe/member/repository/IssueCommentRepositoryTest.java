package com.codingrecipe.member.repository;

import com.codingrecipe.member.entity.IssueCommentEntity;
import com.codingrecipe.member.entity.IssueEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class IssueCommentRepositoryTest {

    @Autowired
    private IssueCommentRepository issueCommentRepository;

    @Test
    public void testFindByIssueId() {

    }

    /**
     * 이슈에 댓글 남기기
     */
    @Test
    public void testSave() {
        List<IssueCommentEntity> issueCommentEntities = new ArrayList<>();
        IssueCommentEntity issueCommentEntity1=new IssueCommentEntity();
        issueCommentEntity1.setId(1L);
        issueCommentEntity1.setWriterId(1L);
        issueCommentEntity1.setContent("test1");
        issueCommentEntities.add(issueCommentEntity1);
        IssueCommentEntity issueCommentEntity2=new IssueCommentEntity();
        issueCommentEntity2.setId(2L);
        issueCommentEntity2.setWriterId(2L);
        issueCommentEntity2.setContent("test2");
        issueCommentEntities.add(issueCommentEntity2);
        IssueEntity issueEntity=new IssueEntity(20L, 1L, 1L, 1L,"test","test","test","test","test","test",issueCommentEntities);
        issueCommentEntity1.setIssueEntity(issueEntity);
        issueCommentEntity2.setIssueEntity(issueEntity);

        issueCommentRepository.save(issueCommentEntity1);
        issueCommentRepository.save(issueCommentEntity2);

    }

    @Test
    public void testDelete() {

    }




}

