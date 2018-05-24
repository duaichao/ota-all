Ext.define('app.controller.index', {
	extend: 'Ext.app.Controller',
	requires: [
		'Ext.ux.form.field.CardNumber',
        'Ext.ux.button.HoverButton',
        'Ext.ux.button.BadgeButton',
        'Ext.ux.upload.FaceUpload'
    ],
    
    views : [
    	'header',
        'container',
        'footer',
        'notice',
        'navigation.Breadcrumb',
        'thumbnails.Thumbnails',
        'thumbnails.AllThumbnails',
        'thumbnails.NavThumbnails'
    ],
    stores: [
        'Thumbnails'
    ],
    redirectParams : '',
	config: {
        control: {
        	'navigation-breadcrumb button[id=btn-user-home]': {
                click: 'onHomeButtonClick'
            }
            ,'navigation-breadcrumb breadcrumb': {
                selectionchange: 'onBreadcrumbNavSelectionChange'
            },'navigation-breadcrumb breadcrumb button': {
                click: function(btn){
                	if(btn._breadcrumbNodeId==btn.up('breadcrumb').getSelection().id){
                		//document.location.reload();
                		this.redirectTo(btn._breadcrumbNodeId);
                	}
                }
            },
            'thumbnails': {
                itemclick: 'onThumbnailClick',
                itemmouseenter:'onThumbnailMouseEnter',
                itemmouseleave:'onThumbnailMouseLeave'
            },
            'allthumbnails': {
                itemclick: 'onAllThumbnailClick',
                itemmouseenter:'onThumbnailMouseEnter',
                itemmouseleave:'onThumbnailMouseLeave'
            },
            'navthumbnails': {
                itemclick: 'onNavThumbnailClick',
                itemmouseenter:'onThumbnailMouseEnter',
                itemmouseleave:'onThumbnailMouseLeave'
                //itemmouseenter:'onNavThumbnailMouseEnter',
                //itemmouseleave:'onNavThumbnailMouseLeave'
            },
			'menu menuitem[id=btn-theme-menu-random]':{
				click:'doRandomImage'
			},
			'dataview#themeMenu':{
				itemclick:'setThemeBackground'
			},
			'colorpicker':{
				select:'selectedColor'
			},
			'button[id=btn-user-setting]':{
				click:'onBtnSettingClick'
			},
			'button[id=btn-user-pwd]':{
				click:'onBtnPwdClick'
			},
			'button[id=btn-pay-pwd]':{
				click:'onBtnPayPwdClick'
			},
			'button[id=btn-user-logout]':{
				click:'onBtnLogoutClick'
			},
			'button[id=btn-logout]':{
				click:'onBtnLogoutClick'
			},
			'button[id=btn-user-msg]':{
				click:function(){
					Ext.Ajax.request({
						url:cfg.getCtx()+'/b2b/user/test/send',
						params:{ID:id},
						success:function(response){
						}
					});
				}
			},
			'button#btnUserInfo':{click:'editUserInfo'}
        },
        refs: {
            viewport: 'viewport',
            navigationBreadcrumb: 'navigation-breadcrumb',
            headNavigation:'navthumbnails',
            appContainer: 'appContainer',
            thumbnails: {
                selector: 'thumbnails',
                xtype: 'thumbnails',
                autoCreate: true
            },
            appNotice:'appNotice'
        },
        routes  : {
            ':id': {
            	allowInactive:true,
                action: 'handleRoute'
                /*before: 'beforeHandleRoute'*/
            }
        }
    },
    beforeHandleRoute:function(id,action){
    	var me = this,
            node = Ext.StoreMgr.get('navigation').getNodeById(id);
        if (node) {
            action.resume();
        } else {
        	util.alert('哇塞，你非法穿越了');
            me.redirectTo(me.getApplication().getDefaultToken());
            action.stop();
        }
    },
    handleRoute:function(id){
    	if(id=='home'){
    		var appContainer = this.getAppContainer()/*,
    			node = Ext.StoreMgr.get('navigation').getNodeById('nav_home');
    		this.getNavigationBreadcrumb().setSelection(node)*/;
	    	appContainer.removeAll(true);
	    	appContainer.add(Ext.create('Ext.ux.IFrame',{
				src:cfg.getCtx()+'/b2b/user/home',
				id:'user-home-iframe',
				frameName:'user-home-iframe',
				style:'width:100%;height:100%;'
			}));
    		return;
    	}
    	var me = this,store = Ext.StoreMgr.get('navigation'),
    		navigationBreadcrumb = me.getNavigationBreadcrumb(),
    		headNav = me.getHeadNavigation(),
    		headStore = Ext.StoreMgr.get('headnavigation'),
            node = store.getNodeById(id),
            headNode = headStore.getNodeById(id);
        if(!node){
        	util.alert('哇塞，你非法穿越了');
        	return;
        }
        var text = node.get('text'),
            appContainer = me.getAppContainer(),
            themeName = Ext.themeName,
            thumbnails = me.getThumbnails(),
            thumbnailsStore;
        navigationBreadcrumb.down('breadcrumb').previousSibling().hide();
		navigationBreadcrumb.setSelection(node);
		
		//头部订单跟随跳转自动对应选中
		if(headNode.parentNode.isRoot()){
			headNav.getSelectionModel().select(headNode.data.index);
		}else{
			headNav.getSelectionModel().select(headNode.parentNode.data.index);
		}
        Ext.suspendLayouts();

        if (node.isLeaf()) {
            if (thumbnails.ownerCt) {
                appContainer.remove(thumbnails, false); 
            } else {
                appContainer.removeAll(true);
            }
            appContainer.body.addCls('app-example');
	        appContainer.add(Ext.create('Ext.ux.IFrame',{
				src:cfg.getCtx()+'/'+node.data.url+me.redirectParams,
				id:node.id+'-iframe',
				frameName:node.id+'-iframe',
				style:'width:100%;height:100%;'
			}));
            me.updateTitle(node);
            Ext.resumeLayouts(true);
            setTimeout(function(){me.redirectParams='';},1000)
        } else {
            thumbnailsStore = me.getThumbnailsStore();
            thumbnailsStore.removeAll();
            thumbnailsStore.add(node.childNodes);
            if (!thumbnails.ownerCt) {
                appContainer.removeAll(true);
            }
            appContainer.body.removeCls('app-example');
            appContainer.add(thumbnails);
            me.updateTitle(node);
            Ext.resumeLayouts(true);
        }
    },
    updateTitle: function(node) {
        var text = node.get('text'),
            title = node.isLeaf() ? (node.parentNode.get('text') + ' - ' + text) : text;
        this.getAppContainer().setTitle(title);
    },
    onHomeButtonClick:function(){
    	//this.getNavigationBreadcrumb().setSelection(null);
    	this.redirectTo('home');
    },
    onBreadcrumbNavSelectionChange:function(breadcrumb, node){
    	if (node) {
            this.redirectTo(node.getId());
        }
    },
    onThumbnailClick:function(view, node){
    	this.redirectTo(node.getId());
    },
    onAllThumbnailClick:function(view, node){
    	this.redirectTo(node.getId());
    	view.findParentByType('window').hide();
    },
    onThumbnailMouseEnter:function(view, record, item, index, e, eOpts){
    	Ext.get(item).addCls('thumbnail-over');
    },
    onThumbnailMouseLeave:function(view, record, item, index, e, eOpts){
    	Ext.get(item).removeCls('thumbnail-over');
    },
    onNavThumbnailClick:function(view, record,item, index, e){
    	var el = Ext.get(item);
    	this.redirectTo(el.getAttribute('nodeId'));
    },
    onNavThumbnailMouseEnter:function(view, record, item, index, e, eOpts){
    	//var el = Ext.get(item);
    	//el.down('.thumbnail-icon-wrap').hide();
    	//el.down('.thumbnail-text').show();
    },
    onNavThumbnailMouseLeave:function(view, record, item, index, e, eOpts){
    	//var el = Ext.get(item);
    	//el.down('.thumbnail-icon-wrap').show();
    	//el.down('.thumbnail-text').hide();
    },
    setThemeBackground:function(view,record,item){
    	var realSrc = cfg.getImgPath()+record.get('src').replace('.png','_1920x1200.jpg');
    	if(record.get('value')==1){
    		util.cookie.set('cookie-theme',cfg.getImgPath()+record.get('src'));
    		Ext.get('background_cover').setStyle({
    			'background-image':'url('+cfg.getImgPath()+record.get('src')+')',
    			'background-repeat':'repeat',
    			'background-size': 'auto',
    			'-webkit-background-size': 'auto',
    			'-o-background-size': 'auto'
    		});
    	}
    	if(record.get('value')==2){
    		util.cookie.set('cookie-theme',realSrc);
    		Ext.get('background_cover').setStyle({
    			'background-image':'url('+realSrc+')'
    		});
    	}
    	if(record.get('value')==3){
    		var ri = this.getRandomImage();
    		this.setRandomImage(ri[0],ri[1]);
    	}
    },
    doRandomImage:function(menuitem){
    	var ri = this.getRandomImage();
    	menuitem.el.down('img').dom.src = ri[2];
    	this.setRandomImage(ri[0],ri[1]);
    },
    getRandomImage:function(){
		var no = Math.ceil(Math.random()*150),seq=Math.ceil(Math.random()*8);
		return [no,seq,'http://'+seq+'.su.bdimg.com/skin_plus/'+no+'.jpg'];
		
	},
	setRandomImage:function(no,seq){
		var	themebi = 'http://'+seq+'.su.bdimg.com/skin/'+no+'.jpg';
		util.cookie.set('cookie-theme',themebi);
		/*Ext.getBody().setStyle({
			'background':'url('+themebi+')',
			'background-position': 'center 0',
			'background-repeat': 'no-repeat',
			'background-attachment': 'fixed;',
			'background-size': 'cover',
			'-webkit-background-size': 'cover',
			'-o-background-size': 'cover'
		});*/
		Ext.get('background_cover').setStyle({
			'background-image':'url('+themebi+')',
			'background-position': 'center 0',
			'background-repeat': 'no-repeat',
			'background-attachment': 'fixed',
			'background-size': 'cover',
			'-webkit-background-size': 'cover',
			'-o-background-size': 'cover',
        	'-webkit-filter': 'blur(0px)',
           	'-moz-filter': 'blur(0px)',
            '-ms-filter': 'blur(0px)',    
        	'filter': 'progid:DXImageTransform.Microsoft.Blur(PixelRadius=0, MakeShadow=false)'
		});
	},
	selectedColor:function(picker, selColor){
		util.cookie.set('cookie-theme',selColor);
		/*Ext.getBody().setStyle({
			'background':'#'+selColor
		});*/
		Ext.get('background_cover').setStyle({
			'background':'#'+selColor
		});
	},
	onBtnPwdClick:function(btn){
		var glyph = btn.glyph?util.glyphToFont(btn.glyph):'';
		var win = Ext.create('Ext.window.Window',{
   			title: util.windowTitle(glyph,'登录密码',''),
   			width:300,
   			height:240,
   			draggable:false,
			resizable:false,
			modal:true,
   			bodyStyle:'background:#fff;font-size:16px;color:#bbb;padding-top:20px;',
   			layout:'fit',
   			items:[{
   				 fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 90
			    },
   				xtype:'form',
   				defaults:{anchor:'85%'},
   				items: [{
   					xtype:'hidden',
   					name:'pm[ID]',
   					value:cfg.getUser().id
   				},{minLength:6,
   					itemId:'userPwd',
			        xtype: 'textfield',
			        name: 'pm[USER_PWD]',
			        inputType: 'password',
			        allowBlank:false,
			        fieldLabel: '新密码'
			    },{minLength:6,
			        xtype: 'textfield',
			        itemId:'userPwdAgin',
			        name: 'pm[userPwdAgin]',
			        inputType: 'password',
			        allowBlank:false,
			        fieldLabel: '再次确认',
			        validator: function(value) {
			            var password1 = this.previousSibling('[inputType=password]');
			            return (value === password1.getValue()) ? true : '两次密码不一致';
			        }
			    }],
			    buttonAlign:'right',
	   			buttons:[{
	   				text:'保存',
	   				disabled: true,
				    formBind: true,
	   				handler:function(){
	   					var f = win.down('form'),bf = f.getForm(),
	   						pw1 = f.down('textfield[itemId=userPwd]').getValue(),
	   						pw2 = f.down('textfield[itemId=userPwdAgin]').getValue();
	   					if(bf.isValid()){
	   						if(pw1==pw2){
	   							bf.submit({
	   								submitEmptyText:false ,
	   								url:cfg.getCtx()+'/b2b/user/updatePWD',
					                success: function(form, action) {
					                   util.success('修改密码成功');
					                   win.close();
					                },
					                failure: function(form, action) {
					                    util.error('修改密码失败');
					                    win.close();
					                }
	   							});
	   						}else{
	   							util.error('两次密码不一致');
	   						}
	   					}
	   				}
	   			}]
   			}]
   		}).show();
	},onBtnPayPwdClick:function(btn){
		var glyph = btn.glyph?util.glyphToFont(btn.glyph):'';
		var isSetPayPassword = cfg.getUser().isSetPayPassword;
		if(isSetPayPassword==0){
			util.alert('未授权，请联系公司管理员或重新登陆');
			return;
		}
		var win = Ext.create('Ext.window.Window',{
   			title: util.windowTitle(glyph,'修改支付密码',''),
   			width:360,
   			height:205,
   			draggable:false,
			resizable:false,
			modal:true,
   			bodyStyle:'background:#fff;font-size:16px;color:#bbb;',
   			layout:'fit',
   			items:[{
   				 fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 65
			    },
   				xtype:'form',
   				bodyStyle:'padding:10px 0px 10px 40px;',
   				items: [{
   					xtype:'hidden',
   					name:'pm[ID]',
   					value:cfg.getUser().id
   				},{
			        xtype: 'textfield',
			        minLength:6,
			        name: 'pm[OLD_PWD]',
			        inputType: 'password',
			        allowBlank:false,
			        fieldLabel: '原密码'
			    },{
   					itemId:'payPwd',
   					minLength:6,
			        xtype: 'textfield',
			        name: 'pm[PAY_PWD]',
			        inputType: 'password',
			        allowBlank:false,
			        fieldLabel: '新密码'
			    },{
			        xtype: 'textfield',
			        minLength:6,
			        itemId:'payPwdAgin',
			        name: 'pm[payPwdAgin]',
			        inputType: 'password',
			        allowBlank:false,
			        fieldLabel: '再次确认',
			        validator: function(value) {
			            var password1 = this.previousSibling('[inputType=password]');
			            return (value === password1.getValue()) ? true : '两次密码不一致';
			        }
			    }],
			    buttonAlign:'right',
	   			buttons:[{
	   				text:'保存',
	   				disabled: true,
				    formBind: true,
	   				handler:function(b){
	   					var f = win.down('form'),bf = f.getForm(),
	   						pw1 = f.down('textfield[itemId=payPwd]').getValue(),
	   						pw2 = f.down('textfield[itemId=payPwdAgin]').getValue();
	   					if(bf.isValid()){
	   						if(pw1==pw2){
	   							b.disable();
	   							bf.submit({
	   								submitEmptyText:false ,
	   								url:cfg.getCtx()+'/site/company/update/pay/pwd',
					                success: function(form, action) {
					                   util.success('修改支付密码成功');
					                   win.close();
					                },
					                failure: function(form, action) {
					                	b.enable();
					                	var state = action.result?action.result.statusCode:0,
					                		errors = ['修改支付密码失败','原密码输入错误','未授权，请联系公司管理员或重新登陆'];
					                    util.error(errors[0-parseInt(state)]);
					                }
	   							});
	   						}else{
	   							util.error('两次密码不一致');
	   						}
	   					}
	   				}
	   			},{	
	   				cls:'disable',
	   				text:'取消',
	   				handler:function(){
	   					win.close();
	   				}
	   			}]
   			}]
   		}).show();
	},onBtnSettingClick:function(btn){
		var glyph = btn.glyph?util.glyphToFont(btn.glyph):'';
		var win = Ext.create('Ext.window.Window',{
   			title: util.windowTitle(glyph,'绑定手机',''),
   			width:360,
   			height:300,
   			draggable:false,
			resizable:false,
			modal:true,
   			bodyStyle:'background:#fff;font-size:16px;color:#bbb;',
   			layout: {
		        type: 'vbox',
		        pack: 'start',
		        align: 'stretch'
		    },
   			items:[{
				xtype:'container',
				html:'<span class="yellow-color"><i class="iconfont f20">&#xe6ae;</i> 绑定手机号码，使用手机登录，提高帐号安全</span>',
				cls:'low'//good
			},{
				flex:1,
				fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 85
			    },
				xtype:'form',
				bodyStyle:'padding:10px 0;',
   				items: [{
			        xtype: 'cardno',
			        name: 'mobile',
			        offset :[-5,5],
			        allowBlank:false,
			        fieldLabel: '绑定手机',
			        enableKeyEvents:true,
			        regex: /^(1[3|5|8|7|4])[\d]{9}$/i,
					regexText: '请输入正确的手机号码',
			        listeners:{
			        	keyup:function(f){
			        		var btn = f.nextSibling().down('button');
			        		if(f.isValid()&&btn.getText().indexOf('重发')==-1){
			        			btn.enable();
			        		}else{
			        			btn.disable();
			        		}
			        	}
			        }
			    },{
			        xtype: 'fieldcontainer',
			        fieldLabel: '验证码',
			        layout: 'hbox',
			        items:[{
			        	width:80,
			        	name:'code',
			        	allowBlank:false,
			        	xtype:'textfield'
			        },{
			        	style:'margin-left:10px;',
			        	text:'获取验证码',
			        	ui:'default-toolbar',
			        	disabled:true,
			        	xtype:'button',
			        	handler:function(b){
			        		var mf = b.up('fieldcontainer').previousSibling();
			        		b.setText('60秒后重发');
			        		b.disable();
			        		var i=60,inter = setInterval(function(){
			        			if(i==0){
			        				b.setText('获取验证码');
			        				b.enable();
			        				clearInterval(inter);
			        				inter = null;
			        			}else{
				        			i--;
				        			b.setText(i+'秒后重发');
			        			}
			        		},1000);
			        		Ext.Ajax.request({
								url:cfg.getCtx()+'/sendCode',
								params:{
									'mobile':mf.getValue(),
									'type':2,
								},
								success:function(response, opts){
									var obj = Ext.decode(response.responseText);
									if(obj.success){
										
									}else{
										var state = obj.statusCode,
				                		errors = ['发送验证码异常', '短信发送失败', '手机号码不存在', '手机号码已存在', '短信验证码发送超过5次或内容重复，请于一小时后重新发送'];
				                    	util.error(errors[0-parseInt(state)]);
									}
								},
								failure: function(resp,opts) { 
									util.error('获取验证码失败！');
								}
							});
			        	}
			        }]
			    }],
			    buttons:[{
			    	text:'保存',
			    	handler:function(b){
			    		var f = b.up('form'),bf = f.getForm();
			    		if(bf.isValid()){
			    			bf.submit({
			    				submitEmptyText:false ,
   								url:cfg.getCtx()+'/b2b/user/regUserMobile ',
				                success: function(form, action) {
				                   util.success('绑定手机号码成功，你现在可以使用手机号码登录了');
				                   win.close();
				                },
				                failure: function(form, action) {
				                    util.error('绑定手机号码失败');
				                    win.close();
				                }
   							});
			    		}
			    	}
			    }]
   			}]
   		}).show();
	},onBtnLogoutClick:function(){
		var ck = Ext.util.Cookies.get("SECURITY_USER_KEY");
		if(ck){
			Ext.util.Cookies.clear('SECURITY_USER_KEY');
		}
		this.longPollOut=true;
		document.location.href = cfg.getCtx()+'/logout';
	},
	editUserInfo:function(btn){
		var win = util.createEmptyWindow('个人中心','',500,485,[{
			xtype:'hidden',
			name:'pm[ID]'
		},{
			xtype:'fieldcontainer',
			height:32,
			layout: 'hbox',
			items:[{
				xtype: 'textfield',
	        	fieldLabel:'姓名',
	        	allowBlank: false,
	        	width:180,
	        	name:'pm[CHINA_NAME]'
			},{
				xtype:'radiogroup',
	        	width:100,
	        	defaults:{
	        		margin:'0 0 0 5'
	        	},
	        	items: [
	                {boxLabel: '男', name: 'pm[SEX]', inputValue: 1, checked: true},
	                {boxLabel: '女', name: 'pm[SEX]', inputValue: 2}
	            ]
			}]
		},{
        	fieldLabel:'生日',
        	width:180,
        	xtype: 'datefield',
        	format:'Y-m-d',
            name:'pm[BIRTHDAY]',
            maxValue: new Date()
        },{
        	xtype: 'textfield',
        	fieldLabel:'手机',
        	readOnly:true,
        	name:'pm[MOBILE]'
        },{
        	xtype: 'textfield',
        	fieldLabel:'传真',
        	name:'pm[FAX]'
        },{
        	name:'pm[EMAIL]',
        	width:300,
        	vtype: 'email',
        	xtype: 'textfield',
        	fieldLabel:'Email'
        },{
			xtype: 'textfield',
			width:300,
    		fieldLabel:'身份证号',
    		name:'pm[ID_CARD]'
        },{
        	xtype:'fieldcontainer',
			layout: 'hbox',
			height:32,
			anchor:'100%',
			items:[{
				xtype: 'textfield',
	    		fieldLabel:'QQ1/2',
	    		width:250,
	        	name:'pm[QQ1]'
			},{xtype: 'splitter'},{
				xtype: 'textfield',
				flex:1,
	    		name:'pm[QQ2]'
			}]
        },{
        	anchor:'100%',
        	fieldLabel:'个性签名',
        	xtype:'textarea',
        	name:'pm[SIGNATURE]'
        }],[{
			text:'确定',
			formBind:true,
			disabled:true,
			handler:function(){
				var form = win.down('form'),
					f = form.getForm();
				var usersignature = form.down('textarea').getValue();
				if(f.isValid()){
					f.submit({
						submitEmptyText:false ,
						url:cfg.getCtx()+'/b2b/user/set/info',
		                success: function(ff, action) {
		                	win.close();
		                	
		                	if(usersignature!=''){
		                		Ext.get('usersignature').update(Ext.String.ellipsis(usersignature,80));
		                	}
		                	util.success('个人信息保存成功');
		                },
		                failure: function(form, action) {
		                	var state = action.result?action.result.statusCode:0,
		                		errors = ['个人信息保存异常'];
		                    util.error(errors[0-parseInt(state)]);
		                }
					});
				}
			}
		}]).show();
		win.mask('加载中...');
		Ext.Ajax.request({
			url:cfg.getCtx()+'/b2b/user/info?id='+cfg.getUser().id,
			success:function(response){
				win.unmask();
				var form = win.down('form');
				var data = Ext.decode(response.responseText).data;
				if(data.length>0){
					var d = data[0];
					var formData = util.pmModel({data:d});
					form.getForm().setValues(formData);
				}
				
			}
		});
	},
	longPollOut:false,
	longPoll:function(){
		var me = this;
		Ext.Ajax.request({
			timeout:60000,
			url:cfg.getCtx()+'/userKicked',
			params:{
				'userId':cfg.getUser().id,
				'sessionId':cfg.getUser().sessionId
			},
			success:function(response, opts){
				var obj = Ext.decode(response.responseText);
				
				if(!me.longPollOut&&obj.message=="no"){
					Ext.create('Ext.window.Window',{
			   			title: util.windowTitle('','信息提示',''),
			   			width:360,
			   			height:250,
			   			draggable:false,
						resizable:false,
						closable:false,
						modal:true,
			   			bodyStyle:'background:#fff;padding:10px;',
			   			layout:'fit',
			   			items:[{
			   				html:'<h3 class="alert-box jd"> 你的帐号已被强制退出！</h3><div class="alert-box-info">你的帐号于'+(util.format.date(new Date(),'Y-m-d H:i:s'))+'在别处登录。如非本人操作，则密码可能已泄露，建议你完善帐号安全设置以确保数据安全。</div>'
			   			}],
			   			buttons:[{
			   				text:'重新登录',
			   				handler:function(){
			   					document.location.href=cfg.getCtx()+'/logout';
			   				}
			   			}]
			   		}).show();
				}else{
					setTimeout(function(){
						me.longPoll();
					},60000);
				}
			},
			failure: function(resp,opts) {
				/*setTimeout(function(){
					me.longPoll();
				},60000);*/
				
				Ext.create('Ext.window.Window',{
		   			title: util.windowTitle('','信息提示',''),
		   			width:360,
		   			height:220,
		   			draggable:false,
					resizable:false,
					closable:false,
					modal:true,
		   			bodyStyle:'background:#fff;padding:10px;',
		   			layout:'fit',
		   			items:[{
		   				html:'<h3 class="alert-box jd"> 你的帐号已被强制退出！</h3><div class="alert-box-info">你的帐号于'+(util.format.date(new Date(),'Y-m-d H:i:s'))+'在别处登录。如非本人操作，则密码可能已泄露，建议你完善帐号安全设置以确保数据安全。</div>'
		   			}],
		   			buttons:[{
		   				text:'重新登录',
		   				handler:function(){
		   					document.location.href=cfg.getCtx()+'/logout';
		   				}
		   			}]
		   		}).show();
				
			}
		});
	},
	init:function(){
		this.longPoll();
	}
});