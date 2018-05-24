package cn.sd.core.pay.cft.config;

public class TenpayConfig {
	//收款方
	public static String spname = "陕西新视线国际旅行社有限公司";  
	//商户号
	public static String partner = "1250803501";
	//密钥
	public static String key = "d052b14e5aef48e8456bd838e97e19a1";

	//回调通知URL，让财付通回调使用
	public static String return_url="/pay/cft/cft_return.jsp";
	public static String notify_url="/pay/cft/cft_notify.jsp";
	//手续费（我们平台的手续费设置，不是财付通的）
	public static Double getFee(Double total_money,String bankCode){
		return  0.0;
	}
}
