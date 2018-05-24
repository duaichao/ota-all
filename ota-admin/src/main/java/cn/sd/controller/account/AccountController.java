package cn.sd.controller.account;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.Assert;
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
import cn.sd.core.web.ExtSupportAction;
import cn.sd.rmi.ServiceProxyFactory;
import cn.sd.service.account.IAccountService;
import cn.sd.service.pay.IPayService;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/account")
@SuppressWarnings("all")
public class AccountController extends ExtSupportAction {

	private static Log log = LogFactory.getLog(AccountController.class);
	
	@Resource 
	private IAccountService accountService;
	@Resource
	private IPayService payService;
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/account/index");
	}
	
	@RequestMapping("/export")
	public void export(HttpServletRequest request,HttpServletResponse response, MapRange mr,String DEPART_ID){
		try {
			mr.pm.put("start", 1);
			mr.pm.put("end", 10000);
			mr.pm.put("OP_TIME_BEGIN", request.getParameter("OP_TIME_BEGIN"));
			mr.pm.put("OP_TIME_END", request.getParameter("OP_TIME_END"));
			mr.pm.put("DEPART_ID", DEPART_ID);
			List<Map<String, Object>> data = accountService.listService(mr.pm);
			
			String filePath = ConfigLoader.config.getStringConfigDetail("system.file.path");
			
			File file = new File(filePath+"/account_log.xls");
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook workBook= new HSSFWorkbook(fis);
			
			String ORDER_ID = "", TRAFFIC_ID = "", sheetName = "";
			int row_num = 1, num = 1;
			
			HSSFSheet hssfSheet = workBook.getSheet("Sheet1");
			
			for (Map<String, Object> d: data) {
			
				HSSFRow row = hssfSheet.createRow(row_num);
				
				row.createCell(0).setCellValue(num);
				row.createCell(1).setCellValue((String)d.get("DES"));
				row.createCell(2).setCellValue((String)d.get("OP_TIME_SS"));
				row.createCell(3).setCellValue(String.valueOf(d.get("MONEY")));
				row.createCell(4).setCellValue(String.valueOf(d.get("YEMONEY")));
				row.createCell(5).setCellValue((String)d.get("NOTE"));
				num++;
				row_num++;
			}
			workBook.removeSheetAt(1);
			/**
			 * 输出文件
			 */
			response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-disposition", "attachment;filename=account-log-"+DateUtil.getNowDate()+".xls");    
	        ServletOutputStream ouputStream = response.getOutputStream();    
	        workBook.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close(); 
	        
		} catch (Exception e) {
			log.error("导出资金流水异常",e);
		}
	}
	
	@RequestMapping("/recharge")
	public ModelAndView recharge(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		try {

			Map<String, Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String DEPART_ID = request.getParameter("paramId");
			double AMOUNT = CommonUtils.checkString(request.getParameter("money"))?Double.parseDouble(request.getParameter("money")):0;
			String NO = ServiceProxyFactory.getProxy().getOnLineNoDao("ZXZFONLINE");
			request.setAttribute("no", NO);
			String ID = CommonUtils.uuid();
			request.setAttribute("id", ID);
			mr.pm.put("ID", ID);
			mr.pm.put("NO", NO);
			mr.pm.put("PAY_TYPE", 0);
			mr.pm.put("STATUS", 0);
			mr.pm.put("CREATE_USER", (String)user.get("USER_NAME"));
			mr.pm.put("CREATE_USER_ID", (String)user.get("ID"));
			mr.pm.put("AMOUNT", AMOUNT);
			mr.pm.put("DEPART_ID", DEPART_ID);

			
			String companyId = accountService.getCompanyIdService(DEPART_ID);
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("ID", companyId);
			Map<String, Object> company = this.companyService.companyDetailService(p);
			mr.pm.put("ACCOUNT_WAY", (String)company.get("ACCOUNT_WAY"));
			
			this.accountService.saveAccountLogService(mr.pm);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("/account/recharge");
	}
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response, MapRange mr,String DEPART_ID){
		ListRange json = new ListRange();
		try {
			
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			
			mr.pm.put("start", start + 1);
			mr.pm.put("end", start + limit);
			mr.pm.put("OP_TIME_BEGIN", request.getParameter("OP_TIME_BEGIN"));
			mr.pm.put("OP_TIME_END", request.getParameter("OP_TIME_END"));
			mr.pm.put("DEPART_ID", DEPART_ID);
			List<Map<String, Object>> data = accountService.listService(mr.pm);
			int totalSize = accountService.countService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询账户信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询账户信息异常");
		}
		return json;
	}
	
	@RequestMapping("/stat")
	public Map<String, String> stat(HttpServletRequest request,HttpServletResponse response, MapRange mr,String DEPART_ID){
		mr.pm.put("DEPART_ID", DEPART_ID);
		Map<String, String> map = accountService.statService(mr.pm);
		if(map == null){
			String a = accountService.accountStateService(DEPART_ID);
			map = new HashMap<String, String>();
			map.put("ZHZT", a==null?"启用":a);
			map.put("YE", "0");
			map.put("ZXCZ", "0");
			map.put("SDCZ", "0");
			map.put("TZ", "0");
			map.put("WH", "0");
			map.put("YEZF", "0");
		}
		 
		return map;
		
	}
	
	@RequestMapping("/updateState")
	public ListRange updateState(HttpServletRequest request,HttpServletResponse response){
		ListRange json = new ListRange();
		Map<String,Object> accountParams = new HashMap<String,Object>();
		accountParams.put("DEPART_ID", request.getParameter("DEPART_ID"));
		accountParams.put("IS_USE", request.getParameter("STATE"));
		
		Map<String,Object> user = new HashMap<String,Object>();
		String companyId = accountService.getCompanyIdService(accountParams.get("DEPART_ID").toString());
		accountParams.put("COMPANY_ID", companyId==null?"":companyId);
		
		user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		accountParams.put("USER_ID",user.get("ID").toString());
		try {
			accountService.createAccountService(accountParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		accountService.updateStateService(accountParams);
		json.setSuccess(true);
		return json;
	}
	
	
	
	@RequestMapping("/save")
	public ListRange save(HttpServletRequest request,HttpServletResponse response, MapRange mr,String DEPART_ID){
		ListRange json = new ListRange();
		Map<String, Object> suser = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		Map<String,Object> accountParams = new HashMap<String,Object>();
		Map<String,Object> user = new HashMap<String,Object>();
		String accountId="";
		String accountWay="";
		String companyId ="";
		try {
			accountParams.put("DEPART_ID", mr.getPm().get("DEPART_ID").toString());
			companyId = accountService.getCompanyIdService(accountParams.get("DEPART_ID").toString());
			accountParams.put("COMPANY_ID", companyId==null?"":companyId);
			
			user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			accountParams.put("USER_ID",user.get("ID").toString());
			accountId = accountService.createAccountService(accountParams);

			accountParams.put("ID", companyId);
			Map<String, Object> company = this.companyService.companyDetailService(accountParams);
			accountWay = (String)company.get("ACCOUNT_WAY");
			accountParams.remove("ID");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			json.setSuccess(false);
			json.setStatusCode("-5");
			json.setMessage("账户创建失败");
			return json;
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ACCOUNT_ID", accountId);
		params.put("MONEY",mr.getPm().get("MONEY").toString());
		params.put("USER_ID",user.get("ID").toString());
		params.put("NOTE",mr.getPm().get("NOTE").toString());
		params.put("DEPART_ID", mr.getPm().get("DEPART_ID").toString());
		params.put("ACCOUNT_WAY", accountWay);
		//凭证
		String CERTIFY_ATTR = "";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile CERTIFY_ATTR_FILE = multipartRequest.getFile("CERTIFY_ATTR");
		if(CERTIFY_ATTR_FILE != null){
			String file_name = CERTIFY_ATTR_FILE.getOriginalFilename();
			if(CommonUtils.checkString(file_name)){
				if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
					log.error("图片类型错误");
					json.setSuccess(false);
					json.setStatusCode("-4");
					json.setMessage("图片类型错误");
					return json;
				}else{
					String[] paths = UploadUtil.pathAdmin("account");
					String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
					file_name = CommonUtils.uuid()+file_suffix;
					try {
						CERTIFY_ATTR = UploadUtil.uploadByte(CERTIFY_ATTR_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.error("图片上传失败");
						json.setSuccess(false);
						json.setStatusCode("-5");
						json.setMessage("图片上传失败");
						return json;
						
					}
				}
			}
		}
		
		params.put("CERTIFY_ATTR", CERTIFY_ATTR);
		
		String type = mr.getPm().get("TYPE").toString();
		if(type.equals("1")){
			//手动充值
			params.put("CREATE_USER_ID", user.get("ID"));
			params.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
			params.put("COMPANY_ID", companyId);
			try {
				accountService.addLineRechargeService(params);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("手动充值失败");
				json.setSuccess(false);
				json.setStatusCode("-6");
				json.setMessage("手动充值失败");
				return json;
			}
			json.setSuccess(true);
		}else if(type.equals("2")){
			//手动退款
			String rs = accountService.refundService(params);
			if(rs.equals("-1")){
				json.setSuccess(false);
				json.setStatusCode("-1");
				json.setMessage("超过余额总额");
			}else if(rs.equals("-2")){
				json.setSuccess(false);
				json.setStatusCode("-2");
				json.setMessage("超过充值总额");
			}else if(rs.equals("-3")){
				json.setSuccess(false);
				json.setStatusCode("-3");
				json.setMessage("链接中断 请重试");
			}else{
				json.setSuccess(true);
			}
		}else if(type.equals("3")){
			//透支
			accountService.addOverdraftService(params);
			json.setSuccess(true);
		}else if(type.equals("4")){
			//还款
			String rs = accountService.overdraftRefundService(params);
			if(rs.equals("-1")){
				json.setSuccess(false);
				json.setStatusCode("-3");
				json.setMessage("超过透支总额");
			}else if(rs.equals("-3")){
				json.setSuccess(false);
				json.setStatusCode("-3");
				json.setMessage("链接中断 请重试");
			}else{
				json.setSuccess(true);
			}
		}
		
		return json;
		
	}

	@RequestMapping("/onlinePay")
	public ModelAndView onlinePay(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId, String bankCode){
		ListRange json = new ListRange();
		String url = "pay/alipay/alipayto";
		try {
			
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String USER_ID = (String)user.get("ID");
			mr.pm.put("ID", orderId);
			
			Map<String, Object> accountLog = this.accountService.listAccountLogService(mr.pm).get(0);
			
			Double m = Double.parseDouble(String.valueOf(accountLog.get("AMOUNT")));
			String orderNo = (String)accountLog.get("NO");
			
			Assert.hasText(orderNo);
			Assert.notNull(m);
			Map<String, Object> result = null;
			//支付宝余额支付 or 网银支付
			if ("1".equals(bankCode)) {
				result = this.payService.alipay(orderNo, bankCode, m, false, USER_ID, "ZXCZ", "");
				request.setAttribute("sParaTemp", result.get("sParaTemp"));
			}else if("2".equals(bankCode)){
				result = this.payService.cftpay(orderNo, bankCode, m, false, USER_ID, request, response);
				request.setAttribute("requestUrl", result.get("requestUrl"));
				url = "pay/cft/tenpayto";
			}else {
				//判断是支付宝网银 or 财付通网银  默认支付宝网银
				result = this.payService.alipay(orderNo, bankCode, m, true, USER_ID, "ZXCZ", "");
				request.setAttribute("sParaTemp", result.get("sParaTemp"));
			}
			
			mr.pm.clear();
			mr.pm.put("STATUS", 1);
			mr.pm.put("ID", orderId);
			this.accountService.updateAccountLogStatusService(mr.pm);
		} catch (Exception e) {
			log.error("订单支付异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("订单支付异常");
		}
		return new ModelAndView(url);
	}
	
	
}
