package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.entity.IssueEntity;
import com.codingrecipe.member.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /**
     * 프로젝트id로 이슈 반환하기
     */
    public List<IssueDTO> findByProjectId(Long projectId){
        List<IssueDTO> issueDTOList=new ArrayList<>();
        List<IssueEntity> issueEntityList=issueRepository.findAll();
        for(IssueEntity issueEntity:issueEntityList){
            //모든 리스트 중 projectId가 일치하는 이슈만 리스트에 추가함.
            if(issueEntity.getProjectId().equals(projectId)){
                issueDTOList.add(new IssueDTO(issueEntity));
            }
        }
        return issueDTOList;
    }

    /**
     * id로 이슈 찾기
     */
    public IssueDTO findById(Long id){
        Optional<IssueEntity> issueEntity=issueRepository.findById(id);
        IssueDTO issueDTO=new IssueDTO(issueEntity.get());
        return issueDTO;
    }

    /**
     * 이슈의 상태 변경하기. (new->assigned ->..)
     */
    public void changeStatus(Long id,String status){
        Optional<IssueEntity> issueEntity=issueRepository.findById(id);
        issueEntity.get().setStatus(status);
        issueRepository.save(issueEntity.get()); //pk(id)값이 겹치면 그냥 변경하게 됨. 삭제 후 추가 구현 안 해도 됨.
    }

}
