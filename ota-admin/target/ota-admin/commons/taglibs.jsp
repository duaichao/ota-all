<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ page import="cn.sd.core.config.ConfigLoader"%>
<%
String visit = ConfigLoader.config.getStringConfigDetail("visit.path"); 
request.setAttribute("visit",visit);
%>
