package cn.sd.controller.produce;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.BaseController;
import cn.sd.entity.RouteEntity;
import cn.sd.power.PowerFactory;
import cn.sd.service.b2b.IDiscountService;
import cn.sd.service.order.IOrderService;
import cn.sd.service.order.ITrafficOrderFacadeService;
import cn.sd.service.order.IVisitorOrderService;
import cn.sd.service.pay.IPayService;
import cn.sd.service.resource.IRouteService;
import cn.sd.service.resource.ITrafficService;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/produce")
public class ProduceController extends BaseController{

	private static Log log = LogFactory.getLog(ProduceController.class);
	
	@Resource
	private IOrderService orderService;
	@Resource
	private IPayService payService;
	@Resource
	private ITrafficOrderFacadeService trafficOrderFacadeService;
	@Resource
	private IVisitorOrderService visitorOrderService;
	@Resource
	private ICompanyService companyService;
	@Resource
	private IRouteService routeService;
	@Resource
	private ITrafficService trafficService;
	@Resource
	private cn.sd.service.produce.IRouteService produceRouteService;
	@Resource
	private IDiscountService discountService;
	
	@RequestMapping("/route")
	public ModelAndView route(HttpServletRequest request, HttpServletResponse response){
		String dynamicParamsSupplyName = request.getParameter("dynamicParamsSupplyName");
		if(CommonUtils.checkString(dynamicParamsSupplyName)){
				try {
					request.setAttribute("dynamicParamsSupplyName", new String(dynamicParamsSupplyName.getBytes("ISO-8859-1"), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
		}
		return new ModelAndView("produce/route");
	}
	@RequestMapping("/route/buy")
	public ModelAndView buySale(HttpServletRequest request, HttpServletResponse response, MapRange mr, String routeId, String planId, String goDate, String planName, String orderId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		mr.pm.put("IS_ALONE", "0");
		mr.pm.put("USER_ID", (String)user.get("ID"));
		mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
		mr.pm = PowerFactory.getPower(request, response, "", "route-list", mr.pm);
		mr.pm.put("start", 1);
		mr.pm.put("end", 50);
		mr.pm.put("ID", routeId);
		List<Map<String, Object>> data = null;
		mr.pm.remove("CITY_ID");
		if(!CommonUtils.checkString(planId)){
			mr.pm.put("IS_SINGLE_PUB", "1");
			data = this.produceRouteService.listSingleRouteService(mr.pm);
		}else{
			data = this.produceRouteService.listService(mr.pm);
		}
		
		if(!CommonUtils.checkList(data)){
			request.setAttribute("msg", "线路过期");
		}else{
			
			/**
			 * 交通是否过期
			 */
			mr.pm.clear();
			mr.pm.put("ORDER_ID", orderId);
			int c = 1;
			if(CommonUtils.checkString(orderId)){
				c = this.routeService.trafficPastService(mr.pm);
			}
			if(c == 0){
				request.setAttribute("msg", "交通过期");
			}else{
				Map<String,Object> obj = data.get(0);
				obj.put("FEATURE", obj.get("FEATURE").toString().replaceAll("(\r\n|\r|\n|\n\r)", "<br>")); 
				obj.put("NOTICE", obj.get("NOTICE").toString().replaceAll("(\r\n|\r|\n|\n\r)", "<br>")); 
				request.setAttribute("routeDetail", net.sf.json.JSONObject.fromObject(obj));
				
				mr.pm.clear();
				int num = 0;
				List<Map<String, Object>> attr = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> priceAttrs = this.trafficService.listPriceAttrService(mr.pm);
				for (int i = 0; i < priceAttrs.size(); i++) {
					String ID = (String)priceAttrs.get(i).get("ID");
					if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
						mr.pm.put("PID", "0FA5123749D08C87E050007F0100BCAD");
						List<Map<String, Object>> childs = this.trafficService.listPriceAttrService(mr.pm);
						for (Map<String, Object> map : childs) {
							attr.add(num, map);
							num++;
						}
					}
					attr.add(num, priceAttrs.get(i));
					num++;
				}
				
				if(CommonUtils.checkString(planId)){
					
					mr.pm.clear();
					mr.pm.put("ROUTE_ID", routeId);
					mr.pm.put("PLAN_ID", planId);
					mr.pm.put("RQ", goDate);
					List<Map<String, Object>> planPrices = this.routeService.listPlanPricesService(mr.pm);
					
					num = 0;
					List<Map<String, Object>> priceInfos = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < attr.size(); i++) {
						String CON_NAME = (String)attr.get(i).get("CON_NAME");
//						if(!CON_NAME.equals("ERTONG")){
							Map<String, Object> priceInfo = new HashMap<String, Object>();
							priceInfo.put("C_ORDER_BY", attr.get(i).get("ORDER_BY"));
							priceInfo.put("ATTR_ID", attr.get(i).get("ID"));
							priceInfo.put("ATTR_NAME", attr.get(i).get("TITLE"));
							
							priceInfo.put("WAIMAI", planPrices.get(0).get(CON_NAME));
							priceInfo.put("TONGHANG", planPrices.get(1).get(CON_NAME));
							priceInfos.add(num, priceInfo);
							num++;
//						}
					}
					request.setAttribute("basePrices", this.getJsonArray(priceInfos));
					
					mr.pm.clear();
					mr.pm.put("PLAN_ID", planId);
					List<Map<String, Object>> musters = this.produceRouteService.listRouteTrafficMusterService(mr.pm);
					request.setAttribute("musters", this.getJsonArray(musters));
					
				}else{
					mr.pm.clear();
					mr.pm.put("ROUTE_ID", routeId);
					List<Map<String, Object>> routePrice = this.routeService.listRoutePriceService(mr.pm);
					num = 0;
					List<Map<String, Object>> priceInfos = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < attr.size(); i++) {
						String CON_NAME = (String)attr.get(i).get("CON_NAME");
						if(!CON_NAME.equals("ERTONG")){
							Map<String, Object> priceInfo = new HashMap<String, Object>();
							priceInfo.put("C_ORDER_BY", attr.get(i).get("ORDER_BY"));
							priceInfo.put("ATTR_ID", attr.get(i).get("ID"));
							priceInfo.put("ATTR_NAME", attr.get(i).get("TITLE"));
							
							priceInfo.put("WAIMAI", routePrice.get(0).get(CON_NAME));
							priceInfo.put("TONGHANG", routePrice.get(1).get(CON_NAME));
							priceInfos.add(num, priceInfo);
							num++;
						}
					}
					request.setAttribute("basePrices", this.getJsonArray(priceInfos));
				}
				
				if(CommonUtils.checkString(planName)){
					try {
						planName = new String(planName.getBytes("ISO-8859-1"), "UTF-8");
						request.setAttribute("planName", planName);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				if(CommonUtils.checkString(orderId)){
					mr.pm.clear();
					mr.pm.put("ID", orderId);
					mr.pm.put("start", 1);
					mr.pm.put("end", 1);
					Map<String, Object> routeOrder = this.orderService.listRouteOrderService(mr.pm).get(0);
					request.setAttribute("detail", JSONObject.fromObject(routeOrder));
				}
			}
		}
		mr.pm.clear();
		mr.pm.put("PRO_ID", routeId);
		mr.pm.put("limitDate", "YES");
		List<Map<String, Object>> discount = this.discountService.listDiscountProductService(mr.pm);
		request.setAttribute("discount", this.getJsonArray(discount));
		
		mr.pm.put("ROUTE_ID", routeId);
		mr.pm.put("TYPE", 5);
		List<Map<String, Object>> others = this.routeService.listRouteOtherService(mr.pm);
		for (Map<String, Object> map : others) {
			if(map.get("CONTENT") != null){
				map.put("CONTENT", map.get("CONTENT").toString().replaceAll("(\r\n|\r|\n|\n\r)", "<br>"));
			}
		}
		request.setAttribute("others", this.getJsonArray(others));
		
		return new ModelAndView("produce/route/buy");
	}
	@RequestMapping("/traffic")
	public ModelAndView traffic(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("produce/traffic");
	}
	
	@RequestMapping("/paycenter")
	public ModelAndView paycenter(HttpServletRequest request,HttpServletResponse response, String orderId, String type, MapRange mr, String discountInfo){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String DEPART_ID = (String)user.get("DEPART_ID");
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("ID", orderId);
		p.put("start", 1);
		p.put("end", 10);
		//type=4是补单的状态 前台用
		List<Map<String, Object>> orders = null;
		Map<String, Object> order = new HashMap<String, Object>();
		if(CommonUtils.checkString(type) && (type.equals("2") || type.equals("3") || type.equals("4"))){
			if(type.equals("3")){
				p.put("TYPE", type);	
			}
			orders = this.orderService.listRouteOrderService(p);
			if(CommonUtils.checkList(orders) && orders.size() == 1){
				order = orders.get(0);
				if(!String.valueOf(order.get("STATUS")).equals("0")){
					request.setAttribute("payStatus", 2);
				}
				request.setAttribute("order", this.getJsonString(order));
				String ye = this.trafficOrderFacadeService.yeStatService(DEPART_ID);
				request.setAttribute("ye", ye);
			}
		}else{
			orders = this.orderService.listService(p);
			if(CommonUtils.checkList(orders) && orders.size() == 1){
				order = orders.get(0);
				if(!String.valueOf(order.get("STATUS")).equals("0")){
					request.setAttribute("payStatus", 2);
				}
				request.setAttribute("order", this.getJsonString(order));
				String ye = this.trafficOrderFacadeService.yeStatService(DEPART_ID);
				request.setAttribute("ye", ye);
			}
		}
		
		p.clear();
		
		mr.pm.put("valid", 1);
		mr.pm.put("IS_USE", 0);
		mr.pm.put("PRO_ID", order.get("PRODUCE_ID"));
		List<Map<String, Object>> discountOrders = this.discountService.listDiscountRuleService(mr.pm);

		if(discountOrders != null && discountOrders.size() > 1){
			
			String paramStr="";
			String[] pt = {"","B2B","APP"},
			pay = {"","在线支付","余额支付"},
			unit = {"","元","%","元/人"};
			
			for (int i = 0; i < discountOrders.size(); i++) {
				Map<String, Object> discountOrder = discountOrders.get(i);
				String str = "";
				str+=pt[Integer.parseInt(String.valueOf(discountOrder.get("PLATFROM")))];
				str+=pay[Integer.parseInt(String.valueOf(discountOrder.get("PAY_WAY")))];
				str+=" 优惠："+discountOrder.get("MONEY")+unit[Integer.parseInt(String.valueOf(discountOrder.get("RULE_TYPE")))];
				if(i>0){paramStr+="-";}
				paramStr+=str;
			}
			
			request.setAttribute("discountInfo", paramStr);
		}
		
		if(CommonUtils.checkList(orders) && orders.size() == 1 && (orders.get(0).get("PRODUCE_TYPE").toString().equals("2") || orders.get(0).get("PRODUCE_TYPE").toString().equals("3"))){
			p.clear();
			String PRODUCE_ID = (String)orders.get(0).get("PRODUCE_ID");
			p.put("ID", PRODUCE_ID);
			RouteEntity re = this.routeService.routeDetailService(p);

			RouteEntity routeEntity = new RouteEntity();
			routeEntity.setEARNEST_INTER(re.getEARNEST_INTER());
			routeEntity.setEARNEST_RETAIL(re.getEARNEST_RETAIL());
			routeEntity.setEARNEST_DAY_COUNT(re.getEARNEST_DAY_COUNT());
			routeEntity.setEARNEST_TYPE(re.getEARNEST_TYPE());
			routeEntity.setIS_EARNEST(re.getIS_EARNEST());
			
			request.setAttribute("routeEntity", this.getJsonString(routeEntity));
		}
		return new ModelAndView("produce/paycenter");
	}
	@RequestMapping("/payfinish")
	public ModelAndView payfinish(HttpServletRequest request,HttpServletResponse response, String orderId, String type){
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("ID", orderId);
		p.put("start", 1);
		p.put("end", 10);
		List<Map<String, Object>> orders = null;
		//type=4是补单的状态 前台用
		if(CommonUtils.checkString(type) && (type.equals("2") || type.equals("3") || type.equals("4"))){
			if(type.equals("3")){
				p.put("TYPE", type);	
			}
			orders = this.orderService.listRouteOrderService(p);
		}else{
			orders = this.orderService.listService(p);
		}
		
		if(CommonUtils.checkList(orders) && orders.size() == 1){
			Map<String, Object> order = orders.get(0);
			request.setAttribute("order", this.getJsonString(order));
			
			/**
			 * 优惠活动信息
			 */
			p.clear();
			p.put("ORDER_ID", orderId);
			List<Map<String, Object >> discountOrders = this.discountService.listDiscountOrderService(p);
			if(CommonUtils.checkList(discountOrders)){
				request.setAttribute("discount", this.getJsonString(discountOrders.get(0)));
			}
		}
		return new ModelAndView("produce/payfinish");
	}
	
	@RequestMapping("/onlinePay")
	public ModelAndView onlinePay(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId, String bankCode, String orderNo, String money){
		ListRange json = new ListRange();
		String url = "pay/alipay/alipayto";
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String USER_ID = (String)user.get("ID");
			Double m = Double.parseDouble(money);
				Assert.hasText(orderNo);
				Assert.notNull(money);
				Map<String, Object> result = null;
				//支付宝余额支付 or 网银支付
				if ("1".equals(bankCode)) {
					result = this.payService.alipay(orderNo, bankCode, m, false, USER_ID, "ZXFK", "");
					request.setAttribute("sParaTemp", result.get("sParaTemp"));
				}else if("2".equals(bankCode)){
					result = this.payService.cftpay(orderNo, bankCode, m, false, USER_ID, request, response);
					request.setAttribute("requestUrl", result.get("requestUrl"));
					url = "pay/cft/tenpayto";
				}else {
					//判断是支付宝网银 or 财付通网银  默认支付宝网银
					result = this.payService.alipay(orderNo, bankCode, m, true, USER_ID, "ZXFK", "");
					request.setAttribute("sParaTemp", result.get("sParaTemp"));
				}
		} catch (Exception e) {
			log.error("订单支付异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("订单支付异常");
		}
		return new ModelAndView(url);
	}
}
