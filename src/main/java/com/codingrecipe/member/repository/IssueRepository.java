package com.codingrecipe.member.repository;

import com.codingrecipe.member.entity.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<IssueEntity,Long> {
    List<IssueEntity> findAll(); //모두 불러오기
//     Optional<IssueEntity> findByTitle(String title); //이름으로 찾기
//     IssueEntity save(IssueEntity issueEntity); //새 이슈 저장하기
//     void deleteById(Long id); //이슈 삭제하기
}
