package cn.sd.service.b2b;

import java.util.List;
import java.util.Map;

public interface IRoleService {
	public void syncRoleService(Map<String,Object> params)throws Exception;
	public List<Map<String,Object>> listService(Map<String,Object> params);
	public int countService(Map<String,Object> params);
	
	public int saveService(Map<String,Object> params)throws Exception;
	public int delService(String id)throws Exception;
	
	public int setModuleService(Map<String,Object> params)throws Exception;
	public void clearModuleService(String id)throws Exception;
	
	public void clearPowerService(Map<String,Object> params)throws Exception;
	public int setPowerService(Map<String,Object> params)throws Exception;
	
	public List<String> userRoleService(String userId);
	
}
