Ext.define('app.model.site.company.roleModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'ID',
        type: 'string'
    },{
        name: 'COMPANY_ID',
        type: 'string'
    },{
        name: 'ROLE_NAME',
        type: 'string'
    },{
        name: 'IS_USE',
        type: 'string'
    },{
        name: 'ROLE_TYPE',
        type: 'string'
    },{
        name: 'IS_DEL',
        type: 'string'
    },'SYNC_CNT']
});