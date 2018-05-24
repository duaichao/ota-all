package cn.sd.controller.produce;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.BaseController;
import cn.sd.power.PowerFactory;
import cn.sd.service.site.ICompanyService;

@RestController
@Controller("produceRouteController")
@RequestMapping("/produce/route")
public class RouteController extends BaseController{

	private static Log log = LogFactory.getLog(RouteController.class);

	@Resource
	private cn.sd.service.produce.IRouteService produceRouteService;
	@Resource
	private ICompanyService companyService;
//	@Resource
//	private IOrderService orderService;
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, 
			String routeType, String area, String citys, String supplys, String themes, String attr, String isSinglePub, 
			String routeId, String BEGIN_DATE, String END_DATE, String price, String dayCount){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");

		mr.pm.put("IS_ALONE", "0");
		mr.pm.put("USER_ID", (String)user.get("ID"));
		mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
		mr.pm = PowerFactory.getPower(request, response, "", "route-list", mr.pm);
		
		try {
			if(CommonUtils.checkString(price)){
				if(price.indexOf("-") != -1){
					mr.pm.put("MIN_PRICE", price.split("-")[0]);
					mr.pm.put("MAX_PRICE", price.split("-")[1]);
				}else{
					mr.pm.put("MAX_PRICE", price);
				}
			}
			mr.pm.put("DAY_COUNT", dayCount);
			
			if(CommonUtils.checkString(attr)){
				attr = new String(attr.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("attr", attr);
			}
			
			mr.pm.put("ROUTE_TYPE", routeType);
			mr.pm.put("IS_SINGLE_PUB", isSinglePub);
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			mr.pm.put("BEGIN_DATE", BEGIN_DATE);
			mr.pm.put("END_DATE", END_DATE);
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
			
			if(CommonUtils.checkString(area) && CommonUtils.checkString(citys)){
				mr.pm.put("LABEL_IDS", citys.split(","));
			}else if(CommonUtils.checkString(area)){
				mr.pm.put("LABEL_IDS", new String[]{area});
			}
			
			if(CommonUtils.checkString(supplys)){
				String[] _supplys = supplys.split(",");
				List<String> listsupplys = new ArrayList<String>();
				for (int i = 0; i < _supplys.length; i++) {
					String supply = new String(_supplys[i].getBytes("ISO-8859-1"), "UTF-8");
					listsupplys.add(supply);
				}
				mr.pm.put("BRAND_NAME", listsupplys);
			}
			if(CommonUtils.checkString(themes)){
				
				String[] _themes = themes.split(",");
				List<String> listThemes = new ArrayList<String>();
				for (int i = 0; i < _themes.length; i++) {
					String theme = new String(_themes[i].getBytes("ISO-8859-1"), "UTF-8");
					listThemes.add(theme);
				}
				mr.pm.put("themes", listThemes);
			}
			mr.pm.put("ID", routeId);
			mr.pm.put("NOTCOMPANYID", (String)user.get("COMPANY_ID"));
			mr.pm.put("AGENCY_ID", (String)user.get("COMPANY_ID"));
			List<Map<String, Object>> data = null;
			int totalSize = 0;
			
			if(CommonUtils.checkString((String)user.get("START_CITY_NAME"))){
				String[] START_CITY_NAME = user.get("START_CITY_NAME").toString().split(",");
				StringBuffer cityIds = new StringBuffer();
				for (String CITY_NAME : START_CITY_NAME) {
					cityIds.append("'"+CITY_NAME+"',");
				}
				mr.pm.remove("CITY_ID");
				mr.pm.remove("CITY_IDS");
				mr.pm.put("START_CITY_NAME", START_CITY_NAME);
				
				if(CommonUtils.checkString(isSinglePub)){
					data = this.produceRouteService.listSingleRouteService(mr.pm);
					totalSize = this.produceRouteService.countSingleRouteService(mr.pm);
				}else{
					data = this.produceRouteService.listService(mr.pm);
					totalSize = this.produceRouteService.countService(mr.pm);
				}
				
			}
			
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路信息异常");
		}
		return json;
	}
	
	
	@RequestMapping("/list/company")
	public ListRange listCompany(HttpServletRequest request, HttpServletResponse response, MapRange mr, String isSinglePub, String type, String lineType){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		try {
			mr.pm.put("IS_ALONE", "0");
			mr.pm.put("USER_ID", (String)user.get("ID"));
			mr.pm.put("IS_SINGLE_PUB", isSinglePub);
			mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
			mr.pm.put("ROUTE_TYPE", type);
			mr.pm = PowerFactory.getPower(request, response, "", "route-list", mr.pm);
			//1:国内 2:出境
			if(CommonUtils.checkString(lineType)){
				if(lineType.equals("1")){
					mr.pm.put("IS_COUNTRY", 1);
				}else if(lineType.equals("2")){
					mr.pm.put("IS_WORLD", 1);
				}
			}
			List<Map<String, Object>> data = this.produceRouteService.listCompanyService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路供应商异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路供应商异常");
		}
		return json;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param mr
	 * @param type null： 查询区域， notNull： 查询城市
	 * @param lableId:区域ID
	 * @return
	 */
	@RequestMapping("/list/label")
	public ListRange listLabel(HttpServletRequest request, HttpServletResponse response, MapRange mr, String type, String labelId, String isSinglePub){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			
			mr.pm.put("IS_ALONE", "0");
			mr.pm.put("USER_ID", (String)user.get("ID"));
			mr.pm.put("IS_SINGLE_PUB", isSinglePub);
			mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
			mr.pm = PowerFactory.getPower(request, response, "", "route-list", mr.pm);
			
			mr.pm.put("TYPE", type);
			mr.pm.put("LABEL_ID", labelId);
			List<Map<String, Object>> data = this.produceRouteService.listLabelService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路区域异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路区域异常");
		}
		return json;
	}
	
	@RequestMapping("/info")
	public ListRange routeInfo(HttpServletRequest request, HttpServletResponse response, MapRange mr, String routeId, String startDate){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			/**
			 * 交通详情
			 * 方案
			 */
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("RQ", startDate);
			
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路区域异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路区域异常");
		}
		return json;
	}
	
	@RequestMapping("/traffic/muster")
	public ListRange trafficMuster(HttpServletRequest request, HttpServletResponse response, MapRange mr, String planId){
		ListRange json = new ListRange();
		try {
			mr.pm.put("PLAN_ID", planId);
			List<Map<String, Object>> data = this.produceRouteService.listRouteTrafficMusterService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路区域异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路区域异常");
		}
		return json;
	}
	
	@RequestMapping("/save/wap/price")
	public ListRange saveWapPrice(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		try {
			this.produceRouteService.delWapPriceService(mr.pm);
			mr.pm.put("ID", CommonUtils.uuid());
			mr.pm.put("AGENCY_ID", user.get("COMPANY_ID"));
			this.produceRouteService.saveWapPriceService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存网站价格异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存网站价格异常");
		}
		return json;
	}
	
//	@Resource
//	private IRouteService routeService;
//	@Resource
//	private ITrafficService trafficService;
	
//	@RequestMapping("/buy")
//	public ModelAndView buy(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
//		try {
//			List<Map<String, Object>> priceAttrs = this.trafficService.listAllPriceAttrService(mr.pm);
//			
//			mr.pm.clear();
//			mr.pm.put("ID", orderId);
//			
//			Map<String, Object> order = this.orderService.listRouteOrderService(mr.pm).get(0);
//			mr.pm.put("ROUTE_ID", (String)order.get("PRODUCE_ID"));
//			mr.pm.put("PLAN_ID", (String)order.get("PRODUCE_ID"));
//			mr.pm.put("RQ", ((String)order.get("START_DATE")).replaceAll("-", ""));
//			List<Map<String, Object>> planPrices = this.routeService.listPlanPricesService(mr.pm);
//			
//			int num = 0;
//			List<Map<String, Object>> priceInfos = new ArrayList<Map<String, Object>>();
//			for (int i = 0; i < priceAttrs.size(); i++) {
//				String CON_NAME = (String)priceAttrs.get(i).get("CON_NAME");
//				if(!CON_NAME.equals("ERTONG")){
//					Map<String, Object> priceInfo = new HashMap<String, Object>();
//					priceInfo.put("C_ORDER_BY", priceAttrs.get(i).get("ORDER_BY"));
//					priceInfo.put("ATTR_ID", priceAttrs.get(i).get("ID"));
//					priceInfo.put("ATTR_NAME", priceAttrs.get(i).get("TITLE"));
//					
//					priceInfo.put("WAIMAI", planPrices.get(0).get(CON_NAME));
//					priceInfo.put("TONGHANG", planPrices.get(1).get(CON_NAME));
//					priceInfos.add(num, priceInfo);
//					num++;
//				}
//			}
//			request.setAttribute("basePrices", this.getJsonArray(priceInfos));
//			
//			//订单
//			mr.pm.put("start", 1);
//			mr.pm.put("end", 10);
//			mr.pm.put("ID", orderId);
//			List<Map<String, Object>> data = this.orderService.listRouteOrderService(mr.pm);
//			request.setAttribute("detail", JSONObject.fromObject(data.get(0)));
//			
//		} catch (Exception e) {
//			log.error("购买线路信息异常",e);
//		}
//		return new ModelAndView("/produce/route/buy");
//	}
}
