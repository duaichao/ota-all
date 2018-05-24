package cn.sd.service.b2b;

import java.util.List;
import java.util.Map;

import cn.sd.entity.b2b.ModuleEntity;

public interface IModuleService {
	public void syncRoleModuleService(Map<String,Object> params)throws Exception;
	public List<ModuleEntity> listService(Map<String,Object> params);
	public List<java.lang.String> listAllService();
	public int countService(Map<String,Object> params);
	
	public List<String> roleModuleService(String roleId);
	
	public List<ModuleEntity> userRoleModuleService(Map<String,Object> params);
	public List<String> userRoleModuleByStringService(String roleId);
	
	public List<ModuleEntity> listRoleOfModuleService(Map<String,Object> params);
	
	public List<String> listUserOfModuleIDService(Map<String, Object> params);
	
	public List<ModuleEntity> userModuleService(Map<String,Object> params);
	
	
}
