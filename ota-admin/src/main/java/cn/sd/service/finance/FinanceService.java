package cn.sd.service.finance;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.dao.finance.IFinanceDao;

@Repository
@Service("financeService")
public class FinanceService implements IFinanceService {

	@Resource
	private IFinanceDao financeDao;
	
	public List<Map<String, Object>> listCalcService(Map<String, Object> params){
		return this.financeDao.listCalcDao(params);
	}
	
	public List<Map<String, Object>> supplyCalcService(Map<String, Object> params){
		return this.financeDao.supplyCalcDao(params);
	}
	public List<Map<String, Object>> managerCalcService(Map<String, Object> params){
		return this.financeDao.managerCalcDao(params);
	}
	public List<Map<String, Object>> saleCalcService(Map<String, Object> params){
		return this.financeDao.saleCalcDao(params);
	}
	
	public void updateOrderAccountStatusService(Map<String, Object> params) throws Exception{
		this.financeDao.updateOrderAccountStatusDao(params);
	}

	public void updateOrderAccountInfoService(Map<String, Object> params)throws Exception{
		this.financeDao.updateOrderAccountInfoDao(params);
	}
	public void saveOrderAccountService(Map<String, Object> params)throws Exception{
		this.financeDao.saveOrderAccountDao(params);
	}
	public void updateOrderAccountService(Map<String, Object> params)throws Exception{
		this.financeDao.updateOrderAccountDao(params);
	}
	
	private void saveOrderAccountDetailService(Map<String, Object> params, String ACCOUNT_ID, String ACCOUNT_DETAIL_NO, String COMPANY_TYPE, Map<String, Object> otherParams, int cnt)throws Exception{
		
		String ACCOUNT_DETAIL_ID = CommonUtils.uuid();
		
		params.put("ID", ACCOUNT_DETAIL_ID);
		params.put("ACCOUNT_ID", ACCOUNT_ID);
		params.put("NO", ACCOUNT_DETAIL_NO);
		params.put("TYPE", 0);
		params.put("COMPANY_TYPE", COMPANY_TYPE);
		params.put("ACCOUNT_USER", otherParams.get("CREATE_USER"));
		params.put("ACCOUNT_USER_ID", otherParams.get("CREATE_USER_ID"));
		params.put("DETAIL_NO", ACCOUNT_DETAIL_NO+"-"+String.valueOf(cnt));
		this.financeDao.saveOrderAccountDetailDao(params);
	}
	public void saveOrderAccountDetailNOService(Map<String, Object> params)throws Exception{
		this.financeDao.saveOrderAccountDetailNODao(params);
	}
	
	private String accountType(String companyType){
		if(companyType.equals("0")){
			return "0";
		}else{
			return "1";
		}
	}
	/**
	 * 2.区间内的订单是否都已对账完成（除取消订单）
	 * 3.区间初次对账对账主表保存,否则更新。只更新-对账状态
	 * 4.保存编号表
	 * 5.保存对账子表
	 * 6.更新订单表的 对账信息（订单编号）
	 */
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> confirmOrderAccountService(Map<String, Object> p)throws Exception{
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		
		String ACCOUNT_ID = "";
		String ACCOUNT_DETAIL_NO_ID = CommonUtils.uuid();
		String ACCOUNT_DETAIL_NO = DateUtil.getNowDateTimeString().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
		
		//是否存在对账记录
		p.put("start", 1);
		p.put("end", 10000);
		String COMPANY_TYPE = this.accountType((String)p.get("COMPANY_TYPE"));
		p.put("ACCOUNT_TYPE", COMPANY_TYPE);
		List<Map<String, Object>> orderAccount = this.financeDao.listOrderAccountDao(p);
		
		if(!CommonUtils.checkList(orderAccount)){
			//保存 对账状态 默认0 1全部对完2部分对完
			ACCOUNT_ID = CommonUtils.uuid();
			params.clear();
			params.put("ID", ACCOUNT_ID);
			params.put("START_DATE", p.get("CREATE_BEGIN_TIME"));
			params.put("END_DATE", p.get("CREATE_END_TIME"));
			params.put("COMPANY_ID", p.get("COMPANY_ID"));
			params.put("SITE_RELA_ID", p.get("SITE_RELA_ID"));
			params.put("ACCOUNT_STATUS", 2);
			params.put("ACCOUNT_TYPE", COMPANY_TYPE);
			params.put("ACCOUNT_COMPANY_ID", p.get("ACCOUNT_COMPANY_ID"));
			this.saveOrderAccountService(params);
		}else{
			ACCOUNT_ID = (String)orderAccount.get(0).get("ID");
			params.clear();
			params.put("ID", ACCOUNT_ID);
			params.put("ACCOUNT_STATUS", 2);
			this.updateOrderAccountService(params);
		}
		
		p.put("ACCOUNT_STATUS", 1);
		p.put("ACCOUNT_DETAIL_NO_NULL", "YES");
		int cnt = 0;
		List<Map<String, Object>> managerCalc = this.managerCalcService(p);
		for (Map<String, Object> d : managerCalc) {
			cnt++;
			this.saveOrderAccountDetailService(d, ACCOUNT_ID, ACCOUNT_DETAIL_NO, "0", p, cnt);
			
		}
		
		List<Map<String, Object>> supplyCalc = this.supplyCalcService(p);
		for (Map<String, Object> d : supplyCalc) {
			cnt++;
			this.saveOrderAccountDetailService(d, ACCOUNT_ID, ACCOUNT_DETAIL_NO, "1", p, cnt);
		}
		
		List<Map<String, Object>> saleCalc = this.saleCalcService(p);
		for (Map<String, Object> d : saleCalc) {
			cnt++;
			this.saveOrderAccountDetailService(d, ACCOUNT_ID, ACCOUNT_DETAIL_NO, "2", p, cnt);
		}
		p.remove("ACCOUNT_STATUS");
		
		params.clear();
		params.put("ID", ACCOUNT_DETAIL_NO_ID);
		params.put("NO", ACCOUNT_DETAIL_NO);
		params.put("ACCOUNT_ID", ACCOUNT_ID);
		params.put("ACCOUNT_USER", p.get("CREATE_USER"));
		params.put("ACCOUNT_USER_ID", p.get("CREATE_USER_ID"));
		this.financeDao.saveOrderAccountDetailNODao(params);
		
		params.clear();
		String IDS = p.get("IDS").toString();
		params.put("IDS", IDS);
		params.put("ACCOUNT_ID", ACCOUNT_ID);
		params.put("ACCOUNT_DETAIL_NO", ACCOUNT_DETAIL_NO);
		this.financeDao.updateOrderAccountInfoDao(params);
		return result;
	}
	
	public Map<String, Object> dateInterval(Map<String, Object> params, String beginMonth, String beginType){
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
	
	public List<Map<String, Object>> listOrderAccountService(Map<String, Object> params){
		return this.financeDao.listOrderAccountDao(params);
	}
	
	public int countOrderAccountSerivce(Map<String, Object> params){
		return this.financeDao.countOrderAccountDao(params);
	}
	
	public List<Map<String, Object>> listOrderAccountSynService(Map<String, Object> params){
		return this.financeDao.listOrderAccountSynDao(params);
	}
	public void saveOrderAccountSynService(Map<String, Object> params) throws Exception{
		this.financeDao.saveOrderAccountSynDao(params);
	}
	public void delOrderAccountSynByUserIdService(Map<String, Object> params) throws Exception{
		this.financeDao.delOrderAccountSynByUserIdDao(params);
	}
	
	public List<Map<String, Object>> listOrderAccountDetailService(Map<String, Object> params){
		return this.financeDao.listOrderAccountDetailDao(params);
	}
	
	public void updateAccountDetailPayStatusService(Map<String, Object> params) throws Exception{
		this.financeDao.updateAccountDetailPayStatusDao(params);
	}
	
	public void updateAccountStatusService(Map<String, Object> params) throws Exception{
		List<Map<String, Object>> data = this.financeDao.listOrderAccountPayFinishDao(params);
		if(CommonUtils.checkList(data)){
			params.put("ACCOUNT_STATUS", "1");
			this.financeDao.updateOrderAccountByIDSDao(params);
		}
	}
	
	public void updateOrderBaseAccountStatusService(Map<String, Object> params) throws Exception{
		
		StringBuffer NOS = new StringBuffer();
		
		List<Map<String, Object>> data = this.financeDao.listOrderAccountDetailPayFinishDao(params);
		if(CommonUtils.checkList(data)){
			params.put("ACCOUNT_STATUS", "2");
			for (Map<String, Object> d : data) {
				NOS.append("'"+d.get("NO")+"',");
			}
			params.put("ACCOUNT_ID", params.get("ACCOUNT_ID"));
			params.put("ACCOUNT_DETAIL_NO", NOS.substring(0, NOS.length()-1));
			this.financeDao.updateOrderBaseAccountStatusByAccountIdAndNoOao(params);
		}
	}
	
	public void updateOrderAccountDetailAttrService(Map<String, Object> params) throws Exception{
		this.financeDao.updateOrderAccountDetailAttrDao(params);
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
		
		List<Map<String, Object>> data = this.financeDao.listOrderAccountDao(params);
		if(CommonUtils.checkList(data)){
			params.put("ACCOUNT_ID", data.get(0).get("ID"));
			this.updateAccountStatusService(params);
		}
	}
	
	public List<Map<String, Object>> listOrderAccountHistoryService(Map<String, Object> params){
		return this.financeDao.listOrderAccountHistoryDao(params);
	}
	
	public List<Map<String, Object>> listAccountInfoService(Map<String, Object> params){
		return this.financeDao.listAccountInfoDao(params);
	}
	
	public List<Map<String, Object>> listSaleOrderAccountHistoryService(Map<String, Object> params){
		return this.financeDao.listSaleOrderAccountHistoryDao(params);
	}
	
	public List<Map<String, Object>> listAloneOrderService(Map<String, Object> params){
		return this.financeDao.listAloneOrderDao(params);
	}
	
	public List<Map<String, Object>> listRechargeService(Map<String, Object> params){
		return this.financeDao.listRechargeDao(params);
	}
	
	public void saveCompanyPayService(Map<String, Object> params){
		
		params.put("COMPANY_PAY_STATUS", "0");
		
		/**
		 * 需要修改的订单
		 */
		List<Map<String, Object>> order_data = this.financeDao.listAloneOrderDao(params);
		StringBuffer ORDER_IDS = new StringBuffer();
		for (Map<String, Object> d : order_data) {
			ORDER_IDS.append("'"+d.get("ID")+"',");
		}
		
		/**
		 * 需要修改的充值记录
		 */
		List<Map<String, Object>> recharge_data = this.financeDao.listRechargeDao(params);
		StringBuffer RECHARGE_IDS = new StringBuffer();
		for (Map<String, Object> d : recharge_data) {
			RECHARGE_IDS.append("'"+d.get("ID")+"',");
		}
		
		
		if(CommonUtils.checkString(ORDER_IDS.toString()) || CommonUtils.checkString(RECHARGE_IDS.toString())){
			String ID = CommonUtils.uuid();
			params.put("ID", ID);
			
			this.financeDao.saveCompanyPayDao(params);
			
			params.put("COMPANY_PAY_ID", ID);
			
			if(CommonUtils.checkString(ORDER_IDS.toString())){
				params.put("IDS", ORDER_IDS.toString().substring(0, ORDER_IDS.length()-1));
				this.financeDao.updateOrderCompanyPayStatusDao(params);
			}
			
			if(CommonUtils.checkString(RECHARGE_IDS.toString())){
				params.put("IDS", RECHARGE_IDS.toString().substring(0, RECHARGE_IDS.length()-1));
				this.financeDao.updateRechargeCompanyPayStatusDao(params);
			}
			
		}
		
	}
	
	public List<Map<String, Object>> listParentUnaccountCompanyService(Map<String, Object> params){
		return this.financeDao.listParentUnaccountCompanyDao(params);
	}
	public int countParentUnaccountCompanyService(Map<String, Object> params){
		return this.financeDao.countParentUnaccountCompanyDao(params);
	}
	
	public List<Map<String, Object>> listUnaccountCompanyService(Map<String, Object> params){
		return this.financeDao.listUnaccountCompanyDao(params);
	}
	
	public int countUnaccountCompanyService(Map<String, Object> params){
		return this.financeDao.countUnaccountCompanyDao(params);
	}
}
