package com.codingrecipe.member.dto;

import com.codingrecipe.member.entity.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
    public Long num;
    public String userid;
    public String username;
    public String email;
    public String password;

    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setNum(memberEntity.getNum());
        memberDTO.setUserid(memberEntity.getUserid());
        memberDTO.setUsername(memberEntity.getUsername());
        memberDTO.setEmail(memberEntity.getEmail());
        memberDTO.setPassword(memberEntity.getPassword());
        return memberDTO;
    }
}
