var product_cost = 0;
var delivery_cost = 0;



$(document).ready(function(){
	

    document.querySelectorAll('input[name="address"]').forEach(function (radio) {
        radio.addEventListener('change', function () {
            var selectedRadio = document.querySelector('input[name="address"]:checked');

            // Populate the input fields with the selected address
            document.getElementById("buyer_name").value = selectedRadio.value;
            document.getElementById("buyer_tel").value = selectedRadio.dataset.phone;
            document.getElementById("postcode").value = selectedRadio.dataset.zipcode;
            document.getElementById("address").value = selectedRadio.dataset.address01;
            document.getElementById("detailAddress").value = selectedRadio.dataset.address02;

            // Make the input fields uneditable
            document.getElementById("buyer_name").readOnly = true;
            document.getElementById("buyer_tel").readOnly = true;
            document.getElementById("postcode").readOnly = true;
            document.getElementById("address").readOnly = true;
            document.getElementById("detailAddress").readOnly = true;
            document.querySelector('.adress_form_btn').disabled = true;
        });
    });
    
    document.getElementById('enterAddressRadio').addEventListener('change', function () {
	    if (this.checked) {
	        document.getElementById("buyer_name").value = '';
	        document.getElementById("buyer_tel").value = '';
	        document.getElementById("postcode").value = '';
	        document.getElementById("address").value = '';
	        document.getElementById("detailAddress").value = '';
	
	        document.getElementById("buyer_name").readOnly = false;
	        document.getElementById("buyer_tel").readOnly = false;
	        document.getElementById("postcode").readOnly = false;
	        document.getElementById("address").readOnly = false;
	        document.getElementById("detailAddress").readOnly = false;
	        document.querySelector('.adress_form_btn').disabled = false;
	    }
	});

	
	
	//포인트.페이머니 잔액
    var emoney = Number($('#mypoint').text());
	var paymoney = Number($('#mypaymoney').text());
    
    $('#mypoint').text(comma(emoney));
    $('#mypaymoney').text(comma(paymoney));

    //포인트모두 사용 클릭시
    $(".emoney_chkbox").click(function(){
    
    	//표시된 결제예정금액
        var sum = Number($('#paper_settlement').text().replace(/[^0-9]/g, ''));
        var inputted = Number($("#emoney").val().replace(/[^0-9]/g, ''));

        if(emoney>=sum+inputted){
        	$(".emoney_reg #emoney").val(comma(sum+inputted));
        	$("#paper_reserves").text(" - " + comma(sum+inputted)+ " 원");
        	$(".emoney_point").val(sum+inputted);
        }else{
        	$(".emoney_reg #emoney").val(comma(emoney));
        	$("#paper_reserves").text(" - " + comma(emoney)+ " 원");
        	$(".emoney_point").val(emoney);
        }
      
        product_price();
       
        
    })
    
    //페이머니 모두 사용 클릭시
    $(".paymoney_chkbox").click(function(){
        var sum = Number($('#paper_settlement').text().replace(/[^0-9]/g, ''));
        var inputted = Number($("#paymoney").val().replace(/[^0-9]/g, ''));
  
        if(paymoney>=sum+inputted){
        	$(".emoney_reg #paymoney").val(comma(sum+inputted));
        	$("#paper_reserves2").text(" - " + comma(sum+inputted)+ " 원");
        	$(".paymoney_point").val(sum+inputted);
        }else{
        	$(".emoney_reg #paymoney").val(comma(paymoney));
        	$("#paper_reserves2").text(" - " + comma(paymoney)+ " 원");
        	$(".paymoney_point").val(paymoney);
        }

        product_price();
       
        
    })
    
	//포인트결제창 숫자입력시
	$("#emoney").on("focusout", function() {
    	
	    var value = $(this).val().replace(/[^0-9]/g, '');
	
	    $(this).val(value);
		var already = Number($('#paper_reserves').text().replace(/[^0-9]/g, ''));
		var sum = Number($('#paper_settlement').text().replace(/[^0-9]/g, ''));
	    var input_emoney = Number(value);
	    
	    if (input_emoney >= sum+already) {
	        if (emoney >= sum+already) {
	            input_emoney = sum+already;
	        } else {
	            input_emoney = emoney;
	        }
	    } else {
	        if (input_emoney > emoney) {
	            input_emoney = emoney;
	        }
	    }

	    $("#emoney").val(comma(input_emoney));
	    $("#paper_reserves").text(" - " + comma(input_emoney) + " 원");
	    $(".emoney_point").val(input_emoney);
	    product_price();
	    
	});
	
	//페이머니결제창 숫자입력시
	$("#paymoney").on("focusout", function() {
    
	    var value = $(this).val().replace(/[^0-9]/g, '');
	
	    $(this).val(value);
		var already = Number($('#paper_reserves2').text().replace(/[^0-9]/g, ''));
	    var sum = Number($('#paper_settlement').text().replace(/[^0-9]/g, ''));
	    var input_paymoney = Number(value);
		
	    if (input_paymoney >= sum+already) {
	        if (paymoney >= sum+already) {
	            input_paymoney = sum+already;
	        } else {
	            input_paymoney = paymoney;
	        }
	    } else {
	        if (input_paymoney > paymoney) {
	            input_paymoney = paymoney;
	        }
	    }

	    $("#paymoney").val(comma(input_paymoney));
	    $("#paper_reserves2").text(" - " + comma(input_paymoney) + " 원");
	    $(".paymoney_point").val(input_paymoney);
	    product_price();
	    
	});
	
    product_price();

	
	
});

//넘어온 상품들 가격합해서 결제금액창에 보여주기
function product_price(){

	product_cost = 0;
    delivery_cost = 0;
        
    $('.info_price .price').each(function() {
        
        var priceText = $(this).text();
        var price = parseFloat(priceText.replace(/[^0-9.-]+/g,""));
        product_cost += price;
        delivery_cost += 3000;
	});
	
    $("#productsTotalPrice").text(comma(product_cost));
	$("#paper_delivery").text(comma(delivery_cost));
	
    var emoney_point = Number($(".emoney_point").val());
	var paymoney_point = Number($(".paymoney_point").val());
	
    $("#paper_settlement").text(comma(product_cost + delivery_cost - emoney_point - paymoney_point));

    $("#product_price_value").val(product_cost + delivery_cost - emoney_point - paymoney_point);
    
}

//콤마찍는 함수
function comma(num){
    var len, point, str; 
       
    num = num + ""; 
    point = num.length % 3 ;
    len = num.length; 
   
    str = num.substring(0, point); 
    while (point < len) { 
        if (str != "") str += ","; 
        str += num.substring(point, point + 3); 
        point += 3; 
    } 
     
    return str;
 
}


//다음지도주소찾기
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                    addr += ', ' + extraAddr
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                
            
            } else {
                
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById('postcode').disabled = true;
            document.getElementById('address').disabled = true;
            document.getElementById("detailAddress").focus();
        }
    }).open();
}

function verifyPay() {

    var verifyName = document.getElementById("buyer_name").value;
    var verifyTel = document.getElementById("buyer_tel").value;
    var verifyPostcode = document.getElementById("postcode").value;
    var verifyAddress = document.getElementById("address").value;
    var verifyDetailAddress = document.getElementById("detailAddress").value;

    if (verifyName === "" || verifyTel === "" || verifyPostcode === "" || verifyAddress === "" || verifyDetailAddress === "") {
        alert("배송지정보를 입력해주세요");
    } else {
 		requestPay();
	}
}

function requestPay() {

	var num =0;
	
	$('.name .inner_name').each(function() {
		num=num+1;
	});	
	
	var name = '';
	var nameElements = document.querySelectorAll(".inner_name");
	name = nameElements[0].textContent;
	
	if(num>=2){
		name += '외 '+(num-1)+'건';
	}
	
	var itemList = document.querySelectorAll(".list_product li"); 

	var itemIds = []; //id와 수량들어간 배열
	
	itemList.forEach(function (element) {
	    var itemId = element.getAttribute("data-id");
	    var count = element.getAttribute("data-count");
	    var price = element.getAttribute("data-price");
	    price = price*count;
	    
	    var itemData = {
	        id: itemId,
	        count: count,
	        price: price
    	};
    	itemIds.push(itemData);
	});
	
	//재고 확인하고 결제진행
	$.ajax({
	    type: 'POST',
	    url: '/order/checkStockBeforePay',
	    contentType: 'application/json',
	    data: JSON.stringify(itemIds),
	    success: function (response) {

	        var merchant_uid = '';
			var randomInteger = Math.floor(Math.random() * 9) + 1;	
			var date = new Date();
			
			merchant_uid += date.getFullYear();
			merchant_uid += (date.getMonth()+1);
			merchant_uid += date.getDate()+'-';
			merchant_uid += Date.now();
			merchant_uid += randomInteger;
			
			var amount = $('#paper_settlement').text().replace(/[^0-9]/g, '');
			var buyer_email = $('#buyer_email').text();
			var buyer_name = $('#buyer_name').val();
			var buyer_tel = $('#buyer_tel').val();
			var buyer_postcode = $('#postcode').val();
			var buyer_addr = $('#address').val();
			var buyer_addr_detail = $('#detailAddress').val();
			var request = $('#request').val();
		
			var pointPay = $('#paper_reserves').text().replace(/[^0-9]/g, '');
			var payMoney = $('#paper_reserves2').text().replace(/[^0-9]/g, '');
			
			//결제금액이 있을경우
			if(amount>0){
			
				IMP.init('imp56668363');
			    IMP.request_pay({
			    
				    pg : 'html5_inicis.INIpayTest', //테스트 시 html5_inicis.INIpayTest 기재 
			        merchant_uid: merchant_uid,   // 주문번호
			        name: name,
			        amount: amount,               // 금액(숫자 타입)
			        buyer_email: buyer_email,
			        buyer_name: buyer_name,
			        buyer_tel: buyer_tel,
			        buyer_addr: buyer_addr,
			        buyer_postcode: buyer_postcode
			        
				}, function(rsp) { // callback 로직
					if (rsp.success) {
			            
			            var paymentData = {
			            
				            merchant_uid: rsp.merchant_uid,
				            name: name,
				            paid_amount: rsp.paid_amount,
				            pay_method: rsp.pay_method,
				            apply_num: rsp.apply_num,
				            buyer_name: buyer_name,
				            buyer_tel: buyer_tel,
				            buyer_addr: buyer_addr,
				            buyer_addr_detail: buyer_addr_detail,
				            buyer_postcode: buyer_postcode,
				            request: request,
				            pointPay: pointPay,
				            payMoney: payMoney,
				            itemIds: itemIds,
			       		};
			       		/*
			       		console.log(rsp.merchant_uid);
			            console.log(name);
			            console.log(rsp.paid_amount);
			            console.log(rsp.pay_method);
			            console.log(rsp.apply_num);
			            console.log(buyer_name);
			            console.log(buyer_tel);
			            console.log(buyer_addr);
			            console.log(buyer_addr_detail);
			            console.log(buyer_postcode);
			            console.log(request);
			            console.log(pointPay);
			            console.log(payMoney);
			            console.log(itemIds);
			            */
			            $.ajax({
				            type: "POST",
				            url: "/order/checkout",
				            contentType: "application/json", 
				            data: JSON.stringify(paymentData),
				            success: function (response) {
				                console.log(response);

				            	$('#paymentsData').val(JSON.stringify(response.paymentsData));
			       				$('#resultForm').submit();
				            	
				            },
				            error: function (xhr, status, error) {
						        console.log(xhr.responseText);
						        alert("error");
						    }
			            });
			            
						
			
						
			        } else {
			        	var message = '결제에 실패했습니다.\n'+rsp.error_msg;
			            alert(message);
			        }
					
				});
			
			}else{
			
			    //전부 포인트or페이머니로 결제했을경우
				var paymentData = {
			            
			            merchant_uid: merchant_uid,
			            name: name,
			            paid_amount: amount,
			            pay_method: '페이머니및포인트',
			            apply_num: '',
			            buyer_name: buyer_name,
			            buyer_tel: buyer_tel,
			            buyer_addr: buyer_addr,
			            buyer_addr_detail: buyer_addr_detail,
			            buyer_postcode: buyer_postcode,
			            request: request,
			            pointPay: pointPay,
			            payMoney: payMoney,
			            itemIds: itemIds,
		       		};
		            
		            $.ajax({
			            type: "POST",
			            url: "/order/checkout",
			            contentType: "application/json", 
			            data: JSON.stringify(paymentData),
			            success: function (response) {
			                console.log(response);
	
			            	$('#paymentsData').val(JSON.stringify(response.paymentsData));
		       				$('#resultForm').submit();
		
			            },
			            error: function (xhr, status, error) {
					        console.log(xhr.responseText);
					        alert("error");
		
		   
					    }
		            });
			
			}

	    },
	    error: function (xhr, status, error) {

	        console.error(error);
	        alert('재고가 부족합니다 장바구니에서 재고확인후 \n다시 확인후 시도해주세요');
	        
	    }
	});
	

  }




