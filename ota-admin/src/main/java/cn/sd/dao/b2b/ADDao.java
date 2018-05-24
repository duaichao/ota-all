package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.entity.b2b.ModuleEntity;

@Repository
@Service("adDao")
@SuppressWarnings("all")
public class ADDao extends BaseDaoImpl implements IADDao{
	
	public List<Map<String, Object>> listDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("AD.listDao", params);
	}

	public int countDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("AD.countDao", params);
	}
	
	public void saveDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("AD.saveDao", params);
	}

	public void delDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().delete("AD.delDao", params);
	}
}
