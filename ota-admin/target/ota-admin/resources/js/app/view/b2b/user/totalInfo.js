Ext.define('app.view.b2b.user.totalInfo', {
    extend: 'Ext.Panel',
    config:{
    	layout: {
	    	type:'vbox',
	        pack: 'start',
	        align: 'stretch'
	    },
	    border:true,
	    defaults:{
	    	style:'border:1px solid #d1d1d1;border-width:1px 0 0 0;',
  		    cls:'total',
  		    bodyStyle:'background:rgba(255,255,255,.5);',
  		    border:false
  	  	},
  	  	params:null
    },
    initComponent: function() {
    	this.callParent();
    	var me = this;
	    var baseTpl = [
	                   '<div class="item">',
	                   '<table width="100%" height="100%" style="border-collapse: collapse;border:none;" class="{CLS}">',
	                   '<tr>',
	                   '<td style="width:10%;">{ACTUAL_AMOUNT}</td>',
	                   '<td style="width:10%;">{ZX_AMOUNT}</td>',
	                   '<td style="width:10%;">{YE_AMOUNT}</td>',
	                   '<td>{CNT}</td>',
	                   '<td>{PRODUCE_CNT}</td>',
	                   '<td>{GROUP_COMPANY_CNT}</td>',
	                   '<td>{SMALL_COMPANY_CNT}</td>',
	                   '<td>{GW_COMPANY_CNT}</td>'
	     ];
	     if(this.getParams().pruviews!='supply'){
	    	 baseTpl.push('<td>{SALE_COMPANY_CNT}</td>');
	    	 baseTpl.splice(3, 0, '<td>{SALE_AMOUNT}</td>');
	    	 baseTpl.splice(7, 0, '<td><h3 class="f14">{PERSON_COUNT}</h3><div style="margin-top:3px;">{MAN_COUNT}/{CHILD_COUNT}</div></td>');
	    	 baseTpl.splice(7, 0, '<td>{PROFIT_AMOUNT}</td>');
	     }else{
	    	 baseTpl.splice(6, 0, '<td>{PERSON_COUNT}</td><td>{MAN_COUNT}</td><td>{CHILD_COUNT}</td>');
	     }
	     baseTpl.push('</tr></table></div>');
	                  
	     var headData = {
	    		 'CLS':'header-tb',
	    		 'SALE_AMOUNT':'外卖金额',
	    		 'ACTUAL_AMOUNT':'结算金额',
	    		 'PROFIT_AMOUNT':'旅行社利润',
	    		 'ZX_AMOUNT':'在线支付',
	    		 'YE_AMOUNT':'余额支付',
	    		 'PERSON_COUNT':'总人数',
	    		 'MAN_COUNT':'成人',
	    		 'CHILD_COUNT':'儿童',
	    		 'CNT':'订单数',
	    		 'PRODUCE_CNT':'产品数',
	    		 'GROUP_COMPANY_CNT':'旅行社',
	    		 'SMALL_COMPANY_CNT':'小社',
	    		 'GW_COMPANY_CNT':'旅游顾问',
	    		 'SALE_COMPANY_CNT':'供应商'
	     };
	     me.add({
	    	 style:{
	    		 border:'none'
	    	 },
			 itemId:'head',
			 dockedItems: [{
				xtype: 'label',
				width:50,
				dock: 'left',
				style:{
					background:'#f5f5f5'
				},
				html:'&nbsp;'
			 }],
			 tpl:baseTpl,
			 data:headData
	     });
	     me.add({
			 itemId:'today',
			 dockedItems: [{
				xtype: 'label',
				width:50,
				dock: 'left',
				style:{
					height:'35px',
					lineHeight:'35px',
					fontWeight:'bold',
					fontSize:'14px',
					background:'#f5f5f5',
					textAlign:'center',
					color:'#404040'
				},
				html:'今日'
			 }],
			 tpl:baseTpl,
			 data:{}
	     });
	     me.add({
	    	 itemId:'total',
	    	 dockedItems: [{
				xtype: 'label',
				width:50,
				dock: 'left',
				style:{
					height:'35px',
					lineHeight:'35px',
					fontWeight:'bold',
					fontSize:'14px',
					background:'#f5f5f5',
					textAlign:'center',
					color:'#404040'
				},
				html:'累计'
			 }],
	    	 tpl:baseTpl,
	    	 data:{}
	     });
    },
    applyParams:function(params){
    	var me = this;
    	if(params){
			Ext.Ajax.request({
				params:params,
				url:cfg.getCtx()+'/stat/sale/today',
				success:function(response, opts){
					var data = Ext.decode(response.responseText);
					data.CLS='today-tb';
					me.down('#today').setData(data);
				}
			});
			Ext.Ajax.request({
				params:params,
				url:cfg.getCtx()+'/stat/sale/total',
				success:function(response, opts){
					var data = Ext.decode(response.responseText);
					data.CLS='total-tb';
					me.down('#total').setData(data);
				}
			});
    	}
    	return params;
    }
});
