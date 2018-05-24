package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.entity.b2b.ModuleEntity;
@Repository
@Service("moduleDao")
@SuppressWarnings("all")
public class ModuleDao extends BaseDaoImpl implements IModuleDao{

	private static Log log = LogFactory.getLog(ModuleDao.class);

	public void syncRoleModuleDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("module.syncRoleModuleDao", params);
	}
	
	@Override
	public List<ModuleEntity> listDao(Map<String, Object> params) {
		return this.getSqlMapClientTemplate().queryForList("module.listDao", params);
	}
	
	@Override
	public List<java.lang.String> listAllDao() {
		return this.getSqlMapClientTemplate().queryForList("module.listAllDao");
	}
	
	


	@Override
	public int countDao(Map<String, Object> params) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("module.countDao",params);
		return obj==null?0:Integer.parseInt(obj.toString());
	}


	@Override
	public List<String> roldModuleDao(String roleId) {
		return (List<String>)this.getSqlMapClientTemplate().queryForList("module.roldModuleDao", roleId);
	}


	@Override
	public List<ModuleEntity> userRoleModuleDao(Map<String, Object> params) {
		return this.getSqlMapClientTemplate().queryForList("module.userRoleModuleDao", params);
	}
	
	@Override
	public List<String> userRoleModuleByStringDao(String roleId){
		return this.getSqlMapClientTemplate().queryForList("module.userRoleModuleByStringDao", roleId);
	}
	
	/**
	 * 角色的模块列表
	 * @param params
	 * @return
	 */
	public List<ModuleEntity> listRoleOfModuleDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("module.listRoleOfModuleDao", params);
	}
	
	public List<String> listUserOfModuleIDDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("module.listUserOfModuleIDDao", params);
	}
	
	@Override
	public List<ModuleEntity> userModuleDao(Map<String, Object> params) {
		return this.getSqlMapClientTemplate().queryForList("module.userModuleDao", params);
	}
}
