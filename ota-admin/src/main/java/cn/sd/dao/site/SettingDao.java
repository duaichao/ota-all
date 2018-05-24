package cn.sd.dao.site;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.entity.b2b.ModuleEntity;

@Repository
@Service("settingDao")
@SuppressWarnings("all")
public class SettingDao extends BaseDaoImpl implements ISettingDao{

	public List<Map<String, Object>> listDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("setting.listDao", params);
	}

	public void saveSiteDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("setting.saveSiteDao", params);
	}
	
	public void editSiteUseStatusDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("setting.editSiteUseStatusDao", params);
	}
	
	public List<Map<String, Object>> usableSitesDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("setting.usableSitesDao", params);
	}
	
	public List<Map<String, Object>> listSiteAttrDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("setting.listSiteAttrDao", params);
	}
	
	public void saveSiteAttrDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("setting.saveSiteAttrDao", params);
	}
	
	public void updateSiteAttrDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("setting.updateSiteAttrDao", params);
	}
}