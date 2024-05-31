package com.codingrecipe.member.dto.comparable;

import com.codingrecipe.member.dto.IssueDTO;
import lombok.*;

@Getter
@Setter
@ToString
public class ComparableIssueDTO extends IssueDTO implements Comparable<ComparableIssueDTO>{


    @Override
    public int compareTo(ComparableIssueDTO o) {
        return getId().compareTo(o.getId());
    }
}
