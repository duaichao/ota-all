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
			
			$('#startDate').datepicker({format: 'yyyy-mm-dd'});
			$('#endDate').datepicker({format: 'yyyy-mm-dd'});
			
		})
	</script>
</head>
<body>
	<!-- 列表 -->
	<div class="section-list-title">
		<a href="javascript:history.go(-1);" class="section-back"><span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span></a>
		<h1 class="head-title">高级搜索</h1>
	</div>
	<form id="searchForm" method="get" action="${ctx}/produce/route/list?1=1">
	<div class="main-box container">
		<div class="section-list-global">
			<dl class="section-dl-inputglobal section-price clearfix">
				<dt>价格区间</dt>
				<dd><input type="text" name="minPrice" value="${minPrice}" /></dd>
				<dd style="text-align:center;">—</dd>
				<dd><input type="text" name="maxPrice" value="${maxPrice}" /></dd>
			</dl>
			<dl class="section-dl-inputglobal section-price clearfix">
				<dt>出游日期</dt>
				<dd>
				<input type="text" name="beginDate" id="startDate" readonly="readonly" value="${beginDate}" /></dd>
				<dd style="text-align:center;">—</dd>
				<dd><input type="text" name="endDate" id="endDate" readonly="readonly" value="${endDate}" /></dd>
			</dl>
			<dl class="section-dl-inputglobal clearfix">
				<p><input type="text" class="section-inputserch" placeholder="搜索关键字" name="query" value="${query}" /></p>
				<input name="themes" value="${themes}" type="hidden" />
				<input name="attr" value="${attr}" type="hidden" />
				<input name="dayCount" value="${dayCount}" type="hidden" />
				<input name="city" value="${city}" type="hidden" />
				<input name="routeType" value="${param.routeType}" type="hidden" />
			</dl>
			<dl class="section-dl-global clearfix">
				<dt>抵达城市</dt>
				<dd>
					<c:set var="citys" value="" />
					<c:forEach items="${routeLabelAndCity}" var="route">
					<c:set var="END_CITY" value="${fn:split(route.CITY_NAME, ',')}"/>
					<c:forEach items="${END_CITY}" var="endCity">
					<c:if test="${fn:indexOf(citys, endCity) <= 0}">
					<a <c:if test="${not empty city && endCity eq city}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=${attr}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${endCity}">${endCity}</a>
					<c:set var="citys" value="${citys},${endCity}" />							
					</c:if>
					</c:forEach>
					</c:forEach>
				</dd>
			</dl>
			<dl class="section-dl-global clearfix">
				<dt>行程天数</dt>
				<dd>
					<li><a <c:if test="${dayCount eq '1'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=${attr}&dayCount=1&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">1天</a></li>
					<li><a <c:if test="${dayCount eq '2'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=${attr}&dayCount=2&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">2天</a></li>
					<li><a <c:if test="${dayCount eq '3'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=${attr}&dayCount=3&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">3天</a></li>
					<li><a <c:if test="${dayCount eq '4'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=${attr}&dayCount=4&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">4天</a></li>
					<li><a <c:if test="${dayCount eq '5'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=${attr}&dayCount=5&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">5天</a></li>
					<li><a <c:if test="${dayCount eq '6'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=${attr}&dayCount=6&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">6天</a></li>
					<li><a <c:if test="${dayCount eq '7'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=${attr}&dayCount=7&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">6天以上</a></li>
				</dd>
			</dl>
			<dl class="section-dl-global clearfix">
				<dt>线路属性</dt>
				<dd>
					<li><a <c:if test="${attr eq '品质'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=品质&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">品质</a></li>
					<li><a <c:if test="${attr eq '特价'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=特价&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">特价</a></li>
					<li><a <c:if test="${attr eq '纯玩'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=纯玩&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">纯玩</a></li>
					<li><a <c:if test="${attr eq '豪华'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=豪华&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">豪华</a></li>
					<li><a <c:if test="${attr eq '包机'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=包机&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">包机</a></li>
					<li><a <c:if test="${attr eq '自由行'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=${themes}&attr=自由行&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">自由行</a></li>
				</dd>
			</dl>
			<dl class="section-dl-global clearfix">
				<dt>主题属性</dt>
				<dd>
					<li><a <c:if test="${themes eq '古镇'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=古镇&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">古镇</a></li>
					<li><a <c:if test="${themes eq '红色'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=红色&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">红色</a></li>
					<li><a <c:if test="${themes eq '滑雪'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=滑雪&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">滑雪</a></li>
					<li><a <c:if test="${themes eq '海岛度假'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=海岛度假&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">海岛度假</a></li>
					<li><a <c:if test="${themes eq '夏令营'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=夏令营&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">夏令营</a></li>
					<li><a <c:if test="${themes eq '美食'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=美食&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">美食</a></li>
					<li><a <c:if test="${themes eq '门票'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=门票&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">门票</a></li>
					<li><a <c:if test="${themes eq '漂流'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=漂流&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">漂流</a></li>
					<li><a <c:if test="${themes eq '沙漠'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=沙漠&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">沙漠</a></li>
					<li><a <c:if test="${themes eq '探险'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=探险&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">探险</a></li>
					<li><a <c:if test="${themes eq '温泉'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=温泉&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">温泉</a></li>
					<li><a <c:if test="${themes eq '夕阳红'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=夕阳红&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">夕阳红</a></li>
					<li><a <c:if test="${themes eq '邮轮'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=邮轮&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">邮轮</a></li>
					<li><a <c:if test="${themes eq '游学'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=游学&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">游学</a></li>
					<li><a <c:if test="${themes eq '宗教'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=宗教&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">宗教</a></li>
					<li><a <c:if test="${themes eq '专列'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=专列&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">专列</a></li>
					<li><a <c:if test="${themes eq '度假'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=度假&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">度假</a></li>
					<li><a <c:if test="${themes eq '亲子'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=亲子&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">亲子</a></li>
					<li><a <c:if test="${themes eq '蜜月'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=蜜月&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">蜜月</a></li>
					<li><a <c:if test="${themes eq '青年会'}">class="cur-global"</c:if> href="${ctx}/produce/route/list?query=${query}&themes=青年会&attr=${attr}&dayCount=${dayCount}&routeType=${param.routeType}&maxPrice=${maxPrice}&minPrice=${minPrice}&beginDate=${beginDate}&endDate=${endDate}&orderby=${orderby}&city=${city}">青年会</a></li>
					
				</dd>
			</dl>
			
		</div>
	</div>
	<div class="section-dl-inputglobal section-btnfix clearfix">
		<p><button class="btn btn-default btn-sectionbtn" id="searchBtn" role="button">搜索</button></p>
	</div>
	</form>
</body>    
</html>
