package com.codingrecipe.member.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import com.codingrecipe.member.repository.MemberRepository;
import com.codingrecipe.member.entity.MemberEntity;
import com.codingrecipe.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    @Transactional
    public void register(MemberDTO registerDTO) {
        memberRepository.save(MemberEntity.toMemberEntity(registerDTO));
    }
    @Transactional
    public MemberDTO findByUserId(String userid) {
        Optional<MemberEntity> memberEntity = memberRepository.findByUserid(userid);
        System.out.println(memberEntity);
        if (memberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(memberEntity.get());
        } else {
            return null;
        }
    }
    @Transactional
    public MemberDTO findByUserId(String userid, boolean isLogin) {
        Optional<MemberEntity> memberEntity = memberRepository.findByUserid(userid);
        System.out.println(memberEntity);
        if (memberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(memberEntity.get(), isLogin);
        } else {
            return null;
        }
    }
    @Transactional
    public boolean isExistEmail(String email) {
        Optional<MemberEntity> MemberEntity = memberRepository.findByEmail(email);
        return MemberEntity.isPresent();
    }
    @Transactional
    public boolean isExistId(String userid) {
        Optional<MemberEntity> memberEntity = memberRepository.findByUserid(userid);
        System.out.println(memberEntity);
        return memberEntity.isPresent();
    }
    @Transactional
    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        List<MemberDTO> memberDTOs = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntities) {
            memberDTOs.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOs;
    }
    @Transactional
    public boolean deleteByUserId(String userId) {
        long deletedCountBefore = memberRepository.count(); // 삭제 작업 전 레코드 수
        memberRepository.deleteByUserid(userId); // 사용자 아이디에 해당하는 레코드 삭제
        long deletedCountAfter = memberRepository.count(); // 삭제 작업 후 레코드 수
    
        // 삭제 작업 전후 레코드 수가 다르면 삭제가 이루어진 것으로 간주
        return deletedCountBefore > deletedCountAfter;
    }
}
