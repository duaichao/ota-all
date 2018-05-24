package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

import cn.sd.entity.b2b.PowerEntity;

public interface IPowerDao {
	
	 
	public void clearRolePowerDao(Map<String,Object> params);
	public void syncRolePowerDao(Map<String,Object> params);
	
	public List<PowerEntity> listDao(Map<String,Object> params);
	public int countDao(Map<String,Object> params);
	
	public List<String> rolePowerDao(String roleId);
	
	public List<PowerEntity> listRoleOfPowerDao(Map<String,Object> params);
	
	public List<String> listUserOfPowerIDDao(Map<String,Object> params);
	
	public List<PowerEntity> userOfPowerDao(Map<String,Object> params);
	
	public List<PowerEntity> listUserPowerDao(Map<String,Object> params);
	
}
