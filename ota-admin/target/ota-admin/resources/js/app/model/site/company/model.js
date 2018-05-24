Ext.define('app.model.site.company.model', {
    extend: 'Ext.data.Model',
    fields: ['SALE_TRAFFIC','TRAFFIC_EXPENSE','SALE_ROUTE','ROUTE_EXPENSE','AMOUNT','IS_CONTRACT_MUST',{
        name: 'ID',
        type: 'string'
    },{
        name: 'USER_ID',
        type: 'string'
    },{
        name: 'USER_NAME',
        type: 'string'
    },{
        name: 'COMPANY',
        type: 'string'
    },{
        name: 'CODE',
        type: 'string'
    },{
        name: 'LICENSE_NO',
        type: 'string'
    },{
        name: 'LICENSE_ATTR',
        type: 'string'
    },{
        name: 'BRAND_NAME',
        type: 'string'
    },{
        name: 'BUSINESS',
        type: 'string'
    },{
        name: 'LOGO',
        type: 'string'
    },{
        name: 'ADDRESS',
        type: 'string'
    },{
        name: 'PHONE',
        type: 'string'
    },{
        name: 'LEGAL_PERSON',
        type: 'string'
    },{
        name: 'ORDER_BY',
        type: 'int'
    },{
        name: 'IS_SHOW',
        type: 'string'
    },{
        name: 'LOGO_URL',
        type: 'string'
    },{
        name: 'BUSINESS_URL',
        type: 'string'
    },{
        name: 'SOURCE',
        type: 'string'
    },{
        name: 'AUDIT_STATUS',
        type: 'string', convert: function(value){
        	return value||0;
        }
    },{
        name: 'AUDIT_TIME',
        type: 'string'
    },{
        name: 'CREATE_TIME',
        type: 'string', convert: function(value){
        	return util.timeAgoInWords(value);
        }
    },{
        name: 'TYPE',
        type: 'string'
    },{
        name: 'AUDIT_USER',
        type: 'string'
    },{
        name: 'AUDIT_USER_ID',
        type: 'string'
    },{
        name: 'TGR_NO',
        type: 'string'
    },{
        name: 'CITY_ID',
        type: 'string'
    },{
        name: 'CITY_NAME',
        type: 'string'
    },{
        name: 'IS_USE',
        type: 'string'
    },{
        name: 'CITY',
        type: 'string'
    },{
        name: 'ROLE_NAME',
        type: 'string'
    },{
        name: 'ROLE_ID',
        type: 'string'
    },'PARENT_COMPANY','BANK_ID','BANK_NO','OPEN_ADDRESS','OPER_NAME','IS_MANAGER','IS_COUNSELOR',
    'IS_CONTACTS','SHARE_ROUTE','CHILD_ROLE_NAME','CHILD_ROLE_ID']
});