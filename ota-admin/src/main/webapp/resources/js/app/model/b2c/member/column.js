Ext.define('app.model.b2c.member.column', {
	constructor:function(){
		return [
				Ext.create('Ext.grid.RowNumberer',{width:35}),
				{
					xtype: 'widgetcolumn',
		        	width:45,
		        	dataIndex:'FACE',
		        	text:'',
		        	sortable: false,
		            menuDisabled: true,
		        	widget: {
		        		xtype:'image',
		        		height:25,
		                cls:'d-img-size-auto',
		                listeners:{
		    				afterrender:function(img){
		    					var rec = img.getWidgetRecord(),face=cfg.getPicCtx()+'/'+rec.get('FACE');
		    					util.imageLoader(face,Ext.emptyFn,function(){
		    						img.setSrc(cfg.getImgPath()+'/noface.gif');
		    					});
		    					var err = 'javascript:this.src=\''+cfg.getImgPath()+'/noface.gif\'';
		    					img.getEl().set({'data-qtip':"<img class=\"d-img-size-auto\" style=\"width:90px;height:90px;\" src=\""+face+"\" onerror=\""+err+"\"/>"});
		    					img.setSrc(face);
		    				}
		    			}
		            }
				},{
			        text: '订单数',
			        width:70,
			        dataIndex: 'CNT',
			        renderer: function(value){
			        	return  '<div class="ht20 blue-color">'+value+'</div>';
			        }
			    },{
			        text: '登录名',
			        width:120,
			        dataIndex: 'USER_NAME',
			        renderer: function(value){
			        	return  '<div class="ht20">'+value+'</div>';
			        }
			    },{
			    	text: '姓名',
			        width:80,
			        dataIndex: 'CHINA_NAME',
			        renderer: function(value){
			        	return  '<div class="ht20">'+(value||'')+'</div>';
			        }
			    },{
			    	text: '生日',
			        width:80,
			        dataIndex: 'BIRTHDAY',
			        renderer: function(value){
			        	return  '<div class="ht20">'+(value||'')+'</div>';
			        }
			    },
			    {
			        text: '性别',
			        width:50,
			        dataIndex: 'SEX',
			        renderer: function(value){
			        	if(value=='1'){
			        		return '<div class="ht20">男</div>';
			        	}else 
			        	if(value=='0'){
			        		return '<div class="ht20">女</div>';
			        	}else{
			        		return '';
			        	}
			        }
			    },
			    {
			        text: '手机',
			        width:150,
			        dataIndex: 'MOBILE',
			        renderer: function(value){
			        	return '<div class="ht20">'+util.formatMobile(value)+'</div>';
			        }
			    },{
			    	text: 'QQ',
			        width:100,
			        dataIndex: 'QQ',
			        renderer: function(value){
			        	return  '<div class="ht20">'+(value||'')+'</div>';
			        }
			    },{
			    	text: '状态',
			        width:50,
			        dataIndex: 'IS_USE',
			        renderer: function(value){
			        	return  '<div class="ht20">'+(value=='1'?'<span class="red-color">禁用</span>':'<span class="green-color">启用</span>')+'</div>';
			        }
			    },{
			    	text: '注册时间',
			        flex:1,
			        dataIndex: 'REG_TIME',
			        renderer: function(value){
			        	value = value ||'';
			        	return  '<div class="ht20">'+value+'</div>';
			        }
			    }];
			}
});