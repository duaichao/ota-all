Ext.define('app.view.site.company', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	region:'north',
    	height:'50%',
    	itemId:'basegrid',
    	style:'border-bottom:1px solid #c1c1c1!important;',
    	xtype:'childcompanygrid'
    },{
    	region:'west',
    	style:'border-right:1px solid #c1c1c1!important;',
    	width:305,
    	minWidth:305,
        xtype: 'departgrid'
    },{
    	region:'center',
        xtype: 'employeegrid'
    }]
});
/*
Ext.define('app.view.site.company', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	region:'center',
    	itemId:'basegrid',
    	style:'border-right:1px solid #c1c1c1!important;',
    	xtype:'childcompanygrid'
    },{
    	region:'east',
    	style:'border-right:1px solid #c1c1c1!important;',
    	width:400,
    	minWidth:400,
        xtype: 'departgrid'
    }]
});*/
/*Ext.define('app.view.site.company', {
    extend: 'Ext.container.Viewport',
    layout:'fit',
    initComponent: function() {
    	var companyStore = util.createGridStore(cfg.getCtx()+'/site/company/listCompanyChild',Ext.create('app.model.site.company.model'),'1',20),
    		companyTools = util.createGridBbar(companyStore),
    		companyListPanel = Ext.create('Ext.panel.Panel',{
    			style:'border-right:1px solid #c1c1c1!important;',
    			scrollable  : false,
    			itemId:'companyPanel',
    			bbar:companyTools,
    			dockedItems:[{
    				xtype: 'toolbar',
    		        dock: 'top',
    		        style:'border-bottom:1px solid #e1e1e1!important;',
    		        itemId:'companytool'
    			}],
    			layout:'fit',
    			items:[{
    				style:'border-bottom:1px solid #c1c1c1!important;',
    				xtype:'dataview',
    	        	tpl  : Ext.create('Ext.XTemplate',
    	                '<tpl for=".">',
    	                    '<div class="block-item">',
    	                        '<img width="64" height="64" src="'+cfg.getPicCtx()+'/{LOGO_URL}" onerror="javascript:this.src=\''+cfg.getImgPath()+"/nologo.png"+'\'"/>',
    	                        '<tpl if="this.isParent(PID)">',
    	                        '<strong class="blue-color">{COMPANY}</strong>',
    	                        '<tpl else>',
    	                        '<strong>{COMPANY}</strong>',
    	                        '</tpl>',
    	                        '<span>{PHONE}</span>',
    	                        '<div class="tags">{TYPE:this.getMyType}</div>',
    	                        '<div class="info"><i class="iconfont blue-color">&#xe635;</i></div>',
    	                    '</div>',
    	                '</tpl>',
    	                {
    	        			isParent:function(pid){
    	        				return pid == '-1';
    	        			},getMyType:function(value){
    	        				if(value==''){
    	        	        		return '';
    	        	        	}
    	        	        	if(value==0){
    	        	        		return '平台管理';
    	        	        	}
    	        	        	if(value==1){
    	        	        		return '供应商';
    	        	        	}
    	        	        	if(value==2){
    	        	        		return '旅行社';
    	        	        	}
    	        	        	if(value==3){
    	        	        		return '门市';
    	        	        	}
    	        	        	if(value==4){
    	        	        		return '分公司';
    	        	        	}
    	        	        	if(value==5){
    	        	        		return '同行';
    	        	        	}
    	        	        	if(value==6){
    	        	        		return '商务中心';
    	        	        	}
    	        	        	if(value==7){
    	        	        		return '子公司';
    	        	        	}
    	        			}
    	                }
    	            ),
    	            store:companyStore,
    	            itemSelector: 'div.block-item',
    	            overItemCls : 'block-item-hover',
    	            multiSelect : false,
    	            scrollable  : true
    			}]
    		});
    	
    	this.items = [companyListPanel];
    	
        this.callParent(arguments);
        
        
        
        var companyView = companyListPanel.down('dataview');
        
        this.menu = Ext.create('Ext.tip.ToolTip',{
        	width: 620,
            maxWidth: 800,
            shadow:false,
            autoHide:false,
            align: 't-b?',
            style:'background:#fff;border:4px solid #eee;',
            bodyStyle:'padding:10px;',
            tpl: new Ext.XTemplate(
    		    '<div class="p-detail" >',
    		    
    		    '<div class="l">',
    		    '<img src="'+cfg.getPicCtx()+'/{LOGO_URL}" class="logo"  onerror="javascript:this.src=\''+cfg.getImgPath()+"/nologo.png"+'\'"/>',
    		    '<div class="title">',
    		    '<div class="name">{COMPANY}</div>',
    		    '<div class="desc">{LEGAL_PERSON}</div>',
    		    '<div class="desc f12 f999">登录名：{USER_NAME}</div>',
    		    '</div>',
    		    '</div>',
    		    
    		    '<div style="padding:10px 0 0 170px;">',
    		    '<a href="javascript:;" class="x-btn blue" style="padding:8px 12px;color:#fff;">部门员工</a> ',
    		    '<a href="'+cfg.getPicCtx()+'/{LICENSE_ATTR}" class="x-btn" style="padding:8px 2px;" target="_blank">下载许可证号附件</a> ',
    		    '<a href="'+cfg.getPicCtx()+'/{BUSINESS_URL}" class="x-btn" style="padding:8px 2px;" target="_blank">下载营业执照附件</a>',
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
    		),dockedItems:[{
    			xtype:'toolbar',
    			dock:'bottom',
    			overflowHandler:'menu',
    			style:'padding:5px 0!important;background:#eee;',
    			itemId:'tools'
    		}]
        });
        this.menu.show();
        this.menu.hide();
        this.showTimeout = null;
        this.hideTimeout = null;
        companyView.on('itemmouseenter',function(dv, record, item, index, e){
        		this.deferShowMenu(item,record);e.preventDefault();
        	
        },this);
        companyView.on('itemmouseleave',function(dv, record, item, index, e){
        	this.deferHideMenu();
        },this);
        
        companyView.on('selectionchange',function(dv,rs){
        	if(rs&&rs.length>0){
        		var record = rs[0];
            	this.loadDeparts(record.get('ID'));
        	}
        	
        },this);
    },
    loadDeparts:function(id){
    	
    },
    deferShowMenu:function(item,record){
    	clearTimeout(this.showTimeout);
    	this.showTimeout = Ext.Function.defer(function() {
            this.renderMenu(record)
            clearTimeout(this.hideTimeout);
            this.menu.showBy(Ext.fly(item));
        }, 200, this)
    },
    deferHideMenu:function(){
    	clearTimeout(this.showTimeout);
        if (!this.menu) {
            return
        }
        this.hideTimeout = Ext.Function.defer(function() {
            this.menu.hide()
        }, 200, this)
    },
    renderMenu:function(record){
    	this.menu.setData(record);
        this.menu.getEl().on({
            click: function(b) {
                this.menu.hide();
                b.preventDefault()
            },
            mouseover: function() {
                clearTimeout(this.hideTimeout)
            },
            mouseout: this.deferHideMenu,
            scope: this
        });
    }
});*/