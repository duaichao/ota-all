<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta charset="UTF-8">
<link rel="shortcut icon" href="${ctx}/favicon.ico" />
<!--<link type="text/css" rel="stylesheet" href="${ctx }/resources/font/fontello/fontello.css" />-->
<!--<script type="text/javascript" src="${ctx }/resources/js/zeroclipboard/ZeroClipboard.min.js"></script>-->
<!-- <script type="text/javascript" src="${ctx }/resources/js/ext/bootstrap.js"></script> -->
<script type="text/javascript" src="${ctx }/resources/js/include-ext.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx }/resources/css/global.css" />
<!-- <script type="text/javascript" src="${ctx }/resources/js/options-toolbar.js"></script>-->
<script type="text/javascript" src="${ctx }/resources/js/app/Config.js"></script>

<script type='text/javascript'>
	var cfg = Ext.create('config',{
		ctx: '${ctx}',
		picCtx: '${visit}',
		webCtx: '',
		jsPath : '${ctx}/resources/js',
		extPath: '${ctx}/resources/js/ext',
		imgPath: '${ctx}/resources/images',
		cssPath: '${ctx}/resources/css',
		fontPath: '${ctx}/resources/font',
		user:{
			id:'${S_USER_SESSION_KEY.ID}',
	    	ip:'${S_USER_SESSION_KEY.LOGIN_IP}',
	    	signature:'${S_USER_SESSION_KEY.SIGNATURE}',
	    	logintime:'${S_USER_SESSION_KEY.LOGIN_TIME}',
	    	username:'${S_USER_SESSION_KEY.USER_NAME}',
	    	chianname:'${S_USER_SESSION_KEY.CHINA_NAME}',
	    	birthday:'${S_USER_SESSION_KEY.BIRTHDAY}',
	    	face:'${S_USER_SESSION_KEY.FACE}',
	    	mobile:'${S_USER_SESSION_KEY.MOBILE}',
	    	email:'${S_USER_SESSION_KEY.EMAIL}',
	    	role:'${S_USER_SESSION_KEY.ROLE}',
	    	score:'${S_USER_SESSION_KEY.SCORE}',
	    	level:'${S_USER_SESSION_KEY.USER_LEVEL}',
	    	roleId:'${S_USER_SESSION_KEY.ROLE_ID}',
	    	modules:'${S_USER_SESSION_KEY.MODULES}',
	    	sessionId:'${pageContext.session.id}',
	    	userType:'${S_USER_SESSION_KEY.USER_TYPE}',
	    	cityId:'${S_USER_SESSION_KEY.CITY_ID}',
	    	cityName:'${S_USER_SESSION_KEY.CITY_NAME}',
	    	cityPinYin:'${S_SITE_SESSION_KEY.PINYIN}',
	    	siteId:'${S_SITE_SESSION_KEY.ID}',
	    	companyId:'${S_USER_SESSION_KEY.COMPANY_ID}',
	    	companyType:'${S_USER_SESSION_KEY.COMPANY_TYPE}',
	    	departId:'${S_USER_SESSION_KEY.DEPART_ID}',
	    	saleTraffic:'${S_USER_SESSION_KEY.SALE_TRAFFIC}',
	    	saleRoute:'${S_USER_SESSION_KEY.SALE_ROUTE}',
	    	departName:'${S_USER_SESSION_KEY.DEPART_NAME}',
	    	companyName:'${S_USER_SESSION_KEY.COMPANY}',
	    	pid:'${S_USER_SESSION_KEY.PID}',
	    	oldDelayDay:'${S_USER_SESSION_KEY.OLD_DELAY_DAY}',
	    	oldAuditDay:'${S_USER_SESSION_KEY.OLD_AUDIT_DAY}',
	    	shareRoute:'${S_USER_SESSION_KEY.SHARE_ROUTE}',
	    	isContractMust:'${S_USER_SESSION_KEY.PARENT_IS_CONTRACT_MUST}',
	    	isSetPayPassword:'${S_USER_SESSION_KEY.PAY_PWD_TYPE}'
		}
	});
	//ZeroClipboard.config( { swfPath: cfg.getJsPath()+"/zeroclipboard/ZeroClipboard.swf" } );
	//Ext.BLANK_IMAGE_URL = '<c:url value="/js/ext/resources/images/default/s.gif"/>';
	Ext.Loader.setConfig({
	    enabled: true,
		paths: {
			'Ext': '${ctx}/resources/js/ext/src',
			'app': '${ctx}/resources/js/app'
		}
	});
	if ('nocss3' in Ext.Object.fromQueryString(location.search)) {
        Ext.supports.CSS3BorderRadius = false;
        Ext.getBody().addCls('x-nbr x-nlg');
    }
	Ext.tip.QuickTipManager.init();
	Ext.override(Ext.tip.ToolTip, {  
	    listeners:{
	    	show:function(tip){
	    		tip.dismissDelay =  1000 * (5 + (tip.html && tip.html.split(/[\\pP‘’“”]/g).length || 0));
	    	}
	    }
	});  
    Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));//默认cookie7天
	//Ext.setGlyphFontFamily('fontello');
	
	document.onkeydown = function() {
	 // 如果按下的是退格键
	    if(event.keyCode == 8) {
	     // 如果是在textarea内不执行任何操作
	        if(event.srcElement.tagName.toLowerCase() != "input"  && event.srcElement.tagName.toLowerCase() != "textarea" && event.srcElement.tagName.toLowerCase() != "password")
	            event.returnValue = false; 
	        // 如果是readOnly或者disable不执行任何操作
	  if(event.srcElement.readOnly == true || event.srcElement.disabled == true) 
	            event.returnValue = false;                             
	     }
	}
</script>

<script type="text/javascript" src="${ctx }/resources/js/app/Utils.js"></script>
<script>util.init();</script>
