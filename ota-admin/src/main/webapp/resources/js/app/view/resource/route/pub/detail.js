Ext.define('app.view.resource.route.pub.detail', {
	extend: 'Ext.panel.Panel',
	xtype:'routedetail',
	config:{
		routeId:'',
		bodyPadding:10,
		dockedItems: [{
	        xtype: 'toolbar',
	        style:'background:#f5f5f5; border-bottom: 1px solid #e1e1e1!important;-webkit-box-shadow: 0 5px 10px -5px #e1e1e1;box-shadow: 0 5px 10px -5px #e1e1e1;',
	        itemId:'detailtool',
	        dock: 'top',
	        items: ['->',{
				itemId:'print',
				height:45,
				width:100,
				cls:'blue',
				ui:'default',
				glyph:'xe64b@iconfont',
				text:'打印行程'
			}]
	    }]
	},
	autoScroll:true,
    initComponent: function() {
		var me = this;
		this.tpl = new Ext.XTemplate(
			'<div class="p-detail" style="max-width:1000px;margin:0 auto;">',
			'<h1 class="f18 blue-color" style="padding:10px 0;"><{[this.typeText(values.TYPE)]}游><{DAY_COUNT}日游>{TITLE}</h1>',
            '<div style="font-size:14px;padding:0 0 10px 0;text-align:center;">编号:{NO}</div>',
            '<div style="border:1px solid #d1d1d1;border-top-width:5px;border-top-color:#427fed;border-bottom:none;padding:8px 0 8px 5px;font-size:15px;">',
            '{parentCompany}',
            '<tpl if="shortName!=\'\'">',
            '-{shortName}',
            '</tpl>',
            /*
            '<tpl if="chinaName!=\'\'">',
            '，联系人：{chinaName}',
            '</tpl>',
            
            '<tpl if="userPhone!=\'\'">',
            '，电话：{userPhone}',
            '</tpl>',
            */
            
            '<tpl if="userPhone!=\'\'">',
            '<p style="padding-top:8px;">地址：{address}</p>',
            '</tpl>',
            
            
            '</div>',
            '<table width="100%" border="0">',
	            '<tr>',
				'<td class="name">销售须知：</td>',
				'<td>{[this.ntobr(values.NOTICE)]}</td>',
			'</tr>',	
            '<tr>',
    				'<td class="name">产品推荐：</td>',
    				'<td>{[this.ntobr(values.FEATURE)]}</td>',
    			'</tr>',
    			'<tr>',
					'<td class="name">产品特色：</td>',
					'<td>{[this.ntobr(values.FEATURE1)]}</td>',
				'</tr>',
    			//目的地
    			'<tpl for="citys">',
    			'<tr>',
    				'<td colspan="2" class="bg"><i class="iconfont blue-color f16">&#xe68d;</i> <span class="blue-color f16" style="margin-right:5px;">{CITY_NAME}</span> ',
    				'<tpl if="TYPE == 2">',
    				'停留<span class="nbr blue-color f16">{STAY_COUNT}</span>天',
    				'</tpl>',
    				'</td>',
    			'</tr>',
    			//日程
    			'<tpl for="days">',
    			'<tr>',
    				'<td colspan="2"  style="padding-left:15px;">',
	    				'<i class="iconfont blue-color">&#xe6a2;</i>',
	    				'<span class="nbr blue-color f14">{NO}</span>',
	    				'<tpl if="this.styleTitle(values.TITLE)">',
	    				'<pre style="display:inline-block;">',
	    				'	{TITLE}',
	    				'</pre>',
	    				'<tpl else>',
	    				'<pre style="display:inline-block;">',
	    				'	{BEGIN_CITY}',
	    				'	 {[this.fmTools(values.TOOL)]}',
	    				'	{END_CITY}',
	    				'	 {[this.fmTools(values.TOOL1)]}',
	    				'	{END_CITY1}',
	    				'</pre>',
	    				'</tpl>',
    				'</td>',
    			'</tr>',
    			'<tr>',
    				'<td class="name">当日提示：</td>',
    				'<td>{TODAY_TIPS}</td>',
    			'</tr>',
    			'<tr>',
    				'<td class="name">住宿提示：</td>',
    				'<td>{HOTEL_TIPS}</td>',
    			'</tr>',
    			'<tr>',
    				'<td class="name">自费提示：</td>',
    				'<td>{PAY_TIPS}</td>',
    			'</tr>',
    			'<tr>',
    				'<td class="name">三餐：</td>',
    				'<td><table style="width:100%" class="inner-table"><tr><td> 早：{[this.emptyText(values.BREAKFAST)]} </td> <td>中：{[this.emptyText(values.LUNCH)]}</td> <td>晚：{[this.emptyText(values.DINNER)]}</td> </tr></table></td>',
    			'</tr>',
    				//安排
	    			'<tpl for="details">',
	    			'<tr>',
	    				'<td class="name">',
	    				'{TITLE}：',
	    				'</td>',
	    				'<td>',
	    				
	    				'<tpl if="this.isNull(scenics)">',
	    				'<div style="height:25px;line-height:20px;">',
	    					
	    					//景点
	    					'游览景点：',
	    					'<tpl for="scenics">',
	    					'<a class="tag" style="padding:0px;background:none;" href="javascript:;">{SCENIC_NAME}</a>',
	    					'</tpl>',
	    					
	    				'</div>',
	    				'</tpl>',
	    				
	    				
	    				
	    				'<div class="content">{[this.fmall(values.CONTENT,true)]}</div>',
	    				'</td>',
	    			'</tr>',
	    			'</tpl>',
    			'</tpl>',
    			'</tpl>',
    			//费用包含
    			'<tr>',
    				'<td colspan="2" class="bg">费用包含</td>',
    			'</tr>',
    			'<tpl for="include">',
    				'<tr>',
	    				'<td class="name">{TITLE}：</td>',
	    				'<td>{[this.ntobr(values.CONTENT)]}</td>',
	    			'</tr>',
    			'</tpl>',
    			//费用不含
    			'<tr>',
    				'<td colspan="2" class="bg">费用不含</td>',
    			'</tr>',
    			'<tpl for="noclude">',
    				'<tr>',
	    				'<td class="name">{TITLE}：</td>',
	    				'<td>{[this.ntobr(values.CONTENT)]}</td>',
	    			'</tr>',
    			'</tpl>',
    			//出行须知
    			'<tr>',
    				'<td colspan="2" class="bg">出行须知</td>',
    			'</tr>',
    			'<tpl for="notice">',
    				'<tr>',
	    				'<td class="name">{TITLE}：</td>',
	    				'<td>{[this.ntobr(values.CONTENT)]}</td>',
	    			'</tr>',
    			'</tpl>',
    			//温馨提示
    			'<tr>',
    				'<td colspan="2" class="bg">温馨提示</td>',
    			'</tr>',
    			'<tpl for="tips">',
    				'<tr>',
	    				'<td class="name">{TITLE}：</td>',
	    				'<td>{[this.ntobr(values.CONTENT)]}</td>',
	    			'</tr>',
    			'</tpl>',
    		'</table>',
			'</div>',{
	    		typeText:function(v){
	    			var arr = ['','周边','国内','出境'];	
	    			return arr[v];
	    		},
	    		isNull:function(v){
	    			if(!v||v==''){
	    				return false;
	    			}else{return true;}
	    		},
	    		emptyText:function(v){
	    			if(!v||v==''){
	    				return '无';
	    			}else{return v;}
	    		}
	    		,
	    		ntobr :function(v){
	    			v = v||'';
	    			v = v.replace(/\n/g,'<br>');
	    			return v;
	    		},
	    		fmTools :function(v){
	    			v = v ||'';
	    			if(v=='')return '';
	    			var key = cfg.getTravelTools(),
	    				val = cfg.getTravelToolsIcon();
	    			return '<i class="iconfont blue-color" data-qtip="'+v+'">'+val[Ext.Array.indexOf(key,v)]+'</i>';
	    		},styleTitle :function(v){
	    			v = v ||'';
	    			return v !='';
	    		},
	    		fmall:function(html,nobr){
	    			
	    			return html;
	    			
	    			
	    			html = html ||'';
	    			if(html=='')return html;
	    			
	    			html = html.replace(/\n/g,'<br>').replace(/\n\t/g, '<br>').replace(/&nbsp;/g,'');
	    			
	    			if(nobr){
	    				html = html.replace(/<br>/g,'').replace(/<br\/>/g, '').replace(/&nbsp;/g,'');
	    			}
	    			
	    			html = html.replace(/<\/?SPAN[^>]*>/gi, "" );// Remove all SPAN tags

	    			html = html.replace(/<(\w[^>]*) class=([^ |>]*)([^>]*)/gi, "<$1$3") ; // Remove Class attributes

	    			html = html.replace(/<(\w[^>]*) style="([^"]*)"([^>]*)/gi, "<$1$3") ; // Remove Style attributes

	    			html = html.replace(/<(\w[^>]*) lang=([^ |>]*)([^>]*)/gi, "<$1$3") ;// Remove Lang attributes

	    			html = html.replace(/<\\?\?xml[^>]*>/gi, "") ;// Remove XML elements and declarations

	    			html = html.replace(/<\/?\w+:[^>]*>/gi, "") ;// Remove Tags with XML namespace declarations: <o:p></o:p>

	    			html = html.replace(/ /, " " );// Replace the


	    			var re = new RegExp("(<P)([^>]*>.*?)(<\/P>)","gi") ; // Different because of a IE 5.0 error

	    			html = html.replace( re, "<div$2</div>" ) ;
	    			return html;
	    		}
	   		}
		);
		this.callParent();
	},
	updateRouteId:function(routeId){
		if(routeId){
			this.loadDetail();
		}
		return routeId;
	},
	loadDetail :function(){
		var me = this;
		top.Ext.getBody().mask('加载线路详情...');
		this.getDockedItems('toolbar[dock="top"]')[0].add({
			itemId:'expend',
			height:45,
			width:100,
			cls:'green',
			ui:'default',
			glyph:'xe65b@iconfont',
			text:'导出行程',
			href:cfg.getCtx()+'/resource/route/export?routeId='+this.getRouteId()
		});
		Ext.Ajax.request({
			url:cfg.getCtx()+'/resource/route/detail?routeId='+me.getRouteId(),
			success:function(response, opts){
				var action = Ext.decode(response.responseText);
				me.setData(action);
				if(action.IS_FULL_PRICE=='1'&&me.up('window').down('#card-price')){
					me.up('window').down('#card-price').show();
					me.up('window').down('#card-price').trafficId = action.TRAFFIC_ID; 
				}
				top.Ext.getBody().unmask();
				me.down('toolbar').down('button[itemId=print]').on('click',function(){
					var html = me.tpl.apply(action);
					util.print('打印行程',html,true,false);
				});
				
			}
		});
	}
});