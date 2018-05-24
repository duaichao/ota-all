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
	<link rel="stylesheet" href="${ctx}/resources/css/docs.min.css" />
	<%@ include file="../commons/meta-js.jsp"%>
	<script src="${ctx}/resources/js/docs.min.js" ></script>
	<c:if test="${USER_AGENT eq 'pc'}"><base target="_blank"></c:if>

</head>
<body>
	<c:set var="header_title" value="帮助中心" />
	<%@ include file="../commons/header.jsp"%>
	
	<div class="container bs-docs-container">
     	<div class="row">
        <div class="col-md-9" role="main">
          	<div class="bs-docs-section">
			  	<h1 id="js-reg" class="page-header">关于我们</h1>
			  	<div class="bs-callout bs-callout-danger">
			  	  	<p>${company.ABOUT}</p>
			  	</div>
		  	</div>
        </div>
        
     	</div>
   </div>
   
	<%@ include file="../commons/footer.jsp"%>
</body>    
</html>
