Ext.define('Weidian.store.product.Results', {
    extend: 'Ext.data.Store',

    alias: 'store.productresults',

    model: 'Weidian.model.product.Result',
	
	
    proxy: {
        type: 'api',
        url: '~api/product/results'
    },

    autoLoad: 'true',

    sorters: {
        direction: 'ASC',
        property: 'title'
    }
});
