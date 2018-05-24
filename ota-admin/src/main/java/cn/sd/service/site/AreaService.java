package cn.sd.service.site;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sd.core.util.CommonUtils;
import cn.sd.dao.site.IAreaDao;

@Service("areaService")
public class AreaService implements IAreaService{
	
	@Resource
	private IAreaDao areaDao;
	
	public List<Map<String, Object>> listService(Map<String,Object> params){
		return this.areaDao.listDao(params);
	}
	
	public void saveService(Map<String,Object> params) throws Exception{
		String ID = (String)params.get("ID");
		if(!CommonUtils.checkString(ID)){
			params.put("ID", CommonUtils.uuid());
			this.areaDao.addDao(params);
		}else{
			this.areaDao.editDao(params);
			//修改区域名称要更新关系表里面的区域名称
			this.areaDao.editAreaNameDao(params);
		}
	}
	
	public int delService(String ID) throws Exception{
		int i = 0;
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("LABEL_ID", ID);
		List<Map<String, Object>> cityLabels = this.areaDao.listCityLabelDao(p);
		if(!CommonUtils.checkList(cityLabels)){
			this.areaDao.delDao(ID);
		}else{
			i = -1;
		}
		return i;
	}
	
	public List<Map<String, Object>> listCityLabelService(Map<String,Object> params){
		return this.areaDao.listCityLabelDao(params);
	}
	
	public void saveCityLabelService(Map<String,Object> params)throws Exception{
		this.areaDao.addCityLabelDao(params);
	}
	
	public void delCityLabelService(Map<String,Object> params)throws Exception{
		this.areaDao.delCityLabelDao(params);
	}
	
	public List<Map<String, Object>> listCityService(Map<String,Object> params){
		return this.areaDao.listCityDao(params);
	}
	
	public int countCityService(Map<String,Object> params){
		return this.areaDao.countCityDao(params);
	}
	
	public List<Map<String, Object>> listCountryService(Map<String,Object> params){
		return this.areaDao.listCountryDao(params);
	}
	
	public int countCountryService(Map<String,Object> params){
		return this.areaDao.countCountryDao(params);
	}
	
	public List<Map<String, Object>> listScenicService(Map<String,Object> params){
		return this.areaDao.listScenicDao(params);
	}
	
	public int countScenicService(Map<String,Object> params){
		return this.areaDao.countScenicDao(params);
	}
	
	public List<String> listCityLabelEntityIDService(Map<String,Object> params){
		return this.areaDao.listCityLabelEntityIDDao(params);
	}
	
	public int countCityAndCountryService(Map<String,Object> params){
		return this.areaDao.countCityAndCountryDao(params);
	}
	
	public List<Map<String, Object>> listCityAndCountryService(Map<String,Object> params){
		return this.areaDao.listCityAndCountryDao(params);
	}
}
