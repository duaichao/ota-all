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
	<link rel="stylesheet" href="${ctx}/resources/css/datepicker.css">
	<%@ include file="../../commons/meta-js.jsp"%>
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript">
		$(function () {
			$('[data-toggle="tooltip"]').tooltip();
			$('#beginDate').datepicker({format: 'yyyy-mm-dd'});
			$('#endDate').datepicker({format: 'yyyy-mm-dd'});
		})
	</script>
	<c:if test="${USER_AGENT eq 'pc'}"><base target="_blank"></c:if>
</head>
<body>
	<c:if test="${USER_AGENT eq 'mobile'}">
	<c:set var="header_title" value="我的订单" />
	<%@ include file="../../commons/header.jsp"%>
	</c:if>
	<div class="usr-box">
	
		<c:if test="${USER_AGENT eq 'mobile'}">
		<!-- 高级搜索 -->
		<div class="global-search">
			<a href="${ctx}/user/center/ordersearch.jsp" target="_self">
				<b><span class="glyphicon glyphicon-zoom-in" aria-hidden="true"></span>&nbsp;&nbsp;高级搜索&nbsp;&nbsp;</b>
				订单状态、下单时间、搜索
				<b>&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span></b>
			</a>
		</div>
		</c:if>
		
		
		<form class="form-horizontal" id="form" action="${ctx}/user/sec/order" method="get">
		<app:pageTag pageSize="10" recordCount="0" recordOffset="0"
		      indexNum="5" indexSkip="2" pageOffset="10" autoScroll="false"
		      createIndex="true" recordCountKey="RecordCount" pageURLKey="pageurl"
		      style="pagebar.htm" pageBar="PageBarCode" pageURL="${ctx}/user/sec/order?1=1" locaPageStart=""
		      locaPageEnd="" hrefStart="" hrefEnd="" startSessionKey="" parameterGlean="query,endDate,beginDate,STATUS">
		      
		<c:if test="${USER_AGENT eq 'pc'}">
		<h2><span>我的订单</span></h2>
		<nav class="sort-list navbar-default usr-search">
			<div class="navbar-header">
	          	<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-sort" aria-expanded="false">
		            <span class="sr-only">列表</span>
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
	          	</button>
	        </div>
	        <div class="navbar-collapse collapse navbar-usr" id="navbar-sort" aria-expanded="false">
		        <ul class="col-md-3 navbar-left padding-rightnone" style="overflow:hidden;">
					<li>订单状态：</li>
					<li>
						<select class="form-control input-sm" name="status">
							<option <c:if test="${empty status}">selected="selected"</c:if> value="">全部订单</option>
							<option <c:if test="${status eq 'pay'}">selected="selected"</c:if> value="pay">已付款</option>
							<option <c:if test="${status eq 'unpay'}">selected="selected"</c:if> value="unpay">未付款</option>
							<option <c:if test="${status eq 'cancel'}">selected="selected"</c:if> value="cancel">已取消</option>
						</select>
					</li>
				</ul>
				<ul class="col-md-6 navbar-left padding-rightnone" style="overflow:hidden;">
					<li>下单时间：</li>
					<li>
						<div class="calendar">
							<div class="fl" style="margin-top: 5px;"><input type="text" name="beginDate" id="beginDate" value="${param.beginDate}"></div>
							<div class="fl">&nbsp;—&nbsp;</div>
							<div class="fl" style="margin-top: 5px;"><input type="text" name="endDate" id="endDate" value="${param.endDate}"></div>
						</div>
					</li>
				</ul>
				<ul class="col-md-3 navbar-left padding-rightnone" style="overflow:hidden;">
					<li>
						<input type="text" class="form-control input-sm" name="query" value="${query}" placeholder="搜索关键字">
					</li>
					<li class="padding-leftnone">
						<button type="submit" class="label label-success btn-sch" data-toggle="modal" data-target=".mini-cal">搜索</button>
					</li>
				</ul>
			</div>
		</nav>
		
		<table class="table table-striped usr-odrtab mt15">
			<tr>
				<th class="col-md-4">产品名称</th>
				<th class="col-md-2">店名</th>
				<th class="col-md-1">游客</th>
				<th class="col-md-1">金额</th>
				<th class="col-md-2">下单/付款时间</th>
				<th class="col-md-2">订单状态</th>
			</tr>
			<c:forEach items="${orders}" var="order">
			<tr>
				<td>
					<span class="usr-odr"><a href="${ctx}/order/detail?orderId=${order.ID}" target="_self">${order.PRODUCE_NAME}</a></span>
					<span class="usr-odr">订单编号：${order.NO}</span>
				</td>
				<td>
					<span class="usr-odr"><a href="${ctx}/store/detail?storeId=${order.CREATE_COMPANY_ID}">${order.BUY_COMPANY}</a></span>
					<span class="usr-odr"><span class="glyphicon glyphicon-phone" aria-hidden="true"></span>&nbsp;${order.BUY_PHONE}</span>
				</td>
				<td>
					<span class="usr-odr">大人：${order.MAN_COUNT}</span>
					<span class="usr-odr">儿童：${order.CHILD_COUNT}</span>
				</td>
				<td>
					<b class="cny">&yen;${order.SALE_AMOUNT}</b>
				</td>
				<td>
					<span class="usr-odr">${order.CREATE_TIME}</span>
					<span class="usr-odr">${order.PAY_TIME}</span>
				</td>
				<td>
					<c:if test="${order.STATUS eq 0}"><c:set var="ord" value="odr-wait" /></c:if>
					<c:if test="${order.STATUS eq 1}"><c:set var="ord" value="odr-wait" /></c:if>
					<c:if test="${order.STATUS eq 2}"><c:set var="ord" value="odr-ok" /></c:if>
					<c:if test="${order.STATUS eq 3}"><c:set var="ord" value="odr-wait" /></c:if>
					<c:if test="${order.STATUS eq 4}"><c:set var="ord" value="odr-wait" /></c:if>
					<c:if test="${order.STATUS eq 5}"><c:set var="ord" value="odr-ok" /></c:if>
					<c:if test="${order.STATUS eq 6}"><c:set var="ord" value="odr-no" /></c:if>
					<c:if test="${order.STATUS eq 7}"><c:set var="ord" value="odr-no" /></c:if>
					<span class="usr-odr"><b class="${ord}">${order.STATUS_TITLE}</b></span>
					<c:if test="${order.STATUS eq 0}">
					<a class="btn btn-xs btn-default" href="${ctx}/order/cancel?orderId=${order.ID}" target="_self" role="button">取消</a>
					</c:if>
					<c:if test="${order.STATUS eq 0 || order.STATUS eq 1}">
					<a class="btn btn-xs btn-success" href="${ctx}/order/to/pay?id=${order.ID}" role="button">付款</a>
					</c:if>
					
				</td>
			</tr>
			</c:forEach>
		</table>
		</c:if>
		
		<c:if test="${USER_AGENT eq 'mobile'}">
		<!-- moblie -->
		<div class="user-orderlist">
			<c:forEach items="${orders}" var="order">
			<ul>
				<li>
					<div class="col-sm-8 col-xs-8">
						<span class="usr-odr"><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span>&nbsp;${order.BUY_COMPANY}</span>
						<span class="usr-odr"><span class="glyphicon glyphicon-phone" aria-hidden="true"></span>&nbsp;${order.BUY_PHONE}</span>
					</div>
					<c:if test="${order.STATUS eq 0}"><c:set var="ord" value="odr-wait" /></c:if>
					<c:if test="${order.STATUS eq 1}"><c:set var="ord" value="odr-wait" /></c:if>
					<c:if test="${order.STATUS eq 2}"><c:set var="ord" value="odr-ok" /></c:if>
					<c:if test="${order.STATUS eq 3}"><c:set var="ord" value="odr-wait" /></c:if>
					<c:if test="${order.STATUS eq 4}"><c:set var="ord" value="odr-wait" /></c:if>
					<c:if test="${order.STATUS eq 5}"><c:set var="ord" value="odr-ok" /></c:if>
					<c:if test="${order.STATUS eq 6}"><c:set var="ord" value="odr-no" /></c:if>
					<c:if test="${order.STATUS eq 7}"><c:set var="ord" value="odr-no" /></c:if>
					<div class="col-sm-4 col-xs-4"><span class="usr-odr"><b class="${ord}">${order.STATUS_TITLE}</b></span></div>
				</li>
				<li class="bg">
					<div class="col-sm-8 col-xs-8">
						<span class="usr-odr"><a href="${ctx}/produce/route/detail?id=${order.PRODUCE_ID}&routeType=${order.PRODUCETYPE}">${order.PRODUCE_NAME}</a></span>
						<span class="usr-odr">订单编号：${order.NO}</span>
					</div>
					<div class="col-sm-4 col-xs-4">
						<span class="usr-odr"><b class="cny" style="font-size: 14px;">&yen;${order.SALE_AMOUNT}</b></span>
						<span class="usr-odr">大人：${order.MAN_COUNT}</span>
						<span class="usr-odr">儿童：${order.CHILD_COUNT}</span>
					</div>
				</li>
				<li class="bg">
					<div class="col-sm-12 col-xs-12">
						<span class="usr-odr">创建时间：${order.CREATE_TIME}</span>
						<c:if test="${not empty order.PAY_TIME}">
						<span class="usr-odr">付款时间：${order.PAY_TIME}</span>
						</c:if>
					</div>
				</li>
				<li>
					<div class="col-sm-12 col-xs-12" style="text-align: right;">
						<a class="btn btn-default" href="${ctx}/order/detail?orderId=${order.ID}" role="button">详情</a>
						<c:if test="${order.STATUS eq 0}">
						<a class="btn btn-default" href="${ctx}/order/cancel?orderId=${order.ID}" role="button">取消</a>
						</c:if>
						<c:if test="${order.STATUS eq 0 || order.STATUS eq 1}">
						<a class="btn btn-success" href="${ctx}/order/to/pay?id=${order.ID}" role="button">付款</a>
						</c:if>
					</div>
				</li>
			</ul>
			</c:forEach>
		</div>
		</c:if>
		<!-- 分页 -->
		${PageBarCode}
		
		</app:pageTag>
		</form>
		
	</div>
	<c:if test="${USER_AGENT eq 'mobile'}">
	<%@ include file="../../commons/m-user-footer.jsp"%>
	</c:if>
</body>    
</html>
