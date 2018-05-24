Ext.define('Weidian.view.authentication.CountryCode', {
    extend: 'Ext.dataview.List',
	xtype:'countrycode',
    config:{
		controller:'auth',
		title:'选择国家或地区',
		showMainPoint:false,
		backHide:false,
		//store: 'countrycode',
		infinite:true,
        indexBar: true,
        shadow: true,
        itemTpl: '{value}',
        grouped: true,
        pinHeaders: true
	},
	listeners:{
		select:{
			scope: 'controller',
			fn:'onCodeItemSelected'
		}
	}	
});