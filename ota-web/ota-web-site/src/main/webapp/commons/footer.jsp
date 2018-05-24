<%@ page language="java" pageEncoding="UTF-8"%>
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
<!-- 		<script language='javaScript' src='http://wljg.snaic.gov.cn/wljgweb/bsjs/612300/61230000000506.js'></script> -->
		</c:if>
	</div>
</footer>

<!-- 侧边导航 -->
<c:if test="${USER_AGENT eq 'pc'}">
<div class="toolbar" style="background: none;">
	<ul class="toolbar-list toolbar-gotop">
		<c:if test="${not empty company.CODE}">
		<li class="toolbar-item">
			<div class="toolbar-phone">
				<a href="javascript:void(0)" target="_self" class="tool-icon qr-tool" style="background: #59a8ff;">
					<span class="glyphicon glyphicon-phone toolbar-icon" aria-hidden="true" style="color: #fff;"></span>
				</a>
			</div>
			<img class="qr-img" src="${picctx}/${company.CODE}">
		</li>
		</c:if>
		<c:if test="${not empty company.WX_IMG}">
		<li class="toolbar-item">
			<div class="toolbar-qr">
				<a href="javascript:void(0)" target="_self" class="tool-icon qr-tool1" style="padding: 10px 2px 11px 3px; background: #59a8ff;">
					<i><img src="${ctx}/resources/images/wx-icon.png" width="30"></i>
				</a>
			</div>
			<img class="qr-img1" src="${picctx}/${company.WX_IMG}">
		</li>
		</c:if>
		<li class="toolbar-item">
			<a id="scrollUp" href="javascript:;" class="tool-icon" data-toggle="tooltip" style="display: none; background: #e4eaf0;">
				<span class="glyphicon glyphicon-plane toolbar-icon" aria-hidden="true" style="color: #7e9dc3;"></span>
			</a>
		</li>
	</ul>
</div>
</c:if>

<div class="kf-box">
	<h3>
		<span class="glyphicon glyphicon-phone-alt" aria-hidden="true"></span>
		${company.PHONE1}
	</h3>
	<p class="head-qq"><a target="blank" class="qq" href="http://wpa.qq.com/msgrd?V=3&amp;uin=${company.QQ1}&amp;Site=QQ客服&amp;Menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:${company.QQ1}:41" alt="点击这里给我发消息"></a></p>
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
