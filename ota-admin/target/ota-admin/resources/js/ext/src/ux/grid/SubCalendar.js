Ext.define('Ext.ux.grid.SubCalendar', {
    extend: 'Ext.grid.plugin.RowExpander',

    alias: 'plugin.subcalendar',
	childEls: ["calendar"],
	rowBodyTpl: ['<div id="{ID}-calendar" class="' + Ext.baseCSSPrefix + 'grid-subcalendar">',
        '{%',
            'this.owner.renderCalendar(out, values);',
        '%}',
        '</div>'
    ],
	
    init: function(grid) {
        var me = this;
        me.callParent(arguments);
        grid.getView().on('expandbody',function( rowNode, record, expandRow, eOpts ){
        	var el = Ext.get(expandRow).down('.x-grid-subcalendar');
        	if(!el.down('.subClass')){
        		Ext.create('Ext.panel.Panel',{
        			cls:'subClass',
        			layout:'fit',
        			height:400,
        			margin:'5 40 0 0',
        			renderTo:el,
        			items:[Ext.create('Ext.ux.IFrame',{
						src:cfg.getCtx()+'/base/calendar',
						style:'width:100%;height:100%;'
					})]
        		});
	        	
    		}
        });
        
		
    },
    destroy: function() {
        this.callParent();
    },
    getRowBodyFeatureData: function(record, idx, rowValues) {
        this.callParent(arguments);
    },
    renderCalendar: function(out, rowValues) {
    	//console.log(rowValues);
    },
	
    handleSelect: function(calendarMatrix, selDt, selDate, selVal) {
        var me=this,
            rangeDt1, rangeDt2, rangeDate1_fmt, rangeDate2_fmt,
            rangeSelectMode = calendarMatrix.getRangeSelectMode(),
            myWindow = me.getView().myWindow,
            startMonthIdx =  calendarMatrix.getStartMonthIdx();

        rangeDt1 = calendarMatrix.rangeDt1;
        rangeDt2 = calendarMatrix.rangeDt2;
        rangeDate1_fmt = Ext.Date.format(calendarMatrix.rangeDate1, 'm/d/Y');
        rangeDate2_fmt = Ext.Date.format(calendarMatrix.rangeDate2, 'm/d/Y');

        me.lookupReference('startBtn').setText(rangeDate1_fmt);
        me.lookupReference('endBtn').setText(rangeDate2_fmt);

        if (rangeSelectMode==='startdate' && Ext.isEmpty(rangeDt2)){
            // auto-select end date button and redisplay calendar to accept end date
            me.onEndBtnClick(me.lookupReference('endBtn'));
          //  myWindow.down('#priorCalGridMatrix').setVisible(false);
        }
        if (rangeSelectMode==='enddate' && Ext.isEmpty(rangeDt1)){
            // auto-select start date button and redisplay calendar to accept start date
            me.onStartBtnClick(me.lookupReference('startBtn'));
        }
        if (!Ext.isEmpty(rangeDt1) && !Ext.isEmpty(rangeDt2)){
            myWindow.setHidden(true);
        }
    },

    onCalendarMouseOver: function(matrix, selDate) {
        var me=this, i, matrixItem, calendarMatrix = matrix.matrixCont,
             rangeSelectMode = calendarMatrix.getRangeSelectMode(),
             selDt = Ext.Date.format(selDate, 'Y-m-d');


        if ((rangeSelectMode==='startdate' && Ext.isEmpty(calendarMatrix.rangeDt2)) ||
            (rangeSelectMode==='enddate'   && Ext.isEmpty(calendarMatrix.rangeDt1)) ||
            (rangeSelectMode==='enddate'   && selDt === calendarMatrix.rangeDt1)  ||
            (rangeSelectMode==='startdate' && selDt === calendarMatrix.rangeDt2)
           ){
            return;
        }

        if (rangeSelectMode==='enddate'){
            calendarMatrix.rangeDate2 = selDate;
            calendarMatrix.rangeDt2 = Ext.Date.format(selDate, 'Y-m-d');
        }
        else {
            calendarMatrix.rangeDate1 = selDate;
            calendarMatrix.rangeDt1 = Ext.Date.format(selDate, 'Y-m-d');
        }

        // refresh all matrix calendars based on selected range
        for (i=0; i<calendarMatrix.matrix.length; i++){
            matrixItem = calendarMatrix.down('#'+calendarMatrix.matrix[i].itemId+'_mc');
            matrixItem.fullUpdate(selDate);
        }
    }
});