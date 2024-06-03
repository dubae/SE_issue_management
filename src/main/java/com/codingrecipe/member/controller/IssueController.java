package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.dto.IssueUpdateDTO;
import com.codingrecipe.member.service.IssueService;
import com.codingrecipe.member.session.SessionManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@RestController
@Transactional
public class IssueController {

    private final IssueService issueService;

    @Autowired
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    /**
     프로젝트 별 이슈 리스트 반환(Json-IssueDto)
     {
     id:1,
     writerId:1,
     projectId:1,
     devId:1,
     fixerId:1,
     title:"test",
     status:"new",
     component:"test",
     priority: "test",
     significance: "test",
     description:"test"
     }
     */
    @GetMapping("/api/issues/{projectId}")
    public ResponseEntity<List<IssueDTO>> issue(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<IssueDTO> issues = issueService.findByProjectId(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(issues));
    }

    /**
     * 프로젝트 별 이슈 추가(Json-IssueDto 형태)
     * {
     *         id:1,
     *         writerId:1,
     *         projectId:1,
     *         devId:1,
     *         fixerId:1,
     *         title:"test",
     *         status:"new",
     *         component:"test",
     *         priority: "test",
     *         significance: "test",
     *         description:"test"
     *      }
     */
    @PostMapping("/api/project/{projectId}/issue/new")
    public ResponseEntity<Void> addIssue(@RequestBody IssueDTO issueDTO, Model model, HttpServletRequest request) {
        System.out.println("hihihi");
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // issueDTO.setWriterId(/** 여기에 입력할 것! */);
        issueService.addNewIssue(issueDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 하나의 이슈 자세히 보기.
     * IssueDto객체를 Json형태로 반환함.
     {
     id:1,
     writerId:1,
     projectId:1,
     devId:1,
     fixerId:1,
     title:"test",
     status:"new",
     component:"test",
     priority: "test",
     significance: "test",
     description:"test"
     }
     */
    @GetMapping("/api/project/{projectId}/issue/{issueId}")
    public ResponseEntity<IssueDTO> issue_info(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueID, Model model, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        IssueDTO issue = issueService.findById(issueID);
        return ResponseEntity.status(HttpStatus.OK).body(issue);
    }

    /**
     * 이름으로 이슈 찾기. issueDto의 리스트로 반환되며, 댓글 역시 같이 반환됨. fetch속도 ?
     [
     {
     id:1,
     writerId:1,
     projectId:1,
     devId:1,
     fixerId:1,
     title:"test",
     status:"new",
     component:"test",
     priority: "test",
     significance: "test",
     description:"test"
     }
     ]
     */
    @GetMapping("/api/project/{projectId}/issue/findByTitle")
    public ResponseEntity<List<IssueDTO>> findByTitle(@PathVariable("projectId") Long projectId, @RequestParam String title, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<IssueDTO> issues = issueService.findByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(issues));
    }

    /**
     * 이슈의 상태 변경하기(new->assigned ...)
     * * 유저의 자격 확인해야 하는지는 나중에 논의.
     /project/{projectId}/issue/{issueId}/status?status=new
     */
    @PostMapping("/api/project/{projectId}/issue/{issueId}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId, @RequestParam String status, Model model, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        issueService.changeStatus(issueId, status);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 이슈의 개발자 지정(변경)하기. 개발자 id를 req.param으로 넣기.
     * * 유저의 자격 확인(PL)해야 하는지는 나중에 논의. // 현재 이 부분은 안쓰임.
     /project/{projectId}/issue/{issueId}/devId?devId=11
     */
    @PostMapping("/api/project/{projectId}/issue/{issueId}/devId")
    public ResponseEntity<Void> changeDevId(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId, @RequestParam String devId, Model model, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        issueService.changeDevId(issueId, devId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 이슈를 상태로 검색하기(status)
     * @RequestParam 으로 status 넘겨주세요.
    /project/{projectId}/issue/findByStatus?status=new
    return [ {IssueDto} ... ]
     */
    @GetMapping("/api/project/{projectId}/issue/findByStatus")
    public ResponseEntity<List<IssueDTO>> findByStatus(@PathVariable("projectId") Long projectId, @RequestParam String status, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<IssueDTO> issues = issueService.findByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(issues));
    }

    /**
     * 글쓴이 id(writerId)로 이슈 검색하기
     * @RequestParam으로 writerId 넘겨주세요.
    /project/{projectId}/issue/findByWriterId?writerId=1
    return [ {issuedto}... ]
     */
    @GetMapping("/api/project/{projectId}/issue/findByWriterId")
    public ResponseEntity<List<IssueDTO>> findByWriterId(@PathVariable("projectId") Long projectId, @RequestParam String writerId, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<IssueDTO> issues = issueService.findByWriterId(writerId);
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(issues));
    }

    /** ----------------------이슈 통계 관련 API ------------------------------**/

    /**
     * 특정 월에 발생한 이슈들을 반환.
     /project/1/issue/month/5
     return [ {issuedto json} .. ]
     */
    @GetMapping("/api/project/{projectId}/issue/month/{month}")
    public ResponseEntity<List<IssueDTO>> findByMonth(@PathVariable("projectId") Long projectId, @PathVariable("month") int month, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<IssueDTO> issues = issueService.findIssuesByMonth(Month.of(month));
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(issues));
    }


    /**
     * 특정 날짜에 발생한 이슈들을 반환. List<issueDto>
     *      @RequestParam으로 year, month, day  넘겨주세요.
     *      /project/{projectId}/issue/findByDate?year=2024&month=5&day=27
     *      return [ {issuedto json} .. ]
     */
    @GetMapping("/api/project/{projectId}/issue/findByDate")
    public ResponseEntity<List<IssueDTO>> findByDate(@PathVariable("projectId") Long projectId, @RequestParam int year, @RequestParam int month, @RequestParam int day, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<IssueDTO> issues = issueService.findIssuesByDate(LocalDate.of(year, month, day));
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(issues));
    }

    /**
     * 특정 날짜에 발생한 이슈의 개수를 반환.
     * @RequestParam으로 year, month, day  넘겨주세요.
    /project/{projectId}/issue/countIssueByDate
     * return 으로 json이 아닌 그냥 int형으로 반환됨.
     */
    @GetMapping("/api/project/{projectId}/issue/countIssueByDate")
    public ResponseEntity<Integer> countIssueByDate(@PathVariable("projectId") Long projectId, @RequestParam int year, @RequestParam int month, @RequestParam int day, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        int count = issueService.countIssuesByDate(LocalDate.of(year, month, day));
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    /**
     *  특정 이슈에 대한 개발자를 추천해줌. (PL이 아직 이슈에 대해 개발자를 배정하지 않은 상황)
     *  추천 개발자는 DB의 데이터 수에 따라 1~3명을 추천.
     *  추천된 개발자는 List<String>에 담겨 반환됩니다.
     [ 1, 3, 12] 이런 형태로 반환.
     */
    @PostMapping("/api/project/{projectId}/issue/{issueId}/suggestDev")
    public ResponseEntity<List<String>> suggestion(@PathVariable Long issueId, HttpServletRequest request) {
        String sessionid = request.getHeader("sessionid");
        if (SessionManager.getSession(sessionid) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<String> devIds = issueService.suggestDev(issueId);
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>(devIds));
    }

    @PostMapping("/api/issue/{issueId}/info")
    public ResponseEntity<?> updateIssueInfo(@PathVariable Long issueId, @RequestBody IssueUpdateDTO issueUpdateDTO) {
        issueService.updateInfo(issueId, issueUpdateDTO);
        return ResponseEntity.ok().build();
    }
}
