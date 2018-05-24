package cn.sd.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
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
import cn.sd.core.web.ExtSupportAction;
import cn.sd.entity.produce.CompanyDetail;
import cn.sd.service.common.ICommonService;
import cn.sd.service.order.IOrderService;
import cn.sd.service.pay.IPayService;
import cn.sd.service.route.IRouteService;
import cn.sd.service.user.IUserService;
import cn.sd.service.visitor.IVisitorService;

@RequestMapping("/order")
@Controller("MiniShopOrderController")
public class OrderController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(OrderController.class);

	@Resource
	private IUserService userService;
	@Resource
	private IOrderService orderService;
	@Resource
	private IRouteService routeService;
	@Resource
	private ICommonService commonService;
	@Resource
	private IPayService payService;
	@Resource
	private IVisitorService visitorService;
	
	@ResponseBody
	@RequestMapping("/cancel")
	public ListRange cancel(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		ListRange json = new ListRange();
		try {
			Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
			CompanyDetail company = (CompanyDetail)request.getSession().getAttribute("company");
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("ID", orderId);
			params.put("start", 1);
			params.put("end", 1);
			Map<String, Object> data = this.orderService.searchOrderBase(params).get(0);
			
			String name = (String)webUser.get("USER_NAME");

			params.clear();
			params.put("ORDER_ID", orderId);
			params.put("CREATE_USER", company.getENTITY_NAME());
			params.put("CREATE_USER_ID", company.getENTITY_ID());
			params.put("REMARK", "微店会员:"+name+",取消订单");
			params.put("CONTRACT_AGENCY_NO", (String)data.get("CON_NO"));
			this.orderService.minishopOrderCancel(params);
			
			params.clear();
			params.put("ID", orderId);
			params.put("start", 1);
			params.put("end", 1);
			data = this.orderService.searchOrderBase(params).get(0);
			String IS_ALONE = String.valueOf(data.get("IS_ALONE"));
			String SOURCES = String.valueOf(data.get("SOURCES"));
			params.put("COMPANY_ID", (String)data.get("COMPANY_ID"));
			params.put("CREATE_TIME", (String)data.get("CREATE_TIME"));
			if(IS_ALONE.equals("0") && SOURCES.equals("0")){
				params.put("ACCOUNT", "0");
				params.put("ACCOUNT_TYPE", "0");
				params.put("CREATE_COMPANY_ID", data.get("CREATE_COMPANY_ID"));
			}else{
				params.put("ACCOUNT", "-1");
				params.put("ACCOUNT_TYPE", "1");
				params.put("CREATE_COMPANY_ID", data.get("CREATE_COMPANY_ID"));
				params.put("ACCOUNT_COMPANY_ID", data.get("CREATE_COMPANY_ID"));
			}
			this.orderService.editOrderAccountService(params);
				
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("取消订单异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, String endDate, String beginDate, String status){
		Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
		ListRange json = new ListRange();
		mr.pm.put("WAP_USER_ID", webUser.get("ID"));
		
		mr.pm.put("BEGIN_DATE", beginDate);
		mr.pm.put("END_DATE", endDate);
		
		if(CommonUtils.checkString(query)){
			try {
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("query", query);
				request.setAttribute("query", query);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		int start = toInt(request.getParameter("start"));
		int limit = toInt(request.getParameter("limit"));
		
		mr.pm.put("start", start+1);
		mr.pm.put("end", start+limit);
		
		int totalSize = this.orderService.countOrderBase(mr.pm);
		List<Map<String, Object>> data = this.orderService.searchOrderBase(mr.pm);
		json.setData(data);
		json.setTotalSize(totalSize);
		return json;
	}
		
	@ResponseBody
	@RequestMapping("/detail") 
	public Map<String, Object> detail(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		mr.pm.clear();
		mr.pm.put("ID", orderId);
		mr.pm.put("start", 1);
		mr.pm.put("end", 1);
		List<Map<String, Object>> orders = this.orderService.searchOrderBase(mr.pm);
		Map<String, Object> order = new HashMap<String, Object>();
		if(CommonUtils.checkString(orders) && orders.size() == 1){
			order = orders.get(0);
			
			mr.pm.clear();
			mr.pm.put("ORDER_ID", orderId);
			List<Map<String, Object>> visitors = this.visitorService.searchVisitor(mr.pm);
			order.put("visitors", visitors);
			
			List<Map<String, Object>> traffics = this.orderService.searchOrderRouteTraffic(mr.pm);
			order.put("traffics", traffics);
			
			List<Map<String, Object>> contacts = this.orderService.listOrderContact(mr.pm);
			order.put("contacts", contacts);
		}
		
		return order;
	}
	
	@RequestMapping("/pay")
	public ModelAndView pay(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId, String bankCode, String platfrom, String appName){
		/**
		 * 微店只支持支付宝支付
		 * bankCode = 1 默认支付宝
		 */
		bankCode = "1";
		
		CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
		ListRange json = new ListRange();
		String url = "pay/alipay/wapalipayto";
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
			double m = 0.01;
//		    double m = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//			double m = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT"))) - DISCOUNT_TOTAL_AMOUNT;
			String orderNo = (String)order.get("NO");
			
			Assert.hasText(orderNo);
			Assert.notNull(m);
			Map<String, Object> result = null;
			////判断是支付宝网银 or 财付通网银  默认支付宝网银
			String payWay = bankCode;
			
			if ("1".equals(bankCode)) {
				result = this.payService.wapalipay(orderNo, bankCode, m, false, USER_ID, "ZXZF", company, appName);	
				request.setAttribute("sParaTemp", result.get("sParaTemp"));
			}else if("2".equals(bankCode)){
				result = this.payService.cftpay(orderNo, bankCode, m, false, USER_ID, request, response);
				request.setAttribute("requestUrl", result.get("requestUrl"));
				url = "pay/cft/tenpayto";
			}else {
				payWay = "3";
				result = this.payService.wapalipay(orderNo, bankCode, m, false, USER_ID, "ZXZF", company, appName);
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
	
	@ResponseBody
	@RequestMapping("/save") 
	public ListRange save(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
		CompanyDetail company = (CompanyDetail)request.getSession().getAttribute("company");
		Map<String, Object> params = new HashMap<String, Object>();
		ListRange json = new ListRange();
		params.put("ID", request.getParameter("routeId"));
		List<Map<String, Object>> routes = this.routeService.searchRouteDetail(params);
		if(!CommonUtils.checkList(routes) || routes.size() != 1){
			json.setSuccess(false);
			return json;
		}
		Map<String, Object> route = routes.get(0);
		//旅行社信息
		params.clear();
		params.put("ID", company.getENTITY_ID());
		List<Map<String, Object>> users = this.userService.searchUser(params);
		if(!CommonUtils.checkList(users) || users.size() != 1){
			json.setSuccess(false);
			return json;
		}
		Map<String, Object> user = users.get(0);
		
		//站信息
		params.clear();
		params.put("CITY_ID", (String)user.get("CITY_ID"));
		List<Map<String, Object>> sites = this.commonService.searchSiteManager(params);
		if(!CommonUtils.checkList(sites) || sites.size() != 1){
			json.setSuccess(false);
			return json;
		}
		Map<String, Object> site = sites.get(0);
		
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
		params.put("START_DATE", DateUtil.parseDate(DateUtil.subDate(request.getParameter("startDate")), "yyyy-MM-dd"));
		params.put("store", request.getParameter("store"));
		params.put("counselor", company.getENTITY_ID());
		params.put("PLAN_ID", request.getParameter("planId"));
		
		params.put("SINGLE_ROOM_CNT", request.getParameter("singleRoomCount"));
		
		params.put("START_CITY", request.getParameter("beginCity"));
		params.put("END_CITY", request.getParameter("endCity"));
		
		String visitors = (String)request.getParameter("visitors");
	    JSONArray jarray = JSONArray.fromObject(visitors);
		Object[] objArray = jarray.toArray();
		String[] keyNames = {"NAME", "ATTR_NAME", "MOBILE", "CARD_TYPE", "CARD_NO", "ATTR_ID"};
		List<Map<String, Object>> datas = this.jsonArrayToList(objArray, keyNames);

		String[] visitor_name = new String[datas.size()];
		String[] visitor_sex = new String[datas.size()];
		String[] visitor_type = new String[datas.size()];
		String[] visitor_mobile = new String[datas.size()];
		String[] visitor_id = new String[datas.size()];
		String[] visitor_id_no = new String[datas.size()];
		
		for (int i = 0; i < datas.size(); i++) {
			Map<String, Object> data = datas.get(i);
			visitor_name[i] = (String)data.get("NAME");
			visitor_sex[i] = "0";
			visitor_type[i] = (String)data.get("ATTR_ID");
			visitor_mobile[i] = (String)data.get("MOBILE");
			visitor_id[i] = (String)data.get("CARD_TYPE");
			visitor_id_no[i] = (String)data.get("CARD_NO");
		}
		
		params.put("visitor_name", visitor_name);
		params.put("visitor_sex", visitor_sex);
		params.put("visitor_type", visitor_type);
		params.put("visitor_mobile", visitor_mobile);
		params.put("visitor_id", visitor_id);
		params.put("visitor_id_no", visitor_id_no);
		
		String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
		String IS_ALONE = String.valueOf(user.get("IS_ALONE"));
		
		if((COMPANY_TYPE.equals("2") || COMPANY_TYPE.equals("3") ||  COMPANY_TYPE.equals("4") ||  COMPANY_TYPE.equals("5") || COMPANY_TYPE.equals("6") || COMPANY_TYPE.equals("7")) && IS_ALONE.equals("1")){
			params.put("IS_ALONE", "1");
		}else{
			params.put("IS_ALONE", "0");
		}
		
//		/**
//		 * 如果微店是公司级别.那创建人保存顾问
//		 */
//		if(company.getUSER_NAME().equals(company.getENTITY_NAME())){
//			mr.pm.clear();
//			mr.pm.put("COMPANY_ID", company.getID());
//			mr.pm.put("STORE_ID", company.getID());
//			List<Map<String, Object>> counselors = this.userService.searchCompanyCounselor(mr.pm);
//			params.put("CREATE_USER", counselors.get(0).get("USER_NAME")); 
//			params.put("CREATE_USER_ID", counselors.get(0).get("ID"));
//			params.put("CREATE_COMPANY_ID", counselors.get(0).get("COMPANY_ID"));
//			params.put("CREATE_DEPART_ID", counselors.get(0).get("DEPART_ID"));
//		}else{
//			params.put("CREATE_USER", user.get("USER_NAME")); 
//			params.put("CREATE_USER_ID", user.get("ID"));
//			params.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
//			params.put("CREATE_DEPART_ID", user.get("DEPART_ID"));
//		}
		
		if(CommonUtils.checkString(company.getACCOUNT_USER_ID())){
			params.put("CREATE_USER", company.getACCOUNT_USER_NAME()); 
			params.put("CREATE_USER_ID", company.getACCOUNT_USER_ID());
			params.put("CREATE_COMPANY_ID", company.getID());
			params.put("CREATE_DEPART_ID", company.getDEPART_ID());
		}else{
			params.put("CREATE_USER", user.get("USER_NAME")); 
			params.put("CREATE_USER_ID", user.get("ID"));
			params.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
			params.put("CREATE_DEPART_ID", user.get("DEPART_ID"));
		}
		
		params.put("SITE_RELA_ID", site.get("ID"));
		params.put("PRODUCE_TYPE", 2);
		
		params.put("WAP_USER", webUser.get("USER_NAME"));
		params.put("WAP_USER_ID", webUser.get("ID"));
		params.put("COUNSELOR_ID", company.getENTITY_ID());
		String ORDER_ID = CommonUtils.uuid();
		params.put("ID", ORDER_ID);
		
		params.put("ACCOUNT_WAY", company.getACCOUNT_WAY());
		
		String[] contactName = {(String)request.getParameter("contactName")};
		String[] contactPhone = {(String)request.getParameter("contactPhone")};
		params.put("contact_name", contactName);
		params.put("contact_phone", contactPhone);
		
		try {
			params.put("PLAT_FROM", "2");
			Map<String, Object> result = this.orderService.saveOrder(params);
			boolean s = (Boolean)result.get("success");
			if(s){
				json.setMessage(ORDER_ID);
			}
			json.setSuccess(s);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			return json;
		}
		return json;
	}
	
	private List<Map<String, Object>> jsonArrayToList(Object[] objArray, String[] keyNames){
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
	    for(int i = 0; i < objArray.length; i++){
	    	JSONObject jobject = JSONObject.fromObject(objArray[i]);
	    	Map<String, Object> data = new HashMap<String, Object>();
	    	for (int j = 0; j < keyNames.length; j++) {
	    		boolean b = jobject.has(keyNames[j]);
	    		if(b){
	    			data.put(keyNames[j], jobject.getString(keyNames[j]));
	    		}
			}
	    	datas.add(data);
		}
		return datas;
	}
}
