package team.project.gday.search.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import team.project.gday.Product.model.vo.GClass;
import team.project.gday.gift.model.vo.Gift;
import team.project.gday.search.model.dao.SearchDAO;
import team.project.gday.search.model.vo.PageInfoMain;
import team.project.gday.search.model.vo.Search;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearchDAO dao;

	//메인 검색결과 선물 10개 보여주기
	@Override
	public List<Gift> selectGiftSearchList(Search search, PageInfoMain pInfo) {
		if(search.getCategory() == null) {
			return dao.selectGiftListAll();
		}
		return dao.selectGiftSearchList(search, pInfo);
	}
	
	//메인 검색결과 클래스 10개 보여주기
	@Override
	public List<GClass> selectClassSearchList(Search search, PageInfoMain pInfo) {
		if(search.getCategory() == null) {
			return dao.selectClassListAll();
		}
		return dao.selectClassSearchList(search, pInfo);
	}
	


}
