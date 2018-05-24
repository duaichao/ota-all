package cn.sd.controller.b2b;

import cn.sd.controller.stat.StatisticsPowerSaleController;
import cn.sd.core.config.ConfigLoader;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.encode.MD5;
import cn.sd.core.util.excel.ExcelUtil;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.power.PowerFactory;
import cn.sd.rmi.ServiceProxyFactory;
import cn.sd.service.account.IAccountService;
import cn.sd.service.b2b.IUserService;
import cn.sd.service.finance.IFinanceService;
import cn.sd.service.order.IOrderService;
import cn.sd.service.order.ITrafficOrderFacadeService;
import cn.sd.service.order.IVisitorOrderService;
import cn.sd.service.resource.IRouteService;
import cn.sd.service.site.ICompanyService;
import cn.sd.service.site.IDepartService;
import cn.sd.service.statistics.IStatisticsService;
import cn.sd.web.Constants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
@RestController
@RequestMapping("/b2b/user")
public class UserController extends ExtSupportAction {
	private static Log log = LogFactory.getLog(UserController.class);
	
	@Resource
	private IDepartService departService;
	@Resource
	private IUserService userService;
	@Resource
	private IOrderService orderService;
	@Resource
	private ICompanyService companyService;
	@Resource
	private IRouteService routeService;
	@Resource
	private IFinanceService financeService;
	@Resource
	private IStatisticsService statisticsService;
	@Resource
	private IVisitorOrderService visitorOrderService;
	@Resource
	private cn.sd.service.produce.IRouteService produceRouteService;
	@Resource
	private IAccountService accountService;
	
	@Resource
	private ITrafficOrderFacadeService trafficOrderFacadeService;
	
	/*@Bean
    public SystemWebSocketHandler systemWebSocketHandler() {
        return new SystemWebSocketHandler();
    }
	@RequestMapping("/test/send")
	public void testMsg(HttpServletRequest request,HttpServletResponse response){
        systemWebSocketHandler().sendMessageToUser("xianadmin", new TextMessage("200"));
	}*/
	@RequestMapping("/index")
	public ModelAndView main(HttpServletRequest request,HttpServletResponse response){
		
		return new ModelAndView("/index");
	}
	
	@RequestMapping("/ye")
	public ListRange ye(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String startDate, String endDate){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		String DEPART_ID = (String)user.get("DEPART_ID");
		String ye = this.trafficOrderFacadeService.yeStatService(DEPART_ID);
		json.setMessage(ye);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/wait/do")
	public Map<String, Object> waitDo(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String departIds, String companyId, 
			String dateType, String startDate, String endDate){
		if(CommonUtils.checkString(cityId) && cityId.equals("all")){
			cityId = "";
		}
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String COMPANY_TYPE = String.valueOf(user.get("COMPANY_TYPE"));
		Map<String, Object> result = new HashMap<String, Object>();
		String USER_ID = (String)user.get("ID"),
		COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID"),
		COMPANY_ID = (String)user.get("COMPANY_ID");
		mr.pm.clear();
		mr.pm.put("USER_ID", USER_ID);
		List<Map<String, Object>> sites = this.companyService.listSiteInfoService(mr.pm);
		
		if(COMPANY_TYPE.equals("0")){
			mr.pm.clear();
			mr.pm.put("MANAGER_SITE", sites);
			PowerFactory.getPower(request, response, null, "site-company", mr.pm);
			mr.pm.put("COMPANY_TYPE", "2,3,4,5,6,7");
			mr.pm.put("AUDIT_STATUS", 0);
			mr.pm.remove("COMPANY_ID");
			mr.pm.remove("ROLE_ID");
			if(CommonUtils.checkString(cityId)){
				mr.pm.remove("CITY_IDS");
				mr.pm.remove("CITY_ID");
				mr.pm.put("CITY_ID", cityId);
			}
			
			int size = this.companyService.countService(mr.pm);
			result.put("size", size);
			
			mr.pm.put("COMPANY_TYPE", "1");
			int supplySize = this.companyService.countService(mr.pm);
			result.put("supplySize", supplySize);
			
			StringBuffer site_ids = new StringBuffer();
			if(CommonUtils.checkList(sites)){
				for (Map<String, Object> site : sites) {
					site_ids.append("'"+site.get("ID")+"',");
				}
			}
			
			mr.pm.clear();
			if(CommonUtils.checkString(cityId)){
				for (Map<String, Object> site : sites) {
					String CITY_ID = (String)site.get("CITY_ID");
					if(cityId.equals(CITY_ID)){
						mr.pm.put("SITE_RELA_ID", "'"+site.get("ID")+"'");	
					}
				}
			}else{
				mr.pm.put("SITE_RELA_ID", site_ids.substring(0, site_ids.length()-1));
			}
			
			mr.pm.put("STATUSES", "'4'");
			mr.pm.put("PRODUCE_TYPES", "'2', '3'");
			int routeOrderTotalSize = this.orderService.countRouteOrderService(mr.pm);
			result.put("routeOrderTotalSize", routeOrderTotalSize);
			
			mr.pm.put("PRODUCE_TYPE", 1);
			int trafficOrderTotalSize = this.orderService.countService(mr.pm);
			result.put("trafficOrderTotalSize", trafficOrderTotalSize);
			
			
			
			mr.pm.clear();
			mr.pm.put("TYPE", 1);
			mr.pm.put("CITY_IDS", site_ids.substring(0, site_ids.length()-1));
			double money = this.accountService.sumAccountDetailMoneyService(mr.pm);
			result.put("money", money);
		}else if(COMPANY_TYPE.equals("1")){
			
			mr.pm.put("sites", sites);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(user);
			
			StatisticsPowerSaleController.powerLevel(mr, COMPANY_TYPE, null, params, null, (String)user.get("USER_NAME"));
//			PowerFactory.getPower(request, response, "order_list", "order_list", mr.pm);
			
			mr.pm.put("STATUSES", "'3', '4'");
			mr.pm.put("PRODUCE_TYPES", "'2', '3'");
			mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
			if(CommonUtils.checkString(departIds)){
				JSONArray _departIds = JSONArray.fromObject(departIds);
		    	StringBuffer ids = new StringBuffer();
		    	for (Object departId : _departIds) {
		    		ids.append(departId+",");
				}
		    	mr.pm.put("SALE_DEPART_IDS", ids.toString().substring(0, ids.length() -1 ).split(","));	
			}
			int routeOrderTotalSize = this.orderService.countRouteOrderService(mr.pm);
			result.put("routeOrderTotalSize", routeOrderTotalSize);
			
			mr.pm.clear();
			mr.pm.put("existDate", "YES");
			mr.pm.put("USER_ID", (String)user.get("ID"));
			
			StringBuffer site_ids = new StringBuffer();
			if(CommonUtils.checkList(sites)){
				for (Map<String, Object> site : sites) {
					site_ids.append("'"+site.get("ID")+"',");
				}
			}
			
			mr.pm.put("MANAGER_SITE", sites);
			PowerFactory.getPower(request, response, "", "route-list", mr.pm);
			mr.pm.remove("CITY_ID");
			if(CommonUtils.checkString(departIds)){
				JSONArray _departIds = JSONArray.fromObject(departIds);
		    	StringBuffer ids = new StringBuffer();
		    	for (Object departId : _departIds) {
		    		ids.append(departId+",");
				}
		    	mr.pm.put("SALE_DEPART_IDS", ids.toString().substring(0, ids.length() -1 ).split(","));	
			}
			int routeTotalSize = this.routeService.countService(mr.pm);
			result.put("routeTotalSize", routeTotalSize);
		}else{
			
			if(CommonUtils.checkString(companyId) || CommonUtils.checkString(departIds)){
				mr.pm.put("CREATE_COMPANY_ID", companyId);
				if(CommonUtils.checkString(departIds)){
					JSONArray _departIds = JSONArray.fromObject(departIds);
			    	StringBuffer ids = new StringBuffer();
			    	for (Object departId : _departIds) {
			    		ids.append(departId+",");
					}
			    	mr.pm.put("CREATE_DEPART_IDS", ids.toString().substring(0, ids.length() -1 ).split(","));
				}
			}else{
				/**
				 * 总公司公司管理员
				 * 子公司/门市管理员
				 */
				String DEPART_IDS = (String)user.get("DEPART_IDS");
				String PID = (String)user.get("PID");
				
				if(USER_ID.equals(COMPANY_USER_ID) && "-1".equals(PID)){
					mr.pm.put("PID", COMPANY_ID);
				}else if(USER_ID.equals(COMPANY_USER_ID)){
					mr.pm.put("CREATE_COMPANY_ID", COMPANY_ID);
				}else{

					//部门经理看部门员工的所有订单
					String IS_MANAGER = (String)user.get("IS_MANAGER");
					if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
						if(CommonUtils.checkString(DEPART_IDS)){
							DEPART_IDS =DEPART_IDS +","+user.get("DEPART_ID");
							mr.pm.put("CREATE_DEPART_IDS", DEPART_IDS.split(","));
						}else{
							mr.pm.put("CREATE_DEPART_ID", user.get("DEPART_ID"));
						}
					}else{
						if(CommonUtils.checkString(DEPART_IDS)){
							mr.pm.put("CREATE_DEPART_IDS", DEPART_IDS.split(","));
							mr.pm.put("buyUserId", USER_ID);
						}else{
							mr.pm.put("CREATE_USER_ID", USER_ID);
						}
					}
					
				}
				
			}
			
			mr.pm.put("PRODUCE_TYPES", "2,3");
			mr.pm.put("STATUSES", "0,3,4");
			
			if(CommonUtils.checkString(dateType)){
				if(dateType.equals("2")){
					mr.pm.put("CT_START_TIME", startDate);
					mr.pm.put("CT_END_TIME", endDate);
				}else{
					mr.pm.put("startDate", startDate);
					mr.pm.put("endDate", endDate);
				}
			}
			
			List<Map<String, Object>> routeOrderCnt = this.statisticsService.totalInfoService(mr.pm);
			result.put("routeOrderCnt", routeOrderCnt.get(0));
			/*
			mr.pm.put("PRODUCE_TYPES", "1");
			mr.pm.put("STATUSES", "1,3,4");
			List<Map<String, Object>> trafficOrderCnt = this.statisticsService.totalInfoService(mr.pm);
			result.put("trafficOrderCnt", trafficOrderCnt.get(0));
			*/
		}
		return result;
	}
	
	@RequestMapping("/home")
	public ModelAndView home(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		Map<String, Object> result = new HashMap<String, Object>();
		request.setAttribute("departs", "[]");
		/**
		 * 待退款 交通 线路
		 */
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String COMPANY_TYPE = String.valueOf(user.get("COMPANY_TYPE"));
		List<Map<String, Object>> citys = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> companys = new ArrayList<Map<String, Object>>();
		String city = "";
		if(user.get("USER_NAME").toString().equals("admin")){
			citys = this.companyService.listSiteManagerService(mr.pm);
			String[] jsonConfig = {"DEL_TIME"};
			city = this.getJsonArray(citys, jsonConfig);
		}else if(COMPANY_TYPE.equals("0")){
			
				/**
				 * 站长管理的城市
				 */
				mr.pm.put("USER_ID", user.get("ID"));
				
				if("0101".equals((String)user.get("USER_TYPE"))){
					/**
					 * 如果是管理员子账户，那应该继承管理员管理的城市
					 */
					mr.pm.clear();
					mr.pm.put("ID", user.get("COMPANY_ID"));
					mr.pm.put("start", 1);
					mr.pm.put("end", 1);
					List<Map<String, Object>> company = this.companyService.listCompanyOfUserService(mr.pm);
					if(CommonUtils.checkList(company)){
						mr.pm.clear();
						mr.pm.put("USER_ID", company.get(0).get("USER_ID"));
					}
					
				}
				
			citys = this.companyService.listSiteManagerService(mr.pm);
			String[] jsonConfig = {"DEL_TIME"};
			city = this.getJsonArray(citys, jsonConfig);
			
			mr.pm.clear();
			mr.pm.put("USER_ID", user.get("ID"));
			List<Map<String, Object>> sites = this.companyService.listSiteInfoService(mr.pm);
			
			/*
			mr.pm.clear();
			mr.pm.put("MANAGER_SITE", sites);
			PowerFactory.getPower(request, response, null, "site-company", mr.pm);
			mr.pm.put("COMPANY_TYPE", "2,3,4,5,6,7");
			mr.pm.put("AUDIT_STATUS", 0);
			mr.pm.remove("COMPANY_ID");
			mr.pm.remove("ROLE_ID");
			int size = this.companyService.countService(mr.pm);
			result.put("size", size);
			
			
			mr.pm.put("COMPANY_TYPE", "1");
			int supplySize = this.companyService.countService(mr.pm);
			result.put("supplySize", supplySize);
			
			mr.pm.clear();
			StringBuffer site_ids = new StringBuffer();
			if(CommonUtils.checkList(sites)){
				for (Map<String, Object> site : sites) {
					site_ids.append("'"+site.get("ID")+"',");
				}
				
			}
			mr.pm.put("SITE_RELA_ID", site_ids.substring(0, site_ids.length()-1));
			mr.pm.put("STATUSES", "'4'");
			mr.pm.put("PRODUCE_TYPES", "'2', '3'");
			int routeOrderTotalSize = this.orderService.countRouteOrderService(mr.pm);
			result.put("routeOrderTotalSize", routeOrderTotalSize);
			
			mr.pm.put("PRODUCE_TYPE", 1);
			int trafficOrderTotalSize = this.orderService.countService(mr.pm);
			result.put("trafficOrderTotalSize", trafficOrderTotalSize);
			
			mr.pm.clear();
			mr.pm.put("TYPE", 1);
			mr.pm.put("CITY_IDS", site_ids.substring(0, site_ids.length()-1));
			double money = this.accountService.sumAccountDetailMoneyService(mr.pm);
			result.put("money", money);
			*/
		}else if(COMPANY_TYPE.equals("1")){
			
			String USER_ID = (String)user.get("ID");
			
			/*
			 * 

			String COMPANY_ID = (String)user.get("COMPANY_ID");
			
			mr.pm.put("USER_ID", USER_ID);
			
			List<Map<String, Object>> sites = this.companyService.listSiteInfoService(mr.pm);
			mr.pm.put("sites", sites);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(user);
			
			StatisticsPowerSaleController.powerLevel(mr, COMPANY_TYPE, null, params, null, (String)user.get("USER_NAME"));
//			PowerFactory.getPower(request, response, "order_list", "order_list", mr.pm);
			
			
			mr.pm.put("STATUSES", "'3', '4'");
			mr.pm.put("PRODUCE_TYPES", "'2', '3'");
			mr.pm.put("COMPANY_ID", COMPANY_ID);
			int routeOrderTotalSize = this.orderService.countRouteOrderService(mr.pm);
			result.put("routeOrderTotalSize", routeOrderTotalSize);

			mr.pm.put("PRODUCE_TYPE", 1);
			int trafficOrderTotalSize = this.orderService.countService(mr.pm);
			result.put("trafficOrderTotalSize", trafficOrderTotalSize);
			
			mr.pm.clear();
			mr.pm.put("existDate", "YES");
			mr.pm.put("USER_ID", (String)user.get("ID"));
			
			StringBuffer site_ids = new StringBuffer();
			if(CommonUtils.checkList(sites)){
				for (Map<String, Object> site : sites) {
					site_ids.append("'"+site.get("ID")+"',");
				}
			}
			
			
			mr.pm.put("MANAGER_SITE", sites);
			PowerFactory.getPower(request, response, "", "route-list", mr.pm);
			mr.pm.remove("CITY_ID");
			int routeTotalSize = this.routeService.countService(mr.pm);
			result.put("routeTotalSize", routeTotalSize);
			
			
			mr.pm.clear();
			mr.pm.put("COMPANY_ID", COMPANY_ID);
			mr.pm.put("ACCOUNT_STATUS", "2");
			List<Map<String, Object>> finishCalcs = this.financeService.listAccountInfoService(mr.pm);
			result.put("finishCalcTotal", this.getActualAmount(finishCalcs));
			
			mr.pm.clear();
			mr.pm.put("COMPANY_ID", COMPANY_ID);
			mr.pm.put("ACCOUNT_STATUS", "0");
			List<Map<String, Object>> unFinishCalcs = this.financeService.listAccountInfoService(mr.pm);
			result.put("unFinishCalcTotal", this.getActualAmount(unFinishCalcs));
			
			mr.pm.clear();
			mr.pm.put("COMPANY_ID", COMPANY_ID);
			mr.pm.put("ACCOUNT_STATUS", "1");
			List<Map<String, Object>> waitFinishCalcTotal = this.financeService.listAccountInfoService(mr.pm);
			result.put("waitFinishCalcTotal", this.getActualAmount(waitFinishCalcTotal));
			
			*/
			/**
			 * 部门
			 */
			List<Map<String, Object>> departs = new ArrayList<Map<String,Object>>();
			String COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID");
			if(USER_ID.equals(COMPANY_USER_ID)){
				mr.pm.clear();
				mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
				mr.pm.put("start", 1);
				mr.pm.put("end", 10000);
				departs = this.departService.listService(mr.pm);
				for (Map<String, Object> depart : departs) {
					depart.put("boxLabel", depart.get("TEXT"));
					depart.put("name", "depart");
					depart.put("inputValue", depart.get("ID"));
					depart.put("checked", "true");
				}
			}else{
				mr.pm.clear();
				mr.pm.put("USER_ID", USER_ID);
				//部门管理员
				String IS_MANAGER = (String)user.get("IS_MANAGER");
				String MANAGER_DEPART_ID = "";
				boolean b = false;
				if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
					MANAGER_DEPART_ID = (String)user.get("DEPART_ID");
					b = true;
				}
				
				departs = this.companyService.searchManagerDepartService(mr.pm);
				for (Map<String, Object> depart : departs) {
					depart.put("boxLabel", depart.get("TEXT"));
					depart.put("name", "depart");
					depart.put("inputValue", depart.get("DEPART_ID"));
					depart.put("checked", "true");
					if(MANAGER_DEPART_ID.equals(depart.get("DEPART_ID").toString())){
						b = false;
					}
				}
				if(b){
					Map<String, Object> managerDepart = new HashMap<String, Object>();
					managerDepart.put("boxLabel", user.get("DEPART_NAME"));
					managerDepart.put("name", "depart");
					managerDepart.put("inputValue", user.get("DEPART_ID"));
					managerDepart.put("checked", "true");
					departs.add(managerDepart);
				}
			}
			request.setAttribute("departs", CommonUtils.checkList(departs)?this.getJsonArray(departs):"[]");
		}else {
			/**
			 * 
			  总计--(公司权限)
			 今日--(上下级权限)
			 营收报表--(分公司：上下级权限)-(总公司)

			 代办提醒
				待付款--数字--(上下级权限)
				待退款--数字，退款中--数字--(上下级权限)
		    */
			String USER_ID = (String)user.get("ID");
			String COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID");
			String COMPANY_ID = (String)user.get("COMPANY_ID");
			/*
			mr.pm.clear();
			if(USER_ID.equals(COMPANY_USER_ID)){
				mr.pm.put("CREATE_COMPANY_ID", COMPANY_ID);
			}else{
				//部门经理看部门员工的所有订单
				String IS_MANAGER = (String)user.get("IS_MANAGER");
				if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
					mr.pm.put("CREATE_COMPANY_ID", user.get("DEPART_ID"));
				}else{
					mr.pm.put("CREATE_COMPANY_ID", USER_ID);
				}
			}
			mr.pm.put("PRODUCE_TYPES", "2,3");
			mr.pm.put("STATUSES", "1,3,4");
			List<Map<String, Object>> routeOrderCnt = this.statisticsService.totalInfoService(mr.pm);
			result.put("routeOrderCnt", routeOrderCnt.get(0));
			mr.pm.put("PRODUCE_TYPES", "1");
			mr.pm.put("STATUSES", "1,3,4");
			List<Map<String, Object>> trafficOrderCnt = this.statisticsService.totalInfoService(mr.pm);
			result.put("trafficOrderCnt", trafficOrderCnt.get(0));
			*/
			
			String PID = (String)user.get("PID");
			mr.pm.clear();
			if("-1".equals(PID)){
				mr.pm.put("PARENT_ID", COMPANY_ID);
			}else{
				mr.pm.put("ID", COMPANY_ID);
			}
			mr.pm.put("start", 1);
			mr.pm.put("end", 50);
			companys = this.companyService.listCompanyService(mr.pm);
			for (Map<String, Object> company : companys) {
				Map<String, Object> menu = new HashMap<String, Object>();
				
				company.put("text", company.get("COMPANY"));
			
				String _COMPANY_USER_ID = (String)company.get("ID");
				
				List<Map<String, Object>> departs = new ArrayList<Map<String,Object>>();
				/**
				 * 总公司管理员-分公司管理员
				 * 
				 */
				if(USER_ID.equals(COMPANY_USER_ID) || USER_ID.equals(_COMPANY_USER_ID)){
					mr.pm.clear();
					mr.pm.put("COMPANY_ID", company.get("ID"));
					mr.pm.put("start", 1);
					mr.pm.put("end", 10000);
					departs = this.departService.listService(mr.pm);
					for (Map<String, Object> depart : departs) {
						depart.put("boxLabel", depart.get("TEXT"));
						depart.put("name", "depart");
						depart.put("inputValue", depart.get("ID"));
					}
				}else{
					
					/**
					 * 总公司员工
					 */
					if("-1".equals(PID)){
						mr.pm.clear();
						mr.pm.put("COMPANY_ID", company.get("ID"));
						mr.pm.put("start", 1);
						mr.pm.put("end", 10000);
						departs = this.departService.listService(mr.pm);
						for (Map<String, Object> depart : departs) {
							depart.put("boxLabel", depart.get("TEXT"));
							depart.put("name", "depart");
							depart.put("inputValue", depart.get("ID"));
						}
					}else{
						
						mr.pm.clear();
						mr.pm.put("USER_ID", USER_ID);
						//部门管理员
						String IS_MANAGER = (String)user.get("IS_MANAGER");
						String MANAGER_DEPART_ID = "";
						boolean b = false;
						if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
							MANAGER_DEPART_ID = (String)user.get("DEPART_ID");
							b = true;
						}
						
						departs = this.companyService.searchManagerDepartService(mr.pm);
						for (Map<String, Object> depart : departs) {
							depart.put("boxLabel", depart.get("TEXT"));
							depart.put("name", "depart");
							depart.put("inputValue", depart.get("DEPART_ID"));
							if(MANAGER_DEPART_ID.equals(depart.get("DEPART_ID").toString())){
								b = false;
							}
						}
						if(b){
							Map<String, Object> managerDepart = new HashMap<String, Object>();
							managerDepart.put("boxLabel", user.get("DEPART_NAME"));
							managerDepart.put("name", "depart");
							managerDepart.put("inputValue", user.get("DEPART_ID"));
							departs.add(managerDepart);
						}
					}
					
				}
				
				Map<String, Object> departsInfo =  new HashMap<String, Object>();
				departsInfo.put("xtype", "checkboxgroup");
				departsInfo.put("padding", "0 0 0 5");
				departsInfo.put("columns", 1);
				departsInfo.put("items", departs);
				List<Map<String, Object>> _departs = new ArrayList<Map<String,Object>>();
				_departs.add(departsInfo);
				menu.put("items", _departs);
				company.put("menu", menu);
				
			}
			
			
		}
		String[] jsonConfig = {"CREATE_TIME"};
		request.setAttribute("companys", CommonUtils.checkList(companys)?this.getJsonArray(companys, jsonConfig):"[]");
		
		request.setAttribute("city", CommonUtils.checkString(city)?city:"[]");
		request.setAttribute("data", this.getJsonString(result));
		
		return new ModelAndView("/b2b/user/home");
	}
	
	
	@RequestMapping("/site/info")
	public Map<String, Object> siteInfo(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId){
		Map<String, Object> result = new HashMap<String, Object>();
		
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		if(CommonUtils.checkString(cityId) && cityId.equals("all")){
			cityId = "";
		}
		mr.pm.clear();
		mr.pm.put("USER_ID", user.get("ID"));
		List<Map<String, Object>> sites = this.companyService.listSiteInfoService(mr.pm);
		
		StringBuffer cityIds = new StringBuffer();
		if(CommonUtils.checkList(sites)){
			for (Map<String, Object> site : sites) {
				cityIds.append("'"+site.get("CITY_ID")+"',");
			}
		}
		
		mr.pm.clear();
		if(CommonUtils.checkString(cityId)){
			for (Map<String, Object> site : sites) {
				String CITY_ID = (String)site.get("CITY_ID");
				if(cityId.equals(CITY_ID)){
					mr.pm.put("CITY_IDS", "'"+cityId+"'");	
				}
			}
		}else{
			mr.pm.put("CITY_IDS", cityIds.substring(0, cityIds.length()-1));
		}
		
		mr.pm.put("TYPE", 1);
		double money = this.accountService.sumAccountDetailMoneyService(mr.pm);
		result.put("money", money);
		
		return result;
	}
	
	@RequestMapping("/site/account")
	public ListRange siteAccount(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String startDate, String endDate){
		ListRange json = new ListRange();
		List<Map<String, Object>> sites = this.companyService.listSiteInfoService(mr.pm);
		StringBuffer site_ids = new StringBuffer();
		if(CommonUtils.checkList(sites)){
			for (Map<String, Object> site : sites) {
				site_ids.append("'"+site.get("ID")+"',");
			}
		}
		mr.pm.clear();
		mr.pm.put("TYPE", 1);
		mr.pm.put("CITY_ID", cityId);
		mr.pm.put("start", 1);
		mr.pm.put("end", 30);
		if(!CommonUtils.checkString(startDate) && !CommonUtils.checkString(endDate)){
			mr.pm.put("START_DATE", DateUtil.parseDate(DateUtil.getNDay(new Date(), -90)));
			mr.pm.put("END_DATE", DateUtil.getNowDate());
		}else{
			mr.pm.put("START_DATE", startDate);
			mr.pm.put("END_DATE", endDate);
			mr.pm.put("start", 1);
			mr.pm.put("end", 100000);
			
		}
		List<Map<String, Object>> data = this.accountService.listAccountDetailService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/site/company")
	public ListRange siteCompany(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String startDate, String endDate){
		ListRange json = new ListRange();
		List<Map<String, Object>> sites = this.companyService.listSiteInfoService(mr.pm);
		StringBuffer site_ids = new StringBuffer();
		if(CommonUtils.checkList(sites)){
			for (Map<String, Object> site : sites) {
				site_ids.append("'"+site.get("ID")+"',");
			}
		}
		mr.pm.clear();
		mr.pm.put("TYPE", 1);
		mr.pm.put("CITY_ID", cityId);
		mr.pm.put("START_DATE", startDate);
		mr.pm.put("END_DATE", endDate);
		mr.pm.put("start", 1);
		mr.pm.put("end", 30);
		if(CommonUtils.checkString(startDate) && CommonUtils.checkString(endDate)){
			mr.pm.put("START_DATE", DateUtil.parseDate(DateUtil.getNDay(new Date(), -90)));
			mr.pm.put("END_DATE", DateUtil.getNowDate());
		}
		List<Map<String, Object>> data = this.accountService.listAccountDetailService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/site/company/order")
	public ListRange siteCompanyOrder(HttpServletRequest request,HttpServletResponse response,MapRange mr, String startDate, String endDate, String companyType, String cityId){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		/**
		        最近出团--线路订单/交通订单
			线路-交通-出团日期-人数-导出游客名单
			订单编号-外卖-结算-人(成人/儿童)-旅行社
		*/
		mr.pm.clear();
		String USER_TYPE = (String)user.get("USER_TYPE");
		if("0101".equals(USER_TYPE)){
			mr.pm.put("SITE_USER_ID", user.get("COMPANY_USER_ID"));
		}else{
			mr.pm.put("SITE_USER_ID", (String)user.get("ID"));
		}
		if(companyType.equals("1")){
			mr.pm.put("groupType", "sale");	
		}else{
			mr.pm.put("groupType", "buy");
		}
		mr.pm.put("start", 1);
		mr.pm.put("end", 30);
		mr.pm.put("CITY_ID", cityId);
		mr.pm.put("START_CREATE_TIME", startDate);
		mr.pm.put("END_CREATE_TIME", endDate);
		mr.pm.put("STATUSES", "2,3,4,5");
		mr.pm.put("PRODUCE_TYPES", "2,3");
		List<Map<String, Object>> data = this.statisticsService.listOrderGroupByCompanyService(mr.pm);

		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	/**
	 * 线路产品列表
	 * @param request
	 * @param response
	 * @param mr
	 * @return
	 */
	@RequestMapping("/routes")
	public ListRange routes(HttpServletRequest request,HttpServletResponse response,MapRange mr, String type){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		/**
		        最近出团--线路订单/交通订单
			线路-交通-出团日期-人数-导出游客名单
			订单编号-外卖-结算-人(成人/儿童)-旅行社
		*/
		
		mr.pm.put("ORDER_STATUS", "2");
		
		mr.pm.put("IS_ALONE", "0");
		mr.pm.put("USER_ID", (String)user.get("ID"));
		mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
		mr.pm = PowerFactory.getPower(request, response, "", "route-list", mr.pm);
		mr.pm.put("start", 1);
		mr.pm.put("end", 10);
		
		if(type.equals("zby")){
			mr.pm.put("ROUTE_TYPE", 1);
		}else if(type.equals("gny")){
			mr.pm.put("ROUTE_TYPE", 2);
		}else if(type.equals("cjy")){
			mr.pm.put("ROUTE_TYPE", 3);
		}else if(type.equals("hot")){
			//系统热卖
			mr.pm.put("ORDER", "HOT");
		}else if(type.equals("buy")){
			//购买过的产品
			mr.pm.put("ORDER", "BUY");
			mr.pm.put("BUY_COMPANY_ID", user.get("COMPANY_ID"));
		}else{
			//优惠活动产品
			mr.pm.remove("ORDER");
			mr.pm.remove("ORDER_STATUS");
			mr.pm.put("DISCOUNT", "YES");
			mr.pm.put("end", 1000);
		}
		
		List<Map<String, Object>> data = this.produceRouteService.listService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	/**
	 * 退款订单列表
	 * @param request
	 * @param response
	 * @param mr
	 * @return
	 */
	@RequestMapping("/refund/order")
	public ListRange refundOrder(HttpServletRequest request,HttpServletResponse response,MapRange mr, String startDate, String endDate, String departIds, String companyId){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		mr.pm.put("STATUS", 5);
		mr.pm.put("HAVE_REFUND", "YES");
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(user);
		params.put("departIds", departIds);
		params.put("companyId", companyId);
		StatisticsPowerSaleController.powerLevel(mr, (String)user.get("COMPANY_TYPE"), null, params, null, (String)user.get("USER_NAME"));
		mr.pm.put("start", 1);
		mr.pm.put("end", 10000);
		mr.pm.put("START_CREATE_TIME", startDate);
		mr.pm.put("END_CREATE_TIME", endDate);
		
		List<Map<String, Object>> data = this.statisticsService.listOrderService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/latest/route")
	public ListRange latestRoute(HttpServletRequest request,HttpServletResponse response,MapRange mr, String departIds, String dateType, String startDate, String endDate){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		/**
		        最近出团--线路订单/交通订单
			线路-交通-出团日期-人数-导出游客名单
			订单编号-外卖-结算-人(成人/儿童)-旅行社
		*/
		String USER_ID = (String)user.get("ID");
		String COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID");
		String COMPANY_ID = (String)user.get("COMPANY_ID");
		mr.pm.clear();
		String COMPANY_TYPE = String.valueOf(user.get("COMPANY_TYPE"));
		if(COMPANY_TYPE.equals("1")){
			
			if(CommonUtils.checkString(departIds)){
		    	JSONArray _departIds = JSONArray.fromObject(departIds);
		    	StringBuffer ids = new StringBuffer();
		    	for (Object departId : _departIds) {
		    		ids.append(departId+",");
				}
		    	mr.pm.put("SUPPLY_DEPART_IDS", ids.toString().substring(0, ids.length() -1 ).split(","));
		    }else if(USER_ID.equals(COMPANY_USER_ID)){
				mr.pm.put("SUPPLY_COMPANY_ID", COMPANY_ID);
			}else{
				String DEPART_IDS = (String)user.get("DEPART_IDS");
				if(CommonUtils.checkString(DEPART_IDS)){
					DEPART_IDS =DEPART_IDS +","+user.get("DEPART_ID");
					mr.pm.put("SUPPLY_DEPART_IDS", DEPART_IDS.split(","));
				}else{
					mr.pm.put("SUPPLY_DEPART_ID", user.get("DEPART_ID"));
				}
			}
		}else{
			
			if(USER_ID.equals(COMPANY_USER_ID)){
				mr.pm.put("CREATE_COMPANY_ID", COMPANY_ID);
			}else{
				//部门经理看部门员工的所有订单
				String IS_MANAGER = (String)user.get("IS_MANAGER");
				if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
					mr.pm.put("CREATE_DEPART_ID", user.get("DEPART_ID"));
				}else{
					mr.pm.put("CREATE_USER_ID", USER_ID);
				}
			}
		}

		mr.pm.put("ROUTE_TYPES", "2,3");
		mr.pm.put("STATUS", "2");
		mr.pm.put("LATEST_START_DATE", DateUtil.getNowDate());
		mr.pm.put("start", 1);
		mr.pm.put("end", 10000);
		mr.pm.put("ORDER_START_DATE", "YES");
		
		
		if(CommonUtils.checkString(dateType)){
			if(dateType.equals("2")){
				mr.pm.put("CT_START_TIME", startDate);
				mr.pm.put("CT_END_TIME", endDate);
			}else{
				mr.pm.put("START_CREATE_TIME", startDate);
				mr.pm.put("END_CREATE_TIME", endDate);
			}
		}
		
		List<Map<String, Object>> data = this.statisticsService.listOrderService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/latest/traffic")
	public ListRange latestTraffic(HttpServletRequest request,HttpServletResponse response,MapRange mr){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		/**
		        最近出团--线路订单/交通订单
			线路-交通-出团日期-人数-导出游客名单
			订单编号-外卖-结算-人(成人/儿童)-旅行社
		*/
		String USER_ID = (String)user.get("ID");
		String COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID");
		String COMPANY_ID = (String)user.get("COMPANY_ID");
		mr.pm.clear();
		if(USER_ID.equals(COMPANY_USER_ID)){
			mr.pm.put("SUPPLY_COMPANY_ID", COMPANY_ID);
		}else{
			//部门经理看部门员工的所有订单
			String IS_MANAGER = (String)user.get("IS_MANAGER");
			if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
				mr.pm.put("SUPPLY_DEPART_ID", user.get("DEPART_ID"));
			}else{
				mr.pm.put("SUPPLY_USER_ID", USER_ID);
			}
		}
		mr.pm.put("ROUTE_TYPES", "1");
		mr.pm.put("STATUS", "2");
		mr.pm.put("LATEST_START_DATE", DateUtil.getNowDate());
		mr.pm.put("start", 1);
		mr.pm.put("end", 10000);
		mr.pm.put("ORDER_START_DATE", "YES");
		List<Map<String, Object>> data = this.statisticsService.listOrderService(mr.pm);

		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/order/num")
	public ListRange orderNum(HttpServletRequest request,HttpServletResponse response,MapRange mr, String startDate, String endDate){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		/**
		        最近出团--线路订单/交通订单
			线路-交通-出团日期-人数-导出游客名单
			订单编号-外卖-结算-人(成人/儿童)-旅行社
		*/
		mr.pm.clear();
		String USER_ID = (String)user.get("ID");
		String COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID");
		String COMPANY_ID = (String)user.get("COMPANY_ID");
		
		if(USER_ID.equals(COMPANY_USER_ID)){
			mr.pm.put("SUPPLY_COMPANY_ID", COMPANY_ID);
		}else{
			//部门经理看部门员工的所有订单
			String IS_MANAGER = (String)user.get("IS_MANAGER");
			if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
				mr.pm.put("SUPPLY_DEPART_ID", user.get("DEPART_ID"));
			}else{
				mr.pm.put("SUPPLY_USER_ID", USER_ID);
			}
		}
		mr.pm.put("START_CREATE_TIME", startDate);
		mr.pm.put("END_CREATE_TIME", endDate);
		mr.pm.put("groupType", "buy");
		mr.pm.put("start", 1);
		mr.pm.put("end", 10000);
		List<Map<String, Object>> data = this.statisticsService.listOrderGroupByCompanyService(mr.pm);

		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	private double getActualAmount(List<Map<String, Object>> data){
		double actual_amount = 0.0;
		if(CommonUtils.checkList(data)){
			for (Map<String, Object> d : data) {
				actual_amount = actual_amount + Double.parseDouble(String.valueOf(d.get("ACTUAL_AMOUNT")));
			}
		}
		return actual_amount;
	}
	
	@RequestMapping("/export/visitor")
	public void exportVisitor(HttpServletRequest request,HttpServletResponse response,MapRange mr, String startDate, String produceId){
		
		mr.pm.put("START_DATE", startDate);
		mr.pm.put("PRODUCE_ID", produceId);
		mr.pm.put("STATUS", "2");
		mr.pm.put("start", 1);
		mr.pm.put("end", 10000);
		List<Map<String, Object>> orders = this.orderService.listOrderService(mr.pm);
		
		/**
		 * 保存基本信息
		 */
		try {
			String filePath = ConfigLoader.config.getStringConfigDetail("system.file.path");
			
			File file = new File(filePath+"\\visitor_export.xls");///-----------------------------------读取文件的地址
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook workBook= new HSSFWorkbook(fis);
			/**
			 * 整理订单ID
			 * 查询游客名单
			 */
			StringBuffer orderIds = new StringBuffer();
			for (Map<String, Object> order: orders) {
				 String ORDER_ID = (String)order.get("ID");
				 orderIds.append("'"+ORDER_ID+"',");
			}
			mr.pm.clear();
			mr.pm.put("ORDER_IDS", orderIds.substring(0, orderIds.length() - 1));
			mr.pm.put("start", 1);
			mr.pm.put("end", 10000);
			List<Map<String, Object>> visitors = this.visitorOrderService.listOrderVisitorAndTrafficService(mr.pm);
			
			String ORDER_ID = "", TRAFFIC_ID = "", sheetName = "";
			int row_num = 3, num = 1;
			HSSFSheet hssfSheet = null;
			for (Map<String, Object> visitor: visitors) {
				
				if(!CommonUtils.checkString(TRAFFIC_ID) || !TRAFFIC_ID.equals((String)visitor.get("TRAFFIC_ID"))){
					String BEGIN_CITY_NAME = (String)visitor.get("BEGIN_CITY_NAME");
					String END_CITY_NAME = (String)visitor.get("END_CITY_NAME");
					String EXCA = (String)visitor.get("EXCA");
					
					sheetName = EXCA+"("+BEGIN_CITY_NAME+"-"+END_CITY_NAME+")";
					if(!CommonUtils.checkString(ORDER_ID)){
						workBook.setSheetName(0, sheetName);
						hssfSheet = workBook.getSheet(sheetName);
					}else{
						hssfSheet = workBook.createSheet(sheetName);
					}
					ExcelUtil.head(workBook, 1, hssfSheet);
					
					hssfSheet.getRow(1).getCell(1).setCellValue(" "+(String)visitor.get("PRODUCE_NAME"));
					hssfSheet.getRow(1).getCell(6).setCellValue(" "+(String)visitor.get("EXCA"));
					row_num = 3;
					ORDER_ID = "";
				}
				
				if(!CommonUtils.checkString(ORDER_ID) || !ORDER_ID.equals((String)visitor.get("ORDER_ID"))){
					ExcelUtil.body(workBook, 1, hssfSheet, hssfSheet.getLastRowNum());
					
					row_num = hssfSheet.getLastRowNum() - 2;
					
					hssfSheet.getRow(row_num).getCell(0).setCellValue(" "+(String)visitor.get("NO"));
					hssfSheet.getRow(row_num).getCell(6).setCellValue(" "+(String)visitor.get("BUY_COMPANY"));
					
					row_num++;
					
					hssfSheet.getRow(row_num).getCell(1).setCellValue(" "+(String)visitor.get("CHINA_NAME"));
					hssfSheet.getRow(row_num).getCell(4).setCellValue(" "+(String)visitor.get("BUY_MOBILE"));
					
					hssfSheet.getRow(row_num).getCell(6).setCellValue(" "+(String)visitor.get("VISITOR_CONCAT"));
					hssfSheet.getRow(row_num).getCell(8).setCellValue(" "+(String)visitor.get("VISITOR_MOBILE"));
				}
				
//				序号	姓名	性别	类型	手机	证件类型	证件号码
				
				row_num = hssfSheet.getLastRowNum()+1;
				ExcelUtil.emptyRow(workBook, 1, hssfSheet, row_num);
				
				HSSFRow row = hssfSheet.getRow(row_num);
				row.getCell(0).setCellValue(num);
				row.getCell(1).setCellValue((String)visitor.get("NAME"));
				row.getCell(2).setCellValue(String.valueOf(visitor.get("SEX")).equals("1") ? "男" : "女");
				row.getCell(3).setCellValue((String)visitor.get("ATTR_NAME"));
				row.getCell(4).setCellValue((String)visitor.get("MOBILE"));
				row.getCell(5).setCellValue((String)visitor.get("CARD_TYPE"));
				row.getCell(6).setCellValue((String)visitor.get("CARD_NO"));
				num++;
				ORDER_ID = (String)visitor.get("ORDER_ID");
				TRAFFIC_ID = (String)visitor.get("TRAFFIC_ID");
				
			}
			workBook.removeSheetAt(1);
			/**
			 * 输出文件
			 */
			response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-disposition", "attachment;filename=visitor-"+DateUtil.getNowDate()+".xls");    
	        ServletOutputStream ouputStream = response.getOutputStream();    
	        workBook.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();    
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response,MapRange mr, String DEPART_ID, String COMPANY_ID, String departType){
		ListRange json = new ListRange();
		String query = toString(request.getParameter("query"));
		try {
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		int start = toInt(request.getParameter("start"));
		int limit = toInt(request.getParameter("limit"));
		
		mr.pm.put("start", start + 1);
		mr.pm.put("end", start + limit);
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if(CommonUtils.checkString(departType) && !CommonUtils.checkString(DEPART_ID)){
			json.setData(data);
			json.setTotalSize(0);
			json.setSuccess(true);
			return json;
		}
		mr.pm.put("DEPART_ID", DEPART_ID);
		mr.pm.put("COMPANY_ID", COMPANY_ID);
		data = userService.listService(mr.pm);
		int total = userService.countService(mr.pm);
		json.setData(data);
		json.setTotalSize(total);
		json.setSuccess(true);
		
		return json;
	}
	
	@RequestMapping("/route/stat")
	public ListRange routeStat(HttpServletRequest request,HttpServletResponse response,MapRange mr, String startDate, String endDate, String departIds){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(user);
		params.put("departIds", departIds);
		StatisticsPowerSaleController.powerLevel(mr, (String)user.get("COMPANY_TYPE"), null, params, null, (String)user.get("USER_NAME"));
//		mr.pm.put("SUPPLY_COMPANY_ID", user.get("COMPANY_ID"));
		
		mr.pm.put("STATUSES", "2,3,4,5");
		mr.pm.put("PRODUCE_TYPES", "2,3");
		mr.pm.put("TRUE_SALE_AMOUNT", "YES");
		mr.pm.put("START_CREATE_TIME", startDate);
		mr.pm.put("END_CREATE_TIME", endDate);
		
		List<Map<String, Object>> data = this.statisticsService.listOrderGroupByProduceService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/end/city/stat")
	public ListRange endCityStat(HttpServletRequest request,HttpServletResponse response,MapRange mr, String startDate, String endDate){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(user);
		StatisticsPowerSaleController.powerLevel(mr, (String)user.get("COMPANY_TYPE"), null, params, null, (String)user.get("USER_NAME"));
//		mr.pm.put("SUPPLY_COMPANY_ID", user.get("COMPANY_ID"));
		mr.pm.put("STATUSES", "2,3,4,5");
		mr.pm.put("PRODUCE_TYPES", "2,3");
		mr.pm.put("TRUE_SALE_AMOUNT", "YES");
		mr.pm.put("START_CREATE_TIME", startDate);
		mr.pm.put("END_CREATE_TIME", endDate);
		List<Map<String, Object>> data = this.statisticsService.listRouteGroupByEndCityService(mr.pm);
		
		Map<String, Object> _data = new HashMap<String, Object>();
		if(CommonUtils.checkList(data)){
			
			List<Map<String, Object>> PERSON_COUNT = new ArrayList<Map<String, Object>>();
			
			List<String> LEGEND = new ArrayList<String>();
			
			for (Map<String, Object> d : data) {
					
				Map<String, Object> _PERSON_COUNT = new HashMap<String, Object>();
				_PERSON_COUNT.put("value", d.get("PERSON_COUNT"));
				_PERSON_COUNT.put("name", d.get("CITY_NAME"));
				PERSON_COUNT.add(_PERSON_COUNT);
				
				LEGEND.add((String)d.get("CITY_NAME"));
				
			}
			
			_data.put("PERSON_COUNT", PERSON_COUNT);
			_data.put("LEGEND", LEGEND);
			
		}
		
		data.clear();
		data.add(0, _data);
		json.setData(data);
		
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/save")
	public ListRange add(HttpServletRequest request,HttpServletResponse response,MapRange mr){
		ListRange json = new ListRange();
		try {
			mr.pm.put("UUID", this.getUuid());
			mr.pm.put("USER_PWD", MD5.toMD5(MD5.toMD5(toString(mr.pm.get("USER_PWD")))));
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("CREATE_USER", user.get("ID").toString());
			mr.pm.put("UPDATE_USER", user.get("ID").toString());
			int ok = userService.saveService(mr.pm);
			if(ok==1){
				json.setSuccess(true);
			}else{
				json.setStatusCode(ok+"");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("添加用户异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加用户异常");
		}
		return json;
	}
	
	@RequestMapping("/updatePWD")
	public ListRange updatePWD(HttpServletRequest request,HttpServletResponse response,MapRange mr){
		ListRange json = new ListRange();
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			String USER_PWD = (String)mr.pm.get("USER_PWD");
			String userPwdAgin = (String)mr.pm.get("userPwdAgin");
			if(!USER_PWD.equals(userPwdAgin)){
				json.setSuccess(false);
				json.setStatusCode("-1");
				json.setMessage("密码与确认密码不相等");
			}
			params.put("USER_PWD", MD5.toMD5(MD5.toMD5(USER_PWD)));
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			params.put("ID", (String)user.get("ID"));
			params.put("UPDATE_USER", (String)user.get("ID"));
			this.userService.updatePWDService(params);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("添加用户异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加用户异常");
		}
		return json;
	}
	
	@RequestMapping("/del")
	public ListRange del(HttpServletRequest request,HttpServletResponse response, String models){
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				this.userService.delService(jobject.getString("ID"));
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除用户异常",e);
			json.setSuccess(false);
			json.setMessage("删除用户异常");
		}
		return json;
	}
	@RequestMapping("/dels")
	public ListRange dels(HttpServletRequest request,HttpServletResponse response){
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(toString(request.getParameter("IDS")));
			Object[] objArray = jarray.toArray();
			List<String> list = new ArrayList<String>();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				list.add(jobject.getString("ID"));
			}
			userService.delBatchService(list);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("批量删除用户异常",e);
			json.setSuccess(false);
			json.setMessage("批量删除用户异常");
		}
		return json;
	}
	@RequestMapping("/set/role")
	public ListRange setRole(HttpServletRequest request,HttpServletResponse response,String models,String userId){
		ListRange json = new ListRange();
		try {
			userService.clearRoleService(userId);
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				Map<String,Object> tp = new HashMap<String,Object>();
				tp.put("ROLE_ID", jobject.getString("ID"));
				tp.put("USER_ID", userId);
				userService.setRoleService(tp);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("配置模块异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	@RequestMapping("/online")
	public ModelAndView online(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/b2b/user/online");
	}
	@RequestMapping("/online/list")
	public ListRange onlineList(HttpServletRequest request,HttpServletResponse response){
		ListRange json = new ListRange();
		try {
			 Map<Object, Object> online = ServiceProxyFactory.getProxy().getS();
//			 Map<String, Map<String, Object>> online = ServiceProxyFactory.getOnLineUsers();
			 
			 List<Map<String, Object>> employ = new ArrayList<Map<String, Object>>();
			 
			 for (Map.Entry<Object, Object> e: online.entrySet()) {
//				 if(!"admin".equals(((Map<String, Object>)e.getValue()).get("USER_NAME"))){
//					 employ.add(e.getValue());
//				 }
				 Map<String,Object> m = (Map<String, Object>)e.getValue();
				 if(!"admin".equals(((Map<String, Object>)m.get(Constants.SESSION_USER_KEY)).get("USER_NAME").toString())){
					 employ.add((Map<String, Object>)m.get(Constants.SESSION_USER_KEY));
				 }
			 }
			 json.setData(employ);
			 json.setSuccess(true);
		} catch (Exception e) {
			log.error("online 发生异常",e);
		}
		return json;
	}
	@RequestMapping("/downline")
	public ListRange downline(HttpServletRequest request,HttpServletResponse response,String userId){
		ListRange json = new ListRange();
		ServiceProxyFactory.getProxy().removeLoginInfobyUserId(userId);
//		SecurityContextHolder.getContext().logoutUserByUser(userId);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/switch")
	public ListRange switchs(HttpServletRequest request,HttpServletResponse response,String models){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				String isUse = jobject.getString("IS_USE");
				if(isUse.equals("1")){
					isUse = "0";
				}else{
					isUse = "1";
				}
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("UPDATE_USER", (String)user.get("ID"));
				params.put("ID", jobject.getString("ID"));
				params.put("IS_USE", isUse);
				this.userService.editService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("启用/禁用异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/set/module")
	public ListRange setModule(HttpServletRequest request,HttpServletResponse response,String models,String userId){
		ListRange json = new ListRange();
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("models", models);
			params.put("USER_ID", userId);
			this.userService.saveUserModuleService(params);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("配置模块异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/set/power")
	public ListRange setPower(HttpServletRequest request,HttpServletResponse response,String models,String userId,String moduleId){
		ListRange json = new ListRange();
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("models", models);
			params.put("USER_ID", userId);
			params.put("MODULE_ID", moduleId);
			this.userService.saveUserPowerService(params);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("配置权限异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/resetPWD")
	public ListRange resetPWD(HttpServletRequest request, HttpServletResponse response, String userId){
		ListRange json = new ListRange();
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("ID", userId);
			params.put("USER_PWD", MD5.toMD5(MD5.toMD5("111111")));
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			params.put("UPDATE_USER", (String)user.get("ID"));
			this.userService.editService(params);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("配置权限异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/regUserMobile")
	public ListRange regUserMobile(HttpServletRequest request, HttpServletResponse response, String mobile, String code){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			//验证码是否填写正确
			//修改用户手机号码
			String _code = (String)request.getSession().getAttribute("code");
			if(_code.equals(code)){
				params.put("ID", (String)user.get("ID"));
				params.put("UPDATE_USER", (String)user.get("ID"));
				params.put("MOBILE", mobile);
				this.userService.editService(params);
				request.getSession().removeAttribute("code");
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
				json.setStatusCode("-1");//验证码填写错误
			}
			
		} catch (Exception e) {
			log.error("手机号码修改失败",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/set/finance")
	public ListRange setCaiWu(HttpServletRequest request, HttpServletResponse response, MapRange mr, String departId, String userId){
		ListRange json = new ListRange();
		try {
			/**
			 * 部门经理是否已经设置
			 */
			mr.pm.clear();
			mr.pm.put("ID", userId);
			Map<String, Object> user = this.userService.detailUserService(mr.pm);
			
			mr.pm.clear();
			mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
			mr.pm.put("IS_FINANCE", "1");
			mr.pm.put("start", 1);
			mr.pm.put("end", 1);
			List<Map<String, Object>> data = this.userService.listService(mr.pm);
			
			mr.pm.clear();
			mr.pm.put("USER_ID", userId);
			if(CommonUtils.checkList(data)){
				String USER_ID = (String)data.get(0).get("ID");
				if(USER_ID.equals(userId)){
					data.get(0).put("USER_ID", (String)data.get(0).get("ID"));
					data.get(0).put("IS_FINANCE", "0");
					this.userService.updateUserPlusService(data.get(0));
					json.setSuccess(true);
				}else{
					json.setStatusCode("-4");
					json.setSuccess(false);
				}
			}else{
				if(CommonUtils.checkString(user.get("IS_FINANCE"))){
					user.put("USER_ID", (String)user.get("ID"));
					user.put("IS_FINANCE", "1");
					this.userService.updateUserPlusService(user);
				}else{
					user.put("USER_ID", (String)user.get("ID"));
					user.put("IS_MANAGER", "0");
					user.put("IS_COUNSELOR", "0");
					user.put("IS_CONTACTS", "0");
					user.put("IS_CONTACTS", "1");
					this.userService.saveUserPlusService(mr.pm);
				}
				json.setSuccess(true);
			}
		} catch (Exception e) {
			log.error("设置财务短信通知异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/set/manager")
	public ListRange setManager(HttpServletRequest request, HttpServletResponse response, MapRange mr, String departId, String userId){
		ListRange json = new ListRange();
		try {
			/**
			 * 部门经理是否已经设置
			 */
			
			mr.pm.clear();
			mr.pm.put("ID", userId);
			Map<String, Object> user = this.userService.detailUserService(mr.pm);
			
			mr.pm.clear();
			mr.pm.put("DEPART_ID", user.get("DEPART_ID"));
			mr.pm.put("IS_MANAGER", "1");
			mr.pm.put("start", 1);
			mr.pm.put("end", 1);
			List<Map<String, Object>> data = this.userService.listService(mr.pm);
			
			mr.pm.clear();
			mr.pm.put("USER_ID", userId);
			if(CommonUtils.checkList(data)){
				//设置了部门经理
				String USER_ID = (String)data.get(0).get("ID");
				if(USER_ID.equals(userId)){
					//部门经理是当前用户,修改当前用户 
					mr.pm.put("IS_MANAGER", "0");
					mr.pm.put("IS_COUNSELOR", data.get(0).get("IS_COUNSELOR"));
					mr.pm.put("IS_CONTACTS", data.get(0).get("IS_CONTACTS"));
					this.userService.updateUserPlusService(mr.pm);
					json.setSuccess(true);
				}else{
					//不是当前用户,提示用户,经理已被设置,需要先取消
					json.setStatusCode("-1");
					json.setSuccess(false);
				}
			}else{
				mr.pm.put("IS_MANAGER", "1");
				if(CommonUtils.checkString(user.get("IS_MANAGER"))){
					mr.pm.put("IS_COUNSELOR", user.get("IS_COUNSELOR"));
					mr.pm.put("IS_CONTACTS", user.get("IS_CONTACTS"));
					this.userService.updateUserPlusService(mr.pm);
				}else{
					mr.pm.put("IS_COUNSELOR", "0");
					mr.pm.put("IS_CONTACTS", "0");
					this.userService.saveUserPlusService(mr.pm);
				}
				json.setSuccess(true);
			}
			
		} catch (Exception e) {
			log.error("设置部门经理异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/set/counselor")
	public ListRange setCounselor(HttpServletRequest request, HttpServletResponse response, MapRange mr, String departId, String userId){
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("ID", userId);
			Map<String, Object> user = this.userService.detailUserService(mr.pm);
			
			mr.pm.clear();
			mr.pm.put("USER_ID", userId);
			String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
			if(CommonUtils.checkString(COMPANY_TYPE) && (
					COMPANY_TYPE.equals("2") || COMPANY_TYPE.equals("3") || COMPANY_TYPE.equals("4") || COMPANY_TYPE.equals("5") || COMPANY_TYPE.equals("6"))){
				if(CommonUtils.checkString(user.get("IS_COUNSELOR"))){
					mr.pm.put("IS_COUNSELOR", "1");
					if(user.get("IS_COUNSELOR").toString().equals("1")){
						mr.pm.put("IS_COUNSELOR", "0");
					}
					mr.pm.put("IS_MANAGER", user.get("IS_MANAGER"));
					mr.pm.put("IS_CONTACTS", user.get("IS_CONTACTS"));
					this.userService.updateUserPlusService(mr.pm);
				}else{
					mr.pm.put("IS_COUNSELOR", "1");
					mr.pm.put("IS_MANAGER", "0");
					mr.pm.put("IS_CONTACTS", "0");
					this.userService.saveUserPlusService(mr.pm);
				}
				json.setSuccess(true);
			}else{
				json.setStatusCode("-1");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("设置顾问异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/set/contacts")
	public ListRange setContacts(HttpServletRequest request, HttpServletResponse response, MapRange mr, String userId){
		ListRange json = new ListRange();
		try {
			
			
			mr.pm.clear();
			mr.pm.put("ID", userId);
			mr.pm.put("USER_TYPE", "0101");
			Map<String, Object> user = this.userService.detailUserService(mr.pm);
			
			/**
			 * 联系人是否是站用户
			 */
			if(CommonUtils.checkMap(user)){
				
				String MOBILE = (String)user.get("MOBILE");
				if(!CommonUtils.checkString(MOBILE)){
					json.setSuccess(false);
					json.setStatusCode("-3");
					return json;
				}
				
				String CITY_ID = (String)user.get("CITY_ID");
				
				/**
				 * 设置的用户是否已是联系人
				 */
				String IS_CONTACTS = (String)user.get("IS_CONTACTS");
				if(CommonUtils.checkString(IS_CONTACTS) && IS_CONTACTS.equals("1")){
					
					mr.pm.put("USER_ID", userId);
					mr.pm.put("IS_COUNSELOR", user.get("IS_COUNSELOR"));
					mr.pm.put("IS_MANAGER", user.get("IS_MANAGER"));
					mr.pm.put("IS_CONTACTS", "0");
					this.userService.updateUserPlusService(mr.pm);
					
					json.setSuccess(true);
					
				}else{
					/**
					 * 是否已设置城市联系人
					 */
					mr.pm.clear();
					mr.pm.put("CITY_ID", CITY_ID);
					mr.pm.put("IS_CONTACTS", "1");
					mr.pm.put("TYPE", "0101");
					mr.pm.put("start", 1);
					mr.pm.put("end", 1);
					List<Map<String, Object>> users = this.userService.listService(mr.pm);
					if(CommonUtils.checkList(users)){
						for (Map<String, Object> u : users) {
							mr.pm.put("USER_ID", u.get("ID"));
							mr.pm.put("IS_COUNSELOR", u.get("IS_COUNSELOR"));
							mr.pm.put("IS_MANAGER", u.get("IS_MANAGER"));
							mr.pm.put("IS_CONTACTS", "0");
							this.userService.updateUserPlusService(mr.pm);
						}
					}
					
					if(CommonUtils.checkString(IS_CONTACTS)){
						mr.pm.put("USER_ID", userId);
						mr.pm.put("IS_COUNSELOR", user.get("IS_COUNSELOR"));
						mr.pm.put("IS_MANAGER", user.get("IS_MANAGER"));
						mr.pm.put("IS_CONTACTS", "1");
						this.userService.updateUserPlusService(mr.pm);
					}else{
						mr.pm.put("USER_ID", userId);
						mr.pm.put("IS_COUNSELOR", "0");
						mr.pm.put("IS_MANAGER", "0");
						mr.pm.put("IS_CONTACTS", "1");
						this.userService.saveUserPlusService(mr.pm);
					}
					
					json.setSuccess(true);
				}
			}else{
				json.setSuccess(false);
				json.setStatusCode("-2");
			}
			
			
		} catch (Exception e) {
			log.error("设置站联系人异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/set/info")
	public ListRange setUsreInfo(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("UPDATE_USER", user.get("USER_NAME"));
			this.userService.editService(mr.pm);
			json.setSuccess(true);
			user.put("SIGNATURE", mr.pm.get("SIGNATURE"));
		} catch (Exception e) {
			log.error("保存个人信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	@RequestMapping("/info")
	public ListRange info(HttpServletRequest request, HttpServletResponse response, MapRange mr, String id){
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("ID", id);
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			Map<String, Object> user = this.userService.detailUserService(mr.pm);
			data.add(user);
			json.setData(data);
		} catch (Exception e) {
			log.error("查询个人信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
}
