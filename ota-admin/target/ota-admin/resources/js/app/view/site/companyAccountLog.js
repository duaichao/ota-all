Ext.define('app.view.site.companyAccountLog', {
	extend:'Ext.Window',
	config:{
		companyId:null,
		width:800,
		height:360,
		modal:true,
		draggable:false,
		resizable:false,
		layout:'fit',
		autoScroll:true,
		bodyPadding:5,
		title:[
       		'<span style="font-size:16px;color:#fff">',
    		'<i class="iconfont" style="font-size:18px;">&#xe633;</i> ',
    		'充值记录',
    		'</span>'
    	].join(''),
		tpl:new Ext.XTemplate(
       		'<div class="p-detail">',
       		'<table width="100%" border="0">',
       			'<tr>',
       				'<td>公司/部门</td>',
       				'<td width="8%">来源</td>',
       				'<td width="15%">金额</td>',
       				'<td width="15%">时间</td>',
       			'</tr>',
       			'<tpl for="data">',
       			'<tr>',
       				'<td>{[this.formatStr(values)]}</td>',
       				'<td>{[this.formatStr1(values)]}</td>',
       				'<td>{[util.moneyFormat(values.MONEY,\'f14\')]}</td>',
       				'<td>{[util.timeAgoInWords(values.CREATE_TIME)]}</td>',
       			'</tr>',
       			'</tpl>',
       		'</table></div>',{
       			formatStr1:function(o){
       				if(o.TYPE=='0'){
       					return '入账';
       				}else{
       					return '使用';
       				}
       			},
       			formatStr:function(o){
       				var r = [];
       				if(o.TYPE=='0'){
       					r.push('<div class="f14 blue-color">'+o.COMPANY_NAME+'</div>');
       				}else{
       					r.push('<div class="f14 blue-color">'+o.DEPART_NAME+'</div>');
       				}
       				r.push('<p class="disable-color" style="padding:5px 0;">备注：'+o.NOTE+'</p>');
       				return r.join('');
       			}
       		}
		)
	},
	dockedItems:[{
		xtype:'toolbar',
		dock:'top',
		items:['->',{
			fieldLabel: '时间',
			labelWidth:60,
			labelAlign:'right',
			width:165,
			xtype:'datefield',
			format:'Y-m-d',
			endDateField: 'endxddt',
	        itemId:'startxddt',
	        showToday:false,
	        vtype: 'daterange'
		},'-',{
			hideLabel:true,
			width:105,
			xtype:'datefield',
			format:'Y-m-d',
			itemId:'endxddt',
			showToday:false,
            startDateField: 'startxddt',
            vtype: 'daterange'
		},{
			text:'查询',
			handler:function(btn){
				var w = btn.up('window'),
					endTime = btn.previousSibling(),
					beginTime = endTime.previousSibling().previousSibling();
				Ext.Ajax.request({
	        		url:cfg.getCtx()+'/site/company/funds/log?companyId='+w.getCompanyId(),
	        		params:{
	        			beginTime:Ext.Date.format(beginTime.getValue(),'Y-m-d'),
	        			endTime:Ext.Date.format(endTime.getValue(),'Y-m-d')
	        		},
	        		success:function(response, opts){
	        			var obj = Ext.decode(response.responseText);
	        			w.setData(obj);
	        		}
	        	});
			}
		},{
			text:'导出',
			handler:function(btn){
				var w = btn.up('window'),
				endTime = btn.previousSibling().previousSibling(),
				beginTime = endTime.previousSibling().previousSibling();
				
				beginTime = beginTime.getValue()||'';
				endTime = endTime.getValue()||'';
        		if(beginTime==''||endTime==''){
        			util.alert('请选择时间范围');
        			return;
        		}
        		beginTime = Ext.Date.format(beginTime,'Y-m-d');
        		endTime = Ext.Date.format(endTime,'Y-m-d');
    			
    			util.downloadAttachment(cfg.getCtx()+'/site/company/funds/log/export?companyId='+w.getCompanyId()+'&beginTime='+beginTime+'&endTime='+endTime);
			}
		}]
	}],
	updateCompanyId: function(companyId) {
    	var me = this;
    	if(companyId){
    		Ext.Ajax.request({
        		url:cfg.getCtx()+'/site/company/funds/log?companyId='+companyId,
        		success:function(response, opts){
        			var obj = Ext.decode(response.responseText);
        			me.setData(obj);
        		}
        	});
    		
    	}
    }
});