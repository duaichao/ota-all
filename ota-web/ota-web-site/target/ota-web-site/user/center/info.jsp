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
			$('#bd').datepicker({format: 'yyyy-mm-dd'});
		})
	</script>
</head>
<body>
	<c:if test="${USER_AGENT eq 'mobile'}">
	<c:set var="header_title" value="我的资料" />
	<%@ include file="../../commons/header.jsp"%>
	</c:if>
	<div class="usr-box">
		<h2><span>我的资料</span></h2>
		<div class="usr-info" id="dvFlashUpload1" imgno="头像">
			<div class="usr-info-left col-xs-4" id="dvFlashUpload1">
				<div class="initial">
				<c:if test="${empty webUser.FACE}"><img src="${ctx}/resources/images/default_pic.png" alt="..." width="96"></c:if>
				<c:if test="${not empty webUser.FACE}">
					<img src="${picctx}/${webUser.FACE}" alt="..." width="96">
				</c:if>
				</div>
			</div>
			<ul class="usr-info-right col-xs-8">
				<li>
					用户名：<a href="javascript:void(0)" target="_self" class="usr-id">${webUser.USER_NAME}</a>
				</li>
				<li>
					<span class="glyphicon glyphicon-phone" aria-hidden="true"></span>&nbsp;
					${fn:substring(webUser.MOBILE, 0, 3)}****${fn:substring(webUser.MOBILE, 7, 11)}
					<a href="${ctx}/user/center/phone.jsp" target="_self" class="usr-link">[修改]</a>
				</li>
			</ul>
		</div>
		<dl class="dl-horizontal">
			<form action="${ctx}/user/sec/update/info" method="post" id="from">
			<dt style="width: 208px; padding-right: 15px;">出生日期：</dt>
		  	<dd>
		  		<div class="col-sm-4 col-xs-12 padding-leftnone" >
		  			<input class="form-control" type="text" id="bd" name="pm[BIRTHDAY]" value="${webUser.BIRTHDAY}">
		  		</div>
 			</dd>
		  	<dt style="width: 208px; padding-right: 15px;">性别：</dt>
		  	<dd>
		  		<div class="col-sm-2 col-xs-12 padding-leftnone" >
			  		<select class="form-control" name="pm[SEX]">
						<option <c:if test="${webUser.SEX eq '1'}">selected="selected"</c:if> value="1">男</option>
						<option <c:if test="${webUser.SEX eq '0'}">selected="selected"</c:if> value="0">女</option>
					</select>
				</div>
 			</dd>
		  	<dt style="width: 208px; padding-right: 15px;">姓名：</dt>
		  	<dd>
		  		<div class="col-sm-4 col-xs-12 padding-leftnone" >
		  			<input class="form-control input-sm" type="text" name="pm[CHINA_NAME]" value="${webUser.CHINA_NAME}">
		  		</div>
		  	</dd>
 			<dt style="width: 208px; padding-right: 15px;">&nbsp;</dt>
		  	<dd>
		  		<div class="col-sm-2 col-xs-12 padding-leftnone" >
		  			<input type="hidden" name="pm[ID]" value="${webUser.ID}" >
		  			<button type="submit" class="btn btn-sign btn-block">保存</button>
		  		</div>
		  	</dd>
		  	</form>
		</dl>
	</div>
	<c:if test="${USER_AGENT eq 'mobile'}">
	<%@ include file="../../commons/m-user-footer.jsp"%>
	</c:if>
</body>    
</html>
