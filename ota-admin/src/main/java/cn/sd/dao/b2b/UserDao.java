package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
@Repository
@Service("userDao")
@SuppressWarnings("all")
public class UserDao extends BaseDaoImpl implements IUserDao{

	private static Log log = LogFactory.getLog(UserDao.class);

	@Override
	public Map<String, Object> detailUserDao(Map<String, Object> params) {
		List<Map<String,Object>> list = this.getSqlMapClientTemplate().queryForList("user.detailUserDao", params);
		return list.size()==0?null:list.get(0);
	}
	@Override
	public List<Map<String,Object>> listDao(Map<String, Object> params) {
		return this.getSqlMapClientTemplate().queryForList("user.listDao", params);
	}
	@Override
	public int countDao(Map<String, Object> params) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("user.countDao",params);
		return obj==null?0:Integer.parseInt(obj.toString());
	}
	@Override
	public void addDao(Map<String, Object> params)  {
		this.getSqlMapClientTemplate().insert("user.addDao",params);
	}
	@Override
	public void delBatchDao(List<String> list)  {
		this.getSqlMapClientTemplate().delete("user.delBatchDao",list);
	}
	@Override
	public void delDao(String ID)  {
		this.getSqlMapClientTemplate().update("user.delDao",ID);
	}
	@Override
	public void editDao(Map<String, Object> params)  {
		this.getSqlMapClientTemplate().update("user.editDao",params);
	}
	@Override
	public int existDao(Map<String, Object> params) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("user.existDao",params);
		return obj==null?0:Integer.parseInt(obj.toString());
	}
	@Override
	public void createAccountDao(Map<String, Object> params)  {
		this.getSqlMapClientTemplate().insert("user.createAccountDao",params);
	}
	@Override
	public void deleteAccountDao(Map<String, Object> params)  {
		this.getSqlMapClientTemplate().delete("user.deleteAccountDao",params);
	}
	@Override
	public void clearRoleDao(String id)  {
		this.getSqlMapClientTemplate().delete("user.clearRoleDao",id);
	}
	@Override
	public void setRoleDao(Map<String, Object> params)  {
		this.getSqlMapClientTemplate().insert("user.setRoleDao",params);
	}
	@Override
	public void saveLoginDao(Map<String, Object> params) {
		this.getSqlMapClientTemplate().update("user.updateLoginDao",params);
		this.getSqlMapClientTemplate().insert("user.saveLoginDao",params);
	}
	
	public void saveUserModuleDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("user.saveUserModuleDao", params);
	}
	public void saveUserPowerDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("user.saveUserPowerDao", params);
	}
	public void delUserModuleDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("user.delUserModuleDao", params);
	}
	public void delUserPowerDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("user.delUserPowerDao", params);
	}
	
	public List<Map<String,Object>> listCompanyManagerDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("user.listCompanyManagerDao", params);
	}
	
	public List<Map<String,Object>> listUserPlusDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("user.listUserPlusDao", params);
	}
	public void saveUserPlusDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("user.saveUserPlusDao", params);
	}
	public void updateUserPlusDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("user.updateUserPlusDao", params);
	}
	
	public List<Map<String,Object>> siteServiceDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("user.siteServiceDao", params);
	}
}
