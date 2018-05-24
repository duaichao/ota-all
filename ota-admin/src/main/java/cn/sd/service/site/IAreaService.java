package cn.sd.service.site;

import java.util.List;
import java.util.Map;

public interface IAreaService {
	
	public List<Map<String, Object>> listService(Map<String,Object> params);
	
	public void saveService(Map<String,Object> params)throws Exception;
	
	public int delService(String ID) throws Exception;
	
	public List<Map<String, Object>> listCityLabelService(Map<String,Object> params);
	
	public void saveCityLabelService(Map<String,Object> params)throws Exception;
	
	public void delCityLabelService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listCityService(Map<String,Object> params);
	
	public int countCityService(Map<String,Object> params);
	
	public List<Map<String, Object>> listCountryService(Map<String,Object> params);
	
	public int countCountryService(Map<String,Object> params);
	
	public List<Map<String, Object>> listScenicService(Map<String,Object> params);
	
	public int countScenicService(Map<String,Object> params);
	
	public List<String> listCityLabelEntityIDService(Map<String,Object> params);
	
	public int countCityAndCountryService(Map<String,Object> params);
	
	public List<Map<String, Object>> listCityAndCountryService(Map<String,Object> params);
}
