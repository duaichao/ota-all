package cn.sd.dao.route;

import java.util.List;
import java.util.Map;

import cn.sd.core.dao.IBaseDao;
import cn.sd.entity.RouteEntity;
import cn.sd.entity.produce.RouteDayDetail;

public interface IRouteDao extends IBaseDao{

	
	public List<Map<String, Object>> searchRecommendCity(Map<String, Object> params);
	
	public RouteEntity routeDetailEntity(Map<String, Object> params);
	
	public List<Map<String, Object>> searchWebRecommend(Map<String, Object> params);
	
	public int countRoute(Map<String, Object> params);
	public List<Map<String, Object>> searchRoute(Map<String, Object> params);
	public List<Map<String, Object>> searchRouteGroupByEndCity(Map<String, Object> params);
	public List<Map<String, Object>> searchRouteLabelAndCity(Map<String, Object> params);
	public List<Map<String, Object>> searchRouteOther(Map<String, Object> params);
	
	public List<Map<String, Object>> searchRouteCity(Map<String, Object> params);
	public List<Map<String, Object>> searchRouteDay(Map<String, Object> params);
	public List<RouteDayDetail> searchRouteDayDetail(Map<String, Object> params);
	public List<Map<String, Object>> searchRouteScenic(Map<String, Object> params);
	public List<Map<String, Object>> saerchRouteAlbum(Map<String, Object> params);
	public List<Map<String, Object>> searchCalendar(Map<String, Object> params);
	
	public List<Map<String, Object>> searchRouteTraffic(Map<String, Object> params);
	public List<Map<String, Object>> listRouteCalendarDao(Map<String, Object> params);
	
	public List<Map<String, Object>> sumPricesDao(Map<String, Object> params);
	
	public List<Map<String, Object>> searchWapPrice(Map<String, Object> params);
	public List<Map<String, Object>> searchRouteDetail(Map<String, Object> params);
	
	public List<Map<String, Object>> searchRouteTrafficDetail(Map<String, Object> params);
	
}
