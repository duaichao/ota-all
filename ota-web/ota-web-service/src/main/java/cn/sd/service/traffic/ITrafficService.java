package cn.sd.service.traffic;

import java.util.List;
import java.util.Map;

public interface ITrafficService {

	public List<Map<String, Object>> searchTrafficMuster(Map<String, Object> params);
	public List<Map<String, Object>> searchPriceAttr(Map<String, Object> params);
	
}
