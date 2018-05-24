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
	<link rel="stylesheet" href="${ctx}/resources/css/datepicker.css">
	
	<%@ include file="../commons/meta-js.jsp"%>
	<script type="text/javascript" src="${ctx}/resources/js/plus/jgestures.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/stickUp.min.js"></script>
	<script type="text/javascript">
		$(function () {
			$('[data-toggle="tooltip"]').tooltip();
			$('#myCarousel').bind('swiperight swiperightup swiperightdown',function(){
		        $("#myCarousel").carousel('prev');
		    })
		    $('#myCarousel').bind('swipeleft swipeleftup swipeleftdown',function(){
		        $("#myCarousel").carousel('next');
		    })
		    $(".item-section").hover(function () {
				$(this).next().show();
			}, function(){
				$(this).next().hide();
			});
			$(".item-box").hover(function () {
				$(this).show();
			}, function(){
				$(this).hide();
			});
			$('.icon-replace').click(function(){
				var start_sub = $('#start_sub').val(), end_sub = $('#end_sub').val();
				$('#start_sub').val(end_sub);
				$('#end_sub').val(start_sub); 
			});
			
			$('#start_date').datepicker({format: 'yyyy-mm-dd'});
			$('#startDate').datepicker({format: 'yyyy-mm-dd'});
			$('#endDate').datepicker({format: 'yyyy-mm-dd'});
			
			var webCategorys = eval('${webCategorys}');
			var h = "";
			for(var webCategory in webCategorys){
				var item_id = webCategorys[webCategory].ITEM_ID;
				h += $('#'+item_id).prop('outerHTML');
				$('#'+item_id).remove();
			}
			$('#aaa').after(h);
		});
	</script>
	<c:if test="${USER_AGENT eq 'pc'}"><base target="_blank"></c:if>
</head>
<body>
	<c:set var="header_title" value="${company.TITLE}" />
	<c:set var="header_index" value="YES" />
	<%@ include file="../commons/header.jsp"%>
	<div class="container mt10" id="aaa">
		<!-- 目录 -->
		<div class="header-catalog">
			<ul class="header-catalog-list">
				<li class="list-section">
					<a href="${ctx}/produce/route/list?categoryPID=36666E8E0C9EEF38E050007F0100469B" class="item-section">
						<i class="icon-gn"></i>
						<p>国内旅游</p>
						<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
					</a>
					<!-- 弹出 -->
					<div class="item-box" style="display: none;">
						<dl>
							<dt>海南</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=三亚">三亚</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=海口">海口</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=蜈支洲岛">蜈支洲岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=天堂森林公园">天堂森林公园</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=亚龙湾">亚龙湾</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=天涯海角">天涯海角</a></dd>
							<dt>广西</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=桂林">桂林</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=北海">北海</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=阳朔">阳朔</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=漓江">漓江</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=南宁">南宁</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=涠洲岛">涠洲岛</a></dd>
							<dt>云南</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=昆明">昆明</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=大理">大理</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=丽江">丽江</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=香格里拉">香格里拉</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=西双版纳">西双版纳</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=腾冲">腾冲</a></dd>
							<dt>北京</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=故宫">故宫</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=长城">长城</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=天安门">天安门</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=颐和园">颐和园</a></dd>
							<dt>天津</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=海河">海河</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=洋货市场">洋货市场</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=外滩">外滩</a></dd>
							<dt>华东</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=上海">上海</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=杭州">杭州</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=苏州">苏州</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=乌镇">乌镇</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=南京">南京</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=扬州">扬州</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=普陀山">普陀山</a></dd>
							<dt>四川</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=成龙">成都</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=九寨沟">九寨沟</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=黄龙">黄龙</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=峨眉山">峨眉山</a></dd>
							<dt>湖南</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=长沙">长沙</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=张家界">张家界</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=凤凰古城">凤凰古城</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=天子山">天子山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=袁家界">袁家界</a></dd>
							<dt>贵州</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=贵阳">贵阳</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=黄果树瀑布">黄果树瀑布</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=荔波">荔波</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=大小七孔">大小七孔</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=西江">西江</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=千户苗寨">千户苗寨</a></dd>
							<dt>重庆</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=渣滓洞">渣滓洞</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=武隆">武隆</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=磁器口古镇">磁器口古镇</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=红崖洞">红崖洞</a></dd>
							<dt>广东</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=长隆欢乐世界">长隆欢乐世界</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=深圳">深圳</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=世界之窗">世界之窗</a></dd>
							<dt>福建</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=厦门">厦门</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=鼓浪屿">鼓浪屿</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=武夷山">武夷山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=永定土楼">永定土楼</a></dd>
							<dt>东北</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=哈尔滨">哈尔滨</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=雪乡">雪乡</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=长白山">长白山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=沈阳">沈阳</a></dd>
							<dt>内蒙古</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=呼和浩特">呼和浩特</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=呼伦贝尔">呼伦贝尔</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=满洲里">满洲里</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=额济纳旗">额济纳旗</a></dd>
							<dt>青海</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=青海湖">青海湖</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=日月山">日月山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=丹葛尔古城">丹噶尔古城</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=西宁">西宁</a></dd>
							<dt>新疆</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=乌鲁木齐">乌鲁木齐</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=喀纳斯">喀纳斯</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=天山天地">天山天地</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=吐鲁番">吐鲁番</a></dd>
							<dt>甘肃</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=敦煌">敦煌</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=嘉峪关">嘉峪关</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=">兰州</a></dd>
							<dt>宁夏</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=沙坡头">沙坡头</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=沙湖">沙湖</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=西部影视城">西部影视城</a></dd>
							<dt>西藏</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=拉萨">拉萨</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=林芝">林芝</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=纳木错">纳木措</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=布达拉宫">布达拉宫</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=日哈则">日喀则</a></dd>
						</dl>
					</div>
				</li>
				<li class="list-section">
					<a href="${ctx}/produce/route/list?categoryPID=36666E8E0C9FEF38E050007F0100469B" class="item-section">
						<i class="icon-cj"></i>
						<p>出境旅游</p>
						<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
					</a>
					<!-- 弹出 -->
					<div class="item-box" style="display: none;">
						<dl>
							<dt>海岛</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=塞班岛">塞班岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=沙巴">沙巴</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=马尔代夫">马尔代夫</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=巴厘岛">巴厘岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=普吉岛">普吉岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=济州岛">济州岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=帕劳">帕劳</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=大溪地">大溪地</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=斐济">斐济</a></dd>
							<dt>东南亚</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=泰国曼谷">泰国曼谷</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=芭提雅">芭提雅</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=普吉岛">普吉岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=苏梅岛">苏梅岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=清迈">清迈</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=斯米兰群岛">斯米兰群岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=新加坡">新加坡</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=民丹岛">民丹岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=马来西亚">马来西亚</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=金浦寨吴哥">柬埔寨吴哥</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=越南岘港">越南岘港</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=芽庄">芽庄</a></dd>
							<dt>南亚</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=尼泊尔">尼泊尔</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=印度">印度</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=斯里兰卡">斯里兰卡</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=不丹">不丹</a></dd>
							<dt>日韩</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=韩国">韩国</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=首尔">首尔</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=济州">济州</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=釜山">釜山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=日本">日本</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=东京">东京</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=大阪">大阪</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=京都">京都</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=箱根">箱根</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=富士山">富士山</a></dd>
							<dt>港澳</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=香港">香港</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=迪士尼">迪士尼</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=海洋公园">海洋公园</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=澳门">澳门</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=威尼斯人">威尼斯人</a></dd>
							<dt>台湾</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=台北101大楼">台北101大楼</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=日语湾">日月湾</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=阿里山">阿里山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=垦丁">垦丁</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=高雄">高雄</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=野柳">野柳</a></dd>
							<dt>欧洲</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=法国">法国</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=瑞士">瑞士</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=意大利">意大利</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=德国">德国</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=英国">英国</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=希腊">希腊</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=奥地利">奥地利</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=荷兰">荷兰</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=比利时">比利时</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=芬兰">芬兰</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=瑞典">瑞典</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=丹麦">丹麦</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=西班牙">西班牙</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=葡萄牙">葡萄牙</a></dd>
							<dt>美洲</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=美国">美国</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=拉斯维加斯">拉斯维加斯</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=夏威夷">夏威夷</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=纽约">纽约</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=西雅图">西雅图</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=旧金山">旧金山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=关岛">关岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=加拿大">加拿大</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=华盛顿">华盛顿</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=温哥华">温哥华</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=墨西哥">墨西哥</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=巴西">巴西</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=智利">智利</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=巴哈马">巴哈马</a></dd>
							<dt>澳洲</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=澳大利亚">澳大利亚</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=悉尼">悉尼</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=黄金海岸">黄金海岸</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=墨尔本">墨尔本</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=布里斯班">布里斯班</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=凯恩斯">凯恩斯</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=新西兰南北岛">新西兰南北岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=乌克兰">乌克兰</a></dd>
							<dt>中东非</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=迪拜">迪拜</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=土耳其">土耳其</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=埃及">埃及</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=南非">南非</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=以色列">以色列</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=约旦">约旦</a></dd>
						</dl>
					</div>
				</li>
				
				<li class="list-section">
					<a href="${ctx}/produce/route/list?categoryPID=36666E8E0CA1EF38E050007F0100469B" class="item-section">
						<i class="icon-zb"></i>
						<p>周边旅游</p>
						<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
					</a>
					<!-- 弹出 -->
					<div class="item-box" style="display: none;">
						<dl>
							<c:if test="${fn:indexOf(url, 'fctrip.net') >= 0}">
							<dt>青岛</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=崂山">崂山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=栈桥沿海">栈桥沿海</a></dd>
							<dt>烟台</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=蓬莱阁">蓬莱阁</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=芝罘岛">芝罘岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=昆嵛山国家森林公园">昆嵛山国家森林公园</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=龙口南山景区">龙口南山景区</a></dd>
							<dt>威海</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=刘公岛">刘公岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=东方天鹅湖">东方天鹅湖</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=乳山度假村">乳山度假村</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=石岛赤山风景区">石岛赤山风景区</a></dd>
							<dt>潍坊</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=十笏园 风筝博物馆">十笏园 风筝博物馆</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=青州云门山">青州云门山</a></dd>
							<dt>济南</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=趵突泉">趵突泉</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=大明湖">大明湖</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=千佛山">千佛山</a></dd>
							<dt>泰安</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=泰山">泰山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=东平湖">东平湖</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=泰安方特">泰安方特</a></dd>
							<dt>临沂</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=山东地下大峡谷">山东地下大峡谷</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=沂蒙山景区">沂蒙山景区</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=沂水天上王城">沂水天上王城</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=沂水天然地下画廊 ">沂水天然地下画廊 </a></dd>
							<dt>济宁</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=曲阜三孔">曲阜三孔</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=汶上宝相寺">汶上宝相寺</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=水泊梁山">水泊梁山</a></dd>
							<dt>日照</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=日照万平口海滨风景区">日照万平口海滨风景区</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=日照五莲山风景区">日照五莲山风景区</a></dd>
							<dt>淄博</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=周村古街">周村古街</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=蒲松龄纪念馆 ">蒲松龄纪念馆 </a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=凤凰天池">凤凰天池</a></dd>
							<dt>枣庄</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=台儿庄古城">台儿庄古城</a></dd>
							</c:if>
							
							<c:if test="${fn:indexOf(url, 'xn--9iq358c424b.net') >= 0 || fn:indexOf(url, 'xn--9iq358c424b.com') >= 0 || fn:indexOf(url, 'xn--9iq358c424b.cn') >= 0}">
							<dt>大同</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=云冈石窟">云冈石窟</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=九龙壁">九龙壁</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=方特游乐场">方特游乐场</a></dd>
							<dt>朔州</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=应县木塔">应县木塔</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=悬空寺">悬空寺</a></dd>
							<dt>忻州</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=五台山">五台山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=芦芽山">芦芽山</a></dd>
							<dt>太原</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=动物园">动物园</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=晋祠">晋祠</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=煤炭博物馆">煤炭博物馆</a></dd>
							<dt>吕梁</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=北武当山">北武当山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=朝阳沟">朝阳沟</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=贾家庄">贾家庄</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=梦幻海">梦幻海</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=曹溪河">曹溪河</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=庞泉沟">庞泉沟</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=卦山">卦山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=刘胡兰纪念馆">刘胡兰纪念馆</a></dd>
							<dt>晋中</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=绵山">绵山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=平遥">平遥</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=王家大院">王家大院</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=乔家大院">乔家大院</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=常家庄园">常家庄园</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=崇宁堡">崇宁堡</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=红崖峡谷">红崖峡谷</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=乌金山">乌金山</a></dd>
							<dt>阳泉</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=娘子关">娘子关</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=大汖温泉">大汖温泉</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=藏山">藏山</a></dd>
							<dt>临汾</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=尧庙">尧庙</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=洪洞大槐树">洪洞大槐树</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=苏三监狱">苏三监狱</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=华门">华门</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=壶口瀑布">壶口瀑布</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=云丘山">云丘山</a></dd>
							<dt>长治</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=行山大峡谷">行山大峡谷</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=通天峡">通天峡</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=武乡">武乡</a></dd>
							<dt>晋城</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=皇城相符">皇城相符</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=蟒河">蟒河</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=珏山">珏山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=王莽岭">王莽岭</a></dd>
							<dt>运城</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=五老峰">五老峰</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=关帝庙">关帝庙</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=盐湖">盐湖</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=普救寺">普救寺</a></dd>
							</c:if>

							<c:if test="${fn:indexOf(url, 'fctrip.net') == -1 && fn:indexOf(url, 'xn--9iq358c424b.net') == -1  && fn:indexOf(url, 'xn--9iq358c424b.com') == -1 && fn:indexOf(url, 'xn--9iq358c424b.cn') == -1}">
							<dt>西安</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=兵马俑">兵马俑</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=华清池">华清池</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=骊山">骊山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=法门寺">法门寺</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=茂陵">茂陵</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=华山">华山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=袁家村">袁家村</a></dd>
							<dt>陕北</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=黄帝陵">黄帝陵</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=壶口瀑布">壶口瀑布</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=延安">延安</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=宝塔山">宝塔山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=枣园">枣园</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=杨家岭">杨家岭</a></dd>
							<dt>陕南</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=汉中">汉中</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=安康">安康</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=瀛湖">瀛湖</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=香溪洞">香溪洞</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=商洛">商洛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=金丝峡">金丝峡</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=丹江">丹江</a></dd>
							<dt>河南</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=云台山">云台山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=洛阳">洛阳</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=少林寺">少林寺</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=龙门石窟">龙门石窟 </a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=郭亮村">郭亮村</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=万仙山">万仙山</a></dd>
							<dt>湖北</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=十堰">十堰</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=五龙河">五龙河</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=武当山">武当山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=黄鹤楼">黄鹤楼</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=汉江三峡">汉江三峡</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=神农架">神农架</a></dd>
							<dt>山西</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=平遥古城">平遥古城</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=王家大院">王家大院</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=五台山">五台山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=皇城相府">皇城相府</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=绵山">绵山</a></dd>
							<dt>宝鸡</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=太白山">太白山</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=关山牧场">关山牧场</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=红河谷">红河谷</a></dd>
							</c:if>
							
							
						</dl>
					</div>
				</li>
				<li class="list-section">
					<a href="#" target="_self" class="item-section">
						<i class="icon-hddj"></i>
						<p>海岛度假</p>
						<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
					</a>
					<!-- 弹出 -->
					<div class="item-box" style="display: none;">
						<dl>
							<dt>海岛度假</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=马尔代夫">马尔代夫</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=毛里求斯">毛里求斯</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=普吉岛">普吉岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=斯米兰">斯米兰</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=关岛">关岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=塞舌尔">塞舌尔</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=巴厘岛">巴厘岛 </a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=帕劳">帕劳</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=海南岛">海南岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=苏梅岛">苏梅岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=大溪地">大溪地</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=斐济">斐济</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=沙巴岛">沙巴岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=塞班岛">塞班岛</a></dd>
						</dl>
					</div>
				</li>
				<li class="list-section">
					<a href="#" target="_self" class="item-section">
						<i class="icon-zyx"></i>
						<p>自由行</p>
						<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
					</a>
					<!-- 弹出 -->
					<div class="item-box" style="display: none;">
						<dl>
							<dt>自由行</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=三亚">三亚</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=丽江">丽江</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=厦门">厦门</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=香港">香港</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=新加坡">新加坡</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=日本">日本</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=泰国">泰国</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=马尔代夫">马尔代夫</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=普吉岛">普吉岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=苏梅岛">苏梅岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=首尔">首尔</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=巴厘岛">巴厘岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=毛里求斯">毛里求斯</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=天宁">天宁</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=塞班岛">塞班岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=斐济">斐济</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=夏威夷">夏威夷</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=济州岛">济州岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=关岛">关岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=涠洲岛">涠洲岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=阳朔">阳朔</a></dd>
						</dl>
					</div>
				</li>
				<li class="list-section last">
					<a href="#" target="_self" class="item-section">
						<i class="icon-bjyl"></i>
						<p>包机邮轮</p>
						<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
					</a>
					<!-- 弹出 -->
					<div class="item-box" style="display: none;">
						<dl>
							<dt>包机邮轮</dt>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=日本">日本</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=巴厘岛">巴厘岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=泰国">泰国</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=普吉岛">普吉岛</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=欧洲">欧洲</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=皇家加勒比游轮">皇家加勒比游轮</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=海洋赞礼号">海洋赞礼号</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=海洋量子号">海洋量子号</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=海洋水手号">海洋水手号</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=海洋魅力号">海洋魅力号</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=歌诗达游">歌诗达游轮</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=赛琳娜号">赛琳娜号</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=大西洋号">大西洋号</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=天海游轮">天海游轮</a></dd>
							<dd><a target="_blank" href="${ctx}/produce/route/list?query=新世纪号">新世纪号</a></dd>
						</dl>
					</div>
				</li>
			</ul>
		</div>
		<!-- 焦点图 -->
		<div id="myCarousel" class="carousel slide header-img" data-ride="carousel">
			<ol class="carousel-indicators">
				<c:forEach items="${ADATTR}" var="ad" varStatus="i">
				<c:if test="${ad.TYPE eq '1'}">
			    <li data-target="#myCarousel" data-slide-to="0" class="<c:if test="${i.index eq 0}">active</c:if>"></li>
			    </c:if>
			    </c:forEach>
			</ol>
			
			<div class="carousel-inner" role="listbox">
				<c:forEach items="${ADATTR}" var="ad" varStatus="i">
				<c:if test="${ad.TYPE eq '1'}">
		        <div class="item <c:if test="${i.index eq 0}">active</c:if>">
		          <a href="${ad.HREF}"><img alt="" src="${picctx}/${ad.URL}" data-holder-rendered="true" style="height: 350px;"></a>
		        </div>
		        </c:if>
			    </c:forEach>
		    </div>
		    
			<a class="left carousel-control" target="_self" href="#myCarousel" role="button" data-slide="prev">
			    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
			    <span class="sr-only">Previous</span>
			</a>
			<a class="right carousel-control" target="_self" href="#myCarousel" role="button" data-slide="next">
			    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
			    <span class="sr-only">Next</span>
			</a>
		</div>

		<!-- 列表 -->
		<div class="header-right">
			<!-- 列表 -->
			<div class="hot-list">
				<h3>热门目的地</h3>
				<ul class="nav nav-tabs navbar-left tab-list">
				  	<li class="active"><a href="#domestic" target="_self" data-toggle="tab">国内游</a></li>
				  	<li><a href="#outbound" target="_self" data-toggle="tab">出境游</a></li>
				  	<li><a href="#excursion" target="_self" data-toggle="tab">周边游</a></li>
				</ul>
				<div class="tab-content tab-listul">
					<div class="tab-pane fade in active domestic" id="domestic">
						<ul class="nav nav-tabs navbar-left tab-place">
							
						</ul>
					</div>
					<div class="tab-pane fade" id="outbound">
						<div class="tab-pane fade in active outbound">
							<ul class="nav nav-tabs navbar-left tab-place">
								
							</ul>
						</div>
					</div>
					<div class="tab-pane fade" id="excursion">
						<div class="tab-pane fade in active excursion">
							<ul class="nav nav-tabs navbar-left tab-place">
								
							</ul>
						</div>
					</div>
				</div>
			</div>
	        <!-- 酒店机票 -->
	        <div class="hotel-ticket">
	        	<!--
	        	<ul class="nav nav-tabs navbar-left tab-inner">
				  	<li class="active"><a href="#ticket" target="_self" data-toggle="tab">团队机票</a></li>
				  	<li><a href="#hotel" target="_self" data-toggle="tab">酒店</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane fade in active" id="ticket" style="position: relative;">
						<dl>
							<dt>出发城市：</dt>
							<dd><input type="text" placeholder="选择出发地" id="start_sub" name="start_sub" value=""></dd>
							<dt>目的城市：</dt>
							<dd><input type="text" placeholder="选择目的地" id="end_sub" name="end_sub" value=""></dd>
							<dt>出发日期：</dt>
							<dd><input type="text" placeholder="选择出发日期" name="" id="start_date" value="" class="tab-calendar"></dd>
						</dl>
						<div class="replace">
							<a href="#" class="icon-replace" target="_self"></a>
						</div>
						<dl>
							<dt>&nbsp;</dt>
							<dd><a href="javascript:;" class="tab-search">搜索</a></dd>
						</dl>
					</div>
					<div class="tab-pane fade" id="hotel" style="position: relative;">
						<dl>
							<dt>入住城市：</dt>
							<dd><input type="text" placeholder="城市名称" name="" value=""></dd>
							<dt>关键字：</dt>
							<dd><input type="text" placeholder="酒店名/位置/名称" name="" value=""></dd>
							<dt>入住日期：</dt>
							<dd><input type="text" placeholder="2016-02-15" name="" id="startDate" value="" class="tab-calendar"></dd>
							<dt>离店日期：</dt>
							<dd><input type="text" placeholder="2016-02-22" name="" id="endDate" value="" class="tab-calendar"></dd>
						<dl>
						<dl>
							<dt>&nbsp;</dt>
							<dd><a href="javascript:;" class="tab-search">搜索</a></dd>
						</dl>
					</div>
				</div>
				-->
	        </div>
		</div>
	</div>
	
	<!-- 主题推荐 -->
	<div class="main-box container" id="zhuti">
		<!-- 标题 -->
<!-- 		<div class="tab-title titstd"> -->
<!-- 			<h2 class="navbar-left">热卖产品</h2> -->
<!-- 		</div> -->
		
		<!-- 线路产品 -->
		
		
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
		
			$(function () {
				for(var i = 0; i < routeCategorys.length; i++){
					$.ajax({  
					type: "post",
					url:'${ctx}/index/routes?PID='+routeCategorys[i][0]+'&ID='+routeCategorys[i][1]+"&ORDER_BY="+routeCategorys[i][2]+"&s_start_city=${s_start_city}",
					dataType:"json",
					success: function(d, textStatus){
						var hotEndCityHTML = '', CITY_NAME = '', routeEndCityHTML = '', routeHotHTML = '', routeHTML = '', topRouteHTML = '', a = 1, c = 0, s = 0;
						
						var PID = d.data[0].PID, ID = d.data[0].ID, routes = d.data[0].routes, hotRoutes = d.data[0].hotRoutes, ORDER_BY = d.data[0].ORDER_BY, topRoutes = d.data[0].topRoutes; 
						/**
						 * 国内游、出境游、周边游热门目的地
						 */
						 
						if(PID == '36666E8E0C9EEF38E050007F0100469B' || PID == '36666E8E0C9FEF38E050007F0100469B' || PID == '36666E8E0CA1EF38E050007F0100469B' 
						|| PID == '36666E8E0CA2EF38E050007F0100469B' || PID == '36666E8E0CA3EF38E050007F0100469B'){
							CITY_NAME = '';
							/**
							 * 线路目的地
							 */
							for(var l = 0; l < hotRoutes.length; l++){	
								var endCity = hotRoutes[l].END_CITY.split(",");
								for(var j = 0; j < endCity.length; j++){
									if(endCity[j] != null && endCity[j] != '' && CITY_NAME.indexOf(endCity[j]) < 0){
										routeEndCityHTML += '<li><a href="#'+ID+a+'" target="_self" data-toggle="tab" class="labelTag">'+endCity[j]+'</a></li>';
										hotEndCityHTML += '<li><a href="${ctx}/produce/route/list?query='+endCity[j]+'">'+endCity[j]+'</a></li>';
										CITY_NAME = CITY_NAME+","+endCity[j];
										a++;
									}
								}
							}
							
							$('#'+ID).find('.tag-city').append(routeEndCityHTML);
							
							var a = 1, cityNames = CITY_NAME.split(',');
							
							/**
							 * 线路产品
							 */
							 
							for(var j = 0; j < cityNames.length; j++){
								if(cityNames[j] != '' && cityNames[j] != null){
									routeHTML = '';
									routeHTML = '<div class="tab-pane fade in" id="'+ID+a+'">';
									c = 0;
									for(var l = 0; l < hotRoutes.length; l++){
										var endCity = hotRoutes[l].END_CITY;
										if(endCity.indexOf(cityNames[j]) >= 0 && c < 8){
											c++;
											routeHTML += getRouteDetailHTML(hotRoutes[l]);
										}
									}	
									routeHTML += '</div>';
									$('#'+ID+'RouteList').append(routeHTML);
									a++;
								}
							}
							
						}else if(PID == '36666E8E0CA0EF38E050007F0100469B'){
							var themes = new Array("亲子游","夏令营","青年会","夕阳红","蜜月游");
							for(var l = 0; l < themes.length; l++){
								routeEndCityHTML += '<li><a href="#'+ID+a+'" target="_self" data-toggle="tab" class="labelTag">'+themes[l]+'</a></li>';
								a++;
							}
							
							$('#'+ID).find('.tag-city').append(routeEndCityHTML);
							
							var a = 1;
						
							/**
							 * 线路产品
							 */
							for(var j = 0; j < themes.length; j++){
								routeHTML = '';	
								routeHTML = '<div class="tab-pane fade in" id="'+ID+a+'">';
								c = 0;
								for(var l = 0; l < hotRoutes.length; l++){
									var t = hotRoutes[l].THEMES;
									if(themes[j].indexOf(t) >= 0 && c < 8){
										c++;
										routeHTML += getRouteDetailHTML(hotRoutes[l]);
									}
								}	
								routeHTML += '</div>';
								$('#'+ID+'RouteList').append(routeHTML);
								a++;
							}
						}
						
						/**
						 * 精彩推荐
						 */
						s = 0;
						for(var l = 0; l < topRoutes.length; l++){	
							if(l <= 5){
								topRouteHTML += '';
								topRouteHTML += '<li>';
									topRouteHTML += '<a href="${ctx}/produce/route/detail?id='+topRoutes[l].ID+'&routeType='+topRoutes[l].TYPE+'" class="best-tablist">';
									var num_css = '';
									if(l == 0){
										num_css = 'one';
									}else if(l == 1){
										num_css = 'two';
									}else if(l == 2){
										num_css = 'three';
									}else if(l == 3){
										num_css = 'four';
									}else if(l == 4){
										num_css = 'five';
									}else{
										num_css = 'six';
									}
									topRouteHTML += '<i class="icon-num '+num_css+'"></i>';
										topRouteHTML += '<img src="${picctx}/'+topRoutes[l].FACE+'">';
										topRouteHTML += '<h6>'+topRoutes[l].TITLE+'</h6>';
										topRouteHTML += '<p>';
											topRouteHTML += '<span class="fl">'+topRoutes[l].ATTR+'</span>';
											topRouteHTML += '<span class="fr">';
												topRouteHTML += '<i>¥'+topRoutes[l].SUM_PRICE+'起</i>';
											topRouteHTML += '</span>';
										topRouteHTML += '</p>';
									topRouteHTML += '</a>';
								topRouteHTML += '</li>';
							}
						}
						$('#'+ID+'TopRoute').append(topRouteHTML);
						
						/**
						 * 线路热卖产品
						 */
						routeHotHTML ='';
						for(var l = 0; l < hotRoutes.length && l < 8; l++){	
							routeHotHTML += getRouteDetailHTML(hotRoutes[l]);
						}
						$('#'+ID+'RouteHot').append(routeHotHTML);
						
						/**
						 * 热卖产品
						 */
						var s = 0, hotRouteHTML = '';
						for(var l = 0; l < hotRoutes.length; l++){	
							if(ORDER_BY == '-1' && s < 4){
								s++;
								hotRouteHTML += getRouteDetailHTML(hotRoutes[l]);
							}
						}

						if(PID == '36666E8E0C9EEF38E050007F0100469B'){
							$('.domestic').children('.tab-place').html(hotEndCityHTML);
						}
						if(PID == '36666E8E0C9FEF38E050007F0100469B'){
							$('.outbound').children('.tab-place').html(hotEndCityHTML);
						}
						if(PID == '36666E8E0CA1EF38E050007F0100469B'){
							$('.excursion').children('.tab-place').html(hotEndCityHTML);
						}
						
// 						if(ORDER_BY == '-1'){
// 							$('#hot').html(hotRouteHTML);
// 						}
						
					}
				});
				}					 
			});
		</script>
		
		
<!-- 		<div class="tab-content"> -->
<!-- 			<div class="tab-pane fade in active" id="hot"> -->
				
<!-- 			</div> -->
<!-- 		</div> -->
	</div>
	<c:set var="themes" value="${fn:split('亲子游,夏令营,青年会,夕阳红,蜜月游', ',')}" />
	<!-- 线路 -->
	<c:forEach items="${routeCategorys}" var="routeCategory">
	<c:if test="${routeCategory.ORDER_BY != -1}">
	<div class="main-box container" id="${routeCategory.ID}">
		<!-- 标题 -->
		<div class="tab-title titgny">
			<h2 class="navbar-left" style="display:inline-block;width:80px;">${routeCategory.CATEGORY}</h2>
			<ul class="nav nav-tabs navbar-left tag-city tag-gny">
			  	<li class="active"><a href="#${routeCategory.ID}RouteHot" target="_self" data-toggle="tab" class="labelTag">热卖</a></li>
			</ul>
			<ul class="nav nav-tabs navbar-right tag-more">
				<li><a href="${ctx}/produce/route/list?categoryPID=${routeCategory.PID}">更多路线>></a></li>
			</ul>
		</div>
		<!-- 线路产品 -->
		<div class="tab-content trv-tabbox gyRouteList" id="${routeCategory.ID}RouteList">
			<div class="tab-pane fade in active" id="${routeCategory.ID}RouteHot">
				
			</div>
		</div>
		<!-- 精彩推荐 -->
		<div class="best-seller">
			<h3 class="best-gny">精彩推荐</h3>
			<ul class="best-con" id="${routeCategory.ID}TopRoute">
				
			</ul>
		</div>
	</div>
	</c:if>
	</c:forEach>
	
	<!-- 页尾 -->
	<footer class="main-footer">
		<div class="container">
			<c:if test="${USER_AGENT eq 'pc'}">
			<ul class="footer-links text-muted col-md-12">
				<li><a href="${ctx}/help/help.jsp">关于我们</a></li>
				<li>|</li>
				<li><a href="${ctx}/help/help.jsp">联系我们</a></li>
				<li>|</li>
				<li><a href="${ctx}/help/help.jsp">诚征英才</a></li>
				<li>|</li>
				<li><a href="${ctx}/help/help.jsp">隐私保护</a></li>
				<li>|</li>
				<li><a href="${ctx}/help/help.jsp">免责声明</a></li>
				<li>|</li>
				<li><a href="${ctx}/help/help.jsp">用户协议</a></li>
				<li>|</li>
				<li><a href="${ctx}/help/help.jsp">帮助中心</a></li>
				<li>|</li>
				<li><a href="javascript:void(0)">友情链接</a></li>
				<li>|</li>
				<li>© 2016 <a href="${ctx}/">${company.DOMAIN}</a> 版权所有</li>
			</ul>
			</c:if>
			<p class="col-md-12">Copyright © 2012-2017 All Rights Reserved | ${company.RECORD_NO}</p>
			
			
			<c:if test="${fn:indexOf(nurl, 'xsx518.com') > 0}">
				<script language='javaScript' src='http://wljg.xags.gov.cn/bsjs_new/610131/610131610131000022014032400137.js'></script>
			</c:if>
			
			<c:if test="${fn:indexOf(nurl, 'otcsxhw.com') > 0}">
				<script type="text/javascript" src="http://wljg.snaic.gov.cn/scripts/businessLicense.js?id=fb73a868a26e11e68a886c92bf251155"></script>
			</c:if>
			
			<c:if test="${fn:indexOf(nurl, 'ctssx.cn') > 0}">
<!-- 			<script language='javaScript' src='http://wljg.snaic.gov.cn/wljgweb/bsjs/610000/61000000008441.js'></script> -->
			</c:if>
			
			<c:if test="${fn:indexOf(nurl, 'hzcct.com') > 0}">
<!-- 			<script language='javaScript' src='http://wljg.snaic.gov.cn/wljgweb/bsjs/612300/61230000000506.js'></script> -->
			</c:if>
		</div>
	</footer>
	
	<!-- 侧边导航 -->
	<script type="text/javascript">
	$(function(){
		$('.toolbar-menu li').click(function(){
			$('.toolbar-menu li').each(function(){
				$(this).removeClass('active');
			});	
			$(this).addClass('active');
		});
	});
	</script>
	<c:if test="${USER_AGENT eq 'pc'}">
	<div class="toolbar">
		<ul class="toolbar-list toolbar-menu">
			
			<c:forEach items="${categorys}" var="category" varStatus="i">
				<c:if test="${category.ORDER_BY ne '-1'}">
				<li <c:if test="${i.index eq 1}">class="active"</c:if>><a href="#${category.ID}" target="_self">${category.CATEGORY}</a></li>
				</c:if>
			</c:forEach>
			<li><a href="#navbar-example" target="_self">门店分布</a></li>
		</ul>
		<ul class="toolbar-list toolbar-gotop">
			<c:if test="${not empty company.CODE}">
			<li class="toolbar-item">
				<div class="toolbar-phone">
					<a href="javascript:void(0)" target="_self" class="tool-icon qr-tool">
						<span class="glyphicon glyphicon-phone toolbar-icon" aria-hidden="true"></span>
					</a>
				</div>
				<img class="qr-img" src="${picctx}/${company.CODE}">
			</li>
			</c:if>
			<c:if test="${not empty company.WX_IMG}">
			<li class="toolbar-item">
				<div class="toolbar-qr">
					<a href="javascript:void(0)" target="_self" class="tool-icon qr-tool1" style="padding: 10px 2px 11px 3px;">
						<i><img src="${ctx}/resources/images/wx-indexicon.png" width="30"></i>
					</a>
				</div>
				<img class="qr-img1" src="${picctx}/${company.WX_IMG}" style="display:none;">
			</li>
			</c:if>
			<li class="toolbar-item">
				<a id="scrollUp" href="javascript:;" class="tool-icon" data-toggle="tooltip" style="display: none;">
					<span class="glyphicon glyphicon-plane toolbar-icon" aria-hidden="true"></span>
				</a>
			</li>
		</ul>
	</div>
	</c:if>
	<c:if test="${not empty company.PHONE1 || not empty company.QQ1}">
	<div class="kf-box">
		<c:if test="${not empty company.PHONE1}"><h3><span class="glyphicon glyphicon-phone-alt" aria-hidden="true"></span>${company.PHONE1}</h3></c:if>
		<c:if test="${not empty company.QQ1}"><p class="head-qq"><a href="http://wpa.qq.com/msgrd?v=3&amp;uin=${company.QQ1}&amp;site=qq&amp;menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:${company.QQ1}:51" alt="我们竭诚为您服务！" title="我们竭诚为您服务！"></a></p></c:if>
	</div>
	</c:if>
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
<script type="text/javascript">
var getTagCss = function(ATTR){
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
   		attr_css = 'flag-zjy';
   	}
   	return attr_css;
}

var getRouteDetailHTML = function(r){
	var h = '';
	h +='<div class="col-md-3 trv-pro trv-pad">';
		h +='<a href="${ctx}/produce/route/detail?id='+r.ID+'&routeType='+r.TYPE+'">';
			h +='<div class="thumbnail trv-box clearfix">';
				h +='<div class="trv-img">';
		          	h +='<img src="${picctx}/'+r.FACE+'" height="162">';
		          	var ATTR = r.ATTR, attr_css = '';
		          	if(ATTR != null){
		          		var attr_css = getTagCss(ATTR);
			          	h +='<i class="trv-flag '+attr_css+'">'+ATTR+'</i>';
		          	}
	          	h +='</div>';
	          	h +='<div class="caption">';
		            h +='<div class="trv-title">';
		            	h += r.TITLE;
		            h +='</div>';
		            h +='<div class="trv-info clearfix">';
		            	h +='<div class="trv-price">¥<em>'+r.SUM_PRICE+'</em>起</div>';
		            	h +='<div class="trv-order"></div>';
		            h +='</div>';
	          	h +='</div>';
	        h +='</div>';
	     h +='</a>';
       h +='</div>';
       return h;
}
</script>
</body>    
</html>
