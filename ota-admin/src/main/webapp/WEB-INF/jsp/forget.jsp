<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <title>找回密码_136旅游</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="${ctx }/resources/css/regist.css" />
</head>
<body>
<div class="container">
	<div class="header">
		<div class="headerWrap">
			<div class="loginLogo">
				<a href="${ctx }/" target="_self">
					<img src="${ctx}/resources/images/login/logo.png" height="60" alt="136旅游" title="136旅游">
				</a>
			</div>
			<div class="contact">
				<span><i class="iconfont" style="font-size:30px;">&#xe781;</i>${siteAttr.SERVICE_PHONE}</span> 
				<span class="servicetime">周一至周日 8:00-18:00</span>
				<a target=“blank” class="qq" href="http://wpa.qq.com/msgrd?V=3&uin=${siteAttr.QQ1}&Site=QQ客服&Menu=yes">
				<img border="0" src="http://wpa.qq.com/pa?p=1:${siteAttr.QQ1}:5" alt="点击这里给我发消息">
				</a>
			</div>	
		</div>
	</div>
	<div class="wrap">
		<div class="container">
			<div class="top-step for">
				<h3>通过手机找回</h3>
			    <!--<ul>
			        <li class="current"><i class="icon-mobile-1"></i>手机找回</li>
			        <li><i class="icon-mail"></i>邮箱找回</li>
			    </ul>-->
			</div>
			<div class="reg-err" id="err">请核对信息是否填写完整</div>
			<!-- 注册项 -->
			<form action="${ctx}/findPwd" method="post" id="registForm">
			<div class="register-box for">
		        <div class="reg-cell">
                    <h4>手机号码</h4>
                    <div class="input-box mini328">
                        <label>填写手机号</label>
                        <input type="text" name="mobile" id="mobile" style="ime-mode:disabled;">
                    </div>
                </div>
                <div class="reg-cell">
                    <h4>重置密码</h4>
                    <div class="input-box mini328">
                        <label>8-20个字符，区分大小写</label>
                        <input type="password" name="passWord" id="passWord">
                        <input type="text" name="password" maxlength="20" style="display:none;">
                        <b class="icon-eye" id="pwdeye" title="查看密码" style="display: none"></b>
                    </div>
                </div>
                <div class="reg-cell">
                    <h4>验证码</h4>
                    <div class="input-box mini128" >
                        <label>输入验证码</label>
                        <input type="text" name="code">
                    </div>
                    <a href="javascript:;" class="button btn-disabled" id="codeBtn">免费获取验证码</a>
                </div>
		        <div class="reg-bottom" style="margin-left:-240px;">
		            <a href="javascript:;" class="button btn-hilight" id="btn_find">提交</a>
		        </div>
		    </div>
		    </form>
		</div>
	</div>
	<!-- 版权 -->
	<div class="footer">
		<p>Copyright &copy; 2012-2017 All Rights Reserved 琼ICP备16003588号 版权所有</p>
	</div>
</body>
</html>
<script type="text/javascript" src="${ctx}/resources/js/jQuery.1.7.1.min.js?ver=${version}"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.form.js?ver=${version}"></script>
<script type="text/javascript">
	(function(){
        $.extend($.fn,{
            mask: function(msg,maskDivClass){
                this.unmask();
                // 参数
                var op = {
                    opacity: 0.9,
                    z: 10000,
                    bgcolor: '#ccc'
                };
                var original=$(document.body);
                var position={top:0,left:0};
                            if(this[0] && this[0]!==window.document){
                                original=this;
                                position=original.position();
                            }
                // 创建一个 Mask 层，追加到对象中
                var maskDiv=$('<div class="maskdivgen">&nbsp;</div>');
                maskDiv.appendTo(original);
                var maskWidth=original.outerWidth();
                if(!maskWidth){
                    maskWidth=original.width();
                }
                var maskHeight=original.outerHeight();
                if(!maskHeight){
                    maskHeight=original.height();
                }
                maskDiv.css({
                    position: 'absolute',
                    top: position.top,
                    left: position.left,
                    'z-index': op.z,
                    width: maskWidth,
                    height:maskHeight,
                    'background-color': op.bgcolor,
                    opacity: 0
                });
                if(maskDivClass){
                    maskDiv.addClass(maskDivClass);
                }
                if(msg){
                    var msgDiv=$([
                    '<div class="alt-box">',
                    '<h3>'+msg+'</h3>',
                    '<a href="javascript:window.opener=null;window.open(\'\',\'_self\');window.close();" class="button-finish" id="btn_finish">关闭</a>',
                    '</div>'
                    ].join('')
                    );
                    msgDiv.appendTo(maskDiv);
                    var widthspace=(maskDiv.width()-msgDiv.width());
                    var heightspace=(maskDiv.height()-msgDiv.height());
                    msgDiv.css({
                                top:(heightspace/2-2)-100,
                                left:(widthspace/2-2)
                      });
                  }
                  maskDiv.fadeIn('fast', function(){
                    // 淡入淡出效果
                    $(this).fadeTo('slow', op.opacity);
                })
                return maskDiv;
            },
         unmask: function(){
                     var original=$(document.body);
                         if(this[0] && this[0]!==window.document){
                            original=$(this[0]);
                      }
                      original.find("> div.maskdivgen").fadeOut('slow',0,function(){
                          $(this).remove();
                      });
            }
        });
    })();
	$(function(){
		$('input[type=text]').focus(function(){
			$(this).prev().hide();
		}).blur(function(){
			if($(this).val()=='')
			$(this).prev().show();
		});
		$('input[type=password]').focus(function(){
			$(this).prev().hide();
		}).blur(function(){
			if($(this).val()=='')
			$(this).prev().show();
		});
		
		$('#passWord').on('keyup',function(){
			$(this).next().val($(this).val());
			$(this).next().next().show();
		});
		$('#pwdeye').on('click',function(){
			if($(this).hasClass('icon-eye')){
				$(this).prev().show();
				$(this).prev().prev().hide();
				$(this).removeClass('icon-eye').addClass('icon-eye-off');
			}else{
				$(this).prev().hide();
				$(this).prev().prev().show();
				$(this).removeClass('icon-eye-off').addClass('icon-eye');
			}
		});
		$('#mobile').on('keyup',function(){
			var reg = /^(1[3|5|8])[\d]{9}$/,
				btn = $('#codeBtn');
			if(reg.test($(this).val())&&btn.html().indexOf('发送')==-1){
				btn.removeClass('btn-disabled');
			}else{
				btn.addClass('btn-disabled');
			}
		});
		$('#codeBtn').click(function(){
			var mf = $('#mobile'),b=$(this);
			if(b.hasClass('btn-disabled')){
				return;
			}
       		b.html('60秒后重新发送');
       		b.addClass('btn-disabled');
       		var i=60,inter = setInterval(function(){
       			if(i==0){
       				b.html('免费获取验证码');
       				b.removeClass('btn-disabled');
       				clearInterval(inter);
       				inter = null;
       			}else{
        			i--;
        			b.html(i+'秒后重新发送');
       			}
       		},1000);
       		$.getJSON('${ctx}/sendCode?type=1&mobile='+mf.val(),function(data){
       			if(data.success){
       			}else{
       				if(data.statusCode=='-2'){
       					showErr('<i class="icon-attention-circled"></i>抱歉，您的手机号码没有绑定');
       				}else{
       					showErr('<i class="icon-attention-circled"></i>获取验证码失败，请稍候重试');
       				}
       			}
       		});
		});
		$('#btn_find').click(findFn);
		document.onkeydown = function(e) {  
		    // 兼容FF和IE和Opera  
		    var theEvent = e || window.event;  
		    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;  
		    var activeElementId = document.activeElement.id;//当前处于焦点的元素的id  
		    if (code == 13) {  
		        registFn();//要触发的方法  
		        return false;  
		    }  
		    return true;  
		}  
	});
	function showErr(msg){
		$('#err').html(msg).slideDown();
		setTimeout(function(){
			$('#err').slideUp();
		},5000);
	}
	function findFn(){
		if($('#btn_find').hasClass('loging')){
			return;
		}
		var isDo = true;
		$('input').each(function(){
			if($(this).val().trim()==''){
				isDo=false;
			}
		});
		if(!isDo){
			showErr('<i class="icon-attention-circled"></i>请核对信息是否填写完整');
			$('#passWord').next().val('');
			$("#passWord").val('');
			return false;
		}else{
			$('#btn_find').addClass('loging');
			var options = {
		        success: function (data) {
		            $(document).mask('<i class="icon-ok-circled"></i>密码找回成功，去试试登录');
		        }
		    };
			$('#registForm').ajaxSubmit(options);
			return false;
		}
	}
</script>