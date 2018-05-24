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
	<link rel="stylesheet" href="${ctx}/resources/css/login.css">
	<%@ include file="../commons/meta-js.jsp"%>
	<script type="text/javascript" src="${ctx}/resources/js/RSA/RSA.min.js"></script>
	
	<script type="text/javascript">
		$(function(){
			
			/**
    		 * 验证码倒计时
    		 */
    		var n = 120;
    		$('#sendBtn').click(function(){
    			var mobile = $('#mobile').val();
    			if(mobile != ''){
    				$.ajax({  
						type: "post",
						url: "${ctx}/sms/send?mobile="+mobile+'&type=reg_tip',
						dataType:"json",
						success: function(data, textStatus){
							var tip = ['短信已发送，请等待！','短信已发送，请等待！','发送失败，请检查你的手机号码！','发送失败，请检查你的手机号码！','手机号码已被注册']
							if(!data.success){
								$('#regAlert').show();
								$('#regAlertcontent').html(tip[0-data.statusCode]);
							}else{
								$('#sendBtn').hide();
		    					$('#countBtn').show();
		    					c();
	    					}
						}
					});
				
	    			
    			}
    		});
    		
    		function c(){
    			if(n == 0){
   					n = 120;
   					$('#sendBtn').show();
   					$('#countBtn').hide();
   					$('#countBtn').html(n+'s后可重新获取');
   					return false;
   				}else{
   					$('#countBtn').html((n--)+'s后可重新获取');
   				}
    			setTimeout(function(){
    				c();
    			},1000);
    		}
    		
			$('#showPwd').click(function(){
				var t = $('#password').attr('type');
				if(t == 'password'){
					$('#password').attr('type', 'text');
				}else{
					$('#password').attr('type', 'password');
				}
			});	 
			
			$('#toReg').click(function(){
				
				var user_name = $('#user_name').val(), mobile = $('#mobile').val(), password = $('#password').val(), code = $('#code').val();
				if(user_name == '' || mobile == '' || password == '' || code == ''){
					$('#regAlert').show();
					$('#regAlertcontent').html('注册信息填写不完成！');
					return false;					
				}
				
				$.ajax({  
					type: "post",
					url: "${ctx}/user/mini/reg?USER_NAME="+user_name+'&PASSWORD='+password+'&MOBILE='+mobile+'&CODE='+code,
					dataType:"json",
					success: function(data, textStatus){
						if(data.success){
							$('#orderForm').submit();
// 							document.location.href = '${ctx}/order/to/save?routeId='+routeId+'&routeTitle='+routeTitle+'&beginCity='+beginCity+'&endCity='+endCity+'&d='+start_date;
						}else{
							var tip1 = ['','用户名/手机号已存在','验证码错误','注册信息填写不完成'];
							$('#regAlertcontent').html(tip1[0-data.statusCode]);
							$('#regAlert').show();
						}
					}
				});
				
			});
			
			$('#toLogin').click(function(){
				
				$('.toolbar-tip').hide();
				var USER_NAME = $('#LOGIN_USER_NAME').val();
				var PASSWORD = $('#LOGIN_PASSWORD').val();
				if(USER_NAME == '' || PASSWORD == ''){
					$('.toolbar-tip').val('请输入正确的用户名和密码！');
					$('.toolbar-tip').show();
					return false;
				}	
				var key,result;
				setMaxDigits(130);
				key = new RSAKeyPair("${e}","","${m}");
				result = encryptedString(key, encodeURIComponent($('#LOGIN_PASSWORD').val()));
				$('#LOGIN_PASSWORD').val(result);
				
				$.ajax({  
					type: "post",
					url: "${ctx}/user/mini/login?USER_NAME="+USER_NAME+'&PASSWORD='+result,
					dataType:"json",
					success: function(data, textStatus){
						if(data.success){
							$('#orderForm').submit();
// 							document.location.href = '${ctx}/order/to/save?routeId='+routeId+'&routeTitle='+routeTitle+'&beginCity='+beginCity+'&endCity='+endCity+'&d='+start_date;
						}else{
							$('.toolbar-tip').val('用户名或密码错误！');
							$('.toolbar-tip').show();
						}
					}
				});
			});
		});
	</script>

</head>
<body>
<div class="modal-content">
	<div class="bs-example bs-example-tabs" data-example-id="togglable-tabs">
		<ul id="myTabs" class="nav nav-tabs" role="tablist">	
			<li role="presentation" class=""><a  target="_self" href="#reg" role="tab" id="reg-tab" class="alert-tab" data-toggle="tab" aria-controls="reg" aria-expanded="true">注册</a></li>
			<li role="presentation" class="active"><a target="_self" href="#login" id="login-tab" class="alert-tab" role="tab" data-toggle="tab" aria-controls="login" aria-expanded="false">登录</a></li>
			
			<button type="button" class="close alert-close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
		</ul>
		<div id="myTabContent" class="tab-content alert-box">
			<div role="tabpanel" class="tab-pane fade in" id="reg" aria-labelledby="reg-tab">
				<form action="${ctx}/user/reg" method="post" id="regFrom" class="form-horizontal">
					<div class="alert alert-warning alert-dismissible fade in" id="regAlert" style="display:none;">
						<button type="button" class="close" data-dismiss="alert" ><span aria-hidden="true">×</span></button>
						<h3 id="regAlertcontent">手机号码已被注册</h3>
					</div>
					<div class="form-group">
					    <label class="col-md-3 control-label" for="formGroupInput">用户名：</label>
					    <div class="col-md-6">
					      	<input class="form-control" type="text" name="USER_NAME" id="user_name" placeholder="请输入用户名">
					    </div>
				  	</div>
				  	<div class="form-group">
					    <label class="col-md-3 control-label" for="formGroupInput">手机号码：</label>
					    <div class="col-md-6">
					      	<input class="form-control" type="text" name="MOBILE" id="mobile" maxlength="11" placeholder="请输入手机号码">
					    </div>
				  	</div>
				  	<div class="form-group">
					    <label class="col-md-3 control-label" for="formGroupInput">校验码：</label>
					    <div class="col-md-3">
					      	<input class="form-control" type="text" name="CODE" id="code" maxlength="6" placeholder="请输入校验码">
					    </div>
					    <div class="col-md-3">
					    	<a href="javascript:void(0);" target="_self" class="btn btn-default disabled" id="countBtn" style="display: none; font-size: 12px;" role="button">120s后可重新获取</a>
					    	<a href="javascript:void(0);" target="_self" class="btn btn-success" style="font-size: 12px;" id="sendBtn" role="button">重新发送校验码</a>
					    </div>
				  	</div>
				  	<div class="form-group">
					    <label class="col-md-3 control-label" for="formGroupInput">登录密码：</label>
					    <div class="input-group col-md-6" style="padding-left:15px; padding-right:15px;">
					      	<input class="form-control" type="password" maxlength="20" name="PASSWORD" id="password" placeholder="6-20位字母、数字和符号">
					      	<span class="input-group-addon" id="showPwd"><a href="javascript:void(0);" target="_self" style="color:#333;" title="显示密码"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a></span>
					    </div>
				  	</div>
				  	<div class="form-group">
					    <label class="col-md-3 control-label" for="formGroupInput"></label>
					    <div class="col-md-6">
					      	<div class="checkbox">
							  	<label>
							    	<input type="checkbox" value="" checked="checked" disabled="disabled">
							    	我已阅读并接受<a href="javascript:void(0);" target="_self"  class="reg-agree" data-toggle="modal" data-target=".bs-example-modal-lg">《会员注册协议》</a>
							  	</label>
							</div>
					    </div>
				  	</div>
				  	<div class="form-group">
					    <label class="col-md-3 control-label" for="formGroupInput"></label>
					    <div class="col-md-6">
					    	<button class="btn btn-sign btn-block" id="toReg" type="button" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">提交</button>
					    </div>
					</div>
				</form>
			</div>
			<div role="tabpanel" class="tab-pane active fade in" id="login" aria-labelledby="login-tab">
				<form action="/user/login" method="post" id="loginForm">
					<div class="toolbar-tip" style="display:none;">
						<div class="tooltip-arrow"></div>
						<div class="tooltip-inner toolbar-danger" stlye="width: 100%;">
							请输入正确的用户名和密码！
						</div>
					</div>
					<div class="form-group">
						<input type="text" class="form-control" name="USER_NAME" id="LOGIN_USER_NAME" placeholder="手机号码">
					</div>
					<div class="form-group">
						<input type="password" class="form-control" name="PASSWORD" id="LOGIN_PASSWORD" placeholder="密码">
					</div>
					<div class="form-group">
						<label>
							<input type="checkbox" id="autoLogin" name="autoLogin"> 记住密码
						</label>
						<a href="${ctx}/user/find_pwd.jsp" target="_blank" class="toolbar-link">忘记密码？</a>
					</div>
					<div class="form-group">
						<button type="button" id="toLogin" class="btn btn-sign btn-block">登录</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<div class="modal fade bs-example-modal-lg in" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
    <div class="modal-dialog modal-lg">
    	<div class="modal-content">
        	<div class="modal-header">
          		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
          		<h2 class="modal-title" id="myLargeModalLabel">136旅游网会员协议</h2>
        	</div>
        	<div class="modal-body">
				<h3>一、服务条款</h3>
				<p>136旅游网提供的服务将完全按照其发布的使用协议、服务条款和操作规则严格执行。为获得136服务，服务使用人（以下称"会员"）应当同意本协议的全部条款并按照页面上的提示完成全部的注册程序。</p>
				
				<h3>二、目的</h3>
				<p>本协议是以规定用户使用136旅游网提供的服务时，136旅游网与会员间的权利、义务、服务条款等基本事宜为目的。</p>
				
				<h3>三、遵守法律及法律效力</h3>
				<p>本服务使用协议在向会员公告后，开始提供服务或以其他方式向会员提供服务同时产生法律效力。</p> 
				会员同意遵守《中华人民共和国保密法》、《计算机信息系统国际联网保密管理规定》、《中华人民共和国计算机信息系统安全保护条例》、《计算机信息网络国际联网安全保护管理办法》、《中华人民共和国计算机信息网络国际联网管理暂行规定》及其实施办法等相关法律法规的任何及所有的规定，并对会员以任何方式使用服务的任何行为及其结果承担全部责任。 </p>
				<p>在任何情况下，如果本网站合理地认为会员的任何行为，包括但不限于会员的任何言论和其他行为违反或可能违反上述法律和法规的任何规定，本网站可在任何时候不经任何事先通知终止向会员提供服务。</p> 
				<p>本网站可能不时的修改本协议的有关条款，一旦条款内容发生变动，本网站将会在相关的页面提示修改内容。在更改此使用服务协议时，本网站将说明更改内容的执行日期，变更理由等。且应同现行的使用服务协议一起，在更改内容发生效力前7日内及发生效力前日向会员公告。 </p>
				<p>会员需仔细阅读使用服务协议更改内容，会员由于不知变更内容所带来的伤害，本网站一概不予负责。 </p>
				<p>如果不同意本网站对服务条款所做的修改，用户有权停止使用网络服务。如果用户继续使用网络服务，则视为用户接受服务条款的变动。</p>
				 
				<h3>四、服务内容</h3>
				<p>136服务的具体内容由本网站根据实际情况提供，本网站保留随时变更、中断或终止部分或全部136服务的权利。</p>
				
				<h3>五、会员的义务</h3>
				<p>用户在申请使用本网站136服务时，必须向本网站提供准确的个人资料，如个人资料有任何变动，必须及时更新。 </p>
				<p>用户注册成功后，本网站将给予每个用户一个用户帐号及相应的密码，该用户帐号和密码由用户负责保管；用户应当对以其用户帐号进行的所有活动和事件负法律责任。 </p>
				<p>用户在使用本网站网络服务过程中，必须遵循以下原则： </p>
				<p>遵守中国有关的法律和法规； </p>
				<p>不得为任何非法目的而使用网络服务系统； </p>
				<p>遵守所有与网络服务有关的网络协议、规定和程序； </p>
				<p>不得利用136服务系统传输任何危害社会，侵蚀道德风尚，宣传不法宗教组织等内容； </p>
				<p>不得利用136服务系统进行任何可能对互联网的正常运转造成不利影响的行为； </p>
				<p>不得利用136服务系统上载、张贴或传送任何非法、有害、胁迫、滥用、骚扰、侵害、中伤、粗俗、猥亵、诽谤、侵害他人隐私、辱骂性的、恐吓性的、庸俗淫秽的及有害或种族歧视的或道德上令人不快的包括其他任何非法的信息资料； </p>
				<p>不得利用136服务系统进行任何不利于本网站的行为； </p>
				<p>如发现任何非法使用用户帐号或帐号出现安全漏洞的情况，应立即通告本网站。 </p>
				
				<h3>六、本网站的权利及义务</h3>
				<p>本网站除特殊情况外（例如：协助公安等相关部门调查破案等），致力于努力保护会员的个人资料不被外漏，且不得在未经本人的同意下向第三者提供会员的个人资料。 </p>
				<p>本网站根据提供服务的过程，经营上的变化，无需向会员得到同意即可更改，变更所提供服务的内容。</p> 
				<p>本网站在提供服务过程中，应及时解决会员提出的不满事宜，如在解决过程中确有难处，可以采取公开通知方式或向会员发送电子邮件寻求解决办法。 </p>
				<p>本网站在下列情况下可以不通过向会员通知，直接删除其上载的内容： </p>
				<p>有损于本网站，会员或第三者名誉的内容； </p>
				<p>利用136服务系统上载、张贴或传送任何非法、有害、胁迫、滥用、骚扰、侵害、中伤、粗俗、猥亵、诽谤、侵害他人隐私、辱骂性的、恐吓性的、庸俗淫秽的及有害或种族歧视的或道德上令人不快的包括其他任何非法的内容； </p>
				<p>侵害本网站或第三者的版权，著作权等内容； </p>
				<p>存在与本网站提供的服务无关的内容； </p>
				<p>无故盗用他人的ID(固有用户名)，姓名上载、张贴或传送任何内容及恶意更改，伪造他人上载内容。</p>
				 
				<h3>七、知识产权声明</h3>
				<p>本网站（www.136LY.com）所有的产品、技术与所有程序及页面（包括但不限于页面设计及内容）均属于知识产权，仅供个人学习、研究和欣赏，未经授权，任何人不得擅自使用，否则，将依法追究法律责任。 </p>
				<p>西安创XX技有限公司拥有本网站内所有资料（包括但不限于本站所刊载的图片、视频、Flash等）的版权，（版权归属有特殊约定的，从其约定），未经授权，任何人不得擅自使用，否则，将依法追究法律责任。 </p>
				<p>"136旅游网"、（图形）及"136LY.com "西安优游科技有限公司注册商标，任何人不得擅自使用，否则，将依法追究法律责任。</p>
				
				<h3>八、免责声明</h3>
				<p>任何人因使用本网站而可能遭致的意外及其造成的损失（包括因下载本网站可能链接的第三方网站内容而感染电脑病毒），我们对此概不负责，亦不承担任何法律责任。 </p>
				<p>本网站禁止制作、复制、发布、传播等具有反动、色情、暴力、淫秽等内容的信息，一经发现，立即删除。若您因此触犯法律，我们对此不承担任何法律责任。 </p>
				<p>本网站会员自行上传或通过网络收集的资源，我们仅提供一个展示、交流的平台，不对其内容的准确性、真实性、正当性、合法性负责，也不承担任何法律责任。 </p>
				<p>任何单位或个人认为通过本网站网页内容可能涉嫌侵犯其著作权，应该及时向我们提出书面权利通知，并提供身份证明、权属证明及详细侵权情况证明。我们收到上述法律文件后，将会依法尽快处理。</p>
				<p>参与广告策划</p>
				<p>用户可在他们发表的信息中加入宣传资料或参与广告策划，在136旅游网上展示他们的产品。任何这类促销方法，包括运输货物、付款、服务、商业条件、担保及与广告有关的描述都只是在相应的用户和广告销售商之间发生。西安优游科技有限公司不承担任何责任，136旅游网服务没有义务为这类广告销售负任何一部分的责任。</p>
				 
				<h3>九、服务变更、中断或终止</h3>
				<p>如因系统维护或升级的需要而需暂停136服务，本网站将尽可能事先进行通告。</p> 
				<p>如发生下列任何一种情形，本网站有权随时中断或终止向用户提供本协议项下的136服务而无需通知用户： </p>
				<p>用户提供的个人资料不真实； </p>
				<p>用户违反本协议中规定的使用规则。 </p>
				<p>除前款所述情形外，本网站同时保留在不事先通知用户的情况下随时中断或终止部分或全部136服务的权利，对于所有服务的中断或终止而造成的任何损失，本网站无需对用户或任何第三方承担任何责任。</p>
				<p>结束服务</p>
				<p>用户或西安优游科技有限公司可随时根据实际情况中止网站服务。西安优游科技有限公司不需对任何个人或第三方负责而随时中断服务。用户若反对任何服务条款的建议或对后来的条款修改有异议，或对136旅游网会员服务不满，用户只有以下的追索权： ⑴不再使用136旅游网会员服务。 ⑵结束用户使用136旅游网会员服务的资格。 ⑶通告西安优游科技有限公司停止该用户的服务。 结束用户服务后，用户使用136旅游网会员服务的权利马上中止。从那时起，西安优游科技有限公司不再对用户承担任何义务。</p>
				
				<h3>十、违约赔偿</h3>
				<p>用户同意保障和维护本网站及其他用户的利益，如因用户违反有关法律、法规或本协议项下的任何条款而给本网站或任何其他第三者造成损失，用户同意承担由此造成的损害赔偿责任。</p>
				
				<h3>十一、修改协议</h3>
				<p>本网站将可能不时的修改本协议的有关条款，一旦条款内容发生变动，本网站将会在相关的页面提示修改内容。 </p>
				<p>如果不同意本网站对服务条款所做的修改，用户有权停止使用136服务。如果用户继续使用136服务，则视为用户接受服务条款的变动。</p>
				
				<h3>十二、法律管辖</h3>
				<p>本协议的订立、执行和解释及争议的解决均应适用中国法律。 </p>
				<p>如双方就本协议内容或其执行发生任何争议，双方应尽量友好协商解决；协商不成时，任何一方均可向本网站所在地的人民法院提起诉讼。</p>
				
				<h3>十三、其他规定</h3>
				<p>本协议构成双方对本协议之约定事项及其他有关事宜的完整协议，除本协议规定的之外，未赋予本协议各方其他权利。</p> 
				<p>如本协议中的任何条款无论因何种原因完全或部分无效或不具有执行力，本协议的其余条款仍应有效并且有约束力。</p>
        	</div>
    	</div>
    </div>
</div>
</body>
</html>
