package cn.sd.dao.user;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.entity.produce.CompanyDetail;

@Repository
@Service("userDao")
public class UserDao extends BaseDaoImpl implements IUserDao {

	public List<Map<String, Object>> searchWapSetting(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("user.searchWapSetting", params);
	}
	
	public void updateUserPwdByID(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("user.updateUserPwdByID", params);
	}
	
	public List<Map<String, Object>> searchWebCategoryByCompanyId(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("user.searchWebCategoryByCompanyId", params);
	}
	
	public List<CompanyDetail> searchCompanyBuyDomain(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("user.searchCompanyBuyDomain", params);
	}
	
	public List<Map<String, Object>> searchCompanyCounselor(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("user.searchCompanyCounselor", params);
	}
	
	public List<Map<String, Object>> searchWebUser(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("user.searchWebUser", params);
	}
	public void saveWebUser(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("user.saveWebUser", params);
	}
	
	public List<Map<String,Object>> searchUser(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("user.searchUser", params);
	}
	public List<Map<String,Object>> searchCompanyBuyID(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("user.searchCompanyBuyID", params);
	}
	
	public void updateWebUserPwd(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("user.updateWebUserPwd", params);
	}
	
	public void updateWebUserInfo(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("user.updateWebUserInfo", params);
	}
	public void updateWebUserMobile(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("user.updateWebUserMobile", params);
	}
	public void updateWebUserFace(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("user.updateWebUserFace", params);
	}
	public void updateUserPwdByMobile(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("user.updateUserPwdByMobile", params);
	}
}
