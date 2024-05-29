package com.codingrecipe.member.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class issueServiceTest {

    @Autowired
    private IssueService issueService;

    @Test
    public void testCountByDate(){
       int count=issueService.countIssuesByDate( LocalDate.of(2024,05,27));
       Assertions.assertThat(count).isEqualTo(3);
    }
}
