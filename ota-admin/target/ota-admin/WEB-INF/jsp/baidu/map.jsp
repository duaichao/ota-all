<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>  
<html>  
<head>  
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta charset="UTF-8">
<%@ include file="/commons/meta-ext.jsp"%>
<title>百度地图</title>  
<style type="text/css">  
html,body,div,span,p,h3,a,pre,input{font:12px/13px tahoma, geneva, "\5fae\8f6f\96c5\9ed1", "\5b8b\4f53";padding:0px;margin:0px;color:#333;}
html{height:100%}  
body{height:100%;margin:0px;padding:0px}  
#container{height:334px;}  
#r-result{
	height:45px;line-height:45px;
	border-bottom: 1px solid #e5e5e5;
    background-color: #f5f5f5;
	padding:8px 10px 5px 10px;
	position:relative;
}
#r-result input{
	height:20px;
	padding:2px 5px;
	color:#333;
}
#r-result a{
  padding: 0 10px;
  height: 35px;
  line-height: 35px;
  text-align: center;
  background: #4d90fe;
  display: inline-block;
  border: 0;
  cursor: pointer;
  border-radius: 3px;
  text-decoration:none;
  position:absolute;
  right:10px;
  top:12px;
}
#r-result a span{
	font-size: 14px;
	color: #fff;
}
</style>  
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=CUy6IDFxmATepSpyZK0HHoMA"></script>
</script>
</head>  
 
<body>  
<div id="r-result">
搜索：<input type="text" id="suggestId" size="20" value="百度" style="width:360px;" />
<span style="color:#dd4b39;padding-left:10px;">拖拽标注点，选择你的位置</span>
<a href="javascript:;" onclick="savePoint()"><span>保存位置</span></a>
</div>
<div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
<div id="container"></div> 

<script type="text/javascript"> 
	// 百度地图API功能
	var lng=('${longitude}'==''?'108.953449':'${longitude}'),
		lat=('${latitude}'==''?'34.265747':'${latitude}');
	var map = new BMap.Map("container");
	var point = new BMap.Point(lng,lat);
	map.centerAndZoom(point,18);
	//单击获取点击的经纬度
	//map.addEventListener("click",function(e){
	//	alert(e.point.lng + "," + e.point.lat);
	//});
	if('${longitude}'==''&&'${latitude}'==''){
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
	
	
	function G(id) {
		return document.getElementById(id);
	}
	var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
		{"input" : "suggestId"
		,"location" : map
	});

	ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
	var str = "";
		var _value = e.fromitem.value;
		var value = "";
		if (e.fromitem.index > -1) {
			value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		}    
		str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
		
		value = "";
		if (e.toitem.index > -1) {
			_value = e.toitem.value;
			value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		}    
		str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
		G("searchResultPanel").innerHTML = str;
	});

	var myValue;
	ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
	var _value = e.item.value;
		myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
		
		setPlace();
	});

	function setPlace(){
		map.clearOverlays();    //清除地图上所有覆盖物
		function myFun(){
			var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
			map.centerAndZoom(pp, 18);
			var mk = new BMap.Marker(pp);
			map.addOverlay(mk);    //添加标注
			mk.enableDragging();
			mk.setAnimation(BMAP_ANIMATION_BOUNCE);
			endAnim(mk);
		}
		var local = new BMap.LocalSearch(map, { //智能搜索
		  onSearchComplete: myFun
		});
		local.search(myValue);
	}
	
	function endAnim(mk){
		setTimeout(function(){
			mk.setAnimation(null);
		},1500);
	}
	function savePoint(){
		var mks = map.getOverlays();
		if(mks.length>0){
			var point = mks[0].point;
			Ext.getBody().mask('保存位置中...');
			Ext.Ajax.request({
				url:cfg.getCtx()+'/site/company/set/point',
				params:{id:'${param.companyId}',longitude:point.lng,latitude:point.lat},
				success:function(response){
					Ext.getBody().unmask();
					util.success('保存位置成功');
				}
			});
		}
		
	}
</script>  
</body>  
</html>