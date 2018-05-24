Ext.define('app.view.account.index',{ 
	extend: 'Ext.panel.Panel',
	layout:'border',
	items:[{
		region:'center',
		itemId:'basegrid',
		xtype:'accountgrid'
	},{
		region:'north',
		cls:'info',
		itemId:'baseinfo',
		dockedItems:[{
			xtype:'toolbar',
	    	layout: {
                overflowHandler: 'scroller'
            },
	    	itemId:'accounttool',
	    	style:'background:#fafafa;',
	    	items:[{
	    		disabled:true,
	    		text:'正在初始化...'
	    	}]
		}],
		height:80,
		bodyPadding:'0 10 8 0',
		tpl:[
       	'<tpl for=".">',
       	'<div class="p-detail column">',
       		'<ul>',
    		'<li ><label>账户状态:</label>{ZHZT:this.FString()}</li>',
    		'<li><label>账户余额:</label>{YE:this.format()}</li>',
    		'<li><label>网上充值:</label>{ZXCZ:this.format()}</li>',
    		'<li><label>线下充值:</label>{SDCZ:this.format()}</li>',
    		'<li><label>透支金额:</label>{TZ:this.format()}</li>',
    		'<li><label>待还款金额:</label>{WH:this.format()}</li>',
    		'<li><label>账户支付:</label>{YEZF:this.format()}</li>',
    		'<li><label>退款金额:</label>{DDTK:this.format()}</li>',
    		'</ul>',
       	'</div>',
       	'</tpl>',
       	{
       		FString:function(v){
       			if(v=='启用'){
       				return '<span class="f14" style="color:#fff;background:#53a93f;padding:1px 3px;">启用中</span>';
       			}else{
       				return '<span class="f14" style="color:#fff;background:#dd4b39;padding:1px 3px;">已停用</span>';
       			}
       		},
        	format:function(value){
        		return util.moneyFormat(value);  
        	}
       	}]
	}],
	initComponent: function() {
        this.callParent();
	}
});