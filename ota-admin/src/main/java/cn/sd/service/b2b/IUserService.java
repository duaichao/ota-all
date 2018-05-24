package cn.sd.service.b2b;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {
	public Map<String,Object> detailUserService(Map<String,Object> params);
	public List<Map<String,Object>> listService(Map<String,Object> params);
	public int countService(Map<String,Object> params);
	
	public int existService(Map<String,Object> params);
	public int saveService(Map<String,Object> params)throws Exception;
	public void delService(String ID)throws Exception;
	public void delBatchService(List<String> list)throws Exception;
	
	public int setRoleService(Map<String,Object> params)throws Exception;
	public void clearRoleService(String id)throws Exception;
	
	public void saveLoginService(HttpServletRequest request, String url, String text);
	
	public void saveLoginLogService(Map<String,Object> params);
	
	public void editService(Map<String,Object> params)throws Exception;
	
	public void saveUserModuleService(Map<String,Object> params)throws Exception;
	public void saveUserPowerService(Map<String,Object> params)throws Exception;
	public void delUserModuleService(Map<String,Object> params)throws Exception;
	public void delUserPowerService(Map<String,Object> params)throws Exception;
	
	public List<Map<String,Object>> listCompanyManagerService(Map<String,Object> params);
	
	public void updatePWDService(Map<String,Object> params)throws Exception;
	
	public List<Map<String,Object>> listUserPlusService(Map<String,Object> params);
	public void saveUserPlusService(Map<String,Object> params)throws Exception;
	public void updateUserPlusService(Map<String,Object> params)throws Exception;
	
	public List<Map<String,Object>> siteServiceService(Map<String,Object> params);
}
