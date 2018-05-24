package cn.sd.controller.b2c;

import java.io.File;
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

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.FileUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.UploadUtil;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.entity.site.WapSettingEntity;
import cn.sd.power.PowerFactory;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/b2c/minishop")
public class MiniShopController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(MiniShopController.class);
	
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("/set/account/user")
	public ListRange setAccountUser(HttpServletRequest request, HttpServletResponse response, MapRange mr, String id, String userId, String userName){
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("ID", id);
			mr.pm.put("ACCOUNT_USER_ID", userId);
			mr.pm.put("ACCOUNT_USER_NAME", userName);
			this.companyService.setAccountUserService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("设置顾问异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("设置顾问异常");
		}
		return json;
	}
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			/**
			 * 站长查询站下的所有微店
			 * 用户登录查询自己的微店
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
				mr.pm.put("ENTITY_ID", user.get("ID"));
			}
			
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start);
			mr.pm.put("end", start+limit);
			
			mr.pm.put("TYPE", 1);
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
	
	@RequestMapping("/isuse")
	public ListRange isuse(HttpServletRequest request, HttpServletResponse response, MapRange mr,String models){
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
				this.companyService.updateMiniShopUseStateDao(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("启用/禁用微店异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("启用/禁用微店异常");
		}
		return json;
	}
	
	@RequestMapping("/open")
	public ListRange open(HttpServletRequest request, HttpServletResponse response, MapRange mr, String companyId, String userId, String accountUserId, String accountUserName){
		ListRange json = new ListRange();
		try {
			mr.pm.put("ID", CommonUtils.uuid());
			mr.pm.put("COMPANY_ID", companyId);
			mr.pm.put("ENTITY_ID", userId);
			mr.pm.put("ACCOUNT_USER_ID", accountUserId);
			mr.pm.put("ACCOUNT_USER_NAME", accountUserName);
			this.companyService.saveMiniShopService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("开通微店信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("开通微店信息异常");
		}
		return json;
	}
	
	@RequestMapping("/update")
	public ListRange update(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("TYPE", 1);
			params.put("DOMAIN", mr.pm.get("DOMAIN"));
			params.put("start", 1);
			params.put("end", 1);
			params.put("NOTID", mr.pm.get("ID"));
			List<WapSettingEntity> data = this.companyService.listWapSettingService(params);
			if(!CommonUtils.checkList(data) || data.size() == 0){
				String LOGO = "";
				String CODE = "";
				String WX_IMG = "";
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile CODE_FILE = multipartRequest.getFile("CODE");
				if(CODE_FILE != null){
					String file_name = CODE_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("wap");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							CODE = UploadUtil.uploadByte(CODE_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}
					}
				}
				
				//logo
				MultipartFile LOGO_FILE = multipartRequest.getFile("LOGO");
				if(LOGO_FILE != null){
					String file_name = LOGO_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("wap");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							String uuid = CommonUtils.uuid();
							file_name = uuid+file_suffix;
							LOGO = UploadUtil.uploadByte(LOGO_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
							UploadUtil.scale(new File(paths[0]+file_name), 320, 320, paths[0], uuid, "320X320");
						}	
					}
				}
				
				MultipartFile WX_IMG_FILE = multipartRequest.getFile("WX_IMG");
				if(WX_IMG_FILE != null){
					String file_name = WX_IMG_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png,ico")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("wap");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							WX_IMG = UploadUtil.uploadByte(WX_IMG_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}	
					}
				}
				
				mr.pm.put("CODE", CODE);
				mr.pm.put("LOGO", LOGO);
				mr.pm.put("WX_IMG", WX_IMG);
				this.companyService.updateMiniShopService(mr.pm);
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
				json.setStatusCode("-1");
				json.setMessage("填写的域名已存在");
			}
		} catch (Exception e) {
			log.error("修改微店信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("修改微店信息异常");
		}
		return json;
	}
	
	@RequestMapping("/syn/web/data")
	public ListRange synWebData(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("ENTITY_ID", user.get("ID"));
			mr.pm.put("TYPE", 1);
			mr.pm.put("start", 1);
			mr.pm.put("end", 10);
			List<WapSettingEntity> data = this.companyService.listWapSettingService(mr.pm);
			if(CommonUtils.checkList(data) && data.size() == 1){
				mr.pm.clear();
				//同步之前,删除已推荐所有的线路
				mr.pm.put("WAP_ID", data.get(0).getID());
				this.companyService.delWebRecommendByWapIdDao(mr.pm);
				mr.pm.put("USER_ID", user.get("ID"));
				mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
				this.companyService.synCompanyWebDataService(mr.pm);
			}else{
				json.setSuccess(false);
				json.setMessage("同步公司网站推荐产品异常");
			}
			
		} catch (Exception e) {
			log.error("修改微店信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("同步公司网站推荐产品异常");
		}
		return json;
	}
	
}
