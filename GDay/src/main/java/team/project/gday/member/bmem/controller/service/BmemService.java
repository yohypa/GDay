package team.project.gday.member.bmem.controller.service;

import java.util.List;

import team.project.gday.Product.model.vo.Attachment;
import team.project.gday.Product.model.vo.GClass;
import team.project.gday.Product.model.vo.Gift;
import team.project.gday.member.bmem.controller.model.PageInfo;

public interface BmemService {

	/** 페이징 처리 객체 생성 Service
	 * @param cp
	 * @return pInfo
	 */
	PageInfo getGiftPageInfo(int cp);
 
	/** 내 판매글 목록 조회 Service
	 * @param pInfo
	 * @return gList
	 */
	List<Gift> bSellList(PageInfo pInfo);

	/** 내 판매글 썸네일 목록 조회 Service
	 * @param gList
	 * @return thList
	 */
	List<Attachment> bSellThumbnailList(List<Gift> gList);
	
	
	/** 페이징 처리 객체 생성 Service
	 * @param cp
	 * @return pInfo
	 */
	PageInfo getClassPageInfo(int cp);
	
	/** 판매 회원 클래스 목록 조회 Service
	 * @param pInfo
	 * @return cList
	 */
	List<GClass> bClassList(PageInfo pInfo);
	
	/** 판매 회원 클래스 썸네일 목록 조회 Service
	 * @param cList
	 * @return thList
	 */
	List<Attachment> bClassThumbnailList(List<GClass> cList);



}
