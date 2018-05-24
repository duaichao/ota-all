Ext.define('Ext.ux.form.field.CardNumber', {
    extend: 'Ext.form.field.Text',
    alias: 'widget.cardno',
    noType :'mobile',
    offset :[0,0],
    enableKeyEvents:true,
    style:'position:relative;',
	initComponent: function () {
        var me = this;
		
		me.on('afterrender', function (t) {
			t.zntip = Ext.DomHelper.append(t.el,{
			    id: 'cardformat',
			    tag: 'div',
			    cls: 'card-no'
			},true);
			
		});
		me.on('keyup', function () {
			var val=this.getValue().replace(/[^\d]/g,''),
                len=val.length,
                tip = this.zntip;
			if(val!=''){
				tip.show();
			}
			if(me.noType=='mobile'){
	            if (len <= 7) {
	                val = val.substring(0, 3) + " " + val.substring(3, len);
	            } else {
	                val = val.substring(0, 3) + " " + val.substring(3, 7) + " " + val.substring(7, len);
	            }
			}
			if(me.noType=='bank'){
				val = val.replace(/\s/g,'').replace(/(\d{4})(?=\d)/g,"$1 ");  
			}
			tip.update(val);
		});
		me.on('blur', function () {
			 this.zntip.hide();
		});
		me.on('focus', function () {
			var width = this.getWidth(),lw = this.labelWidth;
			this.zntip.setLeft(lw+10+this.offset[0]);
			this.zntip.setTop(28+this.offset[1]);
			this.zntip.setSize(width-lw-5,30);
			this.el.down('.x-form-item-body').setWidth(width-lw-5);
			this.zntip.show();
		});
		


        me.callParent(arguments);
	}
	
});