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
	<link rel="stylesheet" href="${ctx}/resources/css/mobile.css">
	
	<%@ include file="../commons/meta-js.jsp"%>
</head>
<body>
	<div class="container">
		<!-- 头部 -->
		<div class="mhead">
			<a href="${ctx}/" class="navbar-brand head-logo"><img src="${picctx}/${company.LOGO}" title="${company.TITLE}" width="65"></a>
			<div class="navbar-right head-mtitle">
				<h3 class="head-mname">${company.TITLE}</h3>
				<h3>
					<div class="head-mphone"><span class="glyphicon glyphicon-phone-alt" aria-hidden="true"></span> ${company.PHONE1}</div>
					<div class="head-mqq"><span class="glyphicon glyphicon-comment" aria-hidden="true"></span> ${company.QQ1}</div>
				</h3>
			</div>
		</div>
		<!-- 搜索切换城市 -->
		<form id="topSearchForm" method="get" action="${ctx}/produce/route/list?1=1" class="bs-example bs-example-form" data-example-id="input-group-dropdowns">
			<div class="input-group head-sch">
	            <div class="input-group-btn">
		            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> 
		            	<c:if test="${not empty s_start_city}">
							${s_start_city}
						</c:if>
						<c:if test="${empty s_start_city}">
						<c:forEach items="${begin_citys}" var="begin_city" end="0">
							${begin_city}
						</c:forEach>
						</c:if>
		            	<span class="caret"></span>
		            </button>
		            <ul class="dropdown-menu head-city">
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
	          <input type="text" class="form-control" aria-label="Text input with dropdown button" name="query" placeholder="搜索目的地、关键字">
	        </div>
	    </form>
		<!-- 焦点图 -->
		<div id="myCarousel" class="carousel slide" data-ride="carousel">
			<ol class="carousel-indicators">
			    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
			    <li data-target="#myCarousel" data-slide-to="1" class=""></li>
			    <li data-target="#myCarousel" data-slide-to="2" class=""></li>
			</ol>
			<div class="carousel-inner" role="listbox">
			
				<c:forEach items="${ADATTR}" var="ad" varStatus="i">
				<c:if test="${ad.TYPE eq '1'}">
					<div class="item <c:if test="${i.index eq 0}">active</c:if>">
			          <a href="${ad.HREF}" target="_blank"><img src="${picctx}/${ad.URL}" data-holder-rendered="true"></a>
			        </div>
				</c:if>
				</c:forEach>
				<a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
				    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
				    <span class="sr-only">Previous</span>
				</a>
				<a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
				    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
				    <span class="sr-only">Next</span>
				</a>
			</div>
		</div>
		<!-- 菜单 -->
		<div class="mmenu">
			<ul>
				<li class="col-xs-4"><a href="${ctx}/produce/route/list?categoryPID=36666E8E0C9EEF38E050007F0100469B" class="m-gn"><span><img src="${ctx}/resources/images/mico-gn.png"></span>&nbsp;<span>国内游</span></a></li>
				<li class="col-xs-4"><a href="${ctx}/produce/route/list?categoryPID=36666E8E0C9FEF38E050007F0100469B" class="m-cj"><span><img src="${ctx}/resources/images/mico-cj.png"></span>&nbsp;<span>出境游</span></a></li>
				<li class="col-xs-4"><a href="${ctx}/produce/route/list?categoryPID=36666E8E0CA1EF38E050007F0100469B" class="m-zb"><span><img src="${ctx}/resources/images/mico-zb.png"></span>&nbsp;<span>周边游</span></a></li>
				<li class="col-xs-4"><a href="${ctx}/produce/route/list?attr=自由行" class="m-zyx"><span><img src="${ctx}/resources/images/mico-zyx.png"></span>&nbsp;<span>自由行</span></a></li>
				<li class="col-xs-4"><a href="${ctx}/produce/route/list?themes=海岛度假" class="m-hddj"><span><img src="${ctx}/resources/images/mico-hd.png"></span>&nbsp;<span>海岛度假</span></a></li>
				<li class="col-xs-4"><a href="${ctx}/produce/route/list?attr=包机&themes=邮轮" class="m-bjyl"><span><img src="${ctx}/resources/images/mico-bjyl.png"></span>&nbsp;<span>包机邮轮</span></a></li>
				<li class="col-xs-4"><a href="#" class="m-jd"><span><img src="${ctx}/resources/images/mico-jd.png"></span>&nbsp;<span>酒店</span></a></li>
				<li class="col-xs-4"><a href="#" class="m-trv"><span><img src="${ctx}/resources/images/mico-trv.png"></span>&nbsp;<span>景点</span></a></li>
				<li class="col-xs-4"><a href="${ctx}/store/list" class="m-std"><span><img src="${ctx}/resources/images/mico-std.png"></span>&nbsp;<span>门市地址</span></a></li>
			</ul>
		</div>
		<!-- 线路切换 -->
		<div class="mtour">
			<div class="mtab" id="tabhead">
				<!-- 标题 -->
			  	<ul class="nav nav-tabs navbar-left">
				  	<li><span>热卖</span></li>
				  	<li class="active"><a class="lvTag" href="#gny" cn="gnyTag" data-toggle="tab">国内游</a></li>
				   	<li><a class="lvTag" href="#cjy" cn="cjyTag" data-toggle="tab">出境游</a></li>
				   	<li><a class="lvTag" href="#zby" cn="zbyTag" data-toggle="tab">周边游</a></li>
				</ul>
				<!-- 线路产品 -->
				<script type="text/javascript">
					$(function(){
					
						$('.lvTag').click(function(){
							$('.sectab').hide();
							$('.'+$(this).attr('cn')).show();
						});
						
						$('.allRoute').click(function(){
							$('.'+$(this).attr('cn')).addClass('in active');
							$(this).parents('ul').children('li').removeClass('active');
							$(this).parent().addClass('active');
						});
						
					});
				</script>
				
				<script type="text/javascript">
					var routeCategorys = new Array();
				</script>		
				<c:set var="c" value="0" />
				<c:forEach items="${routeCategorys}" var="routeCategory">
					<script type="text/javascript">	
						routeCategorys[${c}] = ['${routeCategory.PID}', '${routeCategory.ID}', '${routeCategory.ORDER_BY}', '${routeCategory.CATEGORY}'];
					</script>	
					<c:set var="c" value="${c}+1" />
				</c:forEach>
		
				<script type="text/javascript">
					function showCityRoutes(t, a){
						
						var ec = $(t).attr('ec'), ct = $(t).attr('ct');
						
						if(a == 'all'){
							$('#'+ct+' ul li').each(function(){
								$(this).show();
							})
							return;	
						}
						
						$('#'+ct+' ul li').each(function(){
							$(this).hide();
						})
						var items = $('#'+ct+' ul li');
						for(var i = 0; i < items.length; i++){
							if($(items[i]).attr('ec').indexOf(ec)>=0){
								$(items[i]).show();
							}
						}
					}
					$(function () {
						for(var i = 0; i < routeCategorys.length; i++){
							if(routeCategorys[i][0] == '36666E8E0C9EEF38E050007F0100469B' || routeCategorys[i][0] == '36666E8E0C9FEF38E050007F0100469B' || routeCategorys[i][0] == '36666E8E0CA1EF38E050007F0100469B'){
								$.ajax({  
									type: "post",
									url:'${ctx}/index/routes?PID='+routeCategorys[i][0]+'&ID='+routeCategorys[i][1]+"&ORDER_BY="+routeCategorys[i][2]+"&s_start_city=${s_start_city}",
									dataType:"json",
									success: function(d, textStatus){
										
										var routes = d.data[0].hotRoutes, routeEndCityHTML = '', a = 1, CITY_NAME = '', PID = d.data[0].PID, tag = 'tabgny', cityTag = 'gnyTag', contentTag = 'gny-content';
										
										if(PID == '36666E8E0CA1EF38E050007F0100469B'){
											tag = 'tabzby', cityTag = 'zbyTag', contentTag = 'zby-content';
										}else if(PID == '36666E8E0C9FEF38E050007F0100469B'){
											tag = 'tabcjy', cityTag = 'cjyTag', contentTag = 'cjy-content';
										}
										for(var l = 0; l < routes.length; l++){	
											var endCity = routes[l].END_CITY.split(",");
											for(var j = 0; j < endCity.length; j++){
												var border_css = '';
												if(a==4){
													border_css = 'border';
												}
												if(endCity[j] != null && endCity[j] != '' && CITY_NAME.indexOf(endCity[j]) < 0){
													routeEndCityHTML += '<li class="'+border_css+'"><a href="#" ct="'+contentTag+'" ec="'+endCity[j]+'" onclick="showCityRoutes(this)" data-toggle="tab">'+endCity[j]+'</a></li>';
													CITY_NAME = CITY_NAME+","+endCity[j];
													a++;
												}
											}
											
										}
										$('.'+cityTag).append(routeEndCityHTML);
										
										var routeHTML = '';
										routeHTML += '<ul class="tab-pane fade in active mtour-list">';
										for(var l = 0; l < routes.length; l++){
											routeHTML += '<li ec="'+routes[l].END_CITY+'">';
												routeHTML += '<a href="${ctx}/produce/route/detail?id='+routes[l].ID+'&routeType='+routes[l].TYPE+'">';
													routeHTML += '<div class="thumbnail mtour-box clearfix">';
														routeHTML += '<div class="mtour-img">';
												          	routeHTML += '<img src="${picctx}/'+routes[l].FACE+'">';
												          	if(routes[l].ATTR){
												          		var ATTR = routes[l].ATTR;
													          	if(ATTR == '品质'){
															  		attr_css = 'flag-pz';
															   	}else if(ATTR == '特价'){
															   		attr_css = 'flag-tj';
															   	}else if(ATTR == '纯玩'){
															   		attr_css = 'flag-cw';
															   	}else if(ATTR == '豪华'){
															   		attr_css = 'flag-hh';
															   	}else if(ATTR == '包机'){
															   		attr_css = 'flag-bj';
															   	}else if(ATTR == '自由行'){
															   		attr_css = 'flag-zyx';
															   	}else if(ATTR == '自驾游'){
															   		attr_css = 'flag-zyx';
															   	}
													          	routeHTML += '<i class="trv-flag '+attr_css+'">'+ATTR+'</i>';
												          	}
												          	
											          	routeHTML += '</div>';
											          	routeHTML += '<div class="caption">';
												            routeHTML += '<h3 class="mtour-protit">';
												            	routeHTML += routes[l].TITLE;
												            routeHTML += '</h3>';
												            routeHTML += '<div class="mtour-info clearfix">';
												            	routeHTML += '<div class="mtour-price">&yen;<em>'+routes[l].SUM_PRICE+'</em>起</div>';
											            	routeHTML += '<div class="mtour-order"></div>';
												            routeHTML += '</div>';
											          	routeHTML += '</div>';
											        routeHTML += '</div>';
											     routeHTML += '</a>';
										     routeHTML += '</li>';
										}	
										routeHTML += '</ul>';
										$('#'+contentTag).append(routeHTML);
											
										
									}	
								});
							}
							
						}
					});
				</script>		
				
				<!-- 标签 -->
				<ul class="nav nav-tabs navbar-left sectab gnyTag">
					<li class="active border"><a class="allRoute" href="#" ct="gny-content"  onclick="showCityRoutes(this, 'all')" cn="tabgny">全部国内</a></li>
					
				</ul>
				<!-- 标签 -->
				<ul class="nav nav-tabs navbar-left sectab cjyTag" style="display:none;">
					<li class="active border"><a class="allRoute" href="#" ct="cjy-content"  onclick="showCityRoutes(this, 'all')" cn="tabcjy">全部出境</a></li>
					
				</ul>
				<!-- 标签 -->
				<ul class="nav nav-tabs navbar-left sectab zbyTag" style="display:none;">
					<li class="active border"><a class="allRoute" href="#" ct="zby-content"  onclick="showCityRoutes(this, 'all')" cn="tabzby">全部周边</a></li>
					
				</ul>
			</div>	
			<div class="tab-content mtour-con">
				<div class="tab-pane fade in active" id="gny">
			        <div class="tab-content" id="gny-content">
						
					</div>
				</div>
					
				<div class="tab-pane fade" id="cjy">
					<div class="tab-content" id="cjy-content">
				        
			        </div>
				</div>
				<div class="tab-pane fade" id="zby">
					<div class="tab-content" id="zby-content">
				        
			        </div>
			    </div>
			</div>
		</div>
	</div>
<c:if test="${USER_AGENT eq 'mobile'}">
<!-- 迷你导航 -->
<div class="bottom-nav" id="footer-nav">
	<ul>
		<li style="width:16%"><a href="${ctx}/"><img src="${ctx}/resources/images/icon-home.png" width="40"><span>首&nbsp;页</span></a></li>
	  	<li style="width:17%"><a href="${ctx}/produce/route/list?categoryPID=36666E8E0C9EEF38E050007F0100469B"><img src="${ctx}/resources/images/icon-gn.png" width="40"><span>国内游</span></a></li>
	  	<li style="width:17%"><a href="${ctx}/produce/route/list?categoryPID=36666E8E0CA1EF38E050007F0100469B"><img src="${ctx}/resources/images/icon-zb.png" width="40"><span>周边游</span></a></li>
	  	<li style="width:17%"><a href="${ctx}/produce/route/list?categoryPID=36666E8E0C9FEF38E050007F0100469B"><img src="${ctx}/resources/images/icon-cj.png" width="40"><span>出境游</span></a></li>
	  	<li style="width:17%"><a href="${ctx}/store/list"><img src="${ctx}/resources/images/icon-wd.png" width="40"><span>门市地址</span></a></li>
	  	<li style="width:16%"><a href="${ctx}/user/sec/center/main"><img src="${ctx}/resources/images/icon-user.png" width="40"><span>我&nbsp;的</span></a></li>
	</ul>
</div>
</c:if>
</body>    
</html>
