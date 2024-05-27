package com.codingrecipe.member.service;

import java.util.ArrayList;
import java.util.List;
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
        Optional<MemberEntity> memberEntity = memberRepository.findByUserid(userid);
        System.out.println(memberEntity);
        if (memberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(memberEntity.get());
        } else {
            return null;
        }
    }
    public boolean isExistEmail(String email) {
        Optional<MemberEntity> MemberEntity = memberRepository.findByEmail(email);
        return MemberEntity.isPresent();
    }

    public boolean isExistId(String userid) {
        Optional<MemberEntity> memberEntity = memberRepository.findByUserid(userid);
        System.out.println(memberEntity);
        return memberEntity.isPresent();
    }
    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        List<MemberDTO> memberDTOs = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntities) {
            memberDTOs.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOs;
    }
}
