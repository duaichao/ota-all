package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

import cn.sd.entity.b2b.ModuleEntity;

public interface IModuleDao {
	
	public void syncRoleModuleDao(Map<String,Object> params);
	public List<ModuleEntity> listDao(Map<String,Object> params);
	public List<java.lang.String> listAllDao();
	public int countDao(Map<String,Object> params);
	
	public List<String> roldModuleDao(String roleId);
	
	public List<ModuleEntity> userRoleModuleDao(Map<String, Object> params);
	public List<String> userRoleModuleByStringDao(String roleId);
	
	public List<ModuleEntity> listRoleOfModuleDao(Map<String,Object> params);
	
	public List<String> listUserOfModuleIDDao(Map<String, Object> params);
	
	public List<ModuleEntity> userModuleDao(Map<String, Object> params);
	
}
