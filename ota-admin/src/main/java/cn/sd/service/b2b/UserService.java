package cn.sd.service.b2b;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.WebUtils;
import cn.sd.dao.b2b.IUserDao;



@Service("userService")
public class UserService implements IUserService {
	private static Log log = LogFactory.getLog(UserService.class);
	@Resource
	private IUserDao userDao;
	
	@Override
	public Map<String,Object> detailUserService(Map<String,Object> params){
		return userDao.detailUserDao(params);
	}
	
	@Override
	public List<Map<String,Object>> listService(Map<String,Object> params){
		return userDao.listDao(params);
	}
	@Override
	public int countService(Map<String, Object> params) {
		return userDao.countDao(params);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public int saveService(Map<String, Object> params) throws Exception {
		if(CommonUtils.checkString(params.get("EMAIL"))){
			Map<String,Object> tp = new HashMap<String,Object>();
			tp.put("EMAIL", params.get("EMAIL"));
			if(userDao.existDao(tp)>0){
				log.info("用户邮箱已存在");
				return -2;
			}
			tp.clear();
		}
		if(CommonUtils.checkString(params.get("MOBILE"))){
			Map<String,Object> tp = new HashMap<String,Object>();
			tp.put("MOBILE", params.get("MOBILE"));
			if(userDao.existDao(tp)>0){
				log.info("用户手机已存在");
				return -3;
			}
			tp.clear();
		}
		if(CommonUtils.checkString(params.get("USER_NAME"))){
			Map<String,Object> tp = new HashMap<String,Object>();
			tp.put("USER_NAME", params.get("USER_NAME"));
			if(CommonUtils.checkString(params.get("ID"))){
				tp.put("ID", params.get("ID"));
			}else{
				tp.put("ID","-1");
			}
			if(userDao.existDao(tp)>0){
				log.info("用户名已存在");
				return -1;
			}
			tp.clear();
		}
		//后台保存创建用户状态默认都是启用
		params.put("IS_USE", "0");
		if(params.get("ID")==null || params.get("ID").equals("")){
			params.put("ID", params.get("UUID"));
			userDao.addDao(params);
		}else{
			params.remove("CREATE_USER");
			/**
			 * 修改用户的时候，不修改用户名，密码，用户类型
			 */
			params.remove("USER_NAME");
			params.remove("USER_PWD");
			params.remove("USER_TYPE");
			userDao.editDao(params);
		}
		/**
		if(CommonUtils.checkString(params.get("OPEN_ACCOUNT"))){
			if(params.get("OPEN_ACCOUNT").equals("1")){
				userDao.createAccountDao(params);
			}else{
				userDao.deleteAccountDao(params);
			}
		}
		*/
		return 1;
	}

	@Override
	public void delBatchService(List<String> list) throws Exception {
		userDao.delBatchDao(list);
	}

	@Override
	public void delService(String ID) throws Exception {
		userDao.delDao(ID);
	}


	@Override
	public int existService(Map<String, Object> params) {
		return userDao.existDao(params);
	}

	@Override
	public void clearRoleService(String id) throws Exception {
		userDao.clearRoleDao(id);
	}

	@Override
	public int setRoleService(Map<String, Object> params) throws Exception {
		userDao.setRoleDao(params);
		return 1;
	}

	public void saveLoginService(HttpServletRequest request, String url, String text){
		Object userid = null;
		String user_name = "";
		if(request.getSession().getAttribute("S_USER_SESSION_KEY")!=null){
			userid = ((Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY")).get("ID");
			user_name = (String)((Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY")).get("USER_NAME");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(CommonUtils.checkString(userid)){
			params.put("ID", CommonUtils.uuid());
			params.put("URL", url);
			params.put("TEXT", text);
			params.put("LOGIN_USER_ID", userid);
			params.put("USER_NAME", user_name);
			params.put("IP", WebUtils.getIpAddress(request));
			this.saveLoginLogService(params);
		}
	}
	
	@Override
	public void saveLoginLogService(Map<String, Object> params) {
		userDao.saveLoginDao(params);
	}
	
	public void editService(Map<String,Object> params)throws Exception{
		this.userDao.editDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void saveUserModuleService(Map<String,Object> params)throws Exception{
		String models = (String)params.get("models");
		String USER_ID = (String)params.get("USER_ID");
		//删除用户的模块
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("USER_ID", USER_ID);
		this.userDao.delUserModuleDao(parameters);
		JSONArray jarray = JSONArray.fromObject(models);
		Object[] objArray = jarray.toArray();
		for(int i=0;i<objArray.length;i++){
			JSONObject jobject = JSONObject.fromObject(objArray[i]);
			parameters.clear();
			parameters.put("MODULE_ID", jobject.getString("id"));
			parameters.put("USER_ID", USER_ID);
			this.userDao.saveUserModuleDao(parameters);
		}
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void saveUserPowerService(Map<String,Object> params)throws Exception{
		
		String models = (String)params.get("models");
		String USER_ID = (String)params.get("USER_ID");
		String MODULE_ID = (String)params.get("MODULE_ID");
		
		//删除用户的权限
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("MODULE_ID", MODULE_ID);
		parameters.put("USER_ID", USER_ID);
		this.userDao.delUserPowerDao(parameters);
		JSONArray jarray = JSONArray.fromObject(models);
		Object[] objArray = jarray.toArray();
		for(int i=0;i<objArray.length;i++){
			JSONObject jobject = JSONObject.fromObject(objArray[i]);
			parameters.clear();
			parameters.put("POWER_ID", jobject.getString("id"));
			parameters.put("USER_ID", USER_ID);
			this.userDao.saveUserPowerDao(parameters);
		}
		
	}
	public void delUserModuleService(Map<String,Object> params)throws Exception{
		this.userDao.delUserModuleDao(params);
	}
	public void delUserPowerService(Map<String,Object> params)throws Exception{
		this.userDao.delUserPowerDao(params);
	}
	
	public List<Map<String,Object>> listCompanyManagerService(Map<String,Object> params){
		return this.userDao.listCompanyManagerDao(params);
	}
	
	public void updatePWDService(Map<String,Object> params)throws Exception{
		this.userDao.editDao(params);
	}
	
	public List<Map<String,Object>> listUserPlusService(Map<String,Object> params){
		return this.userDao.listUserPlusDao(params);
	}
	public void saveUserPlusService(Map<String,Object> params)throws Exception{
		this.userDao.saveUserPlusDao(params);
	}
	public void updateUserPlusService(Map<String,Object> params)throws Exception{
		this.userDao.updateUserPlusDao(params);
	}
	public List<Map<String,Object>> siteServiceService(Map<String,Object> params){
		return this.userDao.siteServiceDao(params);
	}
}
