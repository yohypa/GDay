<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!-- orderListJS 용 JSP -->    

<script>

var memberNo = "${loginMember.memberNo}";
var cp;
var periodRadio;
var listContainer;
var maxPage; //최대 페이지

$(document).ready(function(){//ready 함수
	
	listContainer = $(".container-orders");
	
	//기본 화면 결제일 기간 : 일주일 / 전체  + list 초기화
	(function(){
		$("#7days").click();
		loadNewPage();//
	})();	
	
	//period 조회 버튼 클릭
	$("#list-search-btn").on("click", function(){
		cp = 1;
		selectOrderList(cp);
	});
	
	//더보기 버튼 클릭
	$(".btn-more").on("click", function(){
		cp = cp + 1;
		console.log(cp);
		selectOrderList(cp);
	});

});//ready 함수 끝


//화면 리로드 느낌
function loadNewPage(){
		
		listContainer.html("");
		console.log("리셋");
		
		if(cp == undefined || cp <= 0) {
			cp = 1;//첫 페이지
		} 
		console.log(cp);

		for(var i=1; i<=cp; i++){ 
		//목록, 이전으로 버튼으로 돌아온 경우
			selectOrderList(i); 
		 console.log("다시 로딩" + i);
		}

}


/* 주문 내역 조회 */
function selectOrderList(cp){
	var periodStart = $("#periodStart").val();//조회 시작일
	var periodEnd = $("#periodEnd").val();//조회 마지막일
	periodRadio = $("input[name='periodRadio']").val();
	
	var statusNo = $("#giftStatus").val();//상태
	
	
	if(cp<=1){
		listContainer.html("");
	}
	//array를 넘기기 위해 필요한 설정
//	jQuery.ajaxSettings.traditional = true;

	$.ajax({
		url : "../selectOrderList/G/" + memberNo, /* type : 1(선물) + 회원번호*/
		data : { "cp" : cp, 
						"periodStart" : periodStart, 
						"periodEnd" : periodEnd, 
						"periodRadio" : periodRadio,
						"statusNo" : statusNo },
		type : "post",
		dataType : "json",
		success : function(map){
			console.log(map);
			var oList = map.oList;

			if(oList.length > 0){
				
				var thumbnails = map.thumbnails;
				var optList = map.optList;
				var rCheck = map.rCheck;
				
		    $.each(oList, function(index, order){   
					
					var container = $("<div>").addClass('row container-list');
					
					/* listCard 시작 */
					var listCard = $("<div>").addClass('list-card');
					
					//상세 조회하는 함수
					var click = "gotoDetail(" + order.orderNo +","+ order.opNo +","+ order.statusNo + ")";
					
					/* 썸네일 부분 */
					var imgUrl;
					$.each(thumbnails, function(index, image){
						if(order.prdtNo == image.prdtNo){
							imgUrl = "url(" + "${contextPath}" + image.filePath + "/" + image.fileName + ")";
						}
					});
					var listThumb = $("<div>").addClass('list-thumb').attr("onclick", click)
													.css("background-image", imgUrl);
	
					/* list text 부분 */
					var listText = $	("<div>").addClass('list-text');
					
					var orderNo = $("<span>").addClass('orderNo list-hidden').text(order.orderNo).attr("onclick", click);
					var opNo = $("<span>").addClass('opNo list-hidden').text(order.opNo).attr("onclick", click);
					
					var listName = $("<span>").addClass('list-name').text(order.prdtName).attr("onclick", click);
					
					/* 숫자.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","); 화폐 , 찍어주 */
					var text1 = (order.prdtPrice * order.opAmount).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
					var listText1 = $("<span>").addClass('list-text-1').text(text1).attr("onclick", click);
					
					var text2 = "";
					text2 = order.opAmount + "개";
					
					//옵션이 있을 때
					$.each(optList, function(index, option){
						if(order.giftOpNo == option.gOptNo){
							text2 = option.gOptName + " / " + order.opAmount + "개";
						}
					});
					
					var listText2 = $("<span>").addClass('list-text-2').text(text2);
	
					var listText3 = $("<span>").addClass('list-text-3').text(order.statusName);
					
					listText.append(orderNo).append(opNo).append(listName)
									.append(listText1).append(listText2).append(listText3);
					
					listCard.append(listThumb).append(listText);
					/* listCard 끝 */
					
					/* list-btn 시작 */
					var listBtn = $("<div>").addClass('list-btn');
					var listSeller = $("<div>").addClass('list-seller');
					var listStatus = $("<div>").addClass('list-status');
					
					var btn; 
					
					/* seller 부분 */
					var sellerName = $("<div>").addClass('seller-name').text(order.sellerName);
					
					click = "gotoInquiry(" + order.sellerNo + ")"; //문의하기 팝업창 생기게 하는 함수
					var btn = $("<a>").addClass('btn-inquiry').text("문의하기")
												.attr("onclick", click);
					
					listSeller.append(sellerName).append(btn);
					
					/* status 부분 */
					var status = order.statusNo;
					
					var url = "../cancelRequest/G/" + order.opNo;//반품/취소 요청 href + 주문 상품 번호 
					
					if(status == 100) { //결제 완료
						btn = $("<a>").addClass('btn-cancel').attr("href", url).text("취소 신청");
						listStatus.append(btn);
						
					} else if(status == 200){ //발송 완료
						btn = $("<a>").addClass('btn-takeback').attr("href", url).text("반품 신청");
						var btn2 = $("<a>").addClass('btn-confirm')
											.attr("onclick", "confirm(" + order.opNo + ")").text("구매 확정");
	
						listStatus.append(btn2).append(btn);
	
					} else if(status == 300){//구매 확정
						
						btn = $("<a>").addClass('btn-review')
						.attr("onclick", "popUp("+ order.opNo + ", 'g'" +")").text("후기 쓰기");
						
						$.each(rCheck, function(index, review){
							if(review.rvNo == order.opNo) btn = "";
						});

						listStatus.append(btn);
					} //취소-반품 요청 중 + 완료
					
					listBtn.append(listSeller).append(listStatus);
					/* btn 부분 끝 */
					
					container.append(listCard).append(listBtn); //추가
					
					listContainer.append(container);//누적 담기
					
				});//oList 담기 끝
				
			}//oList가 있을 때 if 문 끝
			
			else { //oList가 없을 때
				
				var div = $("<div>").addClass('no-list');
				var span = $("<span>").addClass('no-list-text').text("😥주문 내역이 없습니다😥");		
				div.append(span);
				listContainer.append(div);
				
			}
			
			//더보기 버튼 현재 페이지가 마지막 페이지이면 숨기기 + 아니면 보이기
			var pInfo = map.pInfo;
			maxPage = pInfo.maxPage;
			console.log(maxPage +"/"+ cp);
			if(cp >= maxPage) {
				$(".btn-more").hide();
			} else {
				$(".btn-more").show();
			}
			
			cp = pInfo.currentPage;//재로딩시를 대비하여 cp 저장
			
		},
		error : function(){
			console.log("주문 내역 조회 중 오류");
		}
		
	});//ajax 끝
}//selectOrderList 끝



//클릭 시 상세 이동
	/* 목록 클릭 시 상세 조회로 이동(주문번호 사용) */
function gotoDetail(orderNo, opNo, statusNo){
	var url;
	
	if(statusNo <= 300){
		url = "../orderView/G/" + orderNo;
		
	} else { //취소-반품 요청 중 혹은 완료됐을 때
		url = "../cancelView/G/" + opNo;
	}
		location.href = url;
}


//클릭 시 판매자에게 문의하는 창 연결
function gotoInquiry(sellerNo){

  Swal.fire({ 
  	  input: 'textarea',
  	  inputLabel: 'Message',
  	  inputPlaceholder: '내용을 입력해주세요', 
  	  inputAttributes: {
  	    'aria-label': 'Type your message here'
  	  },
  	  showCancelButton: true
  	}).then((result) => {
		    $.ajax({
		    	url: "${contextPath}/message/gcMsg",
		    	type: "post",
		    	data: {
		    		msgContent: result.value,
		    		me: memberNo,
		    		you: sellerNo,
		    		
		    	},
		    	success: (result) => {
		    		console.log("성공")
		    		Swal.fire(
					  '문의가 전송되었습니다',
					  ' ',
					  'success'
					)
		    	},
		    	error: () => {
		    		console.log("실패")
		    		
		    	}
		    });
  	})
  	
}

//구매확정 메소드
function confirm(opNo){
	
	  //confirm 확인 후 진행
	  swal.fire({icon:"question", 
		  					title: "해당 상품을 구매 확정하시겠습니까?",
		  					html: "구매 확정 시, 반품 요청이 불가합니다.",
		  					showCancelButton: true,
		  					confirmButtonText: "구매 확정",
		  					confirmButtonColor: "#54b39E",
		  					cancelButtonText: "취소",
		  					cancelButtonColor: "#a9a9a9",
		  					reverseButtons: true//버튼 위치 바꾸기
		  					}).then((result) => {
		  						if(result.isConfirmed){
		  							
		  							$.ajax({
		  								url : "${contextPath}/gMember/confirmOrder",
											data : {"opNo" : opNo},
											success : function(result){
												if(result > 0) {
													swal.fire ({icon: "success", 
																			title : "구매 확정되었습니다.",
																			confirmButtonColor: "#54b39E"});	
													
													loadNewPage();
													//ajax로 페이지를 불러와 누적하기 때문에 새로 해당 페이지까지 로드하려면
													//cp 수만큼 selectOrderList(cp)를 다시해야 함
													
												} else {
													swal.fire ({icon: "error", 
																			title : "구매 확정에 실패했습니다.",
																			confirmButtonColor: "#54b39E"});
												}
											},
											error : function(){
												console.log("구매 확정 과정 중 에러 발생");
											}
		  							});//ajax 끝
		  							
		  						} 
		  					});//confirm 끝
}//구매 확정 끝

//팝업은 모달창jsp에 있음


</script>