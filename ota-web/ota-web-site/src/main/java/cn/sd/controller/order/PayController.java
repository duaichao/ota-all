package cn.sd.controller.order;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.pay.alipay.config.AlipayConfig;
import cn.sd.pay.alipay.util.AlipayCore;
import cn.sd.pay.alipay.util.AlipayNotify;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.entity.produce.CompanyDetail;
import cn.sd.service.order.IOrderService;

@RestController
@RequestMapping("/pay")
public class PayController extends ExtSupportAction{
	
	@Resource
	private IOrderService orderService;
	
	@RequestMapping("/alipay/return")
	public ModelAndView alipayReturn(HttpServletRequest request, HttpServletResponse response){
		CompanyDetail company = (CompanyDetail)request.getSession().getAttribute("company");
		ModelAndView mav = new ModelAndView();
		try {
			String url = "";
			String port = "";
			if (80!=request.getServerPort()) {
				port = ":" + request.getServerPort();
			}
			String basePath = "http://" + request.getServerName() + port+ request.getContextPath() ;
			//获取支付宝GET过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				params.put(name, valueStr);
			}
			
//			Enumeration names = request.getParameterNames();
//			while (names.hasMoreElements()) {
//				String name = (String) names.nextElement();
//				System.out.println(name+"-----"+request.getParameter(name));
//			}
			
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号

			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//支付宝交易号

			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			//获取总金额
			String total_fee = new String(request.getParameter("total_fee").getBytes("ISO-8859-1"),"UTF-8");
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			//公共参数
			
			//计算得出通知验证结果
			String key = "", partner = "";
			System.out.println(company.getACCOUNT_WAY().equals("3")+"----company.getACCOUNT_WAY().equals(\"3\")");
			if(company.getACCOUNT_WAY().equals("3")){
				key = AlipayConfig.key; partner = AlipayConfig.partner;
			}else{
				if(CommonUtils.checkString(company.getKEY()) && CommonUtils.checkString(company.getPARTNER())){
					key = company.getKEY(); partner = company.getPARTNER();
				}else{
					key = AlipayConfig.key; partner = AlipayConfig.partner;
				}
			}
			System.out.println(key+"------------"+partner+"--------key***partner");
			
			boolean verify_result = AlipayNotify.verify(params, key, partner);
			System.out.println(verify_result+"----------verify_result");
		    
			 
			if(verify_result){//验证成功
				if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
					
				}
				
				mav.addObject("pay_money", total_fee);
				mav.addObject("order_no", out_trade_no);
				
				mav.addObject("payType", "ZXZF");
				mav.setViewName("pay/pay_success");
			}else{
				mav.setViewName("pay/pay_fail");
				
			}
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping("/alipay/notify")
	public void alipayNotify(HttpServletRequest request, HttpServletResponse response){
		System.out.println("-------------------begin-------------------------alipay/notify---------------begin----------------------------");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		try{
			
			CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
			String KEY = company.getKEY();
			String PARTNER = company.getPARTNER();
			if(company.getACCOUNT_WAY().equals("3")){
				KEY = AlipayConfig.key; PARTNER = AlipayConfig.partner;
			}
			
//			int lockStatus = ServiceProxyByCFactory.getProxy().getReSeatlock();
//			if(lockStatus==1){
//				while(true){
//					lockStatus = ServiceProxyByCFactory.getProxy().getReSeatlock();
//					if(lockStatus==0) break;
//					Thread.currentThread().sleep(300l);
//				}
//			}
			
//			System.out.println("*************************************** alipayNotify  begin *****************************");
			Map<String,Object> p = new HashMap<String,Object>();
			//获取支付宝POST过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号
			String order_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			//获取总金额
			String total_fee = new String(request.getParameter("total_fee").getBytes("ISO-8859-1"),"UTF-8");
			//公共参数
			System.out.println("request.getParameter('extra_common_param')---------------------"+request.getParameter("extra_common_param"));
			//买家支付宝账号
			String buyer_email = new String(request.getParameter("buyer_email").getBytes("ISO-8859-1"),"UTF-8");
//			Enumeration names = request.getParameterNames();
//			while (names.hasMoreElements()) {
//				String name = (String) names.nextElement();
//				System.out.println(request.getParameter(name));
//			}
			//支付信息
			String subject = request.getParameter("subject");
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			
			//日志参数设置
			int payWay = 1;
			boolean b = AlipayNotify.verify(params, KEY, PARTNER);
			System.out.println("***********************************bbbbbbbbbbbbbbbbb**********************");
			System.out.println(b);
			System.out.println("************************************bbbbbbbbbbbbbbbbbb**************************");
			if(b){//验证成功
				if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//如果有做过处理，不执行商户的业务程序
					//在线充值 
						System.out.println("*************************************** NO  "+order_no+" *****************************");
						p.put("NO", order_no);
						this.orderService.onlinePayCallBackService(p);
					
				} else {
					//重复订单
				}
				out.write("success");
			}else{
				try {
						//验证失败,给淘宝提示成功,平台手动解决支付失败问题
						p.clear();
						p.put("NO", order_no);
						p.put("start", 1);
						p.put("end", 1);
						Map<String, Object> order = this.orderService.searchOrderBase(p).get(0);
						p.clear();
						p.put("ID", (String)order.get("ID"));
						p.put("CREATE_USER", (String)order.get("CREATE_USER"));
						p.put("CREATE_USER_ID", (String)order.get("CREATE_USER_ID"));
						p.put("RS", 2);
						this.orderService.reSeatByEntityIdService(p);
					out.write("success");
				} catch (Exception e) {
					e.printStackTrace();
					out.write("success");
				}
			}
//			System.out.println("*************************************** alipayNotify  end *****************************");
		}catch(Exception e){
			try {
				String order_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
				String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
				AlipayCore.logResult("掉单，业务处理异常！订单编号："+order_no+",支付宝单号："+trade_no+",异常明显："+e.getMessage()+"");
				try {
						/**
						 * 保存掉单信息
						 */
						Map<String, Object> p = new HashMap<String, Object>();
						p.put("NO", order_no);
						p.put("start", 1);
						p.put("end", 1);
						Map<String, Object> order = this.orderService.searchOrderBase(p).get(0);

						p.clear();
						p.put("ID", order.get("ID"));
						this.orderService.orderLost(p);
						
						p.clear();
						p.put("ID", CommonUtils.uuid());
						p.put("ORDER_ID", order.get("ID"));
						p.put("TITLE", "掉单");
						p.put("STEP_USER", order.get("WAP_USER"));
						p.put("STEP_USER_ID", order.get("WAP_USER_ID"));
						p.put("REMARK", "");
						this.orderService.saveOrderStep(p);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			} catch (UnsupportedEncodingException e1) {
				//验证失败,给淘宝提示成功,平台手动解决支付失败问题
				out.write("success");
				e1.printStackTrace();
			}
			//验证失败,给淘宝提示成功,平台手动解决支付失败问题
			out.write("success");
			e.printStackTrace();
		}
		System.out.println("-------------------end-------------------------alipay/notify---------------end----------------------------");
	}
}
