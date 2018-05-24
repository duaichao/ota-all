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
	
	<link rel="stylesheet" href="${ctx}/resources/css/bootstrap.min.css">
	<link rel="stylesheet" href="${ctx}/resources/js/bootstrap/dialog/css/bootstrap-dialog.css" />
	<link rel="stylesheet" href="${ctx}/resources/css/reset.css">
	<link rel="stylesheet" href="${ctx}/resources/css/common.css">
	<link rel="stylesheet" href="${ctx}/resources/css/mininav.css" />
	<link rel="stylesheet" href="${ctx}/resources/css/detail.css">
	<link rel="stylesheet" href="${ctx}/resources/css/calendar.css">
	<link rel="stylesheet" href="${ctx}/resources/css/component.css" />
	
	<script type="text/javascript" src="${ctx}/resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap/dialog/js/bootstrap-dialog.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/toolbar.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/modernizr.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/polyfills.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/Calendar.js"></script>
	<script type="text/javascript">var tcalendar = eval('${route.calendarJson}');</script>
	<script type="text/javascript" src="${ctx}/resources/js/fun.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/stickUp.min.js"></script>
	<script type="text/javascript">
        
		var start_date = '', routeId = '${route.ID}', routeTitle = '${route.TITLE}', beginCity = '${route.BEGIN_CITY}', endCity = '${route.END_CITY}', d = '', childCnt = '', manCnt = '', subType = '1'; 
		
		$(function () {
		
			$('[data-toggle="tooltip"]').tooltip({html:true});
			$('#searchBtn').click(function(){
				$('#searchForm').submit();
			});
			$('#calendar').hover(function(){
				$('.select-list').show();
			},function(){
				$('.select-list').hide();
			});
			
			$('.select-list').hover(function(){
				$('.select-list').show();
			},function(){
				$('.select-list').hide();
			});
			
			$('.date').click(function(){
				$('#calendar p').attr('d', $(this).attr('d'));
				$('#calendar p').html($(this).html());
				$('.select-list').hide();
			});
			
			var clickType = '';
			
			$('.chooseNum').click(function(){
				
				subType = 1;
				
				$('#mySmallModalLabel').modal();
// 				document.location.href = '${ctx}/order/to/save?routeId=${route.ID}&routeTitle=${route.TITLE}&beginCity=${route.BEGIN_CITY}&endCity=${route.END_CITY}&d='+start_date+'&childCnt='+$('#childCnt').val()+'&manCnt='+$('#manCnt').val();
				
// 				if('${webUser}' != null && '${webUser}' != ''){
// 						document.location.href = '${ctx}/order/to/save?routeId=${route.ID}&routeTitle=${route.TITLE}&beginCity=${route.BEGIN_CITY}&endCity=${route.END_CITY}&d='+start_date;
// 				}else{
				
// 					var dialog = new BootstrapDialog({
// 			            message: function(dialogRef){
// 			                var $message = $('<div></div>').load('${ctx}/user/to/mini/login');
// 			                return $message;
// 			            },
// 			            closable: false
// 			        });
// 			        dialog.realize();
// 			        dialog.getModalHeader().hide();
// 			        dialog.getModalFooter().hide();
// 			        dialog.getModalBody().css('padding', '0');
// 			        dialog.open();
// 				}
			});
			
			if('${USER_AGENT}' == 'mobile'){
				$('.calendar td').css('height', '44px');
			}
		});
		
		function subOrder(){
		
			if(subType == 1){
				var USER_AGENT = '${USER_AGENT}';
				if(USER_AGENT == 'pc'){
					start_date = $('#calendar p').attr('d');	
				}else{
				    start_date = $("#calendar").find("option:selected").attr('d');
				}
			}
			
			childCnt = $('#childCnt').val(),
			manCnt = $('#manCnt').val();
				
			document.location.href = '${ctx}/order/to/save?routeId=${route.ID}&routeTitle=${route.TITLE}&beginCity=${route.BEGIN_CITY}&endCity=${route.END_CITY}&d='+start_date+'&childCnt='+childCnt+'&manCnt='+manCnt;
		}
			
	</script>
	<c:if test="${USER_AGENT eq 'pc'}"><base target="_blank"></c:if>
</head>
<body>
	<c:set var="header_title" value="线路详情" />
	<%@ include file="../../commons/header.jsp"%>
	<!-- 列表 -->
	<div class="main-box container">
		<!-- 面包屑 -->
		<ol class="breadcrumb">
		  	<li><a target="_self" href="${ctx}/">首页</a></li>
		  	<li class="active"><a target="_self" href="${ctx}/produce/route/list?categoryPID=${categoryPID}"><c:if test="${categoryPID eq '36666E8E0CA1EF38E050007F0100469B'}">周边游</c:if><c:if test="${categoryPID eq '36666E8E0C9EEF38E050007F0100469B'}">国内游</c:if><c:if test="${categoryPIDE eq '36666E8E0C9FEF38E050007F0100469B'}">出境游</c:if></a></li>
		</ol>
		<!-- 详情 -->
		<div class="thumbnail tour-main">
			<div class="tour-tit">
				<h1>${route.TITLE}</h1>
			</div>
			<div class="tour-con">
				<div class="tour-conleft col-md-6">
					<div id="myCarousel" class="carousel slide" data-ride="carousel">
						<ol class="carousel-indicators">
							<c:forEach items="${route.album}" var="album" varStatus="i">
						    <li data-target="#myCarousel" data-slide-to="${i.index -1}" <c:if test="${i.index eq 1}">class="active"</c:if>></li>
						    </c:forEach>
						</ol> 
						
						<div class="carousel-inner" role="listbox">
							<c:forEach items="${route.album}" var="album">
							<div class="item <c:if test="${album.IS_FACE eq 1}">active</c:if>">
								<img alt="" src="${picctx}/${album.IMG_PATH}" width="640" data-holder-rendered="true">
							</div>
							</c:forEach>
							<c:if test="${route.ATTR eq '品质'}"><i class="trv-flag flag-pz">${route.ATTR}</i></c:if>
			          		<c:if test="${route.ATTR eq '特价'}"><i class="trv-flag flag-tj">${route.ATTR}</i></c:if>
			          		<c:if test="${route.ATTR eq '纯玩'}"><i class="trv-flag flag-cw">${route.ATTR}</i></c:if>
			          		<c:if test="${route.ATTR eq '豪华'}"><i class="trv-flag flag-hh">${route.ATTR}</i></c:if>
			          		<c:if test="${route.ATTR eq '包机'}"><i class="trv-flag flag-bj">${route.ATTR}</i></c:if>
			          		<c:if test="${route.ATTR eq '自由行'}"><i class="trv-flag flag-zyx">${route.ATTR}</i></c:if>
			          		<c:if test="${route.ATTR eq '自驾游'}"><i class="trv-flag flag-zjy">${route.ATTR}</i></c:if>
						</div>
		    
						<a class="left carousel-control" href="#myCarousel" target="_self" role="button" data-slide="prev">
						    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
						    <span class="sr-only">Previous</span>
						</a>
						<a class="right carousel-control" href="#myCarousel" target="_self" role="button" data-slide="next">
						    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
						    <span class="sr-only">Next</span>
						</a>
					</div>
				</div>
				<div class="tour-conright col-md-6">
					<div class="tour-intro">
						<div class="tour-intro-left">
							<div class="total-price">
								&yen;<em>${route.SUM_PRICE}</em>起
							</div>
							<a href="javascript:void(0);" target="_self" class="tip-price" data-toggle="tooltip" data-placement="bottom" title="<p style='text-align:left;'>本起价是可选出发日期中，按双人出行共住一间房核算的最低单人价格。产品价格会根据您所选择的出发日期、出行人数、入住酒店房型、航班或交通以及所选附加服务的不同而有所差别。</p>">起价说明</a>
						</div>
						<c:if test="${USER_AGENT eq 'pc'}">
						<div class="tour-intro-right">
							<!-- <span>满意度<em>100%</em></span> -->
							<span><em></em></span><!-- 占位置 -->
							<!-- JiaThis Button BEGIN -->
							<div class="jiathis_style">
								<a class="jiathis_button_qzone"></a>
								<a class="jiathis_button_tsina"></a>
								<a class="jiathis_button_tqq"></a>
								<a class="jiathis_button_weixin"></a>
								<a href="http://www.jiathis.com/share" class="jiathis jiathis_txt jtico jtico_jiathis" target="_blank"></a>
								<a class="jiathis_counter_style"></a>
							</div>
							<script type="text/javascript" src="http://v3.jiathis.com/code_mini/jia.js" charset="utf-8"></script>
							<!-- JiaThis Button END -->
						</div>
						</c:if>
					</div>
					<dl style="width:60%">
						<dt>线路编号：</dt>
						<dd>${route.NO}</dd>
					</dl>
					<dl style="width:40%">
						<dt>出发城市：</dt>
						<dd>${route.BEGIN_CITY}</dd>
					</dl>
					<dl style="width:60%">
						<dt>抵达城市：</dt>
						<dd>${route.END_CITY}</dd>
					</dl>
					<dl style="width:40%">
						<dt>行程天数：</dt>
						<dd>${route.DAY_COUNT}天</dd>
					</dl>
					
					<c:forEach items="${route.calendar}" var="calendar" end="0">
						<fmt:formatDate value="${calendar.D}" pattern="yyyy" var="start_year"/>
						<fmt:formatDate value="${calendar.D}" pattern="MM" var="start_month"/>
						<fmt:formatDate value="${calendar.D}" pattern="yyyy-MM-dd" var="start_date"/>
					</c:forEach>
					
					<c:if test="${USER_AGENT eq 'pc'}">
					<div class="select-date">
						<div class="select-con" id="calendar">
							<c:forEach items="${route.calendar}" var="calendar" end="0">
								<p d="<fmt:formatDate value="${calendar.D}" pattern="yyyy-MM-dd" />"><fmt:formatDate value="${calendar.D}" pattern="MM-dd" /> (${calendar.WEEK}) ${calendar.ACTUAL_PRICE}元/成人</p>
								<span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
							</c:forEach>
							<ul class="select-list" style="display: none;">
								<c:forEach items="${route.calendar}" var="calendar" end="4">
								<li class="date" d="<fmt:formatDate value="${calendar.D}" pattern="yyyy-MM-dd" />"><fmt:formatDate value="${calendar.D}" pattern="MM-dd" /> (${calendar.WEEK}) ${calendar.ACTUAL_PRICE}元/成人 </li>
								</c:forEach>
							</ul>
						</div>
					</div>
					</c:if>
					
					
					<c:if test="${USER_AGENT eq 'mobile'}">
					<select class="form-control" id="calendar" name="calendar">
						<c:forEach items="${route.calendar}" var="calendar" end="4">
					    <option d="<fmt:formatDate value="${calendar.D}" pattern="yyyy-MM-dd" />"><fmt:formatDate value="${calendar.D}" pattern="MM-dd" /> (${calendar.WEEK}) ${calendar.ACTUAL_PRICE}元/成人</option>
					    </c:forEach>
				    </select>
					</c:if>
					
					
					<!-- 选择人数 -->
						<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" id="mySmallModalLabel" aria-hidden="true">
						  <div class="modal-dialog modal-sm" style="margin-top: 300px;">
						    <div class="modal-content">
						      <div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
									<h3 class="modal-title" id="myModalLabel">选择游客人数</h3>
								</div>
								<div class="modal-body">
									<div class="col-xs-4 perchoose">
										<select class="form-control input-sm" id="manCnt">
										  <option>0</option>
										  <option selected="selected">1</option>
										  <option>2</option>
										  <option>3</option>
										  <option>4</option>
										  <option>5</option>
										  <option>6</option>
										  <option>7</option>
										  <option>8</option>
										  <option>9</option>
										  <option>10</option>
										</select>
									</div>
									<div class="col-xs-2 perchoose">成人</div>
									<div class="col-xs-4 perchoose">
										<select class="form-control input-sm" id="childCnt">
										  <option>0</option>
										  <option>1</option>
										  <option>2</option>
										  <option>3</option>
										  <option>4</option>
										  <option>5</option>
										  <option>6</option>
										  <option>7</option>
										  <option>8</option>
										  <option>9</option>
										  <option>10</option>
										</select>
									</div>
									<div class="col-xs-2 perchoose">儿童</div>
									<a href="javascript: subOrder();" target="_self" class="order-btn">立即预定</a>
								</div>
						    </div>
						  </div>
						</div>
					
					<c:if test="${USER_AGENT eq 'pc'}">
					<div style="width: 100%; float: left;">
						<div class="order-now">
							<button type="button" class="btn order-btn chooseNum" data-toggle="modal">立即预定</button>
						</div>
						
<!-- 						<div class="order-kf"> -->
<!-- 							<c:forEach items="${counselors}" var="counselor" end="1"> -->
<!-- 								<p> -->
<!-- 									<c:if test="${not empty counselor.QQ1}"> -->
<!-- 									<a target="blank" class="qq" href="http://wpa.qq.com/msgrd?V=3&amp;uin=${company.QQ1}&amp;Site=QQ客服&amp;Menu=yes"> -->
<!-- 										<img border="0" src="http://wpa.qq.com/pa?p=2:${counselor.QQ1}:46" alt="点击这里给我发消息"> -->
<!-- 									</a> -->
<!-- 									</c:if> -->
<!-- 									<c:if test="${empty counselor.QQ1 && not empty counselor.QQ2}"> -->
<!-- 									<a target="blank" class="qq" href="http://wpa.qq.com/msgrd?V=3&amp;uin=${company.QQ1}&amp;Site=QQ客服&amp;Menu=yes"> -->
<!-- 										<img border="0" src="http://wpa.qq.com/pa?p=2:${counselor.QQ2}:46" alt="点击这里给我发消息"> -->
<!-- 									</a> -->
<!-- 									</c:if> -->
<!-- 									<c:if test="${not empty counselor.PHONE}"> -->
<!-- 									<b><span class="glyphicon glyphicon-phone" aria-hidden="true"></span>&nbsp;${counselor.PHONE}</b> -->
<!-- 									</c:if> -->
<!-- 								</p> -->
<!-- 							</c:forEach> -->
<!-- 						</div> -->
						
					</div>
					</c:if>
					<div class="recom-inner">
						<b class="cny">产品推荐：</b>${route.FEATURE}
					</div>
				</div>
			</div>
			<!-- 日历选择 -->
			<div class="calendar">
				<div class="outer clearfix" id="calendarcontainer"> 
				</div>
			</div> 
			<script type="text/javascript">
				var f = 1;
				if('${USER_AGENT}' == 'mobile'){f=0;}
				//c:容器,y:年,m:月,a:出发时间json,f:是否显示双日历,fu:回调调 d1:最早时间,d2:最晚时间,f:显示双日历用1，单日历用0;
				//clickfu: //回调函数，to为点击对象，点击日期是调用的函数,参数to为点击的日期的节点对象，可以把用户选定的日期通过此函数存入服务端或cookies，具体请自行编写
				//showFu: //回调函数，d为要显示的当前日期，主要用于判断是否要在该日期的格子里显示出指定的内容，在日期格子里额外显示内容的函数,返回值必须为字符串，参数d为显示的日期对象（日期类型）
				var para = {'c':'calendarcontainer','y':${start_year},'m':${start_month},'a':{'d1':'${start_date}','d2':'2020-05-05'},'f':f,
					'clickfu':function (to) {
						subType = 2;
				    	if($(to).attr('price')!=""){
				    		start_date = to.id;
// 				   		 	if('${webUser}' != null && '${webUser}' != ''){
							    $('#mySmallModalLabel').modal();
// 								document.location.href = '${ctx}/order/to/save?routeId=${route.ID}&routeTitle=${route.TITLE}&beginCity=${route.BEGIN_CITY}&endCity=${route.END_CITY}&d='+to.id+'&childCnt='+childCnt+'&manCnt='+manCnt;
// 							}else{
// 								var dialog = new BootstrapDialog({
// 						            message: function(dialogRef){
// 						                var $message = $('<div></div>').load('${ctx}/user/to/mini/login');
// 						                return $message;
// 						            },
// 						            closable: false
// 						        });
// 						        dialog.realize();
// 						        dialog.getModalHeader().hide();
// 						        dialog.getModalFooter().hide();
// 						        dialog.getModalBody().css('padding', '0');
// 						        dialog.open();
// 							}
						}
					},
					'showFu':function(d){
						return "";
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
		</div>
		<!-- tab菜单 -->
		<c:if test="${USER_AGENT eq 'pc'}">
		<div class="navbar-wrapper" style="z-index: 99;">
		    <div class="navwrapper">
		        <div class="navbar navbar-inverse navbar-static-top">
		          	<div class="container">
			            <div class="navbar-header">
			              	<button type="button" class="navbar-toggle navbar-tabbtn" data-toggle="collapse" data-target=".navbar-collapse">
			                	<span class="icon-bar"></span>
			                	<span class="icon-bar"></span>
			                	<span class="icon-bar"></span>
			              	</button>
			            </div>
			            <div class="navbar-collapse collapse">
			              	<ul class="nav navbar-nav">
			                	
			                	<li class="menuItem active"><a target="_self" href="#product">产品特色</a></li>
			                	<li class="menuItem"><a target="_self" href="#feature">产品推荐</a></li>
			                	<li class="menuItem"><a target="_self" href="#introduction">行程介绍</a></li>
<!-- 			                	<li class="menuItem"><a target="_self" href="#info">销售须知</a></li> -->
			                	<li class="menuItem"><a target="_self" href="#pay">费用说明</a></li>
			                	<li class="menuItem"><a target="_self" href="#important">重要提醒</a></li>
			              	</ul>
			            </div>
		          	</div>
		        </div> 
		    </div>
		</div>
		</c:if>
	    <!-- 景点详情 -->
	    <script type="text/javascript">
	    	$(function(){
	    		$('.sidebarList li').click(function(){
					$('.sidebarList li').each(function(){
						$(this).removeClass('active');
					});
					$(this).addClass('active');	
				});
				$(document).ready( function() {
				 	  $('.navbar-wrapper').stickUp();
	                  $('.sidebarDay').stickUp();
	                  $("#introduction").css("margin-top", "-"+($("#dayNum li").length * 32)+"px");
	            });
	    	});
	    </script>
	    <div id="contents" style="margin-bottom: 60px;">

    		<c:if test="${not empty route.FEATURE}">
	    	<div id="product" class="trip-info">
    			<div class="day-list">
    				<div class="day-con">
    					<ul class="day-con-list">
    						<li>
    							<h3 class="resource-title col-xs-2">
    								<span>产品特色</span>
    								<span class="glyphicon glyphicon-fire icon-detail" aria-hidden="true"></span>
    							</h3>
    							<div class="resource-info col-xs-10">
    								<p>${route.FEATURE1}</p>
    							</div>
    						</li>
    					</ul>
    				</div>
    			</div>
	    	</div>
	    	</c:if>
	    	
    		<c:if test="${not empty route.FEATURE}">
	    	<div id="feature" class="trip-info">
    			<div class="day-list">
    				<div class="day-con">
    					<ul class="day-con-list">
    						<li>
    							<h3 class="resource-title col-xs-2">
    								<span>产品推荐</span>
    								<span class="glyphicon glyphicon-star icon-detail" aria-hidden="true"></span>
    							</h3>
    							<div class="resource-info col-xs-10">
    								<p>${route.FEATURE}</p>
    							</div>
    						</li>
    					</ul>
    				</div>
    			</div>
	    	</div>
	    	</c:if>
	    	
	    	<div id="introduction" class="trip-info" style="">
    			<!-- 天数 -->
   				<div id="dayNum" class="bs-docs-sidebar hidden-print hidden-xs hidden-sm affix-top sidebarDay" role="complementary">
		            <ul class="nav bs-docs-sidenav sidebarList">
		            	<c:forEach items="${route.citys}" var="city" varStatus="i">
			            <c:forEach items="${city.days}" var="day">
		                <li <c:if test="${day.NO eq 1}">class="active"</c:if>>
						  <a href="#route-day${day.NO}" target="_self">第${day.NO}天</a>
						</li>
						</c:forEach>
						</c:forEach>
		            </ul>
	          	</div>
    			<!-- 详情 -->
    			<c:forEach items="${route.citys}" var="city">
    			<div class="day-list">
   					<h1 class="day-place">
    					<span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span>
    					<span>${city.CITY_NAME}</span>
    					<c:if test="${not empty city.STAY_COUNT && city.STAY_COUNT ne 0}">
<!--     					游玩${city.STAY_COUNT}天 -->
    					</c:if>
    				</h1>
    				<c:forEach items="${city.days}" var="day">
    				<h1 class="day-title" id="route-day${day.NO}">
    					<span>第${day.NO}天 </span>
    					<c:if test="${not empty day.TITLE}">${day.TITLE}</c:if>
    					<c:if test="${empty day.TITLE}">
    					<span>${day.BEGIN_CITY}
    					<c:if test="${day.TOOL eq '飞机'}"><i class="route-icon plane"></i></c:if>
    					<c:if test="${day.TOOL eq '火车' || day.TOOL eq '动车' || day.TOOL eq '高铁'}"><i class="route-icon train"></i></c:if>
    					<c:if test="${day.TOOL eq '旅游车'}"><i class="route-icon bus"></i></c:if>
    					<c:if test="${day.TOOL eq '轮船'}"><i class="route-icon liner"></i></c:if>
    					${day.END_CITY} 
    					<c:if test="${day.TOOL1 eq '飞机'}"><i class="route-icon plane"></i></c:if>
    					<c:if test="${day.TOOL1 eq '火车' || day.TOOL1 eq '动车' || day.TOOL1 eq '高铁'}"><i class="route-icon train"></i></c:if>
    					<c:if test="${day.TOOL1 eq '旅游车'}"><i class="route-icon bus"></i></c:if>
    					<c:if test="${day.TOOL1 eq '轮船'}"><i class="route-icon liner"></i></c:if>
    					${day.END_CITY1}</span>
    					</c:if>
    				</h1>
    				<div class="day-con">
    					<ul class="day-con-list">
    						<li>
    							<h3 class="time-list col-xs-2">用餐</h3>
    							<div class="day-detail col-xs-10">
    								<span class="glyphicon glyphicon-cutlery icon-detail" aria-hidden="true"></span>
    								<p>早餐：<c:if test="${not empty day.BREAKFAST}">${day.BREAKFAST}</c:if><c:if test="${empty day.BREAKFAST}">无</c:if></p>
									<p>中餐：<c:if test="${not empty day.LUNCH}">${day.LUNCH}</c:if><c:if test="${empty day.LUNCH}">无</c:if></p>
									<p>晚餐：<c:if test="${not empty day.DINNER}">${day.DINNER}</c:if><c:if test="${empty day.DINNER}">无</c:if></p>
    							</div>
    						</li>
    						<c:if test="${not empty day.HOTEL_TIPS}">
    						<li>
    							<h3 class="time-list col-xs-2">住宿</h3>
    							<div class="day-detail col-xs-10">
    								<span class="glyphicon glyphicon-bed icon-detail" aria-hidden="true"></span>
    								<p>${day.HOTEL_TIPS}</p>
    							</div>
    						</li>
    						</c:if>
    						<c:if test="${not empty day.TODAY_TIPS || not empty day.PAY_TIPS}">
    						<li>
    							<h3 class="time-list col-xs-2">提示</h3>
    							<div class="day-detail col-xs-10">
    								<span class="glyphicon glyphicon-tag icon-detail" aria-hidden="true"></span>
    								<c:if test="${not empty day.TODAY_TIPS}"><p>当日提示：${day.TODAY_TIPS}</p></c:if>
    								<c:if test="${not empty day.PAY_TIPS}"><p>自费提示：${day.PAY_TIPS}</p></c:if>
    							</div>
    						</li>
    						</c:if>
    						<c:if test="${not empty day.details}">
    						<c:forEach items="${day.details}" var="detail">
	    						<li>
	    							<h3 class="time-list col-xs-2">${detail.TITLE}</h3>
	    							<div class="day-detail col-xs-10">
	    								<span class="glyphicon glyphicon-list-alt icon-detail" aria-hidden="true"></span>
	    								<div class="day-scene">
	    									<p id="detail${detail.ID}">${detail.CONTENT}</p>
	    								</div>
	    							</div>
	    						</li>
    						</c:forEach>
    						</c:if>
    					</ul>
    				</div>
    				</c:forEach>
    			</div>
    			</c:forEach>
    			
    		</div>
    		<%-- 
	    	<c:if test="${not empty route.NOTICE}">
	    	<div id="info" class="trip-info">
    			<div class="day-list">
    				<div class="day-con">
    					<ul class="day-con-list">
    						<li>
    							<h3 class="resource-title col-xs-2">
    								<span>销售须知</span>
    								<span class="glyphicon glyphicon-comment icon-detail" aria-hidden="true"></span>
    							</h3>
    							<div class="resource-info col-xs-10">
    								<p>${route.NOTICE}</p>
    							</div>
    						</li>
    					</ul>
    				</div>
    			</div>
	    	</div>
	    	</c:if>
	    	--%>
	    	<c:if test="${not empty route.include || not empty route.noclude}">
	    	<div id="pay" class="trip-info">
	    		<div class="day-list">
    				<div class="day-con">
    					<ul class="day-con-list">
    						<li>
    							<h3 class="resource-title col-xs-2">
    								<span>费用说明</span>
    								<span class="glyphicon glyphicon-shopping-cart icon-detail" aria-hidden="true"></span>
    							</h3>
    							<div class="resource-info col-xs-10">
    								<div class="resource-box">
	    								<h4>服务包含</h4>
	    								<c:forEach items="${route.include}" var="o">
	    								<p>${o.TITLE}：${o.CONTENT}</p>
	    								</c:forEach>
									</div>
									<div class="resource-box">
										<h4>服务不含</h4>
										<c:forEach items="${route.noclude}" var="o">
										<p>${o.TITLE}：${o.CONTENT}</p>
										</c:forEach>
									</div>
    							</div>
    						</li>
    					</ul>
    				</div>
    			</div>
	    	</div>
	    	</c:if>
	    	<c:if test="${not empty notice || not empty tips}">
	    	<div id="important" class="trip-info">
	    		<div class="day-list">
    				<div class="day-con">
    					<ul class="day-con-list">
    						<li>
    							<h3 class="resource-title col-xs-2">
    								<span>重要提醒</span>
    								<span class="glyphicon glyphicon-bell icon-detail" aria-hidden="true"></span>
    							</h3>
    							<div class="resource-info col-xs-10">
    								<div class="resource-box">
    									<h4>出行须知</h4>
    									<c:forEach items="${notice}" var="o">
										<p>${o.TITLE}：${o.CONTENT}</p>
										</c:forEach>
									</div>
									<div class="resource-box">
										<h4>温馨提示</h4>
										<c:forEach items="${tips}" var="o">
										<p>${o.TITLE}：${o.CONTENT}</p>
										</c:forEach>
									</div>
    							</div>
    						</li>
    					</ul>
    				</div>
    			</div>
	    	</div>
	    	</c:if>
	    </div>
	</div>
	
	<c:if test="${USER_AGENT eq 'mobile'}">
	<div class="bottom-tour">
		<ul>
			<li style="width:20%"><a target="_self" href="${ctx}/produce/route/list?routeType=${param.routeType}"><span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span></a></li>
			<li style="width:60%"><a href="#" target="_self" class="bottom-orderbtn order-btn chooseNum">立即预定</a></li>
			<li style="width:20%">
				<div class="btn-group dropup">
				  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				    <span class="caret"></span>
				    <span class="sr-only">Toggle Dropdown</span>
				  </button>
				  <ul class="dropdown-menu">
				    <li><a target="_self" href="#introduction">行程介绍</a></li>
				    <li><a target="_self" href="#feature">产品推荐</a></li>
				    <li><a target="_self" href="#info">销售须知</a></li>
				    <li><a target="_self" href="#pay">费用说明</a></li>
				    <li><a target="_self" href="#important">重要提醒</a></li>
				     <li><a target="_self" href="#product">产品特色</a></li>
				  </ul>
				</div>
			</li>
		</ul>
	</div>
	</c:if>
	
	<c:if test="${USER_AGENT eq 'pc'}">
	<%@ include file="../../commons/footer.jsp"%>
	</c:if>
</body>    
</html>
