package team.project.gday.admin.model.vo;

public class adminPageInfo {
	private int currentPage; 	// 현재 페이지 번호를 표시할 변수
	private int listCount; 		// 전체 게시글 수
	private int limit = 10; 			// 한 페이지에 보여질 게시글 수
	private int pageSize = 10; 	// 보여질 페이징바의 페이지 개수
	
	private int maxPage;	 	// 전체 페이지에서 가장 마지막 페이지
	private int startPage;	 	// 페이징바 시작 페이지 번호
	private int endPage; 	 	// 페이징바 끝 페이지 번호
	
	private int boardType;
	
	public int getCurrentPage() {
		return currentPage;
	}

	// 페이징 처리(목록) 
	public adminPageInfo(int listCount, int currentPage) {
		super();
		this.currentPage = currentPage;
		this.listCount = listCount;
		makePageInfo();
	}

	public int getListCount() {
		return listCount;
	}

	public void setListCount(int listCount) {
		this.listCount = listCount;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getBoardType() {
		return boardType;
	}

	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public String toString() {
		return "adminPageInfo [currentPage=" + currentPage + ", listCount=" + listCount + ", limit=" + limit
				+ ", pageSize=" + pageSize + ", maxPage=" + maxPage + ", startPage=" + startPage + ", endPage="
				+ endPage + ", boardType=" + boardType + "]";
	}

	// 페이징 처리에 필요한 값을 계산하는 메소드.
	private void makePageInfo() {
		
		// * maxPage - 총 페이지수 
		// 게시글의 개수가 100개일 경우 필요 페이지 수 : 10 페이지
		// 게시글의 개수가 101개일 경우 필요 페이지 수 : 11 페이지
		// 전체 게시글 수 / 한 페이지에 보여질 개수 결과를 올림 처리함.
		maxPage = (int)Math.ceil(( (double)listCount / limit));
		
		// * startPage - 페이징바 시작 페이지 번호
		//   아래쪽에 페이지 수가 10개씩 보여지게 할 경우
		//   1, 11, 21, 31, .....
		startPage = (currentPage-1)/pageSize * pageSize + 1;
		
		// * endPage - 페이징바 끝 페이지 번호
		//   아래쪽에 페이지 수가 10개씩 보여지게 할 경우
		//   10, 20, 30, 40, ..... 
		endPage = startPage + pageSize - 1;
		
		// 총 페이지의 수가endPage 보다 클 경우
		if(maxPage > endPage) {
			endPage = startPage + pageSize - 1;
		}
		else {
			// 총 페이지 수가 endPage보다 작을 경우
			// ex) maxPage가 13페이지고 endPage가 20페이지일 경우 13이 끝이여야 함.
			endPage = maxPage;
		}
	}
}
