package cn.sd.controller.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.BaseController;
import cn.sd.entity.RouteEntity;
import cn.sd.power.PowerFactory;
import cn.sd.service.b2b.IDiscountService;
import cn.sd.service.b2b.ISMSService;
import cn.sd.service.order.IOrderService;
import cn.sd.service.pay.IPayService;
import cn.sd.service.resource.IRouteService;
import cn.sd.service.site.ICompanyService;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController{

	private static Log log = LogFactory.getLog(OrderController.class);
	
	@Resource
	private IPayService payService;
	
	@Resource
	private IOrderService orderService;
	
	@Resource
	private ICompanyService companyService;
	@Resource
	private IDiscountService discountService;
	@Resource
	private IRouteService routeService;
	@Resource
	private cn.sd.service.produce.IRouteService produceRouteService;
	@Resource
	private ISMSService smsService;
	
	/**
	 * 充值记录
	 * @param request
	 * @param response
	 * @param bankCode
	 * @param money
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sms/pay/record")
	public ListRange smsPayRecord(HttpServletRequest request,HttpServletResponse response, MapRange mr, String beginTime, String endTime, String dateType){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			
			mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
			mr.pm.put("CREATE_BEGIN_DATE", beginTime);
			mr.pm.put("CREATE_END_DATE", endTime);
			if(CommonUtils.checkString(dateType) && dateType.equals("2")){
				mr.pm.remove("CREATE_BEGIN_DATE");
				mr.pm.remove("CREATE_END_DATE");
				mr.pm.put("PAY_BEGIN_DATE", beginTime);
				mr.pm.put("PAY_END_DATE", endTime);
			}
			int totalSize = this.smsService.countUserSmsLogService(mr.pm);
			List<Map<String, Object>> data = this.smsService.listUserSmsLogService(mr.pm);
			json.setTotalSize(totalSize);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询充值记录异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询充值记录异常");
		}
		return json;
	}
	
	
	@RequestMapping("/sms/pay")
	public ModelAndView smsPay(HttpServletRequest request,HttpServletResponse response, String bankCode, String money){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		/**
		 * 保存充值记录
		 */
		
		String payWay = bankCode;
		boolean isBank = false;
		if ("1".equals(bankCode)) {
			
		}else if("2".equals(bankCode)){
			
		}else {
			isBank = true;
			payWay = "3";
		}
		
		String no = DateUtil.getCurrDateTimeString();
		Map<String, Object> params = new HashMap<String, Object>();
		
		double cnt = Double.parseDouble(money) / 0.1;

		params.put("ID", CommonUtils.uuid());
		params.put("COMPANY_ID", user.get("COMPANY_ID"));
		params.put("COUNT", cnt);
		params.put("TYPE", 1);
		params.put("CREATE_USER", user.get("ID"));
		params.put("AMOUNT", money);
		params.put("PAY_STATUS", 0);
		params.put("PAY_TYPE", payWay);
		params.put("NO", no);
		smsService.saveUserSmsLogService(params);

		//--------------------------测试使用
		money = "0.01";
		//--------------------------测试使用
		
		Map<String, Object> result = null;
		Assert.hasText(no);
		Assert.notNull(money);
		result = this.payService.alipay(no, bankCode, Double.parseDouble(money), isBank, (String)user.get("ID"), "DXCZ", "0");
		request.setAttribute("sParaTemp", (Map<String, String>)result.get("sParaTemp"));
		return new ModelAndView("pay/alipay/alipayto");
	}
	
	@RequestMapping("/traffic")
	public ModelAndView traffic(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("order/traffic");
	}
	@RequestMapping("/route")
	public ModelAndView route(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("order/route");
	}
	
	@RequestMapping("/route/renew")
	public ModelAndView routeRenew(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("order/route/renew");
	}
	
	@RequestMapping("/route/renew/buy")
	public ModelAndView routeRenewBuy(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId, String orderId){
		if(CommonUtils.checkString(routeId)){
			
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");

			mr.pm.put("IS_ALONE", "0");
			mr.pm.put("USER_ID", (String)user.get("ID"));
			mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
			mr.pm = PowerFactory.getPower(request, response, "", "route-list", mr.pm);
			
			mr.pm.put("ID", routeId);
			mr.pm.put("start", 1);
			mr.pm.put("end", 1);
			
			List<Map<String, Object>> data = this.produceRouteService.listRenewService(mr.pm);
			
			Map<String,Object> obj = data.get(0);
			obj.put("FEATURE", obj.get("FEATURE").toString().replaceAll("(\r\n|\r|\n|\n\r)", "<br>")); 
			obj.put("NOTICE", obj.get("NOTICE").toString().replaceAll("(\r\n|\r|\n|\n\r)", "<br>")); 
			request.setAttribute("routeDetail", net.sf.json.JSONObject.fromObject(obj));
			
			mr.pm.clear();
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("TYPE", 5);
			List<Map<String, Object>> others = this.routeService.listRouteOtherService(mr.pm);
			for (Map<String, Object> map : others) {
				if(map.get("CONTENT") != null){
					map.put("CONTENT", map.get("CONTENT").toString().replaceAll("(\r\n|\r|\n|\n\r)", "<br>"));
				}
			}
			request.setAttribute("others", this.getJsonArray(others));
		}
		
		if(CommonUtils.checkString(orderId)){
			mr.pm.clear();
			mr.pm.put("ID", orderId);
			mr.pm.put("start", 1);
			mr.pm.put("end", 1);
			Map<String, Object> routeOrder = this.orderService.listRouteOrderService(mr.pm).get(0);
			request.setAttribute("detail", JSONObject.fromObject(routeOrder));
		}
		
		return new ModelAndView("order/route/renew/buy");
	}
	
	@RequestMapping("/online/pay")
	public ModelAndView onlinePay(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId, String bankCode, String platfrom, String isEarnest){
		ListRange json = new ListRange();
		String url = "pay/alipay/alipayto";
		try {
			
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String USER_ID = (String)user.get("ID");
			mr.pm.put("ID", orderId);
			mr.pm.put("start", 1);
			mr.pm.put("end", 1);
			Map<String, Object> order = this.orderService.listOrderService(mr.pm).get(0);
			
			if(!String.valueOf(order.get("STATUS")).equals("0")){
				return new ModelAndView("pay/more_pay_fail");
			}
			
			/**
			 * 保存优惠日志
			 * 优惠规则ID
			 * 平台 B2B APP
			 * 支付类型 在线 余额
			 */
			Map<String, Object> p = new HashMap<String, Object>();
			String SITE_RELA_ID = (String)order.get("SITE_RELA_ID");
			
			p.clear();
			p.put("ID", SITE_RELA_ID);
			p.put("start", 1);
			p.put("end", 1);
			List<Map<String, Object>> siteManagers = this.companyService.listSiteManagerService(p);
			Map<String, Object> siteManager = siteManagers.get(0);
			String SITE_COMPANY_ID = (String)siteManager.get("COMPANY_ID");
			
			p.clear();
			p.put("ORDER_ID", orderId);
			List<Map<String, Object>> discountOrders = this.discountService.listDiscountOrderService(p);
			String IS_EARNEST = String.valueOf(order.get("IS_EARNEST"));
			if(CommonUtils.checkList(discountOrders) && discountOrders.size() == 1 && IS_EARNEST.equals("1")){
				double INTER_AMOUNT = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT")));
				String PRODUCE_ID = (String)order.get("PRODUCE_ID");
				this.orderService.saveDiscountInfo((String)discountOrders.get(0).get("DISCOUNT_ID"), platfrom, "1", orderId, INTER_AMOUNT, PRODUCE_ID, SITE_COMPANY_ID, SITE_RELA_ID, mr.pm, order);
			}
			
			mr.pm.put("ID", orderId);
			mr.pm.put("start", 1);
			mr.pm.put("end", 1);
			order = this.orderService.listOrderService(mr.pm).get(0);
			double m = 0.0, EARNEST_RETAIL_AMOUNT = 0.0;
			RouteEntity routeEntity = null;
			if(CommonUtils.checkString(isEarnest) && isEarnest.equals("1") && IS_EARNEST.equals("0")){
				String PRODUCE_ID = (String)order.get("PRODUCE_ID");
				p.clear();
				p.put("ID", PRODUCE_ID);
				routeEntity = this.routeService.routeDetailService(p);
				if(routeEntity != null && CommonUtils.checkString(routeEntity.getIS_EARNEST()) && routeEntity.getIS_EARNEST().equals("1")){
						double PERSON_COUNT = Double.parseDouble(String.valueOf(order.get("MAN_COUNT"))) + Double.parseDouble(String.valueOf(order.get("CHILD_COUNT")));
						m = Double.parseDouble(routeEntity.getEARNEST_INTER()) * PERSON_COUNT;
						
						EARNEST_RETAIL_AMOUNT = Double.parseDouble(routeEntity.getEARNEST_RETAIL()) * PERSON_COUNT;
				}else{
					url = "pay/seat_fail";
					//线路不支持定金付款
				}
			}else{
				double DISCOUNT_TOTAL_AMOUNT = Double.parseDouble(String.valueOf(order.get("DISCOUNT_TOTAL_AMOUNT")));
				if(IS_EARNEST.equals("1")){
					/**
					 * 总金额 - 定金金额 - 优惠金额
					 */
					BigDecimal b = new BigDecimal(Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT"))) - Double.parseDouble(String.valueOf(order.get("EARNEST_INTER_AMOUNT"))) - DISCOUNT_TOTAL_AMOUNT);
					m = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}else{
					BigDecimal b = new BigDecimal(Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT"))) - DISCOUNT_TOTAL_AMOUNT);
					m = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
			    	
			}
		    
//			double m = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT"))) - DISCOUNT_TOTAL_AMOUNT;
			String orderNo = (String)order.get("NO");
//			m = 0.01;//--------------------------------测试使用
			Assert.hasText(orderNo);
			Assert.notNull(m);
			Map<String, Object> result = null;
			////判断是支付宝网银 or 财付通网银  默认支付宝网银
			String payWay = bankCode;
			if ("1".equals(bankCode)) {
				result = this.payService.alipay(orderNo, bankCode, m, false, USER_ID, "ZXZF", isEarnest);
				request.setAttribute("sParaTemp", (Map<String, String>)result.get("sParaTemp"));
			}else if("2".equals(bankCode)){
				result = this.payService.cftpay(orderNo, bankCode, m, false, USER_ID, request, response);
				request.setAttribute("requestUrl", result.get("requestUrl"));
				url = "pay/cft/tenpayto";
			}else {
				payWay = "3";
				result = this.payService.alipay(orderNo, bankCode, m, true, USER_ID, "ZXZF", isEarnest);
				request.setAttribute("sParaTemp", (Map<String, String>)result.get("sParaTemp"));
			}
			
			/**
			 * 占座
			 */
			mr.pm.clear();
			mr.pm.put("ORDER_ID", orderId);
			mr.pm.put("type", String.valueOf(order.get("PRODUCE_TYPE")));
			Map<String, Object> seatStatus = this.orderService.onlineSeat(mr.pm);
			if((Boolean)seatStatus.get("success") || IS_EARNEST.equals("1")){
				/**
				 * 修改订单支付状态为,支付中.
				 * STATUS	0待付款 1付款中 2已付款 3待退款 4退款中 5已退款 6手动取消订单 7系统自动取消
				 * 修改订单支付状态为,支付中.
				 */

				if(routeEntity != null && CommonUtils.checkString(routeEntity.getIS_EARNEST()) && routeEntity.getIS_EARNEST().equals("1") && IS_EARNEST.equals("0")){
					p.clear();
					p.put("ID",  order.get("ID"));
					p.put("EARNEST_DAY_COUNT", routeEntity.getEARNEST_DAY_COUNT());
					p.put("EARNEST_INTER", routeEntity.getEARNEST_INTER());
					p.put("EARNEST_RETAIL", routeEntity.getEARNEST_RETAIL());
					p.put("EARNEST_INTER_AMOUNT", m);
					p.put("EARNEST_RETAIL_AMOUNT", EARNEST_RETAIL_AMOUNT);
					p.put("EARNEST_TYPE", routeEntity.getEARNEST_TYPE());
					p.put("IS_EARNEST", 4);//定金付款中
					p.put("PAY_TYPE", payWay);
					this.orderService.updateOrderEarnestInfoService(p);
				}else{
					p.clear();
					p.put("ID", order.get("ID"));
					p.put("STATUS", 1);
					p.put("PAY_TYPE", payWay);
					this.orderService.morePayCheckService(p);
				}
			}else{
				url = "pay/seat_fail";
			}
		
		} catch (Exception e) {
			url = "pay/pay_fail";
			log.error("订单支付异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("订单支付异常");
		}
		return new ModelAndView(url);
	}
}
