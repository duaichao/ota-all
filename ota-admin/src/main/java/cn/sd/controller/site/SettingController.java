package cn.sd.controller.site;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.site.ICompanyService;
import cn.sd.service.site.ISettingService;

@RestController
@RequestMapping("/site/setting")
@SuppressWarnings("all")
public class SettingController extends ExtSupportAction {

	private static Log log = LogFactory.getLog(AreaController.class);
	
	@Resource
	private ISettingService settingService;
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("")
	public ModelAndView main(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/site/setting");
	}
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		List<Map<String,Object>> data = this.settingService.listService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/addSite")
	public ListRange addSite(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("CITY_ID", (String)mr.pm.get("CITY_ID"));
			List<Map<String, Object>> data = this.settingService.listService(p);
			if(CommonUtils.checkList(data)){
				//不能重复开站
				json.setStatusCode("-1");
				json.setSuccess(false);
			}else{
				mr.pm.put("ID", CommonUtils.uuid());
				this.settingService.saveSiteService(mr.pm);
				json.setSuccess(true);
			}
		} catch (Exception e) {
			log.error("添加异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加异常");
		}
		return json;
	}
	
	/**
	 * 禁用/启用站
	 * @param request
	 * @param response
	 * @param CITY_ID
	 * @param USE_STATUS
	 * @return
	 */
	@RequestMapping("/editSiteUseStatus")
	public ListRange editSiteUseStatus(HttpServletRequest request, HttpServletResponse response, String CITY_ID, String USE_STATUS){
		ListRange json = new ListRange();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("CITY_ID", CITY_ID);
			List<Map<String, Object>> data = this.companyService.listSiteManagerService(params);
			if(!CommonUtils.checkList(data)){
				params.put("USE_STATUS", USE_STATUS);
				this.settingService.editSiteUseStatusService(params);
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
				json.setStatusCode("-1");
				json.setMessage("城市已被使用，无法禁用！");
			}
			
		} catch (Exception e) {
			log.error("启用/禁用异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("启用/禁用异常");
		}
		return json;
	}
	
	@RequestMapping("/usableSites")
	public ListRange usableSites(HttpServletRequest request, HttpServletResponse response, MapRange mr, String type, String USER_ID){
		ListRange json = new ListRange();
		//启用的、未被分配的站
		mr.pm.put("TYPE", type);
		mr.pm.put("USER_ID", USER_ID);
		List<Map<String,Object>> data = this.settingService.usableSitesService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/siteStyle")
	public ListRange siteStyle(HttpServletRequest request, HttpServletResponse response, MapRange mr, String type, String USER_ID){
		ListRange json = new ListRange();
		//启用的、未被分配的站
		mr.pm.put("TYPE", type);
		mr.pm.put("USER_ID", USER_ID);
		List<Map<String,Object>> data = this.settingService.usableSitesService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/attr")
	public ListRange siteAttr(HttpServletRequest request, HttpServletResponse response, MapRange mr, String id, String cityId){
		ListRange json = new ListRange();
		mr.pm.put("ID", id);
		mr.pm.put("CITY_ID", cityId);
		List<Map<String,Object>> data = this.settingService.listSiteAttrService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/attr/save")
	public ListRange saveSiteAttr(HttpServletRequest request, HttpServletResponse response, MapRange mr, String type){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("CREATE_ID", (String)user.get("ID"));
			mr.pm.put("UPDATE_ID", (String)user.get("ID"));
			String ID = (String)mr.pm.get("ID");
			if(!CommonUtils.checkString(ID)){
				mr.pm.put("ID", CommonUtils.uuid());
				this.settingService.saveSiteAttrService(mr.pm);
			}else{
				this.settingService.updateSiteAttrService(mr.pm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		json.setSuccess(true);
		return json;
	}
}
