package com.codingrecipe.member.repository;

import com.codingrecipe.member.entity.IssueEntity;
import com.codingrecipe.member.entity.TEST_IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TEST_IssueRepository extends  JpaRepository<TEST_IssueEntity,Long>{

}
