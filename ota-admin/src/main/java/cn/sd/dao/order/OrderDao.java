package cn.sd.dao.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.service.finance.IFinanceService;

@Repository
@Service("orderDao")
public class OrderDao extends BaseDaoImpl implements IOrderDao{
	
	
	@Resource
	private IFinanceService financeService;

	public void delOrderDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.delOrderDao", params);
	}
	
	public void saveOrderContactDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("trafficOrder.saveOrderContactDao", params);
	}
	public void delOrderContactDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().delete("trafficOrder.delOrderContactDao", params);
	}
	public List<Map<String, Object>> listOrderContactDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listOrderContactDao", params);
	}
	
	public void batchSaveAutoRefunEarnestStepDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("trafficOrder.batchSaveAutoRefunEarnestStepDao", params);
	}
	
	public void batchUpdateAtuoRefunEarnestStatusDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.batchUpdateAtuoRefunEarnestStatusDao", params);
	}
	
	public List<Map<String, Object>> listRefundEarnestDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listRefundEarnestDao", params);
	}
	
	public void batchSaveSaleAtuoRefunEarnestFundsDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("trafficOrder.batchSaveSaleAtuoRefunEarnestFundsDao", params);
	}
	public void batchSaveBuyAtuoRefunEarnestFundsDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("trafficOrder.batchSaveBuyAtuoRefunEarnestFundsDao", params);
	}
	
	public void reSeatEarnestDao(Map<String, Object> params){
		String time = DateUtil.getNowDateTimeString();
		this.getSqlMapClientTemplate().update("trafficOrder.reSeatEarnestDao", time);
		this.getSqlMapClientTemplate().update("trafficOrder.reSeatEarnestStepDao", time);
		this.getSqlMapClientTemplate().update("trafficOrder.batchEarnestRestContractAgencyDao", params);
		this.getSqlMapClientTemplate().update("trafficOrder.reSeatEarnestStatusDao", params);
	}
	
	public void batchResetEarnestTypeDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.batchResetEarnestTypeDao", params);
	}
	public void batchSaveEarnestTypeStepDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.batchSaveEarnestTypeStepDao", params);
	}
	
	public void updateOrderBaseStatusDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateOrderBaseStatusDao", params);
	}
	
	public void updateOrderEarnestStatusByIdDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateOrderEarnestStatusByIdDao", params);
	}
	
	public void confirmInsteadPayDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.confirmInsteadPayDao", params);
	}
	
	public void updateOrderEarnestInfoDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateOrderEarnestInfoDao", params);
	}
	
	public void updateOrderEarnestStatusDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateOrderEarnestStatusDao", params);
	}
	
	public void updateOrderBaseRemarkDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateOrderBaseRemarkDao", params);
	}
	
	public List<Map<String, Object>> searchOrderPriceDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.searchOrderPriceDao", params);
	}
	public List<Map<String, Object>> searchOrderPriceMainDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.searchOrderPriceMainDao", params);
	}
	
	public void updateStartConfirmByIdDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateStartConfirmByIdDao", params);
	}
	
	public void saveCancelRenewOrderStepDao(){
		this.getSqlMapClientTemplate().insert("trafficOrder.saveCancelRenewOrderStepDao");
	}
	public void batchRenewRestContractAgencyDao(){
		this.getSqlMapClientTemplate().update("trafficOrder.batchRenewRestContractAgencyDao");
	}
	public void cancelRenewOrderDao(){
		this.getSqlMapClientTemplate().update("trafficOrder.cancelRenewOrderDao");
	}
	
	public void updateRenewStatusDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateRenewStatusDao", params);
	}
	
	public void delOrderPriceDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().delete("trafficOrder.delOrderPriceDao", params);
	}
	
	public void saveOrderRenewPriceDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("trafficOrder.saveOrderRenewPriceDao", params);
	}
	
	public void restContractAgencyDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.restContractAgencyDao", params);
	}
	public void restOrderContractAgencyDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.restOrderContractAgencyDao", params);
	}
	
	
	public void morePayCheckDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.morePayCheckDao", params);
	}
	
	public void updateOrderInterAuditDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateOrderInterAuditDao", params);
	}
	
	public void updateOrderInterFloatTempDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateOrderInterFloatTempDao", params);
	}
	
	public void updateOrderInterFloatDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateOrderInterFloatDao", params);
	}
	
	public void updateOrderSaleFloatDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateOrderSaleFloatDao", params);
	}
	
	public List<Map<String, Object>> listOrderOtherPriceDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listOrderOtherPriceDao", params);
	}
	
	public void updateOrderOtherAuditDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateOrderOtherAuditDao", params);
	}

	public void saveOrderOtherPriceDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("trafficOrder.saveOrderOtherPriceDao", params);
	}
	
	public void delOrderOtherPriceDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("trafficOrder.delOrderOtherPriceDao", params);
	}
	
	public Map<String, Object> detailOrderDao(Map<String, Object> params){
		List<Map<String, Object>> data = this.getSqlMapClientTemplate().queryForList("trafficOrder.detailOrderDao", params);
		if(CommonUtils.checkList(data) && data.size() == 1){
			return data.get(0);
		}
		return null;
	}
	
	public Map<String, Object> detailContractAgencyByNoDao(Map<String, Object> params){
		List<Map<String, Object>> data = this.getSqlMapClientTemplate().queryForList("trafficOrder.detailContractAgencyByNoDao", params);
		if(CommonUtils.checkList(data) && data.size() == 1){
			return data.get(0);
		}
		return null;
	}
	
	public void updateOrderConNoDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("trafficOrder.updateOrderConNoDao", params);
	}
	
	public String getOrderNoDao(String PREFIX) throws Exception{
		String no="000001";
		String len = "6";
		if(PREFIX.indexOf("ZXZFONLINE")!=-1){
			no="000000001";
			len="9";
		}
		Map map = new HashMap();
		map.put("PREFIX", PREFIX);
		map.put("CURR_NO", no);
		Integer i = (Integer)this.getSqlMapClientTemplate().queryForObject("trafficOrder.existOrderNo", PREFIX);
		
		if(i==0){
			this.getSqlMapClientTemplate().insert("trafficOrder.saveOrderNo", map);
			return PREFIX+no;
		}else{
			map.put("LEN", len);
			no= (String)this.getSqlMapClientTemplate().queryForObject("trafficOrder.getOrderNo", map);
			String tmp_no = no.replaceFirst(PREFIX, "");
			map.put("CURR_NO", tmp_no);
			this.getSqlMapClientTemplate().update("trafficOrder.updateOrderNo", map);	
		}
		return no;
	}
	
	public String getPrefix(String company_id){
		String perfix = "";
		perfix = (String)this.getSqlMapClientTemplate().queryForObject("trafficOrder.getPrefix", company_id);
		if("".equals(perfix) || perfix == null) return null;
		return perfix;
	}

	public void saveDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("trafficOrder.saveDao", params);
	}
	public void updateDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("trafficOrder.updateDao", params);
	}
	
	public void saveOrderPriceDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("trafficOrder.saveOrderPriceDao", params);
	}
	
	public List<Map<String, Object>> listDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listDao", params);
	}
	
	public int countDao(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("trafficOrder.countDao", params);
	}
	
	public List<Map<String, Object>> listOrderPriceDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listOrderPriceDao", params);
	}
	
	public List<Map<String, Object>> listOrderStepDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listOrderStepDao", params);
	}
	
	public void saveOrderStepDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("trafficOrder.saveOrderStepDao", params);
	}
	
	public List<Map<String, Object>> listOrderFundsDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listOrderFundsDao", params);
	}
	
	public void saveOrderFundsDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("trafficOrder.saveOrderFundsDao", params);
	}
	
	public void saveOrderPriceMainDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("trafficOrder.saveOrderPriceMainDao", params);
	}
	public void saveOrderTrafficDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("trafficOrder.saveOrderTrafficDao", params);
	}
	public void saveOrderTrafficDetailDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("trafficOrder.saveOrderTrafficDetailDao", params);
	}
	
	public List<Map<String, Object>> listOrderTrafficDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listOrderTrafficDao", params);
	}
	
	public List<Map<String, Object>> listOrderTrafficDetailDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listOrderTrafficDetailDao", params);
	}
	
	public List<Map<String, Object>> listRouteOrderDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listRouteOrderDao", params);
	}
	public int countRouteOrderDao(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("trafficOrder.countRouteOrderDao", params);
	}
	
	public List<Map<String, Object>> listVisitorGroupByTrafficAttrDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listVisitorGroupByTrafficAttrDao", params);
	}
	
	public void delOrderTrafficDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("trafficOrder.delOrderTrafficDao", params);
	}
	
	public void delOrderTrafficDetailDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("trafficOrder.delOrderTrafficDetailDao", params);
	}
	
	public void delOrderPriceMainDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("trafficOrder.delOrderPriceMainDao", params);
	}
	
	public List<Map<String, Object>> listRefundsDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listRefundsDao", params);
	}
	
	public void saveContractLogDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("trafficOrder.saveContractLogDao", params);
	}
	
	public List<Map<String, Object>> listOrderDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.listOrderDao", params);
	}
	
	public void saveOrderContractDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("trafficOrder.saveOrderContractDao", params);
	}
	
	public void updateOrderContractDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("trafficOrder.updateOrderContractDao", params);
	}
	
	public void saveOrderBankDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("trafficOrder.saveOrderBankDao", params);
	}
	public void updateOrderBankDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("trafficOrder.updateOrderBankDao", params);
	}
	
	
	public void reSeatOnlineDao(String orderId, String remark) throws Exception{
		
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("ID", orderId);
		List<Map<String, Object>> orders = this.getSqlMapClientTemplate().queryForList("trafficOrder.detailOrderDao", p);
		String IS_LOST = String.valueOf(orders.get(0).get("IS_LOST"));
		if(IS_LOST.equals("1")){
			this.getSqlMapClientTemplate().insert("trafficOrder.saveTrafficSeatTempDao", orderId);
		}
		this.getSqlMapClientTemplate().delete("trafficOrder.reSeatOnlineDao",orderId);
		p.clear();
		p.put("orderId", orderId);
		p.put("remark", remark);
		this.getSqlMapClientTemplate().insert("trafficOrder.reSeatStepOnlineDao",p);
		this.getSqlMapClientTemplate().update("trafficOrder.batchRestContractAgencyOnlineDao", orderId);
		this.getSqlMapClientTemplate().update("trafficOrder.reSeatStatusOnlineDao",orderId);
	}
	
	public void reSeatYYDao() throws Exception{
		String time = DateUtil.getNowDateTimeString();
		this.getSqlMapClientTemplate().update("trafficOrder.batchYYRestContractAgencyDao", time);
		this.getSqlMapClientTemplate().update("trafficOrder.reSeatYYStatusDao",time);
	}
	
	
	public void reSeatDao() throws Exception{
		String time = DateUtil.getNowDateTimeString();
		this.getSqlMapClientTemplate().delete("trafficOrder.reSeatDao",time);
		this.getSqlMapClientTemplate().insert("trafficOrder.reSeatStepDao",time);
		this.getSqlMapClientTemplate().update("trafficOrder.batchRestContractAgencyDao", time);
		this.getSqlMapClientTemplate().update("trafficOrder.reSeatStatusDao",time);
		
		
//		List list = this.getSqlMapClientTemplate().queryForList("trafficOrder.reSeatStatusListDao", time);
//		
//		Map<String, Object> params = new HashMap<String, Object>();
//		for(int i=0;i<list.size();i++){
//			Map data = (Map)list.get(i);
//			String IS_ALONE = String.valueOf(data.get("IS_ALONE"));
//			String SOURCES = String.valueOf(data.get("SOURCES"));
//			params.put("COMPANY_ID", (String)data.get("COMPANY_ID"));
//			params.put("CREATE_TIME", (String)data.get("CREATE_TIME"));
//			if(IS_ALONE.equals("0") && SOURCES.equals("0")){
//				params.put("ACCOUNT", "0");
//				params.put("ACCOUNT_TYPE", "0");
//				params.put("CREATE_COMPANY_ID", data.get("CREATE_COMPANY_ID"));
//			}else{
//				params.put("ACCOUNT", "-1");
//				params.put("ACCOUNT_TYPE", "1");
//				params.put("CREATE_COMPANY_ID", data.get("CREATE_COMPANY_ID"));
//				params.put("ACCOUNT_COMPANY_ID", data.get("CREATE_COMPANY_ID"));
//			}
//			this.financeService.editOrderAccountService(params);
//		}
		
		
	}
	
	public void reSeatByEntityIdDao(Map<String, Object> params) throws Exception{
		String ID = params.get("ID").toString();
		this.getSqlMapClientTemplate().delete("trafficOrder.reSeatByEntityIdDao", ID);
		this.getSqlMapClientTemplate().insert("trafficOrder.reSeatStepByEntityIdDao", params);
		this.getSqlMapClientTemplate().update("trafficOrder.reSeatStatusByEntityIdDao", ID);
		
		List list = this.getSqlMapClientTemplate().queryForList("trafficOrder.reSeatStatusByEntityIdListDao", ID);
		
		Map<String, Object> params1 = new HashMap<String, Object>();
		for(int i=0;i<list.size();i++){
			Map data = (Map)list.get(i);
			String IS_ALONE = String.valueOf(data.get("IS_ALONE"));
			String SOURCES = String.valueOf(data.get("SOURCES"));
			if(IS_ALONE.equals("0") && SOURCES.equals("0")){
				params1.put("COMPANY_ID", (String)data.get("COMPANY_ID"));
				params1.put("CREATE_TIME", (String)data.get("CREATE_TIME"));
				
				this.financeService.editOrderAccountService(params1);
				
			}
		}
	}
	public List<Map<String, Object>> searchOrderRouteTraffic(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("trafficOrder.searchOrderRouteTraffic", params);
	}
}
