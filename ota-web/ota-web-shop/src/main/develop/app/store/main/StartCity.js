Ext.define('Weidian.store.main.StartCity', {
    extend: 'Ext.data.Store',

    alias: 'store.startcity',

    model: 'Weidian.model.main.StartCity',

    data: { datas: [
        {
			'CITY_NAME':'西安'
		},{
			'CITY_NAME':'乌鲁木齐'
		},{
			'CITY_NAME':'咸阳'
		},{
			'CITY_NAME':'海口'
		},{
			'CITY_NAME':'哈萨克斯坦'
		},{
			'CITY_NAME':'北京'
		},{
			'CITY_NAME':'上海'
		}
    ]},

    proxy: {
        type: 'memory',
        reader: {
            type: 'json',
            rootProperty: 'datas'
        }
    }
});
