package cn.sd.service.pay;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.core.util.CommonUtils;
import cn.sd.entity.produce.CompanyDetail;
import cn.sd.pay.alipay.config.AlipayConfig;
import cn.sd.pay.cft.config.TenpayConfig;
import cn.sd.pay.cft.handler.RequestHandler;
import cn.sd.pay.cft.util.TenpayUtil;

@Repository
@Service("payService")
public class PayService extends BaseDaoImpl implements IPayService{
	
	/**
	 * 支付宝支付
	 * String orderNo 订单编号
	 * String bankCode 网银代码
	 * Double money 支付金额
	 * boolean isBankPay 是否网银 财付通网银 or 支付宝网银
	 * title ZXCZ: 在线充值, ZXFK: 在线付款
	 */
	public Map<String, Object> alipay(String orderNo,String bankCode,Double money,boolean isBankPay,String uid, String type,CompanyDetail company){
		Map<String, Object> result = new HashMap<String, Object>();
		String title = money + "元 ,订单编号：" + orderNo + "";
		String return_url_para = "alipay_return_cz";
		//必填参数//
		//请与贵网站订单系统中的唯一订单号匹配
		String out_trade_no = orderNo;
		//订单名称，显示在支付宝收银台里的“商品名称”里，显示在支付宝的交易管理的“商品名称”的列表里。
		String subject = title;
		//订单描述、订单详细、订单备注，显示在支付宝收银台里的“商品描述”里
		String body = title;
		//subject = new String(request.getParameter("subject").getBytes("ISO-8859-1"),"utf-8");			
		//body = new String(request.getParameter("alibody").getBytes("ISO-8859-1"),"utf-8");	
		//订单总金额，显示在支付宝收银台里的“应付总额”里
		String total_fee = String.valueOf(money);
		//扩展功能参数——默认支付方式//
		//默认支付方式，取值见“即时到帐接口”技术文档中的请求参数列表
		//directPay（余额支付）
		//bankPay（网银支付）
		//cartoon（卡通）
		//creditPay（信用支付）
		//CASH（网点支付）
		String paymethod = isBankPay==false?"directPay":"bankPay";//设置为支付宝余额支付 默认是网银支付bankPay
		//默认网银代号，代号列表见“即时到帐接口”技术文档“附录”→“银行列表”
		String defaultbank = isBankPay==false?"":bankCode;
		//扩展功能参数——防钓鱼//
		//防钓鱼时间戳
		String anti_phishing_key  = "";
		//获取客户端的IP地址，建议：编写获取客户端IP地址的程序
		String exter_invoke_ip= "";
		//注意：
		//1.请慎重选择是否开启防钓鱼功能
		//2.exter_invoke_ip、anti_phishing_key一旦被设置过，那么它们就会成为必填参数
		//3.开启防钓鱼功能后，服务器、本机电脑必须支持远程XML解析，请配置好该环境。
		//4.建议使用POST方式请求数据
		//示例：
		//anti_phishing_key = AlipayService.query_timestamp();	//获取防钓鱼时间戳函数
		//exter_invoke_ip = AlipayConfig.phishIp;
		//扩展功能参数——其他///
		//自定义参数，可存放任何内容（除=、&等特殊字符外），不会显示在页面上
		String extra_common_param = type+"#"+bankCode+"#"+uid;
		//默认买家支付宝账号
		String buyer_email = "";
		//商品展示地址，要用http:// 格式的完整路径，不允许加?id=123这类自定义参数
		String show_url = "http://www.xsx518.com";
		//扩展功能参数——分润(若要使用，请按照注释要求的格式赋值)//
		//提成类型，该值为固定值：10，不需要修改
		String royalty_type = "";
		//提成信息集
		String royalty_parameters ="";
		//注意：
		//与需要结合商户网站自身情况动态获取每笔交易的各分润收款账号、各分润金额、各分润说明。最多只能设置10条
		//各分润金额的总和须小于等于total_fee
		//提成信息集格式为：收款方Email_1^金额1^备注1|收款方Email_2^金额2^备注2
		//示例：
		//royalty_type = "10"
		//royalty_parameters	= "111@126.com^0.01^分润备注一|222@126.com^0.01^分润备注二"
		//////////////////////////////////////////////////////////////////////////////////
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
		// 对账类型为3时.是平台对账.那付款都应支付到平台的支付宝账号
        if(!company.getACCOUNT_WAY().equals("3") 
        		&& CommonUtils.checkString(company.getPARTNER())
        		&& CommonUtils.checkString(company.getKEY())
        		&& CommonUtils.checkString(company.getSELLER_EMAIL())){
        	AlipayConfig.key = company.getKEY();
        	sParaTemp.put("AliPayKey", company.getKEY());
        	sParaTemp.put("partner", company.getPARTNER());
        	sParaTemp.put("seller_email", company.getSELLER_EMAIL());
        }else{
        	sParaTemp.put("partner", AlipayConfig.partner);
        	sParaTemp.put("seller_email", AlipayConfig.seller_email);
        }
    	
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", "1");
		sParaTemp.put("paymethod", paymethod);
        sParaTemp.put("defaultbank", defaultbank);
		sParaTemp.put("extra_common_param", extra_common_param);
		
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("body", body);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		result.put("sParaTemp", sParaTemp);
		result.put("success", true);
		return result;
	}
	
	/**
	 * 支付宝支付
	 * String orderNo 订单编号
	 * String bankCode 网银代码
	 * Double money 支付金额
	 * boolean isBankPay 是否网银 财付通网银 or 支付宝网银
	 * title ZXCZ: 在线充值, ZXFK: 在线付款
	 */
	public Map<String, Object> wapalipay(String orderNo,String bankCode,Double money,boolean isBankPay,String uid, String type,CompanyDetail company, String appName){
		Map<String, Object> result = new HashMap<String, Object>();
		String title = money + "元 ,订单编号：" + orderNo + "";
		////////////////////////////////////请求参数//////////////////////////////////////
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = orderNo;
		//订单名称，必填
		String subject = title;
		//付款金额，必填
		String total_fee = String.valueOf(money);
		//收银台页面上，商品展示的超链接，必填
		String show_url = "http://www.xsx518.com";
		//商品描述，可空
		String body = "";
		//////////////////////////////////////////////////////////////////////////////////
		String extra_common_param = type+"#"+bankCode+"#"+uid+"#"+appName;
		System.out.println("extra_common_param---------------"+extra_common_param);
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		
		if(!company.getACCOUNT_WAY().equals("3") 
        		&& CommonUtils.checkString(company.getPARTNER())
        		&& CommonUtils.checkString(company.getKEY())
        		&& CommonUtils.checkString(company.getSELLER_EMAIL())){
        	AlipayConfig.key = company.getKEY();
        	sParaTemp.put("AliPayKey", company.getKEY());
        	sParaTemp.put("partner", company.getPARTNER());
			sParaTemp.put("seller_id", company.getPARTNER());
        }else{
        	sParaTemp.put("partner", cn.sd.pay.wap.alipay.config.AlipayConfig.partner);
			sParaTemp.put("seller_id", cn.sd.pay.wap.alipay.config.AlipayConfig.seller_id);
        }
		
		sParaTemp.put("service", cn.sd.pay.wap.alipay.config.AlipayConfig.service);
		
		sParaTemp.put("_input_charset", cn.sd.pay.wap.alipay.config.AlipayConfig.input_charset);
		sParaTemp.put("payment_type", cn.sd.pay.wap.alipay.config.AlipayConfig.payment_type);
		sParaTemp.put("notify_url", cn.sd.pay.wap.alipay.config.AlipayConfig.notify_url);
		sParaTemp.put("return_url", cn.sd.pay.wap.alipay.config.AlipayConfig.return_url);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("body", "");
		sParaTemp.put("USER_AGENT", "mobile");
		sParaTemp.put("extra_common_param", extra_common_param);
		sParaTemp.put("app_pay", "Y");
		
		result.put("sParaTemp", sParaTemp);
		result.put("success", true);
		return result;
	}
	
	public Map<String, Object> cftpay(String orderNo,String bankCode,Double money,boolean isBankPay,String uid, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Assert.hasText(orderNo);
			Assert.notNull(money);
			
			bankCode = "2".equals(bankCode)?"":bankCode;
			
			//财付通 title 限制长度32
			String title = money+"元 ";
			String description = title;
			double fee = TenpayConfig.getFee(money,bankCode);
			if( fee>0){
				description = "当前需要支付手续费:"+fee;
			}
			//---------------生成订单号 开始------------------------
			//当前时间 yyyyMMddHHmmss
			String currTime = TenpayUtil.getCurrTime();
			//8位日期
			//String strTime = currTime.substring(8, currTime.length());
			//四位随机数
			//String strRandom = TenpayUtil.buildRandom(4) + "";
			//10位序列号,可以自行调整。
			//String strReq = strTime + strRandom;
			//订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行
			//String out_trade_no = strReq;
			//---------------生成订单号 结束------------------------
	
	
			//创建支付请求对象
			RequestHandler reqHandler = new RequestHandler(request, response);
			reqHandler.init();
			//设置密钥
			reqHandler.setKey(TenpayConfig.key);
			//设置支付网关
			reqHandler.setGateUrl("https://gw.tenpay.com/gateway/pay.htm");
			//-----------------------------
			//设置支付参数
			//-----------------------------
			reqHandler.setParameter("partner", TenpayConfig.partner);		        //商户号
			reqHandler.setParameter("out_trade_no", orderNo);		//商家订单号
			reqHandler.setParameter("total_fee", String.valueOf((int)(money*100)));	//商品金额,以分为单位	
			String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
			reqHandler.setParameter("return_url", basePath+TenpayConfig.return_url);		    //交易完成后跳转的URL
			reqHandler.setParameter("notify_url", basePath+TenpayConfig.notify_url);		    //接收财付通通知的URL
			reqHandler.setParameter("body", description);	                    //商品描述
			if(isBankPay){
				reqHandler.setParameter("bank_type", bankCode);		    //银行类型(中介担保时此参数无效)
			}else{
				reqHandler.setParameter("bank_type", "DEFAULT");		    //财付通余额
			}		
			reqHandler.setParameter("spbill_create_ip",request.getRemoteAddr());   //用户的公网ip，不是商户服务器IP
			reqHandler.setParameter("fee_type", "1");                    //币种，1人民币
			reqHandler.setParameter("subject", title);              //商品名称(中介交易时必填)
	
			//系统可选参数
			reqHandler.setParameter("sign_type", "MD5");                //签名类型,默认：MD5
			reqHandler.setParameter("service_version", "1.0");			//版本号，默认为1.0
			reqHandler.setParameter("input_charset", "UTF-8");            //字符编码
			reqHandler.setParameter("sign_key_index", "1");             //密钥序号
	
	
			//业务可选参数
			reqHandler.setParameter("attach", bankCode);                //附加数据，原样返回 (我们自己放置银行代码)
			reqHandler.setParameter("product_fee", "");                 //商品费用，必须保证transport_fee + product_fee=total_fee
			reqHandler.setParameter("transport_fee", "0");               //物流费用，必须保证transport_fee + product_fee=total_fee
			reqHandler.setParameter("time_start", currTime);            //订单生成时间，格式为yyyymmddhhmmss
			reqHandler.setParameter("time_expire", "");                 //订单失效时间，格式为yyyymmddhhmmss
			reqHandler.setParameter("buyer_id", "");                    //买方财付通账号
			reqHandler.setParameter("goods_tag", "");                   //商品标记
			reqHandler.setParameter("trade_mode", "1");                 //交易模式，1即时到账(默认)，2中介担保，3后台选择（买家进支付中心列表选择）
			reqHandler.setParameter("transport_desc", "");              //物流说明
			reqHandler.setParameter("trans_type", "2");                  //交易类型，1实物交易，2虚拟交易
			reqHandler.setParameter("agentid", "");                     //平台ID
			reqHandler.setParameter("agent_type", "0");                  //代理模式，0无代理(默认)，1表示卡易售模式，2表示网店模式
			reqHandler.setParameter("seller_id", "");                   //卖家商户号，为空则等同于partner
	
			//请求的url
			String requestUrl = reqHandler.getRequestURL();
			result.put("requestUrl", requestUrl);
			//获取debug信息,建议把请求和debug信息写入日志，方便定位问题
			String debuginfo = reqHandler.getDebugInfo();
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
		}
		return result;
	}
	
	/**
	 * 根据订单编号 获取Title说明
	 * CZDX 短信充值
	 * CZZX 在线支付
	 * CZFK 在线充值 
	 * CZHK 在线还款
	 */
	private String getTitleByCode(String code){
		if ("CZDX".equals(code)) {
			return "您需要给短信充值";
		}else if ("CZZX".equals(code)) {
			return "您需要给新视线账户充值";
		}else if ("CZZX".equals(code)) {
			return "您需要给新视线账户还款";
		}else if("MPDD".equals(code)){
			return "您需要给新视线支付";
		}else{
			return "您需要给新视线支付";
		}
	}
}
