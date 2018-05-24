package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.entity.b2b.PowerEntity;
@Repository
@Service("powerDao")
@SuppressWarnings("all")
public class PowerDao extends BaseDaoImpl implements IPowerDao{

	private static Log log = LogFactory.getLog(PowerDao.class);

	
	public void clearRolePowerDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("power.clearRolePowerDao", params);
	}
	public void syncRolePowerDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("power.syncRolePowerDao", params);
	}
	
	@Override
	public List<PowerEntity> listDao(Map<String, Object> params) {
		return this.getSqlMapClientTemplate().queryForList("power.listDao", params);
	}
	@Override
	public int countDao(Map<String, Object> params) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("power.countDao",params);
		return obj==null?0:Integer.parseInt(obj.toString());
	}
	@Override
	public List<String> rolePowerDao(String roleId) {
		return (List<String>)this.getSqlMapClientTemplate().queryForList("power.rolePowerDao", roleId);
	}
	
	/**
	 * 角色的权限列表
	 * @param params
	 * @return
	 */
	public List<PowerEntity> listRoleOfPowerDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("power.listRoleOfPowerDao", params);
	}
	
	public List<String> listUserOfPowerIDDao(Map<String,Object> params){
		return (List<String>)this.getSqlMapClientTemplate().queryForList("power.listUserOfPowerIDDao", params);
	}
	
	public List<PowerEntity> userOfPowerDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("power.listDao", params);
	}
	
	public List<PowerEntity> listUserPowerDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("power.listUserPowerDao", params);
	}
}
