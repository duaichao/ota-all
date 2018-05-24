Ext.define('app.view.b2c.route', {
	extend:'Ext.tab.Panel',
	config:{
	    frame:true,
	    padding:'2 0 0 0'
    },
	initComponent: function() {
		var me = this,items = [],grid,bbar,store,columns,docked;
		store = util.createGridStore(cfg.getCtx()+'/b2c/route/list',Ext.create('Ext.data.Model',{
			fields: [
		         'ID','COMPANY_ID','NO','THEMES','ROUTE_WAP_PRICE', 'TITLE','ROUTE_ID',
		         'ORDER_BY','COMPANY_ID','USER_ID','CREATE_TIME',
		         'DAY_COUNT','ATTR','FACE'
			]
		}));
		columns = [{
			text: '线路图片',
        	dataIndex:'FACE',
        	width:145,
	        renderer: function(v,metaData,r,colIndex,store,view){
	        	v = cfg.getPicCtx()+'/'+v;;
	        	var h = [
		        		'<img src="'+v+'" width="125px" height="70px"/>'
		        	],dc = r.get('DAY_COUNT') || '',attr = r.get('ATTR') || '';
		        dc = dc==''?'':dc+'日';
		        if(attr!=''){
		        	h.push('<span style="position:absolute;top:5px;left:10px;background:'+cfg.getRouteAttrTagColor()[attr]+';color:#fff;padding:2px 4px;">'+attr+dc+'</span>');
		        }
		        h.push('<div class="ht16" style="color:#666;">编号: <a href="javascript:;">'+r.get('NO')+'</a></div>');
	        	return h.join('');
	        }
		},{
        	text: '线路名称',
        	dataIndex:'TITLE',
        	flex:1,
        	renderer: function(v,metaData,r,ri,c,store,view){
        		setTimeout(function() {
	                var row = view.getRow(r);
	                if(row){
		                var el = Ext.fly(Ext.fly(row).query('.x-grid-cell')[c]).down('div');
		                var cellWidth = el.getWidth();
		                var wraps = el.query('.d-wrap',false);
		                Ext.each(wraps,function(w,i){
		                	w.setWidth(cellWidth-50);
		                });
	                }
	            }, 50);
        		var s = r.get('IS_TOP')=='1'?'<i class="iconfont orange-color" data-qtip="精彩推荐" style="display:block;margin-top:8px;">&#xe605;</i> ':'';
        		var h = [];
        		h.push('<div style="position:absolute;top:10px;right:0px;padding-left:6px;width:30px;border-left:1px dashed #999;">');
	        	h.push('<i class="iconfont info" data-qtip="点击查看线路详情" style="color:#427fed;cursor:pointer;display:block;">&#xe635;</i>');
	        	h.push(s);
	        	h.push('</div>');
	        	h.push('<a class="f14 d-wrap" href="javascript:;" style="line-height:20px;">'+v+'</a>');
        		h.push('<div class="ht20"><span class="money-color f16"><dfn>￥</dfn>'+util.format.number(r.get('ROUTE_WAP_PRICE')||0,'0.00')+'</span></div>');
        		return h.join('');
        	}
        },{
        	text:'最近日期/价格',
        	width:120,
	        dataIndex: 'SUM_PRICE',
	        renderer:function(v,c,r){
	        	var re = [],isFullPrice = r.get('IS_FULL_PRICE')||'0';
	        	
	        	/*re.push('<div style="position:absolute;top:10px;right:0px;padding-left:6px;width:30px;border-left:1px dashed #999;">');
	        	
	        	if(r.get('RQ')){
	        		re.push('<i class="iconfont pp disable-color f20" style="margin-bottom:8px;display:block;" data-qtip=\'同行价：'+util.moneyFormat(r.get('SUM_INTER_PRICE'),'f16')+'\'>&#xe65c;</i>');
	        		if(isFullPrice=='1'){
	        			re.push('<i class="iconfont pp f20" data-qtip="打包价详情" style="font-weight:bold;display:block;color:#427fed;cursor:pointer;margin-bottom:8px;">&#xe658;</i>');
	        		}else{
	        			re.push('<i class="iconfont pp f20" data-qtip="综合报价详情" style="display:block;color:#427fed;cursor:pointer;margin-bottom:8px;">&#xe65f;</i>');
	        		}
	        	}else{
	        		re.push('<i class="iconfont pp disable-color f20" style="margin-bottom:8px;display:block;" data-qtip=\''+util.moneyFormat(r.get('ROUTE_INTER_PRICE'),'f14 blue-color')+'\'>&#xe65c;</i>');
	        	}
	        	if(isFullPrice=='0'){
	        		re.push('<i class="iconfont dd f20" data-qtip="地接报价详情" style="display:block;color:#427fed;cursor:pointer;margin-bottom:8px;">&#xe65d;</i>');
	        	}
	        	re.push('<i class="iconfont dfc disable-color f20" data-qtip="单房差：<br>外卖：<span class=\'money-color f16\'><dfn>￥</dfn>'+r.get('RETAIL_SINGLE_ROOM')+'</span><br>同行：<span class=\'money-color f14\'><dfn>￥</dfn>'+r.get('INTER_SINGLE_ROOM')+'</span>" style="display:block;">&#xe65e;</i>');
	        	re.push('</div>');
	        	*/
	        	if(r.get('RQ')){
	        		re.push('<div class="ht20 f14" style="margin-bottom:5px;">'+(r.get('RQ')||'')+'</div>');
	        		re.push('<div class="ht20">'+util.moneyFormat(v)+'</div>');
	        	}else{
	        		re.push('<div class="ht20">'+util.moneyFormat(r.get('ROUTE_PRICE'))+'</div>');
	        		
	        	}
	        	return re.join('');
	        }
        },{
        	width:150,
        	text:'属性/主题',
        	dataIndex:'THEMES',
        	renderer:function(v,c,r){
        		var arr = ['','周边','国内','出境'],
	        		cls = ['d-tag-teal','d-tag-green','d-tag-blue','d-tag-red'],vstr='';
        		v = v || '';
        		if(v!=''){
        		var vs = v.split(',');
        		Ext.each(vs,function(s,i){
        			if(i%2==0){
        				vstr+='<p class="ht22">';
        			}
        			vstr+='<span class="d-tag-radius">'+s+'</span>';
        			if(i%2!=0){
        				vstr+='</p>';
        			}
        		});
        		}
	        	return [
	        		'<div class="ht25 f14" ><a class="d-tag '+cls[r.get('TYPE')]+' d-tag-noarrow">'+arr[r.get('TYPE')]+'游</a></div>',
	        		'<div style="margin-top:5px;">'+vstr+'</div>'
	        	].join('');
	        }
        }];
		docked = [{
        	xtype:'toolbar',
        	style:'padding-left:5px;',
            overflowHandler: 'menu',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
        }];
		bbar = util.createGridBbar(store);
		grid = Ext.create('Ext.grid.Panel',{
			itemId:'basegrid',
			title:'线路审核',
			bbar:bbar,
			columns:columns,
			store:store,
			columnLines: true,
			selType:'checkboxmodel',
			selModel:{mode:'MULTI'},
			dockedItems:docked
		});
		items.push(grid);
		items.push({
			title:'推荐到首页',
			xtype:'uxiframe',
			style:'width:100%;height:100%;',
			src:cfg.getCtx()+'/b2c/category'
		});
		this.items = items;
		this.callParent();
	}
});