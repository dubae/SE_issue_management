package com.codingrecipe.member.entity;

import com.codingrecipe.member.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "member")
public class MemberEntity {
    @Id
    @Column(name = "userid", unique = true, nullable = false)
    private String userid;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberentity = new MemberEntity();
        memberentity.setUserid(memberDTO.getUserid());
        memberentity.setUsername(memberDTO.getUsername());
        memberentity.setEmail(memberDTO.getEmail());
        memberentity.setPassword(memberDTO.getPassword());
        return memberentity;
    }
}
