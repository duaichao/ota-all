package cn.sd.service.b2b;

import java.util.List;
import java.util.Map;


public interface IDiscountService {
	
	public void saveDiscountService(Map<String,Object> params)throws Exception;
	public void updateDiscountService(Map<String,Object> params)throws Exception;
	public void updateDiscountUseService(Map<String,Object> params)throws Exception;
	public List<Map<String, Object>> listDiscountService(Map<String,Object> params);
	public int countDiscountService(Map<String,Object> params);
	
	
	public void saveDiscountRuleService(Map<String,Object> params)throws Exception;
	public void updateDiscountRuleService(Map<String,Object> params)throws Exception;
	public void updateDiscountRuleUseService(Map<String,Object> params)throws Exception;
	public List<Map<String, Object>> listDiscountRuleService(Map<String,Object> params);
	
	public void saveDiscountProductService(Map<String,Object> params)throws Exception;
	public void delDiscountProductService(Map<String,Object> params)throws Exception;
	public List<Map<String, Object>> listDiscountProductService(Map<String,Object> params);
	
	public List<Map<String, Object>> listProduceDiscountService(Map<String,Object> params);
	
	public void saveDiscountOrderService(Map<String,Object> params)throws Exception;
	public void updateDiscountOrderService(Map<String,Object> params)throws Exception;
	public void delDiscountOrderService(Map<String,Object> params)throws Exception;
	public List<Map<String, Object>> listDiscountOrderService(Map<String,Object> params);
}
