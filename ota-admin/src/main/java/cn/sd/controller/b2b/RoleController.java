package cn.sd.controller.b2b;

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

import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.encode.MD5;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.b2b.IRoleService;
@RestController
@RequestMapping("/b2b/role")
public class RoleController extends ExtSupportAction {
	private static Log log = LogFactory.getLog(RoleController.class);
	
	@Resource
	private IRoleService roleService;
	
	@RequestMapping("/sync")
	public ListRange sync(HttpServletRequest request, HttpServletResponse response, MapRange mr, String roleId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("PARENT_COMPANY_ID", user.get("COMPANY_ID"));
			mr.pm.put("PARENT_ROLE_ID", roleId);
			this.roleService.syncRoleService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("同步角色异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("同步角色异常");
		}
		return json;
	}
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response,MapRange mr){
		this.setResponse(response);
		ListRange json = new ListRange();
		String isUse = toString(request.getParameter("isUse"));
		if(!"".equals(isUse)){
			mr.pm.put("IS_USE", isUse);
		}
		/*int start = toInt(request.getParameter("start"));
		int limit = toInt(request.getParameter("limit"));
		
		mr.pm.put("start", start + 1);
		mr.pm.put("end", start + limit);*/
		
		List<Map<String,Object>> data = roleService.listService(mr.pm);
		//int total = roleService.countService(mr.pm);
		json.setData(data);
		//json.setTotalSize(total);
		json.setSuccess(true);
		
		return json;
	}
	@RequestMapping("/save")
	public ListRange add(HttpServletRequest request,HttpServletResponse response,MapRange mr){
		ListRange json = new ListRange();
		try {
			mr.pm.put("UUID", this.getUuid());
			mr.pm.put("USER_PWD", MD5.toMD5(MD5.toMD5(toString(mr.pm.get("USER_PWD")))));
			int ok = roleService.saveService(mr.pm);
			if(ok==1){
				json.setSuccess(true);
			}else{
				json.setStatusCode(ok+"");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("添加角色异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加角色异常");
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
				String ID = jobject.getString("ID");
				if(!ID.equals("0D8AE92D48203E81E050007F0100E920")){
					roleService.delService(jobject.getString("ID"));
				}
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除角色异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	@RequestMapping("/switch")
	public ListRange switchs(HttpServletRequest request,HttpServletResponse response,String models){
		ListRange json = new ListRange();
		try {
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
				tp.put("ID", jobject.getString("ID"));
				tp.put("IS_USE", isUse);
				roleService.saveService(tp);
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
	public ListRange setModule(HttpServletRequest request,HttpServletResponse response,String models,String roleId){
		ListRange json = new ListRange();
		try {
			roleService.clearModuleService(roleId);
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				Map<String,Object> tp = new HashMap<String,Object>();
				tp.put("MODULE_ID", jobject.getString("id"));
				tp.put("ROLE_ID", roleId);
				roleService.setModuleService(tp);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("配置模块异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	@RequestMapping("/set/power")
	public ListRange setPower(HttpServletRequest request,HttpServletResponse response,String models,String roleId,String moduleId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> clear = new HashMap<String,Object>();
			clear.put("MODULE_ID", moduleId);
			clear.put("ROLE_ID", roleId);
			roleService.clearPowerService(clear);
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				Map<String,Object> tp = new HashMap<String,Object>();
				tp.put("POWER_ID", jobject.getString("id"));
				tp.put("ROLE_ID", roleId);
				roleService.setPowerService(tp);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("配置角色异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	@RequestMapping("/user")
	public List<String> roleModule(HttpServletRequest request,HttpServletResponse response,String userId){
		this.setResponse(response);
		List<String> data = roleService.userRoleService(userId);
		return data;
	}
	
	@RequestMapping("/listRole")
	public ListRange listRole(HttpServletRequest request,HttpServletResponse response, MapRange mr, String roleType){
		this.setResponse(response);
		ListRange json = new ListRange();
		mr.pm.put("IS_USE", "0");
		//站长ID
		mr.pm.put("NOT_EQUALS_ID", "0D8AE92D48203E81E050007F0100E920");
		mr.pm.put("ROLE_TYPE", roleType);
		List<Map<String,Object>> data = roleService.listService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
}
