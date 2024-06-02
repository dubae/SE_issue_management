package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.dto.ProjectDTO;
import com.codingrecipe.member.entity.IssueEntity;
import com.codingrecipe.member.entity.ProjectEntity;
import com.codingrecipe.member.repository.IssueRepository;
import com.codingrecipe.member.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository, ProjectRepository projectRepository) {
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * 모든 이슈Dto 리스트 반환.
     */
    @Transactional
    public List<IssueDTO> findAllIssue() {
        List<IssueDTO> issueDTOList = new ArrayList<>();
        List<IssueEntity> issueEntityList = issueRepository.findAll();
        for (IssueEntity issueEntity : issueEntityList) {
            issueDTOList.add(new IssueDTO(issueEntity));
        }
        return issueDTOList;
    }

    /**
     * 제목으로 이슈 찾기. 리스트 반환.
     */
    public List<IssueDTO> findByTitle(String title) {

        List<IssueDTO> issueDTOList = new ArrayList<>();
        List<IssueEntity> issueEntityList = issueRepository.findAll();
        for (IssueEntity issueEntity : issueEntityList) {
            //모든 리스트 중 title이 일치하는 이슈만 리스트에 추가함.
            if (issueEntity.getTitle().equals(title)) {
                issueDTOList.add(new IssueDTO(issueEntity));
            }
        }
        return issueDTOList;
    }

    /**
     * 새 이슈 추가하기
     */
    public void addNewIssue(IssueDTO issueDTO) {
        System.out.println("issueDTO: "+ issueDTO.toString());
        IssueEntity issueEntity = IssueEntity.toIssueEntity(issueDTO);
        ProjectEntity findProjectEntity = (projectRepository.findByProjectid(issueDTO.getProjectId())).get();
        ProjectDTO projectDto = new ProjectDTO();

        projectDto.setProjectid(findProjectEntity.getProjectid());
        projectDto.setProjectname(findProjectEntity.getProjectname());
        projectDto.setProjectdescription(findProjectEntity.getProjectdescription());
        projectDto.setProjectcreatedtime(findProjectEntity.getProjectcreatedtime());
        projectDto.setProjectstatus(findProjectEntity.getProjectstatus());

        System.out.println("projectDto:" +projectDto.toString());

        System.out.println("issueEntity: "+ issueEntity.toString());
        issueEntity.setProjectEntity(findProjectEntity);
        issueEntity.setCreatedAt(LocalDate.now());
        issueRepository.save(issueEntity);
    }

    /**
     * 이슈 삭제하기
     */
    public void deleteIssue(IssueDTO issueDTO) {
        issueRepository.delete(IssueEntity.toIssueEntity(issueDTO));
    }

    /**
     * 프로젝트id로 이슈 반환하기
     */
    public List<IssueDTO> findByProjectId(Long projectId) {
        List<IssueDTO> issueDTOList = new ArrayList<>();
        List<IssueEntity> issueEntityList = issueRepository.findAll();
        for (IssueEntity issueEntity : issueEntityList) {
            //모든 리스트 중 projectId가 일치하는 이슈만 리스트에 추가함.
            if (issueEntity.getProjectEntity().getProjectid().equals(projectId)) {
                issueDTOList.add(new IssueDTO(issueEntity));
            }
        }
        return issueDTOList;
    }

    /**
     * id로 이슈 찾기
     */
    public IssueDTO findById(Long id) {
        Optional<IssueEntity> issueEntity = issueRepository.findById(id);
        IssueDTO issueDTO = new IssueDTO(issueEntity.get());
        return issueDTO;
    }

    /**
     * 이슈의 상태 변경하기. (new->assigned ->..)
     */
    public void changeStatus(Long id, String status) {
        Optional<IssueEntity> issueEntity = issueRepository.findById(id);
        issueEntity.get().setStatus(status);
        issueRepository.save(issueEntity.get()); //pk(id)값이 겹치면 그냥 변경하게 됨. 삭제 후 추가 구현 안 해도 됨.
    }

    /**
     * 한 이슈의 개발자(devId) 변경하기.
     */
    public void changeDevId(Long id, Long devId) {
        Optional<IssueEntity> issueEntity = issueRepository.findById(id);
        issueEntity.get().setDevId(devId);
        issueRepository.save(issueEntity.get());
    }

    /**
     * status(상태)로 이슈 검색하기
     */
    public List<IssueDTO> findByStatus(String status) {
        List<IssueDTO> issueDTOList = new ArrayList<>();
        List<IssueEntity> issueEntityList = issueRepository.findAll();
        for (IssueEntity issueEntity : issueEntityList) {
            if (issueEntity.getStatus().equals(status)) {
                issueDTOList.add(new IssueDTO(issueEntity));

            }
        }
        return issueDTOList;
    }

    /**
     * compontent(개발 부분)로 이슈 검색하기
     */
    public List<IssueDTO> findByComponent(String component) {
        List<IssueDTO> issueDTOList = new ArrayList<>();
        List<IssueEntity> issueEntityList = issueRepository.findAll();
        for (IssueEntity issueEntity : issueEntityList) {
            if (issueEntity.getComponent().equals(component)) {
                issueDTOList.add(new IssueDTO(issueEntity));

            }
        }
        return issueDTOList;
    }

    /**
     * 글쓴이 id(writerId)로 이슈 검색하기
     */
    public List<IssueDTO> findByWriterId(String writerId) {
        List<IssueDTO> issueDTOList = new ArrayList<>();
        List<IssueEntity> issueEntityList = issueRepository.findAll();
        for (IssueEntity issueEntity : issueEntityList) {
            if (issueEntity.getWriterId().equals(writerId)) {
                issueDTOList.add(new IssueDTO(issueEntity));
            }
        }
        return issueDTOList;
    }

    /**
     *  -----------------------------------이슈 통계 분석 파트 -----------------------------------
     */


    /**
     * 특정 날짜에 발생한 이슈들을 반환
     */
    public List<IssueDTO> findIssuesByDate(LocalDate date) {
        List<IssueDTO> issueDTOList = new ArrayList<>();
        List<IssueEntity> issueEntityList = issueRepository.findAll();
        for (IssueEntity issueEntity : issueEntityList) {
            if (issueEntity.getCreatedAt().equals(date)) {
                issueDTOList.add(new IssueDTO(issueEntity));
            }
        }
        return issueDTOList;
    }

    /**
     * 특정 월에 발생한 이슈들을 반환.
     * Month는 enum객체임을 명심.
     */
    public List<IssueDTO> findIssuesByMonth(Month month) {
        List<IssueDTO> issueDTOList = new ArrayList<>();
        List<IssueEntity> issueEntityList = issueRepository.findAll();
        for (IssueEntity issueEntity : issueEntityList) {
            if (issueEntity.getCreatedAt().getMonth().equals(month)) {
                issueDTOList.add(new IssueDTO(issueEntity));
            }
        }
        return issueDTOList;
    }


    /**
     * 특정 날짜에 발생한 이슈의 개수를 반환.
     */
    public int countIssuesByDate(LocalDate date) {
        int count = 0;
        List<IssueEntity> issueEntityList = issueRepository.findAll();
        for (IssueEntity issueEntity : issueEntityList) {
            if (issueEntity.getCreatedAt().equals(date)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 새로운 이슈 DTO에 대하여 개발자를 추천해줌.
     * 이슈의 아이디를 인자로 넘겨줌.
     * 새 이슈와 같은 component를 가장 많이 해결한 개발자의 id를 반환.
     */
    public List<Long> suggestDev(Long id){
        IssueDTO issueDTO=findById(id);
        String targetComponent=issueDTO.getComponent();
        HashMap<Long, Integer> devMap=new HashMap<>();
        List<IssueDTO> issueDTOList = findByStatus("closed").stream()
                .filter(issueDTO1 -> issueDTO1.getComponent().equals(targetComponent))
                .collect(Collectors.toList());
        int max=0;
        Long bestDev=1L;


        Long fixerId;

        for (IssueDTO issueDTO1 : issueDTOList) {
            fixerId = issueDTO1.getFixerId();
            devMap.get(fixerId);
            devMap.put(fixerId, devMap.getOrDefault(fixerId, 0) + 1);
            if (devMap.get(fixerId) > max) {
                max = devMap.get(fixerId);
                bestDev = fixerId;
            }
        }

        List<Long> keySet = new ArrayList<>(devMap.keySet());

        // Value 값으로 오름차순 정렬
        keySet.sort(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return devMap.get(o2).compareTo(devMap.get(o1));
            }
        });

        if (keySet.isEmpty()) {
            keySet.add(1L);
        }
        return keySet.subList(0, Math.min(3, keySet.size()));




    }

}
