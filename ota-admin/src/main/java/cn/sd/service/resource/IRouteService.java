package cn.sd.service.resource;

import java.util.List;
import java.util.Map;

import cn.sd.entity.RouteEntity;
import cn.sd.entity.produce.RouteDayDetail;

public interface IRouteService {
	
	public void updateRouteEarnestService(Map<String,Object> params)throws Exception;
	
	public void saveProContactService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listProContactService(Map<String,Object> params);
	
	public void copyDayService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listEndCityOfRouteByCompanyIdAndCityNameService(Map<String,Object> params);
	
	public int validation(Map<String,Object> params);
	
	public String checkPastDateService(Map<String,Object> params);
	
	public List<Map<String, Object>> routePlanAllService(String route_id);
	
	public RouteEntity routeDetailService(Map<String,Object> params);
	
	public Map<String, Object> routeSingleRoomService(Map<String,Object> params);
	
	public void updateSingleRoomService(Map<String,Object> params);
	
	public int trafficPastService(Map<String,Object> params);
	
	public List<Map<String, Object>> listService(Map<String,Object> params);

	public int countService(Map<String,Object> params);
	
	public Map<String, Object> saveService(Map<String,Object> params)throws Exception;
	
	public Map<String, Object> updateService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> routeTitleISExistService(Map<String,Object> params);
	
	public List<Map<String, Object>> listRouteCityService(Map<String,Object> params);
	
	public List<Map<String, Object>> listRouteDayService(Map<String,Object> params);
	public Map<String, Object> routeDayInfoService(Map<String,Object> params);
	public void saveRouteDayService(Map<String,Object> params)throws Exception;
	public void updateRouteDayService(Map<String,Object> params)throws Exception;
	public void delRouteDayService(Map<String,Object> params)throws Exception;
	public void moveRouteDayService(Map<String,Object> params)throws Exception;
	
	public void delRouteDayDetailService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listScenicService(Map<String,Object> params);
	public int countScenicService(Map<String,Object> params);
	
	public void saveRouteInfoService(Map<String,Object> params)throws Exception;
	
	public void route_calendar(String route_id) throws Exception;
	public void getOutTimeByruleIdService(String rule_id)throws Exception;
	
	public void pubeRouteService(Map<String,Object> params)throws Exception;
	
	
	public List<Map<String, Object>> listRouteOtherService(Map<String,Object> params);
	public void saveRouteOtherService(Map<String,Object> params)throws Exception;
	
	public List<RouteDayDetail> listRouteDayDetaiService(Map<String,Object> params);
	public List<Map<String, Object>> listRouteScenicService(Map<String,Object> params);
	
	public List<String> listRouteTagService(Map<String,Object> params);
	public void saveRouteTagService(Map<String,Object> params)throws Exception;
	
	
	public List<Map<String, Object>> listRouteAlbumService(Map<String,Object> params);
	public void saveRouteAlbumService(Map<String,Object> params);
	public void delRouteAlbumService(Map<String,Object> params);
	
	public void setDefaultFaceService(Map<String,Object> params)throws Exception;
	
	public Map<String, Object> savePriceService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> calendarService(Map<String,Object> params);
	
	
	public List<Map<String, Object>> listRouteTrafficService(Map<String,Object> params);
	public void saveRouteTrafficService(Map<String,Object> params);
	public void delRouteTrafficService(Map<String,Object> params);
	 
	
	public List<Map<String, Object>> listRouteTrafficDetailService(Map<String,Object> params);
	public int countRouteTrafficDetailService(Map<String,Object> params);
	public int saveRouteTrafficDetailService(Map<String,Object> params)throws Exception;
	
	public Map<String,Object> usedSeatByRouteOrder(String rq,String plan_id,String ruleids);
	
	public List<Map<String, Object>> listRouteCalendarService(Map<String,Object> params);
	
	public List<Map<String, Object>> sumPricesService(Map<String,Object> params);
	
	public List<Map<String, Object>> listPlanPricesService(Map<String,Object> params);
	
	public List<Map<String,String>> listRouteTrafficInfoService(Map<String,Object> params);
	
	public List<Map<String, Object>> analyzeIds(List<Map<String, Object>> params);
	
	public int countEndCityService(Map<String,Object> params);
	
	public void delRouteService(Map<String,Object> params);
	
	public void updateRouteService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listRoutePriceService(Map<String, Object> params);
}
