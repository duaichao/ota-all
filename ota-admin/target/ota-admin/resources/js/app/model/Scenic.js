Ext.define('app.model.Scenic', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'ID',
        type: 'string'
    },{
        name: 'PID',
        type: 'string'
    },{
        name: 'PTEXT',
        type: 'string'
    },{
        name: 'TEXT',
        type: 'string'
    },{
        name: 'ORDER_BY',
        type: 'string'
    },{
    	name:'MTEXT',
    	type:'string',
    	convert:function(v,r){
    		return r.get('TEXT')+' / '+r.get('PTEXT');
    	}
    }]
});