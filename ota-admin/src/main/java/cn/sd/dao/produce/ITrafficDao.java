package cn.sd.dao.produce;

import java.util.List;
import java.util.Map;

public interface ITrafficDao {
	
	public List<Map<String, Object>> listDao(Map<String,Object> params);

	public int countDao(Map<String,Object> params);
	
	public String seatSql(String RULE_ID);
	public int isSeatNum(Map<String,Object> params);
	public void saveTrafficSeat(Map<String,Object> params);
	public List<Map<String, Object>> getPlanTraffic(String PLAN_ID);
	public List<Map<String, Object>> getRuleIdByDate(String sql);
	public Map<String, Object> getDateSeatByRuleId(Map<String, String> params);
	public List<Map<String, Object>> getRidTraffidIdByRuleId(String ruleids);
	
}
