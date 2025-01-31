package team.project.gday.review.model.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import team.project.gday.Product.model.vo.GClass;
import team.project.gday.Product.model.vo.GOption;
import team.project.gday.Product.model.vo.Order;
import team.project.gday.member.bmem.model.vo.PageInfo10;
import team.project.gday.member.bmem.model.vo.PageInfo9;
import team.project.gday.review.model.dao.ReviewDAO;
import team.project.gday.review.model.exception.UpdateAttachmentFailException;
import team.project.gday.review.model.vo.Review;


@Service
public class ReviewServiceImpl implements ReviewService {
	@Autowired
	private ReviewDAO dao;

	//후기 등록 구현 service
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertReview(Review review, List<MultipartFile> rvImg, String savePath) {
		
		int result = 0;
		
		//후기 번호는 rvNo == opNo
		review.setRvContent(replaceParameter(review.getRvContent().replaceAll("\n", "<br>")));
		
		result = dao.insertReview(review);
		
		if(result > 0) {
			
			if(!rvImg.isEmpty()) {
				if(!rvImg.get(0).getOriginalFilename().equals("")) {//이미지 원본파일명이 있을 때 = 이미지가 있을 때
					String rvImgPath = "/resources/images/reviewImg";
					String rvImgName = rename(rvImg.get(0).getOriginalFilename());
					
					review.setRvImgPath(rvImgPath);
					review.setRvImgName(rvImgName);
				
					result = 0;
					result = dao.insertRvImg(review);
					
					if(result > 0) {
						try {//서버에 넣기
							rvImg.get(0).transferTo(new File(savePath + "/" + rvImgName));
						
						} catch (Exception e) {//서버 저장 실패
							e.printStackTrace();
							throw new UpdateAttachmentFailException("후기 이미지 서버 저장 실패");
						}
					} else {//이미지 DB에 삽입 실패 시
						throw new UpdateAttachmentFailException("후기 이미지 서버 저장 실패");
					}
				}
			}//img 없을 때 
		}
		
		return result;
	}
	

	// 크로스 사이트 스크립트 방지 처리 메소드
	private String replaceParameter(String param) {
		String result = param;
		if(param != null) {
			result = result.replaceAll("&", "&amp;");
			result = result.replaceAll("<", "&lt;");
			result = result.replaceAll(">", "&gt;");
			result = result.replaceAll("\"", "&quot;");
		}
		return result;
	}
	
	// 파일명 변경 메소드
	public String rename(String originFileName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String date = sdf.format(new Date(System.currentTimeMillis()));
		
		int ranNum = (int)(Math.random()*100000); // 5자리 랜덤 숫자 생성
		
		String str = "_r" + String.format("%05d", ranNum);
		//String.format : 문자열을 지정된 패턴의 형식으로 변경하는 메소드
		// %05d : 오른쪽 정렬된 십진 정수(d) 5자리(5)형태로 변경. 빈자리는 0으로 채움(0)
		
		String ext = originFileName.substring(originFileName.lastIndexOf("."));
		
		return date + str + ext;
	}


	//마이페이지 pInfo 생성service 구현
	@Override
	public PageInfo9 getPageInfo(Map<String, Object> map) {
		
		int listCount = dao.reviewListCount(map); //주문 목록
		
		System.out.println("listCount: " + listCount);
		return new PageInfo9((Integer)map.get("cp"), listCount);
	}

	//마이페이지 후기 목록 조회
	@Override
	public List<Review> selectReviewList(PageInfo9 pInfo, Map<String, Object> map) {
		return dao.selectReviewList(pInfo, map);
	}

	//마이페이지 후기에 맞는 주문 내역 조회하기
	@Override
	public List<Order> selectOList(List<Review> rList) {
		return dao.selectOList(rList);
	}

	//마이페이지 후기 조회 옵션 부분
	@Override
	public List<GOption> selectOptList(List<Review> rList) {
		return dao.selectOptList(rList);
	}

	//마이페이지 후기 조회 클래스부분
	@Override
	public List<GClass> selectCList(List<Review> rList) {
		return dao.selectCList(rList);
	}

	//후기 삭제
	@Override
	public int deleteReview(int rvNo) {
		return dao.deleteReview(rvNo);
	}

	//상세 페이지 후기 조회용 pInfo
	@Override
	public PageInfo10 getPageInfo5(Map<String, Object> map) {
		
		int listCount = dao.getViewListCount(map); //주문 목록
		
		System.out.println("listCount: " + listCount);

		return new PageInfo10((Integer)map.get("cp"), listCount);
	}

	//상세페이지 후기 조회
	@Override
	public List<Review> selectReviewView(PageInfo10 pInfo, Map<String, Object> map) {
		return dao.selectReviewView(pInfo, map);
	}

	//제품 후기 별점 평균
	@Override
	public int getStarAvg(int prdtNo) {
		return dao.getStarAvg(prdtNo);
	}
	
	
}
