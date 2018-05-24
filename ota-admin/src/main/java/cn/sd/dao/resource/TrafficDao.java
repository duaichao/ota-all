package cn.sd.dao.resource;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("trafficDao")
@SuppressWarnings("all")
public class TrafficDao extends BaseDaoImpl implements ITrafficDao{
	
	public List<Map<String, Object>> listRenewPriceTypeDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listRenewPriceTypeDao", params);
	}
	
	public List<Map<String, Object>> listDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listDao", params);
	}
	
	public int countDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("traffic.countDao", params);
	}
	
	public void saveDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("traffic.saveDao", params);
	}
	
	public void editDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("traffic.editDao", params);
	}
	
	public List<Map<String, Object>> listMusterDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listMusterDao", params);
	}
	
	public void saveMusterDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("traffic.saveMusterDao", params);
	}
	
	public void editMusterDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("traffic.editMusterDao", params);
	}
	
	public void saveTrafficRuleDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("traffic.saveTrafficRuleDao", params);
	}
	
	public void delRuleDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().delete("traffic.delRuleDao", params);
	}
	
	public void editTrafficRuleDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("traffic.editTrafficRuleDao", params);
	}
	
	public void delTrafficRuleDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().delete("traffic.delTrafficRuleDao", params);
	}
	
	public void pubTrafficRuleDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("traffic.pubTrafficRuleDao", params);
	}
	
	public List<Map<String, Object>> listPriceTypeDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listPriceTypeDao", params);
	}
	
	public List<Map<String, Object>> listPriceAttrDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listPriceAttrDao", params);
	}
	
	public List<Map<String, Object>> listAllPriceAttrDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listAllPriceAttrDao", params);
	}
	
	public List<Map<String, Object>> listTrafficRuleDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listTrafficRuleDao", params);
	}
	
	public List<Map<String, Object>> listTrafficSeatDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listTrafficSeatDao", params);
	}
	
	public void saveTrafficSeatDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("traffic.saveTrafficSeatDao", params);
	}
	
	public void editTrafficSeatDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("traffic.editTrafficSeatDao", params);
	}
	
	public void saveTrafficRuleExpandDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("traffic.saveTrafficRuleExpandDao", params);
	}
	
	public void delTrafficRuleExpandDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("traffic.delTrafficRuleExpandDao", params);
	}
	
	public void saveBasePriceDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("traffic.saveBasePriceDao", params);
	}
	
	public void delBasePriceDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("traffic.delBasePriceDao", params);
	}
	
	public void callProcedureRuleCompile(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("traffic.callProcedureRuleCompile", params);
	}
	
	public void callProcedureTrfficMinprice(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("traffic.callProcedureTrfficMinprice", params);
	}
	
	public List<Map<String, Object>> listTrafficRuleCompileDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listTrafficRuleCompileDao", params);
	}
	
	public List<Map<String, Object>> listTrafficRuleDateDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listTrafficRuleDateDao", params);
	}
	
	public List<Map<String, Object>> listTrafficRuleSeatDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listTrafficRuleSeatDao", params);
	}
	
	public void saveTrafficRuleSeatDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("traffic.saveTrafficRuleSeatDao", params);
	}
	
	public void delTrafficRuleSeatDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().delete("traffic.delTrafficRuleSeatDao", params);
	}
	
	public List<Map<String, Object>> saleSeatNumDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.saleSeatNumDao", params);
	}
	
	public int countTrafficRuleDateDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("traffic.countTrafficRuleDateDao", params);
	}
	
	public List<Map<String, Object>> listBasePriceDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listBasePriceDao", params);
	}
	
	public List<Map<String, Object>> listBasePriceByAttrDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listBasePriceByAttrDao", params);
	}
	
	public List<Map<String, Object>> listMusterPlaceDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("traffic.listMusterPlaceDao", params);
	}
	
	public void delRuleExpandDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().delete("traffic.delRuleExpandDao", params);
	}
	
	public void delProcedureRuleCompileDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().delete("traffic.delProcedureRuleCompileDao", params);
	}
}
