package cn.sd.service.resource;

import java.util.List;
import java.util.Map;

public interface ITrafficService {
	
	
	public List<Map<String, Object>> listRenewPriceTypeService(Map<String,Object> params);
	
	public void callProcedureRuleCompile(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listService(Map<String,Object> params);

	public int countService(Map<String,Object> params);
	
	public void saveService(Map<String,Object> params)throws Exception;
	
	public void editService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listMusterService(Map<String,Object> params);

	public void saveMusterService(Map<String,Object> params)throws Exception;
	
	public void editMusterService(Map<String,Object> params)throws Exception;
	
	public Map<Object, Object> saveTrafficRuleService(Map<String,Object> params)throws Exception;
	
	public void delRuleService(Map<String,Object> params)throws Exception;
	
	public Map<Object, Object> editTrafficRuleService(Map<String,Object> params)throws Exception;
	
	public void delTrafficRuleService(Map<String,Object> params)throws Exception;
	
	public void pubTrafficRuleService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listPriceTypeService(Map<String,Object> params);
	
	public List<Map<String, Object>> listPriceAttrService(Map<String,Object> params);
	
	public List<Map<String, Object>> listAllPriceAttrService(Map<String,Object> params);
	
	public List<Map<String, Object>> listTrafficRuleService(Map<String,Object> params);
	
	public List<Map<String, Object>> listTrafficSeatService(Map<String,Object> params);
	
	public void saveTrafficSeatService(Map<String,Object> params)throws Exception;
	
	public void editTrafficSeatService(Map<String,Object> params)throws Exception;
	
	public void saveTrafficRuleExpandService(Map<String,Object> params)throws Exception;
	
	public void delTrafficRuleExpandService(Map<String,Object> params)throws Exception;
	
	public void saveBasePriceService(Map<String,Object> params)throws Exception;
	
	public void delBasePriceService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listTrafficRuleCompileService(Map<String,Object> params);
	
	public List<Map<String, Object>> listTrafficRuleDateService(Map<String,Object> params);
	
	public List<Map<String, Object>> listTrafficRuleSeatService(Map<String,Object> params);
	
	public void saveTrafficRuleSeatService(Map<String,Object> params)throws Exception;
	
	public void delTrafficRuleSeatService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> saleSeatNumService(Map<String,Object> params);
	
	public Map<String, Object> editSeatService(Map<String,Object> params)throws Exception;
	
	public int countTrafficRuleDateService(Map<String,Object> params);
	
	public List<Map<String, Object>> listBasePriceService(Map<String,Object> params);
	
	public List<Map<String, Object>> listBasePriceByAttrService(Map<String,Object> params);
	
	public List<Map<String, Object>> listMusterPlaceService(Map<String,Object> params);
	
	public void editPubTrafficRuleService(Map<String,Object> params)throws Exception;
	
}
