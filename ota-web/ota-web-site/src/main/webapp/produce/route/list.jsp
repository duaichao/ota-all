<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
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
			
			$('#startDate').datepicker({format: 'yyyy-mm-dd'});
			$('#endDate').datepicker({format: 'yyyy-mm-dd'});

		});
	</script>
</head>
<body>
	<c:set var="header_title" value="线路列表" />
	<%@ include file="../../commons/header.jsp"%>
	<!-- 列表 -->
		<div class="main-box container">
			
			<c:if test="${USER_AGENT eq 'mobile'}">
			<!-- 高级搜索 -->
			<div class="global-search">
				<a href="${ctx}/produce/route/search?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">
					<b><span class="glyphicon glyphicon-zoom-in" aria-hidden="true"></span>&nbsp;&nbsp;高级搜索&nbsp;&nbsp;</b>
					抵达城市、行程天数、线路属性、主题属性、价格
					<b>&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span></b>
				</a>
			</div>
			</c:if>
			<c:if test="${USER_AGENT eq 'pc'}">
			<!-- 面包屑 -->
			<ol class="breadcrumb">
			  	<li><a href="${ctx}/">首页</a></li>
			  	<li class="active">
			  		<c:if test="${categoryPID eq '36666E8E0CA1EF38E050007F0100469B'}">周边游</c:if><c:if test="${categoryPID eq '36666E8E0C9EEF38E050007F0100469B'}">国内游</c:if><c:if test="${categoryPIDE eq '36666E8E0C9FEF38E050007F0100469B'}">出境游</c:if>
				</li>
			</ol>
			<!-- 选择列表 -->
			<div class="section-list">
				<dl class="section-dl clearfix">
					<dt>抵达城市：</dt>
					<dd>
						<ul>
							<c:set var="citys" value="" />
							<li><a <c:if test="${empty city}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}">全部</a></li>
							<c:forEach items="${routeLabelAndCity}" var="route">
							<c:set var="END_CITY" value="${fn:split(route.CITY_NAME, ',')}"/>
							<c:forEach items="${END_CITY}" var="endCity">
							<c:if test="${fn:indexOf(citys, endCity) <= 0}">
							<li><a <c:if test="${not empty city && endCity eq city}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${endCity}">${endCity}</a></li>
							<c:set var="citys" value="${citys},${endCity}" />							
							</c:if>
							</c:forEach>
							</c:forEach>
						</ul>
					</dd>
				</dl>
				<dl class="section-dl clearfix">
					<dt>行程天数：</dt>
					<dd>
						<ul>
							<li><a <c:if test="${empty dayCount}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">全部</a></li>
							<li><a <c:if test="${dayCount eq '1'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&dayCount=1&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">1天</a></li>
							<li><a <c:if test="${dayCount eq '2'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&dayCount=2&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">2天</a></li>
							<li><a <c:if test="${dayCount eq '3'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&dayCount=3&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">3天</a></li>
							<li><a <c:if test="${dayCount eq '4'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&dayCount=4&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">4天</a></li>
							<li><a <c:if test="${dayCount eq '5'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&dayCount=5&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">5天</a></li>
							<li><a <c:if test="${dayCount eq '6'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&dayCount=6&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">6天</a></li>
							<li><a <c:if test="${dayCount eq '7'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&dayCount=7&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">6天以上</a></li>
						</ul>
					</dd>
				</dl>
				<dl class="section-dl clearfix">
					<dt>线路属性：</dt>
					<dd>
						<ul>
							<li><a <c:if test="${empty attr}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">全部</a></li>
							<li><a <c:if test="${attr eq '品质'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=品质&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">品质</a></li>
							<li><a <c:if test="${attr eq '特价'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=特价&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">特价</a></li>
							<li><a <c:if test="${attr eq '纯玩'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=纯玩&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">纯玩</a></li>
							<li><a <c:if test="${attr eq '豪华'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=豪华&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">豪华</a></li>
							<li><a <c:if test="${attr eq '包机'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=包机&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">包机</a></li>
							<li><a <c:if test="${attr eq '自由行'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=自由行&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">自由行</a></li>
							<li><a <c:if test="${attr eq '自驾游'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=自驾游&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">自驾游</a></li>
						</ul>
					</dd>
				</dl>
				<dl class="section-dl clearfix">
					<dt>主题属性：</dt>
					<dd>
						<ul>
							<li><a <c:if test="${empty themes}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">全部</a></li>
							<li><a <c:if test="${themes eq '古镇'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=古镇&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">古镇</a></li>
							<li><a <c:if test="${themes eq '红色'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=红色&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">红色</a></li>
							<li><a <c:if test="${themes eq '滑雪'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=滑雪&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">滑雪</a></li>
							<li><a <c:if test="${themes eq '海岛度假'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=海岛度假&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">海岛度假</a></li>
							<li><a <c:if test="${themes eq '夏令营'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=夏令营&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">夏令营</a></li>
							<li><a <c:if test="${themes eq '美食'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=美食&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">美食</a></li>
							<li><a <c:if test="${themes eq '门票'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=门票&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">门票</a></li>
							<li><a <c:if test="${themes eq '漂流'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=漂流&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">漂流</a></li>
							<li><a <c:if test="${themes eq '沙漠'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=沙漠&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">沙漠</a></li>
							<li><a <c:if test="${themes eq '探险'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=探险&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">探险</a></li>
							<li><a <c:if test="${themes eq '温泉'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=温泉&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">温泉</a></li>
							<li><a <c:if test="${themes eq '夕阳红'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=夕阳红&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">夕阳红</a></li>
							<li><a <c:if test="${themes eq '邮轮'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=邮轮&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">邮轮</a></li>
							<li><a <c:if test="${themes eq '游学'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=游学&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">游学</a></li>
							<li><a <c:if test="${themes eq '宗教'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=宗教&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">宗教</a></li>
							<li><a <c:if test="${themes eq '专列'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=专列&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">专列</a></li>
							<li><a <c:if test="${themes eq '度假'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=度假&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">度假</a></li>
							<li><a <c:if test="${themes eq '亲子'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=亲子&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">亲子</a></li>
							<li><a <c:if test="${themes eq '蜜月'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=蜜月&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">蜜月</a></li>
							<li><a <c:if test="${themes eq '青年会'}">class="cur-all"</c:if> href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=青年会&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">青年会</a></li>
							

						</ul>
					</dd>
				</dl>
			</div>
			
			<!-- 分类类别 -->
			<nav class="sort-list navbar-default padding-leftnone">
		    	<div class="navbar-header">
		          	<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-sort" aria-expanded="false">
			            <span class="sr-only">列表</span>
			            <span class="icon-bar"></span>
			            <span class="icon-bar"></span>
			            <span class="icon-bar"></span>
		          	</button>
		        </div>
		        <div class="navbar-collapse collapse padding-leftnone padding-rightnone" id="navbar-sort" aria-expanded="false">
		        	<form id="searchForm" method="get" action="${ctx}/produce/route/list?1=1">
		        	<ul class="navbar-left sort-menu">
						<li class="cur-sort">
							<a href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&city=${city}" class="crt"><span>综合</span></a>
						</li>
						<li>
							<a href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=saleNum&city=${city}"><span class="crt-ico">销量</span></a>
						</li>
						<li>
							<a href="${ctx}/produce/route/list?categoryPID=${categoryPID}&query=${query}&themes=${themes}&attr=${attr}&dayCount=${dayCount}&routeType=${routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=price&city=${city}"><span class="price-ico">价格</span></a>
						</li>
					</ul>
		        	<ul class="navbar-left rank-price">
						<li>价格区间：</li>
						<li><input type="text" name="minPrice" value="${minPrice}" /></li>
						<li>—</li>
						<li><input type="text" name="maxPrice" value="${maxPrice}" /></li>
					</ul>
					
					<ul class="navbar-left tour-cal">
						<li>出游日期：</li>
						<li>
							<div class="calendar">
								<div class="fl" style="margin-top: 6px;"><input type="text" name="beginDate" id="startDate" value="${beginDate}" /></div>
								<div class="fl">&nbsp;—&nbsp;</div>
								<div class="fl" style="margin-top: 6px;"><input type="text" name="endDate" id="endDate" value="${endDate}" /></div>
							</div>
						</li>
					</ul>
					<ul class="navbar-left tour-sch">
						<li><input type="text" placeholder="搜索关键字" name="query" value="${query}" /></li>
						<li><a class="btn btn-default btn-sch" href="#" id="searchBtn" role="button">搜索</a></li>
					</ul>
					<input name="themes" value="${themes}" type="hidden" />
					<input name="attr" value="${attr}" type="hidden" />
					<input name="dayCount" value="${dayCount}" type="hidden" />
					<input name="city" value="${city}" type="hidden" />
					<input name="routeType" value="${routeType}" type="hidden" />
					</form>
		        </div>
		        
		    </nav>
		    </c:if>
			<!-- 列表内容 -->
			<app:pageTag pageSize="10" recordCount="0" recordOffset="0"
		      indexNum="5" indexSkip="2" pageOffset="10" autoScroll="false"
		      createIndex="true" recordCountKey="RecordCount" pageURLKey="pageurl"
		      style="pagebar.htm" pageBar="PageBarCode" pageURL="${ctx}/produce/route/list?1=1" locaPageStart=""
		      locaPageEnd="" hrefStart="" hrefEnd="" startSessionKey="" parameterGlean="themes,attr,dayCount,city,query,endDate,beginDate,maxPrice,minPrice,orderby,routeType,categoryPID">
			<div class="product-list">
				<ul class="product-ul clearfix">
					<c:forEach items="${routes}" var="route">
					<li class="thumbnail">
						<div class="product-info">
							<div class="col-md-4 imgbox">
								<c:if test="${USER_AGENT eq 'pc'}">
								<a target="_blank" href="${ctx}/produce/route/detail?id=${route.ID}"><img src="${picctx}/${route.FACE}" width="640" style="height:217px;"></a>
								</c:if>
								<c:if test="${USER_AGENT eq 'mobile'}">
								<a target="_blank" href="${ctx}/produce/route/detail?id=${route.ID}"><img src="${picctx}/${route.FACE}" width="640"></a>
								</c:if>
								<c:if test="${route.ATTR eq '品质'}"><i class="trv-flag flag-pz">${route.ATTR}</i></c:if>
				          		<c:if test="${route.ATTR eq '特价'}"><i class="trv-flag flag-tj">${route.ATTR}</i></c:if>
				          		<c:if test="${route.ATTR eq '纯玩'}"><i class="trv-flag flag-cw">${route.ATTR}</i></c:if>
				          		<c:if test="${route.ATTR eq '豪华'}"><i class="trv-flag flag-hh">${route.ATTR}</i></c:if>
				          		<c:if test="${route.ATTR eq '包机'}"><i class="trv-flag flag-bj">${route.ATTR}</i></c:if>
				          		<c:if test="${route.ATTR eq '自由行'}"><i class="trv-flag flag-zyx">${route.ATTR}</i></c:if>
				          		<c:if test="${route.ATTR eq '自驾游'}"><i class="trv-flag flag-zjy">${route.ATTR}</i></c:if>
							</div>
							<dl class="col-md-8 detail">
								<dt class="col-xs-12"><a target="_blank" href="${ctx}/produce/route/detail?id=${route.ID}">${route.TITLE}</a></dt>
								<dd class="col-xs-9">
									<c:if test="${not empty route.THEMES}">
									<c:set var="THEMES" value="${fn:split(route.THEMES, ',')}"/>
									<c:forEach items="${THEMES}" var="theme">
										<c:if test="${theme eq '古镇'}"><i class="icon-tag icon-ms">${theme}</i></c:if>
										<c:if test="${theme eq '红色'}"><i class="icon-tag icon-hs">${theme}</i></c:if>
										<c:if test="${theme eq '滑雪'}"><i class="icon-tag icon-hx">${theme}</i></c:if>
										<c:if test="${theme eq '海岛度假'}"><i class="icon-tag icon-hd">${theme}</i></c:if>
										<c:if test="${theme eq '夏令营'}"><i class="icon-tag icon-xly">${theme}</i></c:if>
										<c:if test="${theme eq '美食'}"><i class="icon-tag icon-ms">${theme}</i></c:if>
										<c:if test="${theme eq '门票'}"><i class="icon-tag icon-mp">${theme}</i></c:if>
										<c:if test="${theme eq '漂流'}"><i class="icon-tag icon-pl">${theme}</i></c:if>
										<c:if test="${theme eq '沙漠'}"><i class="icon-tag icon-sm">${theme}</i></c:if>
										<c:if test="${theme eq '探险'}"><i class="icon-tag icon-tx">${theme}</i></c:if>
										<c:if test="${theme eq '温泉'}"><i class="icon-tag icon-wq">${theme}</i></c:if>
										<c:if test="${theme eq '夕阳红'}"><i class="icon-tag icon-xyh">${theme}</i></c:if>
										<c:if test="${theme eq '邮轮'}"><i class="icon-tag icon-yl">${theme}</i></c:if>
										<c:if test="${theme eq '游学'}"><i class="icon-tag icon-yx">${theme}</i></c:if>
										<c:if test="${theme eq '宗教'}"><i class="icon-tag icon-zj">${theme}</i></c:if>
										<c:if test="${theme eq '专列'}"><i class="icon-tag icon-zl">${theme}</i></c:if>
										<c:if test="${theme eq '度假'}"><i class="icon-tag icon-zl">${theme}</i></c:if>
									</c:forEach>
									</c:if>
								</dd>
								<c:if test="${empty route.THEMES}">
								<dd class="col-xs-9">&nbsp;</dd>
								</c:if>
								<dd class="col-xs-9"><b>${route.BEGIN_CITY}</b>&nbsp;<span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>&nbsp;<b>${route.END_CITY}</b></dd>
								<dd class="col-xs-9">行程天数：${route.DAY_COUNT}天</dd>
								<dd class="col-xs-9">出游日期：${route.RQ}</dd>
								<dd class="col-xs-3 priceinfo">
									<p class="pro-price">
										<a href="${ctx}/produce/route/detail?id=${route.ID}" target="_blank">
										&yen;
										<em>${route.SUM_PRICE}</em>起</a>
									</p>
									<a href="${ctx}/produce/route/detail?id=${route.ID}" target="_blank" class="tour-btn">立即预定</a>
								</dd>
								<c:if test="${USER_AGENT eq 'mobile'}">
									<script type="text/javascript">
										$(function () {
											$('.priceinfo').css('top', '40px');
										});
									</script>
								</c:if>
								<dd class="col-xs-12">
									${route.FEATURE}
								</dd>
							</dl>
						</div>
					</li>
					</c:forEach>
				</ul>
				<!-- 分页 -->
				${PageBarCode}
			</div>
			</app:pageTag>
		</div>
	
	<!-- 页尾 -->
	<%@ include file="../../commons/footer.jsp"%>
</body>    
</html>
