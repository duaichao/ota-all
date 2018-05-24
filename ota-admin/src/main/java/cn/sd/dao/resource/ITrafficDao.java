package cn.sd.dao.resource;

import java.util.List;
import java.util.Map;

public interface ITrafficDao {
	
	public List<Map<String, Object>> listRenewPriceTypeDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listDao(Map<String,Object> params);

	public int countDao(Map<String,Object> params);
	
	public void saveDao(Map<String,Object> params)throws Exception;
	
	public void editDao(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listMusterDao(Map<String,Object> params);

	public void saveMusterDao(Map<String,Object> params)throws Exception;
	
	public void editMusterDao(Map<String,Object> params)throws Exception;
	
	public void saveTrafficRuleDao(Map<String,Object> params)throws Exception;
	
	public void delRuleDao(Map<String,Object> params)throws Exception;
	
	public void editTrafficRuleDao(Map<String,Object> params)throws Exception;
	
	public void delTrafficRuleDao(Map<String,Object> params)throws Exception;
	
	public void pubTrafficRuleDao(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listPriceTypeDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listPriceAttrDao(Map<String,Object> params);
	public List<Map<String, Object>> listAllPriceAttrDao(Map<String,Object> params);

	public List<Map<String, Object>> listTrafficRuleDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listTrafficSeatDao(Map<String,Object> params);
	
	public void saveTrafficSeatDao(Map<String,Object> params)throws Exception;
	
	public void editTrafficSeatDao(Map<String,Object> params)throws Exception;
	
	public void saveTrafficRuleExpandDao(Map<String,Object> params)throws Exception;
	
	public void delTrafficRuleExpandDao(Map<String,Object> params)throws Exception;
	
	public void saveBasePriceDao(Map<String,Object> params)throws Exception;
	
	public void delBasePriceDao(Map<String,Object> params)throws Exception;
	
	public void callProcedureRuleCompile(Map<String,Object> params)throws Exception;
	
	public void callProcedureTrfficMinprice(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listTrafficRuleCompileDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listTrafficRuleDateDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listTrafficRuleSeatDao(Map<String,Object> params);
	
	public void saveTrafficRuleSeatDao(Map<String,Object> params)throws Exception;
	
	public void delTrafficRuleSeatDao(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> saleSeatNumDao(Map<String,Object> params);
	
	public int countTrafficRuleDateDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listBasePriceDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listBasePriceByAttrDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listMusterPlaceDao(Map<String,Object> params);
	
	public void delRuleExpandDao(Map<String,Object> params)throws Exception;
	
	public void delProcedureRuleCompileDao(Map<String,Object> params)throws Exception;
	
}
