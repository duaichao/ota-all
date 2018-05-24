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
	<link rel="stylesheet" href="${ctx}/resources/css/yuding.css">
	<link rel="stylesheet" href="${ctx}/resources/css/iconbank.css">
	
	<%@ include file="../commons/meta-js.jsp"%>
	<c:if test="${USER_AGENT eq 'pc'}"><base target="_blank"></c:if>
</head>
<body>
<c:set var="header_title" value="审核信息" />
<%@ include file="../commons/header.jsp"%>
<div class="main-box container">
	<!-- 流程 -->
	<div class="progress">
		<div class="progress-bar progress-bar-success" style="width: 25%">
			<span>选择产品</span>
			<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
		</div>
		<div class="progress-bar progress-bar-success" style="width: 25%">
			<span>填写与核对</span>
			<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
		</div>
		<div class="progress-bar progress-bar-success" style="width: 25%">
			<span>在线支付</span>
			<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
		</div>
		<div class="progress-bar progress-bar-default" style="width: 25%">
			<span>付款成功</span>
			<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
		</div>
	</div>
	<div class="paypal">
		<!-- 支付 -->
		<div class="panel panel-success">
			<div class="panel-heading">
				<h1>在线支付</h1>
			</div>
			<div class="panel-body">
				<div class="paypal-info">
					<p class="col-md-4 col-xs-12">
						<b>${order.START_CITY}&nbsp;<span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>&nbsp;${order.END_CITY}</b>
					</p>
					<p class="col-md-4 col-xs-12">
						<b>订单编号：${order.NO}</b>
					</p>
					<p class="col-md-4 col-xs-12">
						<b>单房差：<b class="cny">&yen;${order.RETAIL_SINGLE_ROOM}</b>×${order.SINGLE_ROOM_CNT}个</b>
					</p>
				</div>
				<div class="paypal-info">
					<span class="col-md-3 col-xs-12">人数：${order.MAN_COUNT}成人，${order.CHILD_COUNT}儿童</span>
					<span class="col-md-3 col-xs-12">出发日期：${order.START_DATE}</span>
					<span class="col-md-3 col-xs-12">集合地点：${order.MUSTER_PLACE}</span>
					<span class="col-md-3 col-xs-12">集合时间：${order.MUSTER_TIME}</span>
				</div>
				<div class="paypal-info" style="border-bottom: 1px solid #e7e7e7;">
					<p class="paypal-price col-md-6 col-xs-12">
						总计：&yen;
						<em>${order.SALE_AMOUNT}</em>
					</p>
				</div>
				<div class="paypal-info">
					<p class="col-md-12 col-xs-12" style="padding-top: 15px;">
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
				
				<div class="paypal-title">
					<p class="col-md-6 col-xs-12">支付平台</p>
				</div>
				<div class="paypal-bank">
					<div class="paypal-box paypal-box col-md-2 col-xs-6">
						<label class="radio-inline">
						  	<input type="radio" checked="checked" name="inlineRadioOptions" id="inlineRadio1" value="option1"> 
						  	<i class="icon iconfont" style="color:#009ee7;">&#xe610;</i>&nbsp;支付宝
						</label>
					</div>
				</div>
				<a href="${ctx}/order/pay?orderId=${order.ID}&bankCode=1" target="_blank" class="btn btn-sign btn-block btn-lg mt15" id="payBtn">核对订单，去支付</a>
				<script type="text/javascript">
					$(function(){
						$('#payBtn').click(function(){
							$('#myModal').modal('show');
						});
					});
				</script>
				<!-- 弹出窗口 -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
				  <div class="modal-dialog" role="document">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				        <h3 class="modal-title" id="myModalLabel">支付提醒</h3>
				      </div>
				      <div class="modal-body">
				          <h3>请你在新打开的平台支付页面进行支付，支付完成前请不要关闭该窗口！</h3><br />
				          <b>在订单支付完成前请不要关闭此窗口，否则会影响购买。</b>
				      </div>
				      <div class="modal-footer">
				        <a target="_self" href="${ctx}/pay/pay_success.jsp" class="btn btn-success">已支付完成</a>
				        <a target="_self" href="${ctx}/pay/pay_fail.jsp?out_trade_no=${order.NO}" class="btn btn-default">支付遇到问题</a>
				      </div>
				    </div>
				  </div>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="../commons/footer.jsp"%>
</body>    
</html>
