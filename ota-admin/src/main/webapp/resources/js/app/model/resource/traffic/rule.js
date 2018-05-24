Ext.define('app.model.resource.traffic.rule', {
    extend: 'Ext.data.Model',
    fields: [
        'ID','BEGIN_DATE', 'END_DATE', 'SEAT_COUNT','IS_PUB','TRAFFIC_ID','TYPE','BASE_PRICE'
    ]
});