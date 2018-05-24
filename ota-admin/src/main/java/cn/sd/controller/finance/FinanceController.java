package cn.sd.controller.finance;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.sd.core.config.ConfigLoader;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.FileUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.UploadUtil;
import cn.sd.core.web.BaseController;
import cn.sd.rmi.ServiceProxyFactory;
import cn.sd.service.finance.IFinanceService;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/finance")
public class FinanceController extends BaseController{

	private static Log log = LogFactory.getLog(FinanceController.class);
	
	@Resource
	private IFinanceService financeService;
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("/calc")
	public ModelAndView calc(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("finance/calc");
	}
	
	@RequestMapping("/pay")
	public ModelAndView pay(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("finance/pay");
	}
	
	@RequestMapping("/query")
	public ModelAndView query(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("finance/query");
	}
	
	@RequestMapping("/alone")
	public ModelAndView alone(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("finance/alone");
	}
	
	@RequestMapping("/calc/syn")
	public ListRange synCalc(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime, String supplyId){
		ListRange json = new ListRange();
		if(CommonUtils.checkString(supplyId)){
			Object obj = ServiceProxyFactory.getProxy().getLock();
			synchronized(obj){
				try {
					Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
					if(CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime) && CommonUtils.checkString(supplyId)){
						mr.pm.clear();
						mr.pm.put("USER_ID", (String)user.get("ID"));
						mr.pm.put("COMPANY_TYPE", (String)user.get("COMPANY_TYPE"));
						mr.pm.put("ACCOUNT_COMPANY_ID", (String)user.get("COMPANY_ID"));
						this.financeService.delOrderAccountSynByUserIdService(mr.pm);
						mr.pm.clear();
						mr.pm.put("CREATE_BEGIN_TIME", beginTime);
						mr.pm.put("CREATE_END_TIME", endTime);
						mr.pm.put("COMPANY_ID", supplyId);
						mr.pm.put("COMPANY_TYPE", (String)user.get("COMPANY_TYPE"));
						mr.pm.put("ACCOUNT_COMPANY_ID", (String)user.get("COMPANY_ID"));
						List<Map<String, Object>> data = this.financeService.listOrderAccountSynService(mr.pm);
						if(CommonUtils.checkList(data)){
							json.setSuccess(false);
							json.setStatusCode("-2");
							String message = "";
							if(CommonUtils.checkString(data.get(0).get("DEPART_NAME"))){
								message = data.get(0).get("DEPART_NAME")+"-";
							}
							message = message +data.get(0).get("USER_NAME");
							json.setMessage(message);
						}else{
							json.setSuccess(true);
							/**
							 * 保存数据
							 */
							mr.pm.put("ID", CommonUtils.uuid());
							mr.pm.put("USER_ID", (String)user.get("ID"));
							mr.pm.put("USER_NAME", (String)user.get("USER_NAME"));
							mr.pm.put("DEPART_NAME", (String)user.get("DEPART_NAME"));
							this.financeService.saveOrderAccountSynService(mr.pm);
						}
					}else{
						json.setSuccess(false);	
						json.setStatusCode("-1");
					}
					
				} catch (Exception e) {
					log.error("对账异常",e);
					json.setSuccess(false);
					json.setStatusCode("0");
					json.setMessage("对账异常");
				}
			}
		}else{
			json.setSuccess(true);
		}
		return json;
	}
	
	@RequestMapping("/calc/list")
	public ListRange listCalc(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime, String supplyId, String agencyId, String orderType, String accountNo, String no, String timeType){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
		try {
			if((CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime)) || CommonUtils.checkString(accountNo)){
				mr.pm.put("ACCOUNT_DETAIL_NO", accountNo);
				mr.pm.put("COMPANY_ID", supplyId);
				mr.pm.put("NO", no);
				if(CommonUtils.checkString(timeType) && timeType.equals("1")){
					mr.pm.put("CREATE_BEGIN_TIME", beginTime);
					mr.pm.put("CREATE_END_TIME", endTime);
				}else{
					mr.pm.put("START_BEGIN_DATE", beginTime);
					mr.pm.put("START_END_DATE", endTime);
				}
				mr.pm.put("COMPANY_ACCOUNT_WAY", (String)user.get("ACCOUNT_WAY"));
				/**
				 * 
				 * 总公司
				 *     选择公司		查看该公司的订单
				 *     不选择公司	查看总公司和分公司/门市订单
				 * 分公司
				 *     只能查看自己公司的订单
				 * 站长
				 * 	   选择公司		查看该公司的订单
				 *     不选择公司	查看站下面的所有订单
				 *     
				 */
				String PARENT_COMPANY_ID = (String)user.get("PID");
				if(COMPANY_TYPE.equals("0")){
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
					mr.pm.put("SITE_RELA_IDS", siteIds.substring(0, siteIds.toString().length()-1));
					mr.pm.put("CREATE_COMPANY_ID", agencyId);
					mr.pm.put("calc_role", "manager");
				}else if(PARENT_COMPANY_ID.equals("-1")){
					if(CommonUtils.checkString(agencyId)){
						mr.pm.put("CREATE_COMPANY_ID", agencyId);
					}
					mr.pm.put("PARENT_CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
					mr.pm.put("calc_role", "parent");
				}else{
					mr.pm.put("CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
					mr.pm.put("calc_role", "indie");
				}
				
				if(CommonUtils.checkString(orderType)){
					mr.pm.put("ACCOUNT_STATUS", orderType.split(","));
				}
				
				List<Map<String, Object>> data = this.financeService.listCalcService(mr.pm);
				json.setData(data);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询对账信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询对账信息异常");
		}
		return json;
	}
	
	@RequestMapping("/calc/export")
	public void calcExport(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime, String supplyId, String agencyId, String orderType, String timeType){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
		try {
			if(CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime)){
				mr.pm.put("COMPANY_ID", supplyId);
				
				if(CommonUtils.checkString(timeType) && timeType.equals("1")){
					mr.pm.put("CREATE_BEGIN_TIME", beginTime);
					mr.pm.put("CREATE_END_TIME", endTime);
				}else{
					mr.pm.put("START_BEGIN_DATE", beginTime);
					mr.pm.put("START_END_DATE", endTime);
				}
				mr.pm.put("COMPANY_ACCOUNT_WAY", (String)user.get("ACCOUNT_WAY"));
				/**
				 * 
				 * 总公司
				 *     选择公司		查看该公司的订单
				 *     不选择公司	查看总公司和分公司/门市订单
				 * 分公司
				 *     只能查看自己公司的订单
				 * 站长
				 * 	   选择公司		查看该公司的订单
				 *     不选择公司	查看站下面的所有订单
				 *     
				 */
				String PARENT_COMPANY_ID = (String)user.get("PID");
				if(COMPANY_TYPE.equals("0")){
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
					mr.pm.put("SITE_RELA_IDS", siteIds.substring(0, siteIds.toString().length()-1));
					mr.pm.put("CREATE_COMPANY_ID", agencyId);
					mr.pm.put("calc_role", "manager");
				}else if(COMPANY_TYPE.equals("1")){
					
				}else if(PARENT_COMPANY_ID.equals("-1")){
					if(CommonUtils.checkString(agencyId)){
						mr.pm.put("CREATE_COMPANY_ID", agencyId);
					}
					mr.pm.put("PARENT_CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
					mr.pm.put("calc_role", "parent");
				}else{
					mr.pm.put("CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
					mr.pm.put("calc_role", "indie");
				}
				
				if(CommonUtils.checkString(orderType)){
					mr.pm.put("ACCOUNT_STATUS", orderType.split(","));
				}
				
				List<Map<String, Object>> data = this.financeService.listCalcService(mr.pm);
				
				String filePath = ConfigLoader.config.getStringConfigDetail("system.file.path");
				
				File file = new File(filePath+"/calc_account.xls");///-----------------------------------读取文件的地址
				FileInputStream fis = new FileInputStream(file);
				HSSFWorkbook workBook= new HSSFWorkbook(fis);
				int row_num = 1;
				HSSFSheet hssfSheet = workBook.getSheet("Sheet1");
				for (Map<String, Object> d : data) {
					HSSFRow row = hssfSheet.createRow(row_num);
					row.createCell(0).setCellValue(" "+String.valueOf(d.get("BUY_COMPANY")));
					row.createCell(1).setCellValue(" "+String.valueOf(d.get("BUY_CHINA_NAME")));
					row.createCell(2).setCellValue(" "+String.valueOf(d.get("START_DATE")));
					row.createCell(3).setCellValue(" "+String.valueOf(d.get("PRODUCE_NAME")));
					row.createCell(4).setCellValue(" "+String.valueOf(d.get("DAY_COUNT")));
					row.createCell(5).setCellValue(" "+String.valueOf(d.get("P_TYPE")));
					row.createCell(6).setCellValue(" "+String.valueOf(d.get("SALE_COMPANY")));
					row.createCell(7).setCellValue(" "+String.valueOf(d.get("PERSON_COUNT")));
					row.createCell(8).setCellValue(" "+String.valueOf(d.get("NO")));
					
					//旅行社外卖金额	旅行社实收金额	供应商同行金额	供应商实收金额

					row.createCell(9).setCellValue(" "+String.valueOf(d.get("SALE_AMOUNT")));
					row.createCell(10).setCellValue(" "+String.valueOf(d.get("ACTUAL_SALE_AMOUNT")));
					row.createCell(11).setCellValue(" "+String.valueOf(d.get("INTER_AMOUNT")));
					row.createCell(12).setCellValue(" "+String.valueOf(d.get("ACTUAL_AMOUNT")));
					row.createCell(13).setCellValue(" "+String.valueOf(d.get("REFUND_AMOUNT")));
					
					row_num += 1;

				}
				response.setContentType("application/vnd.ms-excel");    
		        response.setHeader("Content-disposition", "attachment;filename=pay_account-"+DateUtil.getNowDate()+".xls");    
		        ServletOutputStream ouputStream = response.getOutputStream();    
		        workBook.write(ouputStream);    
		        ouputStream.flush();    
		        ouputStream.close();   
		        
			}
		} catch (Exception e) {
			log.error("导出对账信息异常",e);
		}
	}
	
	
	@RequestMapping("/calc/supply/order/export")
	public void calcSupplyOrderExport(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime, 
			String orderNo, String calcStatus, String payWho, String groupCity){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		try {
			if(CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime)){
				mr.pm.put("CREATE_BEGIN_TIME", beginTime);
				mr.pm.put("CREATE_END_TIME", endTime);	
				mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
				
				mr.pm.put("orderNo", orderNo);
				mr.pm.put("ACCOUNT_STATUS", calcStatus);
				
				mr.pm.put("payWho", payWho);
				
				mr.pm.put("groupCity", groupCity);
				
				List<Map<String, Object>> data = this.financeService.listAccountInfoService(mr.pm);
				
				String filePath = ConfigLoader.config.getStringConfigDetail("system.file.path");
				
				File file = new File(filePath+"/calc_account.xls");///-----------------------------------读取文件的地址
				FileInputStream fis = new FileInputStream(file);
				HSSFWorkbook workBook= new HSSFWorkbook(fis);
				int row_num = 1;
				HSSFSheet hssfSheet = workBook.getSheet("Sheet1");
				for (Map<String, Object> d : data) {
					HSSFRow row = hssfSheet.createRow(row_num);
					row.createCell(0).setCellValue(" "+String.valueOf(d.get("BUY_COMPANY")));
					row.createCell(1).setCellValue(" "+String.valueOf(d.get("BUY_CHINA_NAME")));
					row.createCell(2).setCellValue(" "+String.valueOf(d.get("START_DATE")));
					row.createCell(3).setCellValue(" "+String.valueOf(d.get("PRODUCE_NAME")));
					row.createCell(4).setCellValue(" "+String.valueOf(d.get("DAY_COUNT")));
					row.createCell(5).setCellValue(" "+String.valueOf(d.get("P_TYPE")));
					row.createCell(6).setCellValue(" "+String.valueOf(d.get("SALE_COMPANY")));
					row.createCell(7).setCellValue(" "+String.valueOf(d.get("PERSON_COUNT")));
					row.createCell(8).setCellValue(" "+String.valueOf(d.get("NO")));
					
					//旅行社外卖金额	旅行社实收金额	供应商同行金额	供应商实收金额

					row.createCell(9).setCellValue(" "+String.valueOf(d.get("SALE_AMOUNT")));
					row.createCell(10).setCellValue(" "+String.valueOf(d.get("ACTUAL_SALE_AMOUNT")));
					row.createCell(11).setCellValue(" "+String.valueOf(d.get("INTER_AMOUNT")));
					row.createCell(12).setCellValue(" "+String.valueOf(d.get("ACTUAL_AMOUNT")));
					row.createCell(13).setCellValue(" "+String.valueOf(d.get("REFUND_AMOUNT")));
					
					row_num += 1;

				}
				response.setContentType("application/vnd.ms-excel");    
		        response.setHeader("Content-disposition", "attachment;filename=pay_account-"+DateUtil.getNowDate()+".xls");    
		        ServletOutputStream ouputStream = response.getOutputStream();    
		        workBook.write(ouputStream);    
		        ouputStream.flush();    
		        ouputStream.close();   
		        
			}
		} catch (Exception e) {
			log.error("导出对账信息异常",e);
		}
	}
	
	@RequestMapping("/ready/calc")
	public ListRange readyCalc(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime, String supplyId, String departIds){
		ListRange json = new ListRange();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String companyPid = (String)user.get("PID");
			if(CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime) && CommonUtils.checkString(supplyId)){
				mr.pm.put("COMPANY_ID", supplyId);
				if(CommonUtils.checkString(departIds)){
					String[] departIdArray = departIds.split(",");
					StringBuffer ids = new StringBuffer();
					for (int i = 0; i < departIdArray.length; i++) {
						ids.append("'"+departIdArray[i]+"',");
					}
					mr.pm.put("departIds", ids.substring(0, ids.length()-1).toString());
				}
				
				mr.pm.put("CREATE_BEGIN_TIME", beginTime);
				mr.pm.put("CREATE_END_TIME", endTime);
				mr.pm.put("ACCOUNT_STATUS", 0);
				String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
				
				if(COMPANY_TYPE.equals("0")){
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
				}else{
					if(companyPid.equals("-1")){
						mr.pm.put("calc_role", "parent");
						mr.pm.put("PARENT_CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
					}else{
						mr.pm.put("CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
						mr.pm.put("calc_role", "indie");
					}
				}
				
				List<Map<String, Object>> supplyCalc = this.financeService.supplyCalcService(mr.pm);
				List<Map<String, Object>> managerCalc = this.financeService.managerCalcService(mr.pm);
				List<Map<String, Object>> saleCalc = this.financeService.saleCalcService(mr.pm);
				
				List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
				if(!CommonUtils.checkList(supplyCalc) && !CommonUtils.checkList(managerCalc) && !CommonUtils.checkList(saleCalc)){
					json.setData(data);
					json.setSuccess(false);
				}else{
					result.put("retail", saleCalc);
					result.put("supply", supplyCalc);
					result.put("site", managerCalc);
					data.add(result);
					json.setData(data);
					json.setSuccess(true);
				}
				
			}
		} catch (Exception e) {
			log.error("准备对账异常",e);
			json.setSuccess(false);
			result.put("success", false);
			
		}
		return json;
	}
	
	@RequestMapping("/del/syn")
	public ListRange dekSyn(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Object obj = ServiceProxyFactory.getProxy().getLock();
			synchronized(obj){
				Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
				mr.pm.put("USER_ID", (String)user.get("ID"));
				mr.pm.put("COMPANY_TYPE", (String)user.get("COMPANY_TYPE"));
				mr.pm.put("ACCOUNT_COMPANY_ID", (String)user.get("COMPANY_ID"));
				this.financeService.delOrderAccountSynByUserIdService(mr.pm);
				json.setSuccess(true);
			}
		} catch (Exception e) {
			log.error("删除同步异常",e);
			json.setSuccess(false);
		}
		return json;
	}
	
	@RequestMapping("/confirm/calc")
	public ListRange confirmCalc(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime, String supplyId, String departIds){
		
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		
		List<Map<String, Object>> f_syn_data = null;
		List<Map<String, Object>> s_syn_data = null;
		mr.pm.clear();
		if(CommonUtils.checkString(departIds)){
			String[] departIdArray = departIds.split(",");
			StringBuffer ids = new StringBuffer();
			for (int i = 0; i < departIdArray.length; i++) {
				ids.append("'"+departIdArray[i]+"',");
			}
			mr.pm.put("departIds", ids.substring(0, ids.length()-1).toString());
		}
		mr.pm.put("COMPANY_ID", supplyId);
		mr.pm.put("ACCOUNT_COMPANY_ID", (String)user.get("COMPANY_ID"));
		mr.pm.put("COMPANY_TYPE", (String)user.get("COMPANY_TYPE"));
		mr.pm.put("CREATE_BEGIN_TIME", beginTime);
		mr.pm.put("CREATE_END_TIME", endTime);
		Object obj = ServiceProxyFactory.getProxy().getLock();
		synchronized(obj){
			f_syn_data = this.financeService.listOrderAccountSynService(mr.pm);
			if(!CommonUtils.checkList(f_syn_data)){
				
				mr.pm.put("ID", CommonUtils.uuid());
				mr.pm.put("USER_ID", (String)user.get("ID"));
				mr.pm.put("USER_NAME", (String)user.get("USER_NAME"));
				mr.pm.put("DEPART_NAME", (String)user.get("DEPART_NAME"));
				try {
					this.financeService.saveOrderAccountSynService(mr.pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		ListRange json = new ListRange();
		synchronized(obj){
			mr.pm.put("USER_ID", (String)user.get("ID"));
			s_syn_data = this.financeService.listOrderAccountSynService(mr.pm);
		}
		if(CommonUtils.checkList(s_syn_data)){
			try {
				/**
				 * 1.修改订单对账状态（对账中）
				 * 2.区间内的订单是否都已对账完成（除取消订单）
				 * 3.区间初次对账对账主表保存,否则更新。只更新-对账状态
				 * 4.保存编号表
				 * 5.保存对账子表
				 * 6.更新订单表的 对账信息（订单编号）
				 */
				String PARENT_COMPANY_ID = (String)user.get("PID");
				if(CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime) && CommonUtils.checkString(supplyId)){
					mr.pm.put("STATUS", "2,5");
//					mr.pm.put("ACCOUNT_STATUS", "0");
					mr.pm.put("ACCOUNT_STATUS", new String[]{"0"});
					String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
					if(COMPANY_TYPE.equals("0")){
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
					}else if(PARENT_COMPANY_ID.equals("-1")){
						mr.pm.put("PARENT_CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
						mr.pm.put("calc_role", "parent");
					}else{
						mr.pm.put("CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
						mr.pm.put("calc_role", "indie");
					}
					
					List<Map<String, Object>> data = this.financeService.listCalcService(mr.pm);
					mr.pm.remove("STATUS");
					StringBuffer IDS = new StringBuffer();
					String SITE_RELA_ID = "";
					for (Map<String, Object> d : data) {
						if(!CommonUtils.checkString(SITE_RELA_ID)){
							SITE_RELA_ID = (String)d.get("SITE_RELA_ID");
						}
						IDS.append("'"+(String)d.get("ID")+"',");
					}
					params.put("IDS", IDS.substring(0, IDS.length()-1));
					params.put("ACCOUNT_STATUS", 1);
					this.financeService.updateOrderAccountStatusService(params);
					mr.pm.put("SITE_RELA_ID", SITE_RELA_ID);
					mr.pm.put("CREATE_USER", user.get("USER_NAME"));
					mr.pm.put("CREATE_USER_ID", user.get("ID"));
					mr.pm.put("IDS", IDS.substring(0, IDS.length()-1));
					mr.pm.put("COMPANY_TYPE", (String)user.get("COMPANY_TYPE"));
					
					this.financeService.confirmOrderAccountService(mr.pm);
					json.setSuccess(true);
					json.setData(data);
				}else{
					json.setSuccess(false);
					json.setStatusCode("-1");
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					mr.pm.put("CREATE_BEGIN_TIME", beginTime);
					mr.pm.put("CREATE_END_TIME", endTime);
					mr.pm.put("COMPANY_ID", supplyId);
					mr.pm.put("$STATUS$", "2,5");
//					mr.pm.put("ACCOUNT_STATUS", "0");
					mr.pm.put("ACCOUNT_STATUS", new String[]{"0"});
					List<Map<String, Object>> data = this.financeService.listCalcService(mr.pm);
					mr.pm.remove("STATUS");
					StringBuffer IDS = new StringBuffer();
					for (Map<String, Object> d : data) {
						IDS.append("'"+(String)d.get("ID")+"',");
					}
					params.put("IDS", IDS.substring(0, IDS.length()-1));
					params.put("ACCOUNT_STATUS", 0);
					this.financeService.updateOrderAccountStatusService(params);
				} catch (Exception e1) {e1.printStackTrace();}
				log.error("确认对账异常",e);
				json.setSuccess(false);
				json.setStatusCode("0");
				json.setMessage("确认对账异常");
			}finally{
				mr.pm.clear();
				mr.pm.put("USER_ID", (String)user.get("ID"));
				mr.pm.put("COMPANY_TYPE", (String)user.get("COMPANY_TYPE"));
				mr.pm.put("ACCOUNT_COMPANY_ID", (String)user.get("COMPANY_ID"));
				try {
					this.financeService.delOrderAccountSynByUserIdService(mr.pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			json.setSuccess(false);
			json.setStatusCode("-2");
			String message = "";
			if(CommonUtils.checkString(f_syn_data.get(0).get("DEPART_NAME"))){
				message = f_syn_data.get(0).get("DEPART_NAME")+"-";
			}
			message = message  +f_syn_data.get(0).get("USER_NAME");
			json.setMessage(message);
		}
		return json;
	}
	
	@RequestMapping("/pay/list")
	public ListRange listAccount(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime, String supplyId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			if(CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime) && CommonUtils.checkString(supplyId)){
				mr.pm.put("CREATE_BEGIN_TIME", beginTime);
				mr.pm.put("CREATE_END_TIME", endTime);
				mr.pm.put("COMPANY_ID", supplyId);
				String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
				if(!COMPANY_TYPE.equals("0")){
					mr.pm.put("ACCOUNT_COMPANY_ID", (String)user.get("COMPANY_ID"));
				}
				List<Map<String, Object>> data = this.financeService.listOrderAccountDetailService(mr.pm);
				json.setData(data);
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("查询对账信息详情异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询对账信息详情异常");
		}
		return json;
	}
	
	@RequestMapping("/pay/supply")
	public ListRange paySupply(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			if(CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime)){
				mr.pm.put("CREATE_BEGIN_TIME", beginTime);
				mr.pm.put("CREATE_END_TIME", endTime);
				mr.pm.put("ACCOUNT_COMPANY_ID", (String)user.get("COMPANY_ID"));
				List<Map<String, Object>> data = this.financeService.listOrderAccountDetailService(mr.pm);
				json.setData(data);
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("查询对账信息详情异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询对账信息详情异常");
		}
		return json;
	}
	
	@RequestMapping("/pay/finish")
	public ListRange payFinish(HttpServletRequest request, HttpServletResponse response, MapRange mr, String models, String beginTime, String endTime, String supplyId){
		ListRange json = new ListRange();
		if(CommonUtils.checkString(models) && CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime) && CommonUtils.checkString(supplyId)){
			mr.pm.put("CREATE_BEGIN_TIME", beginTime);
			mr.pm.put("CREATE_END_TIME", endTime);
			mr.pm.put("COMPANY_ID", supplyId);
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			StringBuffer IDS = new StringBuffer();
			String ACCOUNT_ID = "";
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				IDS.append("'"+jobject.get("ID")+"',");
				ACCOUNT_ID = jobject.get("ACCOUNT_ID").toString();
			}
			
			try {
				Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
				
				mr.pm.put("PAY_USER", user.get("USER_NAME"));
				mr.pm.put("PAY_USER_ID", user.get("ID"));
				
				
				mr.pm.put("IDS", IDS.substring(0, IDS.length()-1));
				this.financeService.updateAccountDetailPayStatusService(mr.pm);
				
				mr.pm.put("ACCOUNT_ID", ACCOUNT_ID);
				
				this.financeService.updateOrderBaseAccountStatusService(mr.pm);
				
				String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
				if(COMPANY_TYPE.equals("0")){
					mr.pm.put("ACCOUNT", "0");
				}else{
					mr.pm.put("ACCOUNT", "-1");
					mr.pm.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));	
				}
				this.financeService.updateAccountStatusService(mr.pm);
				
				json.setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("对账完成异常",e);
				json.setSuccess(false);
				json.setStatusCode("0");
				json.setMessage("对账完成异常");
			}
		}else{
			json.setSuccess(false);
			json.setStatusCode("-1");
		}
		
		return json;
	}
	
	@RequestMapping("/pay/upload")
	public ListRange payUpload(HttpServletRequest request, HttpServletResponse response, MapRange mr, String detailId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			
			String ATTR_URL = "";
			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile FILE = multipartRequest.getFile("file");
			if(FILE != null && FILE.getBytes().length > 0){
				String file_name = FILE.getOriginalFilename();
				if(CommonUtils.checkString(file_name)){
					if (FileUtil.checkFilesuffix(file_name,"xls,doc,xlsx,docx")){
						log.error("文件类型错误");
						json.setSuccess(false);
						json.setStatusCode("-4");
						json.setMessage("文件类型错误");
						return json;
					}else{
						String[] paths = UploadUtil.pathAdmin("finance");
						String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
						file_name = CommonUtils.uuid()+file_suffix;
						ATTR_URL = UploadUtil.uploadByte(FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
					}
				}
				
				mr.pm.put("ATTR_USER", user.get("USER_NAME"));
				mr.pm.put("ATTR_USER_ID", user.get("ID"));
				mr.pm.put("ATTR_URL", ATTR_URL);
				
				mr.pm.put("ID", detailId);
				this.financeService.updateOrderAccountDetailAttrService(mr.pm);
				json.setSuccess(true);
				
			}else{
				json.setSuccess(false);
				json.setStatusCode("-1");
				json.setMessage("不允许上传空文件!");
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("上传凭证异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("上传凭证异常");
		}
		return json;
	}
	
	@RequestMapping("/history")
	public ListRange history(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime){
		ListRange json = new ListRange();
		try {
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			if(CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime)){
//				mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
				String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
				String PARENT_COMPANY_ID = (String)user.get("PID");
				mr.pm.put("CREATE_BEGIN_TIME", beginTime);
				mr.pm.put("CREATE_END_TIME", endTime);
				if(COMPANY_TYPE.equals("0")){
					//平台
					mr.pm.put("calc_role", "manager");
					mr.pm.put("MANAGER_COMPANY_ID", (String)user.get("COMPANY_ID"));
				}else if(PARENT_COMPANY_ID.equals("-1")){
					mr.pm.put("PARENT_CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
					mr.pm.put("calc_role", "parent");
				}else{
					mr.pm.put("CREATE_COMPANY_ID", (String)user.get("COMPANY_ID"));
					mr.pm.put("calc_role", "indie");
				}
				data = this.financeService.listOrderAccountHistoryService(mr.pm);
			}
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询历史对账记录异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询历史对账记录异常");
		}
		return json;
	}
	
	@RequestMapping("/account/info")
	public ListRange accountInfo(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime, 
			String orderNo, String calcStatus, String payWho, String groupCity){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			if(CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime)){
				
				mr.pm.put("CREATE_BEGIN_TIME", beginTime);
				mr.pm.put("CREATE_END_TIME", endTime);	
				mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
				
				mr.pm.put("orderNo", orderNo);
				mr.pm.put("ACCOUNT_STATUS", calcStatus);
				
				mr.pm.put("payWho", payWho);
				
				mr.pm.put("groupCity", groupCity);
				
				List<Map<String, Object>> data = this.financeService.listAccountInfoService(mr.pm);
				json.setData(data);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询对账记录异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询对账记录异常");
		}
		return json;
	}
	
	@RequestMapping("/alone/order")
	public ListRange aloneOrder(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime, String companyId, String status, String companyPayNo, String parentCompanyId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			if(CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime)){
				
				mr.pm.put("CREATE_BEGIN_TIME", beginTime);
				mr.pm.put("CREATE_END_TIME", endTime);	
				
				mr.pm.put("COMPANY_PAY_STATUS", status);
				if(!CommonUtils.checkString(status)){
					mr.pm.put("COMPANY_PAY_STATUS", 0);
				}

				Map<String, Object> params = new HashMap<String, Object>();

				if(CommonUtils.checkString(companyId)){
					
					mr.pm.put("COMPANY_ID", companyId);
					
					params.put("ID", companyId);
					Map<String, Object> company = this.companyService.companyDetailService(params);
					String PARENT_COMPANY_ID = (String)company.get("PID");
					if(PARENT_COMPANY_ID.equals("-1")){
						mr.pm.put("calc_role", "parent");
					}else{
						mr.pm.put("calc_role", "indie");
					}
				}else{
					String PARENT_COMPANY_ID = (String)user.get("PID");
					String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
					if(COMPANY_TYPE.equals("0")){
						params.put("T_USER_ID", user.get("ID"));
						params.put("T_COMPANY_ID", user.get("COMPANY_ID"));
						params.put("HAS_CITY", "YES");
						List<Map<String, Object>> siteManagers = this.companyService.listSiteManagerService(params);
						StringBuffer siteIds = new StringBuffer();
						for (Map<String, Object> siteManager : siteManagers) {
							String id = (String)siteManager.get("ID");
							siteIds.append("'"+id+"',");
						}
						mr.pm.put("SITE_RELA_IDS", siteIds.substring(0, siteIds.toString().length()-1));
					}else if(PARENT_COMPANY_ID.equals("-1") && !COMPANY_TYPE.equals("0")){
						mr.pm.put("calc_role", "parent");
						mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
					}else{
						mr.pm.put("calc_role", "indie");
						mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
					}
				}
				
				if(CommonUtils.checkString(parentCompanyId)){
					mr.pm.put("PARENT_COMPANY_ID", parentCompanyId);
				}
				
				mr.pm.put("COMPANY_PAY_NO", companyPayNo);
				List<Map<String, Object>> data = this.financeService.listAloneOrderService(mr.pm);
				json.setData(data);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询独立对账订单异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询独立对账订单异常");
		}
		return json;
	}
	
	@RequestMapping("/alone/order/export")
	public void aloneOrderExport(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime, String companyId, String status, String companyPayNo, String parentCompanyId){
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			if(CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime)){
				
				mr.pm.put("CREATE_BEGIN_TIME", beginTime);
				mr.pm.put("CREATE_END_TIME", endTime);	
				
				mr.pm.put("COMPANY_PAY_STATUS", status);
				if(!CommonUtils.checkString(status)){
					mr.pm.put("COMPANY_PAY_STATUS", 0);
				}

				Map<String, Object> params = new HashMap<String, Object>();

				if(CommonUtils.checkString(companyId)){
					
					mr.pm.put("COMPANY_ID", companyId);
					
					params.put("ID", companyId);
					Map<String, Object> company = this.companyService.companyDetailService(params);
					String PARENT_COMPANY_ID = (String)company.get("PID");
					if(PARENT_COMPANY_ID.equals("-1")){
						mr.pm.put("calc_role", "parent");
					}else{
						mr.pm.put("calc_role", "indie");
					}
				}else{
					String PARENT_COMPANY_ID = (String)user.get("PID");
					String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
					if(COMPANY_TYPE.equals("0")){
						
					}else if(PARENT_COMPANY_ID.equals("-1") && !COMPANY_TYPE.equals("0")){
						mr.pm.put("calc_role", "parent");
						mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
					}else{
						mr.pm.put("calc_role", "indie");
						mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
					}
				}
				
				if(CommonUtils.checkString(parentCompanyId)){
					mr.pm.put("PARENT_COMPANY_ID", parentCompanyId);
				}
				
				mr.pm.put("COMPANY_PAY_NO", companyPayNo);
				List<Map<String, Object>> data = this.financeService.listAloneOrderService(mr.pm);
				
				String filePath = ConfigLoader.config.getStringConfigDetail("system.file.path");
				
				File file = new File(filePath+"/pay_account.xls");///-----------------------------------读取文件的地址
				FileInputStream fis = new FileInputStream(file);
				HSSFWorkbook workBook= new HSSFWorkbook(fis);
				int row_num = 1;
				HSSFSheet hssfSheet = workBook.getSheet("Sheet1");
				for (Map<String, Object> d : data) {
					HSSFRow row = hssfSheet.createRow(row_num);
					row.createCell(0).setCellValue(" "+String.valueOf(d.get("BUY_COMPANY")));
					row.createCell(1).setCellValue(" "+String.valueOf(d.get("BUY_CHINA_NAME")));
					row.createCell(2).setCellValue(" "+String.valueOf(d.get("START_DATE")));
					row.createCell(3).setCellValue(" "+String.valueOf(d.get("PRODUCE_NAME")));
					row.createCell(4).setCellValue(" "+String.valueOf(d.get("DAY_COUNT")));
					row.createCell(5).setCellValue(" "+String.valueOf(d.get("P_TYPE")));
					row.createCell(6).setCellValue(" "+String.valueOf(d.get("SALE_COMPANY")));
					row.createCell(7).setCellValue(" "+String.valueOf(d.get("PERSON_COUNT")));
					row.createCell(8).setCellValue(" "+String.valueOf(d.get("ACTUAL_AMOUNT")));
					row.createCell(9).setCellValue(" "+String.valueOf(d.get("NO")));
					row_num += 1;

				}
				response.setContentType("application/vnd.ms-excel");    
		        response.setHeader("Content-disposition", "attachment;filename=pay_account-"+DateUtil.getNowDate()+".xls");    
		        ServletOutputStream ouputStream = response.getOutputStream();    
		        workBook.write(ouputStream);    
		        ouputStream.flush();    
		        ouputStream.close();    
			}
		} catch (Exception e) {
			log.error("导出独立对账订单异常",e);
		}
	}
	
	@RequestMapping("/alone/recharge")
	public ListRange aloneRecharge(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime, String companyId, String status, String companyPayNo, String parentCompanyId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			if(CommonUtils.checkString(beginTime) && CommonUtils.checkString(endTime)){
				
				mr.pm.put("CREATE_BEGIN_TIME", beginTime);
				mr.pm.put("CREATE_END_TIME", endTime);	
				
				mr.pm.put("COMPANY_PAY_STATUS", status);
				if(!CommonUtils.checkString(status)){
					mr.pm.put("COMPANY_PAY_STATUS", 0);
				}
				mr.pm.put("COMPANY_PAY_NO", companyPayNo);
				
				Map<String, Object> params = new HashMap<String, Object>();
				
				if(CommonUtils.checkString(companyId)){
					
					mr.pm.put("COMPANY_ID", companyId);
					
					params.put("ID", companyId);
					Map<String, Object> company = this.companyService.companyDetailService(params);
					String PARENT_COMPANY_ID = (String)company.get("PID");
					if(PARENT_COMPANY_ID.equals("-1")){
						mr.pm.put("calc_role", "parent");
					}else{
						mr.pm.put("calc_role", "indie");
					}
				}else{
					String PARENT_COMPANY_ID = (String)user.get("PID");
					String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
					if(COMPANY_TYPE.equals("0")){
						
					}else if(PARENT_COMPANY_ID.equals("-1") && !COMPANY_TYPE.equals("0")){
						mr.pm.put("calc_role", "parent");
						mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
					}else{
						mr.pm.put("calc_role", "indie");
						mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
					}
				}
				
				if(CommonUtils.checkString(parentCompanyId)){
					mr.pm.put("PARENT_COMPANY_ID", parentCompanyId);
				}
				
				List<Map<String, Object>> data = this.financeService.listRechargeService(mr.pm);
				json.setData(data);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询独立充值异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询独立充值异常");
		}
		return json;
	}

	
	@RequestMapping("/pay/attr")
	public ListRange payAttr(HttpServletRequest request, HttpServletResponse response, MapRange mr, String beginTime, String endTime, String companyId, String parentCompanyId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			
			String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
			if(!CommonUtils.checkString(COMPANY_TYPE) || !COMPANY_TYPE.equals("0")){
				json.setStatusCode("-2");//只有站长或者站长子用户可以打款
				json.setSuccess(false);
				return json;
			}
			String ATTR_URL = "";
			
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile FILE = multipartRequest.getFile("file");
			if(FILE != null && FILE.getBytes().length > 0){
				String file_name = FILE.getOriginalFilename();
				if(CommonUtils.checkString(file_name)){
					if (FileUtil.checkFilesuffix(file_name,"xls,doc,xlsx,docx")){
						log.error("文件类型错误");
						json.setSuccess(false);
						json.setStatusCode("-4");
						json.setMessage("文件类型错误");
						return json;
					}else{
						String[] paths = UploadUtil.pathAdmin("finance");
						String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
						file_name = CommonUtils.uuid()+file_suffix;
						ATTR_URL = UploadUtil.uploadByte(FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
					}
				}
				
				Map<String, Object> params = new HashMap<String, Object>();
//				params.put("T_USER_ID", user.get("ID"));
//				params.put("T_COMPANY_ID", user.get("COMPANY_ID"));
//				params.put("HAS_CITY", "YES");
//				List<Map<String, Object>> siteManagers = this.companyService.listSiteManagerService(params);
//				StringBuffer siteIds = new StringBuffer();
//				for (Map<String, Object> siteManager : siteManagers) {
//					String id = (String)siteManager.get("ID");
//					siteIds.append("'"+id+"',");
//				}
//				mr.pm.put("SITE_RELA_ID", siteIds.substring(0, siteIds.toString().length()-1));
				
				
				if(CommonUtils.checkString(companyId)){
					
					mr.pm.put("COMPANY_ID", companyId);
					
					params.put("ID", companyId);
					Map<String, Object> company = this.companyService.companyDetailService(params);
					String PARENT_COMPANY_ID = (String)company.get("PID");
					if(PARENT_COMPANY_ID.equals("-1")){
						mr.pm.put("calc_role", "parent");
					}else{
						mr.pm.put("calc_role", "indie");
					}
				}else{
					String PARENT_COMPANY_ID = (String)user.get("PID");
					if(COMPANY_TYPE.equals("0")){
						
					}else if(PARENT_COMPANY_ID.equals("-1") && !COMPANY_TYPE.equals("0")){
						mr.pm.put("calc_role", "parent");
						mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
					}else{
						mr.pm.put("calc_role", "indie");
						mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
					}
				}
				
				if(CommonUtils.checkString(parentCompanyId)){
					mr.pm.put("PARENT_COMPANY_ID", parentCompanyId);
				}
				
//				mr.pm.put("calc_role", "manager");
				
				mr.pm.put("USER_ID", user.get("ID"));
				mr.pm.put("USER_NAME", user.get("USER_NAME"));
				mr.pm.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
				mr.pm.put("DEPART_ID", user.get("DEPART_ID"));
				mr.pm.put("ATTR_URL", ATTR_URL);
				
				
				mr.pm.put("CREATE_BEGIN_TIME", beginTime);
				mr.pm.put("CREATE_END_TIME", endTime);
				
				this.financeService.saveCompanyPayService(mr.pm);
				
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
				json.setStatusCode("-1");
				json.setMessage("不允许上传空文件!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("上传凭证异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("上传凭证异常");
		}
		return json;
	}

}
