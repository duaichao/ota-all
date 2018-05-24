Ext.define('app.model.resource.route.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{
			width:35
		}),{
			text: '线路图片',
        	dataIndex:'FACE',
        	width:145,
	        renderer: function(v,metaData,r,colIndex,store,view){
	        	v = cfg.getPicCtx()+'/'+v;//util.pathImage(v,'125X70');
	        	var /*arr = ['','周边','国内','出境'],
	        		cls = ['#427fed','#53a93f','#427fed','#dd4b39'],*/
	        	 	h = [
		        		'<img src="'+v+'" width="125px" height="70px" style="border:1px solid #ddd;" onerror="javascript:this.src=\''+cfg.getImgPath()+"/noimage.jpg"+'\'"/>'
		        	];
		        h.push('<div class="ht16" style="color:#666;padding:5px 0;">编号: <a href="javascript:;">'+r.get('NO')+'</a></div>');
	        	return h.join('');
	        }
		},{
        	text: '线路名称',
        	dataIndex:'TITLE',
        	//width:550,
        	flex:1,
	        renderer: function(v,metaData,r,ri,c,store,view){
	        	var isRecommend = parseInt(r.get('WEB_RECOMMEND_CNT')||0);
		        //if(isRecommend>0){
		        //	h.push( '<i class="iconfont f18" style="color:#E64A19;" data-qtip="已推荐到B2C首页">&#xe605;</i> ');
		        //}
	        	setTimeout(function() {
	                var row = view.getRow(r);
	                if(row){
	                	
	                	//是否推荐到B2c
	                	var rowEl = Ext.fly(row),
	                		tb = rowEl.up('table');
	                	if(isRecommend>0){
		                	tb.setStyle({
		                		'position':'relative'
		                	});
			                tb.appendChild({
			                	tag:'i',
			                	cls:'iconfont',
			                	'data-qtip':'已推荐到B2C首页',
			                	html:'&#xe6a6;',
			                	style:'font-size:45px;position:absolute;top:11px;left:-4px;color:#E64A19;'
			                });
	                	}
	                	
	                //Capturing the el (I need the div to do the trick)
	                var el = Ext.fly(rowEl.query('.x-grid-cell')[c]).down('div');
	                
	                var cellWidth = el.getWidth();
	                var wraps = el.query('.d-wrap',false);
	                Ext.each(wraps,function(w,i){
	                	w.setWidth(cellWidth-20);
	                });
	                }
	                //All other codes to build the progress bar
	            }, 50);
	        	var h = [];
	        	/*h.push('<div style="position:absolute;top:10px;right:0px;padding-left:6px;width:30px;border-left:1px dashed #999;">');
	        	h.push('<i class="iconfont info" data-qtip="点击查看线路详情" style="color:#427fed;cursor:pointer;display:block;">&#xe635;</i>');
	        	if(r.get('IS_SHARE')=='1'){
	        		h.push('<i class="iconfont" data-qtip="共享线路" style="color:#999;display:block;margin-top:8px;">&#xe640;</i>');
	        	}
	        	if(r.get('IS_GRANT')=='1'){
	        		h.push('<i class="iconfont bold f18" data-qtip="指定线路" style="color:#999;display:block;margin-top:8px;">&#xe645;</i>');
	        	}
	        	if(r.get('IS_SINGLE_PUB')=='1'){
	        		h.push('<i class="iconfont f20" data-qtip="不含交通<br>'+(r.get('SINGLE_REMARK')||'')+'" style="color:#999;display:block;margin-top:10px;">&#xe657;</i>');
	        	}
	        	h.push('</div>');*/
	        	var　u = r.get('SOURCE_URL')||'';
	        	if(u!=''){
	        		h.push('<a href="'+cfg.getPicCtx()+'/'+r.get('SOURCE_URL')+'" target="_blank"><i class="iconfont" data-qtip="下载源行程" >&#xe648;</i></a> ');
	        	}
        		h.push('<a class="f16 title d-wrap" href="javascript:;" style="display:inline-block;line-height:24px;padding:5px 0 10px 0;">');
        		h.push(v);
        		h.push('</a>');
        		
        		
        		//标签
        		h.push('<div class="d-wrap product-tag">');
        		if(r.get('BRAND_NAME')){
        			h.push('<span class="flash flash-success"><i  class="iconfont f16" style="top:0px;">&#xe6b2;</i> '+r.get('BRAND_NAME')+'</span>');
        		}
        		
        		if(r.get('TYPE')){
        			var routArrs = ['','周边','国内','出境'];
        			h.push('<span class="flash">'+routArrs[r.get('TYPE')]+'游</span>');
        		}
        		/*if(r.get('ATTR')){
        			var routArrs = ['','周边','国内','出境'];
        			h.push('<span class="flash" style="padding:2px 5px;margin-right:3px;background:'+cfg.getRouteAttrTagColor()[r.get('ATTR')]+';color:#fff;">'+r.get('ATTR')+'</span>');
        		}*/
        		if(r.get('IS_SHARE')=='1'){
	        		h.push('<span class="flash"><i class="iconfont f14" style="top:0px;" data-qtip="共享线路">&#xe640;</i> 共享线路</span>');
	        	}
        		if(r.get('IS_GRANT')=='1'){
        			h.push('<span class="flash"><i class="iconfont f16 bold" style="top:0px;" data-qtip="指定线路">&#xe645;</i> 指定线路</span>');
	        	}
        		if(r.get('IS_SINGLE_PUB')=='1'){
	        		h.push('<span class="flash"><i class="iconfont f18" data-qtip="不含交通<br>'+(r.get('SINGLE_REMARK')||'')+'" style="top:0px;">&#xe657;</i> 不含交通</span>');
	        	}
        		if(r.get('DAY_COUNT')){
        			h.push('<span class="flash flash-gray">'+r.get('DAY_COUNT')+'日游</span>');
        		}
        		h.push('</div>');
        		
        		
        		if(r.get('DISCOUNT_INFO')){
        			var str=r.get('DISCOUNT_INFO'),
        				reg = /\*/g,
        				len = str.match(reg).length;
        			for(var i=0;i<len;i++){
        				str = str.replace('*',i+1);
        			}
        			h.push('<div class="d-wrap d-wrap-block d-wrap-block-yh l-c">');
        			h.push('<span class="d-wrap-block-title c-c" ><i class="iconfont f24">&#xe609;</i></span>');
        			h.push('<span  class="d-wrap-block-content" >'+str+'</span>');
        			h.push('</div>');
        		}
        		h.push('<div style="padding:10px;line-height:20px;">');
        		//h.push('<span class="d-wrap-block-title" style="background:#9E9E9E;">产品推荐</span>');
        		h.push('<span>'+r.get('FEATURE')+'</span>');
    			h.push('</div>');
        		return h.join('');
	        }
        },{
        	width:150,
        	text:'主题属性',
        	dataIndex:'THEMES',
        	renderer:function(v,c,r){
        		var arr = ['','周边','国内','出境'],
	        		cls = ['d-tag-teal','d-tag-green','d-tag-blue','d-tag-red'],vstr='',dc = r.get('DAY_COUNT') || '',attr = r.get('ATTR') || '';
        		v = v || '';
        		if(v!=''){
        		var vs = v.split(',');
        		Ext.each(vs,function(s,i){
        			if(i%2==0){
        				vstr+='<p style="line-height:25px;">';
        			}
        			vstr+='<span class="d-tag-radius">'+s+'</span>';
        			if(i%2!=0){
        				vstr+='</p>';
        			}
        		});
        		}
        		var h=[
        		      '<div class="ht30">' /*<a class="d-tag '+cls[r.get('TYPE')]+' d-tag-noarrow">'+arr[r.get('TYPE')]+'游</a>'*/
        		];
        		dc = dc==''?'':dc+'日';
		        if(attr!=''){
		        	h.push('<span class="flash" style="background:'+cfg.getRouteAttrTagColor()[attr]+';border-radius:2px;padding:4px 5px;color:#fff;">'+attr+'</span>');
		        }
		       /* if(dc){
		        	h.push('<span style="background:#9e9e9e; color:#fff;margin-left:5px;padding:2px 4px;border-radius: 2px;">'+dc+'</span>');
		        }*/
		        h.push('</div>');
		        h.push('<div style="margin-top:5px;">'+vstr+'</div>');
	        	return h.join('');
	        }
        },{
        	width:150,
        	text:'出发/目的地',
        	dataIndex: 'BEGIN_CITY',
        	renderer:function(v,c,r){
        		var end = r.get('END_CITY') ||'',startStr='',endStr='';
        		v = v || '';
        		if(v!=''){
            		var vs = v.split(',');
            		Ext.each(vs,function(s,i){
            			startStr+='<span class="d-tag-radius"  style="float:left;margin-bottom:2px;">'+s+'</span>';
            		});
            	}
        		if(end!=''){
            		var vs = end.split(',');
            		Ext.each(vs,function(s,i){
            			endStr+='<span class="d-tag-radius" style="float:left;margin-bottom:2px;">'+s+'</span>';
            		});
            	}
	        	return [
	        		'<div class="ht20">'+startStr+'</div>',
	        		'<div style="clear:both;line-height:22px;padding-left:15px;"><i class="iconfont f18" style="color:#bbb;">&#xe64a;</i></div>',
	        		'<div>'+endStr+'</div>'
	        	].join('');
	        }
        },{
        	text:'最近日期/价格',
        	width:120,
	        dataIndex: 'SUM_PRICE',
	        renderer:function(v,c,r){
	        	var re = [],isFullPrice = r.get('IS_FULL_PRICE')||'0',isEarnest = r.get('IS_EARNEST')||'0';
	        	
	        	
	        	re.push('<div style="position:absolute;top:10px;right:0px;padding-left:6px;width:30px;border-left:1px dashed #999;">');
	        	if(isFullPrice=='0'){
	        		re.push('<i class="iconfont dd f20"  data-qtip="地接报价详情" style="display:block;color:#427fed;cursor:pointer;margin-bottom:8px;">&#xe65d;</i>');
	        	}
	        	if(r.get('RQ')){
	        		if(isFullPrice=='1'){
	        			re.push('<i class="iconfont pp f20" data-qtip="打包价详情" style="display:block;font-weight:bold;display:block;color:#427fed;cursor:pointer;margin-bottom:8px;">&#xe658;</i>');
	        		}else{
	        			re.push('<i class="iconfont pp f20" data-qtip="综合报价详情" style="display:block;color:#427fed;cursor:pointer;margin-bottom:8px;">&#xe65f;</i>');
	        		}
	        	}
	        	re.push('<i class="iconfont dfc f20" data-qtip="单房差：<br>外卖：<span class=\'light-orange-color f16\'><dfn>￥</dfn>'+r.get('RETAIL_SINGLE_ROOM')+'</span><br>同行：<span class=\'light-blue-color f14\'><dfn>￥</dfn>'+r.get('INTER_SINGLE_ROOM')+'</span>" style="display:block;color:#999;">&#xe65e;</i>');
	        	re.push('</div>');
	        	
	        	if(r.get('RQ')){
	        		re.push('<div class="ht20 f14" style="margin-bottom:5px;">'+(r.get('RQ')||'')+'</div>');
	        		re.push('<div class="ht20">'+util.moneyFormat(v)+'</div>');
	        		re.push('<div class="ht20">'+util.moneyFormat(r.get('SUM_INTER_PRICE'),'f14')+'</div>');
	        	}else{
	        		re.push('<div class="ht20">'+util.moneyFormat(r.get('ROUTE_PRICE'))+'</div>');
	        		re.push('<div class="ht20">'+util.moneyFormat(r.get('ROUTE_INTER_PRICE'),'f14')+'</div>');
	        	}
	        	if(isEarnest=='1'){
	        		var earnestInter = util.moneyFormat(r.get('EARNEST_INTER'),'f14 light-blue-color'),
	        			earnestRetail = util.moneyFormat(r.get('EARNEST_RETAIL'),'f14 light-orange-color'),
	        			earnestType = r.get('EARNEST_TYPE');
	        		
	        		re.push('<div style="padding-top:5px;" data-qtip=\'此产品支持定金预付，最低出团费用同行价/人：'+earnestInter+'，外卖价/人:'+earnestRetail+'\'>');
	        		re.push(' <span style="border-radius:2px;font-size:12px;color:#fff;background:#9C27B0;padding:2px 4px;">定</span>');
	        		if(earnestType=='1'){
	        			re.push(' <span style="border-radius:2px;font-size:12px;color:#fff;background:#00796b;padding:2px 4px;" data-qtip=\'代收款定金预付订单，需要供应商确认\' >代</span>');
	        		}
	        		re.push('</div>');
	        	}
	        	return re.join('');
	        }
        },{
        	text: '进度',
	        width:100,
	        dataIndex: 'STATUS',
	        renderer:function(v,c,r){
	        	var isTraffic = parseInt(r.get('ROUTE_TRAFFIC_CNT')),//配交通
	        		isBase = parseInt(r.get('BASE_PRICE_CNT')),//配地接
	        		isSingleSale = parseInt(r.get('IS_SINGLE_PUB')),//是否单卖地接
	        		isPub = parseInt(r.get('IS_PUB')),
	        		txt = ['待配置地接价','待配置交通','待发布','已发布','已停售','单卖地接'],ov=0,cv=0;
	        	
	        	v = 0;
	        	if(isPub==1){
	        		v = 100;
	        		ov = 3;
	        	}else if(isPub==0){
	        		if(isSingleSale==1){
		        		ov = 5;
		        		if(isBase>0){
		        			v = 50;
		        			ov = 2;
		        		}
		        	}else{
		        		if(isBase>0){
		        			v = 30;
		        			ov = 1;
		        		}
		        		if(isTraffic>0){
		        			v = 50;
		        			ov = 2;
		        		}
	        		}
	        	}else{
	        		v = 100;
	        		ov = 4;
	        		cv=1;
	        	}
	        	
	        	var v1 = v > 100 ? 100 : v,
	        		v2 = v==100?'#53a93f':'#FFB90F',
	        		v3 = v==100?'#fff':'#888';  
	        	if(cv==1){
	        		v2='#dd4b39';
	        	}
		        v1 = v1 < 0 ? 0 : v1;  
		        
		        
	            return '<div style="padding:5px 0 10px 0;text-align:center;">'+txt[ov]+'</div>'+Ext.String  
	                    .format(  
	                     '<div style="height:24px;">'  
	                           + '<div style="float:left;border:1px solid {2};height:20px;width:100%;">'  
	                           + '<div style="float:left;text-align:center;line-height:16px;width:100%;color:{3};">{0}%</div>'  
	                           + '<div style="background: {2};width:{1}%;height:18px;"></div>'  
	                           + '</div></div>', v, v1, v2,v3);
	                           ; 
	        }
        }];
	}
});