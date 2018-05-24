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
	<%@ include file="../../commons/meta-css.jsp"%>
	<link rel="stylesheet" href="${ctx}/resources/css/user.css">
	<link rel="stylesheet" href="${ctx}/resources/css/yuding.css">
	<%@ include file="../../commons/meta-js.jsp"%>
	<c:if test="${USER_AGENT eq 'pc'}"><base target="_blank"></c:if>
</head>
<body>
<c:set var="header_title" value="订单详情" />
<c:if test="${USER_AGENT eq 'mobile'}">
<%@ include file="../../commons/header.jsp"%>
</c:if>
<div class="usr-box">
	<h2><span>订单详情</span></h2>
	<div class="paypal">
		<div class="paypal-info" style="padding-top: 20px;">
			<p class="col-sm-6 col-xs-12">
				<b>线路名称：<a href="${ctx}/produce/route/detail?id=${order.PRODUCE_ID}&routeType=${order.PRODUCETYPE}" class="order-link">${order.PRODUCE_NAME}</a></b>
			</p>
			<p class="col-sm-6 col-xs-12">
				<b>${order.START_CITY}&nbsp;<span class="glyphicon glyphicon-refresh" aria-hidden="true" style="padding-bottom: 0;"></span>&nbsp;${order.END_CITY}</b>
			</p>
			<p class="col-sm-6 col-xs-12">
				<b>订单编号：${order.NO}</b>
			</p>
			<p class="col-sm-6 col-xs-12">
				<b>合同号：${order.CON_NO}</b>
			</p>
			<p class="col-sm-6 col-xs-12">
				<b>单房差：<b class="cny">&yen;${order.RETAIL_SINGLE_ROOM}</b>×${order.SINGLE_ROOM_CNT}个</b>
			</p>
		</div>
		<div class="paypal-info usr-odr">
			<c:forEach items="${traffics}" var="traffic">
			<c:if test="${traffic.IS_FULL_PRICE ne '1'}">
			<span class="col-sm-6 col-xs-12">交通名称：${traffic.PRODUCE_NAME}</span>
			</c:if>
			<span class="col-sm-6 col-xs-12">出发日期：${traffic.EXCA}</span>
			</c:forEach>
		</div>
		<div class="paypal-info usr-odr">
			<c:if test="${not empty order.MUSTER_PLACE}">
			<span class="col-sm-6 col-xs-12">集合地点：${order.MUSTER_PLACE}</span>
			<span class="col-sm-6 col-xs-12">集合时间：${order.MUSTER_TIME}</span>
			</c:if>
			<span class="col-sm-6 col-xs-12">停留天数：${order.DAY_COUNT}</span>
			<span class="col-sm-6 col-xs-12">人数：${order.MAN_COUNT}成人，${order.CHILD_COUNT}儿童</span>
		</div>
		<div class="paypal-info usr-odr">
			<span class="col-sm-6 col-xs-12">店名：${order.BUY_COMPANY}</span>
			<span class="col-sm-6 col-xs-12">地址：${order.BUY_COMPANY_ADDRESS}</span>
			<span class="col-sm-6 col-xs-12">电话：<span class="glyphicon glyphicon-phone" aria-hidden="true" style="padding-bottom: 0;"></span>${order.BUY_COMPANY_PHONE}</span>
			<span class="col-sm-6 col-xs-12">顾问：<c:if test="${not empty order.COUNSELOR_CHINA_NAME}">${order.COUNSELOR_CHINA_NAME}</c:if><c:if test="${empty order.COUNSELOR_CHINA_NAME}">${order.COUNSELOR_USER_NAME}</c:if><span class="glyphicon glyphicon-phone" aria-hidden="true" style="padding-bottom: 0;"></span>${order.BUY_PHONE}</span>
		</div>
		<div class="paypal-info" style="padding-top: 20px;">
			<p>
				<b>联系人信息</b>
			</p>
			<table class="table table-striped">
				<tr>
					<td>
						<table class="m-table">
							<c:forEach items="${contacts}" var="contact">
							<tr>
								<td class="col-sm-6 col-xs-6">姓名：${contact.CHINA_NAME}</td>
								<td class="col-sm-6 col-xs-6">手机：${contact.MOBILE}</td>
							</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
			</table>
		</div>
		
		<div class="paypal-info" style="padding-top: 20px;">
			<p>
				<b>旅客信息</b>
			</p>
			<table class="table table-striped">
				<c:forEach items="${visitors}" var="visitor">
				<tr>
					<td>
						<table class="m-table">
							<tr>
								<td class="col-sm-4 col-xs-4">${visitor.NAME}</td>
								<td class="col-sm-2 col-xs-2"><c:if test="${visitor.SEX eq '0'}">女</c:if><c:if test="${visitor.SEX eq '0'}">男</c:if></td>
								<td class="col-sm-6 col-xs-6">${visitor.MOBILE}</td>
							</tr>
							<tr>
								<td class="col-sm-4 col-xs-4">${visitor.CARD_TYPE}</td>
								<td colspan="2" class="col-sm-8 col-xs-8">${visitor.CARD_NO}</td>
							</tr>
							<tr>
								<td colspan="3" class="col-sm-12 col-xs-12">${visitor.ATTR_NAME}</td>
							</tr>
						</table>
					</td>
				</tr>
				</c:forEach>
			</table>
		</div>
		<div class="paypal-info">
			<p class="paypal-price col-md-6 col-xs-12" style="text-align: right;">
				总计：&yen;
				<em>${order.SALE_AMOUNT}</em>
			</p>
		</div>
	</div>
	<c:if test="${USER_AGENT eq 'mobile'}">
	<%@ include file="../../commons/m-user-footer.jsp"%>
	</c:if>
</div>
</body>    
</html>
