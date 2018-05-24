Ext.define('app.controller.produce.traffic', {
	extend: 'app.controller.common.BaseController',
	views:['common.CityCombo','app.view.produce.trafficGrid'],
	config: {
		control: {
			'form[itemId=queryForm] datepicker':{
			},
			'form[itemId=queryForm] button[itemId=queryBtn]':{
				click:function(btn){
					var form = this.getQueryForm(),
						values = form.getValues(false,false,false,true),
						picker = this.getQueryDatePicker(),
						pkDate = Ext.Date.format(picker.getValue(),'Y-m-d');
					values['queryDate'] = pkDate;
					this.getTrafficGrid().getStore().getProxy().setExtraParams(values);
					this.getTrafficGrid().getStore().load();
				}
			}
		},
		refs: {
			trafficGrid: 'protrafficgrid',
			queryForm:'form[itemId=queryForm]',
			queryDatePicker:'form[itemId=queryForm] datepicker'
        }
	},
	onBaseGridRender:function(g){
		var me = this;
		setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children,
		        	ubtn = [];
		        for(var i=0;i<items.length;i++){
		        	ubtn.push(items[i]);
		        }
		        
		        ubtn.push('->');
		        ubtn.push({
		        	emptyText:'供应商/产品名称',
		        	width:150,
		        	xtype:'searchfield',
		        	store:g.getStore()
		        });
		        util.createGridTbar(g,ubtn);
		    }
		});
		},500);
		
		
		g.getStore().on('load',function(){
        	var cnt = g.getStore().getCount(),
        		form = g.up().down('form'),
        		rag = form.down('radiogroup'),
        		dp = form.down('datepicker');
        	var h = [
       			'为你找到 ',
       			'<span class="f18 green-color">'+cnt+'</span>',
       			' 个',
       			' <span class="f18 green-color">'+Ext.Date.format(dp.getValue(),'Y/m/d')+'</span>',
       			' 出发的',
       			''+(rag.getValue().isSingle=='0'?'单程':'往返')+'',
       			'交通'
       		].join('')	
        	var pb = g.down('pagingtoolbar'),
        		displayItem = pb.child('#displayItem');
        		displayItem.setText(h);
        	return;
        });
	}
});