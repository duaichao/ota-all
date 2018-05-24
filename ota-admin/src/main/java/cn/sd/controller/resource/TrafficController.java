package cn.sd.controller.resource;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sd.controller.site.AreaController;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.BaseController;
import cn.sd.power.PowerFactory;
import cn.sd.service.resource.IRouteService;
import cn.sd.service.resource.ITrafficService;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/resource/traffic")
public class TrafficController extends BaseController{

	private static Log log = LogFactory.getLog(AreaController.class);
	
	@Resource
	private ITrafficService trafficService;
	
	@Resource
	private ICompanyService companyService;
	@Resource
	private IRouteService routeService;
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response, MapRange mr, String query, String code, String isSale, String isPub){
		ListRange json = new ListRange();
		try {
			//得到当前用户名和ID
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("IS_SALE", isSale);
			mr.pm.put("IS_PUB", isPub);
			/**
			 * 管理员看所有数据
			 */
			mr.pm.put("USER_ID", (String)user.get("ID"));
			mr.pm.put("MANAGER_SITE", this.companyService.listSiteInfoService(mr.pm));
			mr.pm.put("supply-power", "depart");
			mr.pm = PowerFactory.getPower(request, response, "", "traffic-list", mr.pm);
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
			
			if("1".equals(code)){
				String START_CITY = (String)mr.pm.get("START_CITY");
				String END_CITY = (String)mr.pm.get("END_CITY");
				START_CITY = new String(START_CITY.getBytes("ISO-8859-1"), "UTF-8");
				END_CITY = new String(END_CITY.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("START_CITY", START_CITY);
				mr.pm.put("END_CITY", END_CITY);
			}
			mr.pm.put("IS_FULL_PRICE", "0");
			List<Map<String, Object>> data = trafficService.listService(mr.pm);
			int totalSize = trafficService.countService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询交通信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询交通信息异常");
		}
		return json;
	}
	
	@RequestMapping("/save")
	public ListRange save(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			//得到当前用户名和ID
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			//交通名称不成重复
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("TITLE", (String)mr.pm.get("TITLE"));
			params.put("start", 1);
			params.put("end", 2);
			params.put("NOTEQUALSID", mr.pm.get("ID"));
			int totalSize = this.trafficService.countService(params);
			if(totalSize > 0){
				json.setStatusCode("-1");
				json.setSuccess(false);
				return json;
			}
			mr.pm.put("UPDATE_USER", user.get("USER_NAME"));
			String ID = (String) mr.pm.get("ID");
			mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
			if(!CommonUtils.checkString(ID)){
				mr.pm.put("CREATE_USER", user.get("USER_NAME"));
				mr.pm.put("CREATE_USER_ID", user.get("ID"));
				mr.pm.put("ID", CommonUtils.uuid());
				mr.pm.put("CITY_ID", user.get("CITY_ID"));
				mr.pm.put("CITY_NAME", user.get("CITY_NAME"));
				this.trafficService.saveService(mr.pm);
			}else{
				this.trafficService.editService(mr.pm);
			}
			
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存交通信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存交通信息异常");
		}
		return json;
	}
	
	@RequestMapping("/del")
	public ListRange del(HttpServletRequest request,HttpServletResponse response,String models){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ID", jobject.getString("ID"));
				params.put("IS_DEL", 1);
				params.put("UPDATE_USER", (String)user.get("USER_NAME"));
				this.trafficService.editService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除交通异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/pub")
	public ListRange pub(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
				Map<String,Object> params = new HashMap<String,Object>();
				/**
				 * 发布了价格规则的交通才能被发布
				 */
				params.put("TRAFFIC_ID", mr.pm.get("ID"));
				params.put("IS_PUB", "1");
				List<Map<String, Object>> data = this.trafficService.listTrafficRuleService(params);
				if(CommonUtils.checkList(data)){
//					data = trafficService.listMusterService(params);
//					if(CommonUtils.checkList(data)){
						params.put("ID", mr.pm.get("ID"));
						params.put("IS_PUB", "1");
						params.put("IS_SALE", mr.pm.get("IS_SALE"));
						params.put("MIN_BUY", mr.pm.get("MIN_BUY"));
						params.put("UPDATE_USER", (String)user.get("USER_NAME"));
						params.put("PUB_TIME", DateUtil.getNowDateTimeString());
						params.put("PUB_USER", (String)user.get("USER_NAME"));
						this.trafficService.editService(params);
//					}else{
//						json.setStatusCode("-1");
//						json.setSuccess(false);
//						return json;
//					}
				}else{
					json.setStatusCode("-1");
					json.setSuccess(false);
					return json;
				}
				
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("发布交通异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/unpub")
	public ListRange unpub(HttpServletRequest request,HttpServletResponse response, String trafficId, String isSale){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			Map<String,Object> params = new HashMap<String,Object>();
			if(CommonUtils.checkString(trafficId)){
				/**
				 * 组团交通，被配置到了线路,需要先解除关系
				 */
				if(CommonUtils.checkString(isSale) && isSale.equals("0")){
					params.put("TRAFFIC_ID", trafficId);
					List<Map<String, Object>> data = this.routeService.listRouteTrafficDetailService(params);
					
					if(CommonUtils.checkList(data)){
						json.setData(data);
						json.setStatusCode("-1");//被配置到了线路
						json.setSuccess(false);
						return json;
					}
					
				}
				
				params.put("ID", trafficId);
				params.put("IS_PUB", "0");
				params.put("UPDATE_USER", (String)user.get("USER_NAME"));
				params.put("PUB_TIME", DateUtil.getNowDateTimeString());
				params.put("PUB_USER", (String)user.get("USER_NAME"));
				this.trafficService.editService(params);
				json.setSuccess(true);
			}else{
				json.setStatusCode("-1");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("停售交通异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/route/use")
	public ListRange routeUse(HttpServletRequest request,HttpServletResponse response, MapRange mr,  String trafficId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.clear();
			if(CommonUtils.checkString(trafficId)){
				mr.pm.put("TRAFFIC_ID", trafficId);
				List<Map<String, Object>> data = this.routeService.listRouteTrafficDetailService(mr.pm);
				json.setData(data);
				json.setSuccess(true);
			}
		} catch (Exception e) {
			log.error("停售交通异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/sale")
	public ListRange sale(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("UPDATE_USER", (String)user.get("USER_NAME"));
			this.trafficService.editService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("单卖交通信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/muster")
	public ListRange muster(HttpServletRequest request,HttpServletResponse response, MapRange mr, String trafficId){
		ListRange json = new ListRange();
		try {
			//得到当前用户名和ID
			mr.pm.put("TRAFFIC_ID", trafficId);
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			if(CommonUtils.checkString(trafficId)){
				data = trafficService.listMusterService(mr.pm);
			}
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询集合信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询集合信息异常");
		}
		return json;
	}
	
	@RequestMapping("/company/muster")
	public ListRange companyMuster(HttpServletRequest request,HttpServletResponse response, MapRange mr, String trafficId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			//得到当前用户名和ID
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
			data = trafficService.listMusterPlaceService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询集合信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询集合信息异常");
		}
		return json;
	}
	
	
	@RequestMapping("/muster/save")
	public ListRange saveMuster(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			//得到当前用户名和ID
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("UPDATE_USER", user.get("USER_NAME"));
			String ID = (String) mr.pm.get("ID");
			if(!CommonUtils.checkString(ID)){
				mr.pm.put("CREATE_USER", user.get("USER_NAME"));
				mr.pm.put("CREATE_USER_ID", user.get("ID"));
				mr.pm.put("ID", CommonUtils.uuid());
				this.trafficService.saveMusterService(mr.pm);
			}else{
				this.trafficService.editMusterService(mr.pm);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存集合信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存集合信息异常");
		}
		return json;
	}
	
	@RequestMapping("/muster/del")
	public ListRange delMuster(HttpServletRequest request,HttpServletResponse response,String models){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ID", jobject.getString("ID"));
				params.put("IS_DEL", 1);
				params.put("UPDATE_USER", (String)user.get("USER_NAME"));
				this.trafficService.editMusterService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除集合信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/price/type")
	public ListRange priceType(HttpServletRequest request,HttpServletResponse response, MapRange mr, String ruleId){
		ListRange json = new ListRange();
		try {
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			if(CommonUtils.checkString(ruleId)){
				mr.pm.put("ENTITY_ID", ruleId);	
				
				List<Map<String, Object>> priceAttrs = this.trafficService.listPriceAttrService(mr.pm);
				StringBuffer sql = new StringBuffer();
				for (Map<String, Object> priceAttr : priceAttrs) {
					sql.append(" ,sum(decode(c_order_by,"+priceAttr.get("ORDER_BY").toString()+",price,0)) as "+priceAttr.get("CON_NAME").toString()+" ");
				}
				mr.pm.put("sql", sql.toString());
				data = this.trafficService.listPriceTypeService(mr.pm);
			}else{
				Map<String, Object> retail_data = new HashMap<String, Object>();
				retail_data.put("TITLE", "外卖");
				retail_data.put("ID", "0FA5123749D28C87E050007F0100BCAD");
				retail_data.put("CHENGREN", 0);
				retail_data.put("ERTONG", 0);
				retail_data.put("YINGER", 0);
				data.add(retail_data);
				
				Map<String, Object> inter_data = new HashMap<String, Object>();
				inter_data.put("TITLE", "同行");
				inter_data.put("ID", "0FA5123749D38C87E050007F0100BCAD");
				inter_data.put("CHENGREN", 0);
				inter_data.put("ERTONG", 0);
				inter_data.put("YINGER", 0);
				data.add(inter_data);
			}
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询价格类型异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询价格类型异常");
		}
		return json;
	}
	
	@RequestMapping("/price/attr")
	public ListRange priceAttr(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			List<Map<String, Object>> data = this.trafficService.listPriceAttrService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询价格属性异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询价格属性异常");
		}
		return json;
	}
	
	@RequestMapping("/price/rule")
	public ListRange priceRule(HttpServletRequest request,HttpServletResponse response, MapRange mr, String trafficId, String isFullPrice, String fullPriceFromList){
		ListRange json = new ListRange();
		try {
			mr.pm.put("TRAFFIC_ID", trafficId);
			if(CommonUtils.checkString(fullPriceFromList) && fullPriceFromList.equals("1")){
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("ROUTE_ID", trafficId);
				List<Map<String, Object>> d = this.routeService.listRouteTrafficDetailService(p);
				mr.pm.put("TRAFFIC_ID", d.get(0).get("TRAFFIC_ID"));
			}
			List<Map<String, Object>> data = this.trafficService.listTrafficRuleService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询佳通价格异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询交通价格异常");
		}
		return json;
	}
	
	@RequestMapping("/price/rule/del")
	public ListRange priceRuleDel(HttpServletRequest request, HttpServletResponse response, MapRange mr, String models){
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				mr.pm.clear();
				mr.pm.put("ID", jobject.getString("ID"));
				this.trafficService.delTrafficRuleService(mr.pm);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除交通价格异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/price/rule/pub")
	public ListRange priceRulePub(HttpServletRequest request,HttpServletResponse response, MapRange mr, String models){
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				mr.pm.clear();
				mr.pm.put("ID", jobject.getString("ID"));
				mr.pm.put("IS_SALE", jobject.getString("IS_SALE"));
				this.trafficService.pubTrafficRuleService(mr.pm);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("发布交通价格异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/price/rule/save")
	public ListRange priceRuleSave(HttpServletRequest request,HttpServletResponse response, MapRange mr, String prices, String trafficId, String trafficPub, String isSale, String isFullPrice, String fullPriceFromList){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		
		try {
			if(DateUtil.subDate((String)mr.pm.get("END_DATE")).compareTo(DateUtil.parseDate(DateUtil.getNowDate())) == -1){
				json.setSuccess(false);
				json.setStatusCode("-3");
				return json;
			}
			
			mr.pm.put("TRAFFIC_ID", trafficId);
			
			if(CommonUtils.checkString(isFullPrice) && isFullPrice.equals("1")){
				mr.pm.put("IS_PUB", "1");
			}
			
			List<Map<String, Object>> d = null;
			String end_date = "", end_time = "";
			Map<String, Object> p = new HashMap<String, Object>();
			if(CommonUtils.checkString(fullPriceFromList) && fullPriceFromList.equals("1")){
				mr.pm.put("IS_FULL_PRICE", isFullPrice);
				p.put("ROUTE_ID", trafficId);
				d = this.routeService.listRouteTrafficDetailService(p);
				mr.pm.put("TRAFFIC_ID", d.get(0).get("TRAFFIC_ID"));
				end_date = (String)d.get(0).get("END_DATE");
				end_time = (String)d.get(0).get("END_TIME");
			}else{
				p.put("ID", trafficId);
				p.put("start", 1);
				p.put("end", 1);
				d = this.trafficService.listService(p);
				end_date = (String)d.get(0).get("END_DATE");
				end_time = (String)d.get(0).get("END_TIME");
			}
			p.clear();
			p.put("END_DATE", mr.pm.get("END_DATE"));
			p.put("PAST_DATE", end_date);
			p.put("PAST_TIME", end_time);
			String c = this.routeService.checkPastDateService(p);
			if(c.equals("0")){
				json.setStatusCode("-3");
				json.setSuccess(false);
				return json;
			}
			mr.pm.put("TRAFFIC_PUB", trafficPub);
			mr.pm.put("IS_SALE", isSale);
			mr.pm.put("WEEKS", request.getParameterValues("WEEKS"));
			mr.pm.put("PRICES", prices);
			
			mr.pm.put("USER_ID", (String)user.get("ID"));
			mr.pm.put("USER_NAME", (String)user.get("USER_NAME"));
			String ID = (String) mr.pm.get("ID");
			Map<Object, Object> result = new HashMap<Object, Object>();
			if(!CommonUtils.checkString(ID)){
				mr.pm.put("ID", CommonUtils.uuid());
				mr.pm.put("editType", "save");
				result = this.trafficService.saveTrafficRuleService(mr.pm);
			}else{
				mr.pm.put("editType", "update");
				result = this.trafficService.saveTrafficRuleService(mr.pm);
				this.trafficService.editPubTrafficRuleService(mr.pm);
			}
			String statusCode = String.valueOf(result.get("statusCode"));
			if(CommonUtils.checkString(isFullPrice) && isFullPrice.equals("1") && statusCode.equals("1")){
				String RULE_ID = (String)result.get("RULE_ID");
				mr.pm.clear();
				mr.pm.put("traffic_rule_id", RULE_ID);
				this.trafficService.callProcedureRuleCompile(mr.pm);
				
				this.routeService.getOutTimeByruleIdService(RULE_ID);
			}
			
			json.setStatusCode(String.valueOf(result.get("statusCode")));
			json.setSuccess((Boolean)result.get("success"));
		} catch (Exception e) {
			log.error("保存交通价格异常",e);   
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存交通价格异常");
		}
		return json;
	}
	
	@RequestMapping("/dates")
	public ListRange trafficDates(HttpServletRequest request,HttpServletResponse response, MapRange mr, String trafficId, String month){
		ListRange json = new ListRange();
		try {
			mr.pm.put("TRAFFIC_ID", trafficId);
			mr.pm.put("FIRST_DATE", month.replaceAll("-", ""));
			mr.pm.put("LAST_DATE", DateUtil.getMonthLastDay(DateUtil.parseDate(month)).replaceAll("-", ""));
			mr.pm.put("start", 1);
			mr.pm.put("end", 10000);
			List<Map<String, Object>> trafficRuleCompiles = this.trafficService.listTrafficRuleCompileService(mr.pm);
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>(); 
			StringBuffer sql = new StringBuffer();
			
			String monthLastDay = DateUtil.getMonthLastDay(DateUtil.parseDate(month));
			mr.pm.put("BEGIN_DATE", month);
			mr.pm.put("END_DATE", monthLastDay);
			if(CommonUtils.checkList(trafficRuleCompiles)){
				for (int i = 0; i < trafficRuleCompiles.size(); i++) {
					sql.append((String)trafficRuleCompiles.get(i).get("RULE_SQL"));
					if((i + 1) != trafficRuleCompiles.size()){
						sql.append(" union all ");
					}
				}
				mr.pm.put("sql", sql.toString());
				data = this.trafficService.listTrafficRuleDateService(mr.pm);
			}
			mr.pm.remove("FIRST_DATE");
			mr.pm.remove("LAST_DATE");
			List<Map<String, Object>> _trafficRuleCompiles = this.trafficService.listTrafficRuleCompileService(mr.pm);
			int totalSize = 0;
			StringBuffer _sql = new StringBuffer();
			if(CommonUtils.checkList(_trafficRuleCompiles)){
				for (int i = 0; i < _trafficRuleCompiles.size(); i++) {
					_sql.append((String)_trafficRuleCompiles.get(i).get("RULE_SQL"));
					if((i + 1) != _trafficRuleCompiles.size()){
						_sql.append(" union all ");
					}
				}
				mr.pm.clear();
				mr.pm.put("sql", _sql.toString());
				totalSize = this.trafficService.countTrafficRuleDateService(mr.pm);
			}
			json.setTotalSize(totalSize);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询交通日期异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询交通日期异常");
		}
		return json;
	}
	
	@RequestMapping("/edit/seat")
	public ListRange editSeat(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		try {
			mr.pm.put("USER_ID", (String)user.get("ID"));
			Map<String, Object> result = this.trafficService.editSeatService(mr.pm);
			json.setSuccess((Boolean)result.get("success"));
			json.setMessage((String)result.get("message"));
			json.setStatusCode((String)result.get("statusCode"));
		} catch (Exception e) {
			log.error("修改交通日期座位异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("修改交通日期座位异常");
		}
		return json;
	}
	
}
