package cn.sd.controller.b2b;

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
import cn.sd.entity.b2b.PowerEntity;
import cn.sd.service.b2b.IPowerService;
@RestController
@RequestMapping("/b2b/power")
public class PowerController extends ExtSupportAction {
	private static Log log = LogFactory.getLog(PowerController.class);
	@Resource
	private IPowerService powerService;
	
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response,MapRange mr){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		mr.pm.put("MODULE_ID", toString(request.getParameter("moduleId")));
		String roleId = toString(request.getParameter("roleId"));
		String userId = toString(request.getParameter("userId"));
		if(!"all".equals(roleId)){
			mr.pm.put("ROLE_ID", roleId);
			if(!CommonUtils.checkString(roleId)){
				mr.pm.put("USER_ID", userId);
			}
		}
		List<PowerEntity> data = powerService.listService(mr.pm);
		json.setChildren(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/role")
	public List<String> rolePower(HttpServletRequest request,HttpServletResponse response,String roleId){
		this.setResponse(response);
		return powerService.rolePowerService(roleId); 
	}
	
	@RequestMapping("/listRoleOfPower")
	public ListRange listRoleOfPower(HttpServletRequest request,HttpServletResponse response, MapRange mr, String MODULE_ID){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String ROLE_ID = (String)user.get("ROLE_ID");
		mr.pm.put("MODULE_ID", MODULE_ID);
		List<PowerEntity> data = null;
		if(CommonUtils.checkString(ROLE_ID)){
			mr.pm.put("ROLE_ID", ROLE_ID);
			data = this.powerService.listRoleOfPowerService(mr.pm);
		}else{
			mr.pm.put("USER_ID", (String)user.get("ID"));
			data = this.powerService.userOfPowerService(mr.pm);
		}
		
		json.setChildren(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/user")
	public List<String> listUserOfPowerID(HttpServletRequest request,HttpServletResponse response, MapRange mr, String userId){
		mr.pm.put("USER_ID", userId);
		return this.powerService.listUserOfPowerIDService(mr.pm);
	}
	
	@RequestMapping("/list/user")
	public ListRange listUserPower(HttpServletRequest request,HttpServletResponse response,MapRange mr){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		mr.pm.put("MODULE_ID", toString(request.getParameter("moduleId")));
		mr.pm.put("USER_ID", user.get("ID"));
		String roleId = (String)user.get("ROLE_ID");
		mr.pm.put("ROLE_ID", roleId);
		if(!CommonUtils.checkString(roleId)){
			roleId = (String)user.get("CHILD_ROLE_ID");
			mr.pm.put("ROLE_ID", roleId);
		}
		List<PowerEntity> data = powerService.listUserPowerService(mr.pm);
		json.setChildren(data);
		json.setSuccess(true);
		return json;
	}
	
}
