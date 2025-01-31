<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="../common/header.jsp"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

	<!-- reset.css -->
    <link rel="stylesheet" href="${contextPath}/resources/css/common/reset.css">
    
    <!-- giftView.css -->
    <link rel="stylesheet" href="${contextPath}/resources/css/gift/giftView.css?ver=1.1">
    <title>선물 상세 페이지</title>
</head>
<body>

	<c:if test="${loginMember == null}">
		<c:set var="loginFlag" value="0"></c:set>
	</c:if>
	<c:if test="${loginMember != null}">
		<c:set var="loginFlag" value="1"></c:set>
	</c:if>

    <section>
        <div class="gift_wrapper">
            <form action="">
                <div class="gift_img_wrapper">
                    <div class="gift_img">
                        <img src="${contextPath}${thumbnail.filePath}/${thumbnail.fileName}" alt="">
                    </div>
                    <div class="rate">
                        <span><i class="fas fa-star"></i>${star.rvStarView}</span>
                    </div>
                </div>
                <div class="gift_info">
                    <div class="gift_option">
                        <div class="title">
                            <div>
                                <h1>${gift.prdtName}</h1>
                                <span><fmt:formatNumber value="${gift.prdtPrice}"/>원</span>
                            </div>
                            <div>
                                <i class="far fa-comments"></i>
                                <span>문의하기</span>
                            </div>
                        </div>
                        
                        <c:choose>
							<c:when test="${!empty goption}">
                      		 <div class="option" id="options">
                            	<span>옵션</span>
                           		 <select name="gift-option" id="gift-option">
                            		<c:forEach var="goption" items="${goption}" varStatus="vs">
	                                <option value="${goption.gOptNo}">${goption.gOptName}</option>
                                </c:forEach>
                            	</select>
       						</div>
                        	</c:when>
                        	<c:otherwise>
                        		<div class="gift_cnt">
		                                <div class="opt_title">
		                                    <span class="class-amount">갯수</span>
		                                </div>
		                                <div class="cnt">
		                              		  <i class="fas fa-minus" onclick="del()"></i>
		                                    <input type="text" class="amount" value=1 style="width:30px">
		                                    <i class="fas fa-plus" onclick="add()"></i>
		                                   <!--  <input type=hidden name="sell_price" value="${gclass.prdtPrice}"> -->
		                                </div>
		                            </div>
                        	</c:otherwise>
                        </c:choose> 
                           	 <div id="option_list">

                           	 </div>
                           		 
                        		
                    </div>
                    <div class="priceBtn">
                        <p>총 <span class="acnt amount"></span>개</p>
                        <div class="price">
                            <span>총 상품금액</span>
                            <span class="sum"></span>
                        </div>
    
                        <div class="option_btn">
                            <button type="button" class="cart">장바구니에 넣기</button>
                           <!--  <a class="wish">위시리스트로 등록하기</a> -->
                            <button type="button" class="order">주문하기</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </section>

    <section>
        <div class="gift">
            <div class="gift_title">
                <div><a href="#about">상품 설명</a></div>
                <div><a href="#review">상품 후기</a></div>
                <div><a href="#refund">배송 교환 반품</a></div>
            </div>
            <div class="describe">
                <h1>판매자 정보</h1>
                <div class="report">
                    <a href='javascript:;' onclick="window.open('${contextPath}/admin/reportForm/${gift.prdtNo}', '_blank', 'width=500, height=500, top=200, left=100, scrollbars=no'); return false;">
                        <i class="fas fa-siren-on"></i>
                    </a>
                </div>
                <div class="b_info">
                    <table>
                        <tr>
                            <th colspan="2"><span>${member.memberName}</span> 판매자님</th>
                        </tr>
                        <%-- <tr>
                            <td>이름</td>
                            <td>${member.memberName}</td>
                        </tr>--%>
                         <tr>
                            <td>상호명</td>
                            <td>${member.memberNick}</td>
                        </tr>
                        <tr> 
                            <td>전화번호</td>
                            <td>${member.memberPhone}</td>
                        </tr>
                        <tr>
                            <td>주소</td>
                            <td>${member.memberAddress}</td>
                        </tr>
                        <tr>
                            <td>이메일</td>
                            <td>${member.memberEmail}</td>
                        </tr>
                    </table>
                </div>
                <h1 id="about">상품 설명</h1>
                <div class="about_gift">
                    <p>
                        ${gift.prdtContent}
                    </p>
                </div>
                <div class="g_info">
                    <table>
                        <tr>
                            <th colspan="4">상품 태그</th>
                        </tr>
                        <tr>
                            <td>연령대</td>
                            <td><c:forEach var="tag" items="${prdtTagList}" varStatus="vs">
                            	<c:if test="${tag.ctNo == 100}">
                            		${tag.tagName}
                            	</c:if>
                            </c:forEach></td>
                            <td>가격대</td>
                            <td><c:forEach var="tag" items="${prdtTagList}" varStatus="vs">
                            	<c:if test="${tag.ctNo == 200}">
                            		${tag.tagName}
                            	</c:if>
                            </c:forEach></td>
                        </tr>
                        <tr>
                            <td>분위기</td>
                            <td><c:forEach var="tag" items="${prdtTagList}" varStatus="vs">
                            	<c:if test="${tag.ctNo == 300}">
                            		${tag.tagName}
                            	</c:if>
                            </c:forEach></td>
                            <td>관계</td>
                            <td><c:forEach var="tag" items="${prdtTagList}" varStatus="vs">
                            	<c:if test="${tag.ctNo == 400}">
                            		${tag.tagName}
                            	</c:if>
                            </c:forEach></td>
                        </tr>
                        <tr>
                            <td>기념일/명절</td>
                            <td><c:forEach var="tag" items="${prdtTagList}" varStatus="vs">
                            	<c:if test="${tag.ctNo == 500}">
                            		${tag.tagName}
                            	</c:if>
                            	</c:forEach></td>
                            <td>계절</td>
                            <td><c:forEach var="tag" items="${prdtTagList}" varStatus="vs">
                            	<c:if test="${tag.ctNo == 600}">
                            		${tag.tagName}
                            	</c:if>
                            </c:forEach></td>
                        </tr>
                        <tr>
                            <td>색상</td>
                            <td><c:forEach var="tag" items="${prdtTagList}" varStatus="vs">
                            	<c:if test="${tag.ctNo == 700}">
                            		${tag.tagName}
                            	</c:if>
                            </c:forEach></td>
                            <td>종류</td>
                            <td><c:forEach var="tag" items="${prdtTagList}" varStatus="vs">
                            	<c:if test="${tag.ctNo == 800}">
                            		${tag.tagName}
                            	</c:if>
                            </c:forEach></td>
                        </tr>
                    </table>
                </div>

                <div class="gift_pic">
                    <img src="images/gift1.jpg" alt="">
                    <img src="images/gift1.jpg" alt="">
                </div>

                <div class="refund" id="refund">
                    <h1>반품 / 교환정보</h1>
                    <div class="table_wrapper">
                        <table>
                            <tr>
                                <th colspan="4">
                                    <h2>G-Day 반품/교환 안내</h2>
                                    <p>반품 시 먼저 판매자와 연락하셔서 반품사유, 택배사, 배송비, 반품지 주소등을 협의하신 후 반품상품을 발송해 주시기 바랍니다.</p>
                                </th>
                            </tr>
                            <tr>
                                <td>판매자 지정댁배사</td>
                                <td colspan="3">우체국택배</td>
    
                            </tr>
                            <tr>
                                <td>반품배송비</td>
                                <td>무료</td>
                                <td>교환배송비</td>
                                <td>무료</td>
                            </tr>
                            <tr>
                                <td>보내실 곳</td>
                                <td colspan="3">서울특별시 중구 신당동 어딘가</td>
                            </tr>
                            <tr>
                                <td>반품/교환 사유에 <br>따른요청 가능 시간</td>
                                <td colspan="3">구매자 단순 변심은 상품 수령 후 7일 이내</td>
                            </tr>
                            <tr>
                                <td>반품/교환 불가능 사유</td>
                                <td colspan="3">-</td>
                            </tr>
                            
                        </table>
                    </div>
                </div>

                <div id="review" class="review_wrapper">
                
                <jsp:include page="../review/reviewView.jsp"></jsp:include>
                
                </div>
                <div class="Gbutton">
                    <button class="update">수정</button>
                </div>
            </div> 
        </div> 
    </section>

	<jsp:include page="../common/footer.jsp"/>
    <script src="${contextPath}/resources/js/fontawesome.js"></script>
    <script>
		$("#option_list").html("");
	    var price = ${gift.prdtPrice };
	    function add(){
	    	$(".amount").val( Number($(".amount").val()) + 1  );
	    	$(".sum").text( Number($(".sum").text()) + price  );
	    	$(".acnt.amount").text($(".amount").val() )
	    	total_cnt = Number($(".amount").val())
	    	console.log(total_cnt)
	    	
	    }
	    
	    function del(){
	    	
	    	if($(".amount").val() > 1 ){
		    	$(".amount").val( Number($(".amount").val()) - 1  );
		    	$(".sum").text( Number($(".sum").text()) - price  );
		    	$(".acnt.amount").text($(".amount").val() )
		    	total_cnt = Number($(".amount").val())
	    		console.log(total_cnt)
	    	}
	    }
	    
	    $(".amount").on("input", function(){
	    	
	    	if(isNaN(Number($(".amount").val())) ){ // 숫자 말고 다른 것 입력 시
	    		$(".amount").val("");
	    		$(".sum").text( price  );
	    	}else{
	    		$(".sum").text( Number($(".amount").val()) * price  );
	    	}
	    	
	    	
	    });
	    
	    
	    // 옵션 추가
	    
	    $("#gift-option").on("change", function(){
	    	console.log($("#options option:selected").text())
	    	console.log($("#options option:selected").val())
	    	
	    	$("#option_list").html("");
	    	
	    	var addContent = 
	    		'<div class="gift_cnt">'+
		            '<div class="opt_title">'+
		                '<span>${gift.prdtName}</span>'+
		                '<span>'+ $("#options option:selected").text() +'</span>'+
		                '<input type="hidden" name="gift-option" value="'+$("#options option:selected").val()+'">'+
		                '<input type="hidden" value="'+1+'">'+
		            '</div>'+
		            '<div class="cnt">'+
                    '<input type="number" class="item_cnt"  min="0">'+
                	'</div>'+
		        '</div>';
		        

		        
	    	
	    	$("#option_list").append(addContent)
	    });
	    

	    
	    var total_cnt = 0;
	    var total_sum = 0;
	    $(document).on('input','.item_cnt',function(){
	    	
	    	
	    	
	    
			//console.log($(this).val());
			
			
			cnt = Number($(this).val());
			
		    	var cnt = 0;
			for (let i = 0; i < $(".item_cnt").length; i++) {
		    	console.log(Number($(".item_cnt")[i].value))
		    	cnt +=Number($(".item_cnt")[i].value)
				console.log(cnt);
			}
			total_cnt = cnt;
			
			total_sum = total_cnt*'${gift.prdtPrice}'
			$(".acnt").text(total_cnt);
			$(".sum").text(total_sum);
		});    

	    
    
	    $(".fa-comments").on('click', function(){
	     
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
				    		me: "${loginMember.memberNo}",
				    		you: "${member.memberNo}",
				    		
				    	},
				    	success: (result) =>{
				    		console.log("성공")
				    		Swal.fire(
							  '문의가 전송되었습니다',
							  ' ',
							  'success'
								);
				    	},
				    	error: (result) =>{
				    		console.log("실패")
				    	}
				    	
				    });
		    		
		    	})
	
	    });
	    
	    
	    
	    
	    
	    
	    
 /* ======================== 주문하기 버튼 클릭 시 ======================== */    
	 	var loginFlag = ${loginFlag};  
	 	
	    $(".order").on("click", function() {
	    	var prdtNo = ${gift.prdtNo};
	    	var prdtName = "${gift.prdtName}";
	    	var gOptName = $("#gift-option").find(":selected").text();
	    	var prdtPrice = ${gift.prdtPrice};
	    	var prdtAmount = total_cnt;

	    	 if(loginFlag == 1) {
	    		 $.ajax({
	   	    		url : "${contextPath}/payment/onePaymentInfo",
	   	    		data : {"prdtNo" : prdtNo,
	   	    				"prdtName" : prdtName,
	   	    				"prdtPrice" : prdtPrice,
	   	    				"prdtAmount" : prdtAmount,
	   	    				"gOptName" : gOptName},
	   	    		type : "post",
	   	    		success : function(result) {
	   	    			if(result > 0) {
	   	    				location.href = "${contextPath}/payment/onePaymentForm";
	   	    			} else {
	   	    				window.alert("주문 페이지 이동 중 에러가 발생했습니다.");
	   	    			}
	   	    			
	   	    		},
	   	    		error : function() {
	   	    			window.alert("장바구니에 등록 중 에러가 발생하였습니다.");
	   	    		}
	   	    	 });	 
	    	 } else {
	    		 window.alert("로그인 후 장바구니 사용이 가능합니다.");
	    	 }
	    	
	    	
	    });
	    
	    
	    
	    
 /* ======================== 장바구니 버튼 클릭 시 ======================== */
	     $(".cart").on("click", function() {
	    	 var prdtNo = ${gift.prdtNo};
/* 	    	 var amount = Number($(".amount").val()); */
 	    	 var gOption = Number($("select[name='gift-option']").val());
	    	 
	    	 if (isNaN(gOption)==true) {
	    		 
	    		gOption = 0;
				console.log("//////")
			}
	    	 
	    	 console.log(gOption)
	    	 if(loginFlag == 1) {
	    		 $.ajax({
	   	    		url : "${contextPath}/cart/memberGiftCart",
	   	    		data : {"prdtNo" : prdtNo,
	   	    				"amount" : total_cnt,
	   	    				"gOption" : gOption},
	   	    		type : "post",
	   	    		success : function(result) {
	   	    			if(result > 0) {
	   	    				window.alert("장바구니에 등록되었습니다.");
	   	    			} else {
	   	    				window.alert("이미 등록된 상품입니다.");
	   	    			}
	   	    			
	   	    		},
	   	    		error : function() {
	   	    			window.alert("장바구니에 등록 중 에러가 발생하였습니다.");
	   	    		}
	   	    	 });	 
	    	 } else {
	    		 window.alert("로그인 후 장바구니 사용이 가능합니다.");
	    	 }
	   			
	    	 
	     });
 
 
</script>



</body>
</html>