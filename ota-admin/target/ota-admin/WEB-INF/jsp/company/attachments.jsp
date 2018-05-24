<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <%@ include file="/commons/meta-ext.jsp"%>
    <%@ include file="/commons/meta-ext-init.jsp"%>
</head>
<body>
	<c:if test="${not empty COMPANY && not empty COMPANY.LICENSE_ATTR}">
		<img src="${ctx}/attachment/${COMPANY.LICENSE_ATTR}"/><br />
	</c:if>
	<c:if test="${not empty COMPANY && not empty COMPANY.LOGO_URL}">
		<img src="${ctx}/attachment/${COMPANY.LOGO_URL}" /><br />
	</c:if>
	<c:if test="${not empty COMPANY && not empty COMPANY.BUSINESS_URL}">
		<img src="${ctx}/attachment/${COMPANY.BUSINESS_URL}" /><br />
	</c:if>
</body>
</html>