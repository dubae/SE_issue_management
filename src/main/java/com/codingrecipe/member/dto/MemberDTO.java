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
    @JsonIgnore
    public Long num;
    public String userid;
    public String username;
    public String email;
    @JsonIgnore
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDTO that = (MemberDTO) o;
        return Objects.equals(num, that.num) &&
               Objects.equals(userid, that.userid) &&
               Objects.equals(username, that.username) &&
               Objects.equals(email, that.email) &&
               Objects.equals(password, that.password);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(num, userid, username, email, password);
    }
}
