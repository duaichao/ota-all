package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
@Repository
@Service("roleDao")
@SuppressWarnings("all")
public class RoleDao extends BaseDaoImpl implements IRoleDao{

	private static Log log = LogFactory.getLog(RoleDao.class);
	
	@Override
	public List<Map<String,Object>> listDao(Map<String, Object> params) {
		return this.getSqlMapClientTemplate().queryForList("role.listDao", params);
	}
	@Override
	public int countDao(Map<String, Object> params) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("role.countDao",params);
		return obj==null?0:Integer.parseInt(obj.toString());
	}
	@Override
	public void addDao(Map<String, Object> params) throws Exception {
		this.getSqlMapClientTemplate().insert("role.addDao",params);
	}
	@Override
	public void editDao(Map<String, Object> params) throws Exception {
		this.getSqlMapClientTemplate().update("role.editDao",params);
	}
	@Override
	public int existDao(Map<String, Object> params) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("role.existDao",params);
		return obj==null?0:Integer.parseInt(obj.toString());
	}
	@Override
	public void delDao(String id) throws Exception {
		this.getSqlMapClientTemplate().delete("role.delDao",id);
	}
	@Override
	public void clearModuleDao(String id) throws Exception {
		this.getSqlMapClientTemplate().delete("role.clearModuleDao",id);
	}
	@Override
	public void clearPowerDao(Map<String,Object> params) throws Exception {
		this.getSqlMapClientTemplate().delete("role.clearPowerDao",params);
	}
	@Override
	public void setModuleDao(Map<String, Object> params) throws Exception {
		this.getSqlMapClientTemplate().insert("role.setModuleDao",params);
	}
	@Override
	public void setPowerDao(Map<String, Object> params) throws Exception {
		this.getSqlMapClientTemplate().insert("role.setPowerDao",params);
	}
	@Override
	public List<String> userRoleDao(String userId) {
		return (List<String>)this.getSqlMapClientTemplate().queryForList("role.userRoleDao", userId);
	}
}
