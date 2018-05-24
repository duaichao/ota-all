package cn.sd.dao.produce;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("produceTrafficDao")
@SuppressWarnings("all")
public class TrafficDao extends BaseDaoImpl implements ITrafficDao{
	
	public List<Map<String, Object>> listDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("produceTraffic.listDao", params);
	}
	
	public int countDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("produceTraffic.countDao", params);
	}
	
	public String seatSql(String RULE_ID){
		return (String)this.getSqlMapClientTemplate().queryForObject("produceTraffic.seatSql", RULE_ID);
	}
	
	public int isSeatNum(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("produceTraffic.isSeatNum", params);
	}
	
	public void saveTrafficSeat(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("produceTraffic.saveTrafficSeat", params);
	}
	
	public List<Map<String, Object>> getPlanTraffic(String PLAN_ID){
		return this.getSqlMapClientTemplate().queryForList("produceTraffic.getPlanTraffic", PLAN_ID);
	}
	
	public List<Map<String, Object>> getRuleIdByDate(String sql){
		return this.getSqlMapClientTemplate().queryForList("produceTraffic.getRuleIdByDate", sql);
	}

	public Map<String, Object> getDateSeatByRuleId(Map<String, String> params) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("produceTraffic.getDateSeatByRuleId", params);
	}
	
	public List<Map<String, Object>> getRidTraffidIdByRuleId(String ruleids){
		return this.getSqlMapClientTemplate().queryForList("produceTraffic.getRidTraffidIdByRuleId", ruleids);
	}
	
}
