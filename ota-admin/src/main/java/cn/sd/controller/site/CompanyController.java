package cn.sd.controller.site;

import cn.sd.core.config.ConfigLoader;
import cn.sd.core.util.*;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.entity.site.WapSettingEntity;
import cn.sd.power.PowerFactory;
import cn.sd.service.finance.IFinanceService;
import cn.sd.service.order.IOrderService;
import cn.sd.service.resource.IRouteService;
import cn.sd.service.site.ICompanyService;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.*;

@RestController
@RequestMapping("/site/company")
public class CompanyController extends ExtSupportAction {

	private static Log log = LogFactory.getLog(AreaController.class);
	
	@Resource
	private ICompanyService companyService;
	@Resource
	private IOrderService orderService;
	@Resource
	private IFinanceService financeService;
	@Resource
	private IRouteService routeService;
	
	@RequestMapping("/save/choose/depart")
	public ListRange saveChooseDepart(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		try {
			if(CommonUtils.checkString(mr.pm.get("DEPART_ID"))){
				String[] departIds = mr.pm.get("DEPART_ID").toString().split(",");
				List<String> DEPART_IDS = new ArrayList<String>();
				for (String departId : departIds) {
					DEPART_IDS.add(departId);
				}
				mr.pm.put("DEPART_IDS", DEPART_IDS);
			}
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			this.companyService.saveManagerDepartService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存部门权限异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存部门权限异常");
		}
		return json;
	}
	
	@RequestMapping("/choose/depart")
	public ListRange chooseDepart(HttpServletRequest request,HttpServletResponse response, MapRange mr, String userId){
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("USER_ID", userId);
			List<Map<String, Object>> data = this.companyService.searchManagerDepartService(mr.pm);
			List<String> IDS = new ArrayList<String>();
			for (Map<String, Object> d : data) {
				IDS.add((String)d.get("DEPART_ID"));
			}
			json.setData(IDS);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询部门权限异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询部门权限异常");
		}
		return json;
	}
	
	@RequestMapping("/save/start/city")
	public ListRange saveStartCity(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		try {
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			
			String[] BEGIN_CITYS = request.getParameterValues("BEGIN_CITY");
			if(BEGIN_CITYS != null && BEGIN_CITYS.length > 0){
				List<Map<String, Object>> citys = new ArrayList<Map<String, Object>>();
				for (String BEGIN_CITY : BEGIN_CITYS) {
					Map<String, Object> city = new HashMap<String, Object>();
					city.put("CITY_ID", BEGIN_CITY.split(",")[0]);
					city.put("CITY_NAME", BEGIN_CITY.split(",")[1]);
					citys.add(city);
				}
				mr.pm.put("citys", citys);
			}
			this.companyService.saveCompanyStartCityService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存出发城市异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存出发城市异常");
		}
		return json;
	}
	
	@RequestMapping("/cooperation/supply")
	public ListRange cooperationSupply(HttpServletRequest request,HttpServletResponse response, MapRange mr, String companyId, String query){
		ListRange json = new ListRange();
		try {
			mr.pm.put("COMPANY_ID", companyId);
			List<Map<String, Object>> data = this.companyService.searchCompanyStartCityService(mr.pm);
			mr.pm.clear();
			mr.pm.put("citys", data);
			mr.pm.put("COMPANY_TYPE", 1);
			if(CommonUtils.checkString(query)){
				mr.pm.put("query", new String(query.getBytes("ISO_8859-1"), "UTF-8"));
			}
			data = this.companyService.searchCompanyStartCityService(mr.pm);
			List<Map<String, Object>> supplys = new ArrayList<Map<String, Object>>();
			List<String> companyNames = new ArrayList<String>();
			boolean b = true;
			for (Map<String, Object> d : data) {
				
				b = true;
				 
				for (String companyName : companyNames) {
					if(d.get("COMPANY_NAME").toString().equals(companyName)){
						b = false;
						break;
					}
				}
				if(b){
					supplys.add(d);
					companyNames.add(d.get("COMPANY_NAME").toString());
				}
				
			}
			json.setData(supplys);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询合作商户异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询合作商户异常");
		}
		return json;
	}
	
	@RequestMapping("/list/start/city")
	public ListRange listStartCity(HttpServletRequest request,HttpServletResponse response, MapRange mr, String companyId){
		ListRange json = new ListRange();
		try {
			mr.pm.put("COMPANY_ID", companyId);
			List<Map<String, Object>> data = this.companyService.searchCompanyStartCityService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询出发城市异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询出发城市异常");
		}
		return json;
	}
	
	@RequestMapping("/init/pay/pwd")
	public ListRange initPayPwd(HttpServletRequest request,HttpServletResponse response, MapRange mr, String departId){
		ListRange json = new ListRange();
		try {
			String pay_pwd = (String)mr.pm.get("PAY_PWD");
			pay_pwd = CommonUtils.MD5(pay_pwd);
			mr.pm.put("PAY_PWD", pay_pwd);
			this.companyService.initPayPwdService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("初始化支付密码异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("初始化支付密码异常");
		}
		return json;
	}
	
	@RequestMapping("/update/pay/pwd")
	public ListRange updatePayPwd(HttpServletRequest request,HttpServletResponse response, MapRange mr, String departId){
		Map<String,Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			if(String.valueOf(user.get("PAY_PWD_TYPE")).equals("0")){
				json.setStatusCode("-2");
				json.setSuccess(false);
				return json;	
			}
			
			String pay_pwd = (String)user.get("PAY_PWD");
			String old_pay_pwd = (String)mr.pm.get("OLD_PWD");
			old_pay_pwd = CommonUtils.MD5(old_pay_pwd);
			if(!pay_pwd.equals(old_pay_pwd)){
				json.setStatusCode("-1");
				json.setSuccess(false);
				return json;
			}
			String new_pay_pwd = (String)mr.pm.get("PAY_PWD");
			new_pay_pwd = CommonUtils.MD5(new_pay_pwd);
			mr.pm.put("PAY_PWD", new_pay_pwd);
			this.companyService.updatePayPwdService(mr.pm);
			user.put("PAY_PWD_TYPE", "2");
			user.put("PAY_PWD", new_pay_pwd);
			request.getSession().setAttribute("S_USER_SESSION_KEY", user);
		} catch (Exception e) {
			log.error("修改支付密码异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("修改支付密码异常");
		}
		return json;
	}
	
	@RequestMapping("/get/money")
	public ListRange getCompanyMoney(HttpServletRequest request,HttpServletResponse response, MapRange mr, String departId){
		ListRange json = new ListRange();
		try {
			mr.pm.put("DEPART_ID", departId);
			Map<String, Object> data = this.companyService.getCompanyMoneyByDepartIdService(mr.pm);
			if(CommonUtils.checkMap(data) && CommonUtils.checkString(data.get("AMOUNT"))){
				json.setMessage(String.valueOf(data.get("AMOUNT")));
			}else{
				json.setMessage("0");
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("修改部门充值可用金额异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("修改部门充值可用金额异常");
		}
		return json;
	}
	
	@RequestMapping("/update/money")
	public ListRange updateMoney(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		Map<String,Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			mr.pm.put("ID", CommonUtils.uuid());
			mr.pm.put("TYPE", "0");
			mr.pm.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			this.companyService.updateUserCompanyMoneyService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("修改部门充值可用金额异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("修改部门充值可用金额异常");
		}
		return json;
	}
	
	@RequestMapping("/funds/log")
	public ListRange fundsLog(HttpServletRequest request,HttpServletResponse response, MapRange mr, String companyId, String beginTime, String endTime){
		ListRange json = new ListRange();
		try {
			mr.pm.put("END_TIME", endTime);
			mr.pm.put("BEGIN_TIME", beginTime);
			mr.pm.put("COMPANY_ID", companyId);
			List<Map<String, Object>> data = this.companyService.listUserCompanyFundsLogService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询公司资金流水异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询公司资金流水异常");
		}
		return json;
	}
	
	@RequestMapping("/funds/log/export")
	public void fundsLogExport(HttpServletRequest request,HttpServletResponse response, MapRange mr, String companyId, String beginTime, String endTime){
		try {
			mr.pm.put("COMPANY_ID", companyId);
			mr.pm.put("BEGIN_TIME", beginTime);
			mr.pm.put("END_TIME", endTime);
			List<Map<String, Object>> data = this.companyService.listUserCompanyFundsLogService(mr.pm);
			
			String filePath = ConfigLoader.config.getStringConfigDetail("system.file.path");
			
			File file = new File(filePath+"/company_funds .xls");///-----------------------------------读取文件的地址
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook workBook= new HSSFWorkbook(fis);
			int row_num = 1;
			HSSFSheet hssfSheet = workBook.getSheet("Sheet1");
			for (Map<String, Object> d : data) {
				HSSFRow row = hssfSheet.createRow(row_num);
				
				if(d.get("TYPE").toString().equals("0")){
					row.createCell(0).setCellValue(" "+String.valueOf(d.get("COMPANY_NAME")));
					row.createCell(1).setCellValue(" 入账");
				}else{
					row.createCell(0).setCellValue(" "+String.valueOf(d.get("DEPART_NAME")));
					row.createCell(1).setCellValue(" 使用");
				}
				row.createCell(2).setCellValue(" "+String.valueOf(d.get("MONEY")));
				row.createCell(3).setCellValue(" "+String.valueOf(d.get("CREATE_TIME")));
				row.createCell(4).setCellValue(" "+String.valueOf(d.get("NOTE")));
				row_num += 1;

			}
			response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-disposition", "attachment;filename=company_funds-"+DateUtil.getNowDate()+".xls");    
	        ServletOutputStream ouputStream = response.getOutputStream();    
	        workBook.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close(); 
	        
		} catch (Exception e) {
			log.error("查询公司资金流水异常",e);
		}
	}
	
	@RequestMapping("/give/role")
	public ListRange giveRole(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			this.companyService.saveCompanyUserRoleService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存公司角色异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存公司角色异常");
		}
		return json;
	}
	
	@RequestMapping("/save/role")
	public ListRange saveRole(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		Map<String,Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			String ID = (String)mr.pm.get("ID");
			if(!CommonUtils.checkString(ID)){
				String uuid = CommonUtils.uuid();
				mr.pm.put("ID", uuid);
				mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
				this.companyService.saveCompanyRoleService(mr.pm);
			}else{
				this.companyService.updateCompanyRoleService(mr.pm);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存公司/用户角色异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存公司/用户角色异常");
		}
		return json;
	}

	@RequestMapping("/list/role")
	public ListRange listRole(HttpServletRequest request,HttpServletResponse response, MapRange mr, String roleType, String companyId){
		Map<String,Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
			if(CommonUtils.checkString(roleType) && roleType.equals("1")){
				mr.pm.put("COMPANY_ID", companyId);	
			}
			mr.pm.put("ROLE_TYPE", roleType);
			List<Map<String, Object>> data = this.companyService.listCompanyRoleService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询公司/用户角色异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询公司/用户角色异常");
		}
		return json;
	}
	
	@RequestMapping("/edit/route/type")
	public ListRange editRouteType(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		Map<String,Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			String IS_WORLD = (String)mr.pm.get("IS_WORLD");
			String IS_COUNTRY = (String)mr.pm.get("IS_COUNTRY");
			if(!CommonUtils.checkString(IS_COUNTRY)){
				mr.pm.put("IS_COUNTRY", 0);
			}
			if(!CommonUtils.checkString(IS_WORLD)){
				mr.pm.put("IS_WORLD", 0);
			}
			this.companyService.updateCompanyRouteTypeService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("修改公司专线类型异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("修改公司专线类型异常");
		}
		return json;
	}
	
	@RequestMapping("/update/Filiale")
	public ListRange editFiliale(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		Map<String,Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			this.companyService.updateFilialeService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("修改分公司信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("修改分公司信息异常");
		}
		return json;
	}
	
	@RequestMapping("/save/web/recommend")
	public ListRange saveWebRecommend(HttpServletRequest request,HttpServletResponse response, MapRange mr, String models, String companyId, String parentId, String recommendType, String categoryId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				Map<String,Object> p = new HashMap<String,Object>();
				p.put("ROUTE_ID", jobject.getString("ID"));
				p.put("USER_ID", user.get("ID"));
				p.put("COMPANY_ID", user.get("COMPANY_ID"));
				p.put("WAP_ID", parentId);
				p.put("TYPE", recommendType);
				p.put("ID", CommonUtils.uuid());
				p.put("CATEGORY_ID", categoryId);
				p.put("END_CITY", jobject.getString("END_CITY"));
				this.companyService.saveWebRecommendService(p);
			}
		} catch (Exception e) {
			log.error("添加首页推荐异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加首页推荐异常");
		}
		return json;
	}
	
	@RequestMapping("/del/web/recommend")
	public ListRange delWebRecommend(HttpServletRequest request,HttpServletResponse response, MapRange mr, String models, String categoryId, String parentId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				Map<String,Object> p = new HashMap<String,Object>();
				p.put("ID", jobject.getString("RECOMMEND_ID"));
				p.put("CATEGORY_ID", categoryId);
				p.put("ROUTE_ID", jobject.get("ID"));
				p.put("WAP_ID", parentId);
				this.companyService.delWebRecommendService(p);
			}
		} catch (Exception e) {
			log.error("删除首页推荐异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("删除首页推荐异常");
		}
		return json;
	}
	
	@RequestMapping("/upadte/web/recommend/order")
	public ListRange upadteWebRecommendOrder(HttpServletRequest request,HttpServletResponse response, MapRange mr, String id, String orderBy, String field){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			mr.pm.put("ID", id);
			if(field.equals("ORDER_BY")){
				mr.pm.put("ORDER_BY", orderBy);
				this.companyService.updateWebRecommendOrderService(mr.pm);
			}else{
				mr.pm.put("TOP_ORDER_BY", orderBy);
				this.companyService.updateWebRecommendTopOrderByService(mr.pm);
			}
		} catch (Exception e) {
			log.error("修改首页排序异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("修改首页排序异常");
		}
		return json;
	}
	
	@RequestMapping("/list/web/recommend")
	public ListRange listWebRecommend(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, 
			String routeType, String area, String citys, String supplys, String routeThemes, String routeAttrs, String isSinglePub, 
			String routeId, String BEGIN_DATE, String END_DATE, String isRecommend, String companyId, String parentId, String recommendType,
			String categoryId, String filter){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		
		
		mr.pm.put("filter", filter);
		mr.pm.put("CATEGORY_ID", categoryId);
		mr.pm.put("IS_RECOMMEND", isRecommend);
		
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
			mr.pm.put("WAP_ID", parentId);
			List<Map<String, Object>> data = this.companyService.searchWebRecommendService(mr.pm);
			int totalSize = this.companyService.countWebRecommendService(mr.pm);
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
	
	@RequestMapping("/list/web/recommend/supply")
	public ListRange listWebRecommendSupply(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, 
			String routeType, String area, String citys, String supplys, String routeThemes, String routeAttrs, String isSinglePub, 
			String routeId, String BEGIN_DATE, String END_DATE, String isRecommend, String companyId, String parentId, String recommendType,
			String categoryId, String filter, String type, String supplyName){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		
		
		mr.pm.put("filter", filter);
		mr.pm.put("CATEGORY_ID", categoryId);
		mr.pm.put("IS_RECOMMEND", isRecommend);
		
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
			mr.pm.put("type", type);
			if(CommonUtils.checkString(supplyName)){
				supplyName = new String(supplyName.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("supplyName", supplyName);
			}
			List<Map<String, Object>> data = this.companyService.searchWebRecommendSupplyService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询首页推荐异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询首页推荐异常");
		}
		return json;
	}
	
	
	@RequestMapping("/balance/contract")
	public ListRange balanceContract(HttpServletRequest request,HttpServletResponse response, MapRange mr, String models){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		Map<String, Object> _params = new HashMap<String, Object>();
		List<Map<String, Object>> p = new LinkedList<Map<String, Object>>();
		List<Map<String, Object>> log_p = new LinkedList<Map<String, Object>>();
		ListRange json = new ListRange();
		StringBuffer sb = new StringBuffer();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			boolean b = true;
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				mr.pm.clear();
				mr.pm.put("ID", jobject.get("ID"));
				mr.pm.put("STATUS", 1);
				if(jobject.get("STATUS").toString().equals("1")){
					mr.pm.put("ORDER_ID", jobject.get("ORDER_ID"));
					mr.pm.put("CON_NO", jobject.get("NO"));
					mr.pm.put("CREATE_USER", user.get("USER_NAME"));
					mr.pm.put("CREATE_USER_ID", user.get("ID"));
					mr.pm.put("TYPE", "7");
					this.companyService.saveOrderContractLogService(mr.pm);
					
					mr.pm.clear();
					mr.pm.put("ID", jobject.get("ID"));	
					mr.pm.put("BALANCE_USER", user.get("USER_NAME"));
					mr.pm.put("BALANCE_USER_ID", user.get("ID"));
					mr.pm.put("BALANCE_COMPANY_ID", user.get("COMPANY_ID"));
					this.companyService.balanceContractAgencyService(mr.pm);
				}else{
					b = false;
				}
			}
			
			if(!b){
				json.setStatusCode("-1");//已使用状态的合同才能核销
				json.setSuccess(false);
			}else{
				json.setSuccess(true);
			}
			
		} catch (Exception e) {
			log.error("核销同异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("核销合同异常");
		}
		return json;
	}
	
	@RequestMapping("/del/contract")
	public ListRange delContract(HttpServletRequest request,HttpServletResponse response, MapRange mr, String models){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		Map<String, Object> _params = new HashMap<String, Object>();
		List<Map<String, Object>> p = new LinkedList<Map<String, Object>>();
		List<Map<String, Object>> log_p = new LinkedList<Map<String, Object>>();
		ListRange json = new ListRange();
		StringBuffer sb = new StringBuffer();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				//未使用的合同才能删除
				mr.pm.clear();
				mr.pm.put("ID", jobject.get("ID"));
				mr.pm.put("STATUS", 0);
				if(jobject.get("STATUS").toString().equals("0")){					
					mr.pm.put("ORDER_ID", "");
					mr.pm.put("CON_NO", jobject.get("NO"));
					mr.pm.put("CREATE_USER", user.get("USER_NAME"));
					mr.pm.put("CREATE_USER_ID", user.get("ID"));
					mr.pm.put("TYPE", "8");
					this.companyService.saveOrderContractLogService(mr.pm);
					
					this.companyService.delContractAgencyService(mr.pm);
					json.setSuccess(true);
				}else{
					json.setStatusCode("-1");//只有未使用、未废除的合同才能删除
					json.setSuccess(false);
				}
			}
		} catch (Exception e) {
			log.error("删除合同异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("删除合同异常");
		}
		return json;
	}
	
	@RequestMapping("/list/contract")
	public ListRange listContract(HttpServletRequest request,HttpServletResponse response,MapRange mr, String companyId, String status, String query, String startTime, String endTime, String type, String conNo, String orderNo){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String CITY_ID = (String)user.get("CITY_ID");
		ListRange json = new ListRange();
		mr.pm.clear();
		int start = toInt(request.getParameter("start"));
		int limit = toInt(request.getParameter("limit"));
		mr.pm.put("start", start+1);
		mr.pm.put("end", start+limit);
		if(CommonUtils.checkString(companyId)){
			mr.pm.put("ASSIGN_COMPANY_ID", companyId);
		}else{
			mr.pm.put("ASSIGN_COMPANY_ID", user.get("COMPANY_ID"));
		}
		if(CommonUtils.checkString(status)){
			mr.pm.put("STATUS", status);
			mr.pm.put("IS_CANCEL", 0);
		}
		mr.pm.put("NO", conNo);
		mr.pm.put("ORDER_NO", orderNo);
		mr.pm.put("START_TIME", startTime);
		mr.pm.put("END_TIME", endTime);
		mr.pm.put("TYPE", type);
		mr.pm.put("query", query);
		int totalSize = this.companyService.countOrderContractAgencyService(mr.pm);
		List<Map<String, Object>> data = this.companyService.searchOrderContractAgencyService(mr.pm);
		json.setTotalSize(totalSize);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/save/contract")
	public ListRange saveContract(HttpServletRequest request,HttpServletResponse response, MapRange mr, String no, String start, String end, String companyId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		Map<String, Object> _params = new HashMap<String, Object>();
		List<Map<String, Object>> p = new LinkedList<Map<String, Object>>();
		List<Map<String, Object>> log_p = new LinkedList<Map<String, Object>>();
		ListRange json = new ListRange();
		StringBuffer sb = new StringBuffer();
		if(CommonUtils.checkString(start)){
			try {
				
				if(!CommonUtils.checkString(end)){
					
					Map<String, Object> params = new HashMap<String, Object>();
					Map<String, Object> log_params = new HashMap<String, Object>();
//					if(start.length() == 1){
//						start = "00"+start;
//					}else if(start.length() == 2){
//						start = "0"+start;
//					}
					String _no = no+String.valueOf(start);
					sb.append("'"+_no+"',");
					
					params.put("NO", _no);
					params.put("CREATE_USER", user.get("USER_NAME"));
					params.put("CREATE_USER_ID", user.get("ID"));
					params.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
					params.put("ASSIGN_USER", user.get("USER_NAME"));
					params.put("ASSIGN_USER_ID", user.get("ID"));
					params.put("ASSIGN_COMPANY_ID", companyId);
					params.put("ORDER_CNT", 1);
					p.add(params);
					
					log_params.put("ORDER_ID", "");
					log_params.put("CON_NO", _no);
					log_params.put("CREATE_USER", user.get("USER_NAME"));
					log_params.put("CREATE_USER_ID", user.get("ID"));
					log_params.put("TYPE", "2");
					log_p.add(log_params);
					
					_params.put("NOS", sb.substring(0, sb.length()-1));
					
				}else{
					int s = Integer.parseInt(start), e = Integer.parseInt(end);
					int c = 0;
					String t = "";
					for (int i = s; i <= e; i++) {
						Map<String, Object> params = new HashMap<String, Object>();
						Map<String, Object> log_params = new HashMap<String, Object>();
						t = String.valueOf(i);
//						if(t.length() == 1){
//							t = "00"+t;
//						}else if(t.length() == 2){
//							t = "0"+t;
//						}
						String _no = no+t;
						sb.append("'"+_no+"',");
						
						params.put("NO", _no);
						params.put("CREATE_USER", user.get("USER_NAME"));
						params.put("CREATE_USER_ID", user.get("ID"));
						params.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
						params.put("ASSIGN_USER", user.get("USER_NAME"));
						params.put("ASSIGN_USER_ID", user.get("ID"));
						params.put("ASSIGN_COMPANY_ID", companyId);
						params.put("ORDER_CNT", i);
						p.add(c, params);
						
						log_params.put("ORDER_ID", "");
						log_params.put("CON_NO", _no);
						log_params.put("CREATE_USER", user.get("USER_NAME"));
						log_params.put("CREATE_USER_ID", user.get("ID"));
						log_params.put("TYPE", "2");
						log_p.add(c, log_params);
						c++;
					}
					
					_params.put("NOS", sb.substring(0, sb.length()-1));
					
					
					if(p.size()>100){
						json.setStatusCode("-1");//单次添加的编号不能超过一百条
						json.setSuccess(false);
						return json;
					}
					
				}
				_params.put("start", 1);
				_params.put("end", 1000);
				List<Map<String, Object>> data = this.companyService.searchOrderContractAgencyService(_params);
				if(CommonUtils.checkList(data) || data.size() > 0){
					//合同号已存在
					json.setStatusCode("-2");
					json.setSuccess(false);
				}else{
					mr.pm.put("CONTRACT", p);
					this.companyService.betchSaveOrderContractAgencyService(mr.pm);
					
					mr.pm.put("LOG", log_p);
					this.companyService.betchSaveOrderContractLogService(mr.pm);
					
					json.setSuccess(true);
				}
			} catch (Exception e) {
				log.error("保存合同异常",e);
				json.setSuccess(false);
				json.setStatusCode("0");
				json.setMessage("保存合同异常");
			}
		}else{
			//合同起始号不能为空
			json.setStatusCode("-3");
			json.setSuccess(false);
		}
		return json;
	}
	
	@RequestMapping("")
	public ModelAndView main(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/site/company");
	}
	
	@RequestMapping("/attachments")
	public ModelAndView attachments(HttpServletRequest request,HttpServletResponse response, String COMPANY_ID){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ID", COMPANY_ID);
		params.put("start", 1);
		params.put("end", 10);
		List<Map<String, Object>> data = companyService.listService(params);
		if(CommonUtils.checkList(data) && data.size() == 1){
			request.setAttribute("COMPANY", data.get(0));
		}
		return new ModelAndView("/company/attachments");
	}
	
	@RequestMapping("/list/sale")
	public ListRange listSale(HttpServletRequest request,HttpServletResponse response,MapRange mr, String isShare, String routeId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String CITY_ID = (String)user.get("CITY_ID");
		ListRange json = new ListRange();
		mr.pm.put("PID", (toString(request.getParameter("node")).equals("root")||toString(request.getParameter("node")).equals("nav_home"))?"-1":request.getParameter("node"));
//		mr.pm.put("CITY_ID", CITY_ID);
		mr.pm.put("IS_ALONE", (CommonUtils.checkString(isShare)&&isShare.equals("1"))?"1":null);
		mr.pm.put("AUDIT_STATUS", 1);
		mr.pm.put("start", 1);
		mr.pm.put("end", 10000);
		mr.pm.put("BUSINESS_SELLER", "YES");//只能查到授权的旅行社
		//默认0 平台管理公司 1供应商公司 2分销商公司 3门市部 4子公司 5同行 6商务中心
		//目前业务不查询门市部，直接指定的公司级别
		if(((String)mr.pm.get("PID")).equals("-1")){
			mr.pm.put("COMPANY_TYPE", "2,3,4,5,6,7");
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ROUTE_ID", routeId);
			params.put("TYPE", 1);
			List<Map<String, Object>> routeStartCitys = this.routeService.listRouteCityService(params);
			mr.pm.put("START_CITY_NAME", routeStartCitys);
			
		}
		mr.pm.put("GRANT_ROUTE_ID", routeId);
		mr.pm.put("SUPPLY_ID", user.get("COMPANY_ID"));
		List<Map<String, Object>> _data = companyService.listService(mr.pm);
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> _d : _data) {
			Map<String, Object> d = new HashMap<String, Object>();
			d.put("id", _d.get("ID"));
			d.put("parentId", _d.get("PID"));
			d.put("text", _d.get("COMPANY"));
			d.put("type", _d.get("TYPE"));
			d.put("leaf", String.valueOf(_d.get("CHILD_CNT")).equals("0")?true:false);
			//d.put("leaf", true);
			d.put("glyph", "xe608@iconfont");
			d.put("checked", CommonUtils.checkString(_d.get("GRANT_ID"))?true:false);
			d.put("expanded", true);
			data.add(d);
		}
		json.setChildren(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response, MapRange mr, String CITY_ID, String TYPE, String query){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			//站配置 公司只查询总公司级别 1：分销商，2：供应商
			if(CommonUtils.checkString(TYPE) && (TYPE.equals("1") || TYPE.equals("2"))){
//				mr.pm.put("PID", "-1");
			}
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
			/**
			 * 站子用户查询站公司信息
			 */
			
			mr.pm.put("TYPE", TYPE);
			mr.pm.put("CITY_ID", CITY_ID);
			mr.pm = PowerFactory.getPower(request, response, "", "site-company", mr.pm);
			mr.pm.remove("TYPE");
			/**
			 * 如果是站长的子用户只查询子用户自己创建的公司信息
			 */
			if(!user.get("USER_TYPE").toString().equals("01") && !user.get("USER_NAME").equals("admin")){
				//部门经理看部门员工的所有订单
				String IS_MANAGER = (String)user.get("IS_MANAGER");
				if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
//					mr.pm.put("DEPART_USER", user.get("DEPART_ID"));
				}else{
//					mr.pm.put("CREATE_USER", (String)user.get("ID"));
				}
			}
			List<Map<String, Object>> data = companyService.listService(mr.pm);
			int totalSize = companyService.countService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询公司信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询公司信息异常");
		}
		return json;
	}
	
	@RequestMapping("/save")
	public ListRange save(HttpServletRequest request, HttpServletResponse response, MapRange mr, String source){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("CREATE_ID", (String)user.get("ID"));
			mr.pm.put("CREATE_USER_NAME", (String)user.get("USER_NAME"));
			mr.pm.put("REQUEST_SOURCE", source);
			
			String LICENSE_ATTR = "";
			String LOGO_URL = "";
			String BUSINESS_URL = "";
			String WORD_URL = "";
			String COMPANY_SIGN = "";
			
			/**
			 * 上传文件类型是否正确，-4：图片类型错误,-5：未上传许可证附件，-6：未上传形象图，-7：未上传营业执照
			 */
			try{
				//许可证附件
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile LICENSE_ATTR_FILE = multipartRequest.getFile("LICENSE_ATTR");
				mr.pm.get("LICENSE_ATTR");
				if(LICENSE_ATTR_FILE != null){
					String file_name = LICENSE_ATTR_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("admin");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							LICENSE_ATTR = UploadUtil.uploadByte(LICENSE_ATTR_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}
					}
				}
				
				//形象图
				MultipartFile LOGO_URL_FILE = multipartRequest.getFile("LOGO_URL");
				if(LOGO_URL_FILE != null){
					String file_name = LOGO_URL_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("admin");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							LOGO_URL = UploadUtil.uploadByte(LOGO_URL_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}	
					}
					
				}
				
				//营业执照
				MultipartFile BUSINESS_URL_FILE = multipartRequest.getFile("BUSINESS_URL");
				if(BUSINESS_URL_FILE != null){
					String file_name = BUSINESS_URL_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("admin");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							BUSINESS_URL = UploadUtil.uploadByte(BUSINESS_URL_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}
					}
				}
				
				//模板
				MultipartFile WORD_URL_FILE = multipartRequest.getFile("WORD_TPL");
				if(WORD_URL_FILE != null){
					String file_name = WORD_URL_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						String suffix = file_name.substring(file_name.lastIndexOf(".")+1).toLowerCase();
						if (!suffix.equals("docx")){
							log.error("文件类型错误");
							json.setSuccess(false);
							json.setStatusCode("-5");
							json.setMessage("文件类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("admin");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							WORD_URL = UploadUtil.uploadByte(WORD_URL_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}
					}
				}
				
				//公章
				MultipartFile COMPANY_SIGN_FILE = multipartRequest.getFile("COMPANY_SIGN");
				if(COMPANY_SIGN_FILE != null){
					String file_name = COMPANY_SIGN_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						String suffix = file_name.substring(file_name.lastIndexOf(".")+1).toLowerCase();
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
							log.error("文件类型错误");
							json.setSuccess(false);
							json.setStatusCode("-5");
							json.setMessage("文件类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("admin");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							COMPANY_SIGN = UploadUtil.uploadByte(COMPANY_SIGN_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}
					}
				}
			}catch(Exception e){
				log.error("上传文件异常",e);
				Map<String,Object> err = new HashMap<String,Object>();
				err.put("code", "101");
				err.put("message", "上传文件异常");
			}
			
			mr.pm.put("LICENSE_ATTR", LICENSE_ATTR);
			mr.pm.put("LOGO_URL", LOGO_URL);
			mr.pm.put("BUSINESS_URL", BUSINESS_URL);
			mr.pm.put("WORD_URL", WORD_URL);
			mr.pm.put("COMPANY_SIGN", COMPANY_SIGN);
			String statusCode = "0";
			String ID = (String)mr.pm.get("ID");
			String CITY_ID = (String)mr.pm.get("CITY_ID");
			if(!CommonUtils.checkString(CITY_ID)){
				mr.pm.put("CITY_ID", (String)user.get("CITY_ID"));
			}
			
			
			String BRAND_NAME = (String)mr.pm.get("BRAND_NAME");
			
			if(CommonUtils.checkString(BRAND_NAME)){
				String BRAND_PY = PinyinHelper.convertToPinyinString(BRAND_NAME,"", PinyinFormat.WITHOUT_TONE);
				String BRAND_JP = PinyinHelper.getShortPinyin(BRAND_NAME);
				mr.pm.put("BRAND_PY", BRAND_PY);
				mr.pm.put("BRAND_JP", BRAND_JP);
			}else{
				mr.pm.put("BRAND_PY", "");
				mr.pm.put("BRAND_JP", "");
			}
			
			if(!CommonUtils.checkString(ID)){
				//后台保存创建用户状态默认都是启用,后台创建的公司默认都是审核通过
				mr.pm.put("IS_USE", "0");
				mr.pm.put("AUDIT_STATUS", "1");
				mr.pm.put("IS_SHOW", "0");
				
				String PID = (String)mr.pm.get("PID");
				if(!CommonUtils.checkString(PID)){
					mr.pm.put("PID", "-1");
				}
				ID = CommonUtils.uuid();
				mr.pm.put("ID", ID);
				statusCode = this.companyService.saveSiteManagerService(mr.pm);
				mr.pm.clear();
				mr.pm.put("COMPANY_ID", ID);
				this.companyService.saveCompanyGrantService(mr.pm);
			}else{
				statusCode = this.companyService.editUserCompanyService(mr.pm);
			}
			
			if("0".equals(statusCode)){
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
				json.setStatusCode(String.valueOf(statusCode));
			}
		} catch (Exception e) {
			log.error("添加异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加异常");
		}
		return json;
	}
	
	@RequestMapping("/enableUser")
	public ListRange enableUser(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("CREATE_USER", (String)user.get("ID"));
			mr.pm.put("ID", (String)mr.pm.get("USER_ID"));
			this.companyService.editUser(mr.pm);
		} catch (Exception e) {
			log.error("启用/禁用异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("启用/禁用异常");
		}
		return json;
	}
	
	@RequestMapping("/ownerSites")
	public ListRange ownerSites(HttpServletRequest request,HttpServletResponse response, MapRange mr, String USER_ID){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String USER_NAME = (String)user.get("USER_NAME");
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			
			/**
			 * 如果未传参数USER_ID,根据当前用户ID查询
			 */
			if(!CommonUtils.checkString(USER_ID) && !USER_NAME.equals("admin")){
				USER_ID = (String)user.get("ID");
			}
			mr.pm.put("USER_ID", USER_ID);
			
			if("0101".equals((String)user.get("USER_TYPE"))){
				/**
				 * 如果是管理员子账户，那应该继承管理员管理的城市
				 */
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("ID", user.get("COMPANY_ID"));
				params.put("start", 1);
				params.put("end", 1);
				List<Map<String, Object>> company = this.companyService.listCompanyOfUserService(params);
				if(CommonUtils.checkList(company)){
					mr.pm.put("USER_ID", company.get(0).get("USER_ID"));
				}
				
			}
			
			data = this.companyService.listSiteManagerService(mr.pm);
			
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询站异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询站异常");
		}
		return json;
	}
	
	@RequestMapping("/siteOfUsers")
	public ListRange siteOfUsers(HttpServletRequest request,HttpServletResponse response, MapRange mr, String CITY_ID, String TYPE){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String USER_NAME = (String)user.get("USER_NAME");
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			if(!"admin".equals(USER_NAME) && (!CommonUtils.checkString(CITY_ID) || !CommonUtils.checkString(TYPE))){
				json.setData(data);
				json.setSuccess(true);
				return json;
			}
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("SITE_ID", CITY_ID);
			params.put("TYPE", TYPE);
			
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询站异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询站异常");
		}
		return json;
	}
	
	@RequestMapping("/audiCompany")
	public ListRange audiCompany(HttpServletRequest request, HttpServletResponse response, MapRange mr, String models, String AUDIT_STATUS){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				//前台申请的公司默认是禁用和待审核状态，审核通过后未启用和审核通过状态
				mr.pm.put("IS_USE", 0);
				mr.pm.put("AUDIT_STATUS", AUDIT_STATUS);
				mr.pm.put("AUDIT_TIME", DateUtil.getNowDateTime());
				mr.pm.put("AUDIT_USER", (String)user.get("USER_NAME"));
				mr.pm.put("AUDIT_USER_ID", (String)user.get("ID"));
				mr.pm.put("ID", jobject.get("ID"));
				mr.pm.put("USER_ID", jobject.get("USER_ID"));
				this.companyService.audiCompany(mr.pm);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("审核异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
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
				Map<String,Object> tp = new HashMap<String,Object>();
				tp.put("ID", jobject.getString("USER_ID"));
				tp.put("IS_USE", isUse);
				tp.put("UPDATE_USER", (String)user.get("ID"));
				this.companyService.editUser(tp);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("启用/禁用异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/listCompanyChild")
	public ListRange listCompanyChild(HttpServletRequest request,HttpServletResponse response, MapRange mr, String query){
		ListRange json = new ListRange();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			//得到当前用户名和ID
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String COMPANY_ID = (String)user.get("COMPANY_ID");
			//根据当前登录用户公司ID查询子公司信息
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			if(!user.get("USER_NAME").toString().equals("admin")){
				mr.pm.put("PARENT_ID", COMPANY_ID);
			}
			
			//------------------------------------------------------------------------------
			mr.pm.put("USER_ID", (String)user.get("ID"));
			params.put("HAS_CITY", "TRUE");
			params.put("T_USER_ID", (String)user.get("ID"));
			params.put("T_COMPANY_ID", (String)user.get("COMPANY_ID"));
			mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(params));
			mr.pm = PowerFactory.getPower(request, response, "", "site-company", mr.pm);
			mr.pm.remove("MANAGER_SITE");
			mr.pm.remove("USER_ID");
			mr.pm.remove("ROLE_ID");
			
			if(CommonUtils.checkString(mr.pm.get("CITY_IDS"))){
				mr.pm.put("COMPANY_MANAGER", "YES");
				mr.pm.put("T_PARENT_ID", COMPANY_ID);
				mr.pm.remove("PARENT_ID");
			}
			//------------------------------------------------------------------------------	
			
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
			
			List<Map<String, Object>> data = companyService.listCompanyService(mr.pm);
			int totalSize = companyService.countCompanyService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询子公司信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询子公司信息异常");
		}
		return json;
	}
	
	
	@RequestMapping("/detailCompany")
	public Map<String, Object> detailCompany(HttpServletRequest request,HttpServletResponse response, MapRange mr, String COMPANY_ID){
		Map<String, Object> d = new HashMap<String, Object>();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			if(!CommonUtils.checkString(COMPANY_ID)){
				COMPANY_ID = (String)user.get("COMPANY_ID");
			}
			mr.pm.put("ID", COMPANY_ID);
			mr.pm.put("start", 1);
			mr.pm.put("end", 1);
			List<Map<String, Object>> data = companyService.listService(mr.pm);
			if(CommonUtils.checkList(data) && data.size() == 1){
				d = data.get(0);
			}
		} catch (Exception e) {
			log.error("查询公司详情异常",e);
		}
		return d;
	}
	
	/**
	 * 推荐供应商公司
	 * @param request
	 * @param response
	 * @param mr
	 * @param type
	 * @param USER_ID
	 * @return
	 */
	@RequestMapping("/recommend")
	public ListRange recommend(HttpServletRequest request, HttpServletResponse response, MapRange mr, String models){
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				String IS_SHOW = jobject.getString("IS_SHOW");
				if(CommonUtils.checkString(IS_SHOW)){
					if(IS_SHOW.equals("1")){
						IS_SHOW = "0";
					}else{
						IS_SHOW = "1";
					}
				}else{
					IS_SHOW = "0";
				}
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ID", jobject.getString("ID"));
				params.put("IS_SHOW", IS_SHOW);
				this.companyService.editCompanyService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("推荐异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	/**
	 * 推荐供应商公司
	 * @param request
	 * @param response
	 * @param mr
	 * @param type
	 * @param USER_ID
	 * @return
	 */
	@RequestMapping("/contract/must")
	public ListRange contractMust(HttpServletRequest request, HttpServletResponse response, MapRange mr, String models){
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				String IS_CONTRACT_MUST = jobject.getString("IS_CONTRACT_MUST");
				if(CommonUtils.checkString(IS_CONTRACT_MUST)){
					if(IS_CONTRACT_MUST.equals("1")){
						IS_CONTRACT_MUST = "0";
					}else{
						IS_CONTRACT_MUST = "1";
					}
				}else{
					IS_CONTRACT_MUST = "0";
				}
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ID", jobject.getString("ID"));
				params.put("IS_CONTRACT_MUST", IS_CONTRACT_MUST);
				this.companyService.editCompanyService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("合同必选异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/companyOrderBy")
	public ListRange companyOrderBy(HttpServletRequest request, HttpServletResponse response, MapRange mr, String companyId, String orderBy){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		mr.pm.put("ID", companyId);
		mr.pm.put("ORDER_BY", orderBy);
		try {
			this.companyService.editUserCompanyService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("供应商排序异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("供应商排序商异常");
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 授权交通/线路
	 * @param request
	 * @param response
	 * @param CITY_ID
	 * @param USE_STATUS
	 * @return
	 */
	@RequestMapping("/saleTraffic")
	public ListRange saleTraffic(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String TYPE = (String)mr.pm.get("TYPE");
			if(TYPE.equals("1")){
				//交通
				mr.pm.put("SALE_TRAFFIC", mr.pm.get("SALE"));
				mr.pm.put("TRAFFIC_EXPENSE", mr.pm.get("EXPENSE"));
			}else if(TYPE.equals("2")){
				//线路
				mr.pm.put("SALE_ROUTE", mr.pm.get("SALE"));
				mr.pm.put("ROUTE_EXPENSE", mr.pm.get("EXPENSE"));
			}else if(TYPE.equals("3")){
				mr.pm.put("SHARE_ROUTE", mr.pm.get("SALE"));
			}
			mr.pm.remove("TYPE");
			this.companyService.editCompanyService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("启用/禁用异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/setAlone")
	public ListRange setAlone(HttpServletRequest request,HttpServletResponse response,String models){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				String IS_ALONE = jobject.getString("IS_ALONE");
				if(IS_ALONE.equals("1")){
					IS_ALONE = "0";
				}else{
					IS_ALONE = "1";
				}
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ID", jobject.getString("ID"));
				params.put("IS_ALONE", IS_ALONE);
				this.companyService.editCompanyService(params);
				params.clear();
				params.put("PID", jobject.getString("ID"));
				params.put("IS_ALONE", IS_ALONE);
				this.companyService.updateChildCompanyAloneService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("启用/禁用异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	/**
	 * 保存公司银行信息
	 */
	@RequestMapping("/bank/save")
	public ListRange saveCompanyBank(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			String ID = (String)mr.pm.get("ID");
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("COMPANY_ID", mr.pm.get("COMPANY_ID"));
			List<Map<String, Object>> siteInfos = this.companyService.listCompanySiteService(p);
			String SITE_ID = (String)siteInfos.get(0).get("ID");
			if(CommonUtils.checkString(SITE_ID)){
				mr.pm.put("SITE_RELA_ID", SITE_ID);
				if(CommonUtils.checkString(ID)){
					this.orderService.updateOrderBankService(mr.pm);
				}else{
					mr.pm.put("ID", CommonUtils.uuid());
					this.orderService.saveOrderBankService(mr.pm);
				}
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
				json.setStatusCode("-1");
			}
			
		} catch (Exception e) {
			log.error("保存公司银行信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/combo")
	public ListRange companyCombo(HttpServletRequest request,HttpServletResponse response, MapRange mr, String type, String query){
		ListRange json = new ListRange();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			//得到当前用户名和ID
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String COMPANY_ID = (String)user.get("COMPANY_ID");
			//根据当前登录用户公司ID查询子公司信息
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			if(!user.get("USER_NAME").toString().equals("admin")){
				mr.pm.put("PARENT_ID", COMPANY_ID);
			}
			
			//------------------------------------------------------------------------------
			mr.pm.put("USER_ID", (String)user.get("ID"));
			params.put("HAS_CITY", "TRUE");
			params.put("T_USER_ID", (String)user.get("ID"));
			params.put("T_COMPANY_ID", (String)user.get("COMPANY_ID"));
			mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(params));
			mr.pm = PowerFactory.getPower(request, response, "", "site-company", mr.pm);
			mr.pm.remove("MANAGER_SITE");
			mr.pm.remove("USER_ID");
			mr.pm.remove("ROLE_ID");
			
			if(CommonUtils.checkString(mr.pm.get("CITY_IDS"))){
				mr.pm.put("COMPANY_MANAGER", "YES");
				mr.pm.put("T_PARENT_ID", COMPANY_ID);
				mr.pm.remove("PARENT_ID");
			}
			
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
			mr.pm.put("TYPES", type);//--------供应商公司类型是1
			List<Map<String, Object>> data = companyService.listCompanyService(mr.pm);
			int totalSize = companyService.countCompanyService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询子公司信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询子公司信息异常");
		}
		return json;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param mr
	 * @param beginTime
	 * @param endTime
	 * @param orderType 
	 * @param companyType 1:供应商,2:旅行社
	 * @return
	 */
	@RequestMapping("/order/supply")
	public ListRange orderSupply(HttpServletRequest request,HttpServletResponse response, MapRange mr, String beginTime, String endTime, String orderType, String companyType, String query, String supplyId, String timeType){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.clear();
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			if(!CommonUtils.checkString(beginTime) || !CommonUtils.checkString(endTime)){
				json.setData(data);
				json.setSuccess(true);
				return json;
			}
			if(CommonUtils.checkString(timeType) && timeType.equals("1")){
				mr.pm.put("CREATE_BEGIN_TIME", beginTime);
				mr.pm.put("CREATE_END_TIME", endTime);
			}else{
				mr.pm.put("START_BEGIN_DATE", beginTime);
				mr.pm.put("START_END_DATE", endTime);
			}
			
			mr.pm.put("ACCOUNT_STATUS", orderType);
			mr.pm.put("COMPANY_TYPE", companyType);
			if(!CommonUtils.checkString(companyType)){
				mr.pm.put("COMPANY_TYPE", 1);	
			}
			mr.pm.put("query", query);
			mr.pm.put("supplyId", supplyId);
			mr.pm.put("COMPANY_ACCOUNT_WAY", (String)user.get("ACCOUNT_WAY"));
			String cType = (String)user.get("COMPANY_TYPE");
			String pid = (String)user.get("PID");
			if(cType.equals("0")){
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("T_USER_ID", user.get("ID"));
				params.put("T_COMPANY_ID", user.get("COMPANY_ID"));
				params.put("HAS_CITY", "YES");
				List<Map<String, Object>> siteManagers = this.companyService.listSiteManagerService(params);
				StringBuffer siteIds = new StringBuffer();
				for (Map<String, Object> siteManager : siteManagers) {
					String id = (String)siteManager.get("ID");
					siteIds.append("'"+id+"',");
				}
				mr.pm.put("SITE_RELA_ID", siteIds.substring(0, siteIds.toString().length()-1));
				mr.pm.put("calc_role", "manager");
				
			}else if(pid.equals("-1")){
				mr.pm.put("PARENT_COMPANY_ID", user.get("COMPANY_ID"));
				mr.pm.put("calc_role", "parent");
			}else{
				mr.pm.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
				mr.pm.put("calc_role", "indie");
			}
			
			data = this.companyService.listOrderCompanyService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询订单公司信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询订单公司信息异常");
		}
		return json;
	}
	
	@RequestMapping("/account/supply")
	public ListRange accountSupply(HttpServletRequest request,HttpServletResponse response, MapRange mr, String beginTime, String endTime){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.clear();
			mr.pm.put("CREATE_BEGIN_TIME", beginTime);
			mr.pm.put("CREATE_END_TIME", endTime);
			mr.pm.put("ACCOUNT_COMPANY_ID", user.get("COMPANY_ID"));
			List<Map<String, Object>> data = this.companyService.listAccountCompanyService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询对账公司信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询对账公司信息异常");
		}
		return json;
	}
	
	@RequestMapping("/parent/alone")
	public ListRange parentAloneCompany(HttpServletRequest request,HttpServletResponse response, MapRange mr, String beginTime, String endTime, String status, String query){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.clear();
			mr.pm.put("USER_ID", (String)user.get("ID"));
			
			mr.pm.put("CREATE_BEGIN_TIME", beginTime);
			mr.pm.put("CREATE_END_TIME", endTime);	
			
			mr.pm.put("COMPANY_PAY_STATUS", status);
			if(!CommonUtils.checkString(status)){
				mr.pm.put("COMPANY_PAY_STATUS", 0);
			}
			
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
			
			String  COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
			String  PARENT_COMPANY_ID = (String)user.get("PID");
			if(COMPANY_TYPE.equals("0")){
				//平台
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("T_USER_ID", user.get("ID"));
				params.put("T_COMPANY_ID", user.get("COMPANY_ID"));
				params.put("HAS_CITY", "YES");
				List<Map<String, Object>> siteManagers = this.companyService.listSiteManagerService(params);
				StringBuffer siteIds = new StringBuffer();
				for (Map<String, Object> siteManager : siteManagers) {
					String id = (String)siteManager.get("ID");
					siteIds.append("'"+id+"',");
				}
				mr.pm.put("SITE_RELA_ID", siteIds.substring(0, siteIds.toString().length()-1));
				mr.pm.put("calc_role", "manager");
				mr.pm.put("MANAGER_COMPANY_ID", (String)user.get("COMPANY_ID"));
			}else if(PARENT_COMPANY_ID.equals("-1")){
				mr.pm.put("PARENT_CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
				mr.pm.put("calc_role", "parent");
			}else{
				mr.pm.put("CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
				mr.pm.put("calc_role", "indie");
			}
			List<Map<String, Object>> data = this.financeService.listParentUnaccountCompanyService(mr.pm);
			int totalSize = this.financeService.countParentUnaccountCompanyService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询订单公司信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询订单公司信息异常");
		}
		return json;
	}
	
	@RequestMapping("/alone")
	public ListRange aloneCompany(HttpServletRequest request,HttpServletResponse response, MapRange mr, String beginTime, String endTime, String status, String query){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.clear();
			mr.pm.put("USER_ID", (String)user.get("ID"));
			
			mr.pm.put("CREATE_BEGIN_TIME", beginTime);
			mr.pm.put("CREATE_END_TIME", endTime);	
			
			mr.pm.put("COMPANY_PAY_STATUS", status);
			if(!CommonUtils.checkString(status)){
				mr.pm.put("COMPANY_PAY_STATUS", 0);
			}
			
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
			
			String  COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
			String  PARENT_COMPANY_ID = (String)user.get("PID");
			if(COMPANY_TYPE.equals("0")){
				//平台
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("T_USER_ID", user.get("ID"));
				params.put("T_COMPANY_ID", user.get("COMPANY_ID"));
				params.put("HAS_CITY", "YES");
				List<Map<String, Object>> siteManagers = this.companyService.listSiteManagerService(params);
				StringBuffer siteIds = new StringBuffer();
				for (Map<String, Object> siteManager : siteManagers) {
					String id = (String)siteManager.get("ID");
					siteIds.append("'"+id+"',");
				}
				mr.pm.put("SITE_RELA_ID", siteIds.substring(0, siteIds.toString().length()-1));
				mr.pm.put("calc_role", "manager");
				mr.pm.put("MANAGER_COMPANY_ID", (String)user.get("COMPANY_ID"));
			}else if(PARENT_COMPANY_ID.equals("-1")){
				mr.pm.put("PARENT_CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
				mr.pm.put("calc_role", "parent");
			}else{
				mr.pm.put("CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
				mr.pm.put("calc_role", "indie");
			}
			List<Map<String, Object>> data = this.financeService.listUnaccountCompanyService(mr.pm);
			int totalSize = this.financeService.countUnaccountCompanyService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询订单公司信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询订单公司信息异常");
		}
		return json;
	}
	
	@RequestMapping("/save/wap")
	public ListRange saveWapSetting(HttpServletRequest request, HttpServletResponse response, MapRange mr, String ABOUT){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			
			String LOGO = "";
			String CODE = "";
			String ICON = "";
			String WX_IMG = "";
			try{
				//二维码
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile CODE_FILE = multipartRequest.getFile("CODE");
				if(CODE_FILE != null){
					String file_name = CODE_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("wap");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							CODE = UploadUtil.uploadByte(CODE_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}
					}
				}
				
				//logo
				MultipartFile LOGO_FILE = multipartRequest.getFile("LOGO");
				if(LOGO_FILE != null){
					String file_name = LOGO_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("wap");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							LOGO = UploadUtil.uploadByte(LOGO_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}	
					}
					
				}
				
				//logo
				MultipartFile ICON_FILE = multipartRequest.getFile("ICON");
				if(ICON_FILE != null){
					String file_name = ICON_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png,ico")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("wap");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							ICON = UploadUtil.uploadByte(ICON_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}	
					}
					
				}
				
				//logo
				MultipartFile WX_IMG_FILE = multipartRequest.getFile("WX_IMG");
				if(WX_IMG_FILE != null){
					String file_name = WX_IMG_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png,ico")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("wap");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							WX_IMG = UploadUtil.uploadByte(WX_IMG_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}	
					}
					
				}
				
			}catch(Exception e){
				log.error("上传文件异常",e);
				Map<String,Object> err = new HashMap<String,Object>();
				err.put("code", "101");
				err.put("message", "上传文件异常");
			}
			String COMPANY_ID = (String)mr.pm.get("COMPANY_ID");
			if(!CommonUtils.checkString(COMPANY_ID)){
				mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
			}
			mr.pm.put("CODE", CODE);
			mr.pm.put("LOGO", LOGO);
			mr.pm.put("ICON", ICON);
			mr.pm.put("WX_IMG", WX_IMG);
			mr.pm.put("ABOUT", ABOUT);
			String statusCode = "0";
			String ID = (String)mr.pm.get("ID");
			if(!CommonUtils.checkString(ID)){
				mr.pm.put("ID", CommonUtils.uuid());
				statusCode = this.companyService.saveWapSettingService(mr.pm);
			}else{
				statusCode = this.companyService.updateWapSettingService(mr.pm);
			}
			if("0".equals(statusCode)){
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
				json.setStatusCode(String.valueOf(statusCode));
			}
		} catch (Exception e) {
			log.error("保存网站设置异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存网站设置异常");
		}
		return json;
	}
	
	@RequestMapping("/detail/wap")
	public ListRange listWapSetting(HttpServletRequest request,HttpServletResponse response, MapRange mr, String companyId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.clear();
			mr.pm.put("COMPANY_ID", companyId);
			if(!CommonUtils.checkString(companyId)){
				mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
			}
			mr.pm.put("TYPE", 0);
			mr.pm.put("start", 1);
			mr.pm.put("end", 10000);
			List<WapSettingEntity> data = this.companyService.listWapSettingService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询订单网站设置信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询订单网站设置信息异常");
		}
		return json;
	}
	
	
	
	@RequestMapping("/upload/ad")
	public void uploadAd(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		Map<String,Object> rs = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		try{
			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("Filedata");
			String fileName = request.getParameter("Filename");
			
			String TITLE = request.getParameter("title");
			if(CommonUtils.checkString(TITLE)){
				TITLE = URLDecoder.decode(TITLE, "UTF-8");
			}
			
			String HREF = request.getParameter("href");
			if(CommonUtils.checkString(HREF)){
				HREF = URLDecoder.decode(HREF, "UTF-8");
			}
			
			String type = request.getParameter("type");
			String wapId = request.getParameter("wapId");
			String companyId = request.getParameter("companyId");
			if(!CommonUtils.checkString(companyId)){
				companyId = (String)user.get("COMPANY_ID");
						
			}
			if(!fileName.equals("")){
			}else if(file!=null){
				fileName = file.getName();
			}else{
				fileName = "file_"+this.getUuid();
			}
			
			if (FileUtil.checkFilesuffix(fileName,"jpg,gif,png")){
				log.error("图片类型错误");
				json.setSuccess(false);
				json.setStatusCode("-4");
				json.setMessage("图片类型错误");
			}else{
				String[] paths = UploadUtil.pathAdmin("wap/ad");
				String file_suffix = fileName.substring(fileName.indexOf("."), fileName.length());
				fileName = CommonUtils.uuid()+file_suffix;
				String visitUrl = UploadUtil.uploadByte(file.getBytes(), paths[0], paths[1], fileName).replace("\\", "/");
				rs.put("result", "success");
				params.clear();
				params.put("ID", CommonUtils.uuid());
				params.put("URL", visitUrl);
				params.put("HREF", HREF);
				params.put("TYPE", type);
				params.put("WAP_ID", wapId);
				params.put("COMPANY_ID", companyId);
				params.put("TITLE", TITLE);
				this.companyService.saveWapAdAttrService(params);
				json.setSuccess(true);
			}
		}catch(Exception e){
			log.error("上传广告图异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("上传广告图异常");
		}
		
		//将实体对象转换为JSON Object转换  
	    JSONObject responseJSONObject = JSONObject.fromObject(json);  
	    response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    PrintWriter out = null;  
	    try {  
	        out = response.getWriter();  
	        out.append(responseJSONObject.toString());  
	        log.debug("返回是\n");  
	        log.debug(responseJSONObject.toString());  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (out != null) {  
	            out.close();  
	        }  
	    } 
	}
	
	@RequestMapping("/del/ad")
	public ListRange delAd(HttpServletRequest request,HttpServletResponse response, MapRange mr, String models){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				String id = jobject.getString("ID");
				mr.pm.clear();
				mr.pm.put("ID", id);
				this.companyService.delWapAdAttrService(mr.pm);
			}
			
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询订单网站设置信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询订单网站设置信息异常");
		}
		return json;
	}
	
	@RequestMapping("/list/ad")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String companyId, String type, String id, String wapId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("TYPE", type);
			mr.pm.put("ID", id);
			mr.pm.put("WAP_ID", wapId);
			mr.pm.put("COMPANY_ID", companyId);
			if(!CommonUtils.checkString(companyId)){
				mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
			}
			List<Map<String, Object>> data = this.companyService.listWapAdAttrService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询广告图异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询广告图异常");
		}
		return json;
	}
	
	@RequestMapping("/set/point")
	public ListRange setPoint(HttpServletRequest request, HttpServletResponse response, MapRange mr, String id, String longitude, String latitude){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("ID", id);
			mr.pm.put("LONGITUDE", longitude);
			mr.pm.put("LATITUDE", latitude);
			this.companyService.setPointService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("设置坐标异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("设置坐标异常");
		}
		return json;
	}
	
	@RequestMapping("/save/business/seller")
	public ListRange saveBusinessSeller(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			this.companyService.delBusinessSellerService(mr.pm);
			String supplys = (String)mr.pm.get("SUPPLYS");
			if(CommonUtils.checkString(supplys)){
				this.companyService.saveBusinessSellerService(mr.pm);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存合作商户异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存合作商户异常");
		}
		return json;
	}
	
	@RequestMapping("/list/business/seller")
	public ListRange listBusinessSeller(HttpServletRequest request, HttpServletResponse response, MapRange mr, String companyId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("AGENCY_ID", companyId);
			List<Map<String, Object>> data = this.companyService.listBusinessSellerService(mr.pm);
			List<String> IDS = new ArrayList<String>();
			for (Map<String, Object> d : data) {
				IDS.add((String)d.get("SUPPLY_ID"));
			}
			json.setData(IDS);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询合作商户异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询合作商户异常");
		}
		return json;
	}
}
