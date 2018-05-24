package cn.sd.service.b2b;

import java.util.List;
import java.util.Map;

import cn.sd.entity.b2b.PowerEntity;

public interface IPowerService {
	
	public void clearRolePowerService(Map<String,Object> params)throws Exception;
	public void syncRolePowerService(Map<String,Object> params)throws Exception;
	
	public List<PowerEntity> listService(Map<String,Object> params);
	public int countService(Map<String,Object> params);
	
	public List<String> rolePowerService(String roleId);
	
	public List<PowerEntity> listRoleOfPowerService(Map<String,Object> params);
	
	public List<String> listUserOfPowerIDService(Map<String,Object> params);
	
	public List<PowerEntity> userOfPowerService(Map<String,Object> params);
	
	public List<PowerEntity> listUserPowerService(Map<String,Object> params);
}
