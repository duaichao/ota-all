Ext.define('app.view.b2b.user.supply.orderGrid', {
    extend: 'Ext.grid.Panel',
    config:{
    	columnLines: true,
    	selType:'rowmodel',
        params:null
    },
    listeners:{
    	afterlayout:function( g, layout, eOpts ){
    		console.log(layout);
    	}
    },
    features: [{
        ftype : 'grouping',
        groupHeaderTpl :[
		    '<span>{[this.format(values)]}</span>',
		    {
		        format: function(o) {
	        		var str = o.name,
	        			arr = str.split('*'),
	        			viewStr = arr[0],
	        			paramStr = arr[1],
	        			dateStr = viewStr.split(' ')[0],
	        			proId = paramStr.split(' ')[0];
	        			//planId = paramStr.split(' ')[1];'&planId='+planId
	        		var url = cfg.getCtx()+'/b2b/user/export/visitor?beginDate='+dateStr+'&produceId='+proId;
	        		return viewStr+' ：共'+o.rows.length+'笔订单，<a href="javascript:;" target="_blank" onclick="util.downloadAttachment(\''+url+'\')">导出游客接送名单</a>';
		        }
		    }
		],
        hideGroupedHeader : false,
        startCollapsed:true,
        id:'restaurantGrouping',
        enableGroupingMenu : false
    }],
    initComponent: function() {
    	this.store = Ext.create('Ext.data.Store', {
            groupField:'GROUP_SHOW',
            autoLoad: true,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	        'NO','MAN_COUNT','CHILD_COUNT','SALE_AMOUNT','INTER_AMOUNT'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/b2b/user/latest/route',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.columns=[Ext.create('Ext.grid.RowNumberer',{width:25}),{
    		text: '订单编号',
	        flex:1,
	        dataIndex: 'NO',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20"><a href="javascript:;">'+v+'</a></div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '成人数',
    		width:100,
	        dataIndex: 'MAN_COUNT',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f18 disable-color">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '儿童数',
    		width:100,
	        dataIndex: 'CHILD_COUNT',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f18 disable-color">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '销售金额',
	        width:100,
	        dataIndex: 'SALE_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18 orange-color')+'</div>'
	        	].join('');
	        }
    	},{
    		text: '结算金额',
	        width:100,
	        dataIndex: 'INTER_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18 blue-color')+'</div>'
	        	].join('');
	        }
    	}];
    	/*this.dockedItems = [{
        	xtype:'toolbar',
        	style:'background:#e2eff8;',
        	layout: {
                overflowHandler: 'Menu'
            },
        	items:['<span class="bold f14 blue-color"><i class="iconfont f18">&#xe692;</i> 最近出团</span>','->',{
        		xtype: 'segmentedbutton',
        		listeners:{
        			toggle:function(sb, button, isPressed){
        				var g = sb.up('toolbar').up('gridpanel');
        				if(sb.getValue()==1){
        					g.getStore().getProxy().setUrl(cfg.getCtx()+'/b2b/user/latest/route');
        				}else{
        					g.getStore().getProxy().setUrl(cfg.getCtx()+'/b2b/user/latest/traffic');
        				}
        				g.getStore().load();
        			}
        		},
                allowMultiple: false,
                items: [{
                    text: '线路',
                    value:1,
                    pressed: true
                },{
                    text: '交通',
                    value:2
                }]
        	}]
        }];*/
    	
    	
    	this.callParent();
    },
    applyParams:function(params){
    	if(params){
    		Ext.applyIf(params,this.store.getProxy().getExtraParams());
	    	this.store.getProxy().setExtraParams(params);
	    	this.store.load();
    	}
    	return params;
    }
});