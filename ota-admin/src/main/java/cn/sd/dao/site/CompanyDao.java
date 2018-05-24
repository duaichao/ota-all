package cn.sd.dao.site;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.core.util.CommonUtils;
import cn.sd.entity.site.WapSettingEntity;

@Repository
@Service("companyDao")
@SuppressWarnings("all")
public class CompanyDao extends BaseDaoImpl implements ICompanyDao{
	
	public void setAccountUserDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("company.setAccountUserDao", params);
	}
	
	public void delWebRecommendByWapIdDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().delete("company.delWebRecommendByWapIdDao", params);
	}
	
	public void synCompanyWebDataDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("company.synCompanyWebDataDao", params);
	}
	
	public void delManagerDepartDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.delManagerDepartDao", params);
	}
	public void saveManagerDepartDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.saveManagerDepartDao", params);
	}
	
	public List<Map<String, Object>> searchManagerDepartDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.searchManagerDepartDao", params);
	}
	public void saveCompanyStartCityDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.saveCompanyStartCityDao", params);
	}
	public void delCompanyStartCityDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.delCompanyStartCityDao", params);
	}
	public List<Map<String, Object>> searchCompanyStartCityDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.searchCompanyStartCityDao", params);
	}
	
	public void resetPayErrorCntDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.resetPayErrorCntDao", params);
	}
	
	public void updatePayErrorCntDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updatePayErrorCntDao", params);
	}
	
	public void initPayPwdDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.initPayPwdDao", params);
	}
	public void updatePayPwdDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updatePayPwdDao", params);
	}
	
	public List<Map<String, Object>> searchWebRecommendSupplyDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.searchWebRecommendSupplyDao", params);
	}
	
	public List<Map<String, Object>> searchWebRouteSupplyDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.searchWebRouteSupplyDao", params);
	}
	
	public void saveCompanyGrantDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.saveCompanyGrantDao", params);
	}
	
	public List<Map<String, Object>> searchCompanyBankDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.searchCompanyBankDao", params);
	}
	public List<Map<String, Object>> searchAuditRouteDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.searchAuditRouteDao", params);
	}
	public int countAuditRouteDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("company.countAuditRouteDao", params);
	}
	
	public List<Map<String, Object>> listSaleRouteByCompanyIdDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listSaleRouteByCompanyIdDao", params);
	}
	
	public void saveSaleRouteDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.saveSaleRouteDao", params);
	}
	public void delSaleRouteDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("company.delSaleRouteDao", params);
	}
	
	public void updateWebRecommendTopOrderByDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateWebRecommendTopOrderByDao", params);
	}
	
	public void updateWebRecommendIsTopDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateWebRecommendIsTopDao", params);
	}
	
	public void batchtSaveWebCategoryDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.batchtSaveWebCategoryDao", params);
	}
	
	public void updateWebCategoryByIdDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateWebCategoryByIdDao", params);
	}
	
	public List<Map<String, Object>> listWebCategoryByCompanyIdDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listWebCategoryByCompanyIdDao", params);
	}
	
	public void updateCompanyIsShowByIdsDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateCompanyIsShowByIdsDao", params);
	}
	public void updateCompanyOrderByIdDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateCompanyOrderByIdDao", params);
	}
	
	public Map<String, Object> getCompanyMoneyByDepartIdDao(Map<String,Object> params){
		List<Map<String, Object>> data = this.getSqlMapClientTemplate().queryForList("company.getCompanyMoneyByDepartIdDao", params);
		if(CommonUtils.checkString(data) && data.size() == 1){
			return data.get(0);
		}
		return null;
	}
	
	public void updateUserCompanyMoneyDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateUserCompanyMoneyDao", params);
	}
	public void saveUserCompanyFundsLogDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.saveUserCompanyFundsLogDao", params);
	}
	public List<Map<String, Object>> listUserCompanyFundsLogDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listUserCompanyFundsLogDao", params);
	}
	
	public void deleteCompanyUserRoleDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("company.deleteCompanyUserRoleDao", params);
	}
	public void saveCompanyUserRoleDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.saveCompanyUserRoleDao", params);
	}
	
	public void saveCompanyRoleDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.saveCompanyRoleDao", params);
	}
	
	public void updateCompanyRoleDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateCompanyRoleDao", params);
	}
	
	public List<Map<String, Object>> listCompanyRoleDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listCompanyRoleDao", params);
	}
	
	public void updateCompanyRouteTypeDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateCompanyRouteTypeDao", params);
	}
	
	public Map<String, Object> companyDetailDao(Map<String,Object> params){
		List<Map<String, Object>> data = this.getSqlMapClientTemplate().queryForList("company.companyDetailDao", params);
		if(CommonUtils.checkList(data) && data.size() == 1){
			return data.get(0);
		}
		return null;
	}
	
	public void updateFilialeDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateFilialeDao", params);
	}
	
	public void saveWebRecommendDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.saveWebRecommendDao", params);
	}
	
	public void delWebRecommendDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.delWebRecommendDao", params);
	}
	
	public void updateWebRecommendOrderDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateWebRecommendOrderDao", params);
	}
	
	public int countWebRecommendDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("company.countWebRecommendDao", params);
	}
	
	public List<Map<String, Object>> searchWebRecommendDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.searchWebRecommendDao", params);
	}
	
	public void balanceContractAgencyDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.balanceContractAgencyDao", params);
	}
	
	public void delContractAgencyDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("company.delContractAgencyDao", params);
	}
	
	public void resetContractAgencyDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.resetContractAgencyDao", params);
	}
	
	public void cancelContractAgencyDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.cancelContractAgencyDao", params);
	}
	
	public void saveOrderContractLogDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.saveOrderContractLogDao", params);
	}
	
	public void useContractAgencyDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.useContractAgencyDao", params);
	}
	
	public int countOrderContractAgencyDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("company.countOrderContractAgencyDao", params);
	}
	
	public List<Map<String, Object>> searchOrderContractAgencyDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.searchOrderContractAgencyDao", params);
	}
	
	public void betchSaveOrderContractAgencyDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.betchSaveOrderContractAgencyDao", params);
	}

	public void betchSaveOrderContractLogDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.betchSaveOrderContractLogDao", params);
	}
	
	
	public List<Map<String, Object>> listDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listDao", params);
	}
	
	public int countDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("company.countDao", params);
	}
	
	public void saveSiteManagerDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("company.saveSiteManagerDao", params);
	}
	
	public void saveUserCompanyDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("company.saveUserCompanyDao", params);
	}
	
	public void editUserCompanyDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("company.editUserCompanyDao", params);
	}
	
	public void editSiteManagerDelStatusDao(Map<String,Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("company.editSiteManagerDelStatusDao", params);
	}
	
	public List<Map<String, Object>> listSiteManagerDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listSiteManagerDao", params);
	}
	
	public List<Map<String, Object>> userOfCompanyDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.userOfCompanyDao", params);
	}
	
	public List<Map<String, Object>> listCompanyDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listCompanyDao", params);
	}

	public int countCompanyDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("company.countCompanyDao", params);
	}
	
	public List<Map<String, Object>> listOrderCompanyDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listOrderCompanyDao", params);
	}
	
	public List<Map<String, Object>> listAccountCompanyDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listAccountCompanyDao", params);
	}
	
	public List<Map<String, Object>> listAloneCompanyDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listAloneCompanyDao", params);
	}
	
	public List<Map<String, Object>> listSiteInfoDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listSiteInfoDao", params);
	}
	
	public void saveWapSettingDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.saveWapSettingDao", params);
	}
	public void updateWapSettingDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateWapSettingDao", params);
	}
	public List<WapSettingEntity> listWapSettingDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listWapSettingDao", params);
	}
	
	public int countWapSettingDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("company.countWapSettingDao", params);
	}
	
	public void saveMiniShopDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.saveMiniShopDao", params);
	}
	
	public void updateMiniShopUseStateDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateMiniShopUseStateDao", params);
	}
	
	public void updateMiniShopDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.updateMiniShopDao", params);
	}

	public void saveWapAdAttrDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.saveWapAdAttrDao", params);
	}
	public void delWapAdAttrDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("company.delWapAdAttrDao", params);
	}
	public List<Map<String, Object>> listWapAdAttrDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listWapAdAttrDao", params);
	}
	
	public void setPointDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("company.setPointDao", params);
	}
	
	public void saveBusinessSellerDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.saveBusinessSellerDao", params);
	}
	public void delBusinessSellerDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("company.delBusinessSellerDao", params);
	}
	public List<Map<String, Object>> listBusinessSellerDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listBusinessSellerDao", params);
	}
	
	public List<Map<String, Object>> listCompanySiteDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("company.listCompanySiteDao", params);
	}
	
	public void updateChildCompanyAloneDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("company.updateChildCompanyAloneDao", params);
	}
}
