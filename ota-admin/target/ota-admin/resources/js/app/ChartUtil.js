Ext.define('app.ChartUtils', {
    alternateClassName: 'chartUtil',
    statics:{
    	fontFamily:"微软雅黑",
    	request :function(url,params,callback,scope){
    		Ext.Ajax.request({
                url: url,
                params:params,
                success: function (result) {
                    result = Ext.decode(result.responseText);
                    if (!result.success) {
                    	util.error(result.message);
                    } else {
                    	callback.call(scope,result);
                    }
                }
            });
    	},
    	initTitle:function(title,x){
    		return {
    			text: title||'',
    			x: x||'left',
    	        textStyle:{
    	        	fontFamily:"微软雅黑",
    	        	fontSize:16,
    	        	color:'#427fed',
    	        	fontWeight:'bolder'
    	        }
    		};
    	},
    	defaultsOption:{
    		calculable : false,
    		tooltip : {trigger: 'axis'},
    	    grid: {x:5,y:50,x2:100,y2:60},
    	    dataZoom : {
                show : true,
                realtime: true
            },
    	    legend: {x:'center',y:10,selectedMode:true,textStyle:{fontFamily:"微软雅黑"},data:[]}
    	},
    	initChart:function(p,option,legendData,onClick){
	    	require.config({
	            paths: {
	                echarts: cfg.getJsPath()+'/echarts'
	            }
	        });
    		require(
	        [
	         	'echarts',
	         	'echarts/chart/bar',
	         	'echarts/chart/line'
	        ],function(echarts){
	        	var chart = echarts.init(p.body.dom);
	        	Ext.applyIf(option,chartUtil.defaultsOption);
	    		option.legend.data = [];
	    		option.legend.data = option.legend.data.concat(legendData||[]);
	    		chart.setOption(option);
	    		if(onClick){
		    		var ecConfig = require('echarts/config');
		    		chart.on(ecConfig.EVENT.CLICK,function(params){
		    			onClick.call(chart,params);
		    		});
	    		}
	        });
    		
    	},
    	initPieChart:function(p,option){
    		require.config({
	            paths: {
	                echarts: cfg.getJsPath()+'/echarts'
	            }
	        });
    		require(
	        [
	         	'echarts',
	         	'echarts/chart/pie'
	        ],function(echarts){
	        	var chart = echarts.init(p.body.dom);
	    		chart.setOption(option);
	        });
    	}
    }
});