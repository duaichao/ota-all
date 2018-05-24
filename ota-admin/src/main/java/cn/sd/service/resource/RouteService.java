package cn.sd.service.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.dao.resource.IRouteDao;
import cn.sd.dao.resource.ITrafficDao;
import cn.sd.entity.RouteEntity;
import cn.sd.entity.produce.RouteDayDetail;
import cn.sd.entity.produce.RouteDayDetailList;
import cn.sd.rmi.ServiceProxyFactory;
import cn.sd.service.order.ITrafficOrderFacadeService;

@Repository
@Service("routeService")
public class RouteService extends BaseDaoImpl implements IRouteService{
	
	@Resource
	private IRouteDao routeDao;
	@Resource
	private ITrafficDao trafficDao;
	@Resource
	private cn.sd.dao.produce.ITrafficDao produceTrafficDao;
	
	public void updateRouteEarnestService(Map<String,Object> params)throws Exception{
		this.routeDao.updateRouteEarnestDao(params);
	}
	
	private List<Map<String, Object>> jsonArrayToList(Object[] objArray, String[] keyNames){
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
	    for(int i = 0; i < objArray.length; i++){
	    	JSONObject jobject = JSONObject.fromObject(objArray[i]);
	    	Map<String, Object> data = new HashMap<String, Object>();
	    	for (int j = 0; j < keyNames.length; j++) {
	    		boolean b = jobject.has(keyNames[j]);
	    		if(b){
	    			data.put(keyNames[j], jobject.getString(keyNames[j]));
	    		}
			}
	    	datas.add(data);
		}
		return datas;
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void saveProContactService(Map<String,Object> params)throws Exception{
		
		this.routeDao.delProContactDao(params);
		
		String visitors = (String)params.get("models");
	    JSONArray jarray = JSONArray.fromObject(visitors);
		Object[] objArray = jarray.toArray();
		
		String[] keyNames = {"PRODUCE_ID", "TYPE", "CHINA_NAME", "MOBILE", "PHONE", "CARD_NO", "CONTACT", "IS_MESSAGE"};
		List<Map<String, Object>> datas = this.jsonArrayToList(objArray, keyNames);
		for (Map<String, Object> data : datas) {
			data.put("ID", CommonUtils.uuid());
			data.put("PRODUCE_ID", params.get("PRODUCE_ID"));
			data.put("CREATE_USER_ID", params.get("CREATE_USER_ID"));
			if(CommonUtils.checkString(data.get("IS_MESSAGE")) && (data.get("IS_MESSAGE").equals("true") || data.get("IS_MESSAGE").equals("1"))){
				data.put("IS_MESSAGE", 1);
			}else{
				data.put("IS_MESSAGE", 0);
			}
			this.routeDao.saveProContactDao(data);
		}
		
	}
	
	public List<Map<String, Object>> listProContactService(Map<String,Object> params){
		return this.routeDao.listProContactDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void copyDayService(Map<String,Object> params)throws Exception{
		
		this.routeDao.updateRouteAllDayNOAddDao(params);
		String dayId = CommonUtils.uuid();
		params.put("DAY_ID", dayId);
		this.routeDao.copyRouteDayDao(params);
		
		params.put("DAY_ID", params.get("OLD_DAY_ID"));
		List<RouteDayDetail> routeDayDetails = this.routeDao.listRouteDayDetailDao(params);
		Map<String, Object> p = new HashMap<String, Object>();
		for (RouteDayDetail routeDayDetail : routeDayDetails) {
			String dayDetailId = CommonUtils.uuid();
			
			p.clear();
			p.put("ID", dayDetailId);
			p.put("DAY_ID", dayId);
			p.put("TITLE", routeDayDetail.getTITLE());
			p.put("CONTENT", routeDayDetail.getCONTENT());
			this.routeDao.saveRouteDayDetailDao(p);
			
			p.clear();
			p.put("ROUTE_ID", params.get("ROUTE_ID"));
			p.put("DAY_DETAIL_ID", dayDetailId);
			p.put("OLD_DAY_DETAIL_ID", routeDayDetail.getID());
			
			this.routeDao.copyRouteScenicDao(p);
		}
		
	}
	
	public List<Map<String, Object>> listEndCityOfRouteByCompanyIdAndCityNameService(Map<String,Object> params){
		return this.routeDao.listEndCityOfRouteByCompanyIdAndCityNameDao(params);
	}
	
	public String checkPastDateService(Map<String,Object> params){
		return this.routeDao.checkPastDateDao(params);
	}
	
	public RouteEntity routeDetailService(Map<String,Object> params){
		return this.routeDao.routeDetailDao(params);
	}
	
	public Map<String, Object> routeSingleRoomService(Map<String,Object> params){
		return this.routeDao.routeSingleRoomDao(params);
	}
	
	public void updateSingleRoomService(Map<String,Object> params){
		this.routeDao.updateSingleRoomDao(params);
	}
	
	public int trafficPastService(Map<String,Object> params){
		return this.routeDao.trafficPastDao(params);
	}
	
	public List<Map<String, Object>> listService(Map<String,Object> params){
		return this.routeDao.listDao(params);
	}

	public int countService(Map<String,Object> params){
		return this.routeDao.countDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> saveService(Map<String,Object> params)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("statusCode", "1");
		String routeId = (String)params.get("ID");
		//编号生成失败-3
		String code = ServiceProxyFactory.getProxy().getRouteNoDao((String)params.get("COMPANY_ID"));
		if(code.equals("-1")){
			result.put("success", false);
			result.put("statusCode", "-3");
			return result;
		}
		params.put("NO", code);
		String[] BEGIN_CITYS = (String[])params.get("BEGIN_CITY");
		String[] END_CITYS = (String[])params.get("END_CITY");
		if(BEGIN_CITYS == null || BEGIN_CITYS.length == 0 ||
				END_CITYS == null || END_CITYS.length == 0){
			result.put("success", false);
			result.put("statusCode", "-2");
			return result;
		}
		Map<String, Object> _params = new HashMap<String, Object>();
		
		/**
		 * 保存线路属性
		 */
		_params.put("ROUTE_ID", routeId);
		_params.put("TAG_NAME_ZT", params.get("TAG_NAME_ZT"));
		_params.put("TAG_NAME_XL", params.get("TAG_NAME_XL"));
		this.saveRouteTagService(_params);
		
		int i = 0;
		String beginCity = "", beginCityId = "";
		for (String BEGIN_CITY : BEGIN_CITYS) {
			_params.clear();
			_params.put("ID", CommonUtils.uuid());
			_params.put("ROUTE_ID", routeId);
			_params.put("CITY_ID", BEGIN_CITY.split(",")[0]);
			_params.put("CITY_NAME", BEGIN_CITY.split(",")[1]);
			_params.put("TYPE", 1);
			_params.put("ORDER_BY", i++);
			this.routeDao.saveRouteCityDao(_params);
			if(!CommonUtils.checkString(beginCity)){
				beginCity = BEGIN_CITY.split(",")[1];
				beginCityId = BEGIN_CITY.split(",")[0];
			}
		}
		i = 0;
		String endCity = "", endCityId = "" ;
		for (String END_CITY : END_CITYS) {
			_params.clear();
			_params.put("ID", CommonUtils.uuid());
			_params.put("ROUTE_ID", routeId);
			_params.put("CITY_ID", END_CITY.split(",")[0]);
			_params.put("CITY_NAME", END_CITY.split(",")[1]);
			_params.put("TYPE", 2);
			_params.put("ORDER_BY", i++);
			this.routeDao.saveRouteCityDao(_params);
			if(!CommonUtils.checkString(endCity)){
				endCity = END_CITY.split(",")[1]; 
				endCityId = END_CITY.split(",")[0];
				
			}
		}
		this.routeDao.saveDao(params);
		String IS_FULL_PRICE = (String)params.get("IS_FULL_PRICE");
		if(CommonUtils.checkString(IS_FULL_PRICE) && IS_FULL_PRICE.equals("1")){
			_params.clear();
			String PLAN_ID = CommonUtils.uuid();
			String PLAN_DETAIL_ID = CommonUtils.uuid();
			String TRAFFIC_TITLE = beginCity+"-"+endCity+"-"+code;
			String TRAFFIC_ID = CommonUtils.uuid();
			
			/**
			 * 交通
			 */
			_params.put("ID", TRAFFIC_ID);
			_params.put("BEGIN_STATION", "");
			_params.put("END_CITY", endCity);
			_params.put("END_DATE", params.get("TRAFFIC_END_DATE")); 
			_params.put("END_STATION", "");
			_params.put("END_TIME", params.get("TRAFFIC_END_TIME"));
			_params.put("IS_SINGLE", 0);
			_params.put("ROTE", TRAFFIC_TITLE);
			_params.put("START_CITY", beginCity);
			_params.put("TITLE", TRAFFIC_TITLE);
			_params.put("TYPE", 0);
			_params.put("UPDATE_USER", params.get("CREATE_USER"));
			_params.put("COMPANY_ID", params.get("COMPANY_ID"));
			_params.put("CREATE_USER", params.get("CREATE_USER"));
			_params.put("CREATE_USER_ID", params.get("CREATE_USER_ID"));
			_params.put("CITY_ID", params.get("CITY_ID"));
			_params.put("CITY_NAME", params.get("CITY_NAME"));
			_params.put("IS_FULL_PRICE", "1");
			_params.put("IS_PUB", "1");
			this.trafficDao.saveDao(_params);
			
			/**
			 * 方案
			 */
			_params.clear();
			_params.put("ID", PLAN_ID);
			_params.put("ROUTE_ID", routeId);
			_params.put("NO", "1");
			_params.put("TITLE", TRAFFIC_TITLE);
			this.routeDao.saveRouteTrafficDao(_params);
			
			/**
			 * 方案详情
			 */
			_params.clear();
			_params.put("ID", PLAN_DETAIL_ID);
			_params.put("ROUTE_ID", routeId);
			_params.put("PLAN_ID", PLAN_ID);//方案ID
			_params.put("TRAFFIC_ID", TRAFFIC_ID);//交通ID
			_params.put("TRAFFIC_NAME", TRAFFIC_TITLE);//交通名称
			_params.put("BEGIN_CITY_NAME", beginCity);//出发城市
			_params.put("BEGIN_CITY_ID", beginCityId);//出发城市ID
			_params.put("END_CITY_ID", endCityId);//目的地城市
			_params.put("END_CITY_NAME", endCity);//目的地城市ID
			_params.put("STAY_CNT", 0);//停留天数
			_params.put("ORDER_BY", 1);//序号
			this.routeDao.saveRouteTrafficDetailDao(_params);
			
			/**
			 * 保存地接价
			 */
			_params.clear();
			List<Map<String, Object>> priceAttr = this.trafficDao.listAllPriceAttrDao(_params);
			String[][] price_type = {{"0FA5123749D28C87E050007F0100BCAD", "外卖"},{"0FA5123749D38C87E050007F0100BCAD", "同行"}};
			this.trafficDao.listAllPriceAttrDao(_params);
			for (int l = 0; l < priceAttr.size(); l++) {
				for (int j = 0; j < price_type.length; j++) {
					_params.clear();
					_params.put("ID", CommonUtils.uuid());
					_params.put("ENTITY_ID", routeId);
					_params.put("TYPE_ID", price_type[j][0]);
					_params.put("TYPE_NAME", price_type[j][1]);
					_params.put("ATTR_ID", priceAttr.get(l).get("ID"));
					_params.put("ATTR_NAME", priceAttr.get(l).get("TITLE"));
					_params.put("PRICE", 0);
					_params.put("UPDATE_USER_ID", params.get("CREATE_USER_ID"));
					_params.put("UPDATE_USER", params.get("CREATE_USER"));
					this.trafficDao.saveBasePriceDao(_params);
				}
			}
		}
		return result;
	}
	
	
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> updateService(Map<String,Object> params)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("statusCode", "1");
		Map<String, Object> _params = new HashMap<String, Object>();
		String[] BEGIN_CITYS = (String[])params.get("BEGIN_CITY");
		String[] END_CITYS = (String[])params.get("END_CITY");
		if(BEGIN_CITYS == null || BEGIN_CITYS.length == 0 ||
				END_CITYS == null || END_CITYS.length == 0){
			result.put("success", false);
			result.put("statusCode", "-1");
			return result;
		}
		
		/**
		 * 保存线路属性
		 */
		_params.put("ROUTE_ID", params.get("ID"));
		_params.put("TAG_NAME_ZT", params.get("TAG_NAME_ZT"));
		_params.put("TAG_NAME_XL", params.get("TAG_NAME_XL"));
		this.saveRouteTagService(_params);
		
		List<Map<String, Object>> routeCitys = this.routeDao.listRouteCityDao(_params);
		List<String> CITY_NAMES = new ArrayList<String>();
		boolean existCity = false;
		for (Map<String, Object> routeCity : routeCitys) {
			String CITY_NAME = (String)routeCity.get("CITY_NAME"); 
			for (int i = 0; i < END_CITYS.length; i++) {
				//城市存在城市是否存在
				if(CITY_NAME.equals(END_CITYS[i].split(",")[1])){
					CITY_NAMES.add(CITY_NAME);
					existCity = true;
					break;
				}
			}
			
			/**
			 * 不存在的城市删掉
			 */
			if(!existCity){
				_params.clear();
				_params.put("ID", (String)routeCity.get("ID"));
				this.routeDao.delRouteCityDao(_params);
				/**
				 * 级联删除城市下的route_day
				 */
				this.routeDao.delRouteDayByRouteCityIdDao(_params);
			}
			existCity = false;
		}
		_params.clear();
		_params.put("ROUTE_ID", params.get("ID"));
		this.routeDao.delRouteStartCityByRouteIdDao(_params);
		for (int i = 0; i < BEGIN_CITYS.length; i++) {
			_params.clear();
			_params.put("ID", CommonUtils.uuid());
			_params.put("ROUTE_ID", params.get("ID"));
			_params.put("CITY_ID", BEGIN_CITYS[i].split(",")[0]);
			_params.put("CITY_NAME", BEGIN_CITYS[i].split(",")[1]);
			_params.put("TYPE", 1);
			_params.put("ORDER_BY", i+1);
			this.routeDao.saveRouteCityDao(_params);
		}
		existCity = false;
		for (int i = 0; i < END_CITYS.length; i++) {
			for (String CITY_NAME : CITY_NAMES) {
				if(CITY_NAME.equals(END_CITYS[i].split(",")[1])){
					existCity = true;
				}
			}
			if(!existCity){
				_params.clear();
				_params.put("ID", CommonUtils.uuid());
				_params.put("ROUTE_ID", params.get("ID"));
				_params.put("CITY_ID", END_CITYS[i].split(",")[0]);
				_params.put("CITY_NAME", END_CITYS[i].split(",")[1]);
				_params.put("TYPE", 2);
				_params.put("ORDER_BY", i+1);
				this.routeDao.saveRouteCityDao(_params);
			}
			existCity = false;
		}
		this.routeDao.updateDao(params);
		_params.clear();
		_params.put("ROUTE_ID", params.get("ID"));
		this.saveRouteInfoService(_params);
		
		/**
		 * 综合报价线路
		 * 提前报名天数,提前报名时间
		 */
		String isFullPrice = (String)params.get("IS_FULL_PRICE");
		if(isFullPrice.equals("1")){
			List<Map<String, Object>> routeTraffic = this.routeDao.routeTrafficByRouteIdDao(_params);
			_params.put("ID", routeTraffic.get(0).get("TRAFFIC_ID"));
			_params.put("END_DATE", params.get("TRAFFIC_END_DATE"));
			_params.put("END_TIME", params.get("TRAFFIC_END_TIME"));
			_params.put("UPDATE_USER", params.get("UPDATE_USER"));
			this.trafficDao.editDao(_params);
		}
		return result;
	}
	
	public List<Map<String, Object>> routeTitleISExistService(Map<String,Object> params){
		return this.routeDao.routeTitleISExistDao(params);
	}
	
	public List<Map<String, Object>> listRouteDayService(Map<String,Object> params){
		return this.routeDao.listRouteDayDao(params);
	}
	
	public Map<String, Object> routeDayInfoService(Map<String,Object> params){
		Map<String, Object> routeDay = new HashMap<String, Object>();
		List<Map<String, Object>> routeDays = this.routeDao.listRouteDayDao(params);
		if(routeDays != null && routeDays.size() == 1){
			routeDay = routeDays.get(0);
			params.put("DAY_ID", (String)params.get("ID"));
			params.remove("ID");
			List<RouteDayDetail> routeDayDetails = this.routeDao.listRouteDayDetailDao(params);
			for (RouteDayDetail routeDayDetail: routeDayDetails) {
				params.put("DAY_DETAIL_ID", routeDayDetail.getID());
				List<Map<String, Object>> routeScenic = this.routeDao.listRouteScenicDao(params);
				routeDayDetail.setScenics(routeScenic);
			}
			routeDay.put("routeDayDetails", routeDayDetails);
		}
		return routeDay;
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void saveRouteDayService(Map<String,Object> params)throws Exception{
		this.routeDao.updateRouteAllDayNOAddDao(params);
		this.routeDao.saveRouteDayDao(params);
		this.saveRouteInfoService(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void updateRouteDayService(Map<String,Object> params)throws Exception{
		RouteDayDetailList lp = (RouteDayDetailList)params.get("lp");
		String ROUTE_ID = (String)params.get("ROUTE_ID");
		String DAY_ID = (String)params.get("ID");
		Map<String, Object> _params = new HashMap<String, Object>();
		
		/**
		 * 清理数据
		 * 	1.删除景点
		 * 	2.删除行程详情
		 */
		
		_params.put("DAY_ID", DAY_ID);
		this.routeDao.delRouteScenicDao(_params);
		this.routeDao.delRouteDayDetailDao(_params);
		
		/**
		 * 保存行程详情
		 * 保存景点
		 */
		for (int i = 0; i < lp.getLp().size(); i++) {
			RouteDayDetail routeDayDetail = (RouteDayDetail)lp.getLp().get(i);
			if(CommonUtils.checkString(routeDayDetail.getTITLE())){
				String DAY_DETAIL_ID = CommonUtils.uuid();
				List<String> SCENICS = routeDayDetail.getSCENIC();
				_params.clear();
				_params.put("ID", DAY_DETAIL_ID);
				_params.put("DAY_ID", DAY_ID);
				_params.put("TITLE", routeDayDetail.getTITLE());
				_params.put("CONTENT", routeDayDetail.getCONTENT());
				this.routeDao.saveRouteDayDetailDao(_params);
				
				//添加景点
				if(CommonUtils.checkList(SCENICS)){
					for (String SCENIC : SCENICS) {
						_params.clear();
						_params.put("ID", CommonUtils.uuid());
						_params.put("DAY_DETAIL_ID", DAY_DETAIL_ID);
						_params.put("ROUTE_ID", ROUTE_ID);
						_params.put("SCENIC_ID", SCENIC.split("-")[0]);
						_params.put("SCENIC_NAME", SCENIC.split("-")[1]);
						this.routeDao.saveRouteScenicDao(_params);
					}	
				}
				
			}
		}
		
		this.routeDao.updateRouteDayDao(params);
		
		
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void delRouteDayService(Map<String,Object> params)throws Exception{
		this.routeDao.updateRouteAllDayNOMinusDao(params);
		this.routeDao.updateRouteDayDao(params);
		this.saveRouteInfoService(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void moveRouteDayService(Map<String,Object> params)throws Exception{
		Map<String, Object> _params = new HashMap<String, Object>();
		_params.put("ID", params.get("ID"));
		int NO = Integer.parseInt(String.valueOf(((Map)this.routeDao.listRouteDayDao(_params).get(0)).get("NO")));
		_params.put("ID", params.get("TARGET_ID"));
		int NEWNO = Integer.parseInt(String.valueOf(((Map)this.routeDao.listRouteDayDao(_params).get(0)).get("NO")));
		params.put("NO", NO);
		params.put("NEWNO", NEWNO);
		if(NO < NEWNO){
			//下移------------
			this.routeDao.updateRouteAllDayNODownDao(params);
		}else{
			//上移------------
			this.routeDao.updateRouteAllDayNOUpDao(params);
		}
		params.put("NO", NEWNO);
		this.routeDao.updateRouteDayDao(params);
	}
	
	public void delRouteDayDetailService(Map<String,Object> params)throws Exception{
		this.routeDao.delRouteDayDetailDao(params);
	}
	
	public List<Map<String, Object>> listRouteCityService(Map<String,Object> params){
		return this.routeDao.listRouteCityDao(params);
	}
	
	/**
	 * 按规则日期重置线路日历
	 * */
	public void getOutTimeByruleIdService(String rule_id)throws Exception{
		//过期 和 超过180天的规则 不编辑
		int num = this.routeDao.getOutTimeByruleIdDao(rule_id);
		if(num>=0){
			num = this.routeDao.getOutTimeByruleIdBeginDao(rule_id);
			if(num<=180){
				getRouteIdByruleIdService(rule_id);
			}
			
		}
	}
	/**
	 * 按规则ID重置线路日历
	 * */
	private void getRouteIdByruleIdService(String rule_id)throws Exception{
		List<String> route_ids = this.routeDao.getRouteIdByruleIdDao(rule_id);
		for(String route_id : route_ids){
			this.route_calendar(route_id);
		}
	}
	/**
	 * 线路日历
	 * */
	@Transactional(rollbackFor={Exception.class})
	public void route_calendar(String route_id) throws Exception{
		try{
			String sql = this.route_calendar_sql(route_id);
			if(sql.equals("")) throw new Exception(route_id+"线路没有有效日期");
			
			this.routeDao.delRouteCalendarDao(route_id);
			if(!sql.equals("-1")){
				this.routeDao.routeCalendarDao(sql);
			}
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			throw new Exception(route_id+"线路日历生成异常");
		}
	}
	
	private String route_calendar_sql(String route_id){
		
		List<Map<String, Object>> plan_list = this.routePlanAllService(route_id);
		if(plan_list==null || plan_list.size()==0) return "-1";
		
		StringBuffer sql = new StringBuffer();
		Map date_map = this.routeMinDateService(route_id);//解决 6个月初始化问题
		  
		String MIN_DATE = (String)date_map.get("MIN_DATE");
		String MIN_LAST_DATE = (String)date_map.get("MIN_LAST_DATE");
		String NEXT_LAST_DATE = (String)date_map.get("NEXT_LAST_DATE");
		if(MIN_DATE == null || MIN_LAST_DATE == null || NEXT_LAST_DATE == null){
			return "-1";
		}
		
		sql.append(" select sys_guid() as ID, '"+route_id+"' as ROUTE_ID , a.RQ, a.ACTUAL_PRICE, a.ACTUAL_INTER_PRICE, a.PLANID, a.RULEIDS, a.PLANIDGROUP, a.RULEIDGROUP,a.DAY_NUM,a.END_TIME,a.TRAFFIC_ID from ( ");
		sql.append(" select a.rq,a.actual_price,a.actual_inter_price,a.RULEIDS,a.PLANID,ROW_NUMBER() OVER(PARTITION BY a.rq order by a.actual_price) as rn,wmsys.wm_concat(a.PLANID) OVER(PARTITION BY a.rq) as PLANIDGROUP,wmsys.wm_concat('@'||a.PLANID||'-'||a.RULEIDS) OVER(PARTITION BY a.rq) as RULEIDGROUP,a.DAY_NUM,a.END_TIME,a.traffic_id from ( ");
		int i=0;
		for(Map<String, Object> map : plan_list){
			if(i != 0){
				sql.append(" union all ");
			}else{
				i=i+1;
			}
			String plan_id = map.get("PLAN_ID").toString();
			date_map.put("PLAN_ID", plan_id);
			List<Map<String, Object>> rule_list = this.routeRulebyPlanService(date_map);
			this.routeRulebyPlanSqlHandleService(rule_list);
			String plan_sql = this.route_plan_calendar_sql(rule_list,map.get("CNT").toString(),plan_id);
			sql.append(plan_sql);
		}
		if(i==0) return "";
		sql.append(" ) a");
		sql.append(" ) a where rn=1");
		return sql.toString();
	}

	public List<Map<String, Object>> listScenicService(Map<String,Object> params){
		return this.routeDao.listScenicDao(params);
	}

	/**
	 * 获得线路最近出发日期,最近出发当月最后一天,下一个月最后一天或者下几个月的最后一天
	 **/
	private Map routeMinDateService(String route_id){	
		return this.routeDao.routeMinDateDao(route_id);
	}
	/**
	 * 获得线路所有方案
	 * */
	public List<Map<String, Object>> routePlanAllService(String route_id){	
		return this.routeDao.routePlanAllDao(route_id);
	}
	/**
	 * 按最近出发日期,最近出发当月最后一天,下一个月最后一天
	 * 获取一个方案的所有交通的所有规则SQL集合
	 * */
	private List<Map<String, Object>> routeRulebyPlanService(Map<String,Object> params){	
		return this.routeDao.routeRulebyPlanDao(params);
	}
	/**
	 * 替换sql中的日期
	 * */
	public void routeRulebyPlanSqlHandleService(List<Map<String, Object>> list){
		
		if(list==null || list.size() == 0) return;
		
		String ORDER_BY = "1";
		String ORDER_BY_MAP ="";
		String STAY_CNT = "0";
		int cnt = 0;
		
		for(Map<String, Object> map : list){
			ORDER_BY_MAP = map.get("ORDER_BY").toString();
			if(!ORDER_BY_MAP.equals(ORDER_BY)){//是否读取下一个交通
				ORDER_BY = ORDER_BY_MAP;
				cnt = cnt+Integer.parseInt(STAY_CNT);
			}
			STAY_CNT = map.get("STAY_CNT").toString();
			
			if(ORDER_BY_MAP.equals("1")){
				map.put("RULE_SQL", map.get("RULE_SQL").toString().replaceFirst("a\\.\\*", "a.id,a.rq "));
			}else{
				map.put("RULE_SQL", map.get("RULE_SQL").toString().replaceFirst("a\\.\\*", "a.id,to_char(to_date(a.rq,'yyyymmdd')-"+cnt+",'yyyymmdd') as rq "));
			}
			
		}
	}
	/**
	 * 一个方案的sql
	 * */
	private String route_plan_calendar_sql(List<Map<String, Object>> list,String cnt,String plan_id){
		
		if(list==null || list.size()==0) return "";
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.*,b.DAY_NUM,b.END_TIME,b.traffic_id from ( ");
		sql.append(" select a.*, '"+plan_id+"' as PLANID from ( ");
		sql.append(" select a.rq,wmsys.wm_concat(a.id) as RULEIDS,sum(a.actual_price) as actual_price,sum(a.actual_inter_price) as actual_inter_price,count(1) as cnt from ( ");
		int i=0;
		for(Map<String, Object> map : list){
			if(i != 0){
				sql.append(" union all ");
			}else{
				i=i+1;
			}
			//注意 只考虑首发交通的有效时间
			sql.append("select * from ("+map.get("RULE_SQL").toString()+" ) where isvalid='1' ");
		}
		sql.append(" ) a group by a.rq");
		sql.append(" ) a where cnt = "+cnt);//确定计算出交通组内的交通数量 与 设置的数量一致
		sql.append(" ) a ,(select a.plan_id,a.traffic_id,b.end_date as DAY_NUM,b.end_time as END_TIME from SD_PRO_ROUTE_TRAFFIC_DETAIL a,SD_PRO_TRAFFIC b WHERE a.TRAFFIC_id = b.id and order_by = 1 and a.is_del =0 and b.is_del=0 ) b where a.planid = b.plan_id");
		return sql.toString();
	}
	
	public int countScenicService(Map<String,Object> params){
		return this.routeDao.countScenicDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void saveRouteInfoService(Map<String,Object> params)throws Exception{
		//计算行程天数
		List<Map<String, Object>> routeDayNums = this.routeDao.countRouteDayNum(params);
		Map<String, Object> _params = new HashMap<String, Object>();
		int TOTAL_CNT_DAY = 0;
		for (Map<String, Object> routeDayNum : routeDayNums) {
			int CNT_DAY = Integer.parseInt(String.valueOf(routeDayNum.get("CNT_DAY")));
			String ROUTE_CITY_ID = (String)routeDayNum.get("ROUTE_CITY_ID");
			_params.put("ID", ROUTE_CITY_ID);
			_params.put("STAY_COUNT", CNT_DAY);
			TOTAL_CNT_DAY = TOTAL_CNT_DAY + CNT_DAY;
			this.routeDao.updateRouteCityDao(_params);
		}
		_params.clear();
		_params.put("ID", (String)params.get("ROUTE_ID"));
		_params.put("DAY_COUNT", TOTAL_CNT_DAY);
		this.routeDao.updateDao(_params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void pubeRouteService(Map<String,Object> params)throws Exception{
		Map<String,Object> _params = new HashMap<String,Object>();
		String ROUTE_ID = (String)params.get("ID");
		String IS_PUB = (String)params.get("IS_PUB");
		String models = (String)params.get("models");
		
		if(CommonUtils.checkString(IS_PUB) && IS_PUB.equals("1")){
			
			_params.put("ROUTE_ID", ROUTE_ID);
			this.routeDao.delRouteGrantDao(_params);
			
			if(CommonUtils.checkString(models)){
				
				JSONArray jarray = JSONArray.fromObject(models);
				
				if(jarray != null && !jarray.isEmpty()){
					Object[] objArray = jarray.toArray();
					for(int i=0;i<objArray.length;i++){
						JSONObject jobject = JSONObject.fromObject(objArray[i]);
						if(!jobject.getString("id").equals("root")){//ID是root不保存
							_params.clear();
							_params.put("COMPANY_ID", jobject.getString("id"));
							_params.put("ROUTE_ID", ROUTE_ID);
							_params.put("ID", CommonUtils.uuid());
							this.routeDao.saveRouteGrantDao(_params);
						}
					}
					params.put("IS_GRANT", 1);
				}else{
					params.put("IS_GRANT", 0);
				}
				
			}else{
				params.put("IS_GRANT", 0);
			}
			
			_params.clear();
			_params.put("ROUTE_ID", ROUTE_ID);
			int CITY_NUM = this.routeDao.countEndCityDao(_params);
			params.put("CITY_NUM", CITY_NUM);
			this.routeDao.updateDao(params);
			
		}else if(CommonUtils.checkString(IS_PUB) && IS_PUB.equals("2")){
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				_params.clear();
				_params.put("UPDATE_USER", (String)params.get("UPDATE_USER"));
				_params.put("UPDATE_USER_ID", (String)params.get("UPDATE_USER_ID"));
				_params.put("ID", jobject.get("ID"));
				_params.put("IS_PUB", IS_PUB);
				this.routeDao.updateDao(_params);
			}
		}
	}
	
	public List<Map<String, Object>> listRouteOtherService(Map<String,Object> params){
		return this.routeDao.listRouteOtherDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void saveRouteOtherService(Map<String,Object> params)throws Exception{
		Map<String, Object> _params = new HashMap<String, Object>();
		String routeId = (String)params.get("routeId");
		Map<String, Object> user = (Map<String, Object>)params.get("user");
		_params.put("ROUTE_ID", routeId);
		this.routeDao.delRouteOtherDao(_params);
		
		RouteDayDetailList infos = (RouteDayDetailList)params.get("infos");
		
		if(infos.getInclude() != null){
			for (int i = 0; i < infos.getInclude().size(); i++) {
				if(CommonUtils.checkString(infos.getInclude().get(i).getTITLE())){
					_params.put("ID", CommonUtils.uuid());
					_params.put("TITLE", infos.getInclude().get(i).getTITLE());
					_params.put("CONTENT", infos.getInclude().get(i).getCONTENT());
					_params.put("TYPE", 1);
					this.routeDao.saveRouteOtherDao(_params);
				}
			}
		}
		
		if(infos.getNoclude() != null){
			for (int i = 0; i < infos.getNoclude().size(); i++) {
				if(CommonUtils.checkString(infos.getNoclude().get(i).getTITLE())){
					_params.put("ID", CommonUtils.uuid());
					_params.put("TITLE", infos.getNoclude().get(i).getTITLE());
					_params.put("CONTENT", infos.getNoclude().get(i).getCONTENT());
					_params.put("TYPE", 2);
					this.routeDao.saveRouteOtherDao(_params);
				}
			}
		}
		
		if(infos.getNotice() != null){
			for (int i = 0; i < infos.getNotice().size(); i++) {
				if(CommonUtils.checkString(infos.getNotice().get(i).getTITLE())){
					_params.put("ID", CommonUtils.uuid());
					_params.put("TITLE", infos.getNotice().get(i).getTITLE());
					_params.put("CONTENT", infos.getNotice().get(i).getCONTENT());
					_params.put("TYPE", 3);
					this.routeDao.saveRouteOtherDao(_params);
				}
			}
		}
		
		if(infos.getTips() != null){
			for (int i = 0; i < infos.getTips().size(); i++) {
				if(CommonUtils.checkString(infos.getTips().get(i).getTITLE())){
					_params.put("ID", CommonUtils.uuid());
					_params.put("TITLE", infos.getTips().get(i).getTITLE());
					_params.put("CONTENT", infos.getTips().get(i).getCONTENT());
					_params.put("TYPE", 4);
					this.routeDao.saveRouteOtherDao(_params);
				}
			}
		}
		if(infos.getOther() != null){
			for (int i = 0; i < infos.getOther().size(); i++) {
				if(CommonUtils.checkString(infos.getOther().get(i).getTITLE())){
					_params.put("ID", CommonUtils.uuid());
					_params.put("TITLE", infos.getOther().get(i).getTITLE());
					_params.put("CONTENT", infos.getOther().get(i).getCONTENT());
					_params.put("TYPE", 5);
					_params.put("ORDER_BY", i);
					this.routeDao.saveRouteOtherDao(_params);
				}
			}
			
			if(infos.getOther().size() > 0){
				_params.clear();
				_params.put("ID", routeId);
				_params.put("IS_OTHER_FEE", 1);
				_params.put("UPDATE_USER", user.get("USER_NAME"));
				_params.put("UPDATE_USER_ID", user.get("ID"));
				this.routeDao.updateDao(_params);
			}
		}
		
	}
	
	public List<RouteDayDetail> listRouteDayDetaiService(Map<String,Object> params){
		return this.routeDao.listRouteDayDetailDao(params);
	}
	
	public List<Map<String, Object>> listRouteScenicService(Map<String,Object> params){
		return this.routeDao.listRouteScenicDao(params);
	}
	
	public List<String> listRouteTagService(Map<String,Object> params){
		return this.routeDao.listRouteTagDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void saveRouteTagService(Map<String,Object> params)throws Exception{
		Map<String, Object> _params = new HashMap<String, Object>();
		_params.put("ROUTE_ID", (String)params.get("ROUTE_ID"));
		this.routeDao.delRouteTagDao(_params);
		
		String[] TAG_NAME_ZT = (String[])params.get("TAG_NAME_ZT");
		if(TAG_NAME_ZT != null){
			for (int i = 0; i < TAG_NAME_ZT.length; i++) {
				_params.put("ID", CommonUtils.uuid());
				_params.put("TAG_NAME", TAG_NAME_ZT[i]);
				_params.put("TAG_TYPE", 1);
				this.routeDao.saveRouteTagDao(_params);
			}
		}
	}
	
	
	public List<Map<String, Object>> listRouteAlbumService(Map<String,Object> params){
		
		return this.routeDao.listRouteAlbumDao(params);
	}
	
	public void saveRouteAlbumService(Map<String,Object> params){
		this.routeDao.saveRouteAlbumDao(params);
	}
	
	public void delRouteAlbumService(Map<String,Object> params){
		this.routeDao.delRouteAlbumDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void setDefaultFaceService(Map<String,Object> params)throws Exception{
		Map<String, Object> _params = new HashMap<String, Object>();
		_params.put("ID", params.get("ID"));
		List<Map<String, Object>> album =this.routeDao.listRouteAlbumDao(_params);
		_params.put("ID", params.get("ROUTE_ID"));
		_params.put("FACE", album.get(0).get("IMG_PATH"));
		_params.put("UPDATE_USER", params.get("UPDATE_USER"));
		_params.put("UPDATE_USER_ID", params.get("UPDATE_USER_ID"));
		this.routeDao.updateDao(_params);
		this.routeDao.resetFaceDao(params);
		this.routeDao.setDefaultFaceDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> savePriceService(Map<String,Object> params)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> _params = new HashMap<String, Object>();
		String ROUTE_ID = (String)params.get("ROUTE_ID");
 		
		result.put("success", true);
		result.put("statusCode", 1);
		
		JSONArray PRICES = JSONArray.fromObject(params.get("PRICES"));
		int statusCode = this.validation(params);
		if(statusCode == 0){
			//清空线路基础价格库
			_params.put("ENTITY_ID", ROUTE_ID);
			this.trafficDao.delBasePriceDao(_params);
			
			//保存价格库
			for (Object PRICE : PRICES) {
				JSONObject _PRICE = JSONObject.fromObject((String)PRICE);
				_params.put("ID", CommonUtils.uuid());
				_params.put("ENTITY_ID", ROUTE_ID);
				_params.put("TYPE_ID", (String)_PRICE.get("TYPE_ID"));
				_params.put("TYPE_NAME", _PRICE.get("TYPE_NAME"));
				_params.put("ATTR_ID", (String)_PRICE.get("ATTR_ID"));
				_params.put("ATTR_NAME", _PRICE.get("ATTR_NAME"));
				if(CommonUtils.checkString(_PRICE.get("PRICE"))){
					_params.put("PRICE", _PRICE.get("PRICE"));
				}else{
					_params.put("PRICE", 0);
				}
				_params.put("UPDATE_USER_ID", (String)params.get("USER_ID"));
				_params.put("UPDATE_USER", (String)params.get("USER_NAME"));
				this.trafficDao.saveBasePriceDao(_params);
			}
			
			this.routeDao.updateSingleRoomDao(params);
		}else{
			result.put("success", false);
			result.put("statusCode", statusCode);
			return result;
		}
		return result;
	}
	
	public int validation(Map<String,Object> params){
		String ROUTE_ID = (String)params.get("ROUTE_ID"); 
		JSONArray PRICES = JSONArray.fromObject(params.get("PRICES"));
		if(!CommonUtils.checkString(ROUTE_ID) || PRICES == null || PRICES.isEmpty()){
			return -1;
		}
		
		//门市：0FA5123749D28C87E050007F0100BCAD, 同行：0FA5123749D38C87E050007F0100BCAD, 成人：0FA5123749CF8C87E050007F0100BCAD
		int status = 0;
		for (Object PRICE : PRICES) {
			JSONObject _PRICE = JSONObject.fromObject((String)PRICE);
			String TYPE_ID = (String)_PRICE.get("TYPE_ID");
			String ATTR_ID = (String)_PRICE.get("ATTR_ID");
			
			/**
			 * 0FA5123749D28C87E050007F0100BCAD	门市
			 * 0FA5123749D38C87E050007F0100BCAD	同行
			 */
			
			if(ATTR_ID.equals("0FA5123749CF8C87E050007F0100BCAD")){
				if(TYPE_ID != null && TYPE_ID.equals("0FA5123749D28C87E050007F0100BCAD") && _PRICE.get("PRICE") != null && CommonUtils.checkString(String.valueOf(_PRICE.get("PRICE")))){
					status++;
				}else if(TYPE_ID != null && TYPE_ID.equals("0FA5123749D38C87E050007F0100BCAD") &&  _PRICE.get("PRICE") != null && CommonUtils.checkString(String.valueOf(_PRICE.get("PRICE")))){
					status++;
				}
			}
		}
		
		if(status != 2){
			return -1;
		}
		return 0;
	}
	
	public List<Map<String, Object>> calendarService(Map<String,Object> params){
		return this.routeDao.calendarDao(params);
	}
	
	public List<Map<String, Object>> listRouteTrafficService(Map<String,Object> params){
		return this.routeDao.listRouteTrafficDao(params);
	}
	public void saveRouteTrafficService(Map<String,Object> params){
		int NO = this.routeDao.maxTrafficNODao(params);
		params.put("NO", NO);
		this.routeDao.saveRouteTrafficDao(params);
	}
	@Transactional(rollbackFor={Exception.class})
	public void delRouteTrafficService(Map<String,Object> params){
		this.routeDao.updateRouteTrafficNOMinusDao(params);
		this.routeDao.delRouteTrafficDao(params);
	}
	
	public List<Map<String, Object>> listRouteTrafficDetailService(Map<String,Object> params){
		return this.routeDao.listRouteTrafficDetailDao(params);
	}
	
	public int countRouteTrafficDetailService(Map<String,Object> params){
		return this.routeDao.countRouteTrafficDetailDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public int saveRouteTrafficDetailService(Map<String,Object> params)throws Exception{
		Map<String, Object> _params = new HashMap<String, Object>();
		String PLAN_ID = (String)params.get("PLAN_ID");
		_params.put("ID", PLAN_ID);
		_params.put("TITLE", params.get("TITLE"));
		this.routeDao.updateRouteTrafficDao(_params);
		
		List<Map<String, Object>> newdetails = new ArrayList<Map<String, Object>>();
		Map<String, Object> subDetail = new HashMap<String, Object>();
		StringBuffer IDS = new StringBuffer();
		boolean status = false;
		String[] DETAIL_ID = (String[])params.get("DETAIL_ID");//交通ID
		String[] TRAFFIC_ID = (String[])params.get("TRAFFIC_ID");//交通ID
		String[] TRAFFIC_NAME = (String[])params.get("TRAFFIC_NAME");//交通名称
		String[] BEGIN_CITY_NAME = (String[])params.get("BEGIN_CITY_NAME");//出发城市
		String[] BEGIN_CITY_ID = (String[])params.get("BEGIN_CITY_ID");//出发城市ID
		String[] END_CITY_ID = (String[])params.get("END_CITY_ID");//目的地城市
		String[] END_CITY_NAME = (String[])params.get("END_CITY_NAME");//目的地城市ID
		String[] STAY_CNT = (String[])params.get("STAY_CNT");//停留天数
		String[] ORDER_BY = (String[])params.get("ORDER_BY");//序号
		
		for (int i = 0; i < TRAFFIC_ID.length; i++) {
			Map<String, Object> detail = new HashMap<String, Object>();
			if(CommonUtils.checkString(DETAIL_ID[i])){
				detail.put("ID", DETAIL_ID[i]);
				detail.put("PLAN_ID", PLAN_ID);
				detail.put("ORDER_BY", ORDER_BY[i]);
				detail.put("BEGIN_CITY_ID", BEGIN_CITY_ID[i]);
				detail.put("BEGIN_CITY_NAME", BEGIN_CITY_NAME[i]);
				detail.put("END_CITY_ID", END_CITY_ID[i]);
				detail.put("END_CITY_NAME", END_CITY_NAME[i]);
				detail.put("TRAFFIC_ID", TRAFFIC_ID[i]);
				detail.put("TRAFFIC_NAME", TRAFFIC_NAME[i]);
				detail.put("STAY_CNT", STAY_CNT[i]);
				subDetail.put(DETAIL_ID[i], detail);
				IDS.append("'"+DETAIL_ID[i]+"',");
			}else{
				detail.put("ID", CommonUtils.uuid());
				detail.put("PLAN_ID", PLAN_ID);
				detail.put("ORDER_BY", ORDER_BY[i]);
				detail.put("BEGIN_CITY_ID", BEGIN_CITY_ID[i]);
				detail.put("BEGIN_CITY_NAME", BEGIN_CITY_NAME[i]);
				detail.put("END_CITY_ID", END_CITY_ID[i]);
				detail.put("END_CITY_NAME", END_CITY_NAME[i]);
				detail.put("TRAFFIC_ID", TRAFFIC_ID[i]);
				detail.put("TRAFFIC_NAME", TRAFFIC_NAME[i]);
				detail.put("STAY_CNT", STAY_CNT[i]);
				newdetails.add(detail);
				status = true;
			}
		}
		
		//删除交通
		int l = 0;
		if(IDS.length()!=0){
			_params.clear();
			_params.put("IDS", IDS.subSequence(0, IDS.length()-1));
			_params.put("PLAN_ID", PLAN_ID);
			l = this.routeDao.delRouteTrafficDetailDao(_params);
			
			_params.clear();
			_params.put("PLAN_ID", PLAN_ID);
			List<Map<String, Object>> details = this.routeDao.listRouteTrafficDetailDao(_params);
			for (Map<String, Object> detail : details) {
				String ID = (String)detail.get("ID");
				Map<String, Object> _detail = (Map<String, Object>)subDetail.get(ID);
				if(!detail.toString().equals(_detail.toString())){
					this.routeDao.updateRouteTrafficDetailDao(_detail);
					status = true;
				}
			}
		}else{
			_params.clear();
			_params.put("PLAN_ID", PLAN_ID);
			List<Map<String, Object>> details = this.routeDao.listRouteTrafficDetailDao(_params);
			if(CommonUtils.checkList(details)){
				this.routeDao.delRouteTrafficDetailByPlanIdDao(_params);
			}
		}
		
		for (Map<String, Object> detail : newdetails) {
			this.routeDao.saveRouteTrafficDetailDao(detail);
		}
		
		if(status || l > 0){
			return 1;
		}else{
			return 0;
		}
		
	}
	
	/**
	 * rq = 20140101 
	 * rules = 'id','id'
	 * Map
	 * 	key:isSeat	 value:1 有座位 0没有座位
	 *  key:seatInfo value:List<String>=TRAFFIC_NAME-DATE-STAY_CNT-SEAT
	 *  key:seat 有效座位数
	 * */
	public Map<String,Object> usedSeatByRouteOrder(String rq,String plan_id,String ruleids){
		int flag=1;
		Map<String,Map<String,String>> rs = new HashMap<String,Map<String,String>>();
		
		List<String> keys = new ArrayList<String>();
		
		int stay_cnt = 0;
		int cnt_for = 0;
		int seat_min = 0;
		
		Date dd = DateUtil.subDate(rq);
		
		List<Map<String, Object>> trafficStayList = this.produceTrafficDao.getPlanTraffic(plan_id);
		if(trafficStayList.size()==0) return null;
		
		StringBuffer sb = new StringBuffer();
		for(Map<String, Object> map : trafficStayList){
			Map<String,String> temp = new HashMap<String,String>();
			if(cnt_for==0){
				temp.put("TRAFFIC_NAME", map.get("TITLE").toString());
				temp.put("DATE", rq);
				temp.put("STAY_CNT", map.get("STAY_CNT").toString());
				temp.put("IS_FULL_PRICE", String.valueOf(map.get("IS_FULL_PRICE")));
				rs.put(map.get("TRAFFIC_ID").toString(),temp);
				cnt_for = cnt_for + 1;
			}else{
				temp.put("TRAFFIC_NAME", map.get("TITLE").toString());
				temp.put("DATE", DateUtil.getNowDateDAY(dd,stay_cnt).replaceAll("-", ""));
				temp.put("STAY_CNT", map.get("STAY_CNT").toString());
				temp.put("IS_FULL_PRICE", String.valueOf(map.get("IS_FULL_PRICE")));
				rs.put(map.get("TRAFFIC_ID").toString(),temp);
			}
			stay_cnt = stay_cnt + Integer.valueOf(map.get("STAY_CNT").toString());
		}
		
		List<Map<String, Object>> list = this.produceTrafficDao.getRidTraffidIdByRuleId(ruleids);
		Map<String, String> params = new HashMap<String, String>();
		for(Map<String, Object> map : list){
			int seat_min_temp = 0;
			String traffic_id = map.get("TRAFFIC_ID").toString();
			String rule_id = map.get("ID").toString();
			params.put("RULE_ID", rule_id);
			Map<String,String> temp  = (Map<String,String>)rs.get(traffic_id);
			params.put("PARAM_DATE", temp.get("DATE").toString());
			Map<String, Object> dsr = this.produceTrafficDao.getDateSeatByRuleId(params);
			if(dsr==null){
				seat_min_temp = 0;
				temp.put("SEAT", "0");
				flag=0;
			}else{
				Object obj  = dsr.get("ACTUAL_SEAT");
				if(null==obj || "".equals(obj)|| "0".equals(obj.toString())){
					temp.put("SEAT", "0");
					seat_min_temp = 0;
					flag=0;
				}else{
					temp.put("SEAT", obj.toString());
					seat_min_temp = Integer.parseInt(obj.toString());
				}
			}
			
			if(seat_min > seat_min_temp){
				seat_min = seat_min_temp;
			}
		}
		
		List<Map<String,String>> rs_list = new LinkedList<Map<String,String>>();
		for(Map<String, Object> map : trafficStayList){
			rs_list.add(rs.get(map.get("TRAFFIC_ID").toString()));
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("isSeat", flag);
		map.put("seatInfo", rs_list);
		map.put("seat", seat_min);
		
		return map;
	}
	
	public List<Map<String, Object>> listRouteCalendarService(Map<String,Object> params){
		return this.routeDao.listRouteCalendarDao(params);
	}
	
	public List<Map<String, Object>> sumPricesService(Map<String,Object> params){
		return this.routeDao.sumPricesDao(params);
	}
	
	public List<Map<String, Object>> listPlanPricesService(Map<String, Object> params){
		Map<String, Object> _params = new HashMap<String, Object>();
		Map<String, Object> attrInfo = this.getAttrInfo(params);
		List<Map<String, Object>> routeCalendars = this.listRouteCalendarService(params);
		List<Map<String, Object>> ids = this.analyzeIds(routeCalendars);
		_params.put("attrInfo", attrInfo);
		_params.put("routeCalendars", routeCalendars);
		_params.put("PLAN_ID", params.get("PLAN_ID"));
		_params.put("ROUTE_ID", params.get("ROUTE_ID"));
		_params.put("ids", ids);
		String sql = this.getPriceSql(_params);
		_params.clear();
		_params.put("sql", sql);
		return this.sumPricesService(_params);
	}
	
	public List<Map<String, Object>> analyzeIds(List<Map<String, Object>> params){
		List<Map<String, Object>> IDS = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> p : params) {
			String[] RULEIDGROUP = p.get("RULEIDGROUP").toString().split("@");
			for (int i = 0; i < RULEIDGROUP.length; i++) {
				Map<String, Object> ids = new HashMap<String, Object>();
				if(CommonUtils.checkString(RULEIDGROUP[i])){
					String[] RULEIDGROUPS = RULEIDGROUP[i].split("-");
					String[] RULE_IDS = RULEIDGROUPS[1].toString().split(",");
					StringBuffer _IDS = new StringBuffer();
					for (int j = 0; j < RULE_IDS.length; j++) {
						_IDS.append("'"+RULE_IDS[j]+"',");
					}
					ids.put("ruleIds", _IDS.toString().substring(0, _IDS.toString().length()-1));
					ids.put("planId", RULEIDGROUPS[0]);
				}
				if(!ids.isEmpty()){
					IDS.add(ids);
				}
			}
		}
		return IDS;
	}
	
	private Map<String, Object> getAttrInfo(Map<String, Object> params){
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> priceAttrs = this.trafficDao.listPriceAttrDao(params);
		
		Map<String, Object> attr = new HashMap<String, Object>();
		
		StringBuffer traffic_sql = new StringBuffer();
		StringBuffer route_sql = new StringBuffer();
		StringBuffer item_sql = new StringBuffer();
		
		/**
		 * 整理线路和交通SQL
		 */
		for (Map<String, Object> priceAttr : priceAttrs) {
			String ID = (String)priceAttr.get("ID");
			item_sql.append("a."+priceAttr.get("CON_NAME")+" + b."+priceAttr.get("CON_NAME") +" as "+priceAttr.get("CON_NAME")+", ");
			traffic_sql.append(" ,sum(decode(c_order_by,"+priceAttr.get("ORDER_BY").toString()+",price,0)) as "+priceAttr.get("CON_NAME").toString()+" ");
			route_sql.append(" ,sum(decode(c_order_by,"+priceAttr.get("ORDER_BY").toString()+",price,0)) as "+priceAttr.get("CON_NAME").toString()+" ");
			attr.put((String)priceAttr.get("CON_NAME"), (String)priceAttr.get("TITLE"));
			if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
				params.put("PID", ID);
				List<Map<String, Object>> childs = this.trafficDao.listPriceAttrDao(params);
				for (Map<String, Object> child : childs) {
					item_sql.append("a."+child.get("CON_NAME")+" + b."+child.get("CON_NAME") +" as "+child.get("CON_NAME")+", ");
					attr.put((String)child.get("CON_NAME"), (String)child.get("TITLE"));
					traffic_sql.append(" ,sum(decode(c_order_by,2,price,0)) as "+child.get("CON_NAME").toString()+" ");
					route_sql.append(" ,sum(decode(c_order_by,"+child.get("ORDER_BY").toString()+",price,0)) as "+child.get("CON_NAME").toString()+" ");
				}
			}
		}
		result.put("attr", attr);
		result.put("item_sql", item_sql.toString().substring(0, item_sql.toString().length()-1));
		result.put("traffic_sql", traffic_sql.toString());
		result.put("route_sql", route_sql.toString());
		return result;
	}
	
	private String getPriceSql(Map<String, Object> params){
		Map<String, Object> _params = new HashMap<String, Object>();
		List<Map<String, Object>> routeCalendars = (List<Map<String, Object>>)params.get("routeCalendars");
		List<Map<String, Object>> ids = (List<Map<String, Object>>)params.get("ids");
		Map<String, Object> attrInfo = (Map<String, Object>)params.get("attrInfo");
		StringBuffer priceSql = new StringBuffer();
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> _ids = ids.get(i);
			
			String PLANID = (String)_ids.get("planId");
			if(PLANID.equals(params.get("PLAN_ID"))){
				String RULE_IDS = (String)_ids.get("ruleIds");
				StringBuffer sql = new StringBuffer();
				sql.append("select a.*, b.title as PLAN_TITLE from ");
				
				sql.append("(select "+attrInfo.get("item_sql")+" a.id,a.title,a.plan_id from ");
				
				/**
				 * 交通规则价格sql
				 */
				_params.clear();
				_params.put("sql", attrInfo.get("traffic_sql"));
				_params.put("PLANID", PLANID);
				_params.put("RULE_IDS", RULE_IDS);
				_params.put("temp_name", "a");
				String traffic_sql = this.getRuleSql(_params);
				sql.append(traffic_sql);

				/**
				 * 线路规则价格sql
				 */
				_params.put("sql", attrInfo.get("route_sql"));
				_params.put("RULE_IDS", "'"+params.get("ROUTE_ID")+"'");
				_params.put("temp_name", "b");
				String route_sql = this.getRuleSql(_params);
				sql.append(","+route_sql);
				sql.append(" where a.plan_id = b.plan_id and a.id = b.id and a.title=b.title ");
				
				sql.append(" ) a, sd_pro_route_traffic b where a.plan_id = b.id ");
				
				priceSql.append(sql.toString());
			}
		}
		return priceSql.toString();
		
	}
	
	private String getRuleSql(Map<String, Object> params){
		StringBuffer sql = new StringBuffer();
		String temp_name = (String)params.get("temp_name");
		sql.append(" (");
		sql.append(" select a.b_order_by,a.type_id as id,a.type_name as title, '"+params.get("PLANID")+"' as plan_id");
		sql.append(params.get("sql"));
		sql.append(" from (");
		sql.append(" select a.*,b.order_by as b_order_by,c.order_by as c_order_by from sd_pro_base_price a,SD_PRO_PRICE_TYPE b,SD_PRO_PRICE_ATTR c");
		sql.append(" where a.TYPE_ID = b.id and a.ATTR_ID=c.id ");
		sql.append(" and a.entity_id in ("+params.get("RULE_IDS")+")");
		sql.append(" ) a group by a.b_order_by,a.type_id,a.type_name");
		sql.append(" )  "+temp_name);
		
		return sql.toString();
	}
	
	public List<Map<String,String>> listRouteTrafficInfoService(Map<String,Object> params){
		List<Map<String, Object>> routeCalendars = this.listRouteCalendarService(params);
		List<Map<String, Object>> ids = this.analyzeIds(routeCalendars);
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> _ids = ids.get(i);
			
			String PLANID = (String)_ids.get("planId");
			if(PLANID.equals(params.get("PLAN_ID"))){
				Map<String, Object> searInfo = this.usedSeatByRouteOrder((String)params.get("RQ"), PLANID, (String)_ids.get("ruleIds"));
				if(CommonUtils.checkMap(searInfo)){
					return (List<Map<String,String>>)searInfo.get("seatInfo");
				}
				break;
			}
		}
		return null;
	}

	public int countEndCityService(Map<String,Object> params){
		return this.routeDao.countEndCityDao(params);
	}
	
	public void delRouteService(Map<String,Object> params){
		this.routeDao.delRouteDao(params);
	}
	
	public void updateRouteService(Map<String,Object> params)throws Exception{
		this.routeDao.updateDao(params);
	}
	
	public List<Map<String, Object>> listRoutePriceService(Map<String, Object> params){
		Map<String, Object> _params = new HashMap<String, Object>();
		Map<String, Object> attrInfo = this.getAttrInfo(params);
		
		_params.put("sql", attrInfo.get("route_sql"));
		_params.put("RULE_IDS", "'"+params.get("ROUTE_ID")+"'");
		_params.put("temp_name", "a");
		String route_sql = this.getRuleSql(_params);
		StringBuffer sql = new StringBuffer();
		sql.append("select a.* from ");
		sql.append(route_sql);
		_params.clear();
		_params.put("sql", sql);
		return this.sumPricesService(_params);
	}
}
