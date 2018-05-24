/**
 * Simplified Chinese translation
 * By DavidHu
 * 09 April 2007
 *
 * update by andy_ghg
 * 2009-10-22 15:00:57
 */
Ext.onReady(function() {
    var parseCodes;
	Ext.MessageBox.YESNO =  [
		{text: '', iconCls:'x-fm fm-close f1em', itemId: 'no', ripple:false, ui: 'action'},
		{text: '', iconCls:'x-fm fm-finish f1em', itemId: 'yes', ripple:false, ui: 'action'}
	];
    if (Ext.Date) {
        Ext.Date.monthNames = ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"];

        Ext.Date.dayNames = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];

        Ext.Date.formatCodes.a = "(this.getHours() < 12 ? '上午' : '下午')";
        Ext.Date.formatCodes.A = "(this.getHours() < 12 ? '上午' : '下午')";

        parseCodes = {
            g: 1,
            c: "if (/(上午)/i.test(results[{0}])) {\n"
                + "if (!h || h == 12) { h = 0; }\n"
                + "} else { if (!h || h < 12) { h = (h || 0) + 12; }}",
            s: "(上午|下午)",
            calcAtEnd: true
        };

        Ext.Date.parseCodes.a = Ext.Date.parseCodes.A = parseCodes;
        
    }

    if (Ext.util && Ext.util.Format) {
        Ext.apply(Ext.util.Format, {
            thousandSeparator: ',',
            decimalSeparator: '.',
            currencySign: '\u00a5',
            // Chinese Yuan
            dateFormat: 'y年m月d日'
        });
    }
});






Ext.define("Ext.locale.zh_CN.picker.Date", {
    override: "Ext.picker.Date",
	yearFrom: 2000,
	monthText: '月',
	dayText: '日',
	yearText: '年',
    todayText: "今天",
    minText: "日期必须大于最小允许日期",
    //update
    maxText: "日期必须小于最大允许日期",
    //update
    disabledDaysText: "",
    disabledDatesText: "",
    nextText: '下个月 (Ctrl+Right)',
    prevText: '上个月 (Ctrl+Left)',
    monthYearText: '选择一个月 (Control+Up/Down 来改变年份)',
    //update
    todayTip: "{0} (空格键选择)",
    format: "y年m月d日",
    ariaTitle: '{0}',
    ariaTitleDateFormat: 'Y\u5e74m\u6708d\u65e5',
    longDayFormat: 'Y\u5e74m\u6708d\u65e5',
    monthYearFormat: 'Y\u5e74m\u6708',
    getDayInitial: function (value) {
        // Grab the last character
        return value.substr(value.length - 1);
    }
});
Ext.define("Ext.local_zh_cn.picker.Picker", {
	override: "Ext.picker.Picker",
	applyDoneButton: function (config) {
		if (config) {
			if (Ext.isBoolean(config)) {
				config = {};
			}
			if (typeof config == "string") {
				config = {
					text: config
				};
			}
			Ext.applyIf(config, {
				ui: 'action',
				align: 'right',
				ripple:false,
				text: '', iconCls:'x-fm fm-finish f1em'
			});
		}
		return Ext.factory(config, 'Ext.Button', this.getDoneButton());
	},
	applyCancelButton: function (config) {
		if (config) {
			if (Ext.isBoolean(config)) {
				config = {};
			}
			if (typeof config == "string") {
				config = {
					text: config
				};
			}
			Ext.applyIf(config, {
				ui: 'action',
				align: 'left',
				ripple:false,
				text: '', iconCls:'x-fm fm-close f1em'
			});
		}
		return Ext.factory(config, 'Ext.Button', this.getCancelButton());
	}

});




// This is needed until we can refactor all of the locales into individual files
Ext.define("Ext.locale.zh_CN.Component", {	
    override: "Ext.Component"
});
Ext.define("Ext.locale.zh_CN.dataview.DataView", {
	override: "Ext.dataview.DataView",
	config: {
		selectedCls: '',
	    //禁用加载遮罩，防止跳转时页面卡顿，使用统一的遮罩效果
	    loadingText: false,
	    deferEmptyText:'没有数据',
		emptyText:'没有数据'
	}
});
Ext.define("Ext.locale.zh_CN.dataview.List", {
	override: "Ext.dataview.List",
	config: {
	    //取消选择效果
	    selectedCls: '',
	    //禁用加载遮罩，防止跳转时页面卡顿，使用统一的遮罩效果
	    loadingText: false,
	    deferEmptyText:'没有数据',
		emptyText:'没有数据'
	}
});
//重写分页插件
Ext.define("Ext.locale.zh_CN.plugin.ListPaging", {
    override: "Ext.plugin.ListPaging",
    config: {
        //自动加载
        autoPaging: true,
        loadMoreText:'加载更多',
		noMoreRecordsText:'没有更多了'
    }
});
/*Ext.define("Ext.locale.zh_CN.plugin.PullRefresh", {
    override: "Ext.plugin.PullRefresh",
    config: {
    	releaseText:'<i class="iconfont font42 blue-text" style="margin-right:5px;">&#xe62d;</i>',
    	loadingText:null,
    	loadedText:null,
    	pullText:'<i class="iconfont font42" style="color:#e1e1e1;margin-right:5px;">&#xe62d;</i>',
    	pullTpl:[
    	    '<div class="x-loading-spinner">',
    	    '<div class="loading refresh"></div>',
            '</div>',
            '<div class="x-list-pullrefresh-wrap">',
                '<h3 class="x-list-pullrefresh-message">{message}</h3>',
                '<div class="x-list-pullrefresh-updated">{updated}</div>',
            '</div>'
        ].join('')
    }
});*/


Ext.apply(Ext.MessageBox,{
	OK    : {text: '知道了',     itemId: 'ok'},
	YES   : {text: '确定',    itemId: 'yes'},
	NO    : {text: '取消',     itemId: 'no'},
	CANCEL: {text: '取消', itemId: 'cancel'},
	
	/*INFO    : Ext.baseCSSPrefix + 'msgbox-info',
	WARNING : Ext.baseCSSPrefix + 'msgbox-warning',
	QUESTION: Ext.baseCSSPrefix + 'msgbox-question',
	ERROR   : Ext.baseCSSPrefix + 'msgbox-error',*/
	
	OKCANCEL: [
	    {text: '取消', itemId: 'cancel'},
	    {text: '确定',     itemId: 'ok'}
	],
	YESNOCANCEL: [
	    {text: '关闭', itemId: 'cancel'},
	    {text: '取消',     itemId: 'no'},
	    {text: '确定',    itemId: 'yes'}
	],
	YESNO: [
	    {text: '取消',  itemId: 'no'},
	    {text: '确定', itemId: 'yes'}
	]
});
