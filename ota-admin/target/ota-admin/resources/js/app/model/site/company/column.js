Ext.define('app.model.site.company.column', {
	requires:['Ext.ux.grid.ActionGlyphColumn'],
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:35}),
		{
	        text: '登录名',
	        width:100,
	        dataIndex: 'USER_NAME'
	    },{
	        text: '公司名称',
	        flex:1,
	        dataIndex: 'COMPANY',
	        renderer: function(value,c,r){
	        	/*var urlObjs = [],
	        		la = r.get('LICENSE_ATTR')||'',
	        		ba = r.get('BUSINESS_URL')||'',
	        		loa = r.get('LOGO_URL')||'';
	        	if(la!=''){
	        		urlObjs.push("{title:\\\'许可证号\\\',url:\\\'"+la+"\\\'}");
	        	}
	        	if(ba!=''){
	        		urlObjs.push("{title:\\\'营业执照\\\',url:\\\'"+ba+"\\\'}");
	        	}
	        	if(loa!=''){
	        		urlObjs.push("{title:\\\'形象图\\\',url:\\\'"+loa+"\\\'}");
	        	}
	        	return '<i class="icon-attach blue-color hand" onclick="util.showAndDownAttachment(\''+urlObjs.join(',')+'\')" data-qtip="下载附件"></i>'+value;*/
	        	return value;
	        }
	    },{
	        text: '公司代码',
	        width:120,
	        dataIndex: 'CODE',
	        renderer: function(value){
	        	return value;
	        }
	    },{
	        text: '许可证号',
	        width:150,
	        dataIndex: 'LICENSE_NO'
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
            	iconCls:'iconfont blue-color',
                tooltip: '公司详情',
                scope: this,
                text:'&#xe635;',
                handler: function(grid, rowIndex, colIndex){
                	var rec = grid.getStore().getAt(rowIndex),
                		tpl = new Ext.XTemplate(
        				'<div class="p-detail" >',
            		    
            		    '<div class="l">',
            		    '<img src="'+cfg.getPicCtx()+'/{LOGO_URL}" class="logo"  onerror="javascript:this.src=\''+cfg.getImgPath()+"/nologo.png"+'\'"/>',
            		    '<div class="title">',
            		    '<div class="name">{COMPANY}</div>',
            		    '<div class="desc">{LEGAL_PERSON}</div>',
            		    '<div class="desc f12 orange-color">对账方式：{[this.way(values.ACCOUNT_WAY)]}</div>',
            		    '</div>',
            		    '</div>',
            		    '<div style="padding:10px 0 0 170px;">',
            		    '<tpl if="this.isNull(LICENSE_ATTR)">',
					    '<a href="'+cfg.getPicCtx()+'/{LICENSE_ATTR}" target="_blank" style="margin-right:10px;">下载许可证号附件</a>',
					    '<tpl else>',
			            '<span class="disable-color" style="margin-right:10px;">未上传许可证号</span>',
			            '</tpl>',
			            '<tpl if="this.isNull(BUSINESS_URL)">',
					    '<a href="'+cfg.getPicCtx()+'/{BUSINESS_URL}" target="_blank" style="margin-right:10px;">下载营业执照附件</a>',
					    '<tpl else>',
			            '<span class="disable-color" style="margin-right:10px;">未上传营业执照</span>',
			            '</tpl>',
			            '<tpl if="this.isNull(WORD_TPL)">',
					    '<a href="'+cfg.getPicCtx()+'/{WORD_TPL}" target="_blank" style="margin-right:10px;">下载线路模版</a>',
					    '<tpl else>',
			            '<span class="disable-color" style="margin-right:10px;">未上传线路模版</span>',
			            '</tpl>',
            		    '</div>',
            		    
            		    '<ul>',
            		    '<li><label>许可证号</label>{LICENSE_NO}</li>',
            		    '<li><label>品牌名称</label>{BRAND_NAME}</li>',
            		    '<li><label>公司代码</label>{CODE}</li>',
            		    '<li><label>地址</label>{ADDRESS}</li>',
            		    '<li><label>座机</label>{PHONE}</li>',
            		    '<li><label>经营范围</label>{BUSINESS}</li>',
            		    '</ul>',
            		    '</div>'
					    ,{
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
						width:620,
						height:380,
						modal:true,
						draggable:false,
						resizable:false,
			   			layout:'fit',
			   			items:[{
			   				bodyStyle:'background:#f8f8f8;',
			   				bodyPadding:10,
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