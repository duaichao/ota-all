package cn.sd.dao.order;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;

@Repository
@Service("orderDao")
public class OrderDao extends BaseDaoImpl implements IOrderDao {

	public void updateOrderAccountByIDS(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("order.updateOrderAccountByIDS", params);
	}
	
	public List<Map<String, Object>> searchOrderAccountPayFinish(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("order.searchOrderAccountPayFinish", params);
	}
	
	public List<Map<String, Object>> searchOrderAccount(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("order.searchOrderAccount", params);
	}
	
	public void saveOrderContact(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("order.saveOrderContact", params);
	}
	
	public List<Map<String, Object>> listOrderContact(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("order.listOrderContact", params);
	}
	
	public void orderCancel(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("order.orderCancel", params);
	}
	
	public void saveOrderPrice(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("order.saveOrderPrice", params);
	}
	
	public void saveOrderPriceMain(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("order.saveOrderPriceMain", params);
	}
	
	public void saveOrderTraffic(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("order.saveOrderTraffic", params);
	}
	
	public void saveOrderTrafficDetail(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("order.saveOrderTrafficDetail", params);
	}
	
	public void saveOrder(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("order.saveOrder", params);
	}
	
	public void saveOrderStep(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("order.saveOrderStep", params);
	}
	
	public List<Map<String, Object>> searchOrderBase(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("order.searchOrderBase", params);
	}
	
	public int countOrderBase(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("order.countOrderBase", params);
	}
	
	public void orderLost(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("order.orderLost", params);
	}
	
	public void reSeatByEntityIdDao(Map<String, Object> params) throws Exception{
		String ID = params.get("ID").toString();
		this.getSqlMapClientTemplate().delete("order.reSeatByEntityIdDao", ID);
		this.getSqlMapClientTemplate().insert("order.reSeatStepByEntityIdDao", params);
		this.getSqlMapClientTemplate().update("order.reSeatStatusByEntityIdDao", ID);
		
		List list = this.getSqlMapClientTemplate().queryForList("order.reSeatStatusByEntityIdListDao", ID);
		
		Map<String, Object> params1 = new HashMap<String, Object>();
		for(int i=0;i<list.size();i++){
			Map data = (Map)list.get(i);
			String IS_ALONE = String.valueOf(data.get("IS_ALONE"));
			String SOURCES = String.valueOf(data.get("SOURCES"));
			if(IS_ALONE.equals("0") && SOURCES.equals("0")){
				params1.put("COMPANY_ID", (String)data.get("COMPANY_ID"));
				params1.put("CREATE_TIME", (String)data.get("CREATE_TIME"));
				
				this.editOrderAccountService(params1);
				
			}
		}
	}
	
	public void editOrderAccountService(Map<String, Object> params)throws Exception{
		
		Date CREATE_TIME = DateUtil.parseDate((String)params.get("CREATE_TIME"));
		String YYYYMM = DateUtil.parseDate(CREATE_TIME, "yyyy-MM");
		Date d = DateUtil.parseDate(YYYYMM+"-15");
		if(CREATE_TIME.compareTo(d) > 0){
			this.dateInterval(params, YYYYMM, "2");
		}else{
			this.dateInterval(params, YYYYMM, "1");
		}
		
		List<Map<String, Object>> data = this.getSqlMapClientTemplate().queryForList("order.listOrderAccountDao", params);
		if(CommonUtils.checkList(data)){
			params.put("ACCOUNT_ID", data.get(0).get("ID"));
			this.updateAccountStatusService(params);
		}
	}
	
	public void updateAccountStatusService(Map<String, Object> params) throws Exception{
		List<Map<String, Object>> data = this.getSqlMapClientTemplate().queryForList("order.listOrderAccountPayFinishDao", params);
		if(CommonUtils.checkList(data)){
			params.put("ACCOUNT_STATUS", "1");
			this.getSqlMapClientTemplate().update("order.updateOrderAccountByIDSDao", params);
		}
	}
	
	private Map<String, Object> dateInterval(Map<String, Object> params, String beginMonth, String beginType){
		if(beginType.equals("1")){
			params.put("CREATE_BEGIN_TIME", beginMonth+"-01");
			params.put("CREATE_END_TIME", beginMonth+"-15");	
		}else{
			String end_date = DateUtil.formatString(DateUtil.getLastDayOfMonty(DateUtil.parseDate(beginMonth+"-16")), "yyyy-MM-dd");
			params.put("CREATE_BEGIN_TIME", beginMonth+"-16");
			params.put("CREATE_END_TIME", end_date);
		}
		return params;
	}
	
	public void updateOrderStatus(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("order.updateOrderStatus", params);
	}
	
	public void saveOrderFundsDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("order.saveOrderFundsDao", params);
	}
	
	public List<Map<String, Object>> listOrderPriceDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("order.listOrderPriceDao", params);
	}
	public List<Map<String, Object>> listVisitorGroupByTrafficAttrDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("order.listVisitorGroupByTrafficAttrDao", params);
	}
	public List<Map<String, Object>> listDiscountOrderDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("order.listDiscountOrderDao", params);
	}
	public List<Map<String, Object>> listDiscountProductDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("order.listDiscountProductDao", params);
	}
	public void updateDiscountOrderDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("order.updateDiscountOrderDao", params);
	}
	public void delDiscountOrderDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("order.delDiscountOrderDao", params);
	}
	public List<Map<String, Object>> searchOrderRouteTraffic(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("order.searchOrderRouteTraffic", params);
	}
}
