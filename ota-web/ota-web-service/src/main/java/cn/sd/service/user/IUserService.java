package cn.sd.service.user;

import java.util.List;
import java.util.Map;

import cn.sd.entity.produce.CompanyDetail;

public interface IUserService {
	public List<Map<String, Object>> searchWapSetting(Map<String, Object> params);
	public void updateUserPwdByID(Map<String, Object> params)throws Exception;
	public List<Map<String, Object>> searchWebCategoryByCompanyId(Map<String, Object> params);
	public List<CompanyDetail> searchCompanyBuyDomain(Map<String, Object> params);
	public List<Map<String, Object>> searchCompanyCounselor(Map<String, Object> params);
	
	public List<Map<String, Object>> searchWebUser(Map<String, Object> params);
	public void saveWebUser(Map<String, Object> params)throws Exception;
	
	public List<Map<String,Object>> searchUser(Map<String, Object> params);
	
	public void updateWebUserPwd(Map<String, Object> params)throws Exception;
	public void updateWebUserInfo(Map<String, Object> params)throws Exception;
	public void updateWebUserMobile(Map<String, Object> params)throws Exception;
	public void updateWebUserFace(Map<String, Object> params)throws Exception;
	public void updateUserPwdByMobile(Map<String, Object> params);
}
