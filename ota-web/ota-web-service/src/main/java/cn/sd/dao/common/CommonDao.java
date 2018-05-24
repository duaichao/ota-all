package cn.sd.dao.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("commonDao")
public class CommonDao extends BaseDaoImpl implements ICommonDao {

	public void saveWebCollect(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("common.saveWebCollect", params);
	}
	
	public void delWebCollect(Map<String, Object> params){
		this.getSqlMapClientTemplate().delete("common.delWebCollect", params);
	}
	
	public List<Map<String, Object>> searchWebCollect(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("common.searchWebCollect", params);
	}
	
	public int countWebCollect(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("common.countWebCollect", params);
	}
	
	public void usersmsGroupDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("common.usersmsGroupDao", params);
	}
	
	public List<Map<String, Object>> listUserSmsDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("common.listUserSmsDao", params);
	}
	
	public List<Map<String, Object>> searchWebCategory(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("common.searchWebCategory", params);
	}
	
	public List<Map<String, Object>> serachADATTR(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("common.serachADATTR", params);
	}
	
	public List<Map<String, Object>> searchSiteManager(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("common.searchSiteManager", params);
	}
}
