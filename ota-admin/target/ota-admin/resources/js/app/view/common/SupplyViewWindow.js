Ext.define('app.view.common.SupplyViewWindow', {
    extend: 'Ext.Window',
    config:{
    	currVisitType:null,
    	paramsStr:null,
    	title:null,
    	width:800,
    	height:450,
		modal:true,
		draggable:false,
		resizable:false,
		layout:'fit',
		bodyCls:'py-container',
		letters: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']
    },
    initComponent: function(){
    	this.tpl = new Ext.XTemplate(
    		'<div class="py-index-nav">',
    		'<tpl for=".">',
    		'<a href="#index-{.}" id="py-index-{.}">',
	    	'{.}',
	    	'</a>',
    		'</tpl>',
    		'</div>'
    	);
    	this.data = this.getLetters();
    	var me = this;
    	var params = Ext.Object.fromQueryString(me.getParamsStr()),
    		currVisitType = params.PARAMS_COMPANY_TYPE||'1';
    	var title = currVisitType=='1'?'旅行社':'供应商';
    	this.setTitle(util.windowTitle('&#xe687;',title+'详情',''))
    	Ext.Ajax.request({
			url:cfg.getCtx()+'/stat/supply',
			params:params,
			success:function(res,req){
				var data = Ext.decode(res.responseText).data;
				if(data&&data.length>0){
					var len = [];
					me.add({
						autoScroll:true,
						tpl: new Ext.XTemplate(
				    		'<tpl for="letters">',
				    		'<dl id="index-{.}">',
					    		'<dt><span>&nbsp;</span>{.}</dt>',
					    		'<dd>',
						    		'<ul id="container-{.}">',
						    			'<tpl for="parent.data">',
						    				'<tpl if="this.isLetter(parent,'+(currVisitType=='1'?'BUY':'SALE')+'_BRAND_JP)">',
							    				'<li class="py-crad">',
							    				'<div class="py-crad-body">',
							    				'<h3 class="py-card-title">{'+(currVisitType=='1'?'BUY':'SALE')+'_COMPANY}</h3>',
							    				'<div class="py-card-short-title">{'+(currVisitType=='1'?'BUY':'SALE')+'_BRAND_NAME}</div>',
							    				/*'<p class="py-card-tags">',
							    				'<span>#西安</span>',
							    				'<span>#北京</span>',
							    				'<span>#三亚</span>',
							    				'</p>',*/
							    				'</div>',
							    				'<div class="py-crad-tool">',
								    				'<span class="phone"></span>',
								    				'<div class="py-card-tool-right">',
									    				'<span class="cnt">',
									    				'<span class="iconfont icon">&#xe74a;</span> ',
									    				'<span class="value">{CNT}</span>',
									    				'</span>',
									    				/*'<a class="fav">',
									    				'<span class="iconfont icon">&#xe73d;</span> ',
									    				'<span class="value">收藏</span>',
									    				'</a>',*/
								    				'</div>',
							    				'</div>',
							    				'</li>',
						    				'</tpl>',
						    			'</tpl>',
						    		'</ul>',
						    		'<div class="clear"></div>',
					    		'</dd>',
				    		'</dl>',
				    		'</tpl>',
				    		{
				    			isLetter:function(p,n){
				    				len.push(n[0].toUpperCase());
				    				return p==n[0].toUpperCase();
				    			}
				    		}
				    	),
				    	data:{
							letters:me.getLetters(),
							data:data
						}
					});
					len = Ext.Array.unique(len);
					Ext.Array.each(len,function(c,i){
						if(Ext.get('py-index-'+c)){
							Ext.get('py-index-'+c).setStyle({'color':'#dd4b39'});
						}
					});
				}else{
					me.add({xtype:'container',cls:'nodata',html:'暂无数据'});
				}
			},
			failure:function(){
				util.error("获取数据失败！");
			}
		});
        this.callParent();
    }
});
