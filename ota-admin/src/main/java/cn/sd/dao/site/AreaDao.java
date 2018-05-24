package cn.sd.dao.site;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.entity.b2b.ModuleEntity;

@Repository
@Service("areaDao")
@SuppressWarnings("all")
public class AreaDao extends BaseDaoImpl implements IAreaDao{
	
	public List<Map<String, Object>> listDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("area.listDao", params);
	}

	public void addDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("area.addDao", params);
	}
	
	public void editDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("area.editDao", params);
	}
	
	public void delDao(String ID)throws Exception{
		this.getSqlMapClientTemplate().delete("area.delDao", ID);
	}
	
	public void editAreaNameDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("area.editAreaNameDao", params);
	}
	
	public List<Map<String, Object>> listCityLabelDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("area.listCityLabelDao", params);
	}
	
	public void addCityLabelDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("area.addCityLabelDao", params);
	}
	
	public void delCityLabelDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().delete("area.delCityLabelDao", params);
	}
	
	public List<Map<String, Object>> listCityDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("area.listCityDao", params);
	}
	
	public int countCityDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("area.countCityDao", params);
	}
	
	public List<Map<String, Object>> listCountryDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("area.listCountryDao", params);
	}
	
	public int countCountryDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("area.countCountryDao", params);
	}
	
	public List<Map<String, Object>> listScenicDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("area.listScenicDao", params);
	}
	
	public int countScenicDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("area.countScenicDao", params);
	}
	
	public List<String> listCityLabelEntityIDDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("area.listCityLabelEntityIDDao", params);
	}
	
	public int countCityAndCountryDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("area.countCityAndCountryDao", params);
	}
	
	public List<Map<String, Object>> listCityAndCountryDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("area.listCityAndCountryDao", params);
	}
	
}
