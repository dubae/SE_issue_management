package com.codingrecipe.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codingrecipe.member.repository.MemberRepository;
import com.codingrecipe.member.entity.MemberEntity;
import com.codingrecipe.member.dto.MemberDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public void register(MemberDTO registerDTO) {
        memberRepository.save(MemberEntity.toMemberEntity(registerDTO));
    }
    public MemberDTO findByUserId(String userid) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUserid(userid);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }
    public boolean isExistEmail(String email) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByEmail(email);
        return optionalMemberEntity.isPresent();
    }

    public boolean isExistId(String userid) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUserid(userid);
        return optionalMemberEntity.isPresent();
    }
}
