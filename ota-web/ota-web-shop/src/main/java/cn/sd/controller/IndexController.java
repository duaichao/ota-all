package cn.sd.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.pay.weixin.WeixinUtil;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.IP;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.WebUtils;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.entity.RouteEntity;
import cn.sd.entity.produce.CompanyDetail;
import cn.sd.entity.produce.RouteDayDetail;
import cn.sd.service.common.ICommonService;
import cn.sd.service.route.IRouteService;
import cn.sd.service.traffic.ITrafficService;

@RequestMapping("/")
@Controller("MiniShopIndexController")
public class IndexController extends ExtSupportAction {
	private static Log log = LogFactory.getLog(IndexController.class);
	
	@Resource
	private IRouteService routeService;
	@Resource
	private ITrafficService trafficService;
	@Resource
	private ICommonService commonService;
	
	@ResponseBody
	@RequestMapping("/resource/route/favorite")
	public ListRange favorite(HttpServletRequest request, HttpServletResponse response, MapRange mr, String isDel, String routeId){
		Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
		ListRange json = new ListRange();
		try {
			
			if(CommonUtils.checkMap(webUser)){
				mr.pm.clear();
				if(CommonUtils.checkString(isDel)){
					mr.pm.put("PRODUCE_ID", routeId);
					mr.pm.put("USER_ID", webUser.get("ID"));
					this.commonService.delWebCollect(mr.pm);
				}else{
					mr.pm.put("ID", CommonUtils.uuid());
					mr.pm.put("USER_ID", webUser.get("ID"));
					mr.pm.put("PRODUCE_ID", routeId);
					mr.pm.put("TYPE", 0);
					this.commonService.saveWebCollect(mr.pm);
				}
			}
			json.setSuccess(true);
		}catch (Exception e) {
			log.error("收藏/取消收藏产品异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("收藏/取消收藏产品异常");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/resource/route/favorite/list")
	public ListRange cl(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, String routeType, String dayCount, String attr, String themes, String maxPrice,
			String minPrice, String beginDate, String endDate, String orderby, String cityName, String hot, String indexOrder){
		Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
		ListRange json = new ListRange();
		try {
			CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
			String company_id = company.getID();
			mr.pm.clear();
			mr.pm.put("start", 1);
			mr.pm.put("end", 10);
			mr.pm.put("BUY_COMPANY_ID", company_id);
			mr.pm.put("AGENCY_ID", company_id);
			mr.pm.put("NOTCOMPANYID", company_id);
			mr.pm.put("COMPANY_ID", company_id);
			mr.pm.put("ROUTE_TYPE", routeType);
			
			if(CommonUtils.checkMap(webUser)){
				mr.pm.put("COLLECT_USER_ID", webUser.get("ID"));
			}
			
			mr.pm.put("IS_ALONE", "0");
			if(CommonUtils.checkString(company.getIS_ALONE()) && company.getIS_ALONE().equals("1")){
				mr.pm.remove("IS_ALONE");
			}
			
			String PID = (String)request.getParameter("categoryId");
			
			/**
			 * 查询所有线路.按照订单数量倒叙排列
			 */
			if(!CommonUtils.checkString(hot)){
				mr.pm.put("PID", PID);
			}else{
				mr.pm.put("ORDER", "order_cnt");
			}
			
			mr.pm.put("IS_RECOMMEND", 1);
			
			/**
			 * 目的地城市
			 */
			List<Map<String, Object>> routeLabelAndCity = this.routeService.searchRouteLabelAndCity(mr.pm);
			request.setAttribute("routeLabelAndCity", routeLabelAndCity);
			
			mr.pm.put("DAY_COUNT", dayCount);
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
			if(CommonUtils.checkString(cityName)){
				cityName = new String(cityName.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("END_CITY", cityName);
			}
			if(CommonUtils.checkString(attr)){
				attr = new String(attr.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("ATTR", attr);
			}
			if(CommonUtils.checkString(themes)){
				themes = new String(themes.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("themes", themes.split(","));
			}
			mr.pm.put("orderby", orderby);
			mr.pm.put("MAX_PRICE", maxPrice);
			mr.pm.put("MIN_PRICE", minPrice);
			mr.pm.put("BEGIN_DATE", beginDate);
			mr.pm.put("END_DATE", endDate);
			
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			
			mr.pm.put("WAP_ID", company.getWAP_ID());
			mr.pm.put("index_order", indexOrder);
			List<Map<String, Object>> routes = this.commonService.searchWebCollect(mr.pm);
			int totalSize = this.commonService.countWebCollect(mr.pm);
			json.setTotalSize(totalSize);
			json.setData(routes);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询minisho线路列表异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询minisho线路列表异常");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/get/weixin/sign")
	public Map<String, Object> getWeiXinSign(HttpServletRequest request, HttpServletResponse response, String url){
		return WeixinUtil.getWxConfig(request, url);
	}
	
	@ResponseBody
	@RequestMapping("/applogout")
	public ListRange applogout(HttpServletRequest request, HttpServletResponse response){
		ListRange json = new ListRange();
		json.setData(new ArrayList());
		json.setSuccess(false);
		json.setMessage("appout"); 
		return json;
	}
	
	@ResponseBody
	@RequestMapping("")
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response, MapRange mr, String appName){
		CompanyDetail companyDetail = (CompanyDetail)request.getSession().getAttribute("company");
		List<Map<String, Object>> categorys = (List<Map<String, Object>>)request.getSession().getAttribute("categorys");
		companyDetail.setCategorys(categorys);
		request.getSession().setAttribute("companyJSON", JSONObject.fromObject(companyDetail));
		return new ModelAndView("/weidian");
	}
	
	@ResponseBody
	@RequestMapping("/index")
	public CompanyDetail index(HttpServletRequest request, HttpServletResponse response, MapRange mr, String appName){
		CompanyDetail companyDetail = (CompanyDetail)request.getSession().getAttribute("company");
		List<Map<String, Object>> categorys = (List<Map<String, Object>>)request.getSession().getAttribute("categorys");
		companyDetail.setCategorys(categorys);
		return companyDetail;
	}
	
	@ResponseBody
	@RequestMapping("/start/city")
	public Map<String, Object> startCity(HttpServletRequest request, HttpServletResponse response, MapRange mr, String appName){
		Map<String, Object> result = new HashMap<String, Object>();
		String s_start_city = request.getParameter("s_start_city");
		if(!CommonUtils.checkString(s_start_city)){
			s_start_city = (String)request.getSession().getAttribute("s_start_city");
			if(!CommonUtils.checkString(s_start_city)){
				List<String> begin_citys = (List<String>)request.getSession().getAttribute("begin_citys");
				if(CommonUtils.checkList(begin_citys) && begin_citys.size() > 0){
					
					String areaInfo =  "";
					if(CommonUtils.checkString(WebUtils.getIpAddress(request))){
						areaInfo = Arrays.toString(IP.find(WebUtils.getIpAddress(request)));
					}
					for (String city : begin_citys) {
						if(areaInfo.indexOf(city) != -1){
							request.getSession().setAttribute("s_start_city", city);
							break;
						}
					}
					s_start_city = (String)request.getSession().getAttribute("s_start_city");
					if(!CommonUtils.checkString(s_start_city)){
						for (String city : begin_citys) {
							s_start_city = city;
							request.getSession().setAttribute("s_start_city", city);
							break;
						}
					}
				}
			}
		}else{
			try {
				s_start_city = new String(s_start_city.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			request.getSession().setAttribute("s_start_city", s_start_city);
		}
		
		request.getSession().setAttribute("s_start_city", s_start_city);
		
		result.put("s_start_city", s_start_city);
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/resource/route/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, String routeType, String dayCount, String attr, String themes, String maxPrice,
			String minPrice, String beginDate, String endDate, String orderby, String cityName, String hot, String indexOrder){
		ListRange json = new ListRange();
		try {
			Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
			CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
			String company_id = company.getID();
			mr.pm.clear();
			mr.pm.put("start", 1);
			mr.pm.put("end", 10);
			mr.pm.put("BUY_COMPANY_ID", company_id);
			mr.pm.put("AGENCY_ID", company_id);
			mr.pm.put("NOTCOMPANYID", company_id);
			mr.pm.put("COMPANY_ID", company_id);
			mr.pm.put("ROUTE_TYPE", routeType);
			
			if(CommonUtils.checkMap(webUser)){
				mr.pm.put("COLLECT_USER_ID", webUser.get("ID"));
			}
			
			mr.pm.put("IS_ALONE", "0");
			if(CommonUtils.checkString(company.getIS_ALONE()) && company.getIS_ALONE().equals("1")){
				mr.pm.remove("IS_ALONE");
			}
			
			String PID = (String)request.getParameter("categoryId");
			
			/**
			 * 查询所有线路.按照订单数量倒叙排列
			 */
			if(!CommonUtils.checkString(hot)){
				mr.pm.put("PID", PID);
			}else{
				mr.pm.put("ORDER", "order_cnt");
			}
			
			mr.pm.put("IS_RECOMMEND", 1);
			
			/**
			 * 目的地城市
			 */
			List<Map<String, Object>> routeLabelAndCity = this.routeService.searchRouteLabelAndCity(mr.pm);
			request.setAttribute("routeLabelAndCity", routeLabelAndCity);
			
			mr.pm.put("DAY_COUNT", dayCount);
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
			if(CommonUtils.checkString(cityName)){
				cityName = new String(cityName.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("END_CITY", cityName);
			}
			if(CommonUtils.checkString(attr)){
				attr = new String(attr.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("ATTR", attr);
			}
			if(CommonUtils.checkString(themes)){
				themes = new String(themes.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("themes", themes.split(","));
			}
			mr.pm.put("orderby", orderby);
			mr.pm.put("MAX_PRICE", maxPrice);
			mr.pm.put("MIN_PRICE", minPrice);
			mr.pm.put("BEGIN_DATE", beginDate);
			mr.pm.put("END_DATE", endDate);
			
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			
			mr.pm.put("WAP_ID", company.getWAP_ID());
			mr.pm.put("index_order", indexOrder);
			List<Map<String, Object>> routes = this.routeService.searchRoute(mr.pm);
			int totalSize = this.routeService.countRoute(mr.pm);
			json.setTotalSize(totalSize);
			json.setData(routes);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询minisho线路列表异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询minisho线路列表异常");
		}
		return json;
	}
	
	
	@ResponseBody
	@RequestMapping("/resource/route/detail")
	public RouteEntity detail(HttpServletRequest request, HttpServletResponse response, MapRange mr, String routeId, String routeType){
		try {
			Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
			
 			CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
			String company_id = company.getID();
			mr.pm.clear();
			mr.pm.put("ID", routeId);
			if(CommonUtils.checkMap(webUser)){
				mr.pm.put("COLLECT_USER_ID", webUser.get("ID"));
			}
			RouteEntity routeEntity = this.routeService.routeDetailEntity(mr.pm);
			if(routeEntity == null){
				
			}
			mr.pm.clear();
			mr.pm.put("start", 1);
			mr.pm.put("end", 10);
			mr.pm.put("BUY_COMPANY_ID", company_id);
			mr.pm.put("AGENCY_ID", company_id);
			mr.pm.put("NOTCOMPANYID", company_id);
			mr.pm.put("COMPANYID", company_id);
			mr.pm.put("COMPANY_ID", company_id);
			mr.pm.put("ROUTE_TYPE", routeType);
//			mr.pm.put("CITY_ID", company.getCITY_ID());

			
			
			String s_start_city = (String)request.getSession().getAttribute("s_start_city");
			if(!CommonUtils.checkString(s_start_city)){
				List<String> begin_citys = (List<String>)request.getSession().getAttribute("begin_citys");
				for (String c : begin_citys) {
					mr.pm.put("BEGIN_CITY", c);
					break;
				}
			}else{
				mr.pm.put("BEGIN_CITY", s_start_city);
			}
			
			mr.pm.put("IS_ALONE", "0");
			if(CommonUtils.checkString(company.getIS_ALONE()) && company.getIS_ALONE().equals("1")){
				mr.pm.remove("IS_ALONE");
			}
			mr.pm.put("ID", routeId);
			mr.pm.put("WAP_ID", company.getWAP_ID());
			
//			String PID = (String)request.getParameter("categoryPID");
//			
//			mr.pm.put("PID", PID);
			mr.pm.put("IS_RECOMMEND", 1);
			
			/**
			 * 线路行程
			 */
			mr.pm.clear();
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("AGENCY_ID", company_id);
			mr.pm.put("START_DATE", DateUtil.getMonthOneDay(new Date()).replaceAll("-", ""));
			mr.pm.put("END_DATE", DateUtil.getMonthLastDay(new Date()).replaceAll("-", ""));
			List<Map<String, Object>> calendar = this.routeService.searchCalendar(mr.pm);
			mr.pm.remove("START_DATE");
			mr.pm.remove("END_DATE");
			routeEntity.setCalendar(calendar);
			
			mr.pm.remove("AGENCY_ID");
			
			List<Map<String, Object>> album = this.routeService.saerchRouteAlbum(mr.pm);
			routeEntity.setAlbum(album);
			
			mr.pm.put("TYPE", 1);
			List<Map<String, Object>> include = this.routeService.searchRouteOther(mr.pm);
			routeEntity.setInclude(include);
			
			mr.pm.put("TYPE", 2);
			List<Map<String, Object>> noclude = this.routeService.searchRouteOther(mr.pm);
			routeEntity.setNoclude(noclude);
			
			mr.pm.put("TYPE", 3);
			List<Map<String, Object>> notice = this.routeService.searchRouteOther(mr.pm);
			routeEntity.setNotice(notice);
			
			mr.pm.put("TYPE", 4);
			List<Map<String, Object>> tips = this.routeService.searchRouteOther(mr.pm);
			routeEntity.setTips(tips);
			
			mr.pm.remove("TYPE");
			String city_name = "";
			Map<String, Object> start_city = null;
			List<Map<String, Object>> citys_temp = new ArrayList<Map<String, Object>>();
			
			List<Map<String, Object>> citys = this.routeService.searchRouteCity(mr.pm);
			for (Map<String, Object> city : citys) {
				String ROUTE_CITY_ID = (String)city.get("ID");
				mr.pm.put("ROUTE_CITY_ID", ROUTE_CITY_ID);
				
				if(!city.get("TYPE").toString().equals("1")){
					List<Map<String, Object>> days = this.routeService.searchRouteDay(mr.pm);
					for (Map<String, Object> day : days) {
						String DAY_ID = (String)day.get("ID");
						mr.pm.clear();
						mr.pm.put("DAY_ID", DAY_ID);
						List<RouteDayDetail> details = this.routeService.searchRouteDayDetail(mr.pm);
						for (RouteDayDetail detail : details) {
							String DETAIL_ID = detail.getID();
							mr.pm.clear();
							mr.pm.put("DAY_DETAIL_ID", DETAIL_ID);
							List<Map<String, Object>> scenics = this.routeService.searchRouteScenic(mr.pm);
							detail.setScenics(scenics);
						}
						day.put("details", details);
					}
					city.put("days", days);
				}else{
					if(start_city==null) start_city = city;
					citys_temp.add(city);
					city_name = city_name+","+city.get("CITY_NAME").toString();
				}
			}
			
			start_city.put("CITY_NAME", city_name.substring(1, city_name.length()));
			citys.removeAll(citys_temp);
			citys.add(0,start_city);
			
			routeEntity.setCitys(citys);
			
			String[] beginCitys = routeEntity.getBEGIN_CITY().toString().split(",");
			List<Map<String, String>> bc = new ArrayList<Map<String, String>>();
			for (int i = 0; i < beginCitys.length; i++) {
				Map<String, String> c = new HashMap<String, String>();
				c.put("value", beginCitys[i]);
				c.put("text", beginCitys[i]);
				bc.add(c);
			}
			routeEntity.setStartCity(bc);
			return routeEntity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/resource/route/calendar")
	public ListRange calendar(HttpServletRequest request, HttpServletResponse response, MapRange mr, String routeId, String startDate){
		ListRange json = new ListRange();
		try {
 			CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
			String company_id = company.getID();
			/**
			 * 线路行程
			 */
			mr.pm.clear();
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("AGENCY_ID", company_id);
			mr.pm.put("START_DATE", startDate.replaceAll("-", ""));
			mr.pm.put("END_DATE", DateUtil.getLastDayOfMonty(DateUtil.parseDate(startDate)).replaceAll("-", ""));
			List<Map<String, Object>> calendar = this.routeService.searchCalendar(mr.pm);
			json.setData(calendar);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/resource/route/plan")
	public ListRange routePlan(HttpServletRequest request, HttpServletResponse response, MapRange mr, String routeId, String startDate){
		CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("AGENCY_ID", company.getID());
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("RQ", startDate.replaceAll("-", ""));
			List<Map<String, Object>> routeCalendar = this.routeService.searchCalendar(mr.pm);
			String[] PLANIDGROUP = routeCalendar.get(0).get("PLANIDGROUP").toString().split(",");
			StringBuffer PLANIDS = new StringBuffer();
			for (String PLANID : PLANIDGROUP) {
				PLANIDS.append("'"+PLANID+"',");
			}
			mr.pm.clear();
			mr.pm.put("IDS", PLANIDS.toString().subSequence(0, PLANIDS.toString().length()-1));
			List<Map<String, Object>> plans = this.routeService.searchRouteTraffic(mr.pm);
			json.setData(plans);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/resource/route/price")
	public ListRange routePrice(HttpServletRequest request, HttpServletResponse response, MapRange mr, String routeId, String startDate, String planId){
		CompanyDetail company = (CompanyDetail)request.getSession().getAttribute("company");
		ListRange json = new ListRange();
		try {
			mr.pm.put("AGENCY_ID", company.getID());
			mr.pm.put("ROUTE_ID", routeId);
			List<Map<String, Object>> wapPrices = this.routeService.searchWapPrice(mr.pm);
			double wapPrice = 0.0;
			if(CommonUtils.checkList(wapPrices) && wapPrices.size() == 1){
				wapPrice = Double.parseDouble(String.valueOf(wapPrices.get(0).get("PRICE")));
			}
			startDate = startDate.replaceAll("-", "");
			mr.pm.clear();
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("RQ", startDate);
			mr.pm.put("PLAN_ID", planId);
			List<Map<String, Object>> planPrices = this.routeService.listPlanPricesService(mr.pm);
			if(wapPrice > 0){
				for (Map<String, Object> planPrice : planPrices) {
					planPrice.put("CHENGREN", Double.parseDouble(String.valueOf(planPrice.get("CHENGREN"))) + wapPrice);
					planPrice.put("ERTONG", Double.parseDouble(String.valueOf(planPrice.get("ERTONG"))) + wapPrice);
				}
			}
			json.setData(planPrices);			
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/resource/route/attr")
	public ListRange priceAttr(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			List<Map<String, Object>> attrs = this.trafficService.searchPriceAttr(mr.pm);
			for (Map<String, Object> attr : attrs) {
				String ID = (String)attr.get("ID");
				if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
					mr.pm.put("PID", ID);
					List<Map<String, Object>> childs = this.trafficService.searchPriceAttr(mr.pm);
					attr.put("childs", childs);
				}
			}
			json.setData(attrs);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询价格属性异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询价格属性异常");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/authentication")
	public Map<String, Object> authentication(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		Map<String, Object> webUser = null;
		try {
			webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
			if(!CommonUtils.checkMap(webUser)){
				webUser = new HashMap<String, Object>();
				webUser.put("success", false);
			}else{
				webUser.put("success", true);
			}
		} catch (Exception e) {
			webUser.put("success", false);
		}
		return webUser;
	}
	
	@ResponseBody
	@RequestMapping("/resource/route/city/list")
	public ListRange routeEndCity(HttpServletRequest request, HttpServletResponse response, MapRange mr, String categoryId){
		ListRange json = new ListRange();
		try {
			CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
			if(CommonUtils.checkString(categoryId)){
				mr.pm.put("CATEGORY_ID", categoryId);
				mr.pm.put("WAP_ID", company.getWAP_ID());
				List<Map<String, Object>> data = this.routeService.searchRecommendCity(mr.pm);
				json.setData(data);
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("查询minisho线路列表异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询minisho线路列表异常");
		}
		return json;
	}
}
