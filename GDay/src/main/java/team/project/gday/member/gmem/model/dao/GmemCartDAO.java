package team.project.gday.member.gmem.model.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import team.project.gday.Product.model.vo.Attachment;
import team.project.gday.member.model.vo.Baguni;

@Repository
public class GmemCartDAO {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
		
	// 장바구니 조회 DAO
	public List<Baguni> selectBaguni(int memberNo) {
		return sqlSession.selectList("cartMapper.selectBaguni", memberNo);
	}
	
	// 장바구니 썸네일 조회 Service 구현
	public List<Attachment> cartThumbnailList(List<Baguni> baguniList) {
		return sqlSession.selectList("cartMapper.cartThumbnailList", baguniList);
	}
	
	
	
	// 클래스 장바구니에 추가 DAO
	public int insertClassCart(Map<String, Object> map) {
		return sqlSession.insert("cartMapper.insertClassCart", map);
	}

	// 선물 장바구니에 추가 DAO
	public int insertGiftCart(Map<String, Object> map) {
		return sqlSession.insert("cartMapper.insertGiftCart", map);
	}
	
	
	
	
	// 장바구니에서 상품 삭제 DAO
	public int deleteCart(Map<String, Object> map) {
		return sqlSession.delete("cartMapper.deleteCart", map);
	}


}
