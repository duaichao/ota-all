package cn.sd.controller.b2b;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.entity.b2b.ModuleEntity;
import cn.sd.service.b2b.IModuleService;
@RestController
@RequestMapping("/b2b/module")
public class ModuleController extends ExtSupportAction {
	private static Log log = LogFactory.getLog(ModuleController.class);
	
	@Resource
	private IModuleService moduleService;
	
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response,MapRange mr){
		ListRange json = new ListRange();
		/*int start = toInt(request.getParameter("start"));
		int limit = toInt(request.getParameter("limit"));
		
		mr.pm.put("start", start + 1);
		mr.pm.put("end", start + limit);*/
		mr.pm.put("PID", (toString(request.getParameter("node")).equals("root")||toString(request.getParameter("node")).equals("nav_home"))?"-1":request.getParameter("node"));
		List<ModuleEntity> data = moduleService.listService(mr.pm);
		//int total = moduleService.countService(mr.pm); 
		
		json.setChildren(data);
		json.setSuccess(true);
		return json;
	}
	@RequestMapping("/role")
	public List<String> roleModule(HttpServletRequest request,HttpServletResponse response,String roleId){
		this.setResponse(response);
		List<String> data = moduleService.roleModuleService(roleId);
		
		return data;
	}
	
	@RequestMapping("/listRoleOfModule")
	public ListRange listRoleOfModule(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String ROLE_ID = (String)user.get("ROLE_ID");
		mr.pm.put("PID", (toString(request.getParameter("node")).equals("root")||toString(request.getParameter("node")).equals("nav_home"))?"-1":request.getParameter("node"));
		List<ModuleEntity> data = null;
		if(CommonUtils.checkString(ROLE_ID)){
			mr.pm.put("ROLE_ID", ROLE_ID);
			data = this.moduleService.listRoleOfModuleService(mr.pm);
		}else{
			mr.pm.put("USER_ID", (String)user.get("ID"));
			data = this.moduleService.userModuleService(mr.pm);
		}
		if(!CommonUtils.checkList(data)){
			data = new ArrayList<ModuleEntity>();
		}
		json.setChildren(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/user")
	public List<String>  listUserOfModuleID(HttpServletRequest request,HttpServletResponse response, MapRange mr, String userId){
		mr.pm.put("USER_ID", userId);
		return this.moduleService.listUserOfModuleIDService(mr.pm);
	}
	
	
}
