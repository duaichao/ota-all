package cn.sd.service.produce;

import java.util.List;
import java.util.Map;

public interface IRouteService {
	
	public List<Map<String, Object>> listRenewService(Map<String, Object> params);
	public int countRenewService(Map<String, Object> params);
	
	public List<Map<String, Object>> listService(Map<String,Object> params);

	public int countService(Map<String,Object> params);
	
	public List<Map<String, Object>> listCompanyService(Map<String,Object> params);
	
	public List<Map<String, Object>> listLabelService(Map<String,Object> params);
	
	public List<Map<String, Object>> listRouteTrafficMusterService(Map<String,Object> params);
	
	public List<Map<String, Object>> listSingleRouteService(Map<String,Object> params);

	public int countSingleRouteService(Map<String,Object> params);
	
	public void saveWapPriceService(Map<String,Object> params) throws Exception;
	public void delWapPriceService(Map<String,Object> params)throws Exception;
	public List<Map<String, Object>> listWapPriceService(Map<String,Object> params);
}
