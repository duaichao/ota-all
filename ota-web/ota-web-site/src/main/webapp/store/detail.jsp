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
	<c:set var="header_title" value="门店详情" />
	<%@ include file="../commons/header.jsp"%>
	<!-- 焦点图 -->
	<div class="main-box container">
	<!-- 面包屑 -->
	<ol class="breadcrumb">
	  	<li><a target="_self" href="${ctx}/">首页</a></li>
	  	<li><a target="_self" href="${ctx}/store/list">门市地址</a></li>
	  	<li class="active">${store.COMPANY}</li>
	</ol>
	<!-- 品牌 -->
	<div class="supplier-tit">
		<div class="suptit-left col-md-3">
			<h1>${store.COMPANY}</h1>
			<p>许可证号： ${store.COMPANY}</p>
			<p>门市地址： ${store.ADDRESS}</p>
		</div>
		<div class="suptit-middle col-md-7">
			<p>门店简介：  ${store.BUSINESS}</p>
		</div>
		<div class="suptit-right col-md-2">
			<p><span class="glyphicon glyphicon-phone-alt" aria-hidden="true"></span>&nbsp;服务电话</p>
			<h1>${store.PHONE}</h1>
		</div>
	</div>
	<!-- 推荐 -->
	<h1 class="suphot-tit">热门推荐</h1>
	<div class="tab-content">
		<!-- 热门推荐 -->
		<div class="tab-pane active row">
			<c:forEach items="${hotRoute}" var="route" end="3">
			<div class="col-md-3">
				<div class="thumbnail trv-box clearfix">
					<div class="trv-img">
			          	<a href="${ctx}/produce/route/detail?id=${route.ID}&routeType=${route.TYPE}"><img src="${picctx}/${route.FACE}" width="640"></a>
			          	<c:if test="${route.ATTR eq '品质'}"><i class="trv-flag flag-pz">${route.ATTR}</i></c:if>
		          		<c:if test="${route.ATTR eq '特价'}"><i class="trv-flag flag-tj">${route.ATTR}</i></c:if>
		          		<c:if test="${route.ATTR eq '纯玩'}"><i class="trv-flag flag-cw">${route.ATTR}</i></c:if>
		          		<c:if test="${route.ATTR eq '豪华'}"><i class="trv-flag flag-hh">${route.ATTR}</i></c:if>
		          		<c:if test="${route.ATTR eq '包机'}"><i class="trv-flag flag-bj">${route.ATTR}</i></c:if>
		          		<c:if test="${route.ATTR eq '自由行'}"><i class="trv-flag flag-zyx">${route.ATTR}</i></c:if>
		          		<c:if test="${route.ATTR eq '自驾游'}"><i class="trv-flag flag-zjy">${route.ATTR}</i></c:if>
		          	</div>
		          	<div class="caption">
			            <div class="trv-title">
			            	<a href="${ctx}/produce/route/detail?id=${route.ID}&routeType=${route.TYPE}" class="trv-boxtit">${route.TITLE}</a>
			            </div>
			            <div class="trv-info clearfix">
			            	<div class="trv-price">&yen;<em>${route.SUM_PRICE}</em>元</div>
					        <div class="trv-order"></div>
			            </div>
		          	</div>
		        </div>
	        </div>
	        </c:forEach>
		</div>
	</div>
	<!-- 导游 -->
	<h1 class="suphot-tit">推荐顾问</h1>
	<div class="row">
	
		<c:forEach items="${counselors}" var="counselor">
        <div class="col-md-2 col-xs-6">
			<div class="thumbnail trver-box clearfix">
	          <img src="${picctx}/${counselor.FACE}" width="140" class="img-circle trver-img">
	          <div class="caption">
	            <div class="trver-title">
	            	<p><span class="trver-name">${counselor.CHINA_NAME}</span></p>
	            	<c:if test="${USER_AGENT eq 'pc'}">
	            	<c:if test="${not empty counselor.QQ1}"><p><a href="http://wpa.qq.com/msgrd?v=3&amp;uin=${counselor.QQ1}&amp;site=qq&amp;menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:${counselor.QQ1}:51" alt="我们竭诚为您服务！" title="我们竭诚为您服务！"></a></p></c:if>
	            	<c:if test="${not empty counselor.QQ2}"><p><a href="http://wpa.qq.com/msgrd?v=3&amp;uin=${counselor.QQ2}&amp;site=qq&amp;menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:${counselor.QQ2}:51" alt="我们竭诚为您服务！" title="我们竭诚为您服务！"></a></p></c:if>
	            	</c:if>
	            	<c:if test="${USER_AGENT eq 'mobile'}">
	            	<c:if test="${not empty counselor.QQ1}"><p><img src="${ctx}/resources/images/QQ.png" width="20">${counselor.QQ1}</p></c:if>
	            	<c:if test="${not empty counselor.QQ2}"><p><img src="${ctx}/resources/images/QQ.png" width="20">${counselor.QQ2}</p></c:if>
	            	</c:if>
	            	<c:if test="${not empty counselor.PHONE}"><p class="sup-phone"><span class="glyphicon glyphicon-phone" aria-hidden="true"></span>${counselor.PHONE}</p></c:if>
	            </div>
	            <div class="trver-con">
	            	${counselor.SIGNATURE}
	            </div>
	          </div>
	        </div>
        </div>
        </c:forEach>
        
	</div>
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
								  	<option>${store.COMPANY}</option>
								</select>
							</li>
							<li class="last">
								<img alt="查询公交换乘" src="${ctx}/resources/images/ajax-loader.gif" class="miniloading">
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
                      			<li style="width: 25%;" class="active"><a href="#route_line_detail" target="_self"  data-toggle="tab">时间短</a></li>
                      			<li style="width: 25%;"><a href="#route_line_detail" target="_self"  data-toggle="tab">少换乘</a></li>
                      			<li style="width: 25%;"><a href="#route_line_detail" target="_self"  data-toggle="tab">少步行</a></li>
                      			<li style="width: 25%;"><a href="#route_line_detail" target="_self"  data-toggle="tab">不乘地铁</a></li>
	                  		</ul>
	                  		
	                  		<ul class="nav nav-tabs way-list" style="display:none;" id="carWay">
                    			<li style="width: 25%;" class="active"><a href="#route_line_detail" data-toggle="tab">推荐路线</a></li>
                    			<li style="width: 25%;"><a href="#route_line_detail" target="_self"  data-toggle="tab">最短时间</a></li>
                      			<li style="width: 25%;"><a href="#route_line_detail" target="_self"  data-toggle="tab">最短路程</a></li>
                      			<li style="width: 25%;"><a href="#route_line_detail" target="_self"  data-toggle="tab">不走高速</a></li>
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
	var lng='${store.LONGITUDE}',
		lat='${store.LATITUDE}',
		point = null,
		map = null;
	
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
			transit.search(start, point);
		}else{
			var driving = new BMap.DrivingRoute(map, {
				renderOptions: {map: map, autoViewport: true, panel:  'route_line_detail'}
			});
			var routePolicy = [BMAP_DRIVING_POLICY_LEAST_TIME,BMAP_DRIVING_POLICY_LEAST_DISTANCE,BMAP_DRIVING_POLICY_AVOID_HIGHWAYS];
			if(index > 0){
				driving.setPolicy(routePolicy[index-1]);
			}
			driving.search(start, point);
		}
	}
</script> 

	<!-- 页尾 -->
	<%@ include file="../commons/footer.jsp"%>
</body>    
</html>
