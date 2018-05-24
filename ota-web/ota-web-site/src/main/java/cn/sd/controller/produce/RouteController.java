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
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.core.web.taglib.PageUtil;
import cn.sd.entity.RouteEntity;
import cn.sd.entity.produce.CompanyDetail;
import cn.sd.entity.produce.RouteDayDetail;
import cn.sd.service.route.IRouteService;

@RequestMapping("/produce/route")
@Controller
public class RouteController extends ExtSupportAction {
	private static Log log = LogFactory.getLog(RouteController.class);

	@Resource
	private IRouteService routeService;
	
	@RequestMapping("/search")
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, MapRange mr, String id, String routeType){
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
		mr.pm.put("CITY_ID", company.getCITY_ID());
		
		mr.pm.put("IS_ALONE", "0");
		if(CommonUtils.checkString(company.getIS_ALONE()) && company.getIS_ALONE().equals("1")){
			mr.pm.remove("IS_ALONE");
		}
		
		/**
		 * 目的地城市
		 */
		List<Map<String, Object>> routeLabelAndCity = this.routeService.searchRouteLabelAndCity(mr.pm);
		request.setAttribute("routeLabelAndCity", routeLabelAndCity);
		
		return new ModelAndView("/produce/route/search");
	}
	
	@RequestMapping("/list")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, String routeType, String dayCount, String attr, String themes, String maxPrice,
			String minPrice, String beginDate, String endDate, String orderby, String city){
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
			
			String PID = (String)request.getParameter("categoryPID");
//			if(CommonUtils.checkString(PID)){
//				if(PID.equals("36666E8E0C9EEF38E050007F0100469B")){
//					mr.pm.put("ROUTE_TYPE", 2);
//				}else if(PID.equals("36666E8E0C9FEF38E050007F0100469B")){
//					mr.pm.put("ROUTE_TYPE", 3);
//				}else if(PID.equals("36666E8E0CA0EF38E050007F0100469B")){
//					String _themes = "海岛度假";
//					mr.pm.put("themes", _themes.split(","));
//				}else if(PID.equals("36666E8E0CA1EF38E050007F0100469B")){
//					mr.pm.put("ROUTE_TYPE", 1);
//				}else if(PID.equals("36666E8E0CA2EF38E050007F0100469B")){
//					mr.pm.put("ATTR", "自由行");
//				}else if(PID.equals("36666E8E0CA3EF38E050007F0100469B")){
//					mr.pm.put("ATTR", "包机");
//					String _themes = "邮轮";
//					mr.pm.put("themes", _themes.split(","));
//				}
//			}
			
			String companyPID = company.getPID();
			
			if(CommonUtils.checkString(companyPID) && !companyPID.equals("-1")){
				mr.pm.put("COMPANY_PID", companyPID);
			}
			
			mr.pm.put("PID", PID);
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
			if(CommonUtils.checkString(city)){
				city = new String(city.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("END_CITY", city);
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
			
			mr.pm.put("WAP_ID", company.getWAP_ID());
			
			int recordCount = this.routeService.countRoute(mr.pm);
			
			Map<Object, Object> pageResult = PageUtil.page(recordCount, 10, 10, 10, 2, 10, false, request);
			
			mr.pm.put("start", (Long)pageResult.get("start"));
			mr.pm.put("end", (Long)pageResult.get("end"));
			
			List<Map<String, Object>> routes = this.routeService.searchRoute(mr.pm);
			request.setAttribute("routes", routes);
			
			request.setAttribute("categoryPID", PID);
			
			request.setAttribute("attr", attr);
			request.setAttribute("themes", themes);
			request.setAttribute("dayCount", dayCount);
			request.setAttribute("query", query);
			request.setAttribute("city", city);
			
			request.setAttribute("maxPrice", maxPrice);
			request.setAttribute("minPrice", minPrice);
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("routeType", routeType);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("/produce/route/list");
	}
	
	@RequestMapping("/detail")
	public ModelAndView detail(HttpServletRequest request, HttpServletResponse response, MapRange mr, String id, String routeType){
		
		CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
		String company_id = company.getID();
		mr.pm.clear();
		mr.pm.put("ID", id);
		RouteEntity routeEntity = this.routeService.routeDetailEntity(mr.pm);
		if(routeEntity == null){
			return new ModelAndView("/commons/error");	
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
//		mr.pm.put("CITY_ID", company.getCITY_ID());
		
		mr.pm.put("IS_ALONE", "0");
		if(CommonUtils.checkString(company.getIS_ALONE()) && company.getIS_ALONE().equals("1")){
			mr.pm.remove("IS_ALONE");
		}
		mr.pm.put("ID", id);
		List<Map<String, Object>> routes = this.routeService.searchRoute(mr.pm);
		if(!CommonUtils.checkList(routes) || routes.size() != 1){
			return new ModelAndView("/commons/error");	
		}
		
		Map<String, Object> route = routes.get(0);
		route.put("FEATURE1", routeEntity.getFEATURE1());
		/**
		 * 线路行程
		 */
		mr.pm.clear();
		mr.pm.put("ROUTE_ID", id);
		mr.pm.put("AGENCY_ID", company_id);
		List<Map<String, Object>> calendar = this.routeService.searchCalendar(mr.pm);
		route.put("calendar", calendar);
		route.put("calendarJson", this.getJsonArray(calendar, new String[]{"D"}));
		mr.pm.remove("AGENCY_ID");
		
		List<Map<String, Object>> album = this.routeService.saerchRouteAlbum(mr.pm);
		route.put("album", album);
		
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
		
		route.put("citys", citys);
		
		request.setAttribute("route", route);
		return new ModelAndView("/produce/route/detail");
	}
}
