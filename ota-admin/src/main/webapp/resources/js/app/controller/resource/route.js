Ext.define('app.controller.resource.route', {
	extend: 'app.controller.common.BaseController',
	views:[
		'app.view.resource.route.pub.baseInfo',
		'app.view.resource.route.pub.day',
		'app.view.resource.route.pub.other',
		'app.view.resource.route.pub.detail',
		'app.view.resource.route.pub.price',
		'app.view.resource.route.pub.traffic','Ext.ux.form.DatePickerEvent','common.PriceGrid','app.view.resource.trafficPrice',
		'app.view.resource.contactWin',
		'Ext.ux.upload.BatchUpload'
	],
	config: {
		control: {
			'toolbar button[itemId=addRoute]': {
	             click: 'addRoute'
	        },'toolbar button[itemId=editRoute]': {
	             click: 'editRoute'
	        },'toolbar button[itemId=priceRoute]': {
	             click: 'priceRoute'
	        },'toolbar button[itemId=roomRoute]': {
	             click: 'roomRoute'
	        },'toolbar button[itemId=photoRoute]': {
	             click: 'photoRoute'
	        },'toolbar button[itemId=stopRoute]': {
	             click: 'stopRoute'
	        },'toolbar button[itemId=trafficRoute]': {
	             click: 'trafficRoute'
	        },'toolbar button[itemId=saleBaseRoute]': {
	             click: 'saleBaseRoute'
	        },'toolbar button[itemId=pubRoute]': {
	             click: 'pubRoute'
	        },'toolbar button[itemId=uploadRoute]': {
	             click: 'uploadRoute'
	        },'toolbar button[itemId=delRoute]': {
	             click: 'delRoute'
	        },'toolbar button[itemId=discountRoute]': {
	             click: 'discountRoute'
	        },'toolbar button[itemId=fullPrice]': {
	             click: 'fullPrice'
	        },'toolbar button[itemId=contactRoute]': {
	             click: 'contactRoute'
	        },'toolbar button[itemId=earnestRoute]': {
	             click: 'earnestRoute'
	        }/*,'toolbar button[itemId=assignRoute]': {
	             click: 'assignRoute'
	        }指定销售走发布里*/,'basegrid':{
	        	selectionchange: function(view, records) {
	        		var singleBtns = [
	        			this.getPhotoBtn(),
	        			this.getTrafficBtn(),
	        			this.getEditBtn(),
	        			this.getPriceBtn(),
	        			this.getUploadBtn(),
	        			this.getSaleBtn(),
	        			this.getPubBtn(),
	        			this.getStopBtn(),
	        			this.getDelBtn(),
	        			this.getFullPriceBtn()
	        		];
					this.btnToggle(singleBtns,records.length!=1);
					if(records.length==1){
						var r = records[0],isFullPrice = r.get('IS_FULL_PRICE')||'0';
						if(parseInt(isFullPrice)==1){
							this.btnToggle([this.getTrafficBtn(),this.getPriceBtn()],true);
						}else{
							this.btnToggle([this.getTrafficBtn(),this.getPriceBtn()],false);
							this.btnToggle([this.getFullPriceBtn()],true);
						}
						if(parseInt(r.get('BASE_PRICE_CNT'))==0){
							if(parseInt(isFullPrice)==0){
								this.btnToggle([this.getPubBtn()],true);
							}
							
						}else{
							if(parseInt(r.get('IS_SINGLE_PUB'))==0){
								//this.btnToggle([this.getPubBtn()],true);
							}
						}
						//配置交通先配地接价
						if(parseInt(r.get('BASE_PRICE_CNT'))==0){
							this.btnToggle([this.getSaleBtn()],true);
							this.btnToggle([this.getTrafficBtn()],true);
						}
						
						//配置交通后不能单卖地接
						//if(parseInt(r.get('ROUTE_TRAFFIC_CNT'))>0){
							//this.btnToggle([this.getSaleBtn()],true);
						//}
						//单卖地接不能配置交通
						if(parseInt(r.get('IS_SINGLE_PUB'))==1){
							if(parseInt(r.get('IS_PUB'))==1){
								this.btnToggle([this.getTrafficBtn(),this.getEditBtn(),this.getPriceBtn(),this.getDelBtn()],true);
							}
							this.getSaleBtn().setText('取消单卖');
						}else{
							if(this.getSaleBtn())
							this.getSaleBtn().setText('单卖地接');
						}
						if(parseInt(r.get('IS_PUB'))!=1){
							if(parseInt(r.get('IS_SINGLE_PUB'))!=1&&parseInt(r.get('ROUTE_TRAFFIC_CNT'))==0){
								this.btnToggle([this.getPubBtn()],true);
							}
							this.btnToggle([this.getStopBtn()],true);
						}else{
							this.btnToggle([this.getPubBtn(),this.getTrafficBtn(),this.getSaleBtn(),this.getEditBtn(),this.getPriceBtn(),this.getDelBtn()],true);
						}
						
						
					}
	            },
	            cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					if(cellIndex==2){
						if(e.target.tagName=='IMG')
	                	this.getPhotoBtn().fireEvent('click', this.getPhotoBtn());
					}
					if(cellIndex==3){
						if(e.target.tagName=='A'&&e.target.className.indexOf('title')!=-1)
	                	this.detailWindow(record.get('ID'));
					}
					if(cellIndex==2){
						if(e.target.tagName=='A')
	                	this.detailWindow(record.get('ID'));
					}
					if(cellIndex==6){
						if(e.target.tagName=='I'){
							if(e.target.className.indexOf('pp')!=-1){
								var isFullPrice = parseInt(record.get('IS_FULL_PRICE')||'0');
								this.routePrice(record.get('ID'),isFullPrice==1);
							}
							if(e.target.className.indexOf('dd')!=-1){
								var win = Ext.create('Ext.window.Window',{
									title:util.windowTitle('&#xe61d;','地接价',''),
									width:700,
									bodyPadding:5,
									height:220,
									modal:true,
									draggable:false,
									resizable:false,
						   			layout:'fit',
									items:[{
										routeId:record.get('ID'),
										xtype:'rprice'
									}],
									buttons:[{
										text:'关闭',
										cls:'disable',
										handler:function(){
											win.close();
										}
									}]
								}).show();
							}
						}
					}
				}
	        },
	        'treepanel[itemId=companytree]': {
	             checkchange:'onModuleTreeCheckChange'
	        }
		},
		refs: {
			routeGrid: 'basegrid',
			photoBtn:'toolbar button[itemId=photoRoute]',
			trafficBtn:'toolbar button[itemId=trafficRoute]',
			editBtn:'toolbar button[itemId=editRoute]',
			priceBtn:'toolbar button[itemId=priceRoute]',
			uploadBtn:'toolbar button[itemId=uploadRoute]',
			saleBtn:'toolbar button[itemId=saleBaseRoute]',
			pubBtn:'toolbar button[itemId=pubRoute]',
			stopBtn:'toolbar button[itemId=stopRoute]',
			delBtn:'toolbar button[itemId=delRoute]',
			fullPriceBtn:'toolbar button[itemId=fullPrice]'
			
        }
	},
	onBaseGridRender:function(g){
		var me = this;
		//外部跳转 动态传参数
		if(dynamicParamsTrafficStatus!=''){
			g.getStore().getProxy().setExtraParams({trafficStatus:dynamicParamsTrafficStatus});
	        g.getStore().load();
		}
		setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children;
		        	
		        for(var i=0;i<items.length;i++){
		        	delete items[i].checked;
		        	//如果当前用户没有部门id 隐藏设计线路修改线路
		        	if((items[i].itemId == 'addRoute'||items[i].itemId == 'editRoute')&&cfg.getUser().departId==''){
		        		Ext.applyIf(items[i],{hidden:true});
		        	}
	        		if(items[i].itemId == 'saleBaseRoute'&&cfg.getUser().saleRoute!='1'){
	        			Ext.applyIf(items[i],{hidden:true});
	        		}
		        }
		        items.push('->');
				items.push({
					emptyText:'产品标题/编号/目的地',
		        	xtype:'searchfield',
		        	store:g.getStore(),
		        	width:200
				});
		        util.createGridTbar(g,items);
		        var bbar = g.getDockedItems()[g.getDockedItems().length-1];
		        bbar.add('-');
		        bbar.add({
		        	xtype:'checkbox',
		        	boxLabel:'<span class="orange-color">显示过期线路</span>',
		        	inputValue:'1',
		        	cls:'bbarck',
		        	listeners:{
		        		change:function(c,nv,ov){
		        			nv = nv?'1':'0';
		        			g.getStore().getProxy().setExtraParams({passed:nv});
		        			g.getStore().load();
		        		}
		        	}
		        });
		    }
		});
		},500);
	},
	btnToggle :function(btns,v){
		for(var i=0;i<btns.length;i++){
			if(btns[i]){
				btns[i].setDisabled(v);
			}
		}
	},
	detailWindow :function(routeId){
		var win = Ext.create('Ext.window.Window',{
			title:util.windowTitle('&#xe635;','线路详情',''),
			modal:true,
			draggable:false,
			resizable:false,
			maximizable:false,
   			layout:'fit',
   			width:'85%',
			height:'85%',
   			items:[{
   				xtype:'routedetail'
   			}]
		});
		win.show();
		var rd = win.down('routedetail');
		rd.setRouteId(routeId);
	},
	routeWindow :function(btn){
		var me = this;
		return Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),'线路设计',''),
			width:1000,
			height:420,
			modal:true,
			draggable:false,
			resizable:false,
			maximizable:false,
   			layout: { type: 'card', deferredRender: true},
   			activeItem:0,
   			items: [{
	            id: 'card-0',
	            xtype:'routebaseinfo'
	        },
	        {
	            id: 'card-1',
	            xtype:'routeday'
	        },
	        {
	            id: 'card-2',
	            xtype:'routeother'
	        },
	        {
	            id: 'card-3',
	            xtype:'routedetail'
	        }],
	        listeners:{
   				beforeclose:function(){
   					me.getRouteGrid().getStore().load();
   				}
   			},
	        dockedItems: [{
			    xtype: 'toolbar',
			    dock: 'bottom',
			    ui: 'footer',
			    items: [{
			    	itemId:'stepContainer',
					xtype:'container',
					width:500,
					html:[
						'<ol class="ui-step ui-step-4">',
			   				'<li class="ui-step-start ui-step-active">',
						        '<div class="ui-step-line">-</div>',
						        '<div class="ui-step-icon">',
						            '<i class="iconfont">&#xe69f;</i>',
						            '<i class="ui-step-number">1</i>',
						            '<span class="ui-step-text">基本信息</span>',
						        '</div>',
						    '</li>',
						    '<li class="">',
						        '<div class="ui-step-line">-</div>',
						        '<div class="ui-step-icon">',
						            '<i class="iconfont">&#xe69f;</i>',
						            '<i class="ui-step-number">2</i>',
						            '<span class="ui-step-text">设计行程</span>',
						        '</div>',
						    '</li>',
						    '<li class="">',
						        '<div class="ui-step-line">-</div>',
						        '<div class="ui-step-icon">',
						            '<i class="iconfont">&#xe69f;</i>',
						            '<i class="ui-step-number">3</i>',
						            '<span class="ui-step-text">费用/须知</span>',
						        '</div>',
						    '</li>',
						    '<li class="ui-step-end">',
						        '<div class="ui-step-line">-</div>    ',
						        '<div class="ui-step-icon">',
						            '<i class="iconfont">&#xe69f;</i>',
						            '<i class="iconfont ui-step-number">&#xe6a0;</i>',
						            '<span class="ui-step-text">完成</span>',
						        '</div>',
						    '</li>',
			   			'</ol>'
					].join('')
		        },'->',{
		            itemId: 'card-prev',
		            text: '&laquo; 上一步',
		            handler: function(btn){me.showPrevious(btn)},
		            disabled: true
		        },
		        {
		            itemId: 'card-next',
		            text: '下一步 &raquo;',
		            handler: function(btn){me.showNext(btn)}
		        },
		        {
		            itemId: 'card-price',
		            text: '填写打包价',
		            cls:'red',
		            hidden:true,
		            handler: function(btn){me.showPriceWin(btn,true)}
		        }]
			}]
		});
	},
	showPrevious :function(btn){
		this.doCardNavigation(-1,btn);
	},
	showNext :function(btn){
		if(btn.text=='完成'){
			btn.up('window').close();
		}else{
			this.doCardNavigation(1,btn);
		}
	},
	stepChange :function(lis,next){
		for(var p=0;p<lis.length;p++){
        	if(next==p){
        		lis[p].addCls('ui-step-active');
        	}else{
        		lis[p].removeCls('ui-step-active');
        	}
        	if(next>p){
       			lis[p].addCls('ui-step-done');
       		}else{
       			lis[p].removeCls('ui-step-done');
       		}
        }
	},
	doCardNavigation: function (incr,btn) {
        var win = btn.up('window'),me = this;
        var l = win.getLayout(),
        	active = l.activeItem,
        	i = active.id.split('card-')[1],
        	next = parseInt(i, 10) + incr,
        	step = win.down('container[itemId=stepContainer]').el,
        	lis = step.query('li',false);
        //下一步
        if(incr>0){
	       	if(i==0){
	       		var form = active.getForm();
	       		var bc = active.down('[itemId=beginCityItems]').items,
	       			ec = active.down('[itemId=endCityItems]').items,
	       			pc = active.down('combo#fullPriceComboCmp');
	       		if(bc.length==0){
	       			util.alert('出发地未选择，可多选');
	       			return;
	       		}
	       		if(ec.length==0){
	       			util.alert('目的地为选择，可多选');
	       			return;
	       		}
	       		if(pc&&pc.getValue()=='1'){
	       			var a = active.down('numberfield#fullPriceDayCnt').getValue(),
	       				b =active.down('timefield#fullPriceDayTime').getValue()||'';
	       			if(!Ext.isNumber(a)){
	       				util.alert('请填写提前报名天数');
		       			return;
	       			}
	       			if(b==''){
	       				util.alert('请填写提前报名时间');
		       			return;
	       			}
	       		}
	       		if(form.isValid()){
	       			win.down('button#card-next').setDisabled(true);
	        		form.submit({
	        			submitEmptyText:false ,
		            	url:cfg.getCtx()+'/resource/route/save',
		                success: function(form, action) {
							var nextCard = l.setActiveItem(next);
							active.setRouteId(action.result.message);
							nextCard.setRouteId(action.result.message);
							me.stepChange(lis,next);
							win.down('button#card-next').setDisabled(false);
							win.down('button#card-prev').setDisabled(next===0);
							win.down('button#card-next').setDisabled(next===3);
							win.down('button#card-price').hide();
		                },
		                failure: function(form, action) {
		                	var state = action.result?action.result.statusCode:0,
		                		errors = ['保存异常','线路名称已存在','保存数据不完整','线路编号生成失败'];
		                    util.error(errors[0-parseInt(state)]);
		                    win.down('button#card-next').setDisabled(false);
		                    win.down('button#card-price').hide();
		                }
		            });
	            }
	       	}
	       	if(i==1){
	       		Ext.Ajax.request({
					url:cfg.getCtx()+'/resource/route/saveRouteInfo?routeId='+active.getRouteId(),
					success:function(response, opts){
						var action = Ext.decode(response.responseText);
						var nextCard = l.setActiveItem(next);
						nextCard.setRouteId(active.getRouteId());
						me.stepChange(lis,next);
						win.down('#card-next').setDisabled(false);
						win.down('#card-prev').setDisabled(next===0);
						win.down('#card-next').setDisabled(next===3);
						win.down('#card-price').hide();
					},
	                failure: function(form, action) {
	                	var state = action.result?action.result.statusCode:0,
	                		errors = ['保存异常'];
	                    util.error(errors[0-parseInt(state)]);
	                    win.down('button#card-next').setDisabled(false);
	                    win.down('#card-price').hide();
	                }
				});
	       		
	       	}
	       	if(i==2){
	       		var form = active.getForm();
	       		if(form.isValid()){
	       			win.down('#card-next').setDisabled(true);
	        		form.submit({
	        			submitEmptyText:false ,
		            	url:cfg.getCtx()+'/resource/route/saveRouteOther?routeId='+active.getRouteId(),
		                success: function(form, action) {
		                	var nextCard = l.setActiveItem(next);
							nextCard.setRouteId(active.getRouteId());
							me.stepChange(lis,next);
							win.down('#card-next').setText('完成');
							win.down('#card-next').setDisabled(false);
							
							var a = l.getActiveItem().getData();
							if(a){
								if(a.IS_FULL_PRICE=='1'){
									win.down('#card-price').show();
									win.down('#card-price').trafficId = a.TRAFFIC_ID; 
								}
							}
		                },
		                failure: function(form, action) {
		                	var state = action.result?action.result.statusCode:0,
		                		errors = ['保存异常'];
		                    util.error(errors[0-parseInt(state)]);
		                    win.down('button#card-next').setDisabled(false);
		                }
		            });
	            }
	       	}
       	}else{
       		//上一步
       		if(next!=3){
       			win.down('#card-next').setText('下一步&raquo;');
       		}
       		var prevCard = l.setActiveItem(next);
       		//prevCard.updateRouteId(active.getRouteId());
       		me.stepChange(lis,next);
			win.down('#card-prev').setDisabled(next===0);
			win.down('#card-price').hide();
       	}
    },
	addRoute :function(btn){
		this.routeWindow(btn).show().maximize();
	},
	editRoute :function(btn){
		var me=this,win,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			win = this.routeWindow(btn);
			win.show().maximize();
			win.getLayout().activeItem.setRouteId(sel.get('ID'));
		}
	},
	roomRoute:function(btn){
		var me=this,win,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),'单房差',''),
				width:450,
				bodyPadding:10,
				height:220,
				modal:true,
				draggable:false,
				resizable:false,
				defaults:{labelAlign:'right',width:200},
	   			items:[{
					fieldLabel:'外卖价',
					itemId:'retailSingleRoom',
					xtype:'numberfield',
					minValue:0,
					value:sel.get('RETAIL_SINGLE_ROOM')||0
				},{
					fieldLabel:'同行价',
					itemId:'interSingleRoom',
					xtype:'numberfield',
					minValue:0,
					value:sel.get('INTER_SINGLE_ROOM')||0
						
				}],
				listeners:{
	   				beforeclose:function(){
	   					me.getRouteGrid().getStore().load();
	   				}
	   			},
				buttons:[{
					text:'保存',
					handler:function(btn){
						var win = btn.up('window'),
							routeId = sel.get('ID');
						
						var retailSingleRoom = win.down('numberfield#retailSingleRoom').getValue(),
							interSingleRoom = win.down('numberfield#interSingleRoom').getValue();
						if(retailSingleRoom==null||interSingleRoom==null){
							util.alert('请填写价格');
							return;
						}
						
						//console.log(params);
						//return;
						Ext.Ajax.request({
							url:cfg.getCtx()+'/resource/route/update/single?routeId='+routeId,
							params:{interSingleRoom:interSingleRoom,retailSingleRoom:retailSingleRoom},
							success:function(response, opts){
								var action = Ext.decode(response.responseText);
								if(action.success){
									util.success('保存单房差成功');
									win.close();
								}else{
									var state = action.statusCode||0,
				                		errors = ['保存异常'];
				                    util.error(errors[0-parseInt(state)]);
								}
							}
						});
					}
				}]
			}).show();
		}
	},
	priceRoute :function(btn){
		var me=this,win,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),'地接价',''),
				width:700,
				bodyPadding:10,
				height:250,
				modal:true,
				draggable:false,
				resizable:false,
	   			layout:'fit',
				items:[{
					routeId:sel.get('ID'),
					xtype:'rprice'
				}],
				listeners:{
	   				beforeclose:function(){
	   					me.getRouteGrid().getStore().load();
	   				}
	   			},
				buttons:[{
					text:'保存',
					handler:function(btn){
						var win = btn.up('window'),
							gd = win.down('rprice'),
							params = gd.hiddenParams,
							routeId = gd.routeId;
						if(params.length==0){
							util.alert('请填写价格');
							return;
						}
						//console.log(params);
						//return;
						Ext.Ajax.request({
							url:cfg.getCtx()+'/resource/route/price/save?routeId='+routeId,
							params:{prices:Ext.encode(params)},
							success:function(response, opts){
								var action = Ext.decode(response.responseText);
								if(action.success){
									util.success('保存价格成功');
									gd.hiddenParams = [];
									//gd.getStore().load();
									win.close();
								}else{
									var state = action.statusCode||0,
				                		errors = ['保存异常','数据填写不完整'];
				                    util.error(errors[0-parseInt(state)]);
				                    gd.hiddenParams = [];
									gd.getStore().load();
								}
							}
						});
					}
				}]
			}).show();
			win.down('rprice').hiddenParams = [];
		}
	},
	photoRoute :function(btn){
		var me=this,win,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),'线路图片',''),
				bodyPadding:'5 10',
				dockedItems: [{
	                dock: 'top',
	                enableOverflow: true,
	                xtype: 'toolbar',
	                margin:'10 0 0 10',
	                style: {
	                    background: 'transparent',
	                    border: 'none',
	                    padding: '5px 0 5px 5px'
	                },
					items:[{
						id:'batchuploadCmp',
						text:'添加图片',
						xtype:'batchupload',
						faceUrl:cfg.getCtx()+'/resource/route/setFace?routeId='+sel.get('ID'),
						removeUrl:cfg.getCtx()+'/resource/route/delAtts',
						visitUrl:cfg.getCtx()+'/resource/route/atts?routeId='+sel.get('ID'),
						url: cfg.getCtx()+'/resource/route/uploadAtts?routeId='+sel.get('ID'),
						multipart_params:{}
					}]
				},{
					dock:'bottom',
					xtype:'statusbar',
					//text: 'Ready',
					style:'padding: 10px 0px;border-top: 1px solid #e5e5e5;background-color: #f5f5f5;',
					items:['图片最佳分辨率为640*360，图片最大1M']
				}],
				modal:true,
				draggable:false,
				resizable:false,
				maximizable:false,
				maximized :false,
				width:'85%',
				height:'85%',
	   			layout:'fit',
	   			listeners:{
	   				beforeclose:function(){
	   					grid.getStore().load();
	   				}
	   			}
			}).show();
			win.routeId = sel.get('ID');
		}
	},
	uploadRoute :function(btn){
		var me=this,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			util.uploadSingleAttachment('上传源行程',function(upload,value) {
				var v = value,
					f = upload.up('form'),
					form = f.getForm(),
					win = f.up('window');
				var fix = /\.xls|.doc|.docx|.trf/i;
	               if (!fix.test(v)) {
	               	util.alert('文件格式不正确，支持.xls|.doc');
	               	return;
	               }else{
	               	win.body.mask('行程上传中...');
	               	if(form.isValid()){
	                	form.submit({
	                		submitEmptyText:false ,
							url:cfg.getCtx()+'/resource/route/upload/source?routeId='+sel.get('ID'),
							method:'POST',
							success:function(res,req){
								var msg = req.result.message||'';
								if(msg!=''){
									util.alert('行程上传异常！参照内容：'+msg);
								}else{
									util.success('行程上传成功！');
								}
								win.body.unmask();
								win.close();
								grid.getStore().load();
							},
							failure:function(){
								util.error('行程上传异常！');
								win.body.unmask();
								win.close();
							}
						});
					}
	             }
			});
		}
	},
	stopRoute :function(btn){
		util.switchGridModel(this.getRouteGrid(),'/resource/route/pub?isPub=2');
	},
	trafficRoute :function(btn){
		var me=this,win,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),'交通线路',''),
				bodyPadding:5,
				modal:true,
				draggable:false,
				resizable:false,
				maximizable:false,
	   			layout:'fit',
	   			listeners:{
	   				beforeclose:function(){
	   					me.getRouteGrid().getStore().load();
	   				}
	   			},
	   			items:[{
	   				xtype:'rtraffic'
	   			}]
			});
		
			win.routeId = sel.get('ID');
			win.show().maximize();
		}
	
	},
	saleBaseRoute :function(btn){
		if(btn.text=='取消单卖'){
			util.switchGridModel(this.getRouteGrid(),'/resource/route/single/pub');
			return;
		}
		var me=this,win,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			win = util.createEmptyWindow('单卖地接',util.glyphToFont(btn.glyph),500,240,[{
				xtype:'textarea',
				name:'SINGLE_REMARK',
				anchor:'100% 100%',
				maxLength:200,
				emptyText:'请填写单卖地接的注意事项',
				allowBlank:false,
				value:sel.get('SINGLE_REMARK'),
				fieldLabel:'备注'
			}],[{
   				text:'保存',
   				handler:function(bt){
   					var form = win.down('form'),
						f = form.getForm();
					if(f.isValid()){
						var models = [sel.data]
						f.submit({
							params:{models:Ext.encode(models)},
							submitEmptyText:false ,
							url:cfg.getCtx()+'/resource/route/single/pub',
			                success: function(form, action) {
			                	grid.getStore().load();
			                	win.close();
			                },
			                failure: function(form, action) {
			                	var state = action.result?action.result.statusCode:0,
			                		errors = ['设置单卖地接异常'];
			                    util.error(errors[0-parseInt(state)]);
			                }
						});
					}
   				}
   			},{
   				text:'取消',
   				cls:'disable',
   				handler:function(){win.close();}
   			}]).show();
		}
	},
	pubRoute :function(btn){
		//是否指定销售
		//是否共享销售
		var me=this,win,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			var rq = sel.get('RQ') ||'';
			if(sel.get('IS_FULL_PRICE')=='1'&&rq==''){
				util.alert('请填写打包价');
				return;
			}
			
			
			
			var treeStore = Ext.create('Ext.data.TreeStore',{
	    		autoLoad:true,
	            model: Ext.create('Ext.data.TreeModel',{
	            	fields: ['id','parentId','text','leaf','checked','type','expanded',{
	            		name: 'glyph',
	                    type: 'string',
	                    convert: function(value){
	                    	console.log(value);
	                    	return 'xe608@iconfont';
	                    }
	            	}]
	            }),
	            proxy: {
	                type: 'ajax',
	                url: cfg.getCtx()+'/site/company/list/sale?routeId='+sel.get('ID'),
	                //传参  
	                extraParams : {  
	                    tid : '' , 
	                    isShare : sel.get('IS_SHARE')=='1'?'1':'0'
	                }  
	            },
	            folderSort: false
	        });
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),'发布线路',''),
				width:'85%',
				height:'85%',
				bodyPadding:5,
				modal:true,
				draggable:false,
				resizable:false,
				maximizable:false,
	   			layout:'fit',
	   			listeners:{
	   				beforeclose:function(){
	   					me.getRouteGrid().getStore().load();
	   				}
	   			},
	   			items:[{
			    	border:true,
			    	region:'center',
			    	itemId:'companytree',
			    	xtype:'treepanel',
			    	reserveScrollbar: true,
				    useArrows: true,
				    rootVisible: false,
				    root: {
				        text: "全选/全不选",
				        expanded: true,
				        checked:false
				    },
				    multiSelect: true,
				    singleExpand: false,
				    loadMask: true,
				    animate: false,
			    	store:treeStore,
			    	dockedItems:[{
				    	itemId:'treetool',
			    		xtype:'toolbar',
			        	items:[{
				    		xtype:'container',
							html:'<span class="yellow-color"><i class="iconfont f20">&#xe6ae;</i> 发布线路可指定旅行社销售，指定旅行社后点击发布</span>',
							cls:'low'
			        	},'->',{
			        		xtype:'checkbox',
			        		hidden:cfg.getUser().shareRoute!='1',
			        		checked: sel.get('IS_SHARE')=='1',
			        		boxLabel:'共享给同行',
			        		inputValue:'1',
			        		listeners:{
			        			change :function(ck, newValue, oldValue, eOpts){
			        				var s = ck.up('treepanel').getStore();
			        				
			        				if(!newValue){
			        					s.getProxy().setExtraParams({isShare:0});
			        				}else{
			        					s.getProxy().setExtraParams({isShare:1});
			        				}
			        				s.load();
			        			}
			        		}
			        	}]
			    	}],
			        columns:[{
						xtype: 'treecolumn',
				        text: '指定销售',
				        padding:'1 0 1 0',
				        flex:1,
				        sortable:false,
				        dataIndex: 'text'
				    },{
				    	text: '类型',
				        padding:'1 0 1 0',
				        width:100,
				        sortable:false,
				        dataIndex: 'type',
				        renderer: function(value){
				        	if(value==''){
				        		return '';
				        	}
				        	if(value==0){
				        		return '平台管理';
				        	}
				        	if(value==1){
				        		return '供应商';
				        	}
				        	if(value==2){
				        		return '旅行社';
				        	}
				        	if(value==3){
				        		return '门市';
				        	}
				        	if(value==4){
				        		return '分公司';
				        	}
				        	if(value==5){
				        		return '同行';
				        	}
				        	if(value==6){
				        		return '商务中心';
				        	}
				        	if(value==7){
				        		return '子公司';
				        	}
				        }
				    }]
	   			}],
	   			buttons:[{
	   				text:'发布',
	   				handler:function(bt){
	   					//util.switchGridModel(this.getRouteGrid(),'/resource/route/pub?isPub=1');
	   					bt.disable();
	   					var tms = win.down('treepanel').getChecked(),models=[],ck = win.down('treepanel').down('toolbar').down('checkbox');
						for (var i = 0; i < tms.length; i++) { 
							models.push(tms[i].data);
						}
				       	Ext.Ajax.request({
							url:cfg.getCtx()+'/resource/route/pub?isPub=1',
							params:{models:Ext.encode(models),isShare:ck.getValue()?'1':'2',routeId:sel.get('ID')},
							success:function(response, opts){
								var action = Ext.decode(response.responseText);
								if(action.success){
									util.success('发布线路成功!');
									win.close();
								}else{
									bt.enable();
									var state = action.statusCode||0,
				                		errors = ['发布线路异常','请检查线路日程是否填写'];
				                    util.error(errors[0-parseInt(state)]);
				                    win.close();
								}
							}
						});
	   				}
	   			},{
	   				text:'取消',
	   				cls:'disable',
	   				handler:function(){win.close();}
	   			}]
			});
		
			win.routeId = sel.get('ID');
			win.show();
		}
	},
	onModuleTreeCheckChange:function(node, checked){
		this.childHasChecked(node,checked);
		var parentNode = node.parentNode;
		if(parentNode != null) {   
			this.parentCheck(parentNode,checked);   
		} 
	},
	parentCheck:function(node ,checked){
		var childNodes = node.childNodes,isChildCheck = false;
		for (var i = 0; i < childNodes.length; i++) {
			if (childNodes[i].get('checked')) {
				isChildCheck = true;
				break;
			}
		};
		node.set('checked',isChildCheck);
		var parentNode = node.parentNode;
		if (parentNode != null ) {
			this.parentCheck(parentNode,checked);
		}
	},
	childHasChecked:function(node, checked){
		node.cascadeBy(function (child) {
			child.set("checked",checked)
		});
	},
	delRoute :function(btn){
		util.delGridModel(this.getRouteGrid(),'/resource/route/del');
	},
	routePrice :function(routeId,isFullPrice){
		var me = this,win;
		Ext.require('Ext.ux.form.DatePickerEvent',function(){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(isFullPrice?'&#xe646;':'&#xe61f;',isFullPrice?'线路打包价':'线路综合报价',''),
				width:1000,
				height:377,
				modal:true,
				draggable:false,
				resizable:false,
	   			layout:'border',
	   			items:[{
	   				region:'center',
	   				cls:'eventdatepicker selector',
	   				xtype:'eventdatepicker',
					disabledDatesText:'无发团日期',
					format:'Ymd',
					showToday:false,
					listeners:{
						monthviewchange:function(a,b,c){
							var nextDay = Ext.Date.format(b,'Y-m')+'-01';
							me.loadDateForSeat(nextDay,routeId,win,isFullPrice);
						},
						select :function(p,date,e){
							me.loadPriceForDate(Ext.util.Format.date(date, 'Ymd'),routeId,win,isFullPrice);
						}
					}
	   			},{
	   				region:'east',
	   				width:650,
	   				itemId:'pricePlan',
	   				xtype:'tabpanel',
	   				margin:'0 0 0 1',
	   				activeTab: 0,
	   				plain:true,
	   				defaults: {
				        bodyPadding: 0
				    },
	   				items:[{
		                title: '方案名称',
		                bodyPadding:10,
		                html: '选择具体日期查看交通线路报价'
		            }]
	   			}]
	   		}).show();
	   		var firstDay = Ext.Date.format(Ext.Date.getFirstDateOfMonth(new Date()),'Y-m-d');
			me.loadDateForSeat(firstDay,routeId,win,isFullPrice);
		});
	},
	loadPriceForDate :function(date,routeId,win,isFullPrice){
		var pp = win.down('tabpanel[itemId=pricePlan]');
		Ext.Ajax.request({
		    url: cfg.getCtx()+'/resource/route/list/plan?routeId='+routeId+'&startDate='+date,
		    success: function(response, opts) {
		    	 var obj = Ext.decode(response.responseText),
		        	data = obj.data;
		        pp.removeAll(true);
		    	if(data.length>0){
					for(var i=0;i<data.length;i++){
						var title = data[i].PLAN_TITLE;
						if(isFullPrice){
							title = title.substring(0,title.lastIndexOf('-'));
						}
						pp.add({
							title:title,
							id:data[i].PLANID,
							items:[{
								xtype:'rprice',
								myLoadId:data[i].PLANID,
								myLoadUrl:cfg.getCtx()+'/resource/route/list/plan/price?routeId='+routeId+'&startDate='+date+'&planId='
							},{
								border:false,
						    	cls: 'data-view',
						        itemSelector: '.data-view-item',
						        xtype: 'dataview',
						        overItemCls: 'hover',  
						        trackOver:true,
						        store :Ext.create('Ext.data.Store', {
						        	model:Ext.create('Ext.data.Model',{
						        		fields: ['TRAFFIC_NAME','DATE','STAY_CNT','SEAT']
						        	}),
						        	autoLoad: true, 
						        	planId:data[i].PLANID,
						        	routeId:routeId,
						        	startDate:date,
						        	listeners:{
						        		beforeload:function(s){
						        			s.getProxy().setExtraParams({routeId:s.routeId,planId:s.planId,startDate:s.startDate});
						        		}
						        	},
								    proxy: {
								        type: 'ajax',
								        noCache:true,
								        url: cfg.getCtx()+'/resource/route/list/route/traffic/info',
								        reader: {
								            type: 'json',
								            rootProperty: 'data',
								            totalProperty:'totalSize'
								        }
								   	}
						        }),
						        tpl: [
						        	'<tpl for="."><div class="data-view-item block" style="margin:5px 30px 5px 0px;width:180px;padding:2px 0px 8px 8px;border:1px dashed #d1d1d1;">',
						        	'<h3 style="font-size:14px;padding:2px 0px;line-height:22px;color:#427fed">{TRAFFIC_NAME}</h3>',
						        	'<div class="ht20">',
						        	'出发日期：<span style="font-size:16px;">{[this.fd(values.DATE)]}</span>',
						        	'</div>',
						        	//'<div style="">',
						        	//'停留天数：<span style="font-size:16px;">{STAY_CNT}</span>',
						        	//'</div>',
						        	'<div class="ht20">',
						        	'剩余座位：<span style="font-size:16px;color:#4CAF50;">{SEAT}</span>',
						        	'</div>',
						        	//'<div class="step"><i class="iconfont">&#xe69e;</i></div>',
						        	'</div></tpl>',{
						        		fd :function(v){
											return Ext.Date.format(Ext.Date.parse(v, "Ymd"), "Y/m/d");
						        		}
						        	}
						        ]
							}]
						});
						
					}
				}else{
					pp.add({
						title:'没有查询结果',
						bodyPadding:10,
						html:'更换其他日期试试'
					});
				}
				pp.setActiveTab(0);
		    }
		});	
	},
	loadDateForSeat:function(date,routeId,win){	
		win.mask('数据加载中...');
		Ext.Ajax.request({
		    url: cfg.getCtx()+'/resource/route/calendar?routeId='+routeId+'&startDate='+date,
		    success: function(response, opts) {
		    	win.unmask();
		        var obj = Ext.decode(response.responseText),
		        	data = obj.data,
		        	count = parseInt(obj.totalSize),
		        	enableDates = [],
		        	enableRuleIds = {},
		        	enableEvents = {},
		        	re = '(?:^(?!';
		        if(count>0){
		        	if(data.length>0){
				        for(var i=0;i<data.length;i++){
				        	enableDates.push(data[i].RQ);
				        	enableRuleIds[data[i].RQ] = data[i].ID;
				        	var fmRQ = Ext.Date.parse(data[i].RQ, "Ymd");
				        	enableEvents[parseInt(Ext.Date.format(fmRQ,'d'))] = [
				        		(data[i].ACTUAL_INTER_PRICE||0),//同行价
				        		(data[i].ACTUAL_PRICE||0),//外卖价
				        		(data[i].ACTUAL_SEAT||'无')//座位
				        	].join('-');
				        }
				        re+=enableDates.join('|');
			        }else{
			        	re += '11';
			        }
			        re+='))';
			        win.down('eventdatepicker').setEventTexts(enableEvents);
			        win.down('eventdatepicker').setDisabledDates(new RegExp(re));
			        win.down('eventdatepicker').dataMap = enableRuleIds;
		        }else{
		        	//win.close();
		        	//util.alert('交通没有发布报价');
		        }
		    }
		});	
	},
	discountRoute:function(btn){
		var me=this,win,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			Ext.create('app.view.b2b.discountPlugin',{
				proId:sel.get('ID'),
				proType:'2',
				cityId:sel.get('CITY_ID'),
				discountId:sel.get('DISCOUNT_ID'),
				listeners:{
					beforeclose:function(win){
						grid.getStore().load();
					}
				}
			}).show();
		}
	},
	
	showPriceWin:function(btn,isFullPrice){
		var me = this,win = Ext.create('Ext.window.Window',{
			title:util.windowTitle(isFullPrice?'&#xe646;':'&#xe61f;',isFullPrice?'线路打包价':'线路综合报价',''),
			width:600,
			height:400,
			modal:true,
			draggable:false,
			resizable:false,
   			layout:'fit',
			items:[{
				trafficId:btn.trafficId,
				isFullPrice:1,
				trafficPub :'',
				trafficSale :'',
				xtype:'trcprice'
			}]
		}).show();
		win.down('trcprice').down('pricegrid').hiddenParams = [];
	},
	fullPrice:function(btn){
		var me=this,win,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),'填写打包价格',''),
				width:700,
				height:450,
				modal:true,
				draggable:false,
				resizable:false,
	   			layout:'fit',
	   			listeners:{
	   				close:function(){
	   					grid.getStore().load();
	   				}
	   			},
				items:[{
					trafficId:sel.get('ID'),
					fullPriceFromList:1,
					isFullPrice:1,
					trafficPub :'',
					trafficSale :'',
					xtype:'trcprice'
				}]
			}).show();
			win.down('trcprice').down('pricegrid').hiddenParams = [];
		}
	},
	contactRoute:function(){
		var me=this,win,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			Ext.create('app.view.resource.contactWin',{
				proId:sel.get('ID')
			}).show();
		}
	},
	earnestRoute:function(btn){
		var me=this,win,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			win = util.createEmptyWindow(btn.getText(),util.glyphToFont(btn.glyph),500,360,[{
				xtype:'hiddenfield',
				name:'pm[ID]',
				value:sel.get('ID')
			},{
				xtype: 'radiogroup',
	            fieldLabel: '设置',
	            columns: 5,
	            style:'padding-bottom:5px;margin-bottom:10px;border-bottom:2px solid #e1e1e1;',
	            items: [
	                {boxLabel: '启用', name: 'pm[IS_EARNEST]', inputValue: 1, checked: true},
	                {boxLabel: '禁用', name: 'pm[IS_EARNEST]', inputValue: 0}
	            ]
			},{
				xtype:'fieldcontainer',
				layout:'hbox',
				fieldLabel:'最低费用',
				items:[{
					width:170,
					fieldLabel:'同行价/人',
					allowBlank:false,
					labelWidth:65,
					xtype:'numberfield',
					name : 'pm[EARNEST_INTER]'
				},{
					xtype:'splitter'
				},{
					fieldLabel:'外卖价/人',
					labelWidth:65,
					width:170,
					allowBlank:false,
					name : 'pm[EARNEST_RETAIL]',
					xtype:'numberfield'
				}]
			},{
				xtype: 'radiogroup',
	            fieldLabel: '支付方式',
	            columns: 3,
	            items: [
	                {boxLabel: '余款提前支付天数', name: 'pm[EARNEST_TYPE]', inputValue: 0, checked: true},
	                {boxLabel: '代收款最晚确认天数', name: 'pm[EARNEST_TYPE]', inputValue: 1}
	            ]
			},{
				fieldLabel:'设置天数',
				allowBlank:false,
				name : 'pm[EARNEST_DAY_COUNT]',
				minValue:0,
				xtype:'numberfield',
				width:150
			}],[{
   				text:'保存',
   				handler:function(bt){
   					var form = win.down('form'),
						f = form.getForm();
					if(f.isValid()){
						f.submit({
							submitEmptyText:false ,
							url:cfg.getCtx()+'/resource/route/update/earnest',
			                success: function(form, action) {
			                	grid.getStore().load();
			                	win.close();
			                },
			                failure: function(form, action) {
			                	var state = action.result?action.result.statusCode:0,
			                		errors = ['设置定金异常'];
			                    util.error(errors[0-parseInt(state)]);
			                }
						});
					}
   				}
   			},{
   				text:'取消',
   				cls:'disable',
   				handler:function(){win.close();}
   			}]).show();
			var formData = util.pmModel(sel);
			console.log(formData);
			win.down('form').getForm().setValues(formData);
		}
	}
});