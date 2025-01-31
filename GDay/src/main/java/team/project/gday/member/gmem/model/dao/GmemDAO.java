package team.project.gday.member.gmem.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import team.project.gday.Product.model.vo.Attachment;
import team.project.gday.Product.model.vo.GClass;
import team.project.gday.Product.model.vo.GOption;
import team.project.gday.Product.model.vo.Order;
import team.project.gday.member.bmem.model.vo.PageInfo9;
import team.project.gday.member.model.vo.Member;
import team.project.gday.member.model.vo.ProfileImg;
import team.project.gday.member.model.vo.Refund;
import team.project.gday.review.model.vo.Review;

/**
 * @author Younghyun
 *
 */
@Repository
public class GmemDAO {
	// 마이바티스를 이용한 DB 연결 객체를 의존성 주입
	@Autowired
	private SqlSessionTemplate sqlSession;

	/** 주문용 listCount dao
	 * @param map
	 * @return
	 */
	public int orderListCount(Map<String, Object> map) {
		System.out.println("dao map : " +  map);
		return sqlSession.selectOne("gMemMapper.orderListCount", map);
	}

	/** 주문 목록 조회 dao
	 * @param pInfo
	 * @param map
	 * @return
	 */
	public List<Order> selectOrderList(PageInfo9 pInfo, Map<String, Object> map) {
		
		int offset = (pInfo.getCurrentPage()-1) * pInfo.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pInfo.getLimit());
		
		return sqlSession.selectList("gMemMapper.selectOrderList", map, rowBounds);
	}

	/** 주문 목록 썸네일 조회 dao
	 * @param oList
	 * @return
	 */
	public List<Attachment> selectThumbnails(List<Order> oList) {
		return sqlSession.selectList("gMemMapper.selectThumbnails", oList);
	}

	/** 선물 주문 목록 옵션 조회 dao
	 * @param oList
	 * @return optList
	 */
	public List<GOption> selectOptList(List<Order> oList) {
		return sqlSession.selectList("gMemMapper.selectOptList", oList);
	}

	/** 클래스 주문 목록 상세 정보 dao
	 * @param oList
	 * @return cList
	 */
	public List<GClass> selectCList(List<Order> oList) {
		return sqlSession.selectList("gMemMapper.selectCList", oList);
	}
	

	/** 주문내역 확인용 후기 체크
	 * @param oList
	 * @return
	 */
	public List<Review> selectRCheck(List<Order> oList) {
		return sqlSession.selectList("gMemMapper.selectRCheck", oList);
	}


	//---------------------------------주문 목록 끝 --------------------------------------------
	
	/**주문 상세 조회(주문 번호에 들어간 모든 주문 상품 번호 가져오기)
	 * @param orderNo
	 * @param type
	 * @return
	 */
	public List<Order> selectOrders(Map<String, Object> map) {
		return sqlSession.selectList("gMemMapper.selectOrders", map);
	}

	
	/** 주문 구매 확정 처리(선물)
	 * @param opNo
	 * @return result
	 */
	public int confirmOrder(int opNo) {
		return sqlSession.update("gMemMapper.confirmOrder", opNo);
	}

	/**주문 상품 번호 + 타입으로 하나의 주문 가져오기 dao
	 * @param map
	 * @return order
	 */
	public Order selectAOrder(Map<String, Object> map) {
		return sqlSession.selectOne("gMemMapper.selectAOrder", map);
	}

	/**상품 번호로 썸네일 조회 dao
	 * @param prdtNo
	 * @return thumbnail
	 */
	public Attachment selectAThumbnail(int prdtNo) {
		return sqlSession.selectOne("gMemMapper.selectAThumbnail", prdtNo);
	}

	/**옵션 번호로 옵션 내용 조회 dao
	 * @param giftOpNo
	 * @return gOption
	 */
	public GOption selectOption(int giftOpNo) {
		return sqlSession.selectOne("gMemMapper.selectOption", giftOpNo);
	}

	/**상품 번호로 클래스 상세 내용 조회 dao
	 * @param prdtNo
	 * @return gClass
	 */
	public GClass selectGClass(int prdtNo) {
		return sqlSession.selectOne("gMemMapper.selectGClass", prdtNo);
	}

	/**환불 테이블에 취소-반품 요청 삽입 dao
	 * @param refund
	 * @return result
	 */
	public int insertRequest(Refund refund) {
		return sqlSession.insert("gMemMapper.insertRequest", refund);
	}

	/** 주문 상품 테이블에 상태 변경(100/900 → 400, 200 → 500)
	 * @param map
	 * @return
	 */
	public int updateRfStatus(Map<String, Object> map) {
		return sqlSession.update("gMemMapper.updateRfStatus", map);
	}

	/**반품 요청 시 수거지 정보 변경 dao
	 * @param map
	 * @return result
	 */
	public int updateShipInfo(Map<String, Object> map) {
		return sqlSession.update("gMemMapper.updateShipInfo", map);
	}

	/**취소/반품 상세 조회 dao
	 * @param opNo
	 * @return
	 */
	public Refund selectRefundInfo(int opNo) {
		return sqlSession.selectOne("gMemMapper.selectRefundInfo", opNo);
	}

	/** 회원 조회
	 * @param loginMember
	 * @return
	 */
	public Member loginAction(Member loginMember) {
		return sqlSession.selectOne("memberMapper.loginAction", loginMember);
	}

	/** 일반 회원 탈퇴
	 * @param memberEmail
	 * @return
	 */
	public int accountDel(String memberEmail) {
		return sqlSession.update("memberMapper.accountDel", memberEmail);
	}

	
	//----------------------------------------------

	/** 내정보 기본 정보 조회
	 * @param memberNo
	 * @return
	 */
	public Member getMemInfo(int memberNo) {
		return sqlSession.selectOne("gMemMapper.getMemInfo", memberNo);
	}

	/** 내정보 기본 프로필 조회
	 * @param memberNo
	 * @return
	 */
	public ProfileImg getProfile(int memberNo) {
		return sqlSession.selectOne("gMemMapper.getProfile", memberNo);
	}

	/** 회원 일반 정보 update
	 * @param updateMember
	 * @return
	 */
	public int updateAction(Member updateMember) {
		return sqlSession.update("gMemMapper.updateAction", updateMember);
	}

	/** 기존 프로필 가져오기
	 * @param memberNo
	 * @return oldImg
	 */
	public ProfileImg selectProfile(int memberNo) {
		return sqlSession.selectOne("gMemMapper.selectProfile", memberNo);
	}

	/**새이미지로 프로필 업데이트
	 * @param pf
	 * @return
	 */
	public int updateProfile(ProfileImg pf) {
		return sqlSession.update("gMemMapper.updateProfile", pf);
	}

	/* 프로필 이미지 삽입
	 * @param pf
	 * @return
	 */
	public int insertProfile(ProfileImg pf) {
		return sqlSession.insert("gMemMapper.insertProfile", pf);
	}

	/**프로필 삭제
	 * @param memberNo
	 * @return
	 */
	public int deleteProfile(int memberNo) {
		return sqlSession.delete("gMemMapper.deleteProfile", memberNo);
	}

	/** 비번변경용 비밀번호 가져오기
	 * @param i
	 * @return
	 */
	public String selectPwd(int memberNo) {
		return sqlSession.selectOne("gMemMapper.selectPwd", memberNo);
	}

	/**비밀번호 업데이트
	 * @param map
	 * @return 
	 */
	public int updatePwd(Map<String, Object> map) {
		return sqlSession.update("gMemMapper.updatePwd", map);
	}

	
	
	
	

}






















