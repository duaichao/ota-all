package cn.sd.service.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.lock.LockContainer;
import cn.sd.core.util.sms.SMSSender;
import cn.sd.dao.account.IAccountDao;
import cn.sd.dao.b2b.IUserDao;
import cn.sd.dao.site.ICompanyDao;
import cn.sd.dao.site.IDepartDao;
import cn.sd.service.site.ICompanyService;

@Service("accountService")
public class AccountService implements IAccountService{
	
	@Resource
	private IAccountDao accountDao;
	@Resource
	private IUserDao userDao;
	@Resource
	private IDepartDao departDao;
	@Resource
	private ICompanyService companyService;
	
	public List<Map<String, Object>> listService(Map<String,Object> params){
		return this.accountDao.listDao(params);
	}
	
	public int countService(Map<String,Object> params){
		return this.accountDao.countDao(params);
	}
	
	public Map<String,String> statService(Map<String,Object> params){
		return this.accountDao.statDao(params);
	}

	@Override
	public String yeStatService(String DEPART_ID) {
		// TODO Auto-generated method stub
		return this.accountDao.yeStatDao(DEPART_ID);
	}

	@Override
	public String accountStateService(String DEPART_ID) {
		// TODO Auto-generated method stub
		return this.accountDao.accountStateDao(DEPART_ID);
	}

	@Override
	/**
	 * DEPART_ID
	 * USER_ID
	 * NO
	 * THIRD_NO
	 * THIRD_TYPE 1支付宝2财付通3网银
	 * NOTE:如：招商银行(账号)-开户名，充值 300元
	 * */
	public void addOnLineRechargeService(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> accountParams = new HashMap<String,Object>();
		accountParams.put("DEPART_ID", params.get("DEPART_ID").toString());
		accountParams.put("IS_USE", '0');
		
		String companyId = this.getCompanyIdService(accountParams.get("DEPART_ID").toString());
		accountParams.put("COMPANY_ID", companyId==null?"":companyId);
		
		accountParams.put("USER_ID",params.get("USER_ID").toString());
		String accountId="";
		accountId = this.createAccountService(accountParams);
		
		params.put("ACCOUNT_ID", accountId);
		this.accountDao.addOnLineRechargeDao(params);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void addLineRechargeService(Map<String, Object> params) throws Exception{
		// TODO Auto-generated method stub
		String id = CommonUtils.uuid();
		params.put("ID", id);
		this.accountDao.addLineRechargeDao(params);

		params.put("ACCOUNT_DETAIL_ID", params.get("ID"));
		params.put("ID", CommonUtils.uuid());
		params.put("TYPE", "1");
		params.put("MONEY", 0 - Double.parseDouble(String.valueOf(params.get("MONEY"))));
		this.companyService.updateUserCompanyMoneyService(params);
	}

	@Override
	public String refundService(Map<String, Object> params) {
		// TODO Auto-generated method stub
		Object obj = LockContainer.getLock(params.get("DEPART_ID").toString());
		if(obj==null){
			return "-3";
		}
		synchronized(obj){
			Map<String,Object> map = this.accountDao.isRefundDao(params);
			String YE = map.get("YE").toString();
			//超过余额总额
			if(YE.indexOf("-")!=-1){
				return "-1";
			}
			//超过充值总额
			String YECZ = map.get("YECZ").toString();
			if(YECZ.indexOf("-")!=-1){
				return "-2";
			}
			this.accountDao.addRefundDao(params); 
			return "1";
		}
	}

	@Override
	public void addOverdraftService(Map<String, Object> params) {
		// TODO Auto-generated method stub
		this.accountDao.addOverdraftDao(params); 
	}

	@Override
	public String overdraftRefundService(Map<String, Object> params) {
		// TODO Auto-generated method stub
		Object obj = LockContainer.getLock(params.get("DEPART_ID").toString());
		if(obj==null){
			return "-3";
		}
		synchronized(obj){
			String YE = this.accountDao.isOverdraftRefundDao(params);

			//超过透支总额
			if(YE.indexOf("-")!=-1){
				return "-1";
			}
			
			this.accountDao.addOverdraftRefundDao(params);
			return "1";
		}
		
	}

	@Override
	public String createAccountService(Map<String, Object> params)  throws Exception {
		// TODO Auto-generated method stub
		String id = this.accountDao.getAccountIdDao(params.get("DEPART_ID").toString());
		if(id!=null){
			return id;
		}
		
		id = CommonUtils.uuid();
		params.put("ID", id);
		this.accountDao.createAccountDao(params);
		return id;
	}

	@Override
	public void updateStateService(Map<String, Object> params) {
		// TODO Auto-generated method stub
		this.accountDao.updateStateDao(params);
	}
	
	/**
	 * 余额付款 
	 * DEPART_ID
	   MONEY
	   USER_ID
	   NOTE
	   ORDER_NO
	   PRODUCE_TYPE 产品类型 1线路2交通3酒店4景点5保险
	 * */
	public String balancePayService(Map<String,Object> params){
		String accountId="";
		try {
			accountId = this.createAccountService(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "-1";
		}
		params.put("ACCOUNT_ID", accountId);
		
		Object obj = LockContainer.getLock(params.get("DEPART_ID").toString());
		if(obj==null){
			return "-3";
		}
		synchronized(obj){
			String YE = this.accountDao.isBalancePayDao(params);
				
			//余额不足
			if(YE.indexOf("-")!=-1){
				return "-1";
			}
			
			this.accountDao.addBalancePayDao(params);
			return "1";
		}
	}
	
	/**
	 * 订单退款 
	 * DEPART_ID
	   MONEY
	   USER_ID
	   NOTE 订单退款+订单编号
	   ORDER_NO
	   PRODUCE_TYPE 产品类型 1线路2交通3酒店4景点5保险
	 * */
	public String orderRefundService(Map<String,Object> params){
		String accountId="";
		try {
			accountId = this.createAccountService(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "-1";
		}
		params.put("ACCOUNT_ID", accountId);
		
		this.accountDao.orderRefundDao(params);
		return "1";
	}
	
	public String getCompanyIdService(String DEPART_ID){
		return this.accountDao.getCompanyIdDao(DEPART_ID);
	}
	
	public void saveAccountLogService(Map<String,Object> params)throws Exception{
		this.accountDao.saveAccountLogDao(params);
	}
	public List<Map<String, Object>> listAccountLogService(Map<String,Object> params){
		return this.accountDao.listAccountLogDao(params);
	}
	public void updateAccountLogService(Map<String,Object> params){
		this.accountDao.updateAccountLogDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public int onlineCZCallBackService(Map<String,Object> p, String order_no, String trade_no, int payWay, String total_fee) throws Exception {
		
		p.clear();
		p.put("NO", order_no);
		Map<String, Object> accountLog = this.accountDao.listAccountLogDao(p).get(0);
		String STATUS = String.valueOf(accountLog.get("STATUS"));
		if(STATUS.equals("1")){
			p.clear();
			p.put("ID", accountLog.get("CREATE_USER_ID"));
			Map<String, Object> user = this.userDao.detailUserDao(p);
			String NOTE = (String)user.get("COMPANY");
			if(CommonUtils.checkString(user.get("DEPART_NAME"))){
				NOTE += "-"+user.get("DEPART_NAME");
			}
			NOTE += "-"+user.get("USER_NAME")+",于"+DateUtil.getNowDateTimeString()+"充值 "+String.valueOf(accountLog.get("AMOUNT"))+"元";
			p.clear();
			p.put("NO", order_no);
			p.put("PAY_TYPE", payWay);
			p.put("THIRD_NO", trade_no);
			p.put("STATUS", 2);
			p.put("CONTENT", NOTE);
			
			this.accountDao.updateAccountLogDao(p);
			
			List<Map<String, Object>> logs = this.accountDao.listAccountLogDao(p);
			
			accountLog.put("ACCOUNT_WAY", logs.get(0).get("ACCOUNT_WAY"));
			accountLog.put("NOTE", NOTE);
			accountLog.put("USER_ID", accountLog.get("CREATE_USER_ID"));
			accountLog.put("THIRD_TYPE", payWay);
			accountLog.put("MONEY", accountLog.get("AMOUNT"));
			accountLog.put("THIRD_NO", trade_no);
			accountLog.put("NO", order_no);
			
			/**
			 * 站关系ID
			 */
			p.clear();
			p.put("ID", accountLog.get("CREATE_USER_ID"));
			Map<String, Object> _user = this.userDao.detailUserDao(p);
			accountLog.put("SITE_RELA_ID", _user.get("SITE_RELA_ID"));
			this.addOnLineRechargeService(accountLog);
			
			/**
			 * 给充值用户发送短信
			 */
			String MOBILE = (String)user.get("MOBILE");
			if(CommonUtils.checkString(MOBILE)){
				p.clear();
				p.put("ID", accountLog.get("DEPART_ID"));
				p.put("start", 1);
				p.put("end", 1);
				List<Map<String, Object>> departs = this.departDao.listDao(p);
				if(CommonUtils.checkList(departs)){
					Map<String, Object> depart = departs.get(0);
					NOTE = (String)user.get("USER_NAME")+",于"+DateUtil.getNowDateTimeString()+",给"+(String)depart.get("COMPANY")+" "+(String)depart.get("TEXT")+"充值"+String.valueOf(accountLog.get("AMOUNT"))+"元";
					double c = 50.00;
					double cnt = Math.ceil(Double.parseDouble(String.valueOf(NOTE.length()))/c);
					SMSSender.sendSMS(MOBILE, NOTE, (String)user.get("USER_NAME"), "", "5", (String)user.get("SITE_ID"), "2", "0", "", "", cnt);
				}
			}
		}
		
		return 0;
	}
	
	public void updateAccountLogStatusService(Map<String,Object> params)throws Exception{
		this.accountDao.updateAccountLogStatusDao(params);
	}
	
	public List<Map<String, Object>> listAccountDetailService(Map<String,Object> params){
		return this.accountDao.listAccountDetailDao(params);
	}
	
	public double sumAccountDetailMoneyService(Map<String,Object> params){
		return this.accountDao.sumAccountDetailMoneyDao(params);
	}
}
