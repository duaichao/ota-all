package cn.sd.dao.traffic;

import java.util.List;
import java.util.Map;

import cn.sd.core.dao.IBaseDao;

public interface ITrafficDao extends IBaseDao{

	public List<Map<String, Object>> getPlanTraffic(String PLAN_ID);
	public List<Map<String, Object>> getRidTraffidIdByRuleId(String ruleids);
	public Map<String, Object> getDateSeatByRuleId(Map<String, String> params);
	public List<Map<String, Object>> searchPriceAttr(Map<String, Object> params);
	
	public List<Map<String, Object>> searchTrafficMuster(Map<String, Object> params);
	
}
