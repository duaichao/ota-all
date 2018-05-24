package cn.sd.dao.produce;

import java.util.List;
import java.util.Map;

public interface IRouteDao {
	
	public void saveWapRecommendCityDao(Map<String, Object> params);
	public void delWapRecommendCityDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listRenewDao(Map<String, Object> params);
	public int countRenewDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listDao(Map<String,Object> params);

	public int countDao(Map<String,Object> params);

	public List<Map<String, Object>> listCompanyDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listLabelDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listRouteTrafficMusterDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listSingleRouteDao(Map<String,Object> params);

	public int countSingleRouteDao(Map<String,Object> params);
	
	public void saveWapPriceDao(Map<String,Object> params);
	public void delWapPriceDao(Map<String,Object> params);
	public List<Map<String, Object>> listWapPriceDao(Map<String,Object> params);
	
}
