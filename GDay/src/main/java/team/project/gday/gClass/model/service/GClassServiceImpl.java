package team.project.gday.gClass.model.service;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import team.project.gday.Product.model.vo.Attachment;
import team.project.gday.Product.model.vo.GClass;
import team.project.gday.Product.model.vo.ProductCTag;
import team.project.gday.Product.model.vo.ProductStar;
import team.project.gday.common.model.exception.UserDefineException;
import team.project.gday.gClass.model.dao.GClassDAO;
import team.project.gday.member.bmem.model.vo.PageInfo10;
import team.project.gday.member.model.vo.Member;
import team.project.gday.search.model.vo.Search;

@Service
public class GClassServiceImpl implements GClassService {

	@Autowired
	private GClassDAO dao;
	
	//페이징 처리 객체 Service 구현
	@Override
	public PageInfo10 getPageInfo(int cp) {
		//전체 클래스 수 조회
		int classCount = dao.getClassCount();
		return new PageInfo10(cp, classCount);
	}

	//클래스 목록 조회 Service 구현
	@Override
	public List<GClass> selectList(PageInfo10 pInfo) {
		return dao.selectList(pInfo);
	}

	//썸네일 목록 조회 Service 구현
	@Override
	public List<Attachment> selectThumbnailList(List<GClass> gCList) {
		return dao.selectThumbnailList(gCList);
	}

	//클래스 상세 조회 Service 구현
	@Transactional(rollbackFor=Exception.class)
	@Override
	public GClass selectGClass(int prdtNo) {
		//1) 클래스 상세 조회
		GClass temp = new GClass();
		temp.setPrdtNo(prdtNo);
		GClass gClass = dao.selectGClass(temp);
		
		//2) 상세 조회 성공시 조회수 증가
		if(gClass != null) {
			int result = dao.increaseReadCount(prdtNo);
			
			if(result>0) {
				gClass.setReadCount(gClass.getReadCount() +1);;
			}
		}
		return gClass;
	}
	
	//클래스 상세 페이지에 포함된 이미지 목록 조회 Service 구현
	@Override
	public List<Attachment> selectAttachmentList(int prdtNo) {
		return dao.selectAttachmentList(prdtNo);
	}
	
	//클래스 상세 페이지의 판매자 정보 가져오기 Service 구현
	@Override
	public Member selectMember(int memNo) {
		return dao.selectMember(memNo);
	}

	//클래스 상세 페이지에 포함된 썸네일 조회 Service 구현
	@Override
	public Attachment selectThumbnail(int prdtNo) {
		return dao.selectThumbnail(prdtNo);
	}

	//상품별 해시태그 조회 Service 구현
	@Override
	public List<ProductCTag> selectPrdtTagList(int prdtNo) {
		return dao.selectPrdtTagList(prdtNo);
	}
	
	//클래스 수정 Service 구현
	@Transactional(rollbackFor=Exception.class)
	@Override
	public int updateClass(Map<String, Object> map, List<MultipartFile> images, String savePath) {
		
		//게시글 수정 Service 구현
		//1) 상품 테이블 수정
		int result = dao.updateProduct(map);
		
		if(result>0) {
			//2) 클래스 테이블 수정
			result = dao.updateClass(map);

			if(result>0) {
				//3) 상품-태그 테이블 기존 태그 삭제
				result = dao.deletePrdtTag(Integer.parseInt(map.get("prdtNo").toString()));
				
				if(result>0) {
					//4) 상품-태그 테이블 태그 삽입
					result= dao.insertTag(map);
					
				} if(result>0) {
					//5) 이미지 수정
					//비교 위해 수정 전 업로드 파일 정보를 얻어 옴
					List<Attachment> oldFiles = dao.selectAttachmentList(Integer.parseInt(map.get("prdtNo").toString()));
					
					//새로 업로드된 파일 정보를 담을 리스트
					List<Attachment> uploadImages = new ArrayList<Attachment>();
					
					//삭제되어야 할 파일 정보를 담을 리스트
					List<Attachment> removeFileList = new ArrayList<Attachment>();
					
					//DB에 저장할 웹상 이미지 접근 경로
					String filePath = "/resources/images/thumbnailImg";
					
					//새롭게 업로드된 파일 정보를 가지고 있는 images에 반복 접근
					for(int i=0; i<images.size(); i++) {
						//업로드된 파일 이미지가 있을 경우
						if(!images.get(i).getOriginalFilename().equals("")) {
							//파일명 변경
							String fileName = rename(images.get(i).getOriginalFilename());
							//Attachment 객체 생성
							Attachment at = new Attachment(filePath, fileName, i, Integer.parseInt(map.get("prdtNo").toString()));
							uploadImages.add(at);
							
							for(Attachment old: oldFiles) {
								if(old.getFileLevel() == i) {
									//DB에서 파일 번호가 일치하는 행의 내용을 수정하기 위해 파일 번호를 얻어옴
									//-> 수행 뒤 server에는 아직 남아있음
									at.setFileNo(old.getFileNo());
									removeFileList.add(old);
								}
							}
							
							result = dao.updateAttachment(at);
							
							if(result<=0) {
								throw new UserDefineException("썸네일 정보 수정 실패");
							} 
						}
					}
					
					
					
					
					//images 반복 접근 for문 종료
					//uploadImages == 업로드된 파일 정보 --> 서버에 파일 저장
					//removeFileList == 제거해야 할 파일 정보 --> 서버에서 파일 삭제
					// 수정되거나 새롭게 삽입된 이미지를 서버에 저장하기 위해 transferTo() 수행
					
					if(result>0) {
						for(int i=0; i<uploadImages.size(); i++) {
							try {
								images.get(uploadImages.get(i).getFileLevel())
								.transferTo(new File(savePath + "/" + uploadImages.get(i).getFileName()) );                                             
							}catch (Exception e) {
								e.printStackTrace();
								throw new UserDefineException("파일 정보 수정 실패");
							}
						}
					}
					
					// ------------------------------------------
					// 이전 파일 서버에서 삭제하는 코드 
					for(Attachment removeFile : removeFileList) {
						File tmp = new File(savePath + "/" + removeFile.getFileName());
						tmp.delete();
					}
					// ------------------------------------------
					
					//summernote로 작성된 게시글에 있는 이미지 정보 수정
					//게시글에 작성된 <img> 태그의 src속성을 이용해서 파일명을 얻어오기
					
					Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>"); //img 태그 src 추출 정규표현식

					//게시글에 작성된 <img>태그의 src속성을 이용하여 파일명을 얻어오기
					Matcher matcher = pattern.matcher((String)map.get("prdtContent"));
					
					//정규식을 통해 게시글에 작성된 이미지 파일명만 얻어와 모아둘 List 선언
					List<String> fileNameList = new ArrayList<String>();
					
					String src= null; //matcher에 저장된 src를 꺼내서 임시 저장할 변수
					String filename = null; //src에서 파일 명을 수정해서 임시 저장할 변수
					
					while(matcher.find()) {
						src = matcher.group(1);
					}
					
					//DB에 새로 추가할 이미지파일 정보를 모아둘 List 생성
					List<Attachment> newAttachmentList = new ArrayList<Attachment>();
					
					//DB에서 삭제할 이미지 파일 번호를 모아둔 List 생성
					List<Integer> deleteFileNoList = new ArrayList<>();
					
					//수정된 게시글 파일명 목록(fileNameList)과
					//수정 전 파일 정보 목록(oldFiles)를 비교해서
					//수정된 게시글 파일명 하나를 기준으로 하여 수정 전 파일명과 순서적 비교를 진행
					// --> 수정된 게시글 파일명과 일치하는 수정 전 파일명이 없다면 
					//== 새로 삽입된 이미지임을 의미함
					
					for(String fName : fileNameList) {
						
						boolean flag = true;
						
						for(Attachment oldAt : oldFiles) {
							if(oldAt.getFileLevel() == 0) continue;
							
							if(fName.equals(oldAt.getFileName())) { //수정 후 / 수정 전 같은 파일이 있다 == 수정되지 않았다
								flag = false;
								break;
							}
						}
						
						//flag == true == 수정 후 게시글 파일명과 수정 전 파일명이 일치하는 게 없을 경우
						// ==새로운 이미지 -> newAttachmentList 추가
						if(flag) {
							Attachment at = new Attachment(filePath, fName, 1, Integer.parseInt(map.get("prdtNo").toString()));
							newAttachmentList.add(at);
						}
					}
					
					//수정 전 파일 정보 목록(oldFiles)과
					//수정된 게시글 파일명 목록(fileNameList)을 비교
					//수정전 파일명 하나를 기준으로 하여 수정 후 파일명과 순서적 비교를 진행
					// --> 수정 전 게시글 파일명과 일치하는 수정 후 파일명이 없다면 
					//== 기존 수정 전 이미지가 삭제됨을 의미
					
					for(Attachment oldAt : oldFiles) {
						if(oldAt.getFileLevel() == 0) continue;
						boolean flag = true;

						for(String fName : fileNameList) {
							if(oldAt.getFileName().equals(fName)) {
								flag = false;
								break;
							}
						}
						
						//flag == true == 수정 전 파일명과 수정 후 파일명이 일치하는 게 없을 경우
						// == 삭제된 이미지 --> deleteFileNoList 추가
						if(flag) {
							deleteFileNoList.add(oldAt.getFileNo());
						}
					}
					
					//newAttachmentList / deleteFileNoList 완성됨
					if(!newAttachmentList.isEmpty()) { //새로 삽입된 이미지가 있다면
						result = dao.insertAttachmentList(newAttachmentList);
						
						if(result != newAttachmentList.size()) {//삽입된 결과행의 수와 수정된 리스트의 수가 맞지 않을 경우 == 실패
							throw new UserDefineException("파일 수정 실패(파일 정보 삽입 중 오류 발생)");
						}
					}
					
					if(!deleteFileNoList.isEmpty()) { //삭제될 이미지가 있다면
						result = dao.deleteAttachmentList(deleteFileNoList);
						
						if(result != deleteFileNoList.size()) {
							throw new UserDefineException("파일 수정 실패(파일 정보 삭제 중 오류 발생)");
						}
					}
				}
			}
		}
		
		return result;
	}
	
	
	public String rename(String originFileName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String date = sdf.format(new java.util.Date(System.currentTimeMillis()));

		int ranNum = (int)(Math.random()*100000); // 5자리 랜덤 숫자 생성

		String str = "_" + String.format("%05d", ranNum);
		//String.format : 문자열을 지정된 패턴의 형식으로 변경하는 메소드
		// %05d : 오른쪽 정렬된 십진 정수(d) 5자리(5)형태로 변경. 빈자리는 0으로 채움(0)

		String ext = originFileName.substring(originFileName.lastIndexOf("."));

		return date + str + ext;
	}

	//summernote에 업로드된 이미지 저장 serviceImpl
	@Transactional(rollbackFor=Exception.class)
	@Override
	public Attachment insertImages(MultipartFile uploadFile, String savePath) {
		
		//파일명 변경하기
		String fileName = rename(uploadFile.getOriginalFilename());
		//웹상 접근 주소 적기
		String filePath = "/resources/images/productInfoImg";
		
		Attachment at = new Attachment();
		at.setFilePath(filePath);
		at.setFileName(fileName);
		
		//transferTo == 서버에 파일 저장
		try {
			uploadFile.transferTo(new File(savePath + "/" + fileName ));
		} catch(Exception e) {
			e.printStackTrace();
			throw new UserDefineException("summernote 파일 업로드에 실패했습니다.");
		}
		return at;
	}

	//클래스 마감하기 Service 구현
	@Override
	public int pauseAction(int prdtNo) {
		return dao.pauseAction(prdtNo);
	}

	//평균 별점 목록 가져오기 Service 구현
	@Override
	public List<ProductStar> selectStarList(List<GClass> gCList) {
		return dao.selectStarList(gCList);
	}
	
	//상품별 평균 별점 가져오기	
	@Override
	public ProductStar selectStar(int prdtNo) {
		return dao.selectStar(prdtNo);
	}


	//메인에서 상위 3개 클래스 가져오기
	@Override
	public List<GClass> selectClassList3() {
		return dao.selectClassList3();
	}

	//검색 조건이 포함된 페이징 처리용 객체 얻어오기 Service 구현
	@Override
	public PageInfo10 getSearchPageInfo(Search search, int cp) {
		int listCount = 0;
		
		if(search.getCategory() == null) {
			listCount = dao.getSearchListCountAll(search.getSv());
		} else {
		listCount = dao.getSearchListCount(search);
		}
		return new PageInfo10(cp, listCount);
	}
	
	//검색 조건이 포함된 클래스 목록 조회 Service 구현
	@Override
	public List<GClass> selectSearchList(Search search, PageInfo10 pInfo) {
		if(search.getCategory() == null) {
			return dao.selectClassListAll(search, pInfo);
		}
		return dao.selectSearchList(search, pInfo);
	}

}
