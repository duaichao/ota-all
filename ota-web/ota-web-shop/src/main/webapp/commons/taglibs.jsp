<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<% String url = request.getRequestURL().toString(); request.setAttribute("url", url); %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="rurl" value="${pageContext.request.requestURL}"/>
<c:set var="picctx" value="http://img.1.xsx518.com/attachment"/>
<%@ page import="cn.sd.core.config.ConfigLoader"%>
<%
String nurl = String.valueOf(request.getRequestURL());
request.setAttribute("nurl",nurl);
String visit = ConfigLoader.config.getStringConfigDetail("visit.path"); 
request.setAttribute("visit",visit);
%>
<script type="text/javascript">
	ctx = "${ctx}";
	picUrl = "${picctx}";
	USER_AGENT = '${USER_AGENT}';
</script>