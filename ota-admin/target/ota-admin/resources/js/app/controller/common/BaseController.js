Ext.define('app.controller.common.BaseController', {
	extend: 'Ext.app.Controller',
	requires: [
        'Ext.ux.PagingToolbarResizer',
        'app.vtype.CommonVtype'
    ],
    views:[
    	'Ext.ux.form.SearchField'
    ],
	config: {
		control: {
			'gridpanel[itemId=basegrid]': {
	             afterrender: 'onBaseGridRender'
	         },
	         'menuitem[itemId=refresh]':{
	         	click:function(){
	         		document.location.reload();
	         	}
	         },
	         'gridpanel button':{
	         	render:function(button) {  
		            button.dropZone = new Ext.dd.DropZone(button.getEl(), {  
	                    ddGroup : 'DD_grid_Global' ,  
	                    getTargetFromEvent : function(e) {  
	                        return e.getTarget('');  
	                    },  
	                    onNodeOver : function(target, dd, e, data) {  
	                        return Ext.dd.DropZone.prototype.dropAllowed;  
	                    },  
	                    onNodeDrop : function(target, dd, e, data) {  
	                    	if(button.isDisabled()){}else{
	                        	button.fireEvent('click', button);
	                        }
	                    }  
		            })  
		        }  
	         },'button[itemId=moreQuery]':{
				click:function(btn){
					var condit = this.getConditPanel();
					if(condit){
						if(condit.isHidden()){
							condit.show();
						}else{
							condit.hide();
						}
					}
				}
			},'form[itemId=baseCondit] button[itemId=conditQueryBtn]':{
				click:function(btn){
					var condit = this.getConditPanel(),
						grid = this.getBasegrid(),
						store = grid.getStore();
						var vs = condit.getValues(false,false,false,true);
						Ext.applyIf(vs,store.getProxy().getExtraParams());
						store.getProxy().setExtraParams(vs);
						store.load();
				}
			},'form[itemId=baseCondit] button[itemId=conditResetBtn]':{
				click:function(btn){
					var condit = this.getConditPanel(),
						grid = this.getBasegrid(),
						store = grid.getStore();
					condit.reset();
					var vs = condit.getValues(false,false,false,true);
					Ext.applyIf(vs,store.getProxy().getExtraParams());
					store.getProxy().setExtraParams(vs);
					store.load();
					condit.hide();
				}
			},'button[itemId=printResult]':{
				click:function(){
					Ext.ux.grid.Printer.printAutomatically = false;
	            	Ext.ux.grid.Printer.print(this.getBasegrid());
				}
			}
		},
		refs: {
			baseView:'viewport',
            basegrid: 'gridpanel[itemId=basegrid]',
            conditPanel:'form[itemId=baseCondit]'
        }
	},
	onBaseGridRender:function(g){
		var me = this;
		setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children;
		        items.push('->');
				items.push({
					emptyText:'搜索关键字',
		        	xtype:'searchfield',
		        	store:g.getStore()
				});
		        util.createGridTbar(g,items);
		    }
		});
		},500);
	}
});