package cn.sd.controller.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.site.ICompanyService;
import cn.sd.service.statistics.IStatisticsService;

@RestController
@RequestMapping("/stat/sale")
public class StatisticsSaleController extends ExtSupportAction{
	
	private static Log log = LogFactory.getLog(StatisticsSaleController.class);
	
	@Resource
	private IStatisticsService statisticsService; 
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("/order/retail")
	public ListRange orderRetail(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String type, String startDate, String endDate, String ctype, String ccityId, String departIds, String companyId, String dateType){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
		ListRange json = new ListRange();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(user);
			params.put("departIds", departIds);
			params.put("companyId", companyId);
			StatisticsPowerSaleController.powerCompany(mr, COMPANY_TYPE, cityId, params, "", (String)user.get("USER_NAME"));
			
//			mr.pm.put("startDate", startDate);
//			mr.pm.put("endDate", endDate);
			mr.pm.put("ctype", CommonUtils.checkString(ctype)?ctype:"0");
			mr.pm.put("STATUSES", "2,5");
			
			if(CommonUtils.checkString(dateType)){
				if(dateType.equals("2")){
					mr.pm.put("CT_START_TIME", startDate);
					mr.pm.put("CT_END_TIME", endDate);
				}else{
					mr.pm.put("startDate", startDate);
					mr.pm.put("endDate", endDate);
				}
			}
			
			List<Map<String, Object>> data = this.statisticsService.totalOrderRetailService(mr.pm);
			json.setData(data);
		} catch (Exception e) {
			log.error("查询异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询异常");
		}
		return json;
	}
	
	@RequestMapping("/order/company")
	public ListRange totalCompanyOfOrder(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String type, String startDate, String endDate, String ctype, String ccityId, String departIds){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
		ListRange json = new ListRange();
		try {
			if(CommonUtils.checkString(cityId) && !cityId.equals("all")){
				mr.pm.put("CITY_ID", cityId);
			}else if(!user.get("USER_NAME").toString().equals("admin")){
				if(COMPANY_TYPE.equals("0")){
					mr.pm.clear();
					mr.pm.put("USER_ID", user.get("ID"));
					List<Map<String, Object>> sites = this.companyService.listSiteInfoService(mr.pm);
					StringBuffer cityIds = new StringBuffer();
					for (Map<String, Object> site : sites) {
						cityIds.append("'"+site.get("CITY_ID").toString()+"',");
					}
					mr.pm.put("CITY_IDS", cityIds.toString().substring(0, cityIds.length()-1));
				}else if(COMPANY_TYPE.equals("1")){
					String USER_ID = (String)user.get("ID");
					String COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID");
					
					if(CommonUtils.checkString(departIds)){
						JSONArray _departIds = JSONArray.fromObject(departIds);
				    	StringBuffer ids = new StringBuffer();
				    	for (Object departId : _departIds) {
				    		ids.append(departId+",");
						}
				    	mr.pm.put("SUPPLY_DEPART_IDS", ids.toString().substring(0, ids.length() -1 ).split(","));
				    }else if(USER_ID.equals(COMPANY_USER_ID)){
				    	mr.pm.put("SUPPLY_COMPANY_ID", (String)user.get("COMPANY_ID"));
					}else{
						String DEPART_IDS = (String)user.get("DEPART_IDS");
						if(CommonUtils.checkString(DEPART_IDS)){
							DEPART_IDS =DEPART_IDS +","+user.get("DEPART_ID");
							mr.pm.put("SUPPLY_DEPART_IDS", DEPART_IDS.split(","));
						}else{
							mr.pm.put("SUPPLY_DEPART_ID", user.get("DEPART_ID"));
						}
					}
				}
			}
			mr.pm.put("startDate", startDate);
			mr.pm.put("endDate", endDate);
			mr.pm.put("ccityId", ccityId);//出发/目的地城市ID
			mr.pm.put("ctype", ctype);
			mr.pm.put("cityType", type);
			List<Map<String, Object>> data = this.statisticsService.totalCompanyOfOrderService(mr.pm);
			json.setData(data);
		} catch (Exception e) {
			log.error("查询异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询异常");
		}
		return json;
	}
	
	@RequestMapping("/order")
	public ListRange totalCityOfOrder(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String type, String startDate, String endDate, String departIds){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
		ListRange json = new ListRange();
		try {
			if(CommonUtils.checkString(cityId) && !cityId.equals("all")){
				mr.pm.put("CITY_ID", cityId);
			}else if(!user.get("USER_NAME").toString().equals("admin")){
				if(COMPANY_TYPE.equals("0")){
					mr.pm.clear();
					mr.pm.put("USER_ID", user.get("ID"));
					List<Map<String, Object>> sites = this.companyService.listSiteInfoService(mr.pm);
					StringBuffer cityIds = new StringBuffer();
					for (Map<String, Object> site : sites) {
						cityIds.append("'"+site.get("CITY_ID").toString()+"',");
					}
					mr.pm.put("CITY_IDS", cityIds.toString().substring(0, cityIds.length()-1));
				}else if(COMPANY_TYPE.equals("1")){
					String USER_ID = (String)user.get("ID");
					String COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID");
					
					if(CommonUtils.checkString(departIds)){
						JSONArray _departIds = JSONArray.fromObject(departIds);
				    	StringBuffer ids = new StringBuffer();
				    	for (Object departId : _departIds) {
				    		ids.append(departId+",");
						}
				    	mr.pm.put("SUPPLY_DEPART_IDS", ids.toString().substring(0, ids.length() -1 ).split(","));
				    }else if(USER_ID.equals(COMPANY_USER_ID)){
				    	mr.pm.put("SUPPLY_COMPANY_ID", (String)user.get("COMPANY_ID"));
					}else{
						String DEPART_IDS = (String)user.get("DEPART_IDS");
						if(CommonUtils.checkString(DEPART_IDS)){
							DEPART_IDS =DEPART_IDS +","+user.get("DEPART_ID");
							mr.pm.put("SUPPLY_DEPART_IDS", DEPART_IDS.split(","));
						}else{
							mr.pm.put("SUPPLY_DEPART_ID", user.get("DEPART_ID"));
						}
					}
				}
			}
			mr.pm.put("startDate", startDate);
			mr.pm.put("endDate", endDate);
			mr.pm.put("cityType", type);
			List<Map<String, Object>> data = this.statisticsService.totalCityOfOrderService(mr.pm);
			json.setData(data);
		} catch (Exception e) {
			log.error("查询异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询异常");
		}
		return json;
	}
	
	@RequestMapping("/route/supply/create")
	public ListRange companyProduce(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String type, String startDate, String endDate, String rtype, String companyId, String ccityId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			mr.pm.put("startDate", startDate);
			mr.pm.put("endDate", endDate);
			if(CommonUtils.checkString(cityId) && !cityId.equals("all")){
				mr.pm.put("CITY_ID", cityId);
			}else if(!user.get("USER_NAME").toString().equals("admin")){
				mr.pm.put("USER_ID", user.get("ID"));
				List<Map<String, Object>> sites = this.companyService.listSiteInfoService(mr.pm);
				StringBuffer cityIds = new StringBuffer();
				for (Map<String, Object> site : sites) {
					cityIds.append("'"+site.get("CITY_ID").toString()+"',");
				}
				mr.pm.put("CITY_IDS", cityIds.toString().substring(0, cityIds.length()-1));
			}
			mr.pm.put("ccityId", ccityId);
			mr.pm.put("cityType", type);
			mr.pm.put("type", rtype);
			mr.pm.put("companyId", companyId);
			List<Map<String, Object>> data = this.statisticsService.companyProduceService(mr.pm);
			json.setData(data);
		} catch (Exception e) {
			log.error("查询异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询异常");
		}
		return json;
	}
	
	@RequestMapping("/route/supply")
	public ListRange cntCompanyProduce(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String type, String startDate, String endDate, String rtype, String ccityId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("startDate", startDate);
			mr.pm.put("endDate", endDate);
			
			if(CommonUtils.checkString(cityId) && !cityId.equals("all")){
				mr.pm.put("CITY_ID", cityId);
			}else if(!user.get("USER_NAME").toString().equals("admin")){
				mr.pm.put("USER_ID", user.get("ID"));
				List<Map<String, Object>> sites = this.companyService.listSiteInfoService(mr.pm);
				StringBuffer cityIds = new StringBuffer();
				for (Map<String, Object> site : sites) {
					cityIds.append("'"+site.get("CITY_ID").toString()+"',");
				}
				mr.pm.put("CITY_IDS", cityIds.toString().substring(0, cityIds.length()-1));
			}
			mr.pm.put("ccityId", ccityId);//出发/目的地城市ID
			
			mr.pm.put("cityType", type);
			mr.pm.put("type", rtype);
			List<Map<String, Object>> data = this.statisticsService.cntCompanyProduceService(mr.pm);
			json.setData(data);
		} catch (Exception e) {
			log.error("查询异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询异常");
		}
		return json;
	}
	
	@RequestMapping("/route")
	public ListRange route(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String type, String startDate, String endDate){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			if(CommonUtils.checkString(cityId) && !cityId.equals("all")){
				mr.pm.put("CITY_ID", cityId);
			}else if(!user.get("USER_NAME").toString().equals("admin")){
				mr.pm.clear();
				mr.pm.put("USER_ID", user.get("ID"));
				List<Map<String, Object>> sites = this.companyService.listSiteInfoService(mr.pm);
				StringBuffer cityIds = new StringBuffer();
				for (Map<String, Object> site : sites) {
					cityIds.append("'"+site.get("CITY_ID").toString()+"',");
				}
				mr.pm.put("CITY_IDS", cityIds.toString().substring(0, cityIds.length()-1));
			}
			mr.pm.put("startDate", startDate);
			mr.pm.put("endDate", endDate);
			mr.pm.put("cityType", type);
			List<Map<String, Object>> data = this.statisticsService.totalCompanyRouteCityService(mr.pm);
			json.setData(data);
		} catch (Exception e) {
			log.error("查询异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询异常");
		}
		return json;
	}
	
	@RequestMapping("/opened/company/detail")
	public ListRange openedCompanyDetail(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String type, String startDate, String endDate, String ccityId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			if(CommonUtils.checkString(cityId) && !cityId.equals("all")){
				mr.pm.put("CITY_ID", cityId);
			}else if(!user.get("USER_NAME").toString().equals("admin")){
				mr.pm.clear();
				mr.pm.put("USER_ID", user.get("ID"));
				List<Map<String, Object>> sites = this.companyService.listSiteInfoService(mr.pm);
				StringBuffer cityIds = new StringBuffer();
				for (Map<String, Object> site : sites) {
					cityIds.append("'"+site.get("CITY_ID").toString()+"',");
				}
				mr.pm.put("CITY_IDS", cityIds.toString().substring(0, cityIds.length()-1));
			}
			mr.pm.put("ccityId", ccityId);//出发/目的地城市ID
			mr.pm.put("startDate", startDate);
			mr.pm.put("endDate", endDate);
			mr.pm.put("cityType", type);
			List<Map<String, Object>> data = this.statisticsService.companyOfCityService(mr.pm);
			json.setData(data);
		} catch (Exception e) {
			log.error("查询异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询异常");
		}
		return json;
	}
	
	@RequestMapping("/opened/company")
	public ListRange openedCompany(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String startDate, String endDate){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			mr.pm.put("startDate", startDate);
			mr.pm.put("endDate", endDate);
			if(CommonUtils.checkString(cityId) && !cityId.equals("all")){
				mr.pm.put("CITY_ID", cityId);
			}
			if(!user.get("USER_NAME").toString().equals("admin")){
				mr.pm.put("SITE_MANAGER_ID", user.get("ID"));
			}
			List<Map<String, Object>> data = this.statisticsService.totalCompanyOfCityService(mr.pm);
			json.setData(data);
		} catch (Exception e) {
			log.error("查询旅行社/供应商统计异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询旅行社/供应商统计异常");
		}
		return json;
	}
	
	/**
	 * 总计
	 * @param params
	 */
	@RequestMapping("/total")
	public Map<String, Object> total(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String PARAMS_COMPANY_TYPE, String PARAMS_COMPANY_ID,
			String isParent, String routeType, String departIds, String dateType, String startDate, String endDate, String companyId){
		Map<String, Object> total = new HashMap<String, Object>();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			
			mr.pm.clear();
			
			String COMPANY_TYPE = "";
			PARAMS_COMPANY_ID = request.getParameter("PARAMS_COMPANY_ID");
			if(CommonUtils.checkString(PARAMS_COMPANY_TYPE)){
				COMPANY_TYPE = PARAMS_COMPANY_TYPE;
			}else{
				COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
			}
			
			request.setAttribute("PARAMS_COMPANY_TYPE", COMPANY_TYPE);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(user);
			params.put("departIds", departIds);
			params.put("companyId", companyId);
			
			if(CommonUtils.checkString(PARAMS_COMPANY_ID)){
				params.put("COMPANY_ID", PARAMS_COMPANY_ID);
			}
			
			StatisticsPowerSaleController.powerCompany(mr, COMPANY_TYPE, cityId, params, isParent, (String)user.get("USER_NAME"));	
			
			if(CommonUtils.checkString(routeType) && !routeType.equals("null")){
				mr.pm.put("ROUTE_TYPE", routeType);
			}
			
			if(CommonUtils.checkString(dateType)){
				if(dateType.equals("2")){
					mr.pm.put("CT_START_TIME", startDate);
					mr.pm.put("CT_END_TIME", endDate);
				}else{
					mr.pm.put("startDate", startDate);
					mr.pm.put("endDate", endDate);
				}
			}
			
			mr.pm.put("STATUSES", "2,5");
			List<Map<String, Object>> data = this.statisticsService.totalInfoService(mr.pm);
			if(CommonUtils.checkList(data)){
				total = data.get(0);
			}
		} catch (Exception e) {
			log.error("查询总计异常",e);
		}
		return total;
	}
	
	/**
	 * 今日总计
	 * @param params
	 */
	@RequestMapping("/today")
	public Map<String, Object> today(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String PARAMS_COMPANY_TYPE, String PARAMS_COMPANY_ID,
			String isParent, String routeType, String departIds, String companyId){
		Map<String, Object> total = new HashMap<String, Object>();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");

			mr.pm.clear();
			
			String COMPANY_TYPE = "";
			
			if(CommonUtils.checkString(PARAMS_COMPANY_TYPE)){
				COMPANY_TYPE = PARAMS_COMPANY_TYPE;
			}else{
				COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
			}
			
			request.setAttribute("PARAMS_COMPANY_TYPE", COMPANY_TYPE);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(user);
			params.put("departIds", departIds);
			params.put("companyId", companyId);
			if(CommonUtils.checkString(PARAMS_COMPANY_ID)){
				params.put("COMPANY_ID", PARAMS_COMPANY_ID);
			}
			
			StatisticsPowerSaleController.powerCompany(mr, COMPANY_TYPE, cityId, params, isParent, (String)user.get("USER_NAME"));	
			if(CommonUtils.checkString(routeType) && !routeType.equals("null")){
				mr.pm.put("ROUTE_TYPE", routeType);
			}
			mr.pm.put("NOW_DATE", DateUtil.getNowDate());
			
			mr.pm.put("STATUSES", "2,5");
			
			List<Map<String, Object>> data = this.statisticsService.todayTotalInfoService(mr.pm);
			if(CommonUtils.checkList(data)){
				total = data.get(0);
			}
			
			List<Map<String, Object>> start_data = this.statisticsService.startService(mr.pm);
			if(CommonUtils.checkList(data)){
				Map<String, Object> d = start_data.get(0);
				total.put("TODAY_OUT_CNT", d.get("TODAY_OUT_CNT"));
				total.put("TOMORROW_OUT_CNT", d.get("TOMORROW_OUT_CNT"));
			}else{
				total.put("TODAY_OUT_CNT", 0);
				total.put("TOMORROW_OUT_CNT", 0);
			}
		} catch (Exception e) {
			log.error("查询今日出团总计异常",e);
		}
		return total;
	}
	
	/**
	 * 旅行社公司数据统计
	 */
	@RequestMapping("/company")
	public ListRange company(HttpServletRequest request,HttpServletResponse response, MapRange mr, String TYPE, String WEIDU, String START, String END, String GROUP_TYPE, String VIEW,
			String PARAMS_COMPANY_TYPE, String PARAMS_COMPANY_ID, String isParent){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");

			mr.pm.clear();
			
			String COMPANY_TYPE = "";
			
			if(CommonUtils.checkString(PARAMS_COMPANY_TYPE)){
				COMPANY_TYPE = PARAMS_COMPANY_TYPE;
			}else{
				COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
			}
			
			request.setAttribute("PARAMS_COMPANY_TYPE", COMPANY_TYPE);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(user);
			if(CommonUtils.checkString(PARAMS_COMPANY_ID)){
				params.put("COMPANY_ID", PARAMS_COMPANY_ID);
			}
			
			StatisticsPowerSaleController.powerLevel(mr, COMPANY_TYPE, null, params, isParent, (String)user.get("USER_NAME"));
			
			TYPE = CommonUtils.checkString(TYPE) ? TYPE : "1";
			WEIDU = CommonUtils.checkString(WEIDU) ? WEIDU : "3";
			if(TYPE.equals("1")){
				if(WEIDU.equals("1")){
					mr.pm.put("WEIDU", "CREATE_YEAR");
				}else if(WEIDU.equals("2")){
					mr.pm.put("WEIDU", "CREATE_QUARTER");
				}else if(WEIDU.equals("3")){
					mr.pm.put("WEIDU", "CREATE_MONTH");
				}else if(WEIDU.equals("4")){
					mr.pm.put("WEIDU", "CREATE_DAY");
					mr.pm.put("START_DAY", START);
					mr.pm.put("END_DAY", END);
				}
			}
			/**
			 * 图形数据,不需要分组
			 */
			if(!"1".equals(VIEW)){
				mr.pm.put("GROUP_TYPE", COMPANY_TYPE.equals("1")?"1":"2");
			} 
			
			List<Map<String, Object>> data = this.statisticsService.companyService(mr.pm);
			
			/**
			 * 类型为1时是图表数据
			 */
			if("1".equals(VIEW)){
				Map<String, Object> _data = new HashMap<String, Object>();
				if(CommonUtils.checkList(data)){
					
					List<String> CNT = new ArrayList<String>();
					List<String> SUCCESS_CNT = new ArrayList<String>();
					List<String> SALE_AMOUNT = new ArrayList<String>();
					List<String> INTER_AMOUNT = new ArrayList<String>();
					List<String> TRUE_BUY_AMOUNT = new ArrayList<String>();
					List<String> TRUE_SALE_AMOUNT = new ArrayList<String>();
					List<String> REFUND_AMOUNT = new ArrayList<String>();
					List<String> MARKETING_AMOUNT = new ArrayList<String>();
					List<String> OTHER_CNT = new ArrayList<String>();
					
					List<String> _WEIDU = new ArrayList<String>();
					
					for (Map<String, Object> d : data) {
						CNT.add(String.valueOf(d.get("CNT")));
						SUCCESS_CNT.add(String.valueOf(d.get("SUCCESS_CNT")));
						SALE_AMOUNT.add(String.valueOf(d.get("SALE_AMOUNT")));
						INTER_AMOUNT.add(String.valueOf(d.get("INTER_AMOUNT")));
						TRUE_BUY_AMOUNT.add(String.valueOf(d.get("TRUE_BUY_AMOUNT")));
						TRUE_SALE_AMOUNT.add(String.valueOf(d.get("TRUE_SALE_AMOUNT")));
						OTHER_CNT.add(String.valueOf(d.get("OTHER_CNT")));
						
						REFUND_AMOUNT.add(String.valueOf(d.get("REFUND_AMOUNT")));
						MARKETING_AMOUNT.add(String.valueOf(d.get("MARKETING_AMOUNT")));
						
						_WEIDU.add(String.valueOf(d.get("WEIDU")));
					}
					
					_data.put("CNT", CNT);
					_data.put("SUCCESS_CNT", SUCCESS_CNT);
					_data.put("SALE_AMOUNT", SALE_AMOUNT);
					_data.put("INTER_AMOUNT", INTER_AMOUNT);
					_data.put("TRUE_BUY_AMOUNT", TRUE_BUY_AMOUNT);
					_data.put("TRUE_SALE_AMOUNT", TRUE_SALE_AMOUNT);
					_data.put("OTHER_CNT", OTHER_CNT);
					_data.put("REFUND_AMOUNT", REFUND_AMOUNT);
					_data.put("MARKETING_AMOUNT", MARKETING_AMOUNT);
					_data.put("WEIDU", _WEIDU);
					
				}
				
				data.clear();
				data.add(0, _data);
				json.setData(data);
				
			}else{
				json.setData(data);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询旅行社/供应商统计异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询旅行社/供应商统计异常");
		}
		return json;
	}
	
	/**
	 * 旅行社总公司/站数据统计
	 * 列表
	 */
	@RequestMapping("/parent/company")
	public ListRange parentCompany(HttpServletRequest request,HttpServletResponse response, MapRange mr, String PARAMS_COMPANY_ID, String cityId, String PARAMS_COMPANY_TYPE, String VIEW, String TYPE, String WEIDU, String START, String END,
			String queryDate){
		ListRange json = new ListRange();
		try {
			
			queryDate = CommonUtils.checkString(queryDate) ? queryDate : "";
			 
			/**
			 * 销售额 结算额 利润 订单数
			 */
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.clear();
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

			TYPE = CommonUtils.checkString(TYPE) ? TYPE : "1";
			WEIDU = CommonUtils.checkString(WEIDU) ? WEIDU : "3";
			if(TYPE.equals("1")){
				if(WEIDU.equals("1")){
					mr.pm.put("WEIDU", "CREATE_YEAR");
					mr.pm.put("START_YEAR", queryDate);
					mr.pm.put("END_YEAR", queryDate);
				}else if(WEIDU.equals("2")){
					mr.pm.put("WEIDU", "CREATE_QUARTER");
					
					queryDate = queryDate.replaceAll("年第一季度", "1").replaceAll("年第二季度", "2").replaceAll("年第三季度", "3").replaceAll("年第四季度", "4");
					
					mr.pm.put("START_QUARTER", queryDate);
					mr.pm.put("END_QUARTER", queryDate);
					
				}else if(WEIDU.equals("3")){
					mr.pm.put("WEIDU", "CREATE_MONTH");
					mr.pm.put("START_MONTH", queryDate);
					mr.pm.put("END_MONTH", queryDate);
				}else if(WEIDU.equals("4")){
					mr.pm.put("WEIDU", "CREATE_DAY");
					
					mr.pm.put("START_DAY", START);
					mr.pm.put("END_DAY", END);
					if(CommonUtils.checkString(queryDate)){
						mr.pm.put("START_DAY", queryDate);
						mr.pm.put("END_DAY", queryDate);
					}
					
				}
			}
			
			/**
			 * 站长权限
			 */
			if(CommonUtils.checkString(cityId)){
//				cityId = "0C9D617D9F64DFE3E050007F0100A6CD";//----------------------------------------------------------------------------------------------------------------------------
				mr.pm.put("CITY_ID", cityId);
				String USER_TYPE = (String)user.get("USER_TYPE");
				if("0101".equals(USER_TYPE)){
					mr.pm.put("SITE_USER_ID", user.get("COMPANY_USER_ID"));
				}else{
					mr.pm.put("SITE_USER_ID", (String)user.get("ID"));
				}
				mr.pm.put("TYPE", PARAMS_COMPANY_TYPE);
				/**
				 * 如queryDate不为空时,查询的结果需要按照公司名称分组
				 * VIEW = 2 与 VIEW = 1 使用不同的sql
				 */
				if("1".equals(VIEW) && !CommonUtils.checkString(queryDate)){
					data = this.statisticsService.siteCompanyGroupByWeiDuService(mr.pm);
				}else{
					data = this.statisticsService.siteCompanyService(mr.pm);
				}
				
			}else{
				
				if(CommonUtils.checkString(PARAMS_COMPANY_ID)){
					mr.pm.put("PID", PARAMS_COMPANY_ID);
				}else{
					mr.pm.put("PID", (String)user.get("COMPANY_ID"));
				}
				
				
				if("1".equals(PARAMS_COMPANY_TYPE)){
					if("1".equals(VIEW)){
						data = this.statisticsService.supplyCompanyGroupByWeiDuService(mr.pm);
					}else{
						data = this.statisticsService.supplyCompanyService(mr.pm);
					}
				}else{
					if("1".equals(VIEW)){
						data = this.statisticsService.parentCompanyGroupByWeiDuService(mr.pm);	
					}else{
						data = this.statisticsService.parentCompanyService(mr.pm);
					}
					
				}
				
			}
			

			if("1".equals(VIEW) && !CommonUtils.checkString(queryDate)){
				Map<String, Object> _data = new HashMap<String, Object>();
				if(CommonUtils.checkList(data)){
					
					List<String> CNT = new ArrayList<String>();
					List<String> SUCCESS_CNT = new ArrayList<String>();
					List<String> SALE_AMOUNT = new ArrayList<String>();
					List<String> INTER_AMOUNT = new ArrayList<String>();
					List<String> TRUE_BUY_AMOUNT = new ArrayList<String>();
					List<String> TRUE_SALE_AMOUNT = new ArrayList<String>();
					List<String> REFUND_AMOUNT = new ArrayList<String>();
					List<String> MARKETING_AMOUNT = new ArrayList<String>();
					List<String> OTHER_CNT = new ArrayList<String>();
					
					List<String> _WEIDU = new ArrayList<String>();
					
					for (Map<String, Object> d : data) {
						CNT.add(String.valueOf(d.get("CNT")));
						SUCCESS_CNT.add(String.valueOf(d.get("SUCCESS_CNT")));
						SALE_AMOUNT.add(String.valueOf(d.get("SALE_AMOUNT")));
						INTER_AMOUNT.add(String.valueOf(d.get("INTER_AMOUNT")));
						TRUE_BUY_AMOUNT.add(String.valueOf(d.get("TRUE_BUY_AMOUNT")));
						TRUE_SALE_AMOUNT.add(String.valueOf(d.get("TRUE_SALE_AMOUNT")));
						OTHER_CNT.add(String.valueOf(d.get("OTHER_CNT")));
						
						REFUND_AMOUNT.add(String.valueOf(d.get("REFUND_AMOUNT")));
						MARKETING_AMOUNT.add(String.valueOf(d.get("MARKETING_AMOUNT")));
						
						_WEIDU.add(String.valueOf(d.get("WEIDU")));
					}
					
					_data.put("CNT", CNT);
					_data.put("SUCCESS_CNT", SUCCESS_CNT);
					_data.put("SALE_AMOUNT", SALE_AMOUNT);
					_data.put("INTER_AMOUNT", INTER_AMOUNT);
					_data.put("TRUE_BUY_AMOUNT", TRUE_BUY_AMOUNT);
					_data.put("TRUE_SALE_AMOUNT", TRUE_SALE_AMOUNT);
					_data.put("OTHER_CNT", OTHER_CNT);
					_data.put("REFUND_AMOUNT", REFUND_AMOUNT);
					_data.put("MARKETING_AMOUNT", MARKETING_AMOUNT);
					_data.put("WEIDU", _WEIDU);
					
				}
				
				data.clear();
				data.add(0, _data);
				json.setData(data);
				
			}else if(CommonUtils.checkString(queryDate)){
				
				
				Map<String, Object> _data = new HashMap<String, Object>();
				if(CommonUtils.checkList(data)){
					
					List<Map<String, Object>> CNT = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> SALE_AMOUNT = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> INTER_AMOUNT = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> TRUE_BUY_AMOUNT = new ArrayList<Map<String, Object>>();
					
					List<String> LEGEND = new ArrayList<String>();
					
					for (Map<String, Object> d : data) {
						int i = Integer.parseInt(String.valueOf(d.get("CNT")));
						
						if(i>0){
							
							Map<String, Object> _CNT = new HashMap<String, Object>();
							_CNT.put("value", d.get("CNT"));
							_CNT.put("name", d.get("COMPANY"));
							CNT.add(_CNT);
							
							Map<String, Object> _SALE_AMOUNT = new HashMap<String, Object>();
							_SALE_AMOUNT.put("value", d.get("SALE_AMOUNT"));
							_SALE_AMOUNT.put("name", d.get("COMPANY"));
							SALE_AMOUNT.add(_SALE_AMOUNT);
							
							Map<String, Object> _INTER_AMOUNT = new HashMap<String, Object>();
							_INTER_AMOUNT.put("value", d.get("INTER_AMOUNT"));
							_INTER_AMOUNT.put("name", d.get("COMPANY"));
							INTER_AMOUNT.add(_INTER_AMOUNT);
							
							Map<String, Object> _TRUE_BUY_AMOUNT = new HashMap<String, Object>();
							_TRUE_BUY_AMOUNT.put("value", d.get("TRUE_BUY_AMOUNT"));
							_TRUE_BUY_AMOUNT.put("name", d.get("COMPANY"));
							TRUE_BUY_AMOUNT.add(_TRUE_BUY_AMOUNT);
							
							LEGEND.add((String)d.get("COMPANY"));
							
						}
						
					}
					
					_data.put("CNT", CNT);
					_data.put("SALE_AMOUNT", SALE_AMOUNT);
					_data.put("INTER_AMOUNT", INTER_AMOUNT);
					_data.put("TRUE_BUY_AMOUNT", TRUE_BUY_AMOUNT);
					_data.put("LEGEND", LEGEND);
					
				}
				
				data.clear();
				data.add(0, _data);
				json.setData(data);
				
			}else{
				json.setData(data);
			}
			
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询旅行社总/供应商公司统计异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询旅行社总/供应商公司统计异常");
		}
		return json;
	}
	
}
