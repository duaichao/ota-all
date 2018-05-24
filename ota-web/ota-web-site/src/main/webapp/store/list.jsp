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
	<link rel="stylesheet" href="${ctx}/resources/css/index.css">
	<link rel="stylesheet" href="${ctx}/resources/css/supplier.css">
	
	<%@ include file="../commons/meta-js.jsp"%>
	<script type="text/javascript">
		$(function () {
			$('[data-toggle="tooltip"]').tooltip();
		})
	</script>
	<c:if test="${USER_AGENT eq 'pc'}"><base target="_blank"></c:if>
</head>
<body>
	<c:set var="header_title" value="门店列表" />
	<%@ include file="../commons/header.jsp"%>
	<!-- 焦点图 -->
	
	<!-- 供应商 -->
	<div class="main-box container">
		<!-- 面包屑 -->
		<ol class="breadcrumb">
		  	<li><a target="_self" href="${ctx}/">首页</a></li>
		  	<li class="active">门市地址</li>
		</ol>
		<!-- 门市 -->
		<div class="tab-content trv-allbox">
			<div class="tab-pane active row">
				<c:set var="companyName" value="" />
				<c:set var="counselorCnt" value="0" />
				
				<c:forEach items="${stores}" var="store" varStatus="i">
				
				<c:if test="${store.COMPANY eq companyName}">
					<c:set var="counselorCnt" value="${counselorCnt+1}" />
				</c:if>
				<c:if test="${store.COMPANY ne companyName}">
					<c:set var="counselorCnt" value="0" />
				</c:if>
				<c:if test="${counselorCnt <= 1}">
				
				<c:if test="${store.COMPANY ne companyName && not empty companyName}"></div></div></div></c:if>
				<c:if test="${store.COMPANY ne companyName}">
				<div class="col-md-6">
					<div class="thumbnail trv-box clearfix">
						<div class="trv-img">
				          	<a href="${ctx}/store/detail?storeId=${store.COMPANY_ID}"><img src="${picctx}/${store.LOGO_URL}" style="width:100%;height:315px;"></a>
				          	<div class="sup-name">
				          		<p>店名：${store.COMPANY}</p>
				          		<p>地址：${store.ADDRESS}</p>
				          	</div>
			          	</div>
			          	<div class="caption">
				</c:if>
			          	
				            <div class="trv-guider border-none col-xs-6">
				            	<div class="sup-info" style="width: 50%">
				            		<a href="${ctx}/store/detail?storeId=${store.COMPANY_ID}"><img src="${picctx}/${store.FACE }" style="width:60px;height:60px;" class="img-circle trver-img"><p class="trver-name">${store.CHINA_NAME}</p></a>
				            	</div>
				            	<c:if test="${USER_AGENT eq 'pc'}">
				            	<div class="sup-info" style="width: 50%">
				            		<c:if test="${not empty store.QQ1}"><a href="http://wpa.qq.com/msgrd?v=3&amp;uin=${store.QQ1}&amp;site=qq&amp;menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:${store.QQ1}:51" alt="我们竭诚为您服务！" title="我们竭诚为您服务！"></a></c:if>
				            		<c:if test="${not empty store.QQ2}"><a href="http://wpa.qq.com/msgrd?v=3&amp;uin=${store.QQ2}&amp;site=qq&amp;menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:${store.QQ2}:51" alt="我们竭诚为您服务！" title="我们竭诚为您服务！"></a></c:if>
				            		<p class="sup-phone"><span class="glyphicon glyphicon-phone" aria-hidden="true"></span>${store.PHONE}</p>
				            	</div>
				            	</c:if>
				            	<c:if test="${USER_AGENT eq 'mobile'}">
				            	<div class="sup-infonum">
				            		<p><img src="${ctx}/resources/images/QQ.png" width="20"></p>
				            		<c:if test="${not empty store.QQ1}"><p>${store.QQ1}</p></c:if>
				            		<c:if test="${not empty store.QQ2}"><p>${store.QQ2}</p></c:if>
				            		<p class="sup-phone" style="padding-top: 4px;">${store.PHONE}</p>
				            	</div>
				            	</c:if>
				            </div>
				<c:if test="${fn:length(stores) == (i.index+1)}">    
			          	</div>
			        </div>
		        </div>
		        </c:if>
		        <c:set var="companyName" value="${store.COMPANY}" />
		        </c:if>
		        </c:forEach>
			</div>
		</div>
	</div>
	<div class="main-box container">
		<!-- 地图  -->
		<h1 class="suphot-tit">门市地址</h1>
	 	<div id="mapBox">
	        <div class="mapArea">
	          	<div class="wayList col-md-3">
		            <div class="inner">
		              	<ul class="wayMenu clearfix">
		                	<li class="current"><a href="#busWay" target="_self" title="公交换乘">公交换乘</a></li>
	                	<li><a href="#carWay" target="_self" title="驾车路线">驾车路线</a></li>
		              	</ul>
		              	<div id="result"></div>
		              	<div class="wayForm">
			              	<ul>
								<li>
									<label>出发地</label>
									<input class="form-control" type="text" id="start" placeholder="出发地">
								</li>
								<li>
									<label>目的地</label>
									<select class="form-control" id="end">
										<c:set var="companyName" value="" />
										<c:forEach items="${stores}" var="store" varStatus="i">
										<c:if test="${store.COMPANY ne companyName || empty companyName}">
									  	<option sn="${store.COMPANY}" lng="${store.LONGITUDE}" lat="${store.LATITUDE}">${store.COMPANY}</option>
									  	</c:if>
									  	<c:set var="companyName" value="${store.COMPANY}" />
									  	</c:forEach>
									</select>
								</li>
								<li class="last">
									<img alt="查询公交换乘" src="${ctx}/resources/images/ajax-loader.gif" class="miniloading">
									<button type="submit" id="sbtn" class="btn btn-success" onclick="addAllPoint()" title="全部门市地址">全部门市</button>	
									<button type="submit" id="sbtn" class="btn btn-success searchRouteLine" title="查询公交换乘">查询</button>
								</li>
			              	</ul>
		              	</div>
		              	
		              	<script type="text/javascript">
						$(function () { 
						
							initMap();
							
							//路线切换Tab
							$(".wayMenu li").click(function() {
								
								$(".wayMenu li").removeClass("current");
								$(this).addClass("current"); 
								$(".wayCon ul").hide(); 
								var activeTab = $(this).find("a").attr("href"); 
								$(activeTab).show();
								
								$('#route_line_detail').html('');
								
								initMap();
								return false;
							});	
								
						})
					</script>
				
	              	<div class="wayCon">
	                	<div class="wayDeatil">
	                  		<ul class="nav nav-tabs way-list" id="busWay">
                      			<li style="width: 25%;" class="active"><a href="#route_line_detail" target="_self" data-toggle="tab">时间短</a></li>
                      			<li style="width: 25%;"><a href="#route_line_detail" target="_self" data-toggle="tab">少换乘</a></li>
                      			<li style="width: 25%;"><a href="#route_line_detail" target="_self" data-toggle="tab">少步行</a></li>
                      			<li style="width: 25%;"><a href="#route_line_detail" target="_self" data-toggle="tab">不乘地铁</a></li>
	                  		</ul>
	                  		
	                  		<ul class="nav nav-tabs way-list" style="display:none;" id="carWay">
                    			<li style="width: 25%;" class="active"><a href="#route_line_detail" target="_self" data-toggle="tab">推荐路线</a></li>
                    			<li style="width: 25%;"><a href="#route_line_detail" target="_self" data-toggle="tab">最短时间</a></li>
                      			<li style="width: 25%;"><a href="#route_line_detail" target="_self" data-toggle="tab">最短路程</a></li>
                      			<li style="width: 25%;"><a href="#route_line_detail" target="_self" data-toggle="tab">不走高速</a></li>
	                  		</ul>
	                  		
	                  		<div class="tab-content">
		                  		<div class="tab-pane fade in active" id="route_line_detail"></div>
	                  		</div>
	                	</div>
	              	</div>
	              	
		            </div>
	          	</div>
	          	<div class="mapBox col-md-9">
	            	<div class="mapIn" id="map">
						
					</div>
	          	</div>
	        </div>
	     	</div>
     	</div>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=CUy6IDFxmATepSpyZK0HHoMA"></script>
<script type="text/javascript"> 
// 百度地图API功能
	var s = $("#end").find("option:selected"),
	lng=s.attr('lng')?s.attr('lng'):'',
		lat=s.attr('lat')?s.attr('lat'):'',
		point = null,
		map = null;
	
	function addAllPoint(){
		map.clearOverlays();
		$("#end").children().each(function(){
			var _point = new BMap.Point($(this).attr('lng'),$(this).attr('lat'));
			var label = new BMap.Label($(this).attr('sn'), {
			  position : _point,    // 指定文本标注所在的地理位置
			  offset   : new BMap.Size(10, -20)    //设置文本偏移量
			});  // 创建文本标注对象
			label.setStyle({
				 color : "blue",
				 fontSize : "15px",
				 height : "20px",
				 lineHeight : "20px",
				 fontFamily:"微软雅黑"
			 });
			map.addOverlay(label);   
			
			var marker = new BMap.Marker(_point);  // 创建标注
			map.addOverlay(marker);               // 将标注添加到地图中
	
		});
		
		map.setZoom(15);
	}
	
	function initMap(){
		map = new BMap.Map("map"),
		point = new BMap.Point(lng,lat);
		map.centerAndZoom(point,18);
		if(lng ==''&& lat ==''){
			var geolocation = new BMap.Geolocation();
			geolocation.getCurrentPosition(function(r){
				if(this.getStatus() == BMAP_STATUS_SUCCESS){
					map.clearOverlays();
					var mk = new BMap.Marker(r.point);
					map.addOverlay(mk);
					map.panTo(r.point);
					mk.enableDragging();
					mk.setAnimation(BMAP_ANIMATION_BOUNCE);
					endAnim(mk);
				}
				else {
					console.log('failed'+this.getStatus());
				}        
			},{enableHighAccuracy: true})
		}else{
			map.clearOverlays();
			var mk = new BMap.Marker(point);
			map.addOverlay(mk);
			map.panTo(point);
			mk.enableDragging();
			mk.setAnimation(BMAP_ANIMATION_BOUNCE);
			endAnim(mk);
		}
	}
	
	function endAnim(mk){
		setTimeout(function(){
			mk.setAnimation(null);
		},1500);
	}
	/**
	 公交线路排序
	 1.BMAP_TRANSIT_POLICY_LEAST_TIME 				时间最短
	 2.BMAP_TRANSIT_POLICY_LEAST_TRANSFER			少换乘
	 3.BMAP_TRANSIT_POLICY_LEAST_WALKING			少步行
	 4.BMAP_TRANSIT_POLICY_AVOID_SUBWAYS			不坐地铁
	 */
			
	$('.searchRouteLine').click(function(){
		$('#route_line_detail').html('');
		searchRouteLine();
	});
	$('.wayDeatil li').click(function(){
		$('#route_line_detail').html('');
		setTimeout(function(){
			searchRouteLine();	
		},500)
	});
	
	function searchRouteLine(){
	var ts = $("#end").find("option:selected"),
	    tlng=ts.attr('lng')?ts.attr('lng'):'',
		tlat=ts.attr('lat')?ts.attr('lat'):'',
		tpoint = new BMap.Point(tlng,tlat);
			
		map.clearOverlays();
		var start = $('#start').val();
		var c = $('.wayCon:visible').attr('class');
		var index = 0;
		var li = $('.wayCon:visible ul:visible li').each(function(){
			var c = $(this).attr('class')
			if(c == 'active'){
				index = $(this).index();
				return false;
			}
		});
		if($('.wayCon:visible ul:visible').attr('id').indexOf('busWay') >= 0){
			var transit = new BMap.TransitRoute(map, {
				renderOptions: {map: map, panel:  'route_line_detail'},
				policy: 0
			}); 
			var routePolicy = [BMAP_TRANSIT_POLICY_LEAST_TIME,BMAP_TRANSIT_POLICY_LEAST_TRANSFER,BMAP_TRANSIT_POLICY_LEAST_WALKING,BMAP_TRANSIT_POLICY_AVOID_SUBWAYS];
			transit.setPolicy(routePolicy[index]);
			transit.search(start, tpoint);
		}else{
			var driving = new BMap.DrivingRoute(map, {
				renderOptions: {map: map, autoViewport: true, panel:  'route_line_detail'}
			});
			var routePolicy = [BMAP_DRIVING_POLICY_LEAST_TIME,BMAP_DRIVING_POLICY_LEAST_DISTANCE,BMAP_DRIVING_POLICY_AVOID_HIGHWAYS];
			if(index > 0){
				driving.setPolicy(routePolicy[index-1]);
			}
			driving.search(start, tpoint);
		}
	}
</script> 

	<!-- 页尾 -->
	<%@ include file="../commons/footer.jsp"%>
</body>    
</html>
