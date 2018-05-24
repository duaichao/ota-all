Ext.define('app.model.b2b.discount.rulemodel', {
    extend: 'Ext.data.Model',
    fields: [
        'ID','RULE_TYPE', 'DISCOUNT_ID', 'MONEY', 'PAY_WAY','PLATFROM','IS_USE'
    ]
});