package team.project.gday.member.bmem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import team.project.gday.Product.model.vo.Attachment;
import team.project.gday.Product.model.vo.GClass;
import team.project.gday.Product.model.vo.Gift;
import team.project.gday.member.bmem.model.service.BmemService;
import team.project.gday.member.bmem.model.vo.OrderList;
import team.project.gday.member.bmem.model.vo.PageInfo9;
import team.project.gday.member.bmem.model.vo.RefundList;
import team.project.gday.member.gmem.model.service.GmemService;
import team.project.gday.member.model.service.LoginService;
import team.project.gday.member.model.vo.BmemberInfo;
import team.project.gday.member.model.vo.LicenseImg;
import team.project.gday.member.model.vo.Member;
import team.project.gday.member.model.vo.ProfileImg;


@Controller
@SessionAttributes({"loginMember", "picture"})
@RequestMapping("/bMember/*")
public class BmemController {


	@Autowired 
	private BmemService service;
	
	@Autowired
	private GmemService gService;
	
	@Autowired
	private LoginService lService;
	
	//sweet alert 메시지 전달용 변수 선언
	private String swalIcon;
	private String swalTitle;
	private String swalText;

	
//	===================================== 화면 이동 관련 ======================================
	
	//비즈니스 내 판매 글 이동
	@RequestMapping("bSellList")
	public String bSellList(@RequestParam(value="cp", required = false, defaultValue = "1") int cp,
							@ModelAttribute("loginMember") Member loginMember,
							Model model) {				
		
		PageInfo9 pInfo = service.getGiftPageInfo(cp, loginMember);
		
		List<Gift> gList = service.bSellList(pInfo, loginMember);
		
		if(gList != null && !gList.isEmpty()) { // 게시글 목록 조회 성공 시
			List<Attachment> thumbnailList = service.bSellThumbnailList(gList);
			
			if(thumbnailList != null) {
				model.addAttribute("thList", thumbnailList);
			}
		}
	
		model.addAttribute("gList", gList);
		model.addAttribute("pInfo", pInfo);
		
		return "mypage/bMemPage/bSellList";
	}
	
	//비즈니스 주문조회 이동
	@RequestMapping("bOrderList")
	public String bOrderList(@RequestParam(value="cp", required = false, defaultValue = "1") int cp,
							 @ModelAttribute("loginMember") Member loginMember,
							 Model model) {
		
		PageInfo9 pInfo = service.getOrdListPageInfo(cp, loginMember);
		
		List<OrderList> oList = service.bOrderList(pInfo, loginMember);		

		model.addAttribute("oList", oList);
		model.addAttribute("pInfo", pInfo);
		
		return "mypage/bMemPage/bOrderList";
	}
	
	//비즈니스 환불 목록 조회 이동
	@RequestMapping("bRefundList")
	public String bRefundList(@RequestParam(value="cp", required = false, defaultValue = "1") int cp,
							  @ModelAttribute("loginMember") Member loginMember,
							  Model model) {
		
		PageInfo9 pInfo = service.getRfListPageInfo(cp, loginMember);
		
		List<RefundList> rList = service.bRefundList(pInfo, loginMember);		

		model.addAttribute("rList", rList);
		model.addAttribute("pInfo", pInfo);
		
		return "mypage/bMemPage/bRefundList";
	}
	
	//비즈니스 주문 취소 목록 조회 이동
	@RequestMapping("bCancelList")
	public String bCancelList(@RequestParam(value="cp", required = false, defaultValue = "1") int cp,
							  @ModelAttribute("loginMember") Member loginMember,
							  Model model) {
		
		PageInfo9 pInfo = service.getOcListPageInfo(cp, loginMember);
		
		List<RefundList> oCList = service.bCancelList(pInfo, loginMember);		

		System.out.println(oCList); 
		
		model.addAttribute("oCList", oCList);
		model.addAttribute("pInfo", pInfo);
		
		
		return "mypage/bMemPage/bCancelList";
	}
	
	
	//비즈니스 회원이 등록한 클래스 목록 조회 Controller
	@RequestMapping("bClassList")
	public String bClassList(@RequestParam(value="cp", required = false, defaultValue = "1") int cp,
							 @ModelAttribute("loginMember") Member loginMember,
							 Model model) {
		
		PageInfo9 pInfo = service.getClassPageInfo(cp, loginMember);
		
		List<GClass> cList = service.bClassList(pInfo, loginMember);
		
		if(cList != null && !cList.isEmpty()) { // 게시글 목록 조회 성공 시
			List<Attachment> thumbnailList = service.bClassThumbnailList(cList);
			
			if(thumbnailList != null) {
				model.addAttribute("thList", thumbnailList);
			}
		}
		
		model.addAttribute("cList", cList);
		model.addAttribute("pInfo", pInfo);
		
		return "mypage/bMemPage/bClassList";
	}
	
	//비즈니스 수강 신청 목록 조회
	@RequestMapping("bEnrolmentList")
	public String bEnrolmentList(@RequestParam(value="cp", required = false, defaultValue = "1") int cp,
								 @ModelAttribute("loginMember") Member loginMember,
								 Model model) {
		
		PageInfo9 pInfo = service.getEmListPageInfo(cp, loginMember);
		
		List<OrderList> eList = service.bEnrolmentlList(pInfo, loginMember);		

		System.out.println(eList); 
		
		model.addAttribute("eList", eList);
		model.addAttribute("pInfo", pInfo);
		
		return "mypage/bMemPage/bEnrolmentList";
	}
	
	//비즈니스 수강 취소 목록 조회
	@RequestMapping("bClassCancelList")
	public String bClassCancelList(@RequestParam(value="cp", required = false, defaultValue = "1") int cp,
								   @ModelAttribute("loginMember") Member loginMember,
								   Model model) {
		
		PageInfo9 pInfo = service.getCcListPageInfo(cp, loginMember);
		
		List<RefundList> cList = service.bClassCancelList(pInfo, loginMember);		

		System.out.println(cList); 
		
		model.addAttribute("cList", cList);
		model.addAttribute("pInfo", pInfo);
		
		return "mypage/bMemPage/bClassCancelList";
	}
		
	//비즈니스 내 정보 수정 이동
	@RequestMapping("bMemUpdate/{memberNo}")
	public String bMemUpdateForm(@PathVariable("memberNo") int memberNo,
								Model model) {
		
		System.out.println("memberNo : " + memberNo );
		
		//bMeminfo
		BmemberInfo bmemInfo = service.getBmemInfo(memberNo);
		
		model.addAttribute("bmemInfo", bmemInfo);
		
		//license
		LicenseImg licenseImg = service.getLicense(memberNo);
		
		model.addAttribute("licenseImg", licenseImg);
		
		return "mypage/bMemPage/bMemUpdate";
	}
	
	//내정보 수정
	@RequestMapping("updateMember")
	public String bMemInfoUpdate(@ModelAttribute(name="loginMember", binding=false) Member loginMember,
									@ModelAttribute Member updateMember,
									@RequestParam(value="profile", required = false) List<MultipartFile> profile,
									@RequestParam(value="deleteProfile", required=false) boolean profileFlag, 
									HttpServletRequest request,	SessionStatus status,
									RedirectAttributes ra, Model model) {
		//System.out.println(updateMember);
		System.out.println(profile);
		//System.out.println("profileFlag: " + profileFlag);
		
			
		String savePath = request.getSession().getServletContext().getRealPath("resources/images/profileImg");
		int result = gService.updateProfile(profile, savePath, updateMember, profileFlag) ;

		if(result > 0 ) {
			swalIcon = "success";
			swalTitle = "내 정보 수정 성공";
			
			Member newLoginMember = new Member();
			newLoginMember = loginMember;
			newLoginMember.setMemberNick(updateMember.getMemberNick());
			newLoginMember.setMemberPhone(updateMember.getMemberPhone());
			newLoginMember.setMemberAddress(updateMember.getMemberAddress());
			
			ProfileImg picture = lService.getProfile(loginMember.getMemberNo());
			if(picture == null) {
				picture = new ProfileImg();
				picture.setPfName("profile.jpg");
				//status.setComplete();
			} 
			model.addAttribute("picture", picture);
			System.out.println("picture : " + picture );
			System.out.println("newLogin : "+ newLoginMember);
			//로그인 새로 올리기
			model.addAttribute("loginMember", newLoginMember);
			
			//request.getSession().setAttribute("loginMember", newLoginMember);
		} else {
			swalIcon = "error";
			swalTitle = "내 정보 수정 실패";
		}
		ra.addFlashAttribute("swalIcon", swalIcon);
		ra.addFlashAttribute("swalTitle", swalTitle);
		return "redirect:bMemUpdate/" + updateMember.getMemberNo();
	}
	
	
	//비즈니스 인증 재신청
	@ResponseBody
	@RequestMapping("recertifyLcs/{memberNo}")
	public int recertifyLcs(@PathVariable("memberNo") int memberNo,
							@ModelAttribute BmemberInfo bmemInfo,
							@RequestParam("license") MultipartFile license,
							HttpServletRequest request) {
		
		System.out.println("memberNo:" + memberNo);
		System.out.println("license:" + license);
		
		bmemInfo.setBmemNo(memberNo);
		
		System.out.println("bmemberInfo:" + bmemInfo);
		
		//첨부 파일 삽입을 위한 준비
				String savePath = null;//
				savePath = request.getSession().getServletContext().getRealPath("resources/images/licenseImg");
		
		int result = service.recertifyLcs(bmemInfo, license, savePath);
		
		return result;
	}
	
	
	//비즈니스 비밀번호 변경 이동
	@RequestMapping("bMemPwdUpdateForm")
	public String bMemPwdUpdateForm() {
		return "mypage/memPwdUpdate";
	}
	
	//비즈니스 회원 탈퇴 이동
	@RequestMapping("bMemSessionUpdate")
	public String bMemSessionUpdate() {
		return "mypage/accountDelete";
	}
	
	
	
	
//	===================================== 기능 관련 ======================================
	// 주문 목록 상태 변경
	@ResponseBody
	@RequestMapping("orderStatusChange/{status}")
	public int orderStatusChange(@RequestParam(value = "opAry[]") List<String> opAry,
								 @ModelAttribute("loginMember") Member loginMember,
							 	 @PathVariable("status") int status) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", loginMember.getMemberNo());
		map.put("status", status);
		map.put("opAry", opAry);
		
		int result = service.orderStatusChange(map);
				
		return result;
	}
	
	// 환불 목록 상태 변경
	@ResponseBody
	@RequestMapping("refundStatusChange/{status}")
	public int refundStatusChange(@RequestParam(value = "opAry[]") List<String> opAry,
								  @ModelAttribute("loginMember") Member loginMember,
								  @PathVariable("status") int status) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", loginMember.getMemberNo());
		map.put("status", status);
		map.put("opAry", opAry);
		
		int result = service.refundStatusChange(map);
		
		return result;
	}
	
	// 취소 목록 상태 변경
	@ResponseBody
	@RequestMapping("cancelStatusChange/{status}")
	public int cancelStatusChange(@RequestParam(value = "opAry[]") List<String> opAry,
							      @ModelAttribute("loginMember") Member loginMember,
								  @PathVariable("status") int status) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", loginMember.getMemberNo());
		map.put("status", status);
		map.put("opAry", opAry);
		
		int result = service.cancelStatusChange(map);
		
		return result;
	}
	
	
	// 클래스 상태 변경
	@ResponseBody
	@RequestMapping("enrolmentStatusChange/{status}")
	public int enrolmentStatusChange(@RequestParam(value = "opAry[]") List<String> opAry,
			@ModelAttribute("loginMember") Member loginMember,
			@PathVariable("status") int status) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", loginMember.getMemberNo());
		map.put("status", status);
		map.put("opAry", opAry);
		
		int result = service.enrolmentStatusChange(map);
		
		return result;
	}
	
	
	// 클래스 취소 목록 상태 변경
	@ResponseBody
	@RequestMapping("classCancelStatusChange/{status}")
	public int classCancelStatusChange(@RequestParam(value = "opAry[]") List<String> opAry,
									   @ModelAttribute("loginMember") Member loginMember,
									   @PathVariable("status") int status) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", loginMember.getMemberNo());
		map.put("status", status);
		map.put("opAry", opAry);
		
		int result = service.classCancelStatusChange(map);
		
		return result;
	}
	
	
	// =================================== 회원 탈퇴 =====================================
	
	@RequestMapping("accountDel")
	@ResponseBody
	public int accountDel(@ModelAttribute Member loginMember,SessionStatus status, HttpServletResponse response){
		
		System.out.println(loginMember);
		int result = service.accountDel(loginMember);
		System.out.println("컨트롤러 result"+result);
		if (result >0) {
			status.setComplete();
			Cookie loginSessionId = new Cookie("loginSessionId", null);
			loginSessionId.setPath("/");
			System.out.println("-------");
			System.out.println(loginSessionId.getValue());
			System.out.println("-------");
			loginSessionId.setMaxAge(0);
			response.addCookie(loginSessionId);
		}
		return result;
	}
	
	
}