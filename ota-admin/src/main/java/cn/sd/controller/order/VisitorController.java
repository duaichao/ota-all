package cn.sd.controller.order;

import java.io.InputStream;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.BaseController;
import cn.sd.service.order.IVisitorOrderService;
import cn.sd.service.resource.ITrafficService;

@RestController
@RequestMapping("/order/visitor")
public class VisitorController extends BaseController{

	private static Log log = LogFactory.getLog(VisitorController.class);
	
	@Resource
	private IVisitorOrderService visitorOrderService;
	@Resource
	private ITrafficService trafficService;

	@RequestMapping("/listVisitor")
	public ListRange listVisitor(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start + 1);
			mr.pm.put("end", start + limit);
			List<Map<String, Object>> data = this.visitorOrderService.listVisitorService(mr.pm);
			int totalSize = this.visitorOrderService.countVisitorService(mr.pm);
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
	
	@RequestMapping("/listOrderVisitor")
	public ListRange listOrderVisitor(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId, String orderNo){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("start", 1);
			mr.pm.put("end", 10000);
			mr.pm.put("ORDER_ID", orderId);
			mr.pm.put("ORDER_NO", orderNo);
//			mr.pm.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
			List<Map<String, Object>> data = this.visitorOrderService.listOrderVisitorService(mr.pm);
//			int totalSize = this.visitorOrderService.countOrderVisitorService(mr.pm);
			json.setData(data);
//			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询游客信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询游客信息异常");
		}
		return json;
	}
	
	@RequestMapping("/saveVisitor")
	public ListRange saveVisitor(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			mr.pm.put("ID", CommonUtils.uuid());
			this.visitorOrderService.saveVisitorService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存游客信息异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存游客信息异常");
		}
		return json;
	}
	
	@RequestMapping("/importVisitor")
	public ListRange importVisitor(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		ListRange json = new ListRange();
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			//先清空订单的所有游客
//			params.put("ORDER_ID", orderId);
//			this.visitorOrderService.delOrderVisitorByOrderIDService(params);
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");
			InputStream excel = file.getInputStream();
			HSSFWorkbook workBook= new HSSFWorkbook(excel); 
			//读取工作表的数据 
			List<Map<String, Object>> visitors = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> priceAttrs = this.trafficService.listAllPriceAttrService(params);
			for(int k = 0; k < workBook.getNumberOfSheets(); k++){ 
				HSSFSheet hssfSheet = workBook.getSheetAt(k);
				if (hssfSheet == null) {
					json.setSuccess(false);
					json.setStatusCode("-1");//空sheet
					return json;
	            }
				// 循环行Row  从第2行开始
	            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
	            	HSSFRow hssfRow = hssfSheet.getRow(rowNum);
//	            	hssfSheet.removeRow(arg0);
	            	if (hssfRow == null) {
	            		json.setSuccess(false);
						json.setStatusCode("-2");//空row
	            		return json;
	                }
	            	Map<String, Object> visitor = new HashMap<String, Object>();
	            	if(rowNum>1){
	            		if (!"".equals(cellValue2String(hssfRow.getCell(0)).trim()) && !"".equals(cellValue2String(hssfRow.getCell(1)).trim())) {
	            			String NAME = cellValue2String(hssfRow.getCell(0));
		            		String ATTR_NAME = cellValue2String(hssfRow.getCell(1));
		            		String ATTR_ID = "";
		            		if(!CommonUtils.checkString(NAME) || !CommonUtils.checkString(ATTR_NAME)){
		            			json.setSuccess(false);
								json.setStatusCode("-3");//name或type为空
		            			return json;
		            		}
		            		
		            		for (Map<String, Object> priceAttr : priceAttrs) {
								String TITLE = (String)priceAttr.get("TITLE");
								if(ATTR_NAME.equals(TITLE)){
									ATTR_ID = (String)priceAttr.get("ID");
									break;
								}
							}
		            		
		            		if(!CommonUtils.checkString(ATTR_ID)){
		            			json.setSuccess(false);
								json.setStatusCode("-4");//游客类型不正确
		            			return json;
		            		}
//		            		visitor.put("ID", CommonUtils.uuid());
		            		visitor.put("NAME", NAME);
		            		visitor.put("ATTR_ID", ATTR_ID);
		            		visitor.put("ATTR_NAME", ATTR_NAME);
		            		String SEX = cellValue2String(hssfRow.getCell(2));
		            		if(CommonUtils.checkString(SEX) && SEX.equals("男")){
		            			SEX = "1";
		            		}else if(CommonUtils.checkString(SEX) && SEX.equals("女")){
		            			SEX = "2";
		            		}
		            		visitor.put("SEX", SEX);
//		            		visitor.put("NATION", cellValue2String(hssfRow.getCell(3)));
		            		visitor.put("MOBILE", cellValue2String(hssfRow.getCell(3)));
//		            		visitor.put("EMAIL", cellValue2String(hssfRow.getCell(5)));
		            		visitor.put("CARD_TYPE", cellValue2String(hssfRow.getCell(4)));
		            		visitor.put("CARD_NO", cellValue2String(hssfRow.getCell(5)));
		            		visitors.add(visitor);
	            		}else{
	            			json.setSuccess(false);
							json.setStatusCode("-3");//name或type为空
	            			return json;
	            		}
					}
	            }
			}
			json.setData(visitors);
//			this.visitorOrderService.saveBatchVisitorService(visitors, orderId);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存游客信息异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存游客信息异常");
		}
		return json;
	}
		
	public String cellValue2String(HSSFCell cell){
		String cellvalue = null;  
        if (cell != null){  
           // 判断当前Cell的Type  
           switch (cell.getCellType()){  
           // 如果当前Cell的Type为NUMERIC  
           case HSSFCell.CELL_TYPE_NUMERIC:{  
              // 判断当前的cell是否为Date  
              if (HSSFDateUtil.isCellDateFormatted(cell)){  
                 // 如果是Date类型则，取得该Cell的Date值  
                 //Date date = cell.getDateCellValue();  
                 // 把Date转换成本地格式的字符串  
                 cellvalue = cell.getDateCellValue().toLocaleString();  
              }  
              // 如果是纯数字  
              else{  
                 // 取得当前Cell的数值  
                 long num = (long) cell.getNumericCellValue();  
                 cellvalue = String.valueOf(num);  
              }  
              break;  
           }  
           // 如果当前Cell的Type为STRIN  
           case HSSFCell.CELL_TYPE_STRING:  
              // 取得当前的Cell字符串  
              cellvalue = cell.getStringCellValue().replaceAll("'", "''");  
              break;  
           // 默认的Cell值  
           default:  
              cellvalue = " ";  
           }  
        } else{  
           cellvalue = "";  
        }  
        return	cellvalue.trim();
	}
	
	@RequestMapping("/delVisitor")
	public ListRange delVisitor(HttpServletRequest request,HttpServletResponse response,String models){
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ID", jobject.getString("ID"));
				this.visitorOrderService.delOrderVisitorByIDService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除游客异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
}
