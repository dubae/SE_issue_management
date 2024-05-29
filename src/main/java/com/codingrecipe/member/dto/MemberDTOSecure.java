package com.codingrecipe.member.dto;

import java.util.Objects;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class MemberDTOSecure {
    public String userid;
    public String username;
    public String email;

    public static MemberDTOSecure toMemberDTOSecure(MemberDTO memberDto) {
        MemberDTOSecure memberDTOSecure = new MemberDTOSecure();
        memberDTOSecure.setUserid(memberDto.getUserid());
        memberDTOSecure.setUsername(memberDto.getUsername());
        memberDTOSecure.setEmail(memberDto.getEmail());
        return memberDTOSecure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDTOSecure that = (MemberDTOSecure) o;
        return Objects.equals(userid, that.userid) &&
               Objects.equals(username, that.username) &&
               Objects.equals(email, that.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userid, username, email);
    }
}