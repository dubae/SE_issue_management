package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.IssueCommentDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IssueCommentServiceTest {

    @Autowired
    private IssueCommentService issueCommentService;



    @Test
    public void testAddComment() {
        IssueCommentDTO commentDTO = new IssueCommentDTO(1000L, 1L, "test", 1L);
        issueCommentService.save(commentDTO);
        IssueCommentDTO expected=issueCommentService.findById(1000L);

        Assertions.assertThat(commentDTO).isEqualTo(expected);
    }


}
