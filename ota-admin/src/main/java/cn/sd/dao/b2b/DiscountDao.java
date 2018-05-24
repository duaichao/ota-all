package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("discountDao")

public class DiscountDao extends BaseDaoImpl implements IDiscountDao{
	
	
	public void saveDiscountDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("discount.saveDiscountDao", params);
	}
	public void updateDiscountDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("discount.updateDiscountDao", params);
	}
	
	public void updateDiscountUseDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("discount.updateDiscountUseDao", params);
	}
	public List<Map<String, Object>> listDiscountDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("discount.listDiscountDao", params);
	}
	public int countDiscountDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("discount.countDiscountDao", params);
	}
	
	
	public void saveDiscountRuleDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("discount.saveDiscountRuleDao", params);
	}
	public void updateDiscountRuleDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("discount.updateDiscountRuleDao", params);
	}
	public void updateDiscountRuleUseDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("discount.updateDiscountRuleUseDao", params);
	}
	public List<Map<String, Object>> listDiscountRuleDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("discount.listDiscountRuleDao", params);
	}
	
	public void saveDiscountProductDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("discount.saveDiscountProductDao", params);
	}
	public void delDiscountProductDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("discount.delDiscountProductDao", params);
	}
	public List<Map<String, Object>> listDiscountProductDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("discount.listDiscountProductDao", params);
	}
	
	public List<Map<String, Object>> listProduceDiscountDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("discount.listProduceDiscountDao", params);
	}
	
	public void saveDiscountOrderDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("discount.saveDiscountOrderDao", params);
	}
	public void updateDiscountOrderDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("discount.updateDiscountOrderDao", params);
	}
	public void delDiscountOrderDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("discount.delDiscountOrderDao", params);
	}
	public List<Map<String, Object>> listDiscountOrderDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("discount.listDiscountOrderDao", params);
	}
}
