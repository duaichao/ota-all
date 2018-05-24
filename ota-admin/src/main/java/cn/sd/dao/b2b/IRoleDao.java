package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

public interface IRoleDao {
	public List<Map<String,Object>> listDao(Map<String,Object> params);
	public int countDao(Map<String,Object> params);
	public int existDao(Map<String,Object> params);
	public void addDao(Map<String,Object> params)throws Exception;
	public void editDao(Map<String,Object> params)throws Exception;
	public void delDao(String id)throws Exception;
	
	public void setModuleDao(Map<String,Object> params)throws Exception;
	public void clearModuleDao(String id)throws Exception;
	
	public void setPowerDao(Map<String,Object> params)throws Exception;
	public void clearPowerDao(Map<String,Object> params)throws Exception;
	
	public List<String> userRoleDao(String userId);
	
}
