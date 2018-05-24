package cn.sd.controller.order;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.entity.produce.CompanyDetail;
import cn.sd.service.common.ICommonService;
import cn.sd.service.order.IOrderService;
import cn.sd.service.pay.IPayService;
import cn.sd.service.route.IRouteService;
import cn.sd.service.traffic.ITrafficService;
import cn.sd.service.user.IUserService;
import cn.sd.service.visitor.IVisitorService;

@Controller
@RequestMapping("/order")
public class OrderController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(OrderController.class);
	
	@Resource
	private IUserService userService;
	@Resource
	private IRouteService routeService;
	@Resource
	private ITrafficService trafficService;
	@Resource
	private IOrderService orderService;
	@Resource
	private ICommonService commonService;
	@Resource
	private IPayService payService;
	@Resource
	private IVisitorService visitorService;
	

	@RequestMapping("/cancel") 
	public ModelAndView orderCancel(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		mr.pm.clear();
		mr.pm.put("ORDER_ID", orderId);
		try {
			this.orderService.orderCancel(mr.pm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/user/sec/order");
	}
	
	@RequestMapping("/detail") 
	public ModelAndView detail(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		mr.pm.clear();
		mr.pm.put("ID", orderId);
		mr.pm.put("start", 1);
		mr.pm.put("end", 1);
		List<Map<String, Object>> orders = this.orderService.searchOrderBase(mr.pm);
		request.setAttribute("order", orders.get(0));
		mr.pm.clear();
		mr.pm.put("ORDER_ID", orderId);
		List<Map<String, Object>> visitors = this.visitorService.searchVisitor(mr.pm);
		request.setAttribute("visitors", visitors);
		
		List<Map<String, Object>> traffics = this.orderService.searchOrderRouteTraffic(mr.pm);
		request.setAttribute("traffics", traffics);
		
		List<Map<String, Object>> contacts = this.orderService.listOrderContact(mr.pm);
		request.setAttribute("contacts", contacts);
		return new ModelAndView("/user/center/orderinfo");
	}
	
	/**
	 * 线路
	 * 门店
	 * 顾问
	 * 交通
	 */
	@RequestMapping("/to/pay") 
	public ModelAndView toPay(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		String id = request.getParameter("id");
		if(CommonUtils.checkString(id)){
			mr.pm.put("ID", id);
			mr.pm.put("start", 1);
			mr.pm.put("end", 1);
			request.setAttribute("order", this.orderService.searchOrderBase(mr.pm).get(0));
			
			mr.pm.clear();
			mr.pm.put("ORDER_ID", id);
			List<Map<String, Object>> contacts = this.orderService.listOrderContact(mr.pm);
			request.setAttribute("contacts", contacts);
			
		}
		return new ModelAndView("/order/pay");
	}
	
	@RequestMapping("/pay")
	public ModelAndView pay(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId, String bankCode, String platfrom){
		CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
		ListRange json = new ListRange();
		String url = "pay/alipay/alipayto";
		String USER_AGENT = (String)request.getSession().getAttribute("USER_AGENT");
		if(USER_AGENT.equals("mobile")){
			url = "pay/alipay/wapalipayto";
		}
		try {
			
			String USER_ID = "";
			mr.pm.put("ID", orderId);
			mr.pm.put("start", 1);
			mr.pm.put("end", 1);
			Map<String, Object> order = this.orderService.searchOrderBase(mr.pm).get(0);
			
			
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
			List<Map<String, Object>> siteManagers = this.commonService.searchSiteManager(p);
			Map<String, Object> siteManager = siteManagers.get(0);
			String SITE_COMPANY_ID = (String)siteManager.get("COMPANY_ID");
			
			p.clear();
			p.put("ORDER_ID", orderId);
			List<Map<String, Object>> discountOrders = this.orderService.listDiscountOrderService(p);
			if(CommonUtils.checkList(discountOrders) && discountOrders.size() == 1){
				double INTER_AMOUNT = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT")));
				String PRODUCE_ID = (String)order.get("PRODUCE_ID");
				this.orderService.saveDiscountInfo((String)discountOrders.get(0).get("DISCOUNT_ID"), platfrom, "1", orderId, INTER_AMOUNT, PRODUCE_ID, SITE_COMPANY_ID, SITE_RELA_ID, mr.pm, order);
			}
			
			mr.pm.put("ID", orderId);
			mr.pm.put("start", 1);
			mr.pm.put("end", 1);
			order = this.orderService.searchOrderBase(mr.pm).get(0);
			double DISCOUNT_TOTAL_AMOUNT = Double.parseDouble(String.valueOf(order.get("DISCOUNT_TOTAL_AMOUNT")));
			
//			BigDecimal b = new BigDecimal(Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT"))) - DISCOUNT_TOTAL_AMOUNT);
//			double m = 0.01;
//		    double m = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double m = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT"))) - DISCOUNT_TOTAL_AMOUNT;
			String orderNo = (String)order.get("NO");
			
			Assert.hasText(orderNo);
			Assert.notNull(m);
			Map<String, Object> result = null;
			////判断是支付宝网银 or 财付通网银  默认支付宝网银
			String payWay = bankCode;
			
			if ("1".equals(bankCode)) {
				if(USER_AGENT.equals("pc")){
					result = this.payService.alipay(orderNo, bankCode, m, false, USER_ID, "ZXZF", company);	
				}else{
					result = this.payService.wapalipay(orderNo, bankCode, m, false, USER_ID, "ZXZF", company, "");
				}
				request.setAttribute("sParaTemp", result.get("sParaTemp"));
			}else if("2".equals(bankCode)){
				result = this.payService.cftpay(orderNo, bankCode, m, false, USER_ID, request, response);
				request.setAttribute("requestUrl", result.get("requestUrl"));
				url = "pay/cft/tenpayto";
			}else {
				payWay = "3";
				if(USER_AGENT.equals("pc")){
					result = this.payService.alipay(orderNo, bankCode, m, true, USER_ID, "ZXZF", company);	
				}else{
					result = this.payService.wapalipay(orderNo, bankCode, m, false, USER_ID, "ZXZF", company, "");
				}
				request.setAttribute("sParaTemp", result.get("sParaTemp"));
			}
			
			/**
			 * 占座
			 */
			mr.pm.clear();
			mr.pm.put("ORDER_ID", orderId);
			mr.pm.put("type", String.valueOf(order.get("PRODUCE_TYPE")));
			Map<String, Object> seatStatus = this.orderService.onlineSeat(mr.pm);
			if((Boolean)seatStatus.get("success")){
				/**
				 * 修改订单支付状态为,支付中.
				 * STATUS	0待付款 1付款中 2已付款 3待退款 4退款中 5已退款 6手动取消订单 7系统自动取消
				 */
				p.clear();
				p.put("ID", order.get("ID"));
				p.put("STATUS", 1);
				p.put("PAY_TYPE", payWay);
				this.orderService.updateOrderStatus(p);
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
	
	/**
	 * 线路
	 * 门店
	 * 顾问
	 * 交通
	 */
	@RequestMapping("/to/save") 
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, MapRange mr, String routeId, String d){
		String routeTitle = request.getParameter("routeTitle");
		String beginCity = request.getParameter("beginCity");
		String endCity = request.getParameter("endCity");
		
		try {
			if(CommonUtils.checkString(routeTitle)){
				request.setAttribute("ROUTE_TITLE", new String(routeTitle.getBytes("ISO-8859-1"), "UTF-8"));
			}
			if(CommonUtils.checkString(beginCity)){
				request.setAttribute("BEGIN_CITY", new String(beginCity.getBytes("ISO-8859-1"), "UTF-8"));
			}
			if(CommonUtils.checkString(endCity)){
				request.setAttribute("END_CITY", new String(endCity.getBytes("ISO-8859-1"), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		CompanyDetail company = (CompanyDetail)request.getSession().getAttribute("company");
		String company_id = company.getID();
		mr.pm.put("COMPANY_ID", company_id);
//		List<Map<String, Object>> stores = this.userService.searchCompanyCounselor(mr.pm);
//		request.setAttribute("stores", stores);
		
		mr.pm.put("COMPANY_ID", company_id);
//		mr.pm.put("STORE_ID", stores.get(0).get("COMPANY_ID"));
//		List<Map<String, Object>> counselors = this.userService.searchCompanyCounselor(mr.pm);
//		request.setAttribute("counselors", counselors);
		
		
		mr.pm.put("AGENCY_ID", company_id);
		mr.pm.put("ROUTE_ID", routeId);
		
		List<Map<String, Object>> wapPrices = this.routeService.searchWapPrice(mr.pm);
		double wapPrice = 0.0;
		if(CommonUtils.checkList(wapPrices) && wapPrices.size() == 1){
//			request.setAttribute("wapPrices", wapPrices.get(0));
			wapPrice = Double.parseDouble(String.valueOf(wapPrices.get(0).get("PRICE")));
		}
		
		mr.pm.put("RQ", d.replaceAll("-", ""));
		List<Map<String, Object>> routeCalendar = this.routeService.searchCalendar(mr.pm);
		String[] PLANIDGROUP = routeCalendar.get(0).get("PLANIDGROUP").toString().split(",");
		StringBuffer PLANIDS = new StringBuffer();
		for (String PLANID : PLANIDGROUP) {
			PLANIDS.append("'"+PLANID+"',");
		}
		mr.pm.clear();
		mr.pm.put("IDS", PLANIDS.toString().subSequence(0, PLANIDS.toString().length()-1));
		List<Map<String, Object>> plans = this.routeService.searchRouteTraffic(mr.pm);
		request.setAttribute("plans", plans);
		
		for (Map<String, Object> plan : plans) {
		
			mr.pm.clear();
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("RQ", d.replaceAll("-", ""));
			mr.pm.put("PLAN_ID", plan.get("PLANID"));
			List<Map<String,String>> planInfos = this.routeService.searchRouteTrafficInfo(mr.pm);
			plan.put("planInfos", planInfos);
			
			List<Map<String, Object>> planPrices = this.routeService.listPlanPricesService(mr.pm);
			if(wapPrice > 0){
				for (Map<String, Object> planPrice : planPrices) {
					planPrice.put("CHENGREN", Double.parseDouble(String.valueOf(planPrice.get("CHENGREN"))) + wapPrice);
					planPrice.put("ERTONG", Double.parseDouble(String.valueOf(planPrice.get("ERTONG"))) + wapPrice);
				}
			}
			
			plan.put("planPrices", planPrices);
			
			mr.pm.clear();
			mr.pm.put("TRAFFIC_ID", planInfos.get(0).get("TRAFFIC_ID"));
			List<Map<String, Object>> trafficMusters = this.trafficService.searchTrafficMuster(mr.pm);
			plan.put("trafficMusters", trafficMusters);
			
		}
		JSONArray plan_json = JSONArray.fromObject(plans);
		request.setAttribute("plan_json", plan_json);
		
		mr.pm.clear();
		mr.pm.put("ROUTE_ID", routeId);
		mr.pm.put("AGENCY_ID", company_id);
		List<Map<String, Object>> calendar = this.routeService.searchCalendar(mr.pm);
		request.setAttribute("calendarJson", this.getJsonArray(calendar, new String[]{"D"}));
		
		mr.pm.clear();
		mr.pm.put("ID", routeId);
		List<Map<String, Object>> routes = this.routeService.searchRouteDetail(mr.pm);
		if(CommonUtils.checkList(routes) && routes.size() == 1){
			
			Map<String, Object> route = routes.get(0);
			
			mr.pm.clear();
			
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("AGENCY_ID", company_id);
			
			mr.pm.put("TYPE", 1);
			List<Map<String, Object>> include = this.routeService.searchRouteOther(mr.pm);
			route.put("include", include);
			
			mr.pm.put("TYPE", 2);
			List<Map<String, Object>> noclude = this.routeService.searchRouteOther(mr.pm);
			route.put("noclude", noclude);
			
			mr.pm.put("TYPE", 3);
			List<Map<String, Object>> notice = this.routeService.searchRouteOther(mr.pm);
			route.put("notice", notice);
			
			mr.pm.put("TYPE", 4);
			List<Map<String, Object>> tips = this.routeService.searchRouteOther(mr.pm);
			route.put("tips", tips);
			
			request.setAttribute("route", route);
			
		}
		
//		request.setAttribute("manCnt", 3);
//		request.setAttribute("childCnt", 2);
		
		return new ModelAndView("/order/order");
	}
	
	@RequestMapping("/save") 
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("ID", request.getParameter("routeId"));
		List<Map<String, Object>> routes = this.routeService.searchRouteDetail(params);
		if(!CommonUtils.checkList(routes) || routes.size() != 1){
			return new ModelAndView("/order/order");
		}
		Map<String, Object> route = routes.get(0);
		//旅行社信息
		params.clear();
		String counselor_id = request.getParameter("counselor");
		params.put("ID", counselor_id);
		List<Map<String, Object>> users = this.userService.searchUser(params);
		if(!CommonUtils.checkList(users) || users.size() != 1){
			return new ModelAndView("/order/order");
		}
		Map<String, Object> user = users.get(0);

		//站信息
		params.clear();
		params.put("CITY_ID", (String)user.get("CITY_ID"));
		List<Map<String, Object>> sites = this.commonService.searchSiteManager(params);
		if(!CommonUtils.checkList(sites) || sites.size() != 1){
			return new ModelAndView("/order/order");
		}
		Map<String, Object> site = sites.get(0);
		
		/**
		 * 线路ID
		 * 出发日期
		 * 门店
		 * 顾问
		 * 方案
		 * 集合地点
		 * 游客
		 */
		CompanyDetail company = (CompanyDetail)request.getSession().getAttribute("company");
		
		params.put("AGENCY_ID", company.getID());
		params.put("ROUTE_ID", request.getParameter("routeId"));
		List<Map<String, Object>> wapPrices = this.routeService.searchWapPrice(params);
		
		params.clear();
		
		if(CommonUtils.checkList(wapPrices)){
			params.put("wapPrices", String.valueOf(wapPrices.get(0).get("PRICE")));	
		}
		if(CommonUtils.checkString(request.getParameter("muster"))){
			String[] muster = request.getParameter("muster").toString().split(",");
			
			params.put("MUSTER_TIME", muster[0]);
			params.put("MUSTER_PLACE", muster[1]);
		}
		
		params.put("WEB_COMPANY_ID", company.getID());
		params.put("route", route);
		params.put("RETAIL_SINGLE_ROOM", route.get("RETAIL_SINGLE_ROOM"));
		params.put("INTER_SINGLE_ROOM", route.get("INTER_SINGLE_ROOM"));
		params.put("routeId", request.getParameter("routeId"));
		params.put("START_DATE", request.getParameter("start_date"));
		params.put("store", request.getParameter("store"));
		params.put("counselor", request.getParameter("counselor"));
		params.put("PLAN_ID", request.getParameter("plan_id"));
		
		params.put("SINGLE_ROOM_CNT", request.getParameter("SINGLE_ROOM_CNT"));
		
		params.put("START_CITY", request.getParameter("start_city"));
		params.put("END_CITY", request.getParameter("end_city"));
		
		
		
		params.put("visitor_name", request.getParameterValues("visitor_name"));
		params.put("visitor_sex", request.getParameterValues("visitor_sex"));
		params.put("visitor_type", request.getParameterValues("visitor_type"));
		params.put("visitor_mobile", request.getParameterValues("visitor_mobile"));
		params.put("visitor_id", request.getParameterValues("visitor_id"));
		params.put("visitor_id_no", request.getParameterValues("visitor_id_no"));
		
		String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
		String IS_ALONE = String.valueOf(user.get("IS_ALONE"));
		
		if((COMPANY_TYPE.equals("2") || COMPANY_TYPE.equals("3") ||  COMPANY_TYPE.equals("4") ||  COMPANY_TYPE.equals("5") || COMPANY_TYPE.equals("6") || COMPANY_TYPE.equals("7")) && IS_ALONE.equals("1")){
			params.put("IS_ALONE", "1");
		}else{
			params.put("IS_ALONE", "0");
		}
		
		params.put("CREATE_USER", user.get("USER_NAME")); 
		params.put("CREATE_USER_ID", user.get("ID"));
		params.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
		params.put("CREATE_DEPART_ID", user.get("DEPART_ID"));
		params.put("SITE_RELA_ID", site.get("ID"));
		params.put("PRODUCE_TYPE", 2);
		
		params.put("WAP_USER", webUser.get("USER_NAME"));
		params.put("WAP_USER_ID", webUser.get("ID"));
		params.put("COUNSELOR_ID", counselor_id);
		String ORDER_ID = CommonUtils.uuid();
		params.put("ID", ORDER_ID);
		params.put("ACCOUNT_WAY", company.getACCOUNT_WAY());
		
		params.put("contact_name", request.getParameterValues("contact_name"));
		params.put("contact_phone", request.getParameterValues("contact_phone"));
		
		try {
			params.put("PLAT_FROM", 1);
			Map<String, Object> result = this.orderService.saveOrder(params);
			boolean s = (Boolean)result.get("success");
			if(!s){
				return new ModelAndView("/order/order");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("redirect:/order/to/pay?id="+ORDER_ID);
	}
}
