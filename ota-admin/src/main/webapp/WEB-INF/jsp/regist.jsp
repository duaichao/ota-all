<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <title>注册_136旅游</title>
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
			<div class="city" id="toggle-city">
				<a href="javascrip:;">
					<i class="iconfont">&#xe778;</i>${userCity.localCity} <label>切换</label>
				</a>
				<div class="more-city">
					<c:forEach items="${citys}" var="city">
						<a href="${ctx}/changeCity?cityId=${city.CITY_ID }">${fn:replace(city.SUB_AREA,'市', '') }</a>
					</c:forEach>
				</div>
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
			<div class="top-step">
			    <ul>
			        <li class="current">填写资料</li>
			        <li>平台审核</li>
			        <li>签约入驻</li>
			    </ul>
			</div>
			<div class="reg-err" id="err"><i class="icon-attention-circled"></i>请核对信息是否填写完整，并同意用户服务协议，注：附件必须上传</div>
			<!-- 注册项 -->
			<form action="${ctx}/join" method="post" enctype="multipart/form-data" id="registForm">
			<input type="hidden" name="pm[CITY_ID]" value="${userCity.localCityID}"/>
			<input type="hidden" name="pm[CITY_NAME]" value="${userCity.localCity}"/>
			<input type="hidden" name="pm[SOURCE]" value="1"/>
			<div class="register-box">
		        <div class="reg-cell">
		            <h4>公司名称</h4>
		            <div class="input-box">
		            	<label></label>
		                <input type="text" name="pm[COMPANY]">
		            </div>
		            <div class="input-box mini150">
		            	<label>负责人</label>
		                <input type="text" name="pm[LEGAL_PERSON]">
		            </div>
		            <div class="input-box mini150">
		            	<label>座机/手机</label>
		                <input type="text" name="pm[PHONE]">
		            </div>
		        </div>
		        <div class="reg-cell">
		            <h4>公司地址</h4>
		            <div class="input-box w750">
		                <label></label>
		                <input type="text" name="pm[ADDRESS]">
		            </div>
		        </div>
				<div class="reg-cell">
		            <h4>许可证副本</h4>
		            <div class="input-box">
		                <input type="file" class="file" name="LICENSE_ATTR">
		            </div>
		            <div class="input-box mini310">
		                <label>许可证号</label>
		                <input type="text" name="pm[LICENSE_NO]">
		            </div>
		        </div>
		        <div class="reg-cell">
		            <h4>营业执照</h4>
		            <div class="input-box">
		                <input type="file" class="file" name="BUSINESS_URL">
		            </div>
		            <div class="input-box mini310">
		                <label>品牌名称</label>
		                <input type="text" name="pm[BRAND_NAME]">
		            </div>
		        </div>
		        <div class="reg-cell">
		            <h4>经营范围</h4>
		            <div class="input-box w750 h60">
		                <label>简单描述公司经营业务</label>
		                <textarea name="pm[BUSINESS]"></textarea>
		            </div>
		        </div>
		        <div class="reg-cell" style="padding-top:15px;">
                    <h4>登录名</h4>
                    <div class="input-box mini310" style="margin-left:0px;">
                        <label>4-20位字符，支持字母、数字组合</label>
                        <input type="text" id="userName" name="pm[USER_NAME]">
                    </div>
                </div>
		        <div class="reg-cell">
                    <h4>设置密码</h4>
                    <div class="input-box mini310" style="margin-left:0px;">
                        <label>8-20个字符，区分大小写</label>
                        <input type="password" name="pm[USER_PWD]" id="passWord">
                        <input type="text" name="password" maxlength="20" style="display:none;">
                        <b class="icon-eye" id="pwdeye" title="查看密码" style="display: none"></b>
                    </div>
                </div>
		        <div class="reg-bottom">
		            <input type="checkbox" checked="true">
		            <label class="checkbox" id="agreecheck"></label>
		            <label>同意《<a href="${ctx}/files/agreement.doc" target="_self">136旅游用户服务协议</a>》</label>
		            <a href="javascript:;" class="button btn-hilight" id="btn_regist">注册</a>
		        </div>
		    </div>
		    </form>
		</div>
	</div>
	<!-- 版权 -->
	<div class="footer">
		<p>Copyright &copy; 2012-2017 All Rights Reserved 琼ICP备16003588号 版权所有 </p>
	</div>
</body>
</html>
<script type="text/javascript" src="${ctx}/resources/js/jQuery.1.7.1.min.js?ver=${version}"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.form.js?ver=${version}"></script>
<script type="text/javascript">
	(function(){
        $.extend($.fn,{
            mask: function(msg,maskDivClass,noClose){
                this.unmask();
                // 参数
                var op = {
                    opacity: 0.8,
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
                var btnStr = '<a href="javascript:window.opener=null;window.open(\'\',\'_self\');window.close();" class="button-finish" id="btn_finish">关闭</a>';
                if(noClose){
                	btnStr = '<a href="javascript:;" onclick="closeMask(this)" class="button-finish" id="btn_finish">关闭</a>';
                }
                if(msg){
                    var msgDiv=$([
                    '<div class="alt-box">',
                    '<h3>'+msg+'</h3>',
                    btnStr,
                    '</div>'
                    ].join('')
                    );
                    msgDiv.appendTo(original);
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
    function closeMask(){
         $("div.maskdivgen").fadeOut('slow',0,function(){
             $(this).remove();
             $('div.alt-box').remove();
         });
    }
	$(function(){
		$('#toggle-city').hover(function(){
			$(this).addClass('hover').find('div.more-city').show();
		},function(){
			$(this).removeClass('hover').find('div.more-city').hide();
		});
		$('#agreecheck').click(function(){
			var ck = $(this).prev();
			if(ck[0].checked){
				ck.removeAttr("checked");
			}else{
				ck.attr("checked",'true');
			}
		});
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
		$('textarea').focus(function(){
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
		
		
		$('#btn_regist').click(registFn);
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
	function registFn(){
		if($('#btn_regist').hasClass('loging')){
			return;
		}
		var isDo = true;
		$('input').each(function(){
			if($(this).val().trim()==''){
				isDo=false;
			}
			if($(this).attr('type') == 'checkbox' && 'checked' != $(this).attr('checked')){
				isDo=false;
			}
		});
		if(!isDo){
			$('#err').slideDown();
			setTimeout(function(){
				$('#err').slideUp();
			},5000);
			$('#passWord').next().val('');
			$("#passWord").val('');
			return false;
		}else{
			$('#btn_regist').addClass('loging');
			var options = {
		        success: function (data) {$('#btn_regist').removeClass('loging');
		        	if(parseInt(data.statusCode)<0){
		        		var str=['','公司信息重复','用户名已存在'];
		        		$(document).mask('<i class="iconfont" style="color:#dd4b39;font-size:30px;">&#xe797;</i> <span style="color:#dd4b39;">'+str[0-parseInt(data.statusCode)]+'</span>','',true);
		        	}else{
		        		$(document).mask('<i class="iconfont" style="color:#53a93f;font-size:30px;">&#xe796;</i> 资料提交成功，请等待平台审核...');
		        	}
		            
		        }
		    };
			$('#registForm').ajaxSubmit(options);
			return false;
		}
	}
</script>