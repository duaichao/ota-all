package cn.sd.service.b2b;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sd.core.util.CommonUtils;
import cn.sd.dao.b2b.IDiscountDao;

@Service("discountService")
public class DiscountService implements IDiscountService {
	
	@Resource
	private IDiscountDao discountDao;
	
	public void saveDiscountService(Map<String,Object> params)throws Exception{
		this.discountDao.saveDiscountDao(params);
	}
	public void updateDiscountService(Map<String,Object> params)throws Exception{
		this.discountDao.updateDiscountDao(params);
	}
	public void updateDiscountUseService(Map<String,Object> params)throws Exception{
		this.discountDao.updateDiscountUseDao(params);
	}
	public List<Map<String, Object>> listDiscountService(Map<String,Object> params){
		return this.discountDao.listDiscountDao(params);
	}
	public int countDiscountService(Map<String,Object> params){
		return this.discountDao.countDiscountDao(params);
	}
	
	
	public void saveDiscountRuleService(Map<String,Object> params)throws Exception{
		this.discountDao.saveDiscountRuleDao(params);
	}
	public void updateDiscountRuleService(Map<String,Object> params)throws Exception{
		this.discountDao.updateDiscountRuleDao(params);
	}
	public void updateDiscountRuleUseService(Map<String,Object> params)throws Exception{
		this.discountDao.updateDiscountRuleUseDao(params);
	}
	public List<Map<String, Object>> listDiscountRuleService(Map<String,Object> params){
		return this.discountDao.listDiscountRuleDao(params);
	}
	
	public void saveDiscountProductService(Map<String,Object> params)throws Exception{
		this.discountDao.saveDiscountProductDao(params);
	}
	public void delDiscountProductService(Map<String,Object> params)throws Exception{
		this.discountDao.delDiscountProductDao(params);
	}
	public List<Map<String, Object>> listDiscountProductService(Map<String,Object> params){
		return this.discountDao.listDiscountProductDao(params);
	}
	
	public List<Map<String, Object>> listProduceDiscountService(Map<String,Object> params){
		return this.discountDao.listProduceDiscountDao(params);
	}
	
	public void saveDiscountOrderService(Map<String,Object> params)throws Exception{
		Map<String, Object> _params = new HashMap<String, Object>();
		_params.put("ID", params.get("DISCOUNT_ID"));
		_params.put("LIMIT_DATE", "YRS");
		_params.put("IS_USE", "0");
		_params.put("PRO_ID", params.get("PRO_ID"));
		List<Map<String, Object>> discounts = this.discountDao.listProduceDiscountDao(_params);
		if(CommonUtils.checkList(discounts) && discounts.size() == 1){
			Map<String, Object> discount = discounts.get(0);
			params.put("ID", CommonUtils.uuid());
			params.put("IS_REFUND", discount.get("IS_REFUND"));
			params.put("BEGIN_TIME", discount.get("BEGIN_TIME"));
			params.put("END_TIME", discount.get("END_TIME"));
			this.discountDao.saveDiscountOrderDao(params);
		}
	}
	
	public void updateDiscountOrderService(Map<String,Object> params)throws Exception{
		this.discountDao.updateDiscountOrderDao(params);
	}
	
	public void delDiscountOrderService(Map<String,Object> params)throws Exception{
		this.discountDao.delDiscountOrderDao(params);
	}
	
	public List<Map<String, Object>> listDiscountOrderService(Map<String,Object> params){
		return this.discountDao.listDiscountOrderDao(params);
	}
}
