package cn.sd.controller.b2c;

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
import cn.sd.power.PowerFactory;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/b2c/shop")
public class ShopController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(ShopController.class);
	
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("")
	public ModelAndView traffic(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("b2c/shop");
	}

	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response, MapRange mr, String CITY_ID, String TYPE, String query){
		ListRange json = new ListRange();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			//得到当前用户名和ID
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String COMPANY_ID = (String)user.get("COMPANY_ID");
			//根据当前登录用户公司ID查询子公司信息
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			if(!user.get("USER_NAME").toString().equals("admin")){
				mr.pm.put("PARENT_ID", COMPANY_ID);
			}
			
			//------------------------------------------------------------------------------
			mr.pm.put("USER_ID", (String)user.get("ID"));
			params.put("HAS_CITY", "TRUE");
			params.put("T_USER_ID", (String)user.get("ID"));
			params.put("T_COMPANY_ID", (String)user.get("COMPANY_ID"));
			mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(params));
			mr.pm = PowerFactory.getPower(request, response, "", "site-company", mr.pm);
			mr.pm.remove("MANAGER_SITE");
			mr.pm.remove("USER_ID");
			mr.pm.remove("ROLE_ID");
			
			if(CommonUtils.checkString(mr.pm.get("CITY_IDS"))){
				mr.pm.put("COMPANY_MANAGER", "YES");
				mr.pm.put("T_PARENT_ID", COMPANY_ID);
				mr.pm.remove("PARENT_ID");
			}
			//------------------------------------------------------------------------------	
			
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
			
			List<Map<String, Object>> data = companyService.listCompanyService(mr.pm);
			int totalSize = companyService.countCompanyService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询公司信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询公司信息异常");
		}
		return json;
	}
	
	@RequestMapping("/sort")
	public ListRange sort(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			this.companyService.updateCompanyOrderByIdService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("修改排序异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("修改排序异常");
		}
		return json;
	}
	
	@RequestMapping("/recommend")
	public ListRange recommend(HttpServletRequest request,HttpServletResponse response, MapRange mr, String models){
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				String IS_SHOW = jobject.getString("IS_SHOW");
				if(CommonUtils.checkString(IS_SHOW)){
					if(IS_SHOW.equals("1")){
						IS_SHOW = "0";
					}else{
						IS_SHOW = "1";
					}
				}else{
					IS_SHOW = "0";
				}
				mr.pm.put("ID", jobject.getString("ID"));
				mr.pm.put("IS_SHOW", IS_SHOW);
				this.companyService.updateCompanyIsShowByIdsService(mr.pm);
			}
			
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("推荐异常异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("推荐异常");
		}
		return json;
	}
	
}
