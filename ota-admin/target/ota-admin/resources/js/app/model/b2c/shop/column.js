Ext.define('app.model.b2c.shop.column', {
	requires:['Ext.ux.grid.ActionGlyphColumn'],
	constructor:function(){
		return [
		/*Ext.create('Ext.grid.RowNumberer',{width:35}),*/
		{
	        text: '排序',
	        width:60,
	        dataIndex: 'ORDER_BY'
	    },{
	        text: '实体店名称',
	        flex:1,
	        dataIndex: 'COMPANY',
	        renderer: function(value,c,r){
	        	//IS_SHOW
	        	var show = '';
	        	if(r.get('IS_SHOW')=='1'){
	        		show = '<i class="iconfont orange-color f18" data-qtip="推荐">&#xe644;</i> ';
	        	}
	        	return '<div style="line-height:20px;" class="f14 blue-color">'+show+value+'</div>';
	        }
	    },{
	        text: '简称',
	        width:200,
	        dataIndex: 'SHORT_NAME',
	        renderer: function(value,c,r){
	        	return value;
	        }
	    },{
	        text: '座机',
	        width:100,
	        dataIndex: 'PHONE',
	        renderer: function(value,c,r){
	        	return value;
	        }
	    },{
	        text: '地址',
	        width:300,
	        dataIndex: 'ADDRESS',
	        renderer: function(value,c,r){
	        	return value;
	        }
	    },{
	        text: '状态',
	        width:80,
	        dataIndex: 'IS_USE',
	        renderer: function(value){
	        	return value==1?'<span style="color:red">禁用</span>':'启用';
	        }
	    },
		{
	    	xtype: 'actionglyphcolumn',
            width: 30,
            align:'center',
            sortable: false,
            menuDisabled: true,
            items: [{
            	iconCls:'iconfont',
                tooltip: '公司详情',
                scope: this,
                text:'&#xe635;',
                handler: function(grid, rowIndex, colIndex){
                	var rec = grid.getStore().getAt(rowIndex),
                		tpl = new Ext.XTemplate(
					    '<div class="p-detail">',
					    '<img class="logo" src="'+cfg.getPicCtx()+'/{LOGO_URL}""/>',
					    '<ul>',
					    '<li><label>公司名称</label>{COMPANY}</li>',
					    '<li><label>公司简称</label>{SHORT_NAME}</li>',
					    '<li><label>对账方式</label>{[this.way(values.ACCOUNT_WAY)]}</li>',
					    '<li><label>公司法人</label>{LEGAL_PERSON}</li>',
					    '<li><label>许可证号</label>{LICENSE_NO}</li>',
					    '<li><label>品牌名称</label>{BRAND_NAME}</li>',
					    '<li><label>地址</label>{ADDRESS}</li>',
					    '<li><label>座机</label>{PHONE}</li>',
					    '<li><label>附件</label>',
					    '<tpl if="this.isNull(LICENSE_ATTR)">',
					    '<a href="'+cfg.getPicCtx()+'/{LICENSE_ATTR}" target="_blank">下载许可证号附件</a>',
					    '<tpl else>',
			            '<span class="disable-color" style="margin-right:10px;">未上传许可证号</span>',
			            '</tpl>',
			            '<tpl if="this.isNull(BUSINESS_URL)">',
					    '<a href="'+cfg.getPicCtx()+'/{BUSINESS_URL}" target="_blank">下载营业执照附件</a>',
					    '<tpl else>',
			            '<span class="disable-color" style="margin-right:10px;">未上传营业执照</span>',
			            '</tpl>',
			            '<tpl if="this.isNull(WORD_TPL)">',
					    '<a href="'+cfg.getPicCtx()+'/{WORD_TPL}" target="_blank">下载线路模版</a>',
					    '<tpl else>',
			            '<span class="disable-color" style="margin-right:10px;">未上传线路模版</span>',
			            '</tpl>',
					    '</li>',
					    '<li><label>经营范围</label>{BUSINESS}</li>',
					    '</ul>',
					    '</div>',{
					    	isNull:function(v){
					    		v = v ||'';
					    		return v!='';
					    	},
					    	way:function(value){
					    		if(value=='1'){
					    			return '门市/分公司独立对账';
					    		}
					    		if(value=='2'){
					    			return '总部对账';
					    		}
					    		if(value=='3'){
					    			return '平台对账';
					    		}
					    	}
					    }
					);
					Ext.create('Ext.window.Window',{
						title:util.windowTitle('',''+rec.get('COMPANY'),''),
						width:600,
						height:360,
						modal:true,
						draggable:false,
						resizable:false,
			   			layout:'fit',
			   			bodyPadding:'5',
			   			items:[{
			   				autoScroll:true,
			   				tpl:tpl,
			   				data:rec.data
			   			}]
					}).show();
                }
            }]
	    }];
	}
});