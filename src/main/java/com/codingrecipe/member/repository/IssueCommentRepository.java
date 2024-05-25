package com.codingrecipe.member.repository;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.entity.IssueCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueCommentRepository extends JpaRepository<IssueCommentEntity, Long> {
     List<IssueCommentEntity> findAll(); //모두 불러오기
//    Optional<IssueCommentEntity> save(); //댓글 저장하기
//    void deleteById(Long id); //댓글 삭제하기
}
