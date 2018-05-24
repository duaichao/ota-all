Ext.define('Ext.ux.form.DatePickerEvent', {
    extend: 'Ext.picker.Date',

    alias: 'widget.eventdatepicker',
	
	
	cls:'eventdatepicker',
	
	eventTexts:{},//key 日期 value 同行价格-外卖价格-座位
	
	setEventTexts:function(eventTexts){
		this.eventTexts = eventTexts;
	},
	update : function(date, forceRefresh){
        var me = this;
            monthViewChange = me.isAnotherMonthView(date);
        me.callParent(arguments);
        if (monthViewChange){
            me.fireEvent("monthviewchange", me, date, me.activeDate);
        }
        return me;
    },
    /**
     * return true if the given date is in a different month view of the actual calendar date
     * @param {Date} date
     * @return {Boolean}
     */
    isAnotherMonthView: function(date){
        var activeDate = this.activeDate || date;
        return date.getYear() != activeDate.getYear() || date.getMonth() != activeDate.getMonth();
    }
    
    , /**
     * Update the contents of the picker for a new month
     * @private
     * @param {Date} date The new date
     */
    fullUpdate: function(date) {
        var me = this,
            cells = me.cells.elements,
            textNodes = me.textNodes,
            disabledCls = me.disabledCellCls,
            eDate = Ext.Date,
            i = 0,
            extraDays = 0,
            newDate = +eDate.clearTime(date, true),
            today = +eDate.clearTime(new Date()),
            min = me.minDate ? eDate.clearTime(me.minDate, true) : Number.NEGATIVE_INFINITY,
            max = me.maxDate ? eDate.clearTime(me.maxDate, true) : Number.POSITIVE_INFINITY,
            ddMatch = me.disabledDatesRE,
            ddText = me.disabledDatesText,
            ddays = me.disabledDays ? me.disabledDays.join('') : false,
            ddaysText = me.disabledDaysText,
            format = me.format,
            days = eDate.getDaysInMonth(date),
            firstOfMonth = eDate.getFirstDateOfMonth(date),
            startingPos = firstOfMonth.getDay() - me.startDay,
            previousMonth = eDate.add(date, eDate.MONTH, -1),
            ariaTitleDateFormat = me.ariaTitleDateFormat,
            prevStart, current, disableToday, tempDate, setCellClass, html, cls,
            formatValue, value;

        if (startingPos < 0) {
            startingPos += 7;
        }

        days += startingPos;
        prevStart = eDate.getDaysInMonth(previousMonth) - startingPos;
        current = new Date(previousMonth.getFullYear(), previousMonth.getMonth(), prevStart, me.initHour);

        if (me.showToday) {
            tempDate = eDate.clearTime(new Date());
            disableToday = (tempDate < min || tempDate > max ||
                (ddMatch && format && ddMatch.test(eDate.dateFormat(tempDate, format))) ||
                (ddays && ddays.indexOf(tempDate.getDay()) != -1));

            if (!me.disabled) {
                me.todayBtn.setDisabled(disableToday);
            }
        }

        setCellClass = function(cellIndex, cls){
            var cell = cells[cellIndex];
            
            value = +eDate.clearTime(current, true);
            cell.setAttribute('aria-label', eDate.format(current, ariaTitleDateFormat));
            cell.setAttribute('aria-value', eDate.format(current, 'Ymd'));
            // store dateValue number as an expando
            cell.firstChild.dateValue = value;
            if (value == today) {
                cls += ' ' + me.todayCls;
                cell.firstChild.title = me.todayText;
                
                // Extra element for ARIA purposes
                me.todayElSpan = Ext.DomHelper.append(cell.firstChild, {
                    tag: 'span',
                    cls: Ext.baseCSSPrefix + 'hidden-clip',
                    html: me.todayText
                }, true);
            }
            if (value == newDate) {
                me.activeCell = cell;
                me.eventEl.dom.setAttribute('aria-activedescendant', cell.id);
                cell.setAttribute('aria-selected', true);
                cls += ' ' + me.selectedCls;
                me.fireEvent('highlightitem', me, cell);
            } else {
                cell.setAttribute('aria-selected', false);
            }

            if (value < min) {
                cls += ' ' + disabledCls;
                cell.setAttribute('aria-label', me.minText);
            }
            else if (value > max) {
                cls += ' ' + disabledCls;
                cell.setAttribute('aria-label', me.maxText);
            }
            else if (ddays && ddays.indexOf(current.getDay()) !== -1){
                cell.setAttribute('aria-label', ddaysText);
                cls += ' ' + disabledCls;
            }
            else if (ddMatch && format){
                formatValue = eDate.dateFormat(current, format);
                if(ddMatch.test(formatValue)){
                    cell.setAttribute('aria-label', ddText.replace('%0', formatValue));
                    cls += ' ' + disabledCls;
                }
            }
            cell.className = cls + ' ' + me.cellCls;
        };
        for(; i < me.numDays; ++i) {
            if (i < startingPos) {
                html = (++prevStart);
                cls = me.prevCls;
            } else if (i >= days) {
                html = (++extraDays);
                cls = me.nextCls;
            } else {
                html = i - startingPos + 1;
                cls = me.activeCls;
            }
            
            var info = '',infos,infoHtml='';
            if(cls == me.activeCls){
            	info = me.eventTexts[html]||''
            }
            if(info!=''){
            	infos = info.split('-');
            	infoHtml = '<a class="events">';
            	infoHtml += '<pre data-qtip="同行价"><i class="iconfont">&#xe615;</i>'+infos[0]+'</pre>';
            	infoHtml += '<pre data-qtip="外卖价"><i class="iconfont">&#xe615;</i>'+infos[1]+'</pre>';
            	if(infos[2]!='无'){
            		infoHtml += '<pre data-qtip="剩余座位" class="st">'+infos[2]+'</pre>';
            	}
            	infoHtml +='</a>';
            }
            textNodes[i].innerHTML = html+infoHtml;
            current.setDate(current.getDate() + 1);
            setCellClass(i, cls);
        }

        me.monthBtn.setText(Ext.Date.format(date, me.monthYearFormat));
    }
});