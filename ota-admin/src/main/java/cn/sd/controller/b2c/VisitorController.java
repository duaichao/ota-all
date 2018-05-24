package cn.sd.controller.b2c;

import java.io.File;
import java.io.FileInputStream;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.config.ConfigLoader;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.BaseController;
import cn.sd.service.order.IVisitorOrderService;
import cn.sd.service.site.ICompanyService;

@RestController("b2cVisitorController")
@RequestMapping("/b2c/visitor")
public class VisitorController extends BaseController{

	private static Log log = LogFactory.getLog(VisitorController.class);
	
	@Resource
	private IVisitorOrderService visitorOrderService;
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("/")
	public ModelAndView traffic(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/b2b/visitor");
	}

	//年龄段    线路属性  
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String startDate, String endDate, String produceAttr, String age, String query){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start);
			mr.pm.put("end", start+limit);
			/**
			 * 站长-管理城市
			 * 总公司-总公司,子公司
			 * 子公司-子公司
			 */
			String companyPid = (String)user.get("PID");
			String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
			if(!user.get("USER_NAME").equals("admin")){
				if(COMPANY_TYPE.equals("0")){
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("T_USER_ID", user.get("ID"));
					params.put("T_COMPANY_ID", user.get("COMPANY_ID"));
					params.put("HAS_CITY", "YES");
					List<Map<String, Object>> siteManagers = this.companyService.listSiteManagerService(params);
					if(CommonUtils.checkList(siteManagers)){
						StringBuffer siteIds = new StringBuffer();
						for (Map<String, Object> siteManager : siteManagers) {
							String id = (String)siteManager.get("ID");
							siteIds.append("'"+id+"',");
						}
						mr.pm.put("SITE_RELA_ID", siteIds.substring(0, siteIds.toString().length()-1));
					}
				}else{
					if(companyPid.equals("-1")){
						mr.pm.put("CREATE_COMPANY_PID", user.get("COMPANY_ID"));
					}else{
						mr.pm.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
					}
				}
			}
			
			mr.pm.put("PAY_BEGIN_TIME", startDate);
			mr.pm.put("PAY_END_TIME", endDate);
			if(CommonUtils.checkString(produceAttr)){
				mr.pm.put("PRODUCE_ATTR", new String(produceAttr.getBytes("iso-8859-1"), "utf-8"));	
			}
			
			if(CommonUtils.checkString(age)){
				String[] _age = age.split("-");
				mr.pm.put("MIN_AGE", _age[0]);
				if(_age.length>1){
					mr.pm.put("MAX_AGE", _age[1]);
				}
			}
			if(CommonUtils.checkString(query)){
				mr.pm.put("query", new String(query.getBytes("ISO-8859-1"), "UTF-8"));
			}
			List<Map<String, Object>> data = this.visitorOrderService.listOrderVisitorService(mr.pm);
			int totalSize = this.visitorOrderService.countOrderVisitorService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询游客信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询游客信息异常");
		}
		return json;
	}
	
	//年龄段    线路属性  
	@RequestMapping("/export")
	public void export(HttpServletRequest request, HttpServletResponse response, MapRange mr, String startDate, String endDate, String produceAttr, String age, String query){
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("start", 1);
			mr.pm.put("end", 100000);
			/**
			 * 站长-管理城市
			 * 总公司-总公司,子公司
			 * 子公司-子公司
			 */
			String companyPid = (String)user.get("PID");
			String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
			if(!user.get("USER_NAME").equals("admin")){
				if(COMPANY_TYPE.equals("0")){
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("T_USER_ID", user.get("ID"));
					params.put("T_COMPANY_ID", user.get("COMPANY_ID"));
					params.put("HAS_CITY", "YES");
					List<Map<String, Object>> siteManagers = this.companyService.listSiteManagerService(params);
					if(CommonUtils.checkList(siteManagers)){
						StringBuffer siteIds = new StringBuffer();
						for (Map<String, Object> siteManager : siteManagers) {
							String id = (String)siteManager.get("ID");
							siteIds.append("'"+id+"',");
						}
						mr.pm.put("SITE_RELA_ID", siteIds.substring(0, siteIds.toString().length()-1));
					}
				}else{
					if(companyPid.equals("-1")){
						mr.pm.put("CREATE_COMPANY_PID", user.get("COMPANY_ID"));
					}else{
						mr.pm.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
					}
				}
			}
			
			mr.pm.put("PAY_BEGIN_TIME", startDate);
			mr.pm.put("PAY_END_TIME", endDate);
			if(CommonUtils.checkString(produceAttr)){
				mr.pm.put("PRODUCE_ATTR", new String(produceAttr.getBytes("iso-8859-1"), "utf-8"));	
			}
			
			if(CommonUtils.checkString(age)){
				String[] _age = age.split("-");
				mr.pm.put("MIN_AGE", _age[0]);
				if(_age.length>1){
					mr.pm.put("MAX_AGE", _age[1]);
				}
			}
			if(CommonUtils.checkString(query)){
				mr.pm.put("query", new String(query.getBytes("ISO-8859-1"), "UTF-8"));
			}
			List<Map<String, Object>> data = this.visitorOrderService.listOrderVisitorService(mr.pm);
			int totalSize = this.visitorOrderService.countOrderVisitorService(mr.pm);
			
			String filePath = ConfigLoader.config.getStringConfigDetail("system.file.path");
			
			File file = new File(filePath+"/visitors.xls");
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook workBook= new HSSFWorkbook(fis);
			
			int row_num = 1, num = 1;
			
			HSSFSheet hssfSheet = workBook.getSheet("Sheet1");
			
			for (Map<String, Object> d: data) {
			
				HSSFRow row = hssfSheet.createRow(row_num);
				
				row.createCell(0).setCellValue(num);
				
//				序号	姓名	生日	性别	类型	手机	证件	产品	订单编号	出团日期	订单来源	付款时间
				row.createCell(1).setCellValue((String)d.get("NAME"));
					row.createCell(2).setCellValue((String)d.get("生日"));
				if(CommonUtils.checkString(d.get("sex"))){
					row.createCell(3).setCellValue(String.valueOf(d.get("sex")).equals("1")?"男":"女");
				}
				
				row.createCell(4).setCellValue(String.valueOf(d.get("ATTR_NAME")));
				
				if(CommonUtils.checkString(d.get("MOBILE"))){
					row.createCell(5).setCellValue((String)d.get("MOBILE"));
				}
				if(CommonUtils.checkString(d.get("CARD_NO"))){
					row.createCell(6).setCellValue((String)d.get("CARD_NO"));
				}
				row.createCell(7).setCellValue((String)d.get("PRODUCE_NAME"));
				row.createCell(8).setCellValue((String)d.get("ORDER_NO"));
				row.createCell(9).setCellValue((String)d.get("START_DATE"));
				row.createCell(10).setCellValue((String)d.get("BUY_COMPANY"));
				row.createCell(11).setCellValue((String)d.get("PAY_TIME"));
				
				num++;
				row_num++;
			}
			workBook.removeSheetAt(1);
			/**
			 * 输出文件
			 */
			response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-disposition", "attachment;filename=visitors-"+DateUtil.getNowDate()+".xls");    
	        ServletOutputStream ouputStream = response.getOutputStream();    
	        workBook.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close(); 
		} catch (Exception e) {
			log.error("查询游客信息异常",e);
		}
	}
}
