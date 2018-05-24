package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

public interface IDiscountDao {
	
	public void saveDiscountDao(Map<String,Object> params);
	public void updateDiscountDao(Map<String,Object> params);
	public void updateDiscountUseDao(Map<String,Object> params);
	public List<Map<String, Object>> listDiscountDao(Map<String,Object> params);
	public int countDiscountDao(Map<String,Object> params);
	
	
	public void saveDiscountRuleDao(Map<String,Object> params);
	public void updateDiscountRuleDao(Map<String,Object> params);
	public void updateDiscountRuleUseDao(Map<String,Object> params);
	public List<Map<String, Object>> listDiscountRuleDao(Map<String,Object> params);
	
	public void saveDiscountProductDao(Map<String,Object> params);
	public void delDiscountProductDao(Map<String,Object> params);
	public List<Map<String, Object>> listDiscountProductDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listProduceDiscountDao(Map<String,Object> params);
	
	public void saveDiscountOrderDao(Map<String,Object> params);
	public void updateDiscountOrderDao(Map<String,Object> params);
	public void delDiscountOrderDao(Map<String,Object> params);
	public List<Map<String, Object>> listDiscountOrderDao(Map<String,Object> params);
	
	
}
