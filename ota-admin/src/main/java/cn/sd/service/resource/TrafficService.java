package cn.sd.service.resource;

import java.util.Date;
import java.util.HashMap;
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
import cn.sd.dao.resource.ITrafficDao;

@Repository
@Service("trafficService")
@SuppressWarnings("all")
public class TrafficService extends BaseDaoImpl implements ITrafficService{
	
	@Resource
	private ITrafficDao trafficDao;
	@Resource
	private IRouteService routeService;
	
	public List<Map<String, Object>> listService(Map<String,Object> params){
		return this.trafficDao.listDao(params);
	}

	public int countService(Map<String,Object> params){
		return this.trafficDao.countDao(params);
	}
	
	public void saveService(Map<String,Object> params)throws Exception{
		this.trafficDao.saveDao(params);
	}
	
	public void editService(Map<String,Object> params)throws Exception{
		this.trafficDao.editDao(params);
	}
	
	public List<Map<String, Object>> listMusterService(Map<String,Object> params){
		return this.trafficDao.listMusterDao(params);
	}
	
	public void saveMusterService(Map<String,Object> params)throws Exception{
		this.trafficDao.saveMusterDao(params);
	}
	
	public void editMusterService(Map<String,Object> params)throws Exception{
		this.trafficDao.editMusterDao(params);
	}
	
	public void callProcedureRuleCompile(Map<String,Object> params)throws Exception{
		this.trafficDao.callProcedureRuleCompile(params);
	}
	
//	callProcedureRuleCompile
	
	@Transactional(rollbackFor={Exception.class})
	public Map<Object, Object> saveTrafficRuleService(Map<String,Object> params)throws Exception{
		Map<String, Object> _params = new HashMap<String, Object>();
		Map<Object, Object> result = new HashMap<Object, Object>();
		int statusCode = this.validation(params);
		if(statusCode != 0){
			result.put("success", false);
			result.put("statusCode", statusCode);
			return result;
		}
		String editType = (String)params.get("editType");
		if(CommonUtils.checkString(editType)){
			//删除规则,删除扩展,删除价格库
			_params.put("ID", params.get("ID"));
			this.trafficDao.delRuleDao(_params);
			
			_params.clear();
			_params.put("RULE_ID", params.get("ID"));
			this.trafficDao.delRuleExpandDao(_params);
			
			_params.clear();
			_params.put("ENTITY_ID", params.get("ID"));
			this.trafficDao.delBasePriceDao(_params);
			
			_params.clear();
			_params.put("ID", params.get("ID"));
			this.trafficDao.delProcedureRuleCompileDao(_params);
		}
		String TYPE = (String)params.get("TYPE"), 
		DAYS = (String)params.get("DAYS");
		JSONArray PRICES = JSONArray.fromObject(params.get("PRICES"));
		
		String RULE_ID = (String)params.get("ID");
		_params.clear();
		_params.put("RULE_ID", RULE_ID);
		
		//外卖：0FA5123749D28C87E050007F0100BCAD, 同行：0FA5123749D38C87E050007F0100BCAD, 成人：0FA5123749CF8C87E050007F0100BCAD
		double retail_man_price = 0, inter_man_price = 0;
		for (Object PRICE : PRICES) {
			JSONObject _PRICE = JSONObject.fromObject((String)PRICE);
			String TYPE_ID = (String)_PRICE.get("TYPE_ID");
			String ATTR_ID = (String)_PRICE.get("ATTR_ID");
			
			/**
			 * 0FA5123749D28C87E050007F0100BCAD	外卖
			 * 0FA5123749D38C87E050007F0100BCAD	同行
			 */
			
			if(ATTR_ID.equals("0FA5123749CF8C87E050007F0100BCAD")){
				if(TYPE_ID.equals("0FA5123749D28C87E050007F0100BCAD")){
					retail_man_price = Double.parseDouble(String.valueOf(_PRICE.get("PRICE")));
				}else if(TYPE_ID.equals("0FA5123749D38C87E050007F0100BCAD")){
					inter_man_price = Double.parseDouble(String.valueOf(_PRICE.get("PRICE")));
				}
			}
			
			//保存价格库
			_params.put("ID", CommonUtils.uuid());
			_params.put("ENTITY_ID", RULE_ID);
			_params.put("TYPE_ID", TYPE_ID);
			_params.put("TYPE_NAME", _PRICE.get("TYPE_NAME"));
			_params.put("ATTR_ID", ATTR_ID);
			_params.put("ATTR_NAME", _PRICE.get("ATTR_NAME"));
			_params.put("PRICE", _PRICE.get("PRICE"));
			_params.put("UPDATE_USER_ID", (String)params.get("USER_ID"));
			_params.put("UPDATE_USER", (String)params.get("USER_NAME"));
			this.trafficDao.saveBasePriceDao(_params);
		}
		
		_params.clear();
		_params.put("RULE_ID", RULE_ID);
		//保存价格规则扩展
		if(TYPE.equals("1") || TYPE.equals("2")){
			//星期规则 WEEKS
			String[] WEEKS = (String[])params.get("WEEKS");
			for (String week : WEEKS) {
				_params.put("ID", CommonUtils.uuid());
				_params.put("TEXT", week);
				this.trafficDao.saveTrafficRuleExpandDao(_params);
			}
		}else if(TYPE.equals("3") || TYPE.equals("4")){
			//日期规则DAYS
			String[] DAYS_ARRAY = DAYS.split("-");
			for (String DAY : DAYS_ARRAY) {
				_params.put("ID", CommonUtils.uuid());
				_params.put("TEXT", DAY);
				this.trafficDao.saveTrafficRuleExpandDao(_params);
			}
		}
		String IS_PUB = (String)params.get("IS_PUB");
		if(!CommonUtils.checkString(IS_PUB)){
			params.put("IS_PUB", "0");
		}
		//保存价格规则
		params.put("ID", RULE_ID);
		params.put("BASE_PRICE", retail_man_price);
		params.put("BASE_INTER_PRICE", inter_man_price);
		this.trafficDao.saveTrafficRuleDao(params);
		
		result.put("success", true);
		result.put("statusCode", "1");
		result.put("RULE_ID", RULE_ID);
		return result;
	}
	
	public void delRuleService(Map<String,Object> params)throws Exception{
		this.trafficDao.delRuleDao(params);
	}
	
	private int validation(Map<String,Object> params){
		Map<String, Object> p = new HashMap<String, Object>();
		String BEGIN_DATE = (String)params.get("BEGIN_DATE"),
		END_DATE = (String)params.get("END_DATE"), TRAFFIC_ID = (String)params.get("TRAFFIC_ID"), 
		SEAT_COUNT = (String)params.get("SEAT_COUNT"),TYPE = (String)params.get("TYPE"), 
		DAYS = (String)params.get("DAYS"),
		editType = (String)params.get("editType");
		JSONArray PRICES = JSONArray.fromObject(params.get("PRICES"));
		String[] WEEKS = (String[])params.get("WEEKS");
		
		if(!CommonUtils.checkString(BEGIN_DATE) || !CommonUtils.checkString(END_DATE) 
				|| !CommonUtils.checkString(TRAFFIC_ID) || !CommonUtils.checkString(SEAT_COUNT) 
				|| !CommonUtils.checkString(TYPE) || "01234".indexOf(TYPE) == -1 
				|| PRICES == null || PRICES.isEmpty()){
			return -1;
		}
		
		
		if("1".equals(TYPE) || "2".equals(TYPE)){
			//星期规则 WEEKS
			if(WEEKS == null && WEEKS.length > 0){
				return -1;
			}
		}else if("3".equals(TYPE) || "4".equals(TYPE)){
			//日期规则 DAYS 
			if(!CommonUtils.checkString(DAYS)){
				return -1;
			}
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
		
		p.clear();
		p.put("TRAFFIC_ID", TRAFFIC_ID);
		p.put("NOTEQUALSID", (String)params.get("ID"));
		/**
		 * 日期区间是否已存在
		 */
		
		List<Map<String, Object>> data = this.trafficDao.listTrafficRuleDao(p);
		if(CommonUtils.checkList(data)){
			Date BEGINDATE = DateUtil.subDate(BEGIN_DATE), ENDDATE =DateUtil.subDate(END_DATE);
			List<Date> allDates = DateUtil.getAllDates(BEGINDATE, ENDDATE);
			for (Map<String, Object> d : data) {
				Date _BEGIN_DATE = DateUtil.subDate((String)d.get("BEGIN_DATE")), _END_DATE = DateUtil.subDate((String)d.get("END_DATE"));
				List<Date> _allDates = DateUtil.getAllDates(_BEGIN_DATE, _END_DATE);
				
				if(DateUtil.dateCompare(BEGINDATE, _BEGIN_DATE) == 0 && DateUtil.dateCompare(BEGINDATE, _END_DATE) == 0
						|| DateUtil.dateCompare(ENDDATE, _BEGIN_DATE) == 0 && DateUtil.dateCompare(ENDDATE, _END_DATE) == 0){
					return -2;
				}else{
					for (Date date : allDates) {
						for (Date _date : _allDates) {
							if(DateUtil.dateCompare(date, _date) == 0){
								return -2;
							}
						}
					}
				}
			}
		}
		return 0;
	}
	
	@Transactional(rollbackFor={Exception.class})
	public Map<Object, Object> editTrafficRuleService(Map<String,Object> params)throws Exception{
		Map<String, Object> _params = new HashMap<String, Object>();
		//交通、交通规则
		_params.put("ID", (String)params.get("ID"));
		Map<String, Object> trafficRule = this.detailTrafficRuleService(_params);
		JSONArray PRICES = JSONArray.fromObject(params.get("PRICES"));
	
		double BASE_PRICE = Double.parseDouble(String.valueOf(trafficRule.get("BASE_PRICE"))),
		BASE_INTER_PRICE = Double.parseDouble(String.valueOf(trafficRule.get("BASE_INTER_PRICE"))),
		NEW_BASE_PRICE = 0.0, NEW_BASE_INTER_PRICE = 0.0;
		
		for (Object PRICE : PRICES) {
			JSONObject _PRICE = JSONObject.fromObject((String)PRICE);
			String TYPE_ID = (String)_PRICE.get("TYPE_ID");
			String ATTR_ID = (String)_PRICE.get("ATTR_ID");
			
			/**
			 * 0FA5123749D28C87E050007F0100BCAD	门市
			 * 0FA5123749D38C87E050007F0100BCAD	同行
			 */
			if(ATTR_ID.equals("0FA5123749CF8C87E050007F0100BCAD")){
				if(TYPE_ID.equals("0FA5123749D28C87E050007F0100BCAD") && CommonUtils.checkString(String.valueOf(_PRICE.get("PRICE")))){
					NEW_BASE_PRICE = Double.parseDouble(String.valueOf(_PRICE.get("PRICE")));
				}else if(TYPE_ID.equals("0FA5123749D38C87E050007F0100BCAD") && CommonUtils.checkString(String.valueOf(_PRICE.get("PRICE")))){
					NEW_BASE_INTER_PRICE = Double.parseDouble(String.valueOf(_PRICE.get("PRICE")));
				}
			}
			
		}
		
		//成人价格是否修改过
		String editBasePrice = "NO";
		if("1".equals(String.valueOf(trafficRule.get("IS_PUB"))) && (BASE_PRICE != NEW_BASE_PRICE || BASE_INTER_PRICE != NEW_BASE_INTER_PRICE)){
			editBasePrice = "YES";
		}
		
		_params.clear();
		_params.put("ID", (String)params.get("TRAFFIC_ID"));
		Map<String, Object> traffic = this.detailService(_params);
		
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		int statusCode = this.validation(params);
		if(statusCode != 0){
			result.put("success", false);
			result.put("statusCode", statusCode);
			return result;
		}
		
		String TYPE = (String)params.get("TYPE"), 
		DAYS = (String)params.get("DAYS");
		
		
		String RULE_ID = (String)params.get("ID");
		
		//门市：0FA5123749D28C87E050007F0100BCAD, 同行：0FA5123749D38C87E050007F0100BCAD, 成人：0FA5123749CF8C87E050007F0100BCAD
		double retail_man_price = 0, inter_man_price = 0;
		
		//删除价格
		_params.put("ENTITY_ID", RULE_ID);
		this.trafficDao.delBasePriceDao(_params);
		
		_params.put("RULE_ID", RULE_ID);
		for (Object PRICE : PRICES) {
			JSONObject _PRICE = JSONObject.fromObject((String)PRICE);
			String TYPE_ID = (String)_PRICE.get("TYPE_ID");
			String ATTR_ID = (String)_PRICE.get("ATTR_ID");
			
			/**
			 * 0FA5123749D28C87E050007F0100BCAD	门市
			 * 0FA5123749D38C87E050007F0100BCAD	同行
			 */
			
			if(ATTR_ID.equals("0FA5123749CF8C87E050007F0100BCAD")){
				if(TYPE_ID.equals("0FA5123749D28C87E050007F0100BCAD")){
					retail_man_price = Double.parseDouble(String.valueOf(_PRICE.get("PRICE")));
				}else if(TYPE_ID.equals("0FA5123749D38C87E050007F0100BCAD")){
					inter_man_price = Double.parseDouble(String.valueOf(_PRICE.get("PRICE")));
				}
			}
			
			//保存价格库
			_params.put("ID", CommonUtils.uuid());
			_params.put("ENTITY_ID", RULE_ID);
			_params.put("TYPE_ID", TYPE_ID);
			_params.put("TYPE_NAME", _PRICE.get("TYPE_NAME"));
			_params.put("ATTR_ID", ATTR_ID);
			_params.put("ATTR_NAME", _PRICE.get("ATTR_NAME"));
			_params.put("PRICE", _PRICE.get("PRICE"));
			_params.put("UPDATE_USER_ID", (String)params.get("USER_ID"));
			_params.put("UPDATE_USER", (String)params.get("USER_NAME"));
			
			this.trafficDao.saveBasePriceDao(_params);
		}
		String IS_PUB = String.valueOf(trafficRule.get("IS_PUB"));
		if("0".equals(IS_PUB)){
			_params.clear();
			_params.put("RULE_ID", RULE_ID);
			//删除价格规则扩展
			this.trafficDao.delTrafficRuleExpandDao(_params);
			//保存价格规则扩展
			if(TYPE.equals("1") || TYPE.equals("2")){
				//星期规则 WEEKS
				String[] WEEKS = (String[])params.get("WEEKS");
				for (String week : WEEKS) {
					_params.put("ID", CommonUtils.uuid());
					_params.put("TEXT", week);
					this.trafficDao.saveTrafficRuleExpandDao(_params);
				}
			}else if(TYPE.equals("3") || TYPE.equals("4")){
				//日期规则DAYS
				String[] DAYS_ARRAY = DAYS.split("-");
				for (String DAY : DAYS_ARRAY) {
					_params.put("ID", CommonUtils.uuid());
					_params.put("TEXT", DAY);
					this.trafficDao.saveTrafficRuleExpandDao(_params);
				}
			}
		}
		
		//修改价格规则
		params.put("ID", RULE_ID);
		params.put("BASE_PRICE", retail_man_price);
		params.put("BASE_INTER_PRICE", inter_man_price);
		
		if("0".equals(IS_PUB)){
			params.remove("BEGIN_DATE");
			params.remove("END_DATE");
		}
		this.trafficDao.editTrafficRuleDao(params);
		
		/**
		 * 新价格是否等于旧价格
		 * 交通表中的规则ID是否等于当前规则ID
		 * 规则是否发布过
		 */
		if(traffic.get("MIN_RULE_ID") != null && (retail_man_price != BASE_PRICE || inter_man_price != BASE_INTER_PRICE)){
			String MIN_RULE_ID = (String)traffic.get("MIN_RULE_ID");
			if(MIN_RULE_ID.equals(RULE_ID)){
				if("1".equals(IS_PUB)){
					_params.clear();
					_params.put("traffic_id", (String)params.get("TRAFFIC_ID"));
					this.trafficDao.callProcedureTrfficMinprice(_params);
				}
			}
		}
		result.put("editBasePrice", editBasePrice);
		result.put("success", true);
		result.put("statusCode", "1");
		return result;
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void delTrafficRuleService(Map<String,Object> params)throws Exception{
		params.put("RULE_ID", params.get("ID"));
		this.trafficDao.delTrafficRuleExpandDao(params);
		params.put("ENTITY_ID", params.get("ID"));
		this.trafficDao.delBasePriceDao(params);
		this.trafficDao.delTrafficRuleDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void pubTrafficRuleService(Map<String,Object> params)throws Exception{
		List<Map<String, Object>> data = this.trafficDao.listTrafficRuleDao(params);
		Map<String, Object> _params = new HashMap<String, Object>();
		if(CommonUtils.checkList(data) && data.size() == 1){
			Map<String, Object> d = data.get(0);
			if(d.get("IS_PUB") != null && Integer.parseInt(String.valueOf(d.get("IS_PUB"))) == 0){
				this.trafficDao.pubTrafficRuleDao(params);
				
				params.put("traffic_rule_id", params.get("ID"));
				this.trafficDao.callProcedureRuleCompile(params);
				
				//交通是单卖还是组团
				String IS_SALE = d.get("IS_SALE").toString(); 
				if(IS_SALE.equals("0")){
					/**
					 * 交通发布
					 */
					String TRAFFIC_PUB = d.get("TRAFFIC_PUB").toString(); 
					if(TRAFFIC_PUB.equals("1")){
						String TRAFFIC_ID = (String)d.get("TRAFFIC_ID"); 
						_params.put("TRAFFIC_ID", TRAFFIC_ID);
						_params.put("ROUTE_PUB", "1");
						/**
						 * 交通被配置到了线路
						 * 线路已发布
						 */
						int cnt = this.routeService.countRouteTrafficDetailService(_params);
						if(cnt > 0){
							this.routeService.getOutTimeByruleIdService((String)params.get("ID"));
						}
					}
				}
				
			}
		}
	}
	
	public void editPubTrafficRuleService(Map<String,Object> params)throws Exception{
		Map<String, Object> _params = new HashMap<String, Object>();
		if(params.get("IS_PUB") != null && Integer.parseInt(String.valueOf(params.get("IS_PUB"))) == 1){
			params.put("traffic_rule_id", params.get("ID"));
			this.trafficDao.callProcedureRuleCompile(params);
			
			//交通是单卖还是组团
			
			String IS_SALE = params.get("IS_SALE").toString(); 
			if(IS_SALE.equals("0")){
				/**
				 * 交通发布
				 */ 
				String TRAFFIC_PUB = params.get("TRAFFIC_PUB").toString(); 
				if(CommonUtils.checkString(TRAFFIC_PUB) && TRAFFIC_PUB.equals("1")){
					String TRAFFIC_ID = (String)params.get("TRAFFIC_ID"); 
					_params.put("TRAFFIC_ID", TRAFFIC_ID);
					_params.put("ROUTE_PUB", "1");
					/**
					 * 交通被配置到了线路
					 * 线路已发布
					 */
					int cnt = this.routeService.countRouteTrafficDetailService(_params);
					if(cnt > 0){
						this.routeService.getOutTimeByruleIdService((String)params.get("ID"));
					}
				}
			}
			
		}
	}
	

	public List<Map<String, Object>> listRenewPriceTypeService(Map<String,Object> params){
		return this.trafficDao.listRenewPriceTypeDao(params);
	}
	
	public List<Map<String, Object>> listPriceTypeService(Map<String,Object> params){
		return this.trafficDao.listPriceTypeDao(params);
	}
	
	public List<Map<String, Object>> listPriceAttrService(Map<String,Object> params){
		return this.trafficDao.listPriceAttrDao(params);
	}
	
	public List<Map<String, Object>> listAllPriceAttrService(Map<String,Object> params){
		return this.trafficDao.listAllPriceAttrDao(params);
	}
	
	public List<Map<String, Object>> listTrafficRuleService(Map<String,Object> params){
		return this.trafficDao.listTrafficRuleDao(params);
	}
	
	public List<Map<String, Object>> listTrafficSeatService(Map<String,Object> params){
		return this.trafficDao.listTrafficSeatDao(params);
	}
	
	public void saveTrafficSeatService(Map<String,Object> params)throws Exception{
		this.trafficDao.saveTrafficRuleDao(params);
	}
	
	public void editTrafficSeatService(Map<String,Object> params)throws Exception{
		this.trafficDao.saveTrafficSeatDao(params);
	}
	
	public void saveTrafficRuleExpandService(Map<String,Object> params)throws Exception{
		this.trafficDao.saveTrafficRuleExpandDao(params);
	}
	
	public void delTrafficRuleExpandService(Map<String,Object> params)throws Exception{
		this.trafficDao.delTrafficRuleExpandDao(params);
	}
	
	public void saveBasePriceService(Map<String,Object> params)throws Exception{
		this.trafficDao.saveBasePriceDao(params);
	}
	
	public void delBasePriceService(Map<String,Object> params)throws Exception{
		this.trafficDao.delBasePriceDao(params);
	}
	
	public Map<String, Object> detailTrafficRuleService(Map<String,Object> params){
		List<Map<String, Object>> data = this.trafficDao.listTrafficRuleDao(params);
		if(CommonUtils.checkList(data) && data.size() == 1){
			return data.get(0);
		}
		return null;
	}
	
	public Map<String, Object> detailService(Map<String,Object> params){
		params.put("start", 1);
		params.put("end", 1);
		List<Map<String, Object>> data = this.trafficDao.listDao(params);
		if(CommonUtils.checkList(data) && data.size() == 1){
			return data.get(0);
		}
		return null;
	}
	
	public List<Map<String, Object>> listTrafficRuleCompileService(Map<String,Object> params){
		return this.trafficDao.listTrafficRuleCompileDao(params);
	}
	
	public List<Map<String, Object>> listTrafficRuleDateService(Map<String,Object> params){
		return this.trafficDao.listTrafficRuleDateDao(params);
	}
	
	public List<Map<String, Object>> listTrafficRuleSeatService(Map<String,Object> params){
		return this.trafficDao.listTrafficRuleSeatDao(params);
	}
	
	public void saveTrafficRuleSeatService(Map<String,Object> params)throws Exception{
		this.trafficDao.saveTrafficRuleSeatDao(params);
	}
	
	public void delTrafficRuleSeatService(Map<String,Object> params)throws Exception{
		this.trafficDao.delTrafficRuleSeatDao(params);
	}
	
	public List<Map<String, Object>> saleSeatNumService(Map<String,Object> params){
		return this.trafficDao.saleSeatNumDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> editSeatService(Map<String,Object> params)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> _params = new HashMap<String, Object>();
		
		String SEAT_RULE = (String)params.get("SEAT_RULE");
		String SEAT_DATE = (String)params.get("SEAT_DATE");
		String TRAFFIC_ID = (String)params.get("TRAFFIC_ID");
		
		if(!CommonUtils.checkString(SEAT_DATE) || !CommonUtils.checkString(SEAT_RULE) || !CommonUtils.checkString(TRAFFIC_ID)
				|| params.get("SEAT_COUNT") == null){
			result.put("statusCode", "-2");
			result.put("success", false);
			result.put("message", "数据不完整");
			return result;
		}
		
		
		int SEAT_COUNT = Integer.parseInt((String)params.get("SEAT_COUNT"));
		
		String[] rules = SEAT_RULE.split("-");
		String[] dates = SEAT_DATE.split("-");
		
		if(rules.length != dates.length){
			result.put("statusCode", "-2");
			result.put("success", false);
			result.put("message", "数据不完整");
			return result;
		}
		
		StringBuffer _dates = new StringBuffer();
		//整理需要修改的日期
		for (int i = 0; i < dates.length; i++) {
			_dates.append("'"+dates[i]+"'");
			if((i+1) != dates.length){
				_dates.append(",");
			}
		}
		
		//查询已销售的日期座位数
		_params.put("EDITDATES", _dates.toString());
		_params.put("TRAFFIC_ID", TRAFFIC_ID);
		List<Map<String, Object>> saleNums = this.saleSeatNumService(_params);

		//判断要修改座位数量 是否小于 已销售座位数量
		Map<String, Object> saleSeat = new HashMap<String, Object>();
		if(CommonUtils.checkList(saleNums)){
			for (Map<String, Object> saleNum : saleNums){
				saleSeat.put((String)saleNum.get("SALE_DATE"), Integer.parseInt(String.valueOf(saleNum.get("SALE_SEAT"))));
				
				/*
				int saleSeat = Integer.parseInt(String.valueOf(saleNum.get("SALE_SEAT")));
				if(saleSeat > SEAT_COUNT){
					result.put("statusCode", "-1");
					result.put("success", false);
					String SALE_DATE = (String)saleNum.get("SALE_DATE");
					result.put("message", SALE_DATE);
					return result;
				}
				*/
			}
		}
		
		
		//删除要修改的日期
		this.delTrafficRuleSeatService(_params);
		_params.clear();
		//保存要修改的日期
		for (int i = 0; i < dates.length; i++) {
			if(saleSeat.get(dates[i]) != null){
				_params.put("SEAT", SEAT_COUNT + (Integer)saleSeat.get(dates[i]));
			}else{
				_params.put("SEAT", SEAT_COUNT);
			}
			_params.put("EDIT_DATE", dates[i]);
			_params.put("RULE_ID", rules[i]);
			_params.put("TRAFFIC_ID", TRAFFIC_ID);
			_params.put("ID", CommonUtils.uuid());
			
			_params.put("UPDATE_USER", (String)params.get("USER_ID"));
			this.saveTrafficRuleSeatService(_params);
		}
		result.put("success", true);
		result.put("statusCode", "");
		result.put("message", "");
		return result;
	}
	
	public int countTrafficRuleDateService(Map<String,Object> params){
		return this.trafficDao.countTrafficRuleDateDao(params);
	}
	
	public List<Map<String, Object>> listBasePriceService(Map<String,Object> params){
		return this.trafficDao.listBasePriceDao(params);
	}
	
	public List<Map<String, Object>> listBasePriceByAttrService(Map<String,Object> params){
		return this.trafficDao.listBasePriceByAttrDao(params);
	}
	
	public List<Map<String, Object>> listMusterPlaceService(Map<String,Object> params){
		return this.trafficDao.listMusterPlaceDao(params);
	}
}
