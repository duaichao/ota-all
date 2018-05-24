Ext.define('app.model.City', {
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
        name: 'PINYIN',
        type: 'string'
    },{
        name: 'JIANPIN',
        type: 'string'
    },{
    	name:'MTEXT',
    	type:'string',
    	convert:function(v,r){
    		return ''+r.get('TEXT')+'/'+(r.get('PTEXT')==''?'中国':r.get('PTEXT'))+'<span style="float:right;">'+r.get('JIANPIN')+'</span>';
    	}
    }]
});