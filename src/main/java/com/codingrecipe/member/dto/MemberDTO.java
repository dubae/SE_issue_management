package com.codingrecipe.member.dto;

import com.codingrecipe.member.entity.MemberEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
    public String userid;
    public String username;
    public String email;
    public String password;

    public static MemberDTO toMemberDTO(MemberEntity memberEntity, boolean login) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUserid(memberEntity.getUserid());
        memberDTO.setUsername(memberEntity.getUsername());
        memberDTO.setEmail(memberEntity.getEmail());
        if (login){
            memberDTO.setPassword(memberEntity.getPassword());
        }
        return memberDTO;
    }

    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUserid(memberEntity.getUserid());
        memberDTO.setUsername(memberEntity.getUsername());
        memberDTO.setEmail(memberEntity.getEmail());
        return memberDTO;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDTO that = (MemberDTO) o;
        return Objects.equals(userid, that.userid) &&
               Objects.equals(username, that.username) &&
               Objects.equals(email, that.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userid, username, email);
    }
}
