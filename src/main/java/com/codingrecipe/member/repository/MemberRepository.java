package com.codingrecipe.member.repository;

import com.codingrecipe.member.entity.MemberEntity;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    Optional<MemberEntity> findByUserid(String userid);
    Optional<MemberEntity> findByEmail(String email);
    void deleteByUserid(String userid);
    List<MemberEntity> findAll();
}