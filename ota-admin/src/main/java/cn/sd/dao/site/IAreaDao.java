package cn.sd.dao.site;

import java.util.List;
import java.util.Map;

public interface IAreaDao {
	public List<Map<String, Object>> listDao(Map<String,Object> params);
	
	public void addDao(Map<String,Object> params)throws Exception;
	
	public void editDao(Map<String,Object> params)throws Exception;
	
	public void delDao(String ID)throws Exception;
	
	public void editAreaNameDao(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listCityLabelDao(Map<String,Object> params);

	public void addCityLabelDao(Map<String,Object> params)throws Exception;
	
	public void delCityLabelDao(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listCityDao(Map<String,Object> params);
	
	public int countCityDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listCountryDao(Map<String,Object> params);
	
	public int countCountryDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listScenicDao(Map<String,Object> params);
	
	public int countScenicDao(Map<String,Object> params);
	
	public List<String> listCityLabelEntityIDDao(Map<String,Object> params);
	
	public int countCityAndCountryDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listCityAndCountryDao(Map<String,Object> params);
	
}
