Ext.define('Weidian.view.product.route.Days', {
    extend: 'Ext.DataView',
    xtype: 'routedays',

    cls: 'timeline-items-wrap',

    scrollable: false,


    itemSelector: '.timeline-item',
    
    itemTpl: [
        '<tpl for="days">',
        '<div class="timeline-item{userId:this.cls(values,parent[xindex-2],xindex-1,xcount)}">' +
            
            '<div class="profile-pic-wrap">' +
                '<span class="x-fm fm-marker"></span>' +
                '<div class="date">{NO}</div>' +
            '</div>' +
            '<div class="line-wrap">' +
                '<div class="contents-wrap">' +
                    '<div class="job-meeting"><a href="#">'+
                    '<tpl if="this.styleTitle(values.TITLE)">'+
    				'{TITLE}&nbsp;'+
    				'<tpl else>'+
    				'{BEGIN_CITY} '+
    				'{[this.fmTools(values.TOOL)]} '+
    				'{END_CITY} '+
    				'{[this.fmTools(values.TOOL1)]} '+
    				'{END_CITY1}&nbsp;',
    				'</tpl>'+
                    '</a></div>' +
                    //用餐
					'<div class="repast item">'+
						'<div class="x-fm fm-repast">'+
						'早：{[this.emptyText(values.BREAKFAST)]}</br>'+
						'中：{[this.emptyText(values.LUNCH)]}</br>'+
						'晚：{[this.emptyText(values.DINNER)]}</br></div>'+
					'</div>'+
					//当日提示
    				'<div class="tips item">'+
    					'<div class="x-fm fm-tips">{TODAY_TIPS}</div>'+
    				'</div>'+
    				//住宿酒店
    				'<div class="hotel item">'+
	    				'<div class="x-fm fm-hotel">{HOTEL_TIPS}</div>'+
    				'</div>'+
    				//自费购物
    				'<div class="shop item">'+
	    				'<div class="x-fm fm-shopping">{PAY_TIPS}</div>'+
    				'</div>',
    				//安排
	    			'<tpl for="details">'+
	    				'<div class="plan item">'+
	    					'<span class="time">{TITLE}</span>'+
	    					'<div class="content x-fm fm-time">{[this.fmText(values.CONTENT)]}</div>'+
	    					//景点
	    					//'<div class="x-fm fm-scenic">'+
	    					//'<tpl for="scenics">'+
	    					//'<a class="tag" style="padding:0px;background:none;" href="javascript:;">{SCENIC_NAME}</a>'+
	    					//'</tpl>'+
	    					//'</div>'+
	    				'</div>'+
	    			'</tpl>'+
                '</div>' +
            '</div>' +
        '</div>'+
        '</tpl>',
        {
        	styleTitle :function(v){
    			v = v ||'';
    			return v !='';
    		},
    		emptyText:function(v){
    			if(!v||v==''){
    				return '无';
    			}else{return v;}
    		},
    		fmTools :function(v){
    			v = v ||'';
    			if(v=='')return '';
    			var key = {'其他':'fm-road','飞机':'fm-air','火车':'fm-tram','动车':'fm-train','高铁':'fm-train','旅游车':'fm-bus','轮船':'fm-ferry'};
    			return '<span class="f18 x-fm '+key[v]+'"></span>';
    		},
    		fmText :function(html){
    			return util.fmWordText(html,true);
    		},
            cls: function (value, record, previous, index, count) {
                var cls = '';
                if (!index) {
                    cls += ' timeline-item-first';
                }
                if (index + 1 === count) {
                    cls += ' timeline-item-last';
                }

                return cls;
            }
        }
    ]
});
