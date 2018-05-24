Ext.define('app.view.stat.sale.parent', {
    extend: 'Ext.Panel',
    config:{
    	layout:'fit'
    },
    initComponent: function(){
    	this.callParent();
    	var me = this;
    	if(citys.length>0){
        	var innerItems = [];
        	Ext.Array.each(citys,function(city,index){
        		innerItems.push({
        			itemId:city.CITY_ID,
        			closable:false,
        			title:city.SUB_AREA,
        			cityId:city.CITY_ID,
        			xclass:'app.view.stat.sale.parentContainer',
        			tabConfig: {
        	            margin:'2 0 0 0'
        	        }
        		});
        	});
        	me.add({
        		frame:true,
        		itemId:'frameContainer',
        		xtype: 'tabpanel',
	            items:innerItems,
	            tabPosition: 'left',
                activeTab: 0
        	});
        }else{
        	var myTotal = Ext.create('Ext.ux.IFrame',{
				src:cfg.getCtx()+'/stat/sale?isParent=1&PARAMS_COMPANY_ID='+(cfg.getUser().companyType==0?currVisitCompanyId:cfg.getUser().companyId)+'&PARAMS_COMPANY_TYPE=2',
				title:cfg.getUser().companyType==0?currVisitCompanyName:cfg.getUser().companyName,
				itemId:cfg.getUser().companyType==0?currVisitCompanyId:cfg.getUser().companyId,
				style:'width:100%;height:100%;'
			});
        	innerItems = [{
        		title:'公司统计',
        		xclass:'app.view.stat.sale.parentContainer'
        	},myTotal];
        }
    	me.add({
    		frame:true,
    		itemId:'frameContainer',
    		xtype: 'tabpanel',
            items:innerItems,
            tabPosition: 'left',
            activeTab: 0
    	});
    }
});

