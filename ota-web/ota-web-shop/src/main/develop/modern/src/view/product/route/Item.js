Ext.define('Weidian.view.product.route.Item', {
    extend: 'Ext.Container',
    xtype: 'routeitem',

    cls: 'route-item',
     
    tpl:[
	     '<tpl for=".">'+
	     '<h3>{[util.fmWordText(values.TITLE)]}</h3>'+
	     '<div>{[util.fmWordText(values.CONTENT)]}</div>'+
	     '</tpl>'
	]
});
