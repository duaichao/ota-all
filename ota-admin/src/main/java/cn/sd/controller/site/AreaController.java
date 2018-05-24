package cn.sd.controller.site;

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
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.site.IAreaService;

@RestController
@RequestMapping("/site/area")
public class AreaController extends ExtSupportAction {
	private static Log log = LogFactory.getLog(AreaController.class);
	
	@Resource
	private IAreaService areaService;
	
	@RequestMapping("")
	public ModelAndView main(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/site/area");
	}
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response,MapRange mr, String type){
		ListRange json = new ListRange();
		mr.pm.put("TYPE", type);
		List<Map<String,Object>> data = areaService.listService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/save")
	public ListRange save(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("TEXT", mr.pm.get("TEXT"));
			p.put("TYPE", mr.pm.get("TYPE"));
			p.put("unExistID", mr.pm.get("ID"));
			List<Map<String,Object>> data = areaService.listService(p);
			if(!CommonUtils.checkList(data)){
				areaService.saveService(mr.pm);
				json.setSuccess(true);
			}else{
				//相同类型的名称不能重复,-1区域名称已存在
				json.setStatusCode("-1");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("添加区域异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加区域异常");
		}
		return json;
	}
	
	@RequestMapping("/del")
	public ListRange del(HttpServletRequest request,HttpServletResponse response,String models){
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				int status = areaService.delService(jobject.getString("ID"));
				if(status == 0){
					json.setSuccess(true);
				}else if(status == -1){
					json.setStatusCode("-1");
					json.setSuccess(false);
				}
			}
			
		} catch (Exception e) {
			log.error("删除区域异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/listCityLabel")
	public ListRange listCityLabel(HttpServletRequest request,HttpServletResponse response, MapRange mr, String labelID, String type){
		ListRange json = new ListRange();
		mr.pm.put("LABEL_ID", labelID);
		mr.pm.put("TYPE", type);
		List<Map<String,Object>> data = areaService.listCityLabelService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/saveCityLabel")
	public ListRange saveCityLabel(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			List<Map<String,Object>> data = areaService.listCityLabelService(mr.pm);
			if(!CommonUtils.checkList(data)){
				mr.pm.put("ID", CommonUtils.uuid());
				areaService.saveCityLabelService(mr.pm);
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
				json.setStatusCode("-1");
				json.setMessage((String)mr.pm.get("ENTITY_NAME"));
			}
		} catch (Exception e) {
			log.error("添加异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加异常");
		}
		return json;
	}
	
	@RequestMapping("/delCityLabel")
	public ListRange delCityLabel(HttpServletRequest request,HttpServletResponse response, String models, String ENTITY_ID, String LABEL_ID){
		ListRange json = new ListRange();
		Map<String, Object> params = new HashMap<String, Object>(); 
		try {
			params.put("ENTITY_ID", ENTITY_ID);
			params.put("LABEL_ID", LABEL_ID);
			areaService.delCityLabelService(params);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/listCityLabelEntityID")
	public List<String> listCityLabelEntityID(HttpServletRequest request,HttpServletResponse response,MapRange mr, String labelID, String type){
		mr.pm.put("LABEL_ID", labelID);
		mr.pm.put("TYPE", type);
		List<String> data = areaService.listCityLabelEntityIDService(mr.pm);
		return data;
	}
}
