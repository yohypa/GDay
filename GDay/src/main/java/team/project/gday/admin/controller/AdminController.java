package team.project.gday.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import team.project.gday.Product.model.vo.Product;
import team.project.gday.admin.model.service.AdminService;
import team.project.gday.admin.model.vo.Customor;
import team.project.gday.admin.model.vo.Reply;
import team.project.gday.admin.model.vo.Report;
import team.project.gday.admin.model.vo.adminPageInfo;
import team.project.gday.magazine.model.vo.Magazine;
import team.project.gday.member.model.vo.Member;

@Controller
@SessionAttributes({"loginMember"})
@RequestMapping("/admin/*")
public class AdminController {
	
	@Autowired
	private AdminService service;

	private String swalIcon = null;
	private String swalTitle = null;
	
	// 관리자 페이지/전체 회원관리로 이동 Controller
	@RequestMapping("adminMember")
	public String adminMember(@RequestParam(value = "cp", required = false, defaultValue= "1") int cp,
							  Model model) {
		
		// 페이징 처리
		adminPageInfo pInfo = service.getPageInfo(cp);
		// System.out.println(pInfo);
		
		// 전체 회원 조회
		List<Member> mList = service.adminMember(pInfo);
		
		/*
		 * for(Member m : mList) { System.out.println(m); }
		 */
	
		model.addAttribute("mList", mList);
		model.addAttribute("pInfo", pInfo);
		
		return "admin/adminMember";
	}
	
	// 회원 등급 변경 Controller
	@RequestMapping("memberGrade")
	@ResponseBody
	public int memberGrade(@RequestParam String[] memberNo,
						   @RequestParam String memberGrade) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", memberNo);
		map.put("memberGrade", memberGrade);
		
		// System.out.println(map);
		int result = service.updateMemberGrade(map);
		
		return result;
	}

	// -----------------------------------------------------------------------------
	
	// 비즈니스 회원 목록 조회 화면 전환 Controller
	@RequestMapping("adminBMemSub")
	public String adminBMemSub(@RequestParam(value = "cp", required = false, defaultValue= "1") int cp,
							   @ModelAttribute Member member,
			  				   Model model) {
		
		List<Member> bmList = service.getMember();
		System.out.println(bmList);
		if (bmList != null) {
			model.addAttribute("bmList", bmList);
		}
		
		return "admin/adminBMemSub";
	}

	// 비즈니스 회원신청 상세조회 화면 전환 Controller
	@RequestMapping("adminBMemView/{memberNo}")
	public String adminBMemView(@PathVariable("memberNo") int memberNo, Model model) {
		// System.out.println(memberNo);
		
		Member member = service.getbMember(memberNo);
		// System.out.println(member);
		
		if (member != null) {
			model.addAttribute("member", member);
			
			String bmemShop = service.bmemShop(memberNo);
			if (bmemShop != null) {
				model.addAttribute("bmemShop", bmemShop);
			}
		}
		return "admin/adminBMemView";
	}

	//-------------------------------------------------------------------------------
	
	// 블랙리스트 회원 조회 화면 전환 Controller
	@RequestMapping("adminBlackMem")
	public String adminBlackMem(@RequestParam(value = "cp", required = false, defaultValue= "1") int cp,
			  					Model model) {
		
		// 페이징 처리
		adminPageInfo pInfo = service.getPageBmInfo(cp);
		// System.out.println(pInfo);
		
		// 블랙 리스트 회원 조회
		List<Member> bmList = service.adminBlackMem(pInfo); 
		/* for(Member m : bmList) { System.out.println(m); } */
		
		model.addAttribute("bmList", bmList);
		model.addAttribute("pInfo", pInfo);
		
		return "admin/adminBlackMem";
	}
	
	// -------------------------------------------------------------------------------
	
	// 게시판 전체 조회 화면 전환 Controller
	@RequestMapping("adminBoard")
	public String adminBoard(@RequestParam(value = "cp", required = false, defaultValue= "1") int cp,
							 Model model) {

		// 페이징 처리
		adminPageInfo pInfo = service.getPageBdInfo(cp);
		
		List<Product> pList = service.productBoard(pInfo);
		
		/*
		 * for(Product p: product) { System.out.println(p); }
		 */
		
		model.addAttribute("pList", pList);
		model.addAttribute("pInfo", pInfo);
		
		return "admin/adminBoard";
	}
	
	// 게시글 상태 변경 Controller
	@RequestMapping("boardApUpdate")
	@ResponseBody
	public int boardUpdate(@RequestParam String[] prdtNo,
						   @RequestParam String prdtStatus) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prdtNo", prdtNo);
		map.put("prdtStatus", prdtStatus);
		
		// System.out.println(map);
		
		int result = service.boardUpdate(map);
		return result;
	}
	
	// ----------------------------------------------------------
	
	// 매거진 게시판 조회 화면 전환 Controller
	@RequestMapping("adminMagazine")
	public String adminMagazine(@RequestParam(value = "cp", required = false, defaultValue= "1") int cp,
								Model model) {
		
		// 페이징 처리
		adminPageInfo pInfo = service.getPageMzInfo(cp);
		
		// 매거진 조회
		List<Magazine> mzList = service.adminMagazine(pInfo);
		/*
		 * for(Magazine mz: mzList) { System.out.println(mz); }
		 */
		
		model.addAttribute("pInfo", pInfo);
		model.addAttribute("mzList", mzList);
		
		return "admin/adminMagazine";
	}
	
	// ----------------------------------------------------------------

	// 신고 게시판 화면 전환 Controller
	@RequestMapping("adminReportStand")
	public String adminReportStand(@RequestParam(value = "cp", required = false, defaultValue= "1") int cp,
								   Model model) {
		

		// 페이징 처리
		adminPageInfo pInfo = service.getPageRsInfo(cp);
		// System.out.println(pInfo);
		
		// 신고 게시판 목록 조회
		List<Report> rsList = service.adminReportStand(pInfo);
		// System.out.println(rsList);
		
		model.addAttribute("pInfo", pInfo);
		model.addAttribute("rsList", rsList);

		
		// 신고 페이지 조회
		List<Report> rpList = service.adminReportStand(pInfo);
		
		model.addAttribute("pInfo", pInfo);
		
		return "admin/adminReportStand";
	}
	
	
	// 신고하기 화면 팝업창 Controller
	@RequestMapping("reportForm/{prdtNo}")
	public String reportForm(@PathVariable int prdtNo) {
		return "admin/reportForm";
	}
	
	// 신고하기 Controller
	@RequestMapping(value="reportForm/report", method=RequestMethod.POST)
	public String report(@ModelAttribute(name="loginMember", binding=false) Member loginMember,
						 @ModelAttribute Report report,
						 @RequestParam int prdtNo,
						 Model model) {
		
		// System.out.println(prdtNo);
		// System.out.println(report);
		// System.out.println(loginMember);
		
		report.setParentNo(prdtNo);
		report.setMemberNo(loginMember.getMemberNo());
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prdtNo", prdtNo);
		map.put("memberNo", loginMember.getMemberNo());
		
		// System.out.println("map " + map);
		
		int result = service.report(map, report);
		
		model.addAttribute("result", result);
		
		return "admin/reportResult";
	}
	// -----------------------------------------------------------------
	
	// 관리자 고객센터 목록 화면 전환 Controller
	@RequestMapping("adminCustomerService")
	public String adminCustomerService(@RequestParam(value = "cp", required = false, defaultValue= "1") int cp,
									   Model model) {
		
		// 페이징 처리
		adminPageInfo pInfo = service.getPageAcInfo(cp);
		// System.out.println(pInfo);
		
		// 고객센터 목록 읽어오기
		List<Customor> cList = service.adminCustomor(pInfo);
		// System.out.println(cList);
		
		model.addAttribute("pInfo", pInfo);
		model.addAttribute("cList", cList);
		
		return "admin/adminCustomerService";
	}
	
	// ---------------------------------------------------------------
	
	// 고객센터 문의 화면 전환 Controller
	@RequestMapping("memberCustomerInsert")
	public String memberCustomerInsert() {
		return "admin/memberCustomerInsert";
	}
	
	// 고객센터 문의 작성 Controller
	@RequestMapping("customerInsert")
	public String customerInsert(@ModelAttribute("loginMember") Member loginMember,
								 @RequestParam("title") String title,
								 @RequestParam("content") String content,
								 HttpServletRequest request,
								 RedirectAttributes ra) {
		
		String url = "";
		
		System.out.println(title);
		System.out.println(content);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", loginMember.getMemberNo());
		map.put("title", title);
		map.put("content", content);
		
		int result = service.customerInsert(map);
		
		String referer = request.getHeader("Referer");
		
		if(result > 0) {
			ra.addFlashAttribute("swalIcon", "success");
			ra.addFlashAttribute("swalTitle", "문의글이 등록되었습니다.");
			
			System.out.println(1);
			
			url = "redirect:memberCustomer";
		} else {
			ra.addFlashAttribute("swalIcon", "error");
			ra.addFlashAttribute("swalTitle", "문의글 등록 중 에러가 발생하였습니다.");
			
			System.out.println(2);
			
			url = "redirect:" + referer;
		}
		
		return url;
	}
	
	
	// 회원 고객센터 목록 화면 전환 Controller
	@RequestMapping("memberCustomer")
	public String memberCustomer(@RequestParam(value = "cp", required = false, defaultValue= "1") int cp,
								 @ModelAttribute("loginMember") Member loginMember,
								 Model model){
		
		// 페이징 처리
		adminPageInfo pInfo = service.getPageMcInfo(cp, loginMember);
		// System.out.println(pInfo);
		
		// 회원의 게시글 목록 조회
		List<Report> rList = service.memberCustomer(pInfo, loginMember);
		// System.out.println(rList);
		
		model.addAttribute("pInfo", pInfo);
		model.addAttribute("rList", rList);
		
		return "admin/memberCustomer";
	}

	// 회원 고객센터 상세조회 화면 전환 Controller
	@RequestMapping("memberCustomerView/{cusNo}")
	public String memberCustomerView(@PathVariable("cusNo") int cusNo,
									 Model model){
		
		// 문의글 확인
		// System.out.println(cusNo);
		Customor customer = service.memberView(cusNo);
		//System.out.println(customer);
		
		if(customer != null) {
			model.addAttribute("customer", customer);
		}
		
		return "admin/memberCustomerView";
	}
	
	
	// 고객센터 댓글 목록 조회 Controller
	@RequestMapping("selectReplyList/{parentCustomerNo}")
	@ResponseBody
	public String selectReplyList(@PathVariable("parentCustomerNo") int parentCustomerNo,
								  Model model) {
		// System.out.println(parentCustomerNo);
		Reply reply = service.selectReplyList(parentCustomerNo);
		// System.out.println(reply);
		
		System.out.println(reply);
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd / HH:mm").create();
		
		if(reply != null) {
			model.addAttribute("customer" , reply);
		}
		return gson.toJson(reply);
	}
	
	// 고객센터 댓글 작성 Controller
	@RequestMapping("insertReply/{parentCustomerNo}")
	@ResponseBody
	public int insertReply(@PathVariable("parentCustomerNo") int parentCustomerNo,
						   @RequestParam("replyContent") String replyContent) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentCustomerNo", parentCustomerNo);
		map.put("curContent", replyContent);
		
		service.deleteReply(map);
		
		int result = service.insertReply(map); 		
		
		if(result > 0) {
			service.cusUpdateStatus(map);
			System.out.println(1);
		}
		
		return result;
	}
	
	//----------------------------------
	
	@RequestMapping("approval")
	@ResponseBody
	public int approval(@RequestParam int memberNo) {
		int result = service.approval(memberNo);
		return result;
	}
	
	
	
	@RequestMapping("deny")
	@ResponseBody
	public int deny(@RequestParam int memberNo) {
		
		System.out.println(memberNo);
		
		int result = service.deny(memberNo);
		return result;
	}
	
}
