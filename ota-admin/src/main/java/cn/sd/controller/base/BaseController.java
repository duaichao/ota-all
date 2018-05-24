package cn.sd.controller.base;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.resource.IRouteService;
import cn.sd.service.site.IAreaService;

@RestController
@Controller("BaseController")
@RequestMapping("/base")
public class BaseController extends ExtSupportAction{

	@Resource
	private IAreaService areaService;
	@Resource
	private IRouteService routeService;
	
	//查询城市的列表
	@RequestMapping("/listCity")
	public ListRange listCity(HttpServletRequest request,HttpServletResponse response,MapRange mr, String PID, String type, String query, String cityId){
		ListRange json = new ListRange();
		mr.pm.put("PID", PID);
		mr.pm.put("CITY_ID", cityId);
		try {
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(CommonUtils.checkString(query)){
			query = query.toLowerCase();
		}
		mr.pm.put("query", query);
		
		int start = toInt(request.getParameter("start"));
		int limit = toInt(request.getParameter("limit"));
		
		mr.pm.put("start", start + 1);
		mr.pm.put("end", start + limit);
		
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		int size = 0;
		if(CommonUtils.checkString(type) && (type.equals("2") || type.equals("1")) || CommonUtils.checkString(cityId)){
			mr.pm.put("NotEqualsPID", "0C215329C56EB3B4E050007F0100F1D4");
			data = areaService.listCityService(mr.pm);
			size = areaService.countCityService(mr.pm);
		}else if(CommonUtils.checkString(type) && type.equals("3")){
			mr.pm.put("IS_USE", "0");
			mr.pm.put("NotEqualsPID", "-1");
			data = areaService.listCountryService(mr.pm);
			size = areaService.countCountryService(mr.pm);
		}else {
			mr.pm.put("NotEqualsPID", "0C215329C56EB3B4E050007F0100F1D4");
			mr.pm.put("IS_USE", "0");
			mr.pm.put("NotCountryEqualsPID", "-1");
			data = areaService.listCityAndCountryService(mr.pm);
			size = areaService.countCityAndCountryService(mr.pm);
		}
		json.setTotalSize(size);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	//查询景点列表
	@RequestMapping("/listScenic")
	public ListRange listScenic(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, String PID){
		ListRange json = new ListRange();
		try {
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("iso-8859-1"), "UTF-8");
				query = query.toLowerCase();
				mr.pm.put("QUERY", query);
			}
			if(CommonUtils.checkString(PID)){
				mr.pm.put("PID", PID);
			}else{
				mr.pm.put("PID", "-1");
			}
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			
			mr.pm.put("start", start + 1);
			mr.pm.put("end", start + limit);
			List<Map<String,Object>> data = this.routeService.listScenicService(mr.pm);
			int size = this.routeService.countScenicService(mr.pm);
			json.setData(data);
			json.setTotalSize(size);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询经典异常");
		}
		return json;
	}
	
}
