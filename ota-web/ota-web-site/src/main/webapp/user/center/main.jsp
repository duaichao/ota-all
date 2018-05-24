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
	<%@ include file="../../commons/meta-css.jsp"%>
	<link rel="stylesheet" href="${ctx}/resources/css/user.css">
	<%@ include file="../../commons/meta-js.jsp"%>
	<link href="${ctx}/resources/js/flashUpload/upload.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/resources/js/flashUpload/FlashUploadNew.js"></script>
	<script type="text/javascript">
		$(function () {
			$('[data-toggle="tooltip"]').tooltip();
			BindFlashToButton();
		});
		
		var filtertypes = '*.jpg;*.png;';
		var uploadPathForflash = '${ctx}/user/sec/face';
		
		var uploadoption = { url: uploadPathForflash, allowFile: filtertypes, maxsize: 512};
		var arrControls = [
			"btnFlashUploadControl1"
		];
		var hdUrlsPathId = "imagePaths";
		
		function BindFlashToButton() {
		    ClearGlueFlash();
		    ZeroClipboard.nextId = 1;
		    for (var i = 0; i < arrControls.length; i++) {
		        var clip = new ZeroClipboard.Client();
		        clip.setHandCursor(true);
		        var url = uploadoption.url;
		        clip.setParms(url, uploadoption.allowFile, uploadoption.maxsize);
		        clip.glue(arrControls[i]);
		    }
		}
		
		function FillEditImg(editImgList) {
		    for (var i = 0; i < editImgList.length; i++) {
		        var data = { id: i + 1, name: '', url: editImgList[i].thumbUrl };
		        KuBaoUploader.UI.completeController(data, "dvFlashUpload" + (i + 1));
		        var path = editImgList[i].imgUrl + "|" + editImgList[i].thumbUrl;
		        KuBaoUploader.list.push(path);
		    }
		}
		function ClearGlueFlash() {
		    for (var i = 1; i < ZeroClipboard.nextId; i++) {
		        ZeroClipboard.clients[i].hide();
		    }
		    ZeroClipboard.nextId = 1;
		}
		function mobileUpload(t){
			$('#tipModal').modal('show');
			$('#uploadFace').submit();
		}
	</script>
</head>
<body>
	<c:if test="${USER_AGENT eq 'mobile'}">
	<c:set var="header_title" value="会员中心" />
	<%@ include file="../../commons/header.jsp"%>
	</c:if>
	<div class="bg-warning usr-info" id="dvFlashUpload1" imgno="头像">
		<div class="usr-info-left col-xs-4" id="dvFlashUpload1">
			<div class="initial">
			<c:if test="${empty webUser.FACE}"><img src="${ctx}/resources/images/default_pic.png" alt="..." width="96"></c:if>
			<c:if test="${not empty webUser.FACE}">
				<img id="userFace" src="http://xsx518.com/tattachment/${webUser.FACE}" alt="..." height="96" width="96">
			</c:if>
			</div>
			<p>
			<c:if test="${USER_AGENT eq 'mobile'}">
			<form action="${ctx}/user/mobile/face" method="post" enctype="multipart/form-data" id="uploadFace">
			<input type="file" name="Filedata" id="Filedata" onchange="mobileUpload(this.data)" accept="image/*;capture=camera" style="width:68px;">
			</form>
			</c:if>
			<c:if test="${USER_AGENT eq 'pc'}">
		    <button type="button" class="up-btn" id="btnFlashUploadControl1">上传头像</button><input type="hidden" value="" id="imagePaths"/>
		    </c:if>
		    </p>
		</div>
		<ul class="usr-info-right col-xs-8">
			<li>
				尊敬的会员：<a href="javascript:void(0)" target="_self" class="usr-id"><c:if test="${not empty webUser.CHINA_NAME}">${webUser.CHIAN_NAME}</c:if><c:if test="${empty webUser.CHINA_NAME}">${webUser.USER_NAME}</c:if></a>，欢迎您！
			</li>
			<!--
			<li>
				<b>安全级别：</b>
				<div class="progress safety">
				  	<div class="progress-bar progress-bar-success" style="width: 100%;">安全</div>
				</div>
			</li>
			-->
			<li>
				<span class="glyphicon glyphicon-phone" aria-hidden="true"></span>&nbsp;
				${fn:substring(webUser.MOBILE, 0, 3)}****${fn:substring(webUser.MOBILE, 7, 11)}
				<a href="${ctx}/user/center/phone.jsp" target="_self" class="usr-link">[修改]</a>
			</li>
			<li>
				<b>交易提醒：</b>
				<a target="_self" href="${ctx}/user/sec/order">未付款订单（${cnt}）</a>
			</li>
		</ul>
	</div>
	<c:if test="${USER_AGENT eq 'mobile'}">
	<%@ include file="../../commons/m-user-footer.jsp"%>
	</c:if>
	<!-- 弹出窗口 -->
	<div class="modal fade" id="tipModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="myModalLabel">提示</h3>
	      </div>
	      <div class="modal-body">
	          <h3 id="tipContent">请等待，正在上传头像...</h3>
	      </div>
	    </div>
	  </div>
	</div>
</body>    
</html>
