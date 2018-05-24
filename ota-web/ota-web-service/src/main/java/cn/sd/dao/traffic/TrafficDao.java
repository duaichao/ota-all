package cn.sd.dao.traffic;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("trafficDao")
public class TrafficDao extends BaseDaoImpl implements ITrafficDao {

	public List<Map<String, Object>> getPlanTraffic(String PLAN_ID){
		return this.getSqlMapClientTemplate().queryForList("traffic.getPlanTraffic", PLAN_ID);
	}
	
	public List<Map<String, Object>> getRidTraffidIdByRuleId(String ruleids){
		return this.getSqlMapClientTemplate().queryForList("traffic.getRidTraffidIdByRuleId", ruleids);
	}
	
	public Map<String, Object> getDateSeatByRuleId(Map<String, String> params){
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("traffic.getDateSeatByRuleId", params);
	}
	
	public List<Map<String, Object>> searchPriceAttr(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.searchPriceAttr", params);
	}
	
	public List<Map<String, Object>> searchTrafficMuster(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.searchTrafficMuster", params);
	}
}
