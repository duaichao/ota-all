package cn.sd.dao.site;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.entity.b2b.ModuleEntity;

@Repository
@Service("departDao")
@SuppressWarnings("all")
public class DepartDao extends BaseDaoImpl implements IDepartDao{
	
	public List<Map<String, Object>> listDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("depart.listDao", params);
	}
	
	public int countDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.countDao", params);
	}
	
	public void saveDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("depart.saveDao", params);
	}
	
	public void editDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("depart.editDao", params);
	}
	
	public void delDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().delete("depart.delDao", params);
	}
}
