package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

public interface IUserDao {
	public Map<String,Object>detailUserDao(Map<String,Object> params);
	public List<Map<String,Object>> listDao(Map<String,Object> params);
	public int countDao(Map<String,Object> params);
	
	public int existDao(Map<String,Object> params);
	public void addDao(Map<String,Object> params);
	public void editDao(Map<String,Object> params);
	
	public void createAccountDao(Map<String,Object> params);
	public void deleteAccountDao(Map<String,Object> params);
	
	public void delDao(String ID)throws Exception;
	public void delBatchDao(List<String> list);
	
	public void setRoleDao(Map<String,Object> params);
	public void clearRoleDao(String id)throws Exception;
	
	public void saveLoginDao(Map<String,Object> params);
	
	public void saveUserModuleDao(Map<String,Object> params);
	public void saveUserPowerDao(Map<String,Object> params);
	public void delUserModuleDao(Map<String,Object> params);
	public void delUserPowerDao(Map<String,Object> params);
	
	public List<Map<String,Object>> listCompanyManagerDao(Map<String,Object> params);
	
	public List<Map<String,Object>> listUserPlusDao(Map<String,Object> params);
	public void saveUserPlusDao(Map<String,Object> params);
	public void updateUserPlusDao(Map<String,Object> params);
	
	public List<Map<String,Object>> siteServiceDao(Map<String,Object> params);
	
	  
}
