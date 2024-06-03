package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.IssueCommentDTO;
import com.codingrecipe.member.entity.IssueCommentEntity;
import com.codingrecipe.member.repository.IssueCommentRepository;
import com.codingrecipe.member.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IssueCommentService {

    private final IssueRepository issueRepository;
    IssueCommentRepository issueCommentRepository;

    @Autowired //의존성 주입
    public IssueCommentService(IssueCommentRepository issueCommentRepository, IssueRepository issueRepository) {
        this.issueCommentRepository = issueCommentRepository;
        this.issueRepository = issueRepository;
    }


    /**
     * 특정 이슈id에 해당하는 댓글만 반환.
     */
    public List<IssueCommentDTO> findByIssueId(Long issueId) {
        List<IssueCommentDTO> issueCommentDTOS = new ArrayList<>();
        List<IssueCommentEntity> issueCommentEntities= issueCommentRepository.findAll();
        for(IssueCommentEntity issueCommentEntity : issueCommentEntities){
            //모든 이슈 코멘트 중 이슈 아이디 값이 일치하는 것만 반환.
            //특정 이슈에 대한 댓글만 반환됨.
            // .getIssueEntity().getId() 이 부분 다시 생각. 아이디 필드.
            if(issueCommentEntity.getIssueEntity().getId().equals(issueId)){
                issueCommentDTOS.add(new IssueCommentDTO(issueCommentEntity));
            }
        }
        return issueCommentDTOS;
    }

    /**
     * 특정 이슈에 댓글 추가
     */
    public void save(IssueCommentDTO issueCommentDTO) {
        IssueCommentEntity issueCommentEntity = IssueCommentEntity.toIssueCommentEntity(issueCommentDTO);
        issueCommentEntity.setIssueEntity(issueRepository.findById(issueCommentDTO.getIssueId()).get());
        issueCommentRepository.save(issueCommentEntity);

    }

    /**
     * comment id로 댓글 찾기.
     */
    public IssueCommentDTO findById(Long id) {
        return new IssueCommentDTO(issueCommentRepository.findById(id).get());

    }

    /**
     * 테스트용) 모든 댓글 반환
     */
    public List<IssueCommentDTO> findAll(){
        List<IssueCommentDTO> issueCommentDTOS = new ArrayList<>();
        List<IssueCommentEntity> issueCommentEntities= issueCommentRepository.findAll();
        for(IssueCommentEntity issueCommentEntity : issueCommentEntities){
            IssueCommentDTO issueCommentDTO=new IssueCommentDTO(issueCommentEntity);
            issueCommentDTOS.add(issueCommentDTO);
            //System.out.println(issueCommentDTO.getIssueId()); //여기서 문제 발생. issueId 제대로 객체 입력 안됨.
            //System.out.println(issueCommentEntity.getIssueEntity().getId());
            //잘 들어가긴 하는 듯?
        }
        return issueCommentDTOS;
    }


}

