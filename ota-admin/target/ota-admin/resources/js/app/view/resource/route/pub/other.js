Ext.define('app.view.resource.route.pub.other', {
	extend: 'Ext.form.Panel',
	xtype:'routeother',
	fieldDefaults: {
        labelWidth: 60,
        labelAlign:'right'
    },
    autoScroll:true,
    defaults:{
    	height:120,
    	//border:true,
		xtype:'panel',
		layout:'card',
		flex:1,
		margin:'5 1 0 1',
		bodyPadding:5
    },
    config:{
    	routeId:''
    },
    initComponent: function() {
		this.callParent();
	},
	updateRouteId:function(routeId){
		var me = this;
		if(routeId){
			top.Ext.getBody().mask('加载其他信息...');
			this.add([{
				dockedItems:me.getDk('其他费用'),
				itemId:'otherFee'
			},{
				dockedItems:me.getDk('费用包含')
			},{
				dockedItems:me.getDk('费用不含')
			},{
				dockedItems:me.getDk('出行须知')
			},{
				dockedItems:me.getDk('温馨提示')
			}]);
		}
		return routeId;
	},
	getDk :function(text){
		var me = this;
		return {
    		dock: 'top',
    		style:'padding:1px 5px',
		    xtype: 'toolbar',
		    items:[{
	       		xtype:'button',
	       		ui:'default-toolbar',
	       		glyph:'xe631@iconfont',
	       		text: text,
	       		listeners:{
	       			click:function(btn){me.addItem.apply(me,[btn,null,false]);},
	       			afterrender:function(btn){
	       				var initParam ={'其他费用':'5','费用包含':'1','费用不含':'2','出行须知':'3','温馨提示':'4'};
	       				Ext.Ajax.request({
							url:cfg.getCtx()+'/resource/route/others?routeId='+me.getRouteId()+'&type='+initParam[text],
							success:function(response, opts){
								var action = Ext.decode(response.responseText);
								me.addItem(btn,action.data,true);
								if(text=='温馨提示'){
									setTimeout(function(){top.Ext.getBody().unmask();},500);
								}
							}
						});
	       			}
	       		}
	       		
	       	},{
	        	xtype:'container',
	        	layout:'hbox'
	        }]
    	};
	},
	addItem :function(btn,data,init){
		var title = btn.nextSibling('container'),
			content = title.up('toolbar').up('panel'),
			no = title.items.length;
		if(data&&data.length>0){
			for(var i=0;i<data.length;i++){
				no = title.items.length;
				title.add(this.itemTpl(btn.text,no,data[i]));
				content.add(this.contentTpl(btn.text,no,data[i]));
			}
		}else{
			if(init&&btn.getText()=='其他费用'){
				content.setHeight(35);
				return;
			}
			var newItem = title.add(this.itemTpl(btn.text,no));
			for(var i=0;i<title.items.length;i++){title.items.get(i).removeCls('select');}
			newItem.addCls('select');
			content.add(this.contentTpl(btn.text,no));
			content.getLayout().setActiveItem(no++);
			content.setHeight(120);
		}
		
	},
	removeItem :function(fc){
		var no = fc.no,
			titleContainer = fc.up('container'),
			contentContainer = titleContainer.up('toolbar').up('panel'),
			titleItem = titleContainer.items.get(no),
			contentItem = contentContainer.items.get(no);
		titleItem.hide();
		contentItem.hide();
		if(titleItem.down('combo')){
			titleItem.down('combo').disable();
			contentItem.disable();
			titleContainer.items.get(0).addCls('select');
			contentContainer.getLayout().setActiveItem(0);
		}
		if(titleItem.down('textfield')){
			titleItem.remove(titleItem.down('textfield'),true);
			titleContainer.remove(contentItem,true);
			if(titleContainer.query('textfield').length==0){
				contentContainer.setHeight(35);
			}
		}
	},
	removeAllItems :function(){
		var items = this.items;
		for(var i=0;i<items.length;i++){
			var contentContainer = items.get(i),
				tbr = contentContainer.down('toolbar'),
				titleContainer = tbr.down('container');
				titleContainer.removeAll(true);
				contentContainer.removeAll(true);
		}
		
	},
	itemTpl :function(txt,no,record){
		var me = this,
			record = record ||{},
			initParam ={'其他费用':'other','费用包含':'include','费用不含':'noclude','出行须知':'notice','温馨提示':'tips'},
			initData={
				'费用包含':[
					{text:'交通',value:'往返团队经济舱机票含税费（团队机票将统一出票，如遇政府或航空公司政策性调整燃油税费，在未出票的情况下将进行多退少补，敬请谅解。团队机票一经开出，不得更改、不得签转、不得退票）。'},
					{text:'小交通',value:'景区内用车;机场往返接送服务。'},
					{text:'住宿',value:'普通酒店标准2人间5晚海景标间，热水、空调、独立卫生间。 '},
					{text:'用餐',value:'行程中团队标准用餐，5早7正，正餐餐标30元/人/餐，10人1桌，10菜1汤，品类及数量根据人数变化有所调整（中式餐或自助餐或特色餐，自由活动期间用餐请自理；如因自身原因放弃用餐，则餐费不退）。'},
					{text:'门票',value:'行程中所含的景点首道大门票'},
					{text:'导服',value:'当地中文导游'},
					{text:'儿童标准价',value:'年龄2~12周岁（不含），不占床，含往返机票（含税）、半价正餐（不含早）、导服、旅游车车位，其他当地自理）。 '},
					{text:'其他',value:''}
				],
				'费用不含':[
					{text:'交通',value:'往返团队经济舱机票含税费（团队机票将统一出票，如遇政府或航空公司政策性调整燃油税费，在未出票的情况下将进行多退少补，敬请谅解。团队机票一经开出，不得更改、不得签转、不得退票）。'},
					{text:'小交通',value:'景区内用车;机场往返接送服务。'},
					{text:'住宿',value:'普通酒店标准2人间5晚海景标间，热水、空调、独立卫生间。 '},
					{text:'用餐',value:'行程中团队标准用餐，5早7正，正餐餐标30元/人/餐，10人1桌，10菜1汤，品类及数量根据人数变化有所调整（中式餐或自助餐或特色餐，自由活动期间用餐请自理；如因自身原因放弃用餐，则餐费不退）。'},
					{text:'门票',value:'行程中所含的景点首道大门票'},
					{text:'导服',value:'当地中文导游'},
					{text:'儿童标准价',value:'年龄2~12周岁（不含），不占床，含往返机票（含税）、半价正餐（不含早）、导服、旅游车车位，其他当地自理）。 '},
					{text:'其他',value:''}
				],
	            '出行须知':[
	            	{text:'交通',value:[
	            		'（1）合同一经签订且付全款，团队机票、列车票、船票即为出票，不得更改、签转、退票。\n',
	            		'（2）飞行时间、车程时间、船程时间以当日实际所用时间为准。\n',
	            		'（3）本产品如因淡季或收客人数较少，有可能与相近方向的发班线路拼车出游，届时请游客见谅。'
	            	].join('')},
	            	{text:'住宿',value:[
	            		'按2人入住1间房核算，如出现单男单女，尽量安排该客人与其他同性团友拼房，如未能拼房者，',
	            		'可选择与同行亲友共享双人房并加床（加床者按占半房收费，即3人1间）；如不愿意与同行亲友共享3人房或单人出行者，请补齐单房差以享用单人房间。'
	            	].join('')},
	            	{text:'团队用餐',value:'十人一桌，十菜一汤，人数不足十人时，在每人用餐标准不变的前提下调整餐食的分量。'},
	            	{text:'游览',value:[
	            		'（1）景点游览、自由活动、购物店停留的时间以当天实际游览为准。\n',
	            		'（2）行程中需自理门票和当地导游推荐项目，请自愿选择参加。\n',
	            		'（3）请您仔细阅读本行程，根据自身条件选择适合自己的旅游线路，出游过程中，如因身体健康等自身原因需放弃部分行程的，或游客要求放弃部分住宿、交通的，均视为自愿放弃，已发生费用不予退还，放弃行程期间的人身安全由旅游者自行负责。\n',
	            		'（4）团队游览中不允许擅自离团（自由活动除外），中途离团视同游客违约，按照合同总金额的20%赔付旅行社，由此造成未参加行程内景点、用餐、房、车等费用不退，旅行社亦不承担游客离团时发生意外的责任。\n',
	            		'（5）如遇台风、暴雪等不可抗因素导致无法按约定行程游览，行程变更后增加或减少的费用按旅行社团队操作实际发生的费用结算。\n',
	            		'（6）出游过程中，如产生退费情况，以退费项目旅行社折扣价为依据，均不以挂牌价为准。'
	            	].join('')},
	            	{text:'购物',value:'当地购物时请慎重考虑，把握好质量与价格，务必索要发票。'},
	            	{text:'差价说明',value:[
	            		'（1）如遇国家政策性调整门票、交通价格等，按调整后的实际价格结算。\n',
	            		'（2）赠送项目因航班、天气等不可抗因素导致不能赠送的，费用不退。\n',
	            		'（3）本线路价格为团队行程综合旅游报价，持有任何优惠证件的游客均不再享受景区门票的优惠政策。'
	            	].join('')},
	            	{text:'出团通知',value:'出团通知最晚于出团前1天发送，若能提前确定，我们将会第一时间通知您。'},
	            	{text:'意见反馈',value:'请配合导游如实填写当地的意见单，不填或虚填者归来后投诉将无法受理。'},
	            	{text:'其他',values:''}
	            ],
	            '温馨提示':[
	            	{text:'病患者/孕妇',value:[
	            		'1.为了确保旅游顺利出行，防止旅途中发生人身意外伤害事故，请旅游者在出行前做一次必要的身体检查， 如存在下列情况，因服务能力所限无法接待： \n',
	            		'（1）传染性疾病患者，如传染性肝炎、活动期肺结核、伤寒等传染病人； \n',
	            		'（2）心血管疾病患者，如严重高血压、心功能不全、心肌缺氧、心肌梗塞等病人； \n',
	            		'（3）脑血管疾病患者，如脑栓塞、脑出血、脑肿瘤等病人； \n',
	            		'（4）呼吸系统疾病患者，如肺气肿、肺心病等病人； \n',
	            		'（5）精神病患者，如癫痫及各种精神病人；  \n',
	            		'（6）严重贫血病患者，如血红蛋白量水平在50克/升以下的病人； \n ',
	            		'（7）大中型手术的恢复期病患者； \n ',
	            		'（8）孕妇及行动不便者。 '
	            	].join('')},
	            	{text:'老年人',value:[
	            		'1.70周岁以上老年人预订出游，须与我司签订《健康证明》并有家属或朋友（因服务能力所限无法接待及限制接待的人除外）陪同方可出游。  \n',
	            		'2. 因服务能力所限，无法接待80周岁以上的旅游者报名出游，敬请谅解。  \n',
	            		'3. 因服务能力所限，不受理65周岁（含65周岁）以上老年人预订云南跟团产品。 \n',
	            		'4. 因服务能力所限，不受理70周岁（含70周岁）以上老年人预订四川、西藏跟团产品。 '
	            	].join('')},
	            	{text:'未成年人',value:[
	            		'1.未满18周岁的旅游者请由家属（因服务能力所限无法接待及限制接待的人除外）陪同参团。\n',
	            		'2.因服务能力所限，无法接待18周岁以下旅游者单独报名出游，敬请谅解。 '
	            	].join('')},
	            	{text:'外籍友人',value:[
	            		'本产品网上报价适用持有大陆居民身份证的游客。如您持有其他国家或地区的护照，请在预订过程中注明。 '
	            	].join('')},
	            	{text:'其他',value:''}
	            ]
			};
			
			
		return {
        	xtype:'fieldcontainer',
        	layout:'hbox',
        	no:no,
        	margin:'0 2 0 0',
        	listeners:{
        		afterrender:function(fc){
        			var el = fc.el,
						content = fc.up('toolbar').up('panel');
        			fc.el.on('click',function(e, t, eOpts){
        				var m = Ext.get(this),
        					ms = m.parent().query('.time',false);
        					for(var i=0;i<ms.length;i++){ms[i].removeCls('select');}
        					m.addCls('select');
        					var l = content.getLayout();
			        		l.setActiveItem(fc.no);
        				if(t.tagName=='I'&&t.className.indexOf('del')!=-1){
		        			me.removeItem(fc);
		        		}
        			});
        			fc.el.hover(function(){
        				Ext.get(this).addCls('hover');
        			},function(){
        				Ext.get(this).removeCls('hover');
        			})
        		}
        	},
       		cls:'time'+(no==0?' select':''),
        	width:126,
        	items:[txt=='其他费用'?{
        		margin:'0 0 0 5',
        		width:100,
        		name:initParam[txt]+'['+no+'].TITLE',
        		value:record.TITLE,
        		xtype:'textfield'
        	}:{
        		margin:'0 0 0 5',
	       		xtype:'combo',
	       		width:100,
	       		editable:false,
	       		queryMode:'local',
	       		allowBlank:false,
	       		value:record.TITLE,
	       		valueField:'text',
	       		displayField:'text',
	       		name:initParam[txt]+'['+no+'].TITLE',
	       		hiddenName:initParam[txt]+'['+no+'].TITLE',
				listConfig:{maxHeight:150},
				emptyText:'请选择',
	            store: util.createComboStore(initData[txt]),
	            listeners:{
	            	select:function(c,r){
	            		var fc = c.up('fieldcontainer'),
	            			no = fc.no,
	            			content = fc.up('toolbar').up('panel');
	            		content.items.get(no).setValue(r.get('value'));
	            	}
	            }
	       	},{
	       		xtype:'label',
	       		width:16,
	       		cls:'del',
	       		margin:'3 0 0 1',
	       		html:((no==0&&txt!='其他费用')?'':'<i data-qtip="删除" class="iconfont money-color del f15">&#xe62c;</i>')
	       	}]
        };
	},
	contentTpl :function(txt,no,record){
		var initParam ={'其他费用':'other','费用包含':'include','费用不含':'noclude','出行须知':'notice','温馨提示':'tips'};
		record = record || '';
		return {
			xtype:'textarea',
			maxLength:600,
			value:record.CONTENT,
			name:initParam[txt]+'['+no+'].CONTENT',
			listeners:{
				validitychange:function(nf,is){
					if(!is){
						var v = nf.getValue();
						nf.setValue(v.substring(0,600));
					}
				}
			}
		};
	}
});