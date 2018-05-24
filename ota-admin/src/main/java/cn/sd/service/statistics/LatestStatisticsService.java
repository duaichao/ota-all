package cn.sd.service.statistics;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.dao.statistics.IUtilStatisticsDao;

@Repository
@Service("latestStatisticsService")
public class LatestStatisticsService extends BaseDaoImpl implements ILatestStatisticsService{
	
	@Resource
	private IUtilStatisticsDao utilStatisticsDao;
	
	public List<Map<String, Object>> totalOrderRetailService(Map<String, Object> params){
		return this.utilStatisticsDao.totalOrderRetailDao(params);
	}
	
	public List<Map<String, Object>> totalCompanyOfOrderService(Map<String, Object> params){
		return this.utilStatisticsDao.totalCompanyOfOrderDao(params);
	}
	
	public List<Map<String, Object>> totalCityOfOrderService(Map<String, Object> params){
		return this.utilStatisticsDao.totalCityOfOrderDao(params);
	}
	public List<Map<String, Object>> companyProduceService(Map<String, Object> params){
		return this.utilStatisticsDao.companyProduceDao(params);
	}
	
	public List<Map<String, Object>> cntCompanyProduceService(Map<String, Object> params){
		return this.utilStatisticsDao.cntCompanyProduceDao(params);
	}
	
	public List<Map<String, Object>> totalCompanyRouteCityService(Map<String, Object> params){
		return this.utilStatisticsDao.totalCompanyRouteCityDao(params);
	}
	
	public List<Map<String, Object>> companyOfCityService(Map<String, Object> params){
		return this.utilStatisticsDao.companyOfCityDao(params);
	}
	
	public List<Map<String, Object>> totalCompanyOfCityService(Map<String, Object> params){
		return this.utilStatisticsDao.totalCompanyOfCityDao(params);
	}
	
	public List<Map<String, Object>> totalInfoService(Map<String, Object> params){
		return this.utilStatisticsDao.totalInfoDao(params);
	}
	public List<Map<String, Object>> todayTotalInfoService(Map<String, Object> params){
		return this.utilStatisticsDao.todayTotalInfoDao(params);
	}
	public List<Map<String, Object>> todaySupplyService(Map<String, Object> params){
		return this.utilStatisticsDao.todaySupplyDao(params);
	}
	public List<Map<String, Object>> companyService(Map<String, Object> params){
		return this.utilStatisticsDao.companyDao(params);
	}
	public List<Map<String, Object>> parentCompanyService(Map<String, Object> params){
		return this.utilStatisticsDao.parentCompanyDao(params);
	}
	public List<Map<String, Object>> supplyCompanyService(Map<String, Object> params){
		return this.utilStatisticsDao.supplyCompanyDao(params);
	}
	public List<Map<String, Object>> siteCompanyService(Map<String, Object> params){
		return this.utilStatisticsDao.siteCompanyDao(params);
	}
	public List<Map<String, Object>> startService(Map<String, Object> params){
		return this.utilStatisticsDao.startDao(params);
	}
	public List<Map<String, Object>> listOrderService(Map<String, Object> params){
		return this.utilStatisticsDao.listOrderDao(params);
	}
	public int countOrderService(Map<String, Object> params){
		return this.utilStatisticsDao.countOrderDao(params);
	}
	public List<Map<String, Object>> siteCompanyGroupByWeiDuService(Map<String, Object> params){
		return this.utilStatisticsDao.siteCompanyGroupByWeiDuDao(params);
	}
	public List<Map<String, Object>> parentCompanyGroupByWeiDuService(Map<String, Object> params){
		return this.utilStatisticsDao.parentCompanyGroupByWeiDuDao(params);
	}
	public List<Map<String, Object>> supplyCompanyGroupByWeiDuService(Map<String, Object> params){
		return this.utilStatisticsDao.supplyCompanyGroupByWeiDuDao(params);
	}
	
	
	public List<Map<String, Object>> listOrderGroupByCompanyService(Map<String, Object> params){
		return this.utilStatisticsDao.listOrderGroupByCompanyDao(params);
	}
	public List<Map<String, Object>> listOrderGroupByProduceService(Map<String, Object> params){
		return this.utilStatisticsDao.listOrderGroupByProduceDao(params);
	}
	public List<Map<String, Object>> listRouteGroupByEndCityService(Map<String, Object> params){
		return this.utilStatisticsDao.listRouteGroupByEndCityDao(params);
	}
}
