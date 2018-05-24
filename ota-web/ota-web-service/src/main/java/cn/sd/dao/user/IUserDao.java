package cn.sd.dao.user;

import java.util.List;
import java.util.Map;

import cn.sd.core.dao.IBaseDao;
import cn.sd.entity.produce.CompanyDetail;

public interface IUserDao extends IBaseDao{
	
	public List<Map<String, Object>> searchWapSetting(Map<String, Object> params);
	
	public void updateUserPwdByID(Map<String, Object> params);
	
	public List<Map<String, Object>> searchWebCategoryByCompanyId(Map<String, Object> params);
	public List<CompanyDetail> searchCompanyBuyDomain(Map<String, Object> params);
	public List<Map<String, Object>> searchCompanyCounselor(Map<String, Object> params);
	
	public List<Map<String, Object>> searchWebUser(Map<String, Object> params);
	public void saveWebUser(Map<String, Object> params);
	
	public List<Map<String,Object>> searchUser(Map<String, Object> params);
	public List<Map<String,Object>> searchCompanyBuyID(Map<String, Object> params);
	
	public void updateWebUserPwd(Map<String, Object> params);
	public void updateWebUserInfo(Map<String, Object> params);
	public void updateWebUserMobile(Map<String, Object> params);
	public void updateWebUserFace(Map<String, Object> params);
	public void updateUserPwdByMobile(Map<String, Object> params);
	
	
}
