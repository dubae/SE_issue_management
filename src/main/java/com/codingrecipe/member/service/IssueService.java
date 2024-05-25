package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.entity.IssueEntity;
import com.codingrecipe.member.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository){
        this.issueRepository=issueRepository;
    }

    /**
     * 모든 이슈Dto 리스트 반환.
     */
    public List<IssueDTO> findAllIssue(){
        List<IssueDTO> issueDTOList=new ArrayList<>();
        List<IssueEntity> issueEntityList=issueRepository.findAll();
        for(IssueEntity issueEntity:issueEntityList){
            issueDTOList.add(new IssueDTO(issueEntity));
        }
        return issueDTOList;
    }

    /**
     * 제목으로 이슈 찾기. 리스트 반환.
     */
    public List<IssueDTO> findByTitle(String title){

        List<IssueDTO> issueDTOList=new ArrayList<>();
        List<IssueEntity> issueEntityList=issueRepository.findAll();
        for(IssueEntity issueEntity:issueEntityList){
            //모든 리스트 중 title이 일치하는 이슈만 리스트에 추가함.
            if(issueEntity.getTitle().equals(title)){
                issueDTOList.add(new IssueDTO(issueEntity));
            }
        }
        return issueDTOList;
    }

    /**
     * 새 이슈 추가하기
     */
    public void addNewIssue(IssueDTO issueDTO){
        issueRepository.save(IssueEntity.toIssueEntity(issueDTO));
    }

    /**
     * 이슈 삭제하기
     */
    public void deleteIssue(IssueDTO issueDTO){
        issueRepository.delete(IssueEntity.toIssueEntity(issueDTO));
    }

}
