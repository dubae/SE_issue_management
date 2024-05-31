package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.IssueDTO;
import com.codingrecipe.member.service.IssueService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Controller
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
    @GetMapping("/project/{projectId}/issue")
    @ResponseBody
    public List<IssueDTO> issue(@PathVariable("projectId") Long projectId, Model model) {
        return issueService.findByProjectId(projectId);
    }
    //json형태로 받을 거면 위에 선택, 인터페이스는 아래 선택.
//    @GetMapping("/project/{projectId}/issue")
//    public String issue(@PathVariable("projectId") Long projectId, Model model) {
//        List<IssueDTO> issues = issueService.findByProjectId(projectId);
//        model.addAttribute("issues", issues);
//        return "issues";
//    }


//    /**
//     * 프로젝트 별 이슈 추가 사이트 구현(필요x)
//     */
//    @GetMapping("/project/{projectId}/issue/new")
//    public String newIssue(@PathVariable("projectId") Long projectId, Model model) {
//        model.addAttribute("projectId", projectId);
//        model.addAttribute("issueDTO", new IssueDTO());
//        return "addissue";
//    }



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
    @ModelAttribute
    @PostMapping("/project/{projectId}/issue/new")
    public void addIssue(@ModelAttribute IssueDTO issueDTO, Model model) {
        /**
         * 현재 내가 무슨 계정으로 로그인 되어 있는지
         * memberId값을 자동으로 issueDto에 연결시켜야 함.
         */
       // issueDTO.setWriterId(/** 여기에 입력할 것! */);
        issueService.addNewIssue(issueDTO);
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
    @GetMapping("/project/{projectId}/issue/{issueId}")
    @ResponseBody
    public IssueDTO issue_info(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueID, Model model) {
        return issueService.findById(issueID);

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
    @GetMapping("/project/{projectId}/issue/findByTitle")
    @ResponseBody
    public List<IssueDTO> findByTitle(@PathVariable("projectId") Long projectId, @RequestParam String title) {
        return issueService.findByTitle(title);
    }

    /**
     * 이슈의 상태 변경하기(new->assigned ...)
     * * 유저의 자격 확인해야 하는지는 나중에 논의.
        /project/{projectId}/issue/{issueId}/status?status=new
     */
    @ModelAttribute
    @PostMapping("/project/{projectId}/issue/{issueId}/status")
    public void changeStatus(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId,@RequestParam String status, Model model) {
        issueService.changeStatus(issueId,status);
    }

    /**
     * 이슈의 개발자 지정(변경)하기. 개발자 id를 req.param으로 넣기.
     * * 유저의 자격 확인(PL)해야 하는지는 나중에 논의.
         /project/{projectId}/issue/{issueId}/devId?devId=11
     */
    @ModelAttribute
    @PostMapping("/project/{projectId}/issue/{issueId}/devId")
    public void changeDevId(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId,@RequestParam Long devId, Model model){
        issueService.changeDevId(issueId,devId);
    }

    /**
     * 이슈를 상태로 검색하기(status)
     * @RequestParam 으로 status 넘겨주세요.
    /project/{projectId}/issue/findByStatus?status=new
     return [ {IssueDto} ... ]
     */
    @GetMapping("/project/{projectId}/issue/findByStatus")
    @ResponseBody
    public List<IssueDTO> findByStatus(@PathVariable("projectId") Long projectId, @RequestParam String status) {
        return issueService.findByStatus(status);
    }

    /**
     * 글쓴이 id(writerId)로 이슈 검색하기
     * @RequestParam으로 writerId 넘겨주세요.
    /project/{projectId}/issue/findByWriterId?writerId=1
     return [ {issuedto}... ]
     */
    @GetMapping("/project/{projectId}/issue/findByWriterId")
    @ResponseBody
    public List<IssueDTO> findByWriterId(@PathVariable("projectId") Long projectId, @RequestParam Long writerId) {
        return issueService.findByWriterId(writerId);
    }

    /** ----------------------이슈 통계 관련 API ------------------------------**/

    /**
     * 특정 월에 발생한 이슈들을 반환.
          /project/1/issue/month/5
             return [ {issuedto json} .. ]
     */
    @GetMapping("/project/{projectId}/issue/month/{month}")
    @ResponseBody
    public List<IssueDTO> findByMonth(@PathVariable("projectId") Long projectId, @PathVariable("month") int month) {
        return issueService.findIssuesByMonth(Month.of(month));
    }


     /**
     * 특정 날짜에 발생한 이슈들을 반환. List<issueDto>
     *      @RequestParam으로 year, month, day  넘겨주세요.
     *      /project/{projectId}/issue/findByDate?year=2024&month=5&day=27
     *      return [ {issuedto json} .. ]
     */
    @GetMapping("/project/{projectId}/issue/findByDate")
    @ResponseBody
    public List<IssueDTO> findByDate(@PathVariable("projectId") Long projectId, @RequestParam int year, @RequestParam int month, @RequestParam int day) {
        return issueService.findIssuesByDate(LocalDate.of(year, month, day));
    }

    /**
     * 특정 날짜에 발생한 이슈의 개수를 반환.
     * @RequestParam으로 year, month, day  넘겨주세요.
        /project/{projectId}/issue/countIssueByDate
     * return 으로 json이 아닌 그냥 int형으로 반환됨.
     */
    @GetMapping("/project/{projectId}/issue/countIssueByDate")
    @ResponseBody
    public int countIssueByDate(@PathVariable("projectId") Long projectId, @RequestParam int year, @RequestParam int month, @RequestParam int day) {
        return issueService.countIssuesByDate(LocalDate.of(year, month, day));
    }

    @PostMapping("/test/suggest/{id}")
    @ResponseBody
    public List<Long> suggestion(@PathVariable Long id) {
        return issueService.suggestDev(id);
    }

}
