package cn.sd.service.b2b;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import cn.sd.dao.b2b.IPowerDao;
import cn.sd.entity.b2b.PowerEntity;

@Service("powerService")
public class PowerService implements IPowerService {
	private static Log log = LogFactory.getLog(PowerService.class);
	@Resource
	private IPowerDao powerDao;
	
	public void clearRolePowerService(Map<String,Object> params)throws Exception{
		this.powerDao.clearRolePowerDao(params);
	}
	public void syncRolePowerService(Map<String,Object> params)throws Exception{
		this.powerDao.syncRolePowerDao(params);
	}
	
	@Override
	public List<PowerEntity> listService(Map<String,Object> params){
		return powerDao.listDao(params);
	}
	@Override
	public int countService(Map<String, Object> params) {
		return powerDao.countDao(params);
	}
	@Override
	public List<String> rolePowerService(String roleId) {
		return powerDao.rolePowerDao(roleId);
	}
	
	public List<PowerEntity> listRoleOfPowerService(Map<String,Object> params){
		return this.powerDao.listRoleOfPowerDao(params);
	}
	
	public List<String> listUserOfPowerIDService(Map<String,Object> params){
		return this.powerDao.listUserOfPowerIDDao(params);
	}
	
	public List<PowerEntity> userOfPowerService(Map<String,Object> params){
		return this.powerDao.userOfPowerDao(params);
	}
	
	public List<PowerEntity> listUserPowerService(Map<String,Object> params){
		return this.powerDao.listUserPowerDao(params);
	}
}
