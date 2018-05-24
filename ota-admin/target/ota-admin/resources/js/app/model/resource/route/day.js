Ext.define('app.model.resource.route.day', {
    extend: 'Ext.data.Model',
    fields: [
        'ID','NO','ROUTE_ID', 'TODAY_TIPS', 'HOTEL_TIPS', 'PAY_TIPS', 'BREAKFAST', 'LUNCH', 'DINNER', 
        'TITLE','BEGIN_CITY',
        'TOOL','END_CITY',
        'TOOL1','END_CITY1','ROUTE_CITY_ID'
    ]
});