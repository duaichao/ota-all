package cn.sd.controller.b2c;

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

import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/b2c/category")
public class CategoryController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(CategoryController.class);
	
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("")
	public ModelAndView b2cCategory(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("b2c/category");
	}
	
	@RequestMapping("/list")
	public ListRange listCategory(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
			List<Map<String, Object>> data = companyService.listWebCategoryByCompanyIdService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询首页分类异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询首页分类异常");
		}
		return json;
	}

	@RequestMapping("/sort")
	public ListRange categorySort(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			this.companyService.updateWebCategoryByIdService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("修改排序异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("修改排序异常");
		}
		return json;
	}
	
	@RequestMapping("/top")
	public ListRange top(HttpServletRequest request, HttpServletResponse response, MapRange mr, String models){
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				String isTop = jobject.getString("IS_TOP");
				if(isTop.equals("1")){
					isTop = "0";
				}else{
					isTop = "1";
				}
				mr.pm.put("ID", jobject.getString("RECOMMEND_ID"));
				mr.pm.put("IS_TOP", isTop);
				this.companyService.updateWebRecommendIsTopService(mr.pm);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("修改精彩推荐排序异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("修改精彩推荐排序异常");
		}
		return json;
	}
	
}
