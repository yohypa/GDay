package team.project.gday.review.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import oracle.net.ano.Service;
import team.project.gday.Product.model.vo.Attachment;
import team.project.gday.Product.model.vo.GClass;
import team.project.gday.Product.model.vo.GOption;
import team.project.gday.Product.model.vo.Order;
import team.project.gday.member.bmem.model.vo.PageInfo10;
import team.project.gday.member.bmem.model.vo.PageInfo9;
import team.project.gday.member.gmem.model.service.GmemService;
import team.project.gday.member.model.vo.ListCondition;
import team.project.gday.review.model.service.ReviewService;
import team.project.gday.review.model.vo.Review;

//전체가 ajax로 통신되기 때문에 restController 사용
@RestController
@RequestMapping("/review/*")
@SessionAttributes({"loginMember"})
public class ReviewController {

	@Autowired
	private ReviewService rService;
	
	@Autowired
	private GmemService mService;
	
	//sweet alert 메시지 전달용 변수 선언
	private String swalIcon;
	private String swalTitle;
	private String swalText;
	
	
	//리뷰창 팝업 시 정보 조회
	@RequestMapping("getOrderInfo/{type}/{opNo}")
	public String getOrderInfo(@PathVariable("type") String type,
								@PathVariable("opNo") int opNo) {
		
		String url = null;//주소 담기
		//파라미터 맵에 담기
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("type", type);
		map.put("opNo", opNo);
		
		//json 형태 반환용 map returnMap 생성
		Map<String, Object> returnMap = new HashMap<String, Object>();
				
		//주문 상품 번호 + 타입으로 주문 정보 조회
		Order order = mService.selectAOrder(map);
		if(order != null) {
			returnMap.put("order", order);

			//썸네일 리스트 구하기
			Attachment thumbnail = mService.selectAThumbnail(order.getPrdtNo());
			System.out.println("thumbnail : " + thumbnail);
			
			if(thumbnail != null) {
				//modelattribute로 보내기
				returnMap.put("thumbnail", thumbnail);
				
				if(type.equals("G")) {//선물일 때
					
					//선물이면 goption 가져와야 함
					GOption gOption = mService.selectOption(order.getGiftOpNo());
					
					if(gOption != null) {
						returnMap.put("gOption", gOption);
						System.out.println("gOption : " + gOption);
					}
					
				} else if (type.equals("C")) {//클래스일 때

					//클래스면 gclass를 가져와야 함
					GClass gClass = mService.selectGClass(order.getPrdtNo());
					
					if(gClass != null) {
						returnMap.put("gClass", gClass);
						System.out.println("gClass: " + gClass);
					}
				} 
			}
		}
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		
		return gson.toJson(returnMap); 
	}
	
	//후기 삽입
	@RequestMapping("insertReview")
	public int insertReview(@ModelAttribute Review review,
							/* @RequestParam("orderNo") int orderNo, */
							@RequestParam(value="rvImage", required=false) List<MultipartFile> rvImg,
							HttpServletRequest request) {
		
		System.out.println("review : " + review);
		System.out.println("rvImg : " + rvImg);
		
		//첨부 파일 삽입을 위한 준비
		String savePath = null;//
		savePath = request.getSession().getServletContext().getRealPath("resources/images/reviewImg");
		
		int result = rService.insertReview(review, rvImg, savePath);
		
		return result;
	}
	
	//후기 조회
	@RequestMapping("selectReviewList")
	public String selectReviewList(@ModelAttribute ListCondition listCd,
									@RequestParam("writerNo") int memberNo,
									@RequestParam("type") String type) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("type", type);
		map.put("writerNo", memberNo);
		map.put("start", listCd.getPeriodStart());		
		map.put("end", listCd.getPeriodEnd());
		map.put("cp", listCd.getCp());
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		PageInfo9 pInfo = rService.getPageInfo(map);
		pInfo.setLimit(5);
		
		returnMap.put("pInfo", pInfo);
		
		List<Review> rList = rService.selectReviewList(pInfo, map);
		
		returnMap.put("rList", rList);
		
		if(rList != null && !rList.isEmpty()) {
			List<Order> oList = rService.selectOList(rList);
			
			if(oList != null & !oList.isEmpty()) {
				returnMap.put("oList", oList);
			
				
				if(type.equals("G")) {//선물일 때
						
					//선물이면 goption 가져와야 함
					List<GOption> optList = rService.selectOptList(rList);
					
					if(optList != null && !optList.isEmpty()) {
						returnMap.put("optList", optList);//선물 : returnMap에 optList 추가
					}
					System.out.println("optList : " + optList);
						
				} else if (type.equals("C")) {//클래스일 때
					//클래스면 gclass를 가져와야 함
					
					List<GClass> cList = rService.selectCList(rList);
					
					if(cList != null && !cList.isEmpty()) {
						returnMap.put("cList", cList);
					}
					System.out.println(cList);
				} 
			}
		}
		
		System.out.println(returnMap);
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		
		return gson.toJson(returnMap); 
	}
	
	//후기 삭제
	@RequestMapping("deleteReview/{rvNo}")
	public int deleteReview(@PathVariable("rvNo") int rvNo) {
		
		int result = rService.deleteReview(rvNo);
		
		return result;
	}
	
	
	//선물, 클래스 상세 페이지 후기 조회(prdtNo)
	@RequestMapping("selectReviewView")
	public String selectReviewView(@RequestParam("cp") int cp,
									@RequestParam("prdtNo") int prdtNo,
									@RequestParam("type") String type) {
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("type", type);
		map.put("cp", cp);
		map.put("prdtNo", prdtNo);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();		
		
		PageInfo10 pInfo = rService.getPageInfo5(map);
		pInfo.setLimit(5);
		pInfo.setPageSize(5);
		
		returnMap.put("pInfo", pInfo);
		
		List<Review> rList = rService.selectReviewView(pInfo, map);
		
		returnMap.put("rList", rList);
		
		if(rList != null && !rList.isEmpty()) {
			if(type.equals("G")) {//선물일 때
				
				//선물이면 goption 가져와야 함
				List<GOption> optList = rService.selectOptList(rList);
				
				if(optList != null && !optList.isEmpty()) {
					returnMap.put("optList", optList);//선물 : returnMap에 optList 추가
				}
				System.out.println("optList : " + optList);
			}
			
			//별점 평균
			int starAvg = rService.getStarAvg(prdtNo);
			returnMap.put("starAvg", starAvg);
		}
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		
		return gson.toJson(returnMap); 
	}
	
	
	
	
	
}
