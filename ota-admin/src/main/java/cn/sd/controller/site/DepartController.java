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

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.b2b.IUserService;
import cn.sd.service.site.IDepartService;

@RestController
@RequestMapping("/site/depart")
@SuppressWarnings("all")
public class DepartController extends ExtSupportAction {

	private static Log log = LogFactory.getLog(AreaController.class);
	
	@Resource
	private IDepartService departService;
	@Resource
	private IUserService userService;
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response, MapRange mr, String COMPANY_ID){
		ListRange json = new ListRange();
		try {
			//得到当前用户名和ID
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("COMPANY_ID", COMPANY_ID);
			
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start + 1);
			mr.pm.put("end", start + limit);
			
			List<Map<String, Object>> data = this.departService.listService(mr.pm);
			int totalSize = this.departService.countService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询部门信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询部门信息异常");
		}
		return json;
	}
	
	@RequestMapping("/save")
	public ListRange save(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String statusCode = "";
			String ID = (String)mr.pm.get("ID");
			if(!CommonUtils.checkString(ID)){
				mr.pm.put("ORDER_BY", 0);
				statusCode = this.departService.saveService(mr.pm);
			}else{
				statusCode = this.departService.editService(mr.pm);
			}
			if("0".equals(statusCode)){
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
				json.setStatusCode(String.valueOf(statusCode));
			}
		} catch (Exception e) {
			log.error("添加\\或修改公司异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加\\或修改公司异常");
		}
		return json;
	}
	
	
	@RequestMapping("/del")
	public ListRange del(HttpServletRequest request,HttpServletResponse response,String models){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			JSONArray jarray = JSONArray.fromObject(models);
			Map<String,Object> params = new HashMap<String,Object>();
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				String DEPART_ID = jobject.getString("ID");
				params.put("DEPART_ID", DEPART_ID);
				params.put("start", 1);
				params.put("end", 10);
				List<Map<String,Object>> users = this.userService.listService(params);
				if(CommonUtils.checkList(users)){
					json.setStatusCode("-1");
					json.setSuccess(false);
					break;
				}else{
					params.clear();
					params.put("ID", DEPART_ID);
					this.departService.delService(params);
					json.setSuccess(true);
				}
			}
		} catch (Exception e) {
			log.error("删除部门异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("删除部门异常");
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
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ID", jobject.getString("ID"));
				params.put("IS_USE", isUse);
				this.departService.editService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("启用/禁用异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
}
