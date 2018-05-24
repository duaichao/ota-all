package cn.sd.controller.b2c;

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
import cn.sd.entity.site.WapSettingEntity;
import cn.sd.power.PowerFactory;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/b2c/setting")
public class WebSettingController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(WebSettingController.class);
	
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("")
	public ModelAndView traffic(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("b2c/setting");
	}
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			/**
			 * 站长查询站下的所有网站
			 * 用户登录查询自己公司的网站
			 */
			String companyType = String.valueOf(user.get("COMPANY_TYPE"));
			mr.pm.clear();
			if(companyType.equals("0")){
				mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
				mr.pm = PowerFactory.getPower(request, response, "", "site-company", mr.pm);
				mr.pm.remove("ROLE_ID");
				mr.pm.remove("COMPANY_ID");
				mr.pm.remove("COMPANY_TYPE");
			}else{
				mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
			}
			
			if(CommonUtils.checkString(query)){
				mr.pm.put("query", new String(query.getBytes("ISO_8859-1"), "UTF-8"));
			}
			
			mr.pm.put("TYPE", 0);
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start);
			mr.pm.put("end", start+limit);
			
			List<WapSettingEntity> data = this.companyService.listWapSettingService(mr.pm);
			int totalSize = this.companyService.countWapSettingService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询微店列表异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询微店列表异常");
		}
		return json;
	}
}
