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
	<link rel="stylesheet" href="${ctx}/resources/css/tour.css">
	<link rel="stylesheet" href="${ctx}/resources/css/datepicker.css">
	<%@ include file="../../commons/meta-js.jsp"%>
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript">
		$(function () {
			
			$('[data-toggle="tooltip"]').tooltip();
			
			$('#searchBtn').click(function(){
				$('#searchForm').submit();
			});
			
			$('#beginDate').datepicker({format: 'yyyy-mm-dd'});
			$('#endDate').datepicker({format: 'yyyy-mm-dd'});
			
		})
	</script>
</head>
<body>
	<!-- 列表 -->
	<div class="section-list-title">
		<a href="javascript:history.go(-1);" target="_self" class="section-back"><span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span></a>
		<span>高级搜索</span>
	</div>
	<form id="searchForm" method="get" action="${ctx}/user/sec/order?1=1" target="_self">
	<div class="main-box container">
		<div class="section-list-global">
			<dl class="section-dl-inputglobal clearfix">
				<dt>订单状态</dt>
				<dd>
					<select class="form-control input-sm" name="status">
						<option <c:if test="${empty status}">selected="selected"</c:if> value="">全部订单</option>
						<option <c:if test="${status eq 'pay'}">selected="selected"</c:if> value="pay">已付款</option>
						<option <c:if test="${status eq 'unpay'}">selected="selected"</c:if> value="unpay">未付款</option>
						<option <c:if test="${status eq 'cancel'}">selected="selected"</c:if> value="cancel">已取消</option>
					</select>
				</dd>
			</dl>
			<dl class="section-dl-inputglobal section-price clearfix">
				<dt>下单日期</dt>
				<dd>
				<input type="text" name="beginDate" id="beginDate" readonly="readonly" value="${beginDate}" /></dd>
				<dd style="text-align:center;">—</dd>
				<dd><input type="text" name="endDate" id="endDate" readonly="readonly" value="${endDate}" /></dd>
			</dl>
			<dl class="section-dl-inputglobal clearfix">
				<p><input type="text" class="section-inputserch" placeholder="搜索关键字" name="query" value="${query}" /></p>
			</dl>
		</div>
	</div>
	<div class="section-dl-inputglobal section-btnfix clearfix">
		<p><button type="submit" class="btn btn-default btn-sectionbtn" id="searchBtn" role="button">搜索</button></p>
	</div>
	</form>
</body>    
</html>
