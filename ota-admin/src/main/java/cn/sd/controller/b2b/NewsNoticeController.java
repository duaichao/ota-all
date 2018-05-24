package cn.sd.controller.b2b;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import cn.sd.entity.b2b.NewsNoticeEntity;
import cn.sd.power.PowerFactory;
import cn.sd.service.b2b.INewsNoticeService;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/b2b/notice")
public class NewsNoticeController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(NewsNoticeController.class);
	
	@Resource
	private INewsNoticeService newsNoticeService;
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String type){
		Map<String, Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String query = toString(request.getParameter("query"));
		try {
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("QUERY", query);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		ListRange json = new ListRange();
		int start = toInt(request.getParameter("start"));
		int limit = toInt(request.getParameter("limit"));
		
		mr.pm.put("start", start + 1);
		mr.pm.put("end", start + limit);
		mr.pm.put("TYPE", type);
		mr.pm.put("USER_ID", (String)user.get("ID"));
		if(!"admin".equals((String)user.get("USER_NAME"))){
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
			}else{
				mr.pm.put("CITY_ID", user.get("CITY_ID"));
			}
		}
		
		mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
		mr.pm.remove("USER_ID");
		mr.pm.put("ROLE", "MANAGER");
		PowerFactory.getPower(request, response, "", "AD-list", mr.pm);
		mr.pm.remove("ROLE");
		
		List<Map<String,Object>> data = newsNoticeService.listService(mr.pm);
		int totalSize = newsNoticeService.countService(mr.pm);
		json.setData(data);
		json.setTotalSize(totalSize);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/save")
	public ListRange save(HttpServletRequest request, HttpServletResponse response, MapRange mr, String CONTENT){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			Map<String, Object> site = (Map<String, Object>)request.getSession().getAttribute("S_SITE_SESSION_KEY");
			if(!CommonUtils.checkMap(site) || !CommonUtils.checkString(site.get("ID"))){
				json.setSuccess(false);
				json.setStatusCode("-1");
				return json;
			}
			mr.pm.put("SITE_ID", (String)site.get("ID"));
			mr.pm.put("CREATE_USER", (String)user.get("USER_NAME"));
			mr.pm.put("UPDATE_USER", (String)user.get("USER_NAME"));
			String ID = (String)mr.pm.get("ID");
			mr.pm.put("CONTENT", CONTENT);
			if(!CommonUtils.checkString(ID)){
				mr.pm.put("ID", CommonUtils.uuid());
				this.newsNoticeService.saveService(mr.pm);
			}else{
				this.newsNoticeService.editService(mr.pm);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存异常");
		}
		return json;
	}
	
	@RequestMapping("/del")
	public ListRange del(HttpServletRequest request, HttpServletResponse response, String models){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ID", jobject.getString("ID"));
				params.put("IS_DEL", "1");
				params.put("UPDATE_USER", (String)user.get("USER_NAME"));
				this.newsNoticeService.editService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/top")
	public ListRange top(HttpServletRequest request, HttpServletResponse response, String models){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ID", jobject.getString("ID"));
				
				String IS_TOP = jobject.getString("IS_TOP");
				if(IS_TOP.equals("1")){
					IS_TOP = "0";
				}else{
					IS_TOP = "1";
				}
				
				params.put("IS_TOP", IS_TOP);
				params.put("UPDATE_USER", (String)user.get("USER_NAME"));
				this.newsNoticeService.editService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("置顶异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/detail")
	public ListRange detail(HttpServletRequest request, HttpServletResponse response, MapRange mr, String ID){
		ListRange json = new ListRange();
		try {
			mr.pm.put("ID", ID);
			List<NewsNoticeEntity> data = new ArrayList<NewsNoticeEntity>();
			NewsNoticeEntity newsNoticeEntity = newsNoticeService.detailService(mr.pm);
			data.add(newsNoticeEntity);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("置顶异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		
		return json;
	}
}
