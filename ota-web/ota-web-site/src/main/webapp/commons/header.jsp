<div style="display: none;"><img width="320" src="${picctx}/${company.LOGO}"></div>
<%@ page language="java" pageEncoding="UTF-8"%>
<!-- 页头 -->
<c:if test="${USER_AGENT eq 'pc'}">
<script type="text/javascript">
$(function(){
	$('#topSearchBtn').click(function(){
		$('#topSearchForm').submit();
	});
	
	$("#drop").hover(function () {
		$('#drop-zyx').show();
	}, function(){
		$('#drop-zyx').hide();
	});
	
	$("#drop-zyx").hover(function () {
		$('#drop-zyx').show();
	}, function(){
		$('#drop-zyx').hide();
	});
	
	$(".local-city").hover(function () {
		$('.show-city').show();
		$('.local-city .city').addClass('choose-city');
		
	}, function(){
		$('.show-city').hide();
		$('.local-city .city').removeClass('choose-city');
	});
	
});
</script>
  <div class="header container <c:if test="${USER_AGENT eq 'mobile'}">header-mobile</c:if>">
	<a href="${ctx}/" class="navbar-brand head-logo"><img src="${picctx}/${company.LOGO}" title="${company.TITLE}" height="60"></a>
	<h3 class="navbar-brand head-name">${company.TITLE}</h3>
	<!-- 切换城市 -->
	<div class="local-city">
		<a href="javascript:;" class="city">
			<span class="now-city">
			<c:if test="${not empty s_start_city}">
				${s_start_city}
			</c:if>
			<c:if test="${empty s_start_city}">
			<c:forEach items="${begin_citys}" var="begin_city" end="0">
				${begin_city}
			</c:forEach>
			</c:if>
			</span>
			<c:if test="${fn:length(begin_citys) > 1}">
			<span class="glyphicon glyphicon-menu-down cr" aria-hidden="true"></span>
			</c:if>
		</a>
		<div class="show-city" style="display: none;">
			<ul>
				<c:if test="${fn:length(begin_citys) > 1}">
				<c:set var="bc" value=""/>
				<c:forEach items="${begin_citys}" var="begin_city">
				<c:if test="${fn:indexOf(bc, begin_city) < 0}">
				<c:set var="bc" value="${bc},${begin_city}"/>
				<li><a href="${ctx}?s_start_city=${begin_city}" target="_self">${begin_city}</a></li>
				</c:if>
				</c:forEach>
				</c:if>
			</ul>
		</div>
	</div>
	<!-- 搜索 -->
	<form class="navbar-form navbar-left" id="topSearchForm" method="get" action="${ctx}/produce/route/list?1=1">
		<div class="input-group navbar-right head-search">
   			<input type="text" class="form-control" placeholder="搜索目的地、关键字" name="query" value="${query}">
  			<span class="input-group-btn">
      		<button class="btn btn-default" type="button" style="padding: 9px 12px;">
      			<span class="glyphicon glyphicon-search" id="topSearchBtn" aria-hidden="true"></span>
 			</button>
    		</span>
    	</div>
    </form>
    <c:if test="${not empty company.PHONE1 and not empty company.QQ1}">
	<h3 class="navbar-brand navbar-right head-phone">
		<span class="glyphicon glyphicon-phone-alt" aria-hidden="true"></span>
		${company.PHONE1}
		<p class="head-qq"><a target="blank" class="qq" href="http://wpa.qq.com/msgrd?V=3&amp;uin=${company.QQ1}&amp;Site=QQ客服&amp;Menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=1:${company.QQ1}:3" alt="点击这里给我发消息"></a></p>
	</h3>
	</c:if>
	<c:if test="${empty company.PHONE1 and empty company.QQ1}">
	<div class="head-ser">
		<a href="${ctx}/store/list"><h6>如需咨询,请联系实体店客服&nbsp;<span class="glyphicon glyphicon-user" id="topSearchBtn" aria-hidden="true"></span></h6></a>
	</div>
	</c:if>
  </div>
  <!-- 导航 -->
  
  <nav class="navbar navbar-default navbar-index">  
  
  	<div class="container">
			<div class="navbar-header">
		      	<button type="button" class="navbar-toggle collapsed navbar-menubtn" data-toggle="collapse" data-target="#navbar-collapse" aria-expanded="false">
		        	<span class="icon-bar"></span>
		        	<span class="icon-bar"></span>
		        	<span class="icon-bar"></span>
		      	</button>
		    </div>
		     <div class="collapse navbar-collapse navbar-togbg" id="navbar-collapse">
		      	<ul class="nav navbar-nav navbar-left head-nav">
		        	<li <c:if test="${empty param.routeType && fn:indexOf(rurl, 'help/help.jsp') < 0 && fn:indexOf(rurl, '/store/') < 0 && fn:indexOf(rurl, '/produce/route/list') < 0}">class="active"</c:if>><a href="${ctx}/">首页</a></li>
					<c:forEach items="${categorys}" var="category">
						<c:if test="${category.ORDER_BY ne '-1'}">
						<li <c:if test="${not empty param.categoryPID && param.categoryPID eq category.PID}">class="active"</c:if>><a target="_blank" href="${ctx}/produce/route/list?categoryPID=${category.PID}">${category.CATEGORY}</a></li>
						</c:if>
					</c:forEach>
					<li <c:if test="${fn:indexOf(rurl, '/store/') >= 0}">class="active"</c:if>><a target="_blank" href="${ctx}/store/list">门市地址</a></li>	            	
	            	
	            	<li <c:if test="${fn:indexOf(rurl, 'help/help.jsp') >= 0}">class="active"</c:if>><a href="${ctx}/help/help.jsp" target="_blank">帮助中心</a></li>
		      	</ul>
		      	<c:if test="${not empty webUser}">
		      	<ul class="nav navbar-nav navbar-right head-nav">
		        	<li><a href="${ctx}/user/sec/center" target="_blank"><c:if test="${not empty webUser.CHINA_NAME}">${webUser.CHINA_NAME}</c:if><c:if test="${empty webUser.CHINA_NAME}">${webUser.USER_NAME}</c:if></a></li>
		        	<li><a href="${ctx}/user/logout">退出</a></li>
		      	</ul>
				</c:if>
				<c:if test="${empty webUser}">
		      	<ul class="nav navbar-nav navbar-right head-nav">
		        	<li><a target="_blank" href="${ctx}/user/to/login">登录</a></li>
					<li><a target="_blank" href="${ctx}/user/to/reg">注册</a></li>
		      	</ul>
		      	</c:if>
		     </div>
  	</div>
</nav>
</div>
</c:if>
<c:if test="${USER_AGENT eq 'mobile' && not empty header_index}">
<div class="header container <c:if test="${USER_AGENT eq 'mobile'}">header-mobile</c:if>">
	<a href="${ctx}/" class="navbar-brand head-logo"><img src="${picctx}/${company.LOGO}" title="${company.TITLE}" width="65"></a>
	<h3 class="navbar-brand head-name">${company.TITLE}</h3>
  </div>
</c:if>
<c:if test="${USER_AGENT eq 'mobile' && empty header_index}">
<nav class="navbar navbar-default m-navbar">
  <div class="container-fluid">
    <div class="section-list-title">
      <a href="javascript:history.go(-1);" class="section-back"><span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span></a>
	  <h1 class="head-title">${header_title}</h1>
	  <a href="${ctx}/produce/route/search" class="section-search"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></a>
    </div>
  </div>
</nav>
</c:if>
<c:if test="${USER_AGENT eq 'mobile'}">
<div class="alert alert-warning m-kf" role="alert">
      <div class="m-mobile"><strong>客服：</strong>&nbsp;<span class="glyphicon glyphicon-phone-alt" aria-hidden="true"></span>&nbsp;<strong>${company.PHONE1}</strong></div>
      <div class="m-qq">
      	<img src="${ctx}/resources/images/QQ.png" width="20">&nbsp;<strong>${company.QQ1}</strong>
      </div>
  </div>
</c:if>