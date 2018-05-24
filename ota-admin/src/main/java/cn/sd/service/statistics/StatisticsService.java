package cn.sd.service.statistics;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("StatisticsService")
public class StatisticsService extends BaseDaoImpl implements IStatisticsService{
	
	@Resource
	private ILatestStatisticsService latestStatisticsService;
	
	public List<Map<String, Object>> totalOrderRetailService(Map<String, Object> params){
		return this.latestStatisticsService.totalOrderRetailService(params);
	}
	public List<Map<String, Object>> totalCompanyOfOrderService(Map<String, Object> params){
		return this.latestStatisticsService.totalCompanyOfOrderService(params);
	}
	public List<Map<String, Object>> totalCityOfOrderService(Map<String, Object> params){
		return this.latestStatisticsService.totalCityOfOrderService(params);
	}
	
	public List<Map<String, Object>> companyProduceService(Map<String, Object> params){
		return this.latestStatisticsService.companyProduceService(params);
	}
	
	public List<Map<String, Object>> cntCompanyProduceService(Map<String, Object> params){
		return this.latestStatisticsService.cntCompanyProduceService(params);
	}
	
	public List<Map<String, Object>> totalCompanyRouteCityService(Map<String, Object> params){
		return this.latestStatisticsService.totalCompanyRouteCityService(params);
	}
	
	public List<Map<String, Object>> companyOfCityService(Map<String, Object> params){
		return this.latestStatisticsService.companyOfCityService(params);
	}
	
	public List<Map<String, Object>> totalCompanyOfCityService(Map<String, Object> params){
		return this.latestStatisticsService.totalCompanyOfCityService(params);
	}
	
	public List<Map<String, Object>> totalInfoService(Map<String, Object> params){
		return this.latestStatisticsService.totalInfoService(params);
	}
	public List<Map<String, Object>> todayTotalInfoService(Map<String, Object> params){
		return this.latestStatisticsService.todayTotalInfoService(params);
	}
	public List<Map<String, Object>> todaySupplyService(Map<String, Object> params){
		return this.latestStatisticsService.todaySupplyService(params);
	}
	public List<Map<String, Object>> companyService(Map<String, Object> params){
		return this.latestStatisticsService.companyService(params);
	}
	public List<Map<String, Object>> parentCompanyService(Map<String, Object> params){
		return this.latestStatisticsService.parentCompanyService(params);
	}
	public List<Map<String, Object>> supplyCompanyService(Map<String, Object> params){
		return this.latestStatisticsService.supplyCompanyService(params);
	}
	public List<Map<String, Object>> siteCompanyService(Map<String, Object> params){
		return this.latestStatisticsService.siteCompanyService(params);
	}
	public List<Map<String, Object>> startService(Map<String, Object> params){
		return this.latestStatisticsService.startService(params);
	}
	public List<Map<String, Object>> listOrderService(Map<String, Object> params){
		return this.latestStatisticsService.listOrderService(params);
	}
	public int countOrderService(Map<String, Object> params){
		return this.latestStatisticsService.countOrderService(params);
	}
	public List<Map<String, Object>> siteCompanyGroupByWeiDuService(Map<String, Object> params){
		return this.latestStatisticsService.siteCompanyGroupByWeiDuService(params);
	}
	public List<Map<String, Object>> parentCompanyGroupByWeiDuService(Map<String, Object> params){
		return this.latestStatisticsService.parentCompanyGroupByWeiDuService(params);
	}
	public List<Map<String, Object>> supplyCompanyGroupByWeiDuService(Map<String, Object> params){
		return this.latestStatisticsService.supplyCompanyGroupByWeiDuService(params);
	}
	
	public List<Map<String, Object>> listOrderGroupByCompanyService(Map<String, Object> params){
		return this.latestStatisticsService.listOrderGroupByCompanyService(params);
	}
	public List<Map<String, Object>> listOrderGroupByProduceService(Map<String, Object> params){
		return this.latestStatisticsService.listOrderGroupByProduceService(params);
	}
	public List<Map<String, Object>> listRouteGroupByEndCityService(Map<String, Object> params){
		return this.latestStatisticsService.listRouteGroupByEndCityService(params);
	}
}
