package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("smsDao")
public class SMSDao extends BaseDaoImpl implements ISMSDao{
	
	public void saveUserSmsLogDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("sms.saveUserSmsLogDao", params);
	}
	
	public void saveUserSmsDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("sms.saveUserSmsDao", params);
	}
	
	public void updateUserSmsCountDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("sms.updateUserSmsCountDao", params);
	}

	public void updateUserSmsLogStatusDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("sms.updateUserSmsLogStatusDao", params);
	}
	
	public int countUserSmsLogDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("sms.countUserSmsLogDao", params);
	}
	
	public List<Map<String, Object>> listUserSmsDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("sms.listUserSmsDao", params);
	}
	
	public List<Map<String, Object>> listUserSmsLogDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("sms.listUserSmsLogDao", params);
	}
	
	public List<Map<String, Object>> listDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("sms.listDao", params);
	}
	
	public int countDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("sms.countDao", params);
	}
	
	public void eidtStatusDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("sms.eidtStatusDao", params);
	}
	
	public void saveDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("sms.saveDao", params);
	}
	
	public int smsSendStatusDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("sms.smsSendStatusDao", params);
	}
	
	public int smsSendStatusGroupDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("sms.smsSendStatusGroupDao", params);
	}
	
	public void usersmsGroupDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("sms.usersmsGroupDao", params);
	}
	public int searchhalfHourCntDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("sms.searchhalfHourCntDao", params);
	}
	public int searchHourCntDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("sms.searchHourCntDao", params);
	}
	public int searchhalfDayCntDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("sms.searchhalfDayCntDao", params);
	}
	public int searchDayCntDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("sms.searchDayCntDao", params);
	}
}
