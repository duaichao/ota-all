<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%
// 		String token = cn.sd.core.util.CommonUtils.randomint(6);
		String token = cn.sd.core.util.CommonUtils.uuid();
		javax.servlet.http.Cookie c = cn.sd.core.util.WebUtils.getCookie(request, cn.sd.rmi.ServiceProxyFactory.SSO_TOKEN_NAME);
		cn.sd.core.util.WebUtils.saveCookieValue(response, cn.sd.rmi.ServiceProxyFactory.SSO_TOKEN_NAME, token, -1);
 %>
<!DOCTYPE html>
<html>
<head>
  <title>登录 136旅游</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="${ctx }/resources/css/component.css" />
  <link rel="stylesheet" type="text/css" href="${ctx }/resources/css/login.css" />
  
  <!-- <link rel="stylesheet" type="text/css" media="screen and (max-device-width: 400px)" href="tinyScreen.css" />
  <link rel="stylesheet" type="text/css" media="screen and (min-width: 400px) and (max-device-width: 600px)" href="smallScreen.css" /> -->
  <!--[if lt IE 9]>
　　　　<script src="http://css3-mediaqueries-js.googlecode.com/svn/trunk/css3-mediaqueries.js"></script>
　　<![endif]-->
</head>
<body>
<div class="container">
	<div class="header">
		<div class="headerWrap">
			<div class="loginLogo">
				<a href="javascript:;" target="_self">
					<img src="${ctx}/resources/images/login/logo.png" height="60" alt="136旅游" title="136旅游">
				</a>
			</div>
			<div class="city" id="toggle-city">
				<a href="javascrip:;">
					<i class="iconfont">&#xe600;</i>${userCity.localCity} <label>切换</label>
				</a>
				<div class="more-city">
					<c:forEach items="${citys}" var="city">
						<a href="${ctx}/changeCity?cityId=${city.CITY_ID }">${fn:replace(city.SUB_AREA,'市', '') }</a>
					</c:forEach>
				</div>
			</div>
			<div class="contact">
				<span><i class="iconfont" style="font-size:30px;">&#xe601;</i>${siteAttr.SERVICE_PHONE}</span> 
				<span class="servicetime">周一至周日 8:00-18:00</span>
				<a target=“blank” class="qq" href="http://wpa.qq.com/msgrd?V=3&uin=${siteAttr.QQ1}&Site=QQ客服&Menu=yes">
				<img border="0" src="http://wpa.qq.com/pa?p=1:${siteAttr.QQ1}:5" alt="点击这里给我发消息">
				</a>
			</div>	
			
			<div class="nav">
			</div>
		</div>
	</div>
	<div class="container">
		<!-- 焦点图 -->
		<div class="focusImg" id="js_focus">
			<ul class="sliderCon">
				<c:forEach items="${ads}" var="ad">
				<li style="display:block;background:url(${visit}/${ad.PATH }) no-repeat center 0;">
					<a href="${ad.HREF }" target="_blank"></a>
				</li>
				</c:forEach>
			</ul>
			<div class="control" id="js_focus_control">
				<a href="javascript:;" class="prev">前一个</a>
				<a href="javascript:;" class="next">后一个</a>
			</div>
		</div>
		<!-- 登录框 -->
		<div class="loginWrap">
			<div class="loginBox">
				<h1>欢迎登录136旅游!</h1>
				<div class="input-cell">
					<span class="iconfont inputIcon">&#xe603;</span>
					<label>用户名/手机号</label>
					<input type="text" class="inputFields" id="userName">
					<b></b>
					<div class="toolTip" id="login_err">
						<div class="tipArrow"></div>
						<div class="tipInner" id="login_err_msg">用户名或密码不正确！</div>
					</div>
				</div>
				<div class="input-cell">
					<span class="iconfont inputIcon">&#xe602;</span>
					<label>密码</label>
					<input type="password" class="inputFields" id="passWord">
				</div>
				<div class="login-status">
					<input type="checkbox" id="autoLogin"/>
					<label class="checkbox" id="cookieloginck" style="background-repeat:repeat-x;"></label>
					<label>7天内免登录</label>
					<a href="${ctx}/forget" target="_blank">找回密码</a>
				</div>
				<div class="input-cell btns">
				<a href="javascript:;" class="btnSign" id="btn_login">登录</a>
				<a href="${ctx}/regist" target="_blank" class="btnSign reg">入驻136</a>
				</div>
			</div>
		</div>
		<!-- 品牌展示 -->
		<div class="tt-grid-wrapper" style="display:none;">
			<h1>品牌商家</h1>	  
			<ul class="tt-grid tt-effect-stackback tt-effect-delay" id="rec-supply">
			</ul>
			<nav>
			</nav>
        </div>


<!-- 		<div class="por-warpper">  -->
			<!-- 入驻流程 -->
<!-- 			<div class="process"> -->
<!-- 				<h1>入驻流程</h1> -->
<!-- 				<div class="step info"> -->
<!-- 					<img src="${ctx}/resources/images/login/commit-step.png"/> -->
<!-- 					<span>填写资料</span> -->
<!-- 				</div> -->
<!-- 				<div class="arrow"></div> -->
<!-- 				<div class="step audit"> -->
<!-- 					<img src="${ctx}/resources/images/login/audit-step.png"/> -->
<!-- 					<span>平台审核</span> -->
<!-- 				</div> -->
<!-- 				<div class="arrow"></div> -->
<!-- 				<div class="step happy"> -->
<!-- 					<img src="${ctx}/resources/images/login/finish-step.png"/> -->
<!-- 					<span>签约入驻</span> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- 服务保障 -->
<!-- 			<div class="promise"> -->
<!-- 				<h1>服务保障</h1> -->
<!-- 				<div class="step "> -->
<!-- 					<img src="${ctx}/resources/images/login/sev01.png"/> -->
<!-- 				</div>  -->
<!-- 				<div class="step "> -->
<!-- 					<img src="${ctx}/resources/images/login/sev02.png"/> -->
<!-- 				</div> -->
<!-- 				<div class="step ">  -->
<!-- 					<img src="${ctx}/resources/images/login/sev03.png"/> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div style="clear:both;"></div> -->
<!-- 		</div> -->



	</div>
	<!-- 版权 -->
	<div class="footer">
		<p>Copyright &copy; 2012-2017 All Rights Reserved 琼ICP备16003588号 版权所有 </p>
		<p>
		<iframe style="width:94px;height:32px;margin-top:10px;" src="http://aic.hainan.gov.cn:880/lz.ashx?vie=076144A08548ACE3DB843B1F0B84B9CF105A91E7284F2543730B39E8A1763DCBFA0E26883413D48C3F79BAD6F27B318B" allowtransparency="true" scrolling="no" style="overflow:hidden;" frameborder="0"></iframe>
		</p>
	</div>
	
</body>
</html>
<script type="text/javascript" src="${ctx}/resources/js/modernizr.custom.js" ></script>
<script type="text/javascript" src="${ctx}/resources/js/jQuery.1.7.1.min.js?ver=${version}"></script>
<script type="text/javascript" src="${ctx}/resources/js/jquery.cookie.js?ver=${version}"></script>
<script type="text/javascript" src="${ctx }/resources/js/RSA.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#toggle-city').hover(function(){
			$(this).addClass('hover').find('div.more-city').show();
		},function(){
			$(this).removeClass('hover').find('div.more-city').hide();
		});
		$('#cookieloginck').click(function(){
			var ck = $(this).prev();
			if(ck[0].checked){
				ck.removeAttr("checked");
			}else{
				ck.attr("checked",'true');
			}
		});
		$('#userName').focus(function(){
			$(this).prev().hide();
		}).blur(function(){
			if($(this).val()=='')
			$(this).prev().show();
		});
		$('#passWord').focus(function(){
			$(this).prev().hide();
		}).blur(function(){
			if($(this).val()=='')
			$(this).prev().show();
		});
		$('#btn_login').click(loginFn);
		document.onkeydown = function(e) {  
		    // 兼容FF和IE和Opera  
		    var theEvent = e || window.event;  
		    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;  
		    var activeElementId = document.activeElement.id;//当前处于焦点的元素的id  
		    if (code == 13) {  
		        loginFn();//要触发的方法  
		        return false;  
		    }  
		    return true;  
		}  
	});
	function doLogin(name,pwd){
		var key,spwd;
		setMaxDigits(130);
		key = new RSAKeyPair("${e}","","${m}");
		spwd = encryptedString(key, encodeURIComponent(pwd));
		$('#btn_login').addClass('loging');
		$.ajax({ 
			type: "post", 
			url:'${ctx}/login',
			dataType:'json',
			data: {'pm[USER_NAME]':name,'pm[USER_PWD]':spwd},
			success: function(rs){
				if(rs.success){
					if($('#autoLogin').attr('checked')){
						$.cookie('SECURITY_USER_KEY', name+':'+pwd, { expires: 7 });
					}
					window.location.href = '${ctx}/b2b/user/index' ;
				}else{
					$('#btn_login').removeClass('loging');
					$("#login_err_msg").html("<i class=\"icon-info-circled\"></i> "+rs.info);
					$('#login_err').show();
					$('#login_err').prev().show();
					$("#passWord").val('');
				}
			},
			error:function(){
				$('#btn_login').removeClass('loging');
				$("#login_err_msg").html("<i class=\"icon-info-circled\"></i> "+rs.info);
				$('#login_err').show();
				$('#login_err').prev().show();
				$("#passWord").val('');
			}
		}); 
	}
	function loginFn(){
		if($('#btn_login').hasClass('loging')){
			return;
		}
		if($("#userName").val()==''||$("#passWord").val()==''){
			$("#login_err_msg").html("<i class=\"icon-info-circled\"></i> 请输入用户或密码");
			$('#login_err').show();
			$('#login_err').prev().show();
			$("#passWord").val('');
			return;
		}
		doLogin($("#userName").val(),$("#passWord").val());
	}
	;(function () {
        var defaultInd = 0;
        var list = $('#js_focus ul').children();
        var count = 0;
        var change = function (newInd, callback) {
            if (count) return;
            count = 2;
            $(list[defaultInd]).fadeOut(400, function () {
                count--;
                if (count <= 0) {
                    if (start.timer) window.clearTimeout(start.timer);
                    callback && callback();
                }
            });
            $(list[newInd]).fadeIn(400, function () {
                defaultInd = newInd;
                count--;
                if (count <= 0) {
                    if (start.timer) window.clearTimeout(start.timer);
                    callback && callback();
                }
            });
        }

        var next = function (callback) {
            var newInd = defaultInd + 1;
            if (newInd >= list.length) {
                newInd = 0;
            }
            change(newInd, callback);
        }

        var start = function () {
            if (start.timer) window.clearTimeout(start.timer);
            start.timer = window.setTimeout(function () {
                next(function () {
                    start();
                });
            }, 8000);
        }
        start();
        $('#js_focus_control').on('click', 'a', function () {
            var btn = $(this);
            if (btn.hasClass('next')) {
                //next
                next(function () {
                    start();
                });
            }
            else {
                //prev
                var newInd = defaultInd - 1;
                if (newInd < 0) {
                    newInd = list.length - 1;
                }
                change(newInd, function () {
                    start();
                });
            }
            return false;
        });
    })();
    ;(function(){
		var pageSize=6,animEndEventNames = {
				'WebkitAnimation' : 'webkitAnimationEnd',
				'OAnimation' : 'oAnimationEnd',
				'msAnimation' : 'MSAnimationEnd',
				'animation' : 'animationend'
			},
			animEndEventName = animEndEventNames[ Modernizr.prefixed( 'animation' ) ],
			eventtype =  'click',
			support = Modernizr.cssanimations;

		function onAnimationEnd( elems, len, callback ) {
			var finished = 0,
				onEndFn = function() {
					this.removeEventListener( animEndEventName, onEndFn );
					++finished;
					if( finished === len ) {
						callback.call();
					}
				};
			for(var i=0;i<elems.length;i++){
				var a = $(elems[i]).find('a')[0];
				a.addEventListener( animEndEventName, onEndFn ); 
			}
		}
		function init() {
			var grid = $('.tt-grid-wrapper  .tt-grid'),
				items = $('.tt-grid-wrapper  .tt-grid  li'),
				navDots = $('.tt-grid-wrapper nav  a'),
				isAnimating = false,current = 0;

			navDots.each(function(i){
				this.addEventListener( eventtype, function( ev ) {
					if( isAnimating || current === i ) return false;
					ev.preventDefault();
					isAnimating = true;
					updateCurrent( i );
					loadNewSet( i );
				} );
			});
			function updateCurrent( set ) {
				$( navDots[ current ]).removeClass('tt-current' );
				$(  navDots[ set ]).addClass( 'tt-current' );
				current = set;
			}
			function loadNewSet( set ) {
				$.getJSON("${ctx}/companys",{
					start:set*pageSize,
					limit:pageSize
				},function(json){
					var data = json.data,newImages=[];
					for(var i=0;i<data.length;i++){
						newImages.push([
							'<a href="javascript:;" class="shadow">',
							'<img src="${visit}/'+data[i].LOGO_URL+'"/>',
							'<span class="name">'+data[i].COMPANY+'</span>',
							'</a>'
						].join(''));
					};
					items.each( function( i ) {
						$(this).find( 'a' ).each(function(){
							$(this).addClass('tt-old');
						});
					});
					setTimeout( function() {
						for(var i=0;i<newImages.length;i++){
							items[ i ].innerHTML += newImages[i];
						}
						grid.addClass('tt-effect-active');
						var onEndAnimFn = function() {
							items.each( function( i ) {
								var old = $(this).find( 'a.tt-old' );
								if( old.length>0 ) { this.removeChild( old[0] ); }
								$(this).removeClass('tt-empty' );
								if ( !this.hasChildNodes() ) {
									$(this).addClass('tt-empty' );
								};
							} );
							grid.removeClass('tt-effect-active' );
							isAnimating = false;
						};
						if( support ) {
							onAnimationEnd( items, items.length, onEndAnimFn );
						}
						else {
							onEndAnimFn.call();
						}
					}, 25 );
				});
			}
		}
		$.getJSON("${ctx}/companys",{
			start:0,
			limit:pageSize
		},function(json){
			var target = $('#rec-supply'),nav = target.next(),data = json.data,total=parseInt(json.totalSize),
				pages = total%pageSize==0?total/pageSize:parseInt(total/pageSize)+1;
			for(var i=0;i<data.length;i++){
				target.append([
					'<li><a href="javascript:;" class="shadow">',
					'<img src="${visit}/'+data[i].LOGO_URL+'"/>',
					'<span class="name">'+data[i].COMPANY+'</span>',
					'</a></li>'
				].join(''));
			};
			for(var j=0;j<pages;j++){
				nav.append('<a class="'+(j==0?'tt-current':'')+'"></a>');
			}
			init();
		});
		var ck = $.cookie('SECURITY_USER_KEY'),np;if(ck){np = ck.split(':');doLogin(np[0],np[1]);}
    })();
</script>