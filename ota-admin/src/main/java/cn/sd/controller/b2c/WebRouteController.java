package cn.sd.controller.b2c;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.power.PowerFactory;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/b2c/route")
public class WebRouteController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(WebRouteController.class);
	
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("")
	public ModelAndView b2cRoute(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("b2c/route");
	}
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, 
			String routeType, String area, String citys, String supplys, String routeThemes, String routeAttrs, String isSinglePub, 
			String routeId, String BEGIN_DATE, String END_DATE, String isRecommend, String companyId, String parentId, String recommendType,
			String categoryId, String filter){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			
//			3.国内游推荐   TYPE	线路分类默认0 1周边2国内3出境
//			4.出境游推荐    
//			6.周边游推荐
//			7.自由行推荐		线路属性
//			8.包机邮轮推荐		线路和主题属性
//			5.海岛度假推荐		主题属性
			if(CommonUtils.checkString(routeType)){
				if("2".equals(routeType)){
					mr.pm.put("ROUTE_TYPE", 2);
				}else if("3".equals(routeType)){
					mr.pm.put("ROUTE_TYPE", 3);
				}else if("1".equals(routeType)){
					mr.pm.put("ROUTE_TYPE", 1);
				}
			}
			
			mr.pm.put("IS_ALONE", "0");
			mr.pm.put("USER_ID", (String)user.get("ID"));
			mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
			mr.pm = PowerFactory.getPower(request, response, "", "route-list", mr.pm);
			
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
			if(CommonUtils.checkString(routeThemes) && !routeThemes.equals("[{}]")){
				List<String> listThemes = new ArrayList<String>();
				JSONArray _themes = JSONArray.fromObject(routeThemes);
				for (int i = 0; i < _themes.size(); i++) {
					JSONObject _theme = JSONObject.fromObject(_themes.get(i));
					listThemes.add(_theme.get("value").toString());
				}
				mr.pm.put("themes", listThemes);
			}
			if(CommonUtils.checkString(routeAttrs)){
				routeAttrs = new String(routeAttrs.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("ATTR", routeAttrs);
			}
			mr.pm.put("ID", routeId);
			mr.pm.put("NOTCOMPANYID", (String)user.get("COMPANY_ID"));
			mr.pm.put("AGENCY_ID", (String)user.get("COMPANY_ID"));
			mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
			mr.pm.put("IS_AUDIT", "1");
			
			if(CommonUtils.checkString((String)user.get("START_CITY_NAME"))){
				String[] START_CITY_NAME = user.get("START_CITY_NAME").toString().split(",");
				StringBuffer cityIds = new StringBuffer();
				for (String CITY_NAME : START_CITY_NAME) {
					cityIds.append("'"+CITY_NAME+"',");
				}
				mr.pm.remove("CITY_ID");
				mr.pm.remove("CITY_IDS");
				mr.pm.put("START_CITY_NAME", START_CITY_NAME);
				
			}
			
			List<Map<String, Object>> data = this.companyService.searchAuditRouteService(mr.pm);
			int totalSize = this.companyService.countAuditRouteService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询首页分类异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询首页分类异常");
		}
		return json;
	}
	
	@RequestMapping("/select")
	public ListRange listRoute(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, 
			String routeType, String area, String citys, String supplys, String routeThemes, String routeAttrs, String isSinglePub, 
			String routeId, String BEGIN_DATE, String END_DATE, String isRecommend, String companyId, String parentId, String recommendType,
			String categoryId, String filter, String supplyId){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		
//		3.国内游推荐   TYPE	线路分类默认0 1周边2国内3出境
//		4.出境游推荐    
//		6.周边游推荐
//		7.自由行推荐		线路属性
//		8.包机邮轮推荐		线路和主题属性
//		5.海岛度假推荐		主题属性
		
		if(CommonUtils.checkString(routeType)){
			if("2".equals(routeType)){
				mr.pm.put("ROUTE_TYPE", 2);
			}else if("3".equals(routeType)){
				mr.pm.put("ROUTE_TYPE", 3);
			}else if("1".equals(routeType)){
				mr.pm.put("ROUTE_TYPE", 1);
			}
		}
		
		mr.pm.put("IS_ALONE", "0");
		mr.pm.put("USER_ID", (String)user.get("ID"));
		mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
		mr.pm = PowerFactory.getPower(request, response, "", "route-list", mr.pm);
		
		try {
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
			if(CommonUtils.checkString(routeThemes) && !routeThemes.equals("[{}]")){
				List<String> listThemes = new ArrayList<String>();
				JSONArray _themes = JSONArray.fromObject(routeThemes);
				for (int i = 0; i < _themes.size(); i++) {
					JSONObject _theme = JSONObject.fromObject(_themes.get(i));
					listThemes.add(_theme.get("value").toString());
				}
				mr.pm.put("themes", listThemes);
			}
			if(CommonUtils.checkString(routeAttrs)){
				routeAttrs = new String(routeAttrs.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("ATTR", routeAttrs);
			}
			mr.pm.put("supplyId", supplyId);
			mr.pm.put("ID", routeId);
			mr.pm.put("NOTCOMPANYID", (String)user.get("COMPANY_ID"));
			mr.pm.put("AGENCY_ID", (String)user.get("COMPANY_ID"));
			mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
			
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
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
				
			}
			
			data = this.companyService.searchAuditRouteService(mr.pm);
			totalSize = this.companyService.countAuditRouteService(mr.pm);
			
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询首页推荐异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询首页推荐异常");
		}
		return json;
	}
	
	@RequestMapping("/select/supply")
	public ListRange listRouteSupply(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, 
			String routeType, String area, String citys, String supplys, String routeThemes, String routeAttrs, String isSinglePub, 
			String routeId, String BEGIN_DATE, String END_DATE, String isRecommend, String companyId, String parentId, String recommendType,
			String categoryId, String filter, String type, String supplyName){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");


//		3.国内游推荐   TYPE	线路分类默认0 1周边2国内3出境
//		4.出境游推荐    
//		6.周边游推荐
//		7.自由行推荐		线路属性
//		8.包机邮轮推荐		线路和主题属性
//		5.海岛度假推荐		主题属性
		
		if(CommonUtils.checkString(routeType)){
			if("2".equals(routeType)){
				mr.pm.put("ROUTE_TYPE", 2);
			}else if("3".equals(routeType)){
				mr.pm.put("ROUTE_TYPE", 3);
			}else if("1".equals(routeType)){
				mr.pm.put("ROUTE_TYPE", 1);
			}
		}
		
		mr.pm.put("IS_ALONE", "0");
		mr.pm.put("USER_ID", (String)user.get("ID"));
		mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
		mr.pm = PowerFactory.getPower(request, response, "", "route-list", mr.pm);
		
		try {
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
			if(CommonUtils.checkString(routeThemes) && !routeThemes.equals("[{}]")){
				List<String> listThemes = new ArrayList<String>();
				JSONArray _themes = JSONArray.fromObject(routeThemes);
				for (int i = 0; i < _themes.size(); i++) {
					JSONObject _theme = JSONObject.fromObject(_themes.get(i));
					listThemes.add(_theme.get("value").toString());
				}
				mr.pm.put("themes", listThemes);
			}
			if(CommonUtils.checkString(routeAttrs)){
				routeAttrs = new String(routeAttrs.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("ATTR", routeAttrs);
			}
			mr.pm.put("ID", routeId);
			mr.pm.put("NOTCOMPANYID", (String)user.get("COMPANY_ID"));
			mr.pm.put("AGENCY_ID", (String)user.get("COMPANY_ID"));
			mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
			
			if(CommonUtils.checkString(supplyName)){
				supplyName = new String(supplyName.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("supplyName", supplyName);
			}
			
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
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
				
			}
			
			data = this.companyService.searchWebRouteSupplyService(mr.pm);
			
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询首页推荐异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询首页推荐异常");
		}
		
		
		
//		
		return json;
	}
	
	@RequestMapping("/save")
	public ListRange save(HttpServletRequest request, HttpServletResponse response, MapRange mr, String models){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				mr.pm.put("ROUTE_ID", jobject.getString("ID"));
				mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
				mr.pm.put("CREATE_USER_ID", user.get("ID"));
				this.companyService.saveSaleRouteService(mr.pm);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("审核线路异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("审核线路异常");
		}
		return json;
	}
	
	@RequestMapping("/del")
	public ListRange del(HttpServletRequest request, HttpServletResponse response, MapRange mr, String models){
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				mr.pm.put("ID", jobject.getString("SALE_ROUTE_ID"));
				this.companyService.delSaleRouteService(mr.pm);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除审核线路异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("删除审核线路异常");
		}
		return json;
	}
	
}
