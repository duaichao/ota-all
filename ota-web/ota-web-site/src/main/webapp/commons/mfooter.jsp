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
	</div>
</footer>

<c:if test="${USER_AGENT eq 'mobile'}">
<!-- 迷你导航 -->
<div class="bottom-nav" id="footer-nav">
	<ul>
		<li style="width:16%"><a href="${ctx}/"><img src="${ctx}/resources/images/icon-home.png" width="40"><span>首&nbsp;页</span></a></li>
	  	<li style="width:17%"><a href="${ctx}/produce/route/list?categoryPID=36666E8E0C9EEF38E050007F0100469B"><img src="${ctx}/resources/images/icon-gn.png" width="40"><span>国内游</span></a></li>
	  	<li style="width:17%"><a href="${ctx}/produce/route/list?routeType=1"><img src="${ctx}/resources/images/icon-zb.png" width="40"><span>周边游</span></a></li>
	  	<li style="width:17%"><a href="${ctx}/produce/route/list?categoryPID=36666E8E0C9FEF38E050007F0100469B"><img src="${ctx}/resources/images/icon-cj.png" width="40"><span>出境游</span></a></li>
	  	<li style="width:17%"><a href="${ctx}/store/list"><img src="${ctx}/resources/images/icon-wd.png" width="40"><span>门市地址</span></a></li>
	  	<li style="width:16%"><a href="${ctx}/user/sec/center/main"><img src="${ctx}/resources/images/icon-user.png" width="40"><span>我&nbsp;的</span></a></li>
	</ul>
</div>
</c:if>
