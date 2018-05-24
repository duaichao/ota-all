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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.sd.controller.site.AreaController;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.FileUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.UploadUtil;
import cn.sd.core.web.BaseController;
import cn.sd.power.PowerFactory;
import cn.sd.service.b2b.IADService;
import cn.sd.service.b2b.IModuleService;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/b2b/ad")
@SuppressWarnings("all")
public class ADController extends BaseController{

	private static Log log = LogFactory.getLog(AreaController.class);
	
	@Resource
	private IADService adService;
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("TYPE", "0");
			mr.pm.put("USER_ID", (String)user.get("ID"));
			if("0101".equals((String)user.get("USER_TYPE"))){
				/**
				 * 如果是管理员子账户，那应该继承管理员管理的城市
				 */
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("ID", user.get("COMPANY_ID"));
				params.put("start", 1);
				params.put("end", 1);
				List<Map<String, Object>> company = this.companyService.listCompanyOfUserService(params);
				if(CommonUtils.checkList(company)){
					mr.pm.put("USER_ID", company.get(0).get("USER_ID"));
				}
			}
			
			mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
			mr.pm.remove("USER_ID");
			mr.pm.put("ROLE", "MANAGER");
			PowerFactory.getPower(request, response, "", "AD-list", mr.pm);
			mr.pm.remove("ROLE");
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			
			mr.pm.put("start", start + 1);
			mr.pm.put("end", start + limit);
			
			List<Map<String, Object>> data = this.adService.listService(mr.pm);
			int totalSize = this.adService.countService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询广告图异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询广告图异常");
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
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ID", jobject.getString("ID"));
				this.adService.delService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除广告图异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
}
