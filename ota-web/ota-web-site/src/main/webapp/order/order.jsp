<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="${picctx}/${company.ICON}">
	<title>${company.TITLE}</title>
	<%@ include file="../commons/meta-css.jsp"%>
	<link rel="stylesheet" href="${ctx}/resources/js/bootstrap/dialog/css/bootstrap-dialog.css" />
	<link rel="stylesheet" href="${ctx}/resources/css/index.css">
	<link rel="stylesheet" href="${ctx}/resources/css/calendar.css">
	<link rel="stylesheet" href="${ctx}/resources/css/yuding.css">
	
	<script type="text/javascript" src="${ctx}/resources/js/Calendar.js"></script>
	<script type="text/javascript">var tcalendar = eval('${calendarJson}');</script>
	
	<script type="text/javascript" src="${ctx}/resources/js/fun.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<%@ include file="../commons/meta-js.jsp"%>
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap/dialog/js/bootstrap-dialog.js"></script>
	<script type="text/javascript">
		function get_baseinfo(usercode){
			var date = new Date();
			var year = date.getFullYear(); 
			var birthday_year = parseInt(usercode.substr(6,4));
			var userage = year - birthday_year;
			return userage
		}
		
		function removeContact(t){
			$(t).parent().parent().remove();
		}
		
		var plan_json = '${plan_json}';
		$(function () {
			
			$('#addContact').click(function(){
				var h = '';
				h+='<tr>';
					h+='<td>&nbsp;</td>';
					h+='<td class="col-xs-1 contact">姓名：</td>';
					h+='<td class="col-xs-5"><input class="form-control" name="contact_name" type="text" placeholder="请输入联系人姓名"></td>';
					h+='<td class="col-xs-1 contact">手机：</td>';
					h+='<td class="col-xs-5"><input class="form-control" name="contact_phone" type="text" placeholder="请输入联系人手机"></td>';
					h+='<td><a href="javascript: void(0);" target="_self" onclick="removeContact(this)" class="order-add" title="删除"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>';
				h+='</tr>';
				$('#addContact').parent().parent().before(h);
			});
				
			$('[data-toggle="tooltip"]').tooltip();
			
			$('.addVisitor').click(function(){
				addVisitor();
			});
				
			$('.storeRadio').click(function(){
				var storeId = $(this).val();
				$.ajax({  
					type: "post",
					url:'${ctx}/store/counselors?storeId='+storeId,
					dataType:"json",
					success: function(d, textStatus){
						var h = '';
						for(var i = 0; i < d.data.length; i++){
							var c = (i == 0 ? 'checked="checked"' : '')
							h+='<tr>';
							h+='<td><div class="radio"><label><input class="counselorRadio" type="radio" name="counselor" '+c+'value="'+d.data[i].ID+'"></label></div></td>';
							h+='<td class="col-xs-4"><img src="${picctx}/'+d.data[i].FACE+'" width="60" class="img-circle trver-img"></td>';
							h+='<td class="col-xs-6">'+d.data[i].CHINA_NAME+'</td>';
							h+='<td class="col-xs-2"><span class="glyphicon glyphicon-phone" aria-hidden="true"></span>'+d.data[i].MOBILE+'</td>';
							h+='</tr>';
						}
						$('#counselors').html(h);
					}
				});
			});
		
			$('#chooseDate').click(function(){
				var s = $('#calendarcontainer').parent().css('display')
				if(s == 'none'){
					$('#calendarcontainer').parent().show();
				}else{
					$('#calendarcontainer').parent().hide();
				}
			});	
			
			$('#SINGLE_ROOM_CNT').blur(function(){},function(){
				var SINGLE_ROOM_CNT = $('#SINGLE_ROOM_CNT').val();
				if(isNaN(SINGLE_ROOM_CNT)){
					$('#SINGLE_ROOM_CNT').val(0);
				}
				$('#SINGLE_ROOM_CNT').val(parseInt(SINGLE_ROOM_CNT));
			});
			
			$('#trafficplan li').click(function(){
				setTimeout(function(){c();},500);
			});
			$('.subBtn').click(function(){
			
// 				var muster = $(".pricepanel:visible input[type='radio'][name='muster']:checked").val();
// 				var contact = $(".visitor input[type='radio'][name='visitor_radio']:checked").val();
				
				var SINGLE_ROOM_CNT = $('#SINGLE_ROOM_CNT').val();
				var vl = $(".visitor input[name='visitor_name']").length;
				if(isNaN(SINGLE_ROOM_CNT)){
					$('#roomTipContent').html('单房差数量填写错误');
					$('#roomTipModal').modal('show');
					return false;
				}
				
				SINGLE_ROOM_CNT = parseInt(SINGLE_ROOM_CNT);
// 				if(vl == 1 && SINGLE_ROOM_CNT < 1){
// 					//单房差必选为1
// 					$('#roomTipContent').html('如果游客为一人，单房差必填');
// 					$('#roomTipModal').modal('show');
// 					return false;
// 				}				
				
				if(vl < SINGLE_ROOM_CNT){
					$('#roomTipContent').html('单房差数量超出游客数量');
					$('#roomTipModal').modal('show');
					return false;
				}

				var b = false;
				$(".visitor input[name='visitor_name']").each(function(){
					if($(this).val() == ''){
						b = false;
						return;
					}else{
						b = true;
					}
				});
				
				var visitor_id_nos = $(".visitor input[name='visitor_id_no']");
				for(var i = 0; i < visitor_id_nos.length; i++){
					if(visitor_id_nos[i] == '' || visitor_id_nos[i] == null || !idcard(visitor_id_nos[i].value)){
						$('#tipContent').html('请填写正确的身份证号');
						$('#tipModal').modal('show');
						return false;	
					}
					if(get_baseinfo(visitor_id_nos[i].value) > 65){
						$('#tipContent').html('该产品价格有变，请联系客服');
						$('#tipModal').modal('show');
						return false;	
					}
				}
				
				var contact_names = $("input[name='contact_name']");
				if(contact_names.length == 0){
					$('#tipContent').html('请填写联系人姓名');
					$('#tipModal').modal('show');
					return false;
				}else{
					for(var i = 0; i < contact_names.length; i++){
						if(contact_names[i].value == null || contact_names[i].value == ''){
							$('#tipContent').html('请填写联系人姓名');
							$('#tipModal').modal('show');
							return false;
						}
					}
				}
				
				var contact_phones = $("input[name='contact_phone']");
				if(contact_phones.length == 0){
					$('#tipContent').html('请填写联系人电话');
					$('#tipModal').modal('show');
					return false;
				}else{
					for(var i = 0; i < contact_phones.length; i++){
						if(contact_phones[i].value == null || contact_phones[i].value == ''){
							$('#tipContent').html('请填写联系人电话');
							$('#tipModal').modal('show');
							return false;
						}
					}
				}
				
// 				if(!muster || !contact || !b){
				if(!b){
					$('#tipContent').html('请填写游客信息，填写联系人信息。');
					$('#tipModal').modal('show');
					return false;	
				}
				
				if('${webUser}' != null && '${webUser}' != ''){

				}else{
					var dialog = new BootstrapDialog({
			            message: function(dialogRef){
			                var $message = $('<div></div>').load('${ctx}/user/to/mini/login');
			                return $message;
			            },
			            closable: false,
			            onshown: function(){
			            	$('.modal-dialog').css('padding', $('.m-navbar').css('height')+' 0 0 0');
			            }
			        });
			        dialog.realize();
			        dialog.getModalHeader().hide();
			        dialog.getModalFooter().hide();
			        dialog.getModalBody().css('padding', '0');
			        dialog.open();
			        return false;
				}
			
				$('#orderForm').submit();
				
			});
			
			$('#showPeopleInfo').click(function(){
				var c = 0, h = '',name = new Array(), sex = new Array(), mobile = new Array(), visitor_type = new Array(), visitor_id = new Array(), visitor_id_no = new Array();
			    var data = [['visitor_name', name],  ['visitor_sex_name', sex],  ['visitor_mobile', mobile], ['visitor_type_name', visitor_type], ['visitor_id',visitor_id], ['visitor_id_no', visitor_id_no]];
				getVisitorInfo(data);
				for(var i = 0; i < name.length; i++){
					h += '<table class="table m-tabpeople"><tr><td class="col-xs-4">'+name[i]+'</td><td class="col-xs-2">'+sex[i]+'</td><td class="col-xs-6">'+mobile[i]+'</td></tr>';
	          	  	h += '<tr><td class="col-xs-4">'+visitor_id[i]+'</td><td colspan="2" class="col-xs-8">'+visitor_id_no[i]+'</td></tr>';
	          	  	h += '<tr><td colspan="3" class="col-xs-12">'+visitor_type[i]+'</td></tr></table>';	
				} 
				
				$('#visitor_infos').html(h);
				$('#people-info').modal('show');
			});
			
			$('.plantilte').click(function(){
		  		$('#plan_id').val($(this).id);
		  	});
		  	
		  	$('.showPriceInfo').click(function(){
		  		var vt = [['CHENGREN', '成人', 0, 0], ['ERTONG', '儿童', 0, 0]];
		  		for(var i = 0; i < vt.length; i++){
		  			vt[i][2] = $('#'+vt[i][0]).val();
		  			if(!$('#'+vt[i][0]).val()){
		  				vt[i][2] = 0;	
		  			}
		  			
		  		}
		  		$('input[name="visitor_type_attr"]').each(function(){
					for(var l = 0; l < vt.length; l++){
						if($(this).val() == vt[l][0]){
							vt[l][3] += 1;
						}
					}
				});
				var h = '';
				h += '<table class="table m-tabprice"><tr><td align="right"><span class="fl">成人</span><em class="cny">&yen;'+vt[0][2]+'</em><b>×'+vt[0][3]+'</b></td></tr></table>';
	          	h += '<table class="table m-tabprice"><tr><td align="right"><span class="fl">儿童</span><em class="cny">&yen;'+vt[1][2]+'</em><b>×'+vt[1][3]+'</b></td></tr></table>';
	          	$('#priceList').html(h)
				$('#price-info').modal('show');
			});
			
			for(var i = 0; i < Number('${param.manCnt}'); i++){
				addVisitor('成人');
			}
			for(var i = 0; i < Number('${param.childCnt}'); i++){
				addVisitor('儿童');
			}
		});	
		
		function addVisitor(visitorType){
			var h = '';
			if(USER_AGENT == 'mobile'){
				$('.toolbar-tip').hide();
				var name = $('#visitor_name_t').val(), mobile = $('#visitor_mobile_t').val(), visitor_type = $('#visitor_type_t').find('option:selected').text(), visitor_sex_name = $('#visitor_sex_t').find('option:selected').text();
				if(name == null || name == ''){
					$('.toolbar-tip').show();
					return false;
				}
				h += '<tr class="vd visitor"><td><table class="m-table">';
				h += '<tr><td rowspan="3" class="col-xs-1"><label><input type="radio" name="visitor_radio" onclick="chooseContact(this);"><input class="visitor_contact" type="hidden" name="visitor_contact" value=""></label></td>';
				h += '<td class="col-xs-4">'+name+'</td>';
				h += '<td class="col-xs-6">'+visitor_type+'</td>';
				h += '<td rowspan="3" class="col-xs-1"><a href="javascript: void(0);" target="_self" onclick="removeVisitor(this);" class="order-add" title="删除"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td></tr>';
				if(mobile != null && mobile != ''){
					h += '<tr><td colspan="2" class="col-xs-10">'+mobile+'</td></tr>';
				}
				h += '<input type="hidden" name="visitor_name" value="'+name+'"/>';
				h += '<input type="hidden" name="visitor_sex" value="'+$('#visitor_sex_t').val()+'" />';
				h += '<input type="hidden" name="visitor_sex_name" value="'+visitor_sex_name+'" />';
				h += '<input type="hidden" name="visitor_type" value="'+$('#visitor_type_t').find('option:selected').val()+'" />';
				h += '<input type="hidden" name="visitor_type_name" value="'+visitor_type+'" />';
				h += '<input type="hidden" name="visitor_type_attr" value="'+$('#visitor_type_t').find('option:selected').attr('atype')+'" />';
				h += '<input type="hidden" name="visitor_mobile" value="'+$('#visitor_mobile_t').val()+'"/>';
				h += '<input type="hidden" name="visitor_id" value="'+$('#visitor_id_t').val()+'"/>';
				h += '<input type="hidden" name="visitor_id_no" value="'+$('#visitor_id_no_t').val()+'"/>';
				h += '</table></td></tr>';
				$('.addV').before(h);
				$('#addVisitordialog').modal('hide');
				
			}else{
				h += '<tr class="visitor vd">';
				h += '<td><div class="radio"></div></td>';
				h += '<td><input class="form-control" type="text" name="visitor_name" id="visitor_name" placeholder="姓名"></td>';
				h += '<td><select class="form-control" name="visitor_sex"><option value="1">男</option><option value="0">女</option></select></td>';
				h += '<td><select class="form-control" name="visitor_type" onchange="c()">';
				
				if(visitorType && visitorType == '成人'){
					h += '<option selected="selected" value="0FA5123749CF8C87E050007F0100BCAD">成人</option>';
				}else{
					h += '<option value="0FA5123749CF8C87E050007F0100BCAD">成人</option>';
				}
				
				if(visitorType && visitorType == '儿童'){
					h += '<option selected="selected" value="2D80E611DBA35261E050007F0100ED30">儿童</option>';
				}else{
					h += '<option value="2D80E611DBA35261E050007F0100ED30">儿童</option>';
				}

				h += '</select></td>';
				
				h += '<td><input class="form-control" type="text" name="visitor_mobile" maxlength="11" placeholder="手机号码"></td>';
				h += '<td><select class="form-control" name="visitor_id"><option value="身份证">身份证</option></select></td>';
				h += '<td><input class="form-control" name="visitor_id_no" type="text" placeholder="证件号码"></td>';
				h += '<td><a href="javascript: void(0);" target="_self" onclick="removeVisitor(this);" class="order-add" title="删除"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>';
				h += '</tr>';
				$('#visitorTitle').after(h);
			} 
			c();
		}
			
							
		function getVisitorInfo(a){
			var c = 0;
			for(var i = 0; i < a.length; i++){
				$('input[name="'+a[i][0]+'"]').each(function(){
					a[i][1][c] = $(this).val();
					c++;
				});
				c = 0;
			}
		}
		
		
		function chooseContact(t){
			$('.visitor_contact').val('');
			$(t).next().val('YES');
		}
			
		function c(){
			var sum_price = 0;
			if(USER_AGENT == 'mobile'){
				$('input[name="visitor_type_attr"]').each(function(){
					sum_price += parseInt($('#'+$(this).val()).val());
				});
				$('.sumPrice').html(sum_price);
			}else{
				var vn = [0,0];
				$(".visitor td:nth-child(4) option:selected").each(function(){
					vn[$(this).index()] = vn[$(this).index()]+1;
				});
				for(var i = 0; i < vn.length; i++){
					var h = '';
					h = '<em class="cny">&yen;'+($('.pricepanel:visible .priceInfo td:nth-child('+(i+1)+')').attr('price'))+'</em><b>×'+vn[i]+'</b>';
					
					sum_price += ($('.pricepanel:visible .priceInfo td:nth-child('+(i+1)+')').attr('price') * vn[i]);
					
					$('.pricepanel:visible .priceInfo td:nth-child('+(i+1)+')').html(h);
				}
				$('.defary-total').html('总计：&yen;<em>'+sum_price+'</em>')
			}
		}
		
		function removeVisitor(t){
			$(t).parents('.vd').remove();
			c();
		}
		
	</script>
	<c:if test="${USER_AGENT eq 'pc'}"><base target="_blank"></c:if>
</head>
<body>
	<c:set var="header_title" value="线路预定" />
	<%@ include file="../commons/header.jsp"%>
	<!-- 订单 -->
	<div class="main-box container">
		<!-- 流程 -->
		<div class="progress process">
			<div class="progress-bar progress-bar-success" style="width: 25%">
				<span>选择产品</span>
				<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
			</div>
			<div class="progress-bar progress-bar-success" style="width: 25%">
				<span>填写与核对</span>
				<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
			</div>
			<div class="progress-bar progress-bar-default" style="width: 25%">
				<span>在线支付</span>
				<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
			</div>
			<div class="progress-bar progress-bar-default" style="width: 25%">
				<span>付款成功</span>
				<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
			</div>
		</div>
		${order}
		<!-- 订单内容 -->
		<div class="panel panel-success" style="margin-bottom: 60px;">
			<form action="${ctx}/order/save" class="form-horizontal" id="orderForm" method="post" target="_self">
			<div class="panel-heading">
				<h1>线路信息</h1>
			</div>
			<div class="panel-body">
				<dl class="dl-horizontal">
					<dt class="col-sm-2 col-xs-4">线路名称：</dt>
					<dd class="col-sm-10 col-xs-8" style="margin-left:0; padding-left:0;"><a href="${ctx}/produce/route/detail?id=${param.routeId}" class="order-link">${ROUTE_TITLE}</a></dd>
					<dt class="col-sm-2 col-xs-4 mt15">出发城市：</dt>
					<dd class="col-sm-10 col-xs-8 mt15" style="margin-left:0; padding-left:0;">${BEGIN_CITY}</dd>
					<dt class="col-sm-2 col-xs-4 form-dt mt15">出游日期：</dt>
					<dd class="col-sm-10 col-xs-8 mt15" style="margin-left:0; padding-left:0;">
						<div class="calendar">
							<div>
								<input type="text" value="${param.d}">
								<a href="javascript: void(0);" target="_self" class="label label-success" id="chooseDate" data-toggle="modal" data-target=".mini-cal" style="padding:5px 8px;">更换</a>
							</div>
						</div>
					</dd>
				</dl>
			</div>
			
			<!-- 日历选择 -->
			<div class="calendar" style="display:none;">
				<div class="outer clearfix" id="calendarcontainer"> 
				</div>
			</div>
			<fmt:parseDate value="${param.d}" var="date"/><fmt:formatDate value="${date}" pattern="yyyy" var="start_year"/><fmt:formatDate value="${date}" pattern="MM" var="start_month"/><fmt:formatDate value="${date}" pattern="yyyy-MM-dd" var="start_date"/>
			<script type="text/javascript">	
				var f = 1;
				if('${USER_AGENT}' == 'mobile'){f=0;}
				//c:容器,y:年,m:月,a:出发时间json,f:是否显示双日历,fu:回调调 d1:最早时间,d2:最晚时间,f:显示双日历用1，单日历用0;
				//clickfu: //回调函数，to为点击对象，点击日期是调用的函数,参数to为点击的日期的节点对象，可以把用户选定的日期通过此函数存入服务端或cookies，具体请自行编写
				//showFu: //回调函数，d为要显示的当前日期，主要用于判断是否要在该日期的格子里显示出指定的内容，在日期格子里额外显示内容的函数,返回值必须为字符串，参数d为显示的日期对象（日期类型）
				var para = {'c':'calendarcontainer','y':${start_year},'m':${start_month},'a':{'d1':'${param.d}','d2':'2020-05-05'},'f':f,
					'clickfu':function (to) {
				    	if($(to).attr('price')!=""){       
							document.location.href = '${ctx}/order/to/save?routeId=${param.routeId}&routeTitle=${ROUTE_TITLE}&beginCity=${BEGIN_CITY}&d='+to.id;
						}	 
					},
					'showFu':function(d){	
						var t=new Date();
							if(t.toLocaleDateString()==d.toLocaleDateString()){		
								return "<br/>今天";
						}else{
							return "";	 
						}
					}
				}
				CreateCalendar(para,"para",'',tcalendar); //参数前一个是对象，后一个是对象名称
			</script>
			<div class="panel-heading">
				<h1 class="order-tit">顾问信息</h1>
			</div>
			<table class="table table-striped" id="counselors">
				<c:forEach items="${counselors}" var="counselor" varStatus="i">
				<tr>
					<td><div class="radio"><label><input class="counselorRadio" type="radio" name="counselor" <c:if test="${i.index eq 0}">checked="checked"</c:if> value="${counselor.ID}"></label></div></td>
					<td class="col-sm-4"><img src="${picctx}/${counselor.FACE}" width="60" class="img-circle trver-img"></td>
					<td class="col-sm-6">${counselor.CHINA_NAME}</td>
					<td class="col-sm-2"><span class="glyphicon glyphicon-phone" aria-hidden="true"></span>${counselor.MOBILE}</td>
				</tr>
				</c:forEach>
			</table>
			<div class="panel-heading">
				<h1>费用信息</h1>
			</div>
			<c:if test="${USER_AGENT eq 'pc'}">
			<div class="panel-body">
			  	<ul class="nav nav-tabs navbar-right tag-city" id="trafficplan" role="tablist">
			  		<c:forEach items="${plans}" var="plan" varStatus="i">
			  		<c:set var="plan_active" value="" />
			  		<c:if test="${i.index eq 0}">
			  			<c:set var="plan_id" value="${plan.ID}" />
			  			<c:set var="plan_active" value="active" />
			  		</c:if>
			    	<li role="presentation" id="${plan.ID}" class="plantilte ${plan_active}"><a href="#route${i.index + 1}" target="_self" aria-controls="home" role="tab" data-toggle="tab">&nbsp;${plan.PLAN_TITLE}</a></li>
			    	</c:forEach>
			  	</ul>
			  	<div class="tab-content">
			  		
			  		<c:forEach items="${plans}" var="plan" varStatus="i">
				    <div role="tabpanel" id="${plan.ID}" class="pricepanel tab-pane <c:if test="${i.index eq 0}">active</c:if>" id="route${i.index + 1}">
				    	<table class="table table-bordered table-price">
							<tr>
								<th colspan="3">成人</th>
								<th colspan="3">儿童</th>
							</tr>
							<c:forEach items="${plan.planPrices}" var="planPrice">
							<c:if test="${planPrice.ID eq '0FA5123749D28C87E050007F0100BCAD'}">
							<tr class="priceInfo">
								<td colspan="3" price="${planPrice.CHENGREN}"><em class="cny">&yen;${planPrice.CHENGREN}</em><b>×0</b></td>
								<td colspan="3" price="${planPrice.ERTONG}"><em class="cny">&yen;${planPrice.ERTONG}</em><b>×0</b></td>
							</tr>
							</c:if>
							</c:forEach>
							<c:forEach items="${plan.trafficMusters}" var="muster" varStatus="i">
							<tr class="active">
								<td colspan="1"><input type="radio" name="muster" value="${muster.MUSTER_TIME},${muster.MUSTER_PLACE}" /></td>
								<td colspan="2">集合时间：${muster.MUSTER_TIME}</td>
								<td colspan="3">集合地点：${muster.MUSTER_PLACE}</td>
							</tr>
							</c:forEach>
							
							<c:forEach items="${plan.planInfos}" var="planInfo">
							<tr>
								<td colspan="2">交通名称：${planInfo.TRAFFIC_NAME}</td>
								<td colspan="2">出发日期：${fn:substring(planInfo.DATE, 0, 4)}-${fn:substring(planInfo.DATE, 4, 6)}-${fn:substring(planInfo.DATE, 6, 8)}</td>
								<td colspan="1">停留天数：${planInfo.STAY_CNT}</td>
								<td colspan="1">剩余座位：${planInfo.SEAT}</td>
							</tr>
							</c:forEach>
							<tr>
								<td colspan="2">单房差：<em class="cny">&yen;${route.RETAIL_SINGLE_ROOM}</em></td>
								<td colspan="4" style="line-height: 28px;">
									<div class="input-group input-group-sm col-md-2 col-xs-6">
										<input type="text" class="form-control" placeholder="单房差个数" value="0" name="SINGLE_ROOM_CNT" id="SINGLE_ROOM_CNT">
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="6">
									<div class="defary-total">总计：&yen;<em>0</em></div>
								</td>
							</tr>
						</table>
				    </div>
				    </c:forEach>
			  	</div>
			</div>
			
			<div class="panel-heading">
				<h1 class="order-tit">旅客信息</h1>
				<input type="hidden" name="start_date" value="${param.d}" />
				<input type="hidden" name="plan_id" value="${plan_id}" />
				<input type="hidden" name="routeId" value="${param.routeId}" />
				<input type="hidden" name="start_city" value="${BEGIN_CITY}" />
				<input type="hidden" name="end_city" value="${END_CITY}" />
				<p class="order-dis"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>&nbsp;请选择一位作为联系人</p>
			</div>
			<table class="table table-striped">
				<tr id="visitorTitle">
					<th>&nbsp;</th>
					<th class="col-xs-2">姓名</th>
					<th class="col-xs-1">性别</th>
					<th class="col-xs-2">游客类型</th>
					<th class="col-xs-2">手机</th>
					<th class="col-xs-2">证件类型</th>
					<th class="col-xs-3">证件号码</th>
					<th>&nbsp;</th>
				</tr>
				<tr>
					<td colspan="8" align="right">
						<a href="javascript: void(0);" target="_self" class="addVisitor order-add"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>&nbsp;添加一位旅客</a>
					</td>
				</tr>
			</table>
			
			</c:if>
			
			<c:if test="${USER_AGENT eq 'mobile'}">
			<!-- mobile -->
			<div class="panel-body">
			  	<ul class="nav nav-tabs navbar-right tag-city" id="trafficplan" role="tablist">
			  		<c:forEach items="${plans}" var="plan" varStatus="i">
			  		<c:set var="plan_active" value="" />
			  		<c:if test="${i.index eq 0}">
			  			<c:set var="plan_id" value="${plan.ID}" />
			  			<c:set var="plan_active" value="active" />
			  		</c:if>
			    	<li role="presentation" id="${plan.ID}" class="plantilte ${plan_active}"><a href="#route${i.index + 1}" target="_self" aria-controls="home" role="tab" data-toggle="tab">&nbsp;${plan.PLAN_TITLE}</a></li>
			    	</c:forEach>
			  	</ul>
			  	<div class="tab-content">
			  		
			  		<c:forEach items="${plans}" var="plan" varStatus="i">
				    <div role="tabpanel" class="pricepanel tab-pane <c:if test="${i.index eq 0}">active</c:if>" id="route${i.index + 1}">
				    	<table class="table table-bordered table-price">
				    		<c:forEach items="${plan.planPrices}" var="planPrice">
							<c:if test="${planPrice.ID eq '0FA5123749D28C87E050007F0100BCAD'}">
							<tr>
								<td class="col-xs-1">&nbsp;</td>
								<td class="col-xs-8">
								成人价：<em class="cny">&yen;${planPrice.CHENGREN}</em>
								<input type="hidden" name="CHENGREN" id="CHENGREN"  value="${planPrice.CHENGREN}" />
								<input type="hidden" name="ERTONG" id="ERTONG" value="${planPrice.YINGER}" />
								</td> 
								<td class="col-xs-3"><a href="javascript: void(0);" class="label label-success showPriceInfo" data-toggle="modal">价格明细</a></td>
							</tr>
							</c:if>
							</c:forEach>
							<c:forEach items="${plan.trafficMusters}" var="muster" varStatus="i">
							<tr class="active">
								<td><input type="radio" name="muster" value="${muster.MUSTER_TIME},${muster.MUSTER_PLACE}" /></td>
								<td colspan="2">
									<p>集合时间：${muster.MUSTER_TIME}</p>
									<p>集合地点：${muster.MUSTER_PLACE}</p>
								</td>
							</tr>
							</c:forEach>
							<c:forEach items="${plan.planInfos}" var="planInfo">
							<tr>
								<td>&nbsp;</td>
								<td colspan="2">
									<p>交通名称：${planInfo.TRAFFIC_NAME}</p>
									<p>出发日期：${fn:substring(planInfo.DATE, 0, 4)}-${fn:substring(planInfo.DATE, 4, 6)}-${fn:substring(planInfo.DATE, 6, 8)}</p>
									<p>停留天数：${planInfo.STAY_CNT}</p>
									<p>剩余座位：${planInfo.SEAT}</p>
								</td>
							</tr>
							<tr>
								<td colspan="2">单房差：<em class="cny">&yen;${route.RETAIL_SINGLE_ROOM}</em></td>
								<td colspan="4" style="line-height: 28px;">
									<div class="input-group input-group-sm col-md-2 col-xs-6">
										<input type="text" class="form-control" placeholder="单房差个数" value="0" name="SINGLE_ROOM_CNT" id="SINGLE_ROOM_CNT">
									</div>
								</td>
							</tr>
							</c:forEach>
							<tr class="active">
								<td colspan="3">
									<div class="defary-total">总计：&yen;<em class="sumPrice">0</em></div>
								</td>
							</tr>
						</table>
				    </div>
				    </c:forEach>
			  	</div>
			</div>
			<!-- mobile -->
			<div class="panel-heading">
				<h1 class="order-tit">旅客信息<a href="#" class="label label-success m-odrpeople" data-toggle="modal" id="showPeopleInfo">游客列表</a></h1>
				<input type="hidden" name="start_date" value="${param.d}" />
				<input type="hidden" name="plan_id" value="${plan_id}" />
				<input type="hidden" name="routeId" value="${param.routeId}" />
				<input type="hidden" name="start_city" value="${BEGIN_CITY}" />
				<input type="hidden" name="end_city" value="${END_CITY}" />
				<p class="order-dis"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>&nbsp;请选择一位作为联系人</p>
			</div>
			<div class="modal fade" id="people-info" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
			  <div class="modal-dialog modal-sm">
			      <div class="modal-content">
			          <div class="modal-header">
			          	  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
			          	  <h2 class="modal-title" id="mySmallModalLabel">游客列表</h2>
			          </div>
			          <div class="modal-body m-odrint" id="visitor_infos"></div>
			      </div>
			    </div>
			</div>
			<table class="table table-striped">
				<tr class="addV">
					<td align="right"><button type="button" class="btn btn-success" data-toggle="modal" data-target=".add-people"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>&nbsp;添加一位旅客</button></td>
				</tr>
			</table>
			<div class="modal fade add-people" tabindex="-1" role="dialog" id="addVisitordialog" aria-labelledby="mySmallModalLabel">
			  <div class="modal-dialog modal-sm">
			      <div class="modal-content">
			          <div class="modal-header">
			          	  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
			          	  <h2 class="modal-title" id="mySmallModalLabel">游客信息</h2>
			          </div>
			          <div class="modal-body m-odrint">
			          		<div class="toolbar-tip" style="display:none;">
						      	<div class="tooltip-arrow"></div>
						      	<div class="tooltip-inner toolbar-danger">
						      		<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>&nbsp;请输入游客姓名！					
						      	</div>
						    </div>
							<input class="form-control" type="text" id="visitor_name_t" placeholder="姓名">
							<select class="form-control" id="visitor_sex_t"><option value="1">男</option><option value="0">女</option></select>
							<select class="form-control" id="visitor_type_t" onchange="c()"><option atype="CHENGREN" value="0FA5123749CF8C87E050007F0100BCAD">成人</option><option atype="ERTONG" value="2D80E611DBA35261E050007F0100ED30">儿童</option></select>
							<input class="form-control" type="text" id="visitor_mobile_t" maxlength="11" placeholder="手机号码">
							<select class="form-control" id="visitor_id_t"><option value="身份证">身份证</option></select>
							<input class="form-control" id="visitor_id_no_t" type="text" placeholder="证件号码">
							<button type="button" class="btn btn-sign btn-block btn-sm addVisitor">确定</button>
			          </div>
			      </div>
			    </div>
			</div>
			</c:if>
			
			<div class="panel-heading">
				<h1>联系人信息</h1>
			</div>
			<table class="table table-striped">
				<tr>
					<td>&nbsp;</td>
					<td class="col-xs-2 contact">姓名：</td>
					<td class="col-xs-4"><input class="form-control contact" name="contact_name" type="text" placeholder="姓名"></td>
					<td class="col-xs-2 contact">手机：</td>
					<td class="col-xs-4"><input class="form-control contact" name="contact_phone" type="text" placeholder="手机"></td>
					<td><a href="javascript: void(0);" target="_self" onclick="removeContact(this)" class="order-add" title="删除"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>
				</tr>
				<tr>
					<td colspan="6" align="right">
						<a href="javascript: void(0);" id="addContact" target="_self" class="order-add"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>&nbsp;添加一位联系人</a>
					</td>
				</tr>
			</table>
			
			<c:if test="${USER_AGENT eq 'pc'}">
			<div class="defary">
				<button type="button" class="subBtn btn btn-sign btn-block btn-lg">同意以下条款，提交订单</button>
			</div>
			</c:if>
			<div class="panel-heading">
				<h1>预定须知</h1>
			</div>
			<div class="panel-body">
				<div class="order-info">
					<p class="strong">费用说明</p>
					<p class="sectit">服务包含：</p>
					<c:forEach items="${route.include}" var="o">
					<p>${o.TITLE}：${o.CONTENT}</p>
					</c:forEach>
					<p class="sectit">服务不含：</p>
					<c:forEach items="${route.noclude}" var="o">
					<p>${o.TITLE}：${o.CONTENT}</p>
					</c:forEach>
					<p class="strong">重要提醒 </p>
					<c:forEach items="${notice}" var="o">
					<p>${o.TITLE}：${o.CONTENT}</p>
					</c:forEach>
					<p class="sectit">温馨提示</p>
					<c:forEach items="${tips}" var="o">
					<p>${o.TITLE}：${o.CONTENT}</p>
					</c:forEach>
				</div>
			</div>
			</form>
		</div>
		
		<div id="tipModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="myModalLabel">友情提示</h3>
					</div>
					<div class="modal-body">
						<h3 id="tipContent">请选择集合地点，填写游客信息，选择联系人。</h3>
					</div>
				</div>
			</div>
		</div>
		
		<div id="roomTipModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title" id="myModalLabel">友情提示</h3>
					</div>
					<div class="modal-body">
						<h3 id="roomTipContent"></h3>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- mobile -->
	<c:if test="${USER_AGENT eq 'mobile'}">
	<div class="bottom-tour" style="padding: 0 10px;">
		<ul>
			<li style="width:60%">
				<div class="m-odrtotal">总计：&yen;<em class="sumPrice">0</em></div>
				<a href="javascript: void(0);" class="m-odrprice label label-success showPriceInfo" data-toggle="modal">价格明细</a>
			</li>
			<li style="width:40%"><a href="javascript:void(0)" target="_self" class="subBtn bottom-orderbtn order-btn">提交订单</a></li>
		</ul>
	</div>
	<div class="modal fade" id="price-info" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
	  <div class="modal-dialog modal-sm">
	      <div class="modal-content">
	          <div class="modal-header">
	          	  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	          	  <h2 class="modal-title" id="mySmallModalLabel">价格明细</h2>
	          </div>
	          <div class="modal-body m-odrint" id="priceList">
	          	  
	          </div>
	      </div>
	    </div>
	</div>
	</c:if>
</body>    
</html>
