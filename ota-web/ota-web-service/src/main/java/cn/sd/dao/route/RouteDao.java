package cn.sd.dao.route;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.core.util.CommonUtils;
import cn.sd.entity.RouteEntity;
import cn.sd.entity.produce.RouteDayDetail;

@Repository
@Service("routeDao")
public class RouteDao extends BaseDaoImpl implements IRouteDao {

	public List<Map<String, Object>> searchRecommendCity(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchRecommendCity", params);
	}
	
	public RouteEntity routeDetailEntity(Map<String, Object> params){
		List<RouteEntity> routeEntities = this.getSqlMapClientTemplate().queryForList("route.routeDetailEntity", params);
		if(CommonUtils.checkList(routeEntities) && routeEntities.size() == 1){
			return routeEntities.get(0);
		}
		return null;
	}
	
	@Override
	public List<Map<String, Object>> searchWebRecommend(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchWebRecommend", params);
	}
	
	@Override
	public int countRoute(Map<String, Object> params) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("route.countRoute", params);
	}

	@Override
	public List<Map<String, Object>> searchRoute(Map<String, Object> params) {
		return this.getSqlMapClientTemplate().queryForList("route.searchRoute", params);
	}
	
	public List<Map<String, Object>> searchRouteGroupByEndCity(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchRouteGroupByEndCity", params);
	}

	public List<Map<String, Object>> searchRouteLabelAndCity(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchRouteLabelAndCity", params);
	}
	public List<Map<String, Object>> searchRouteOther(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchRouteOther", params);
	}
	public List<Map<String, Object>> searchRouteCity(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchRouteCity", params);
	}
	public List<Map<String, Object>> searchRouteDay(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchRouteDay", params);
	}
	public List<RouteDayDetail> searchRouteDayDetail(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchRouteDayDetail", params);
	}
	public List<Map<String, Object>> searchRouteScenic(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchRouteScenic", params);
	}
	public List<Map<String, Object>> saerchRouteAlbum(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.saerchRouteAlbum", params);
	}
	public List<Map<String, Object>> searchCalendar(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchCalendar", params);
	}
	public List<Map<String, Object>> searchRouteTraffic(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchRouteTraffic", params);
	}
	public List<Map<String, Object>> listRouteCalendarDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listRouteCalendarDao", params);
	}
	public List<Map<String, Object>> sumPricesDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.sumPricesDao", params);
	}
	public List<Map<String, Object>> searchWapPrice(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchWapPrice", params);
	}
	
	public List<Map<String, Object>> searchRouteDetail(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchRouteDetail", params);
	}
	
	public List<Map<String, Object>> searchRouteTrafficDetail(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.searchRouteTrafficDetail", params);
	}
}
