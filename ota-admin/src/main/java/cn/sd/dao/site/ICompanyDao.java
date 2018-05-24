package cn.sd.dao.site;

import java.util.List;
import java.util.Map;

import cn.sd.entity.site.WapSettingEntity;

public interface ICompanyDao {
	
	public void setAccountUserDao(Map<String,Object> params)throws Exception;
	
	public void delWebRecommendByWapIdDao(Map<String,Object> params)throws Exception;
	public void synCompanyWebDataDao(Map<String,Object> params)throws Exception;
	
	public void delManagerDepartDao(Map<String,Object> params);
	public void saveManagerDepartDao(Map<String,Object> params);
	
	public List<Map<String, Object>> searchManagerDepartDao(Map<String,Object> params);
	
	public void saveCompanyStartCityDao(Map<String,Object> params);
	public void delCompanyStartCityDao(Map<String,Object> params);
	public List<Map<String, Object>> searchCompanyStartCityDao(Map<String,Object> params);
	
	public void resetPayErrorCntDao(Map<String,Object> params);
	
	public void updatePayErrorCntDao(Map<String,Object> params);
	public void saveCompanyGrantDao(Map<String,Object> params);
	public void initPayPwdDao(Map<String,Object> params);
	public void updatePayPwdDao(Map<String,Object> params);
	
	public List<Map<String, Object>> searchWebRecommendSupplyDao(Map<String,Object> params);
	public List<Map<String, Object>> searchWebRouteSupplyDao(Map<String,Object> params);
	
	public List<Map<String, Object>> searchCompanyBankDao(Map<String,Object> params);
	public List<Map<String, Object>> searchAuditRouteDao(Map<String,Object> params);
	public int countAuditRouteDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listSaleRouteByCompanyIdDao(Map<String,Object> params);
	public void saveSaleRouteDao(Map<String,Object> params);
	public void delSaleRouteDao(Map<String,Object> params);
	
	public void updateWebRecommendTopOrderByDao(Map<String,Object> params);
	public void updateWebRecommendIsTopDao(Map<String,Object> params);
	
	public void batchtSaveWebCategoryDao(Map<String,Object> params);
	public void updateWebCategoryByIdDao(Map<String,Object> params);
	public List<Map<String, Object>> listWebCategoryByCompanyIdDao(Map<String,Object> params);
	
	public void updateCompanyIsShowByIdsDao(Map<String,Object> params);
	public void updateCompanyOrderByIdDao(Map<String,Object> params);
	
	public Map<String, Object> getCompanyMoneyByDepartIdDao(Map<String,Object> params);
	public void updateUserCompanyMoneyDao(Map<String,Object> params);
	public void saveUserCompanyFundsLogDao(Map<String,Object> params);
	public List<Map<String, Object>> listUserCompanyFundsLogDao(Map<String,Object> params);
	
	public void deleteCompanyUserRoleDao(Map<String,Object> params);
	public void saveCompanyUserRoleDao(Map<String,Object> params);
	
	public void saveCompanyRoleDao(Map<String,Object> params);
	public void updateCompanyRoleDao(Map<String,Object> params);
	public List<Map<String, Object>> listCompanyRoleDao(Map<String,Object> params);
	
	public void updateCompanyRouteTypeDao(Map<String,Object> params);
	public Map<String, Object> companyDetailDao(Map<String,Object> params);
	public void updateFilialeDao(Map<String,Object> params);
	public void saveWebRecommendDao(Map<String,Object> params);
	
	public void delWebRecommendDao(Map<String,Object> params);
	
	public void updateWebRecommendOrderDao(Map<String,Object> params);
	
	
	public int countWebRecommendDao(Map<String,Object> params);
	public List<Map<String, Object>> searchWebRecommendDao(Map<String,Object> params);
	
	public void balanceContractAgencyDao(Map<String,Object> params);
	
	public void delContractAgencyDao(Map<String,Object> params);
	
	public void resetContractAgencyDao(Map<String,Object> params);
	
	public void cancelContractAgencyDao(Map<String,Object> params);
	
	public void saveOrderContractLogDao(Map<String,Object> params);
	
	public void useContractAgencyDao(Map<String,Object> params);
	
	public int countOrderContractAgencyDao(Map<String,Object> params);
	
	public List<Map<String, Object>> searchOrderContractAgencyDao(Map<String,Object> params);
	
	public void betchSaveOrderContractAgencyDao(Map<String,Object> params);
	
	public void betchSaveOrderContractLogDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listDao(Map<String,Object> params);

	public int countDao(Map<String,Object> params);
	
	public void saveSiteManagerDao(Map<String,Object> params)throws Exception;
	
	public void saveUserCompanyDao(Map<String,Object> params)throws Exception;
	
	public void editUserCompanyDao(Map<String,Object> params)throws Exception;

	public void editSiteManagerDelStatusDao(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listSiteManagerDao(Map<String,Object> params);
	
	public List<Map<String, Object>> userOfCompanyDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listCompanyDao(Map<String,Object> params);

	public int countCompanyDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listOrderCompanyDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listAccountCompanyDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listAloneCompanyDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listSiteInfoDao(Map<String,Object> params);
	
	public void saveWapSettingDao(Map<String,Object> params);
	public void updateWapSettingDao(Map<String,Object> params);
	public List<WapSettingEntity> listWapSettingDao(Map<String,Object> params);
	public int countWapSettingDao(Map<String,Object> params);
	public void saveMiniShopDao(Map<String,Object> params);
	public void updateMiniShopUseStateDao(Map<String,Object> params);
	public void updateMiniShopDao(Map<String,Object> params);
	
	public void saveWapAdAttrDao(Map<String,Object> params);
	public void delWapAdAttrDao(Map<String,Object> params);
	public List<Map<String, Object>> listWapAdAttrDao(Map<String,Object> params);
	
	public void setPointDao(Map<String,Object> params);
	
	public void saveBusinessSellerDao(Map<String,Object> params);
	public void delBusinessSellerDao(Map<String,Object> params);
	public List<Map<String, Object>> listBusinessSellerDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listCompanySiteDao(Map<String,Object> params);
	
	public void updateChildCompanyAloneDao(Map<String,Object> params);
	  
}

