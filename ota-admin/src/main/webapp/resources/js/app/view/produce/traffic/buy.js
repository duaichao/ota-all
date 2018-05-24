/*var nation = [["汉族","汉族"],["蒙古族","蒙古族"],["回族","回族"],["壮族","壮族"],["维吾尔族","维吾尔族"],["藏族","藏族"],["苗族"," 苗族"],["彝族"," 彝族"],["布依族"," 布依族"],["朝鲜族","朝鲜族"],["满族","满族"],["侗族"," 侗族"],["瑶族"," 瑶族"],["白族"," 白族"],["土家族","土家族"],["哈尼族","哈尼族"],["哈萨克族","哈萨克族"],["傣族"," 傣族"],["黎族"," 黎族"],["僳僳族","僳僳族"],["佤族","佤族"],["畲族","畲族"],["拉祜族","拉祜族"],["水族","水族"],["东乡族","东乡族"],["纳西族","纳西族"],["景颇族","景颇族"],["柯尔克孜族","柯尔克孜族"],["土族","土族"],["达斡尔族","达斡尔族"],["仫佬族","仫佬族"],["仡佬族","仡佬族"],["羌族","羌族"],["锡伯族","锡伯族"],["布朗族","布朗族"],["撒拉族","撒拉族"],["毛南族","毛南族"],["阿昌族","阿昌族"],["普米族","普米族"],["塔吉克族","塔吉克族"],["怒族","怒族"],["乌孜别克族","乌孜别克族"],["俄罗斯族","俄罗斯族"],["鄂温克族","鄂温克族"],["德昂族","德昂族"],["保安族","保安族"],["裕固族","裕固族"],["京族","京族"],["基诺族","基诺族"],["高山族","高山族"],["塔塔尔族","塔塔尔族"],["独龙族","独龙族"],["鄂伦春族","鄂伦春族"],["赫哲族","赫哲族"],["门巴族","门巴族"],["珞巴族","珞巴族"]];*/
var totalHtml = [
	'<ul>',
	'<i class="eye icon-eye" data-qtip="显示同行"></i>',
	'<h3 style="text-align:right;font-size:12px;color:#bbb;padding-bottom:5px;border-bottom:1px solid #bbb;">出发日期：<span class="f16 blue-color">'+formatQueryDate+'</h3>',
	'</li>',
	'<li class="total">',
	'<label class="title">总计</label>',
	'<label class="th">',
	util.moneyFormat(0,'f20'),
	'</label>',
	'<label class="wm">',
	util.moneyFormat(0,'f20'),
	'</label>',
	'</li>',
];
if(prices.length>0){
	for(var i=0;i<prices.length;i++){
		var price = prices[i];
		if(price.TYPE_NAME!='同行'){
			totalHtml.push([
				'<li id="'+price.ATTR_ID+'" price="'+price.WAIMAI+'" thprice="'+price.TONGHANG+'">',
				'<label class="title">'+price.ATTR_NAME+'</label>',
				'<label class="th">',
				'<b>0 ×</b>'+util.moneyFormat(price.TONGHANG,'f12 disable-color'),
				'</label>',
				'<label class="wm">',
				'<b>0 ×</b>'+util.moneyFormat(price.WAIMAI,'f12 disable-color'),
				'</label>',
				'</li>'
			].join(''));
		}
	}
}
totalHtml.push('</ul>');
Ext.define('app.view.produce.traffic.buy', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	region:'center',
    	autoScroll:true,
    	border:false,
    	buttons:[{
    		xtype:'panel',
    		width:800,
    		margin:'0 0 0 20',
    		bodyStyle:'background:transparent;',
    		html:[
    			'<ol class="ui-step ui-step-4">',
    				'<li class="ui-step-start ui-step-done">',
				        '<div class="ui-step-line">-</div>',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="ui-step-number">1</i>',
				            '<span class="ui-step-text">选择产品</span>',
				        '</div>',
				    '</li>',
				    '<li class="ui-step-active">',
				        '<div class="ui-step-line">-</div>',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="ui-step-number">2</i>',
				            '<span class="ui-step-text">填写与核对</span>',
				        '</div>',
				    '</li>',
				    '<li class="">',
				        '<div class="ui-step-line">-</div>',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="ui-step-number">3</i>',
				            '<span class="ui-step-text">支付</span>',
				        '</div>',
				    '</li>',
				    '<li class="ui-step-end">',
				        '<div class="ui-step-line">-</div>    ',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="iconfont ui-step-number">&#xe6a0;</i>',
				            '<span class="ui-step-text">成功</span>',
				        '</div>',
				    '</li>',
    			'</ol>'
    		].join('')
    	},'->',{
    		cls:'disable',
    		itemId:'backBtn',
    		text:'返回'
    	},{
    		itemId:'saveBtn',
    		disabled: true,
			formBind: true,
			//href:cfg.getCtx()+'/produce/payfinish?orderId=a06c0b3d51744be38ea0ce362385516f',
    		text:'保存，下一步'
    	}],
    	itemId:'subForm',
    	xtype:'form',
   		fieldDefaults: {
	        labelAlign: 'right',
	        labelWidth: 60,
	        msgTarget: 'side'
	    },
    	margin:'1',
    	layout:{
    		type: 'hbox',
	        pack: 'start',
	        align: 'stretch'
    	},
    	items:[{
    		flex:1,
    		border:false,
    		layout:{
	    		type: 'vbox',
		        pack: 'start',
		        align: 'stretch'
	    	},
	    	items:[{
	    		xtype:'trafficdetailgrid',
	    		height:81
	    	},{
	    		flex:1,
	    		margin:'1 0 1 0',
	    		xtype:'trafficvisitorgrid'
	    		//border:true,
	    		//title:'<span class="f16"><i class="iconfont f18">&#xe60b;</i> 游客信息</span>',
	    	}]
    	},{
    		margin:'0 0 1 1',
    		itemId:'rightPanel',
    		width:240,
    		layout:{
    			type: 'vbox',
		        pack: 'start',
		        align: 'stretch'
    		},
    		items:[{
	    		margin:'0 0 1 0',
	    		height:100,
	    		bodyPadding:'5',
	    		border:true,
	    		title:'<span class="f16"><i class="iconfont f18">&#xe606;</i> 集合信息</span>',
	    		items:[{
	    			xtype:'fieldcontainer',
			    	layout:'vbox',
			    	items:[{
		    			xtype:'hidden',
		    			name:'pm[ID]',
		    			value:(od?od.ID:'')
		    		},{
		    			xtype:'combo',
		    			fieldLabel:'集合地点',
		    			width:226,pageSize:50,
		    			editable:false,
					    displayField: 'MUSTER_PLACE',
					    focusOnToFront:true,
					    forceSelection:true,
					    queryMode: 'remote',
					    triggerAction: 'all',
					    valueField: 'MUSTER_PLACE',
					    emptyText:'选择集合信息',
		    			value:(od?od.MUSTER_PLACE:''),
					    listConfig:{
					    	minWidth:360,
					    	itemTpl:[
								 '<tpl for=".">',
						            '<li class="city-item">{MUSTER_PLACE}<span>{MUSTER_TIME}</span></li>',
						        '</tpl>'
							]
					    },
					    store:Ext.create('Ext.data.Store',{
					    	pageSize:50,
					    	autoLoad:true,
						    proxy: {
						        type: 'ajax',
						        noCache:true,
						        model:Ext.create('Ext.data.Model',{
						        	fields: ['MUSTER_PLACE','MUSTER_TIME']
						        }),
						        url: cfg.getCtx()+'/resource/traffic/muster?trafficId='+trafficId,
						        reader: {
						            type: 'json',
						            rootProperty: 'data',
						            totalProperty:'totalSize'
						        }
						   	}
					    }),listeners:{
					    	select:function( combo, records, eOpts ){
					    		combo.nextSibling().setValue(records.get('MUSTER_TIME'));
					    		combo.nextSibling().nextSibling().setValue(combo.getValue());
					    	}
					    }
		    		},{
		    			xtype:'textfield',
		    			name:'pm[MUSTER_TIME]',
		    			readOnly:true,
		    			width:200,//allowBlank: false,
		    			fieldLabel:'集合时间',
		    			value:(od?od.MUSTER_TIME:'')
		    		},{
		    			xtype:'hidden',
		    			name:'pm[MUSTER_PLACE]',
		    			value:(od?od.MUSTER_PLACE:'')
		    		},{
		    			xtype:'hidden',
		    			itemId:'saleAmount',
		    			name:'pm[SALE_AMOUNT]',
		    			value:(od?od.SALE_AMOUNT:'')
		    		},{
		    			xtype:'hidden',
		    			itemId:'interAmount',
		    			name:'pm[INTER_AMOUNT]',
		    			value:(od?od.INTER_AMOUNT:'')
		    		},{
		    			xtype:'hidden',
		    			itemId:'manCount',
		    			name:'pm[MAN_COUNT]',
		    			value:(od?od.MAN_COUNT:'')
		    		},{
		    			xtype:'hidden',
		    			itemId:'childCount',
		    			name:'pm[CHILD_COUNT]',
		    			value:(od?od.CHILD_COUNT:'')
		    		}]
	    		}]
	    		
	    	},{
	    		bodyStyle:'background:transparent;',
	    		flex:1,
	    		itemId:'totalPanel',
	    		cls:'checkout',
	    		html:totalHtml.join(''),
	    		listeners:{
	    			afterrender:function(p){
	    				var el = p.getEl().down('ul'),
	    					eye = el.down('i.eye'),
	    					lis = el.query('li',false);
	    				eye.on('click',function(){
	    					if(eye.hasCls('icon-eye-off')){
	    						eye.removeCls('icon-eye-off');
	    						Ext.Array.each(lis, function(obj, index, countriesItSelf) {
								    obj.down('label.th').setStyle({visibility:'hidden'});
								});
	    					}else{
	    						eye.addCls('icon-eye-off');
	    						Ext.Array.each(lis, function(obj, index, countriesItSelf) {
								    obj.down('label.th').setStyle({visibility:'visible'});
								});
	    					}
	    				});
	    				
	    			}
	    		}
	    	}]
    	}]
    }]
});

