package cn.sd.controller.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
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
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.sd.core.config.ConfigLoader;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.FileUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.POIWORD;
import cn.sd.core.util.StyledText;
import cn.sd.core.util.UploadUtil;
import cn.sd.core.web.BaseController;
import cn.sd.entity.RouteEntity;
import cn.sd.entity.produce.RouteDayDetail;
import cn.sd.entity.produce.RouteDayDetailList;
import cn.sd.power.PowerFactory;
import cn.sd.service.resource.IRouteService;
import cn.sd.service.resource.ITrafficService;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/resource/route")
public class RouteController extends BaseController{

	private static Log log = LogFactory.getLog(RouteController.class);
	
	@Resource
	private IRouteService routeService;
	@Resource
	private ITrafficService trafficService;
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("/update/earnest")
	public ListRange updateRouteEarnest(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("UPDATE_USER_ID", user.get("ID"));
			mr.pm.put("UPDATE_USER", user.get("USER_NAME"));
			this.routeService.updateRouteEarnestService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("定金设置异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("定金设置异常");
		}
		return json;
	}
	
	@RequestMapping("/save/pro/contact")
	public ListRange saveProContact(HttpServletRequest request, HttpServletResponse response, MapRange mr, String models, String produceId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			mr.pm.put("PRODUCE_ID", produceId);
			mr.pm.put("models", models);
			this.routeService.saveProContactService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存线路联系人异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存线路联系人异常");
		}
		return json;
	}
	
	@RequestMapping("/list/pro/contact")
	public ListRange listProContact(HttpServletRequest request, HttpServletResponse response, MapRange mr, String produceId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.clear();
			mr.pm.put("PRODUCE_ID", produceId);
			List<Map<String, Object>> data = this.routeService.listProContactService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路联系人异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路联系人异常");
		}
		return json;
	}
	
	@RequestMapping("/end/city/list")
	public ListRange endCityList(HttpServletRequest request, HttpServletResponse response, MapRange mr, String cityId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("CITY_ID", cityId);
			mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
			List<Map<String, Object>> data = this.routeService.listEndCityOfRouteByCompanyIdAndCityNameService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路信息异常");
		}
		return json;
	}
	
	@RequestMapping("/copy/day")
	public ListRange copyDay(HttpServletRequest request, HttpServletResponse response, MapRange mr, String dayId, String dayCount, String routeId, String routeCityId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("OLD_DAY_ID", dayId);
			mr.pm.put("ROUTE_CITY_ID", routeCityId);
			mr.pm.put("NO", dayCount);
			this.routeService.copyDayService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路信息异常");
		}
		return json;
	}
	
	@RequestMapping("/export")
	public void export(HttpServletRequest request, HttpServletResponse response, MapRange mr, String routeId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		RouteEntity route = null;
		try {
			mr.pm.put("ID", routeId);
			route = this.routeService.routeDetailService(mr.pm);
			if(route != null){
				/**
				 * 线路行程
				 */
				
				mr.pm.clear();
				mr.pm.put("ROUTE_ID", routeId);
				mr.pm.put("TYPE", 1);
				List<Map<String, Object>> include = this.routeService.listRouteOtherService(mr.pm);
				route.setInclude(include);
				
				mr.pm.put("TYPE", 2);
				List<Map<String, Object>> noclude = this.routeService.listRouteOtherService(mr.pm);
				route.setNoclude(noclude);
				
				mr.pm.put("TYPE", 3);
				List<Map<String, Object>> notice = this.routeService.listRouteOtherService(mr.pm);
				route.setNotice(notice);
				
				mr.pm.put("TYPE", 4);
				List<Map<String, Object>> tips = this.routeService.listRouteOtherService(mr.pm);
				route.setTips(tips);
				
				List<Map<String, Object>> allCity = new ArrayList<Map<String, Object>>();
				
				mr.pm.remove("TYPE");
				String city_name = "";
				Map<String, Object> start_city = null;
				List<Map<String, Object>> citys_temp = new ArrayList<Map<String, Object>>();
				
				List<Map<String, Object>> citys = this.routeService.listRouteCityService(mr.pm);
				for (Map<String, Object> city : citys) {
					String ROUTE_CITY_ID = (String)city.get("ID");
					mr.pm.put("ROUTE_CITY_ID", ROUTE_CITY_ID);
					
					if(!city.get("TYPE").toString().equals("1")){
						List<Map<String, Object>> days = this.routeService.listRouteDayService(mr.pm);
						for (Map<String, Object> day : days) {
							String DAY_ID = (String)day.get("ID");
							mr.pm.clear();
							mr.pm.put("DAY_ID", DAY_ID);
							List<RouteDayDetail> details = this.routeService.listRouteDayDetaiService(mr.pm);
							for (RouteDayDetail detail : details) {
								String DETAIL_ID = detail.getID();
								mr.pm.clear();
								mr.pm.put("DAY_DETAIL_ID", DETAIL_ID);
								List<Map<String, Object>> scenics = this.routeService.listRouteScenicService(mr.pm);
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
				
				if(route.getIS_FULL_PRICE().equals("1")){
					mr.pm.clear();
					mr.pm.put("ROUTE_ID", routeId);
					List<Map<String, Object>> trafficDetails = this.routeService.listRouteTrafficDetailService(mr.pm);
					route.setTRAFFIC_ID((String)trafficDetails.get(0).get("TRAFFIC_ID"));
				}
				route.setCitys(citys);
			}
			
			POIWORD pw = new POIWORD();
			String WORD_TPL = (String)user.get("WORD_TPL"), path = "";
			if(CommonUtils.checkString(WORD_TPL)){
				path = ConfigLoader.config.getStringConfigDetail("upload.path")+"/"+WORD_TPL;
			}else{
				path = ConfigLoader.config.getStringConfigDetail("system.file.path")+"/route_export_tmp.docx";
			}

			FileInputStream is = new FileInputStream(new File(path));
		    XWPFDocument xdoc = new XWPFDocument(is);
		    XWPFHeaderFooterPolicy headerFooterPolicy = xdoc.getHeaderFooterPolicy();
		    
		    String short_name = (String)user.get("SHORT_NAME");
		    if(!CommonUtils.checkString(short_name)){
		    	short_name = (String)user.get("PARENT_SHORT_NAME");
		    	if(!CommonUtils.checkString(short_name)){
		    		short_name = (String)user.get("COMPANY");	
		    	}
		    }
		    
		    XWPFHeader header = headerFooterPolicy.getDefaultHeader();
		    XWPFParagraph header_paragraph = header.getListParagraph().get(0);
		    pw.setParagraphAlignInfo(header_paragraph, ParagraphAlignment.RIGHT, TextAlignment.BOTTOM);
		    XWPFRun header_run = pw.getOrAddParagraphFirstRun(header_paragraph, false, false);
		    pw.setParagraphRunFontInfo(header_paragraph, header_run, "\r\n"+short_name, "宋体",
		    		"Times New Roman", "22", true, false, false, true, null, null,
		            10, 0, 90);
		    
		    
		    XWPFFooter footer = headerFooterPolicy.getDefaultFooter();
		    XWPFParagraph footer_paragraph = footer.getListParagraph().get(0);
		    pw.setParagraphAlignInfo(footer_paragraph, ParagraphAlignment.LEFT, TextAlignment.BOTTOM);
		    XWPFRun footer_run = pw.getOrAddParagraphFirstRun(footer_paragraph, false, false);
		    pw.setParagraphRunFontInfo(footer_paragraph, footer_run, "地址:"+user.get("ADDRESS")+"        电话: "+user.get("MOBILE"), "宋体",
		    		"Times New Roman", "22", true, false, false, true, null, null,
		            10, 0, 90);
		    
		    XWPFParagraph p = xdoc.createParagraph();
		    // 固定值25磅
		    pw.setParagraphSpacingInfo(p, true, "0", "80", null, null, true, "500",
		        STLineSpacingRule.EXACT);
		    // 居中
		    pw.setParagraphAlignInfo(p, ParagraphAlignment.CENTER,
		        TextAlignment.CENTER);
		    XWPFRun pRun = pw.getOrAddParagraphFirstRun(p, false, false);
		    pw.setParagraphRunFontInfo(p, pRun, route.getTITLE(), "宋体",
		        "Times New Roman", "44", true, false, false, false, null, null,
		        0, 0, 90);

		    p = xdoc.createParagraph();
		    // 固定值25磅
		    pw.setParagraphSpacingInfo(p, true, "0", "80", null, null, true, "500",
		        STLineSpacingRule.EXACT);
		    // 居中
		    pw.setParagraphAlignInfo(p, ParagraphAlignment.RIGHT,
		        TextAlignment.CENTER);
		    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
		    pw.setParagraphRunFontInfo(p, pRun, "编号:"+route.getNO(), "宋体", "Times New Roman",
		        "22", true, false, false, false, null, null, 0, 0, 90);
		    
		    XWPFTable table = xdoc.createTable(3, 2);
		    
		    pw.setTableBorders(table, STBorder.SINGLE, "2", "auto", "0");
		    pw.setTableWidthAndHAlign(table, "9024", STJc.CENTER);
		    pw.setTableCellMargin(table, 0, 80, 0, 80);
		    int[] colWidths = new int[] {2512, 6512};
		    pw.setTableGridCol(table, colWidths);
		    
		    XWPFTableRow row = table.getRow(0);
		    pw.setRowHeight(row, "460", STHeightRule.AT_LEAST);
		    
		    XWPFTableCell cell = row.getCell(0);
		    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
		    pw.setCellShdStyle(cell, true, "FFFFFF", null);
		    p = pw.getCellFirstParagraph(cell);
		    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
		    pw.setParagraphRunFontInfo(p, pRun, "销售须知", "宋体",
		        "Times New Roman", "22", false, false, false, false, null, null,
		        0, 6, 0);
		    
		    CTTcPr tcpr = cell.getCTTc().addNewTcPr(); 
		    CTTblWidth cellw = tcpr.addNewTcW(); 
		    cellw.setType(STTblWidth.DXA); 
		    cellw.setW(BigInteger.valueOf(500)); 
		    
		    cell = row.getCell(1);
		    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
		    pw.setCellShdStyle(cell, true, "FFFFFF", null);
		    p = pw.getCellFirstParagraph(cell);
		    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
		    pw.setParagraphRunFontInfo(p, pRun, route.getNOTICE(), "宋体",
		        "Times New Roman", "22", false, false, false, false, null, null,
		        0, 6, 0);
		    
		    
		    row = table.getRow(1);
		    pw.setRowHeight(row, "460", STHeightRule.AT_LEAST);
		    
		    cell = row.getCell(0);
		    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
		    pw.setCellShdStyle(cell, true, "FFFFFF", null);
		    p = pw.getCellFirstParagraph(cell);
		    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
		    pw.setParagraphRunFontInfo(p, pRun, "产品推荐", "宋体",
		        "Times New Roman", "22", false, false, false, false, null, null,
		        0, 6, 0);
		    
		    cell = row.getCell(1);
		    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
		    pw.setCellShdStyle(cell, true, "FFFFFF", null);
		    p = pw.getCellFirstParagraph(cell);
		    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
		    pw.setParagraphRunFontInfo(p, pRun, route.getFEATURE(), "宋体",
		        "Times New Roman", "22", false, false, false, false, null, null,
		        0, 6, 0);
		    
		    row = table.getRow(2);
		    pw.setRowHeight(row, "460", STHeightRule.AT_LEAST);
		    
		    cell = row.getCell(0);
		    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
		    pw.setCellShdStyle(cell, true, "FFFFFF", null);
		    p = pw.getCellFirstParagraph(cell);
		    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
		    pw.setParagraphRunFontInfo(p, pRun, "产品特色", "宋体",
		        "Times New Roman", "22", false, false, false, false, null, null,
		        0, 6, 0);
		    
		    cell = row.getCell(1);
		    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
		    pw.setCellShdStyle(cell, true, "FFFFFF", null);
		    p = pw.getCellFirstParagraph(cell);
		    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
		    
		    if(CommonUtils.checkString(route.getFEATURE1())){
		    	StyledText.formatText(p, route.getFEATURE1(), xdoc);
		    }
		    
		    List<Map<String, Object>> citys = route.getCitys();
		    
		    pRun.addBreak();
		    
		    int r = 2; //行号
		    for (Map<String, Object> city : citys) {
				r = r + 1;
		    	table.createRow();
		    	row = table.getRow(r);
			    pw.setRowHeight(row, "387", STHeightRule.AT_LEAST);
			    cell = row.getCell(0);
			    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			    pw.setCellShdStyle(cell, true, "FFFFFF", null);
			    p = pw.getCellFirstParagraph(cell);
			    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
			    if(String.valueOf(city.get("STAY_COUNT")).equals("0")){
			    	pw.setParagraphRunFontInfo(p, pRun, (String)city.get("CITY_NAME"), "宋体", "Times New Roman",
					        "22", true, false, false, false, null, null, 0, 6, 0);
			    }else{
			    	pw.setParagraphRunFontInfo(p, pRun, (String)city.get("CITY_NAME")+ " 停留"+city.get("STAY_COUNT")+"天 ", "宋体", "Times New Roman",
					        "22", true, false, false, false, null, null, 0, 6, 0);
			    }
			    pw.mergeCellsHorizontal(table, r, 0, 1);
				
			    
			    List<Map<String, Object>> days = (List<Map<String, Object>>)city.get("days");
			    
			    String begin_city = "", tool = "", end_city = "", tool1 = "", end_city1 = "", tip = "", day_title = "",
			    		breakfast = "", lunch = "", dinner = "";
			    
			    if(CommonUtils.checkList(days)){
			    	for (Map<String, Object> day : days) {
			    		r = r + 1;
				    	table.createRow();
				    	row = table.getRow(r);
					    pw.setRowHeight(row, "387", STHeightRule.AT_LEAST);
					    cell = row.getCell(0);
					    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
					    pw.setCellShdStyle(cell, true, "FFFFFF", null);
					    p = pw.getCellFirstParagraph(cell);
					    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
					    day_title = (String)day.get("TITLE");
					    if(CommonUtils.checkString(day_title)){
					    	pw.setParagraphRunFontInfo(p, pRun, "第"+day.get("NO")+"天 "+day_title, "宋体", "Times New Roman",
						    		"22", true, false, false, false, null, null, 0, 6, 0);
					    }else{
					    	day_title = "";
					    	begin_city = (String)day.get("BEGIN_CITY");
					    	tool = (String)day.get("TOOL");
					    	end_city = (String)day.get("END_CITY");
					    	tool1 = (String)day.get("TOOL1");
					    	end_city1 = (String)day.get("END_CITY1");
					    	if(CommonUtils.checkString(begin_city)){
					    		day_title += begin_city;
					    	}
					    	if(CommonUtils.checkString(tool)){
					    		day_title += "--"+tool+"--";
					    	}
					    	if(CommonUtils.checkString(end_city)){
					    		day_title += end_city;
					    	}
					    	if(CommonUtils.checkString(tool1)){
					    		day_title += "--"+tool1+"--";
					    	}
					    	if(CommonUtils.checkString(end_city1)){
					    		day_title += end_city1;
					    	}
					    	pw.setParagraphRunFontInfo(p, pRun, "第"+day.get("NO")+"天 " + day_title, "宋体", "Times New Roman",
						    		"22", true, false, false, false, null, null, 0, 6, 0);
					    }
					    pw.mergeCellsHorizontal(table, r, 0, 1);
					    
					    r = r + 1;
				    	table.createRow();
				    	row = table.getRow(r);
					    pw.setRowHeight(row, "460", STHeightRule.AT_LEAST);
					    
					    cell = row.getCell(0);
					    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
					    pw.setCellShdStyle(cell, true, "FFFFFF", null);
					    p = pw.getCellFirstParagraph(cell);
					    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
					    pw.setParagraphRunFontInfo(p, pRun, "当日提示", "宋体",
					        "Times New Roman", "22", false, false, false, false, null, null,
					        0, 6, 0);
					    
					    cell = row.getCell(1);
					    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
					    pw.setCellShdStyle(cell, true, "FFFFFF", null);
					    p = pw.getCellFirstParagraph(cell);
					    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
					    pw.setParagraphRunFontInfo(p, pRun, (String)day.get("TODAY_TIPS"), "宋体",
					        "Times New Roman", "22", false, false, false, false, null, null,
					        0, 6, 0);
					    
					    
					    
					    r = r + 1;
				    	table.createRow();
				    	row = table.getRow(r);
					    pw.setRowHeight(row, "460", STHeightRule.AT_LEAST);
					    
					    cell = row.getCell(0);
					    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
					    pw.setCellShdStyle(cell, true, "FFFFFF", null);
					    p = pw.getCellFirstParagraph(cell);
					    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
					    pw.setParagraphRunFontInfo(p, pRun, "住宿提示", "宋体",
					        "Times New Roman", "22", false, false, false, false, null, null,
					        0, 6, 0);
					    
					    cell = row.getCell(1);
					    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
					    pw.setCellShdStyle(cell, true, "FFFFFF", null);
					    p = pw.getCellFirstParagraph(cell);
					    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
					    pw.setParagraphRunFontInfo(p, pRun, (String)day.get("HOTEL_TIPS"), "宋体",
					        "Times New Roman", "22", false, false, false, false, null, null,
					        0, 6, 0);
					    
					    r = r + 1;
				    	table.createRow();
				    	row = table.getRow(r);
					    pw.setRowHeight(row, "460", STHeightRule.AT_LEAST);
					    
					    cell = row.getCell(0);
					    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
					    pw.setCellShdStyle(cell, true, "FFFFFF", null);
					    p = pw.getCellFirstParagraph(cell);
					    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
					    pw.setParagraphRunFontInfo(p, pRun, "自费提示", "宋体",
					        "Times New Roman", "22", false, false, false, false, null, null,
					        0, 6, 0);
					    
					    cell = row.getCell(1);
					    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
					    pw.setCellShdStyle(cell, true, "FFFFFF", null);
					    p = pw.getCellFirstParagraph(cell);
					    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
					    pw.setParagraphRunFontInfo(p, pRun, (String)day.get("PAY_TIPS"), "宋体",
					        "Times New Roman", "22", false, false, false, false, null, null,
					        0, 6, 0);
					    
					    r = r + 1;
				    	table.createRow();
				    	row = table.getRow(r);
					    pw.setRowHeight(row, "460", STHeightRule.AT_LEAST);
					    
					    cell = row.getCell(0);
					    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
					    pw.setCellShdStyle(cell, true, "FFFFFF", null);
					    p = pw.getCellFirstParagraph(cell);
					    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
					    pw.setParagraphRunFontInfo(p, pRun, "三餐", "宋体",
					        "Times New Roman", "22", false, false, false, false, null, null,
					        0, 6, 0);
					    
					    
					    
					    breakfast = (String)day.get("BREAKFAST");
					    lunch = (String)day.get("LUNCH");
					    dinner = (String)day.get("DINNER");
					    tip = "早餐: ";
					    if(CommonUtils.checkString(breakfast)){
					    	tip += breakfast+"        ";
					    }else{
					    	tip += "无        ";
					    }
					    
					    tip += "中餐: ";
					    if(CommonUtils.checkString(lunch)){
					    	tip += lunch+"        ";
					    }else{
					    	tip += "无        ";
					    }
					    
					    tip += "晚餐: ";
					    if(CommonUtils.checkString(dinner)){
					    	tip += dinner+"        ";
					    }else{
					    	tip += "无        ";
					    }
					    
					    cell = row.getCell(1);
					    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
					    pw.setCellShdStyle(cell, true, "FFFFFF", null);
					    p = pw.getCellFirstParagraph(cell);
					    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
					    pw.setParagraphRunFontInfo(p, pRun, tip, "宋体",
					        "Times New Roman", "22", false, false, false, false, null, null,
					        0, 6, 0);
					    
					    
					    
					    List<RouteDayDetail> details = (List<RouteDayDetail>)day.get("details");
					    for (RouteDayDetail detail : details) {
							
					    	r = r + 1;
					    	table.createRow();
					    	row = table.getRow(r);
						    pw.setRowHeight(row, "460", STHeightRule.AT_LEAST);
						    
						    cell = row.getCell(0);
						    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
						    pw.setCellShdStyle(cell, true, "FFFFFF", null);
						    p = pw.getCellFirstParagraph(cell);
						    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
						    pw.setParagraphRunFontInfo(p, pRun, detail.getTITLE(), "宋体",
						        "Times New Roman", "22", false, false, false, false, null, null,
						        0, 6, 0);
						    
						    cell = row.getCell(1);
						    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
						    pw.setCellShdStyle(cell, true, "FFFFFF", null);
						    p = pw.getCellFirstParagraph(cell);
						    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
						    
						    if(CommonUtils.checkString(detail.getCONTENT())){
						    	StyledText.formatText(p, detail.getCONTENT(), xdoc);
						    }
						}
					    
					}
			    }
			}
		    
//		    费用包含/不含 出行须知 notice  温馨提示 tips
		    
		    r = r + 1;
	    	table.createRow();
	    	row = table.getRow(r);
		    pw.setRowHeight(row, "387", STHeightRule.AT_LEAST);
		    cell = row.getCell(0);
		    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
		    pw.setCellShdStyle(cell, true, "FFFFFF", null);
		    p = pw.getCellFirstParagraph(cell);
		    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
	    	pw.setParagraphRunFontInfo(p, pRun, "费用包含", "宋体", "Times New Roman",
			        "22", true, false, false, false, null, null, 0, 6, 0);
		    pw.mergeCellsHorizontal(table, r, 0, 1);
		    
		    List<Map<String, Object>> includes = route.getInclude();
		    for (Map<String, Object> include : includes) {
		    	
		    	r = r + 1;
		    	table.createRow();
		    	row = table.getRow(r);
			    pw.setRowHeight(row, "460", STHeightRule.AT_LEAST);
			    
			    cell = row.getCell(0);
			    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			    pw.setCellShdStyle(cell, true, "FFFFFF", null);
			    p = pw.getCellFirstParagraph(cell);
			    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
			    pw.setParagraphRunFontInfo(p, pRun, (String)include.get("TITLE"), "宋体",
			        "Times New Roman", "22", false, false, false, false, null, null,
			        0, 6, 0);
			    
			    cell = row.getCell(1);
			    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			    pw.setCellShdStyle(cell, true, "FFFFFF", null);
			    p = pw.getCellFirstParagraph(cell);
			    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
			    pw.setParagraphRunFontInfo(p, pRun, (String)include.get("CONTENT"), "宋体",
			        "Times New Roman", "22", false, false, false, false, null, null,
			        0, 6, 0);
			    
			}
		    
		    r = r + 1;
	    	table.createRow();
	    	row = table.getRow(r);
		    pw.setRowHeight(row, "387", STHeightRule.AT_LEAST);
		    cell = row.getCell(0);
		    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
		    pw.setCellShdStyle(cell, true, "FFFFFF", null);
		    p = pw.getCellFirstParagraph(cell);
		    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
	    	pw.setParagraphRunFontInfo(p, pRun, "费用不含", "宋体", "Times New Roman",
			        "22", true, false, false, false, null, null, 0, 6, 0);
		    pw.mergeCellsHorizontal(table, r, 0, 1);
		    
		    List<Map<String, Object>> nocludes = route.getNoclude();
		    for (Map<String, Object> noclude : nocludes) {
		    	
		    	r = r + 1;
		    	table.createRow();
		    	row = table.getRow(r);
			    pw.setRowHeight(row, "460", STHeightRule.AT_LEAST);
			    
			    cell = row.getCell(0);
			    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			    pw.setCellShdStyle(cell, true, "FFFFFF", null);
			    p = pw.getCellFirstParagraph(cell);
			    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
			    pw.setParagraphRunFontInfo(p, pRun, (String)noclude.get("TITLE"), "宋体",
			        "Times New Roman", "22", false, false, false, false, null, null,
			        0, 6, 0);
			    
			    cell = row.getCell(1);
			    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			    pw.setCellShdStyle(cell, true, "FFFFFF", null);
			    p = pw.getCellFirstParagraph(cell);
			    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
			    pw.setParagraphRunFontInfo(p, pRun, (String)noclude.get("CONTENT"), "宋体",
			        "Times New Roman", "22", false, false, false, false, null, null,
			        0, 6, 0);
			    
			}

		    r = r + 1;
	    	table.createRow();
	    	row = table.getRow(r);
		    pw.setRowHeight(row, "387", STHeightRule.AT_LEAST);
		    cell = row.getCell(0);
		    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
		    pw.setCellShdStyle(cell, true, "FFFFFF", null);
		    p = pw.getCellFirstParagraph(cell);
		    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
	    	pw.setParagraphRunFontInfo(p, pRun, "出行须知", "宋体", "Times New Roman",
			        "22", true, false, false, false, null, null, 0, 6, 0);
		    pw.mergeCellsHorizontal(table, r, 0, 1);
		    List<Map<String, Object>> notices = route.getNotice();
		    for (Map<String, Object> notice : notices) {
		    	
		    	r = r + 1;
		    	table.createRow();
		    	row = table.getRow(r);
			    pw.setRowHeight(row, "460", STHeightRule.AT_LEAST);
			    
			    cell = row.getCell(0);
			    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			    pw.setCellShdStyle(cell, true, "FFFFFF", null);
			    p = pw.getCellFirstParagraph(cell);
			    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
			    pw.setParagraphRunFontInfo(p, pRun, (String)notice.get("TITLE"), "宋体",
			        "Times New Roman", "22", false, false, false, false, null, null,
			        0, 6, 0);
			    
			    cell = row.getCell(1);
			    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			    pw.setCellShdStyle(cell, true, "FFFFFF", null);
			    p = pw.getCellFirstParagraph(cell);
			    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
			    pw.setParagraphRunFontInfo(p, pRun, (String)notice.get("CONTENT"), "宋体",
			        "Times New Roman", "22", false, false, false, false, null, null,
			        0, 6, 0);
			    
			}
		    
		    r = r + 1;
	    	table.createRow();
	    	row = table.getRow(r);
		    pw.setRowHeight(row, "387", STHeightRule.AT_LEAST);
		    cell = row.getCell(0);
		    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
		    pw.setCellShdStyle(cell, true, "FFFFFF", null);
		    p = pw.getCellFirstParagraph(cell);
		    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
	    	pw.setParagraphRunFontInfo(p, pRun, "温馨提示", "宋体", "Times New Roman",
			        "22", true, false, false, false, null, null, 0, 6, 0);
		    pw.mergeCellsHorizontal(table, r, 0, 1);
		    List<Map<String, Object>> tips = route.getTips();
		    for (Map<String, Object> tip : tips) {
		    	
		    	r = r + 1;
		    	table.createRow();
		    	row = table.getRow(r);
			    pw.setRowHeight(row, "460", STHeightRule.AT_LEAST);
			    
			    cell = row.getCell(0);
			    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			    pw.setCellShdStyle(cell, true, "FFFFFF", null);
			    p = pw.getCellFirstParagraph(cell);
			    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
			    pw.setParagraphRunFontInfo(p, pRun, (String)tip.get("TITLE"), "宋体",
			        "Times New Roman", "22", false, false, false, false, null, null,
			        0, 6, 0);
			    
			    cell = row.getCell(1);
			    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			    pw.setCellShdStyle(cell, true, "FFFFFF", null);
			    p = pw.getCellFirstParagraph(cell);
			    pRun = pw.getOrAddParagraphFirstRun(p, false, false);
			    pw.setParagraphRunFontInfo(p, pRun, (String)tip.get("CONTENT"), "宋体",
			        "Times New Roman", "22", false, false, false, false, null, null,
			        0, 6, 0);
			    
			}
		    
		    //------------------------------------------背景图片-----------------------------------------------------------------------
//		    r = r + 1;
//	    	table.createRow();
//	    	row = table.getRow(r);
//		    pw.setRowHeight(row, "387", STHeightRule.AT_LEAST);
//		    cell = row.getCell(1);
//		    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
//		    pw.setCellShdStyle(cell, true, "FFFFFF", null);
//		    p = pw.getCellFirstParagraph(cell);
//		    pRun = p.createRun();
//		    String imgFile1 = "D:\\1.jpg";
//		    String imgFile2 = "D:\\2.jpg";
//		    pRun.addPicture(new FileInputStream(imgFile1), XWPFDocument.PICTURE_TYPE_JPEG, imgFile1, Units.toEMU(200), Units.toEMU(200));
//		    pRun.addPicture(new FileInputStream(imgFile2), XWPFDocument.PICTURE_TYPE_JPEG, imgFile2, Units.toEMU(200), Units.toEMU(200));
		    //------------------------------------------背景图片-----------------------------------------------------------------------		    
		    
		    
		    response.setContentType("application/vnd.ms-word");
	        response.setHeader("Content-disposition", "attachment;filename="+route.getNO()+".docx");    
		    ServletOutputStream sos = response.getOutputStream();
		    xdoc.write(sos);    
		    sos.flush();    
		    sos.close();
		} catch (Exception e) {
			log.error("查询线路详情异常", e);
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, String passed){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
//			String trafficStatus = request.getParameter("trafficStatus");
//			mr.pm.put("existDate", trafficStatus);
			if(CommonUtils.checkString(passed)){
				mr.pm.put("passed", passed);
			}else{
				mr.pm.put("passed", 0);
			}
			mr.pm.put("USER_ID", (String)user.get("ID"));
			mr.pm.put("MANAGER_SITE", this.companyService.listSiteInfoService(mr.pm));
			mr.pm.put("supply-power", "depart");
			PowerFactory.getPower(request, response, "", "route-list", mr.pm);
			mr.pm.remove("CITY_ID");
			List<Map<String, Object>> data = this.routeService.listService(mr.pm);
			int totalSize = this.routeService.countService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路信息异常");
		}
		return json;
	}
	
	@RequestMapping("/save")
	public ListRange save(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			//得到当前用户名和ID
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			//线路名称不能重复
			String ID = (String) mr.pm.get("ID");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
			String title = mr.pm.get("TITLE").toString();
			title = title.replaceAll("\"", "“");
			title = title.replaceAll("'", "‘");
			mr.pm.put("TITLE", title);
			params.put("TITLE", mr.pm.get("TITLE"));
			params.put("notEqualID", ID);
			List<Map<String, Object>> routes = this.routeService.routeTitleISExistService(params);
			if(routes != null && routes.size() > 0){
				json.setStatusCode("-1");
				json.setSuccess(false);
				return json;
			}
			mr.pm.put("UPDATE_USER", user.get("USER_NAME"));
			mr.pm.put("UPDATE_USER_ID", user.get("ID"));
			mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
			mr.pm.put("COMPANY_NAME", (String)user.get("COMPANY"));
			mr.pm.put("PORDUCE_CONCAT", (String)user.get("USER_NAME"));
			mr.pm.put("PRODUCE_MOBILE", (String)user.get("MOBILE"));
			
			mr.pm.put("CITY_ID", (String)user.get("CITY_ID"));
			mr.pm.put("CITY_NAME", (String)user.get("CITY_NAME"));
			
			String[] BEGIN_CITY = request.getParameterValues("BEGIN_CITY");
			String[] END_CITY = request.getParameterValues("END_CITY");
			mr.pm.put("BEGIN_CITY", BEGIN_CITY);
			mr.pm.put("END_CITY", END_CITY);
			
			String[] TAG_NAME_ZT = request.getParameterValues("TAG_NAME_ZT");
			mr.pm.put("TAG_NAME_ZT", TAG_NAME_ZT);
			
			Map<String, Object> result = null;
			String NOTICE = (String)mr.pm.get("NOTICE");
			if(CommonUtils.checkString(NOTICE)){
				NOTICE = NOTICE.replaceAll("\"", "“").replaceAll("'", "‘");
				mr.pm.put("NOTICE", NOTICE);
			}
			
			String FEATURE = (String)mr.pm.get("FEATURE");
			if(CommonUtils.checkString(FEATURE)){
				FEATURE = FEATURE.replaceAll("\"", "“").replaceAll("'", "‘");
				mr.pm.put("FEATURE", FEATURE);
			}
			
			if(!CommonUtils.checkString(ID)){
				ID = CommonUtils.uuid();
				mr.pm.put("CREATE_USER", user.get("USER_NAME"));
				mr.pm.put("CREATE_USER_ID", user.get("ID"));
				mr.pm.put("ID", ID);
				result = this.routeService.saveService(mr.pm);
			}else{
				result = this.routeService.updateService(mr.pm);
			}
			json.setMessage(ID);
			json.setSuccess((Boolean)result.get("success"));
		} catch (Exception e) {
			log.error("保存线路异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存线路异常");
		}
		return json;
	}
	
	@RequestMapping("/listRouteCitys")
	public ListRange listRouteCitys(HttpServletRequest request, HttpServletResponse response, MapRange mr, String routeId){
		ListRange json = new ListRange();
		try {
			List<Map<String, Object>> endCitys = new ArrayList<Map<String, Object>>();
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("TYPE", 2);
			endCitys = this.routeService.listRouteCityService(mr.pm);
			if(CommonUtils.checkList(endCitys)){
				for (Map<String, Object> endCity: endCitys) {
					mr.pm.clear();
					mr.pm.put("ROUTE_ID", routeId);
					mr.pm.put("ROUTE_CITY_ID", (String)endCity.get("ID"));
					endCity.put("text", endCity.get("CITY_NAME"));
					List<Map<String, Object>> routeDays = this.routeService.listRouteDayService(mr.pm);
					endCity.put("days", routeDays);
				}
				mr.pm.put("ROUTE_ID", routeId);
				mr.pm.put("TYPE", 1);
				List<Map<String, Object>> startCitys = this.routeService.listRouteCityService(mr.pm);
				Map<String, Object> _citys = new HashMap<String, Object>();
				for (Map<String, Object> startCity : startCitys) {
					_citys.put("text", startCity.get("CITY_NAME"));
					for (Map<String, Object> _startCity : startCitys) {
						_startCity.put("text", _startCity.get("CITY_NAME"));
					}
					_citys.put("menu", startCitys);
					endCitys.add(0, _citys);
					break;
				}
			}
			json.setData(endCitys);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路城市异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路城市异常");
		}
		return json;
	}
	
	@RequestMapping("/list/city")
	public ListRange listRouteCity(HttpServletRequest request, HttpServletResponse response, MapRange mr, String routeId){
		ListRange json = new ListRange();
		try {
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("TYPE", 1);
			List<Map<String, Object>> citys = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> startCitys = this.routeService.listRouteCityService(mr.pm);
			int num = 0;
			for (Map<String, Object> startCity : startCitys) {
				citys.add(num++, startCity);
			}
			
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("TYPE", 2);
			List<Map<String, Object>> endCitys = this.routeService.listRouteCityService(mr.pm);
			for (Map<String, Object> endCity : endCitys) {
				citys.add(num++, endCity);
			}
			json.setData(citys);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路城市异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路城市异常");
		}
		return json;
	}
	
	@RequestMapping("/routeDayDetail")
	public Map<String, Object> routeDayDetail(HttpServletRequest request, HttpServletResponse response, MapRange mr, String dayId){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			mr.pm.put("ID", dayId);
			result = this.routeService.routeDayInfoService(mr.pm);
			result.put("success", true);
		} catch (Exception e) {
			log.error("查询线路行程详情异常",e);
			result.put("success", false);
		}
		return result;
	}
	
	@RequestMapping("/saveRouteDayID")
	public ListRange saveRouteDayID(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("ID", CommonUtils.uuid());
			this.routeService.saveService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存线路行程异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存线路行程异常");
		}
		return json;
	}
	
	@RequestMapping("/saveRouteDay")
	public ListRange saveRouteDay(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			List<Map<String, Object>> routeDays = this.routeService.listRouteDayService(mr.pm);
			if(routeDays != null && routeDays.size() > 0){
				json.setStatusCode("-1");//编号重复
				json.setSuccess(false);
				return json;
			}
			String ID = CommonUtils.uuid();
			mr.pm.put("ID", ID);
			this.routeService.saveRouteDayService(mr.pm);
			json.setMessage(ID);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存线路行程异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存线路行程异常");
		}
		return json;
	}
	
	@RequestMapping("/updateRouteDay")
	public ListRange updateRouteDay(HttpServletRequest request,HttpServletResponse response, MapRange mr, RouteDayDetailList lp){
		ListRange json = new ListRange();
		try {
			mr.pm.put("lp", lp);
			this.routeService.updateRouteDayService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存线路行程异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存线路行程异常");
		}
		return json;
	}
	
	@RequestMapping("/delRouteDay")
	public ListRange delRouteDay(HttpServletRequest request,HttpServletResponse response){
		ListRange json = new ListRange();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("IS_DEL", "1");
			params.put("ID", (String)request.getParameter("dayId"));
			params.put("ROUTE_ID", (String)request.getParameter("routeId"));
			params.put("NO", (String)request.getParameter("no"));
			this.routeService.delRouteDayService(params);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除线路行程异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/moveRouteDay")
	public ListRange moveRouteDay(HttpServletRequest request,HttpServletResponse response){
		ListRange json = new ListRange();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ID", (String)request.getParameter("dayId"));
			params.put("ROUTE_ID", (String)request.getParameter("routeId"));
			params.put("ROUTE_CITY_ID", (String)request.getParameter("routeCityId"));
			params.put("TARGET_ID", (String)request.getParameter("targetId"));
			this.routeService.moveRouteDayService(params);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除线路行程异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	/**
	 * 保存线路行程其他信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/saveRouteInfo")
	public ListRange saveRouteInfo(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("UPDATE_USER", user.get("USER_NAME"));
			mr.pm.put("UPDATE_USER_ID", user.get("ID"));
			this.routeService.saveRouteInfoService(mr.pm);
			json.setMessage(routeId);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存线路行程其他信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	
	/**
	 * 线路发布
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/pub")
	public ListRange pub(HttpServletRequest request,HttpServletResponse response, MapRange mr, String models, String isPub, String routeId, String isShare){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("ROUTE_ID", routeId);
			if(isPub.equals("2")){
				
				JSONArray jarray = JSONArray.fromObject(models);
				Object[] objArray = jarray.toArray();
				for(int i=0;i<objArray.length;i++){
					JSONObject jobject = JSONObject.fromObject(objArray[i]);
					routeId = (String)jobject.get("ID");
					mr.pm.put("ROUTE_ID", routeId);
					break;
				}
			}
			if(CommonUtils.checkString(routeId)){
				List<RouteDayDetail> routeDayDetais = this.routeService.listRouteDayDetaiService(mr.pm);
				if(CommonUtils.checkList(routeDayDetais)){
					mr.pm.remove("ROUTE_ID");
					mr.pm.put("ID", routeId);
					mr.pm.put("UPDATE_USER", (String)user.get("USER_NAME"));
					mr.pm.put("UPDATE_USER_ID", (String)user.get("ID"));
					mr.pm.put("IS_PUB", isPub);
					mr.pm.put("models", models);
					mr.pm.put("IS_SHARE", isShare);
					this.routeService.pubeRouteService(mr.pm);
					this.routeService.route_calendar(routeId);
					json.setSuccess(true);
				}else{
					json.setStatusCode("-1");
					json.setSuccess(false);
				}
			}else{
				json.setStatusCode("-1");
				json.setSuccess(false);
			}
			
		} catch (Exception e) {
			log.error("线路发布异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/single/pub")
	public ListRange singlePub(HttpServletRequest request,HttpServletResponse response,String models, String SINGLE_REMARK){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("UPDATE_USER", (String)user.get("USER_NAME"));
				params.put("UPDATE_USER_ID", (String)user.get("ID"));
				params.put("ID", jobject.getString("ID"));
				
				String IS_SINGLE_PUB = jobject.getString("IS_SINGLE_PUB");
				if(IS_SINGLE_PUB.equals("0") || IS_SINGLE_PUB.equals("2")){
					params.put("IS_SINGLE_PUB", 1);
				}else{
					params.put("IS_SINGLE_PUB", 2);
				}
				params.put("SINGLE_REMARK", SINGLE_REMARK);
				this.routeService.updateRouteService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("单卖线路异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	/**
	 * 线路删除
	 * @param request
	 * @param response
	 * @return
	 */
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
				params.put("UPDATE_USER", (String)user.get("USER_NAME"));
				params.put("UPDATE_USER_ID", (String)user.get("ID"));
				params.put("ID", jobject.getString("ID"));
				params.put("IS_DEL", 1);
				this.routeService.delRouteService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("线路删除异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	/**
	 * 线路辅助信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/listRouteOther")
	public ListRange listRouteOther(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId, String type){
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("TYPE", type);
			this.routeService.pubeRouteService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路辅助信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/saveRouteOther")
	public ListRange saveRouteOther(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId,
			RouteDayDetailList infos){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			mr.pm.put("routeId", routeId);
			mr.pm.put("infos", infos);
			mr.pm.put("user", user);
			this.routeService.saveRouteOtherService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存线路辅助信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/detail")
	public RouteEntity detail(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId){
		RouteEntity route = null;
		
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		try {
			mr.pm.put("ID", routeId);
			route = this.routeService.routeDetailService(mr.pm);
			if(route != null){
				/**
				 * 线路行程
				 */
				
				if(CommonUtils.checkString(user.get("SHORT_NAME"))){
					route.setShortName((String)user.get("SHORT_NAME"));
				}
				
				if(CommonUtils.checkString(user.get("ADDRESS"))){
					route.setAddress((String)user.get("ADDRESS"));
				}
				
				if(CommonUtils.checkString(user.get("PARENT_COMPANY"))){
					route.setParentCompany((String)user.get("PARENT_COMPANY"));
				}else{
					route.setParentCompany((String)user.get("COMPANY"));
				}
				
				if(CommonUtils.checkString(user.get("CHINA_NAME"))){
					route.setChinaName((String)user.get("CHINA_NAME"));
				}
				
				if(CommonUtils.checkString(user.get("MOBILE"))){
					route.setUserPhone((String)user.get("MOBILE"));
				}
				
				mr.pm.clear();
				mr.pm.put("ROUTE_ID", routeId);
				mr.pm.put("TYPE", 1);
				List<Map<String, Object>> include = this.routeService.listRouteOtherService(mr.pm);
				route.setInclude(include);
				
				mr.pm.put("TYPE", 2);
				List<Map<String, Object>> noclude = this.routeService.listRouteOtherService(mr.pm);
				route.setNoclude(noclude);
				
				mr.pm.put("TYPE", 3);
				List<Map<String, Object>> notice = this.routeService.listRouteOtherService(mr.pm);
				route.setNotice(notice);
				
				mr.pm.put("TYPE", 4);
				List<Map<String, Object>> tips = this.routeService.listRouteOtherService(mr.pm);
				route.setTips(tips);
				
				List<Map<String, Object>> allCity = new ArrayList<Map<String, Object>>();
				
				mr.pm.remove("TYPE");
				String city_name = "";
				Map<String, Object> start_city = null;
				List<Map<String, Object>> citys_temp = new ArrayList<Map<String, Object>>();
				
				List<Map<String, Object>> citys = this.routeService.listRouteCityService(mr.pm);
				for (Map<String, Object> city : citys) {
					String ROUTE_CITY_ID = (String)city.get("ID");
					mr.pm.put("ROUTE_CITY_ID", ROUTE_CITY_ID);
					
					if(!city.get("TYPE").toString().equals("1")){
						List<Map<String, Object>> days = this.routeService.listRouteDayService(mr.pm);
						for (Map<String, Object> day : days) {
							String DAY_ID = (String)day.get("ID");
							mr.pm.clear();
							mr.pm.put("DAY_ID", DAY_ID);
							List<RouteDayDetail> details = this.routeService.listRouteDayDetaiService(mr.pm);
							for (RouteDayDetail detail : details) {
								String DETAIL_ID = detail.getID();
								mr.pm.clear();
								mr.pm.put("DAY_DETAIL_ID", DETAIL_ID);
								List<Map<String, Object>> scenics = this.routeService.listRouteScenicService(mr.pm);
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
				
				if(route.getIS_FULL_PRICE().equals("1")){
					mr.pm.clear();
					mr.pm.put("ROUTE_ID", routeId);
					List<Map<String, Object>> trafficDetails = this.routeService.listRouteTrafficDetailService(mr.pm);
					route.setTRAFFIC_ID((String)trafficDetails.get(0).get("TRAFFIC_ID"));
				}
				route.setCitys(citys);
			}
		} catch (Exception e) {
			log.error("查询线路详情异常", e);
		}
		return route;
	}
	
	@RequestMapping("/info")
	public RouteEntity info(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId){
		RouteEntity route = null;
		try {
			mr.pm.put("ID", routeId);
			route = this.routeService.routeDetailService(mr.pm);
			if(route != null){
				mr.pm.clear();
				mr.pm.put("ROUTE_ID", routeId);
				mr.pm.put("TYPE", 1);
				List<Map<String, Object>> begin = this.routeService.listRouteCityService(mr.pm);
				route.setBegin(begin);

				mr.pm.put("TYPE", 2);
				List<Map<String, Object>> end = this.routeService.listRouteCityService(mr.pm);
				route.setEnd(end);
				
				mr.pm.clear();
				mr.pm.put("ROUTE_ID", routeId);
				mr.pm.put("TAG_TYPE", 1);
				List<String> zt = this.routeService.listRouteTagService(mr.pm);
				route.setZt(zt);
				
				if(CommonUtils.checkString(route.getIS_FULL_PRICE()) && route.getIS_FULL_PRICE().equals("1")){
					mr.pm.clear();
					mr.pm.put("ROUTE_ID", routeId);
					List<Map<String, Object>> data = this.routeService.listRouteTrafficDetailService(mr.pm);
					route.setTRAFFIC_END_DATE((String)data.get(0).get("END_DATE"));
					route.setTRAFFIC_END_TIME((String)data.get(0).get("END_TIME"));
				}
				
			}
		} catch (Exception e) {
			log.error("查询线路信息异常",e);
		}
		return route;
	}
	
	@RequestMapping("/others")
	public ListRange others(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId, String type){
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("TYPE", type);
			List<Map<String, Object>> others = new ArrayList<Map<String, Object>>();
			if(CommonUtils.checkString(routeId) && CommonUtils.checkString(type)){
				others = this.routeService.listRouteOtherService(mr.pm);
			}
			json.setData(others);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路辅助异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/atts")
	public ListRange atts(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId){
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("ROUTE_ID", routeId);
			List<Map<String, Object>> album = new ArrayList<Map<String, Object>>();
			if(CommonUtils.checkString(routeId)){
				album = this.routeService.listRouteAlbumService(mr.pm);
				if(CommonUtils.checkList(album)){
					for (Map<String, Object> att : album) {
						att.put("id", att.get("ID"));
						att.put("src", att.get("IMG_PATH"));
						att.put("loaded", "");
						att.put("name", "");
						att.put("size", "0");
						att.put("percent", "0");
						att.put("status", "4");
						att.put("msg", "");
						att.put("face", att.get("IS_FACE"));
					}
				}
			}
			json.setData(album);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路相册异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/uploadAtts")
	public ListRange uploadAtts(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId){
		ListRange json = new ListRange();
		try {
			String path = "";
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> atts = multipartRequest.getFiles("file");
			mr.pm.put("ROUTE_ID", routeId);
			if(CommonUtils.checkList(atts)){
				/**
				 * 图片类型是否正确
				 */
				for (MultipartFile att : atts) {
					String file_name = att.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}
					}else{
						log.error("图片类型错误");
						json.setSuccess(false);
						json.setStatusCode("-4");
						json.setMessage("图片类型错误");
						return json;
					}
				}
				for (MultipartFile att : atts) {
					String file_name = att.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						String[] paths = UploadUtil.path("route/album", false);
						String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
						String uuid = CommonUtils.uuid();
						file_name = uuid + file_suffix;
						path = UploadUtil.uploadByte(att.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						UploadUtil.scale(new File(paths[0]+file_name), 640, 360, paths[0], uuid, "640X360");
						UploadUtil.scale(new File(paths[0]+file_name), 500, 280, paths[0], uuid, "500X280");
						UploadUtil.scale(new File(paths[0]+file_name), 60, 60, paths[0], uuid, "60X60");
						mr.pm.put("IMG_PATH", path.replaceAll("\\.", "-500X280."));
						mr.pm.put("ID", CommonUtils.uuid());
						this.routeService.saveRouteAlbumService(mr.pm);
					}
				}
				
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存线路相册异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/delAtts")
	public ListRange delAtts(HttpServletRequest request,HttpServletResponse response, MapRange mr, String ID){
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("ID", ID);
			this.routeService.delRouteAlbumService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除线路相册异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/setFace")
	public ListRange setFace(HttpServletRequest request,HttpServletResponse response, MapRange mr, String ID, String routeId){
		ListRange json = new ListRange();
		try {
			
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.clear();
			mr.pm.put("ID", ID);
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("UPDATE_USER", user.get("USER_NAME"));
			mr.pm.put("UPDATE_USER_ID", user.get("ID"));
			this.routeService.setDefaultFaceService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("设置封面图异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/price/attr")
	public ListRange priceAttr(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			List<Map<String, Object>> attrs = this.trafficService.listPriceAttrService(mr.pm);
			for (Map<String, Object> attr : attrs) {
				String ID = (String)attr.get("ID");
				if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
					mr.pm.put("PID", ID);
					List<Map<String, Object>> childs = this.trafficService.listPriceAttrService(mr.pm);
					attr.put("childs", childs);
				}
			}
			json.setData(attrs);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询价格属性异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询价格属性异常");
		}
		return json;
	}
	
	
	@RequestMapping("/price/type")
	public ListRange priceType(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId){
		ListRange json = new ListRange();
		try {
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			mr.pm.put("ENTITY_ID", routeId);
			List<Map<String, Object>> basePrice = this.trafficService.listBasePriceService(mr.pm);
			if(CommonUtils.checkList(basePrice)){
				List<Map<String, Object>> priceAttrs = this.trafficService.listPriceAttrService(mr.pm);
				StringBuffer sql = new StringBuffer();
				for (Map<String, Object> priceAttr : priceAttrs) {
					String ID = (String)priceAttr.get("ID");
					sql.append(" ,sum(decode(c_order_by,"+priceAttr.get("ORDER_BY").toString()+",price,0)) as "+priceAttr.get("CON_NAME").toString()+" ");
					if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
						mr.pm.put("PID", ID);
						List<Map<String, Object>> childs = this.trafficService.listPriceAttrService(mr.pm);
						for (Map<String, Object> child : childs) {
							sql.append(" ,sum(decode(c_order_by,"+child.get("ORDER_BY").toString()+",price,0)) as "+child.get("CON_NAME").toString()+" ");
						}
					}
					
				}
				mr.pm.remove("PID");
				mr.pm.put("sql", sql.toString());
				data = this.trafficService.listPriceTypeService(mr.pm);
			}else{
				Map<String, Object> retail_data = new HashMap<String, Object>();
				retail_data.put("TITLE", "外卖");
				retail_data.put("ID", "0FA5123749D28C87E050007F0100BCAD");
				retail_data.put("CHENGREN", 0);
				retail_data.put("ERTONG", 0);
				retail_data.put("YINGER", 0);
				retail_data.put("ET1", 0);
				retail_data.put("ET2", 0);
				retail_data.put("ET3", 0);
				retail_data.put("ET4", 0);
				data.add(retail_data);
				
				Map<String, Object> inter_data = new HashMap<String, Object>();
				inter_data.put("TITLE", "同行");
				inter_data.put("ID", "0FA5123749D38C87E050007F0100BCAD");
				inter_data.put("CHENGREN", 0);
				inter_data.put("ERTONG", 0);
				inter_data.put("YINGER", 0);
				inter_data.put("ET1", 0);
				inter_data.put("ET2", 0);
				inter_data.put("ET3", 0);
				inter_data.put("ET4", 0);
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

	
	@RequestMapping("/price/save")
	public ListRange priceSave(HttpServletRequest request,HttpServletResponse response, MapRange mr, String prices, String routeId){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		try {
			mr.pm.put("PRICES", prices);
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("USER_ID", (String)user.get("ID"));
			mr.pm.put("USER_NAME", (String)user.get("USER_NAME"));
			Map<String, Object> result = new HashMap<String, Object>();
			result = this.routeService.savePriceService(mr.pm);
			json.setStatusCode(String.valueOf(result.get("statusCode")));
			json.setSuccess((Boolean)result.get("success"));
		} catch (Exception e) {
			log.error("保存线路价格异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存线路价格异常");
		}
		return json;
	}
	
	@RequestMapping("/update/single")
	public ListRange updateSingle(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId, String retailSingleRoom, String interSingleRoom){
		ListRange json = new ListRange();
		try {
			mr.pm.put("ID", routeId);
			
			if(!CommonUtils.checkString(retailSingleRoom)){
				mr.pm.put("RETAIL_SINGLE_ROOM", 0);
			}else{
				mr.pm.put("RETAIL_SINGLE_ROOM", retailSingleRoom);
			}
			
			if(!CommonUtils.checkString(interSingleRoom)){
				mr.pm.put("INTER_SINGLE_ROOM", 0);
			}else{
				mr.pm.put("INTER_SINGLE_ROOM", interSingleRoom);
			}
			this.routeService.updateSingleRoomService(mr.pm);
		} catch (Exception e) {
			log.error("保存线路单房差异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存线路单房差异常");
		}
		return json;
	}
	@RequestMapping("/calendar")
	public ListRange calendar(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId, String startDate){
		ListRange json = new ListRange();
		try {
			mr.pm.put("ROUTE_ID", routeId);
			if(CommonUtils.checkString(startDate)){
				mr.pm.put("START_DATE", startDate.replaceAll("-", ""));
				mr.pm.put("END_DATE", DateUtil.getMonthLastDay(DateUtil.parseDate(startDate)).replaceAll("-", ""));				
			}
			List<Map<String, Object>> data = this.routeService.calendarService(mr.pm);
			json.setData(data);
			mr.pm.remove("START_DATE");
			mr.pm.remove("END_DATE");
			List<Map<String, Object>> _data = this.routeService.calendarService(mr.pm);
			if(CommonUtils.checkList(_data)){
				json.setTotalSize(_data.size());	
			}else{
				json.setTotalSize(0);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询价格日历异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询价格日历异常");
		}
		return json;
	}
	
	@RequestMapping("/traffic/plan/save")
	public ListRange saveTrafficPlan(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId, String planId){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		try {
			String PLAN_ID = CommonUtils.uuid();
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("ID", PLAN_ID);
			this.routeService.saveRouteTrafficService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存线路交通异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存线路交通异常");
		}
		return json;
	}
	
	@RequestMapping("/list/plan/price")
	public ListRange listPlanPrices(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId, String startDate, String planId, String isSingle){
		ListRange json = new ListRange();
		try {
			if(CommonUtils.checkString(routeId) && CommonUtils.checkString(startDate)){
				/**
				 * 线路价格
				 */
				mr.pm.clear();
				mr.pm.put("ROUTE_ID", routeId);
				mr.pm.put("PLAN_ID", planId);
				mr.pm.put("RQ", startDate);
//				isSingle = "YES";
				List<Map<String, Object>> data = null;
				if(CommonUtils.checkString(isSingle)){
					data = this.routeService.listRoutePriceService(mr.pm);
				}else{
					data = this.routeService.listPlanPricesService(mr.pm);
				}
				json.setData(data);
				json.setSuccess(true);
			}else{
				json.setStatusCode("-1");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("查询方案列表异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询方案列表异常");
		}
		return json;
	}
	
	@RequestMapping("/list/plan")
	public ListRange listPlan(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId, String startDate){
		ListRange json = new ListRange();
		try {
			if(CommonUtils.checkString(routeId) && CommonUtils.checkString(startDate)){
				mr.pm.clear();
				mr.pm.put("ROUTE_ID", routeId);
				mr.pm.put("RQ", startDate);
				List<Map<String, Object>> routeCalendar = this.routeService.listRouteCalendarService(mr.pm);
				String[] PLANIDGROUP = routeCalendar.get(0).get("PLANIDGROUP").toString().split(",");
				StringBuffer PLANIDS = new StringBuffer();
				for (String PLANID : PLANIDGROUP) {
					PLANIDS.append("'"+PLANID+"',");
				}
				mr.pm.clear();
				mr.pm.put("IDS", PLANIDS.toString().subSequence(0, PLANIDS.toString().length()-1));
				List<Map<String, Object>> plans = this.routeService.listRouteTrafficService(mr.pm);
				json.setData(plans);
				json.setSuccess(true);
			}else{
				json.setStatusCode("-1");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("查询方案列表异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询方案列表异常");
		}
		return json;
	}
	
	@RequestMapping("/list/route/traffic/info")
	public ListRange listRouteTrafficInfo(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId, String startDate, String planId){
		ListRange json = new ListRange();
		try {
			if(CommonUtils.checkString(routeId) && CommonUtils.checkString(startDate)){
				mr.pm.clear();
				mr.pm.put("ROUTE_ID", routeId);
				mr.pm.put("RQ", startDate);
				mr.pm.put("PLAN_ID", planId);
//				List<Map<String,String>> plans = new ArrayList<Map<String, String>>();
				List<Map<String,String>> plans = this.routeService.listRouteTrafficInfoService(mr.pm);
				for (Map<String, String> plan : plans) {
					String IS_FULL_PRICE = plan.get("IS_FULL_PRICE");
					if(CommonUtils.checkString(IS_FULL_PRICE) && IS_FULL_PRICE.equals("1")){
						plan.remove("TRAFFIC_NAME");
					}
				}
				json.setData(plans);
				json.setSuccess(true);
			}else{
				json.setStatusCode("-1");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("查询方案列表异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询方案列表异常");
		}
		return json;
	}
	
	@RequestMapping("/traffic/plan/del")
	public ListRange delTrafficPlan(HttpServletRequest request,HttpServletResponse response, MapRange mr, String planId, String routeId){
		ListRange json = new ListRange();
		try {
			
//			mr.pm.put("PLAN_ID", planId);
//			List<Map<String, Object>> data = this.routeService.listRouteTrafficDetailService(mr.pm);
			
			mr.pm.put("ID", planId);
			this.routeService.delRouteTrafficService(mr.pm);
//			if(data.size() > 0){
//				this.routeService.route_calendar(routeId);
//			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除线路交通异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("删除线路交通异常");
		}
		return json;
	}
	
	
	@RequestMapping("/traffic/plan/list")
	public ListRange listTrafficPlan(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId){
		ListRange json = new ListRange();
		try {
			mr.pm.put("ROUTE_ID", routeId);
			List<Map<String, Object>> data = this.routeService.listRouteTrafficService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路交通异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路交通异常");
		}
		return json;
	}
	
	@RequestMapping("/traffic/detail/save")
	public ListRange saveTrafficDetail(HttpServletRequest request,HttpServletResponse response, MapRange mr, String routeId){
		ListRange json = new ListRange();
		try {
			mr.pm.put("ROUTE_ID", routeId);
			mr.pm.put("DETAIL_ID", request.getParameterValues("ID"));//交通ID
			mr.pm.put("TRAFFIC_ID", request.getParameterValues("TRAFFIC_ID"));//交通ID
			mr.pm.put("TRAFFIC_NAME", request.getParameterValues("TRAFFIC_NAME"));//交通名称
			mr.pm.put("BEGIN_CITY_NAME", request.getParameterValues("BEGIN_CITY_NAME"));//出发城市
			mr.pm.put("BEGIN_CITY_ID", request.getParameterValues("BEGIN_CITY_ID"));//出发城市ID
			mr.pm.put("END_CITY_ID", request.getParameterValues("END_CITY_ID"));//目的地城市
			mr.pm.put("END_CITY_NAME", request.getParameterValues("END_CITY_NAME"));//目的地城市ID
			mr.pm.put("STAY_CNT", request.getParameterValues("STAY_CNT"));//停留天数
			mr.pm.put("ORDER_BY", request.getParameterValues("ORDER_BY"));//序号
			String[] ORDER_BY = request.getParameterValues("ORDER_BY");
			String status = "-1";
			Map<String, String> code = new HashMap<String, String>();
			for (int i = 0; i < ORDER_BY.length; i++) {
				if(code.containsKey(ORDER_BY[i])){
					status = "-2";
					json.setSuccess(false);
					json.setStatusCode("-2");//数据不正确
					return json;
				}
				String num = ORDER_BY[i];
				if(num.equals("1")){
					status = "1";
				}
				code.put(ORDER_BY[i], ORDER_BY[i]);
			}
			
			if(status.equals("1")){
				this.routeService.saveRouteTrafficDetailService(mr.pm);
				/**
				int i = this.routeService.saveRouteTrafficDetailService(mr.pm);
				if(i > 0){
					this.routeService.route_calendar(routeId);
				}
				*/
				json.setSuccess(true);				
			}else{
				json.setSuccess(false);
				json.setStatusCode("-1");//数据不正确
			}

		} catch (Exception e) {
			log.error("保存线路交通详情异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存线路交通详情异常");
		}
		return json;
	}
	
	@RequestMapping("/traffic/detail/list")
	public ListRange listTrafficDetail(HttpServletRequest request,HttpServletResponse response, MapRange mr, String planId){
		ListRange json = new ListRange();
		try {
			mr.pm.put("PLAN_ID", planId);
			List<Map<String, Object>> data = this.routeService.listRouteTrafficDetailService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路交通详情异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路交通闲情异常");
		}
		return json;
	}
	
	@RequestMapping("/upload/source")
	public ListRange uploadSource(HttpServletRequest request, HttpServletResponse response, MapRange mr, String routeId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");
			String file_name = file.getOriginalFilename();
			if(CommonUtils.checkString(file_name)){
				String[] paths = UploadUtil.pathAdmin("word");
				String file_suffix = file_name.substring(file_name.lastIndexOf("."), file_name.length());
				file_name = CommonUtils.uuid()+file_suffix;
				try {
					String SOURCE_URL = UploadUtil.uploadByte(file.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
					params.put("ID", routeId);
					params.put("UPDATE_USER", (String)user.get("USER_NAME"));
					params.put("UPDATE_USER_ID", (String)user.get("ID"));
					params.put("SOURCE_URL", SOURCE_URL);
					this.routeService.updateRouteService(params);
				} catch (IOException e) {
					e.printStackTrace();
					log.error("图片上传失败");
					json.setSuccess(false);
					json.setStatusCode("-5");
					json.setMessage("图片上传失败");
					return json;
					
				}
			}
			
			
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存游客信息异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存游客信息异常");
		}
		return json;
	}
	
}
