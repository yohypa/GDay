package team.project.gday.member.gmem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import team.project.gday.Product.model.vo.Attachment;
import team.project.gday.member.gmem.model.service.GmemCartService;
import team.project.gday.member.model.vo.Baguni;
import team.project.gday.member.model.vo.Member;

@Controller
@SessionAttributes({"loginMember"})
@RequestMapping("/cart/*")
public class GmemCartController {

	@Autowired
	private GmemCartService service;
	
	// 장바구니 화면으로 이동 Controller
	@RequestMapping("memberCart")
	public String memberCartForm(@ModelAttribute("loginMember") Member loginMember,
							     Model model) {	
		
		int memberNo = loginMember.getMemberNo();
		
		List<Baguni> baguniList = service.selectBaguni(memberNo);
		
		if(baguniList != null && !baguniList.isEmpty()) { // 게시글 목록 조회 성공 시
			List<Attachment> thumbnailList = service.cartThumbnailList(baguniList);
			
			if(thumbnailList != null) {
				model.addAttribute("thList", thumbnailList);
			}
		}
		
		model.addAttribute("bList", baguniList);
		
		return "mypage/gMemPage/gMemCart";
	}
		
	
	// 클래스 장바구니에 추가 Controller
	@RequestMapping("memberClassCart")
	@ResponseBody
	public int memberClassCart(@RequestParam("prdtNo") int prdtNo,
							   @RequestParam("amount") int amount,
							   @ModelAttribute("loginMember") Member loginMember,
							   Model model) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prdtNo", prdtNo);
		map.put("amount", amount);
		map.put("memberNo", loginMember.getMemberNo());
		
		int result = service.insertClassCart(map);
		
		return result;
	}
	
	// 선물 장바구니에 추가 Controller
	@RequestMapping("memberGiftCart")
	@ResponseBody
	public int memberGiftCart(@RequestParam("prdtNo") int prdtNo,
							   @RequestParam("amount") int amount,
							   @RequestParam("gOption") int gOption,
							   @ModelAttribute("loginMember") Member loginMember,
							   Model model) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prdtNo", prdtNo);
		map.put("amount", amount);
		map.put("gOption", gOption);
		map.put("memberNo", loginMember.getMemberNo());
		
		int result = service.insertGiftCart(map);
		
		return result;
	}
	
	
	
	
	
	
	// 장바구니에서 제거 Controller
	@RequestMapping("deleteCart/{cartNo}")
	@ResponseBody
	public int deleteCart(@ModelAttribute("loginMember") Member loginMember,
						      @PathVariable("cartNo") int cartNo) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", loginMember.getMemberNo());
		map.put("cartNo", cartNo);
			
		return service.deleteCart(map);
	}
	
}
