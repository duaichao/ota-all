package cn.sd.controller.b2b;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.b2b.IDiscountService;
@RestController
@RequestMapping("/b2b/discount")
public class DiscountController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(DiscountController.class);
	
	@Resource
	private IDiscountService discountService;
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response,MapRange mr, String query, String cityId, String isUse, String proType){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		mr.pm.put("USER_ID", user.get("ID"));
		mr.pm.put("CITY_ID", cityId);
		mr.pm.put("IS_USE", isUse);
		mr.pm.put("PRO_TYPE", proType);
		if(CommonUtils.checkString(query)){
			try {
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			mr.pm.put("QUERY", query);	
		}
		
		List<Map<String, Object>> data = this.discountService.listDiscountService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/save")
	public ListRange save(HttpServletRequest request, HttpServletResponse response, MapRange mr, String source){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			
			/**
			 * 城市/站下的优惠名称不能重复
			 */
			String ID = (String)mr.pm.get("ID");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("NOTID", ID);
			params.put("QUERY", mr.pm.get("TITLE"));
			params.put("SITE_RELE_ID", mr.pm.get("SITE_RELE_ID"));
			List<Map<String, Object>> data = this.discountService.listDiscountService(params);
			if(CommonUtils.checkList(data)){
				json.setStatusCode("-1");//优惠打折名称重复
				json.setSuccess(false);
				return json;
			}else{
				mr.pm.put("CREATE_USER", user.get("USER_NAME"));
				mr.pm.put("CREATE_USER_ID", user.get("ID"));
				if(CommonUtils.checkString(ID)){
					this.discountService.updateDiscountService(mr.pm);
				}else{
					mr.pm.put("ID", CommonUtils.uuid());	
					this.discountService.saveDiscountService(mr.pm);
				}
				json.setSuccess(true);
			}
			
		} catch (Exception e) {
			log.error("添加/修改优惠打折异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加/修改优惠打折异常");
		}
		return json;
	}
	
	@RequestMapping("/switch")
	public ListRange switchs(HttpServletRequest request,HttpServletResponse response,String models){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				String isUse = jobject.getString("IS_USE");
				if(isUse.equals("1")){
					isUse = "0";
				}else{
					isUse = "1";
				}
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ID", jobject.getString("ID"));
				params.put("IS_USE", isUse);
				params.put("CREATE_USER", user.get("USER_NAME"));
				params.put("CREATE_USER_ID", user.get("ID"));
				this.discountService.updateDiscountUseService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("启用/禁用优惠打折异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("启用/禁用优惠打折异常");
		}
		return json;
	}
	
	@RequestMapping("/rule")
	public ListRange rule(HttpServletRequest request,HttpServletResponse response,MapRange mr, String discountId, String isUse, String proId, String valid){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if(CommonUtils.checkString(discountId)){
			mr.pm.put("valid", valid);
			mr.pm.put("IS_USE", isUse);
			mr.pm.put("DISCOUNT_ID", discountId);
			mr.pm.put("PRO_ID", proId);
			data = this.discountService.listDiscountRuleService(mr.pm);
		}
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/save/rule")
	public ListRange saveRule(HttpServletRequest request, HttpServletResponse response, MapRange mr, String source){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			/**
			 * 同一优惠打折 不能出现 平台类型，支付类型， 规则方式 相同的数据
			 */
			String ID = (String)mr.pm.get("ID");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("NOTID", ID);
//			params.put("RULE_TYPE", mr.pm.get("RULE_TYPE"));
			params.put("PAY_WAY", mr.pm.get("PAY_WAY"));
			params.put("PLATFROM", mr.pm.get("PLATFROM"));
			params.put("DISCOUNT_ID", mr.pm.get("DISCOUNT_ID"));
			List<Map<String, Object>> data = this.discountService.listDiscountRuleService(params);
			if(CommonUtils.checkList(data)){
				json.setStatusCode("-1");//优惠打折规则重复
				json.setSuccess(false);
				return json;
			}else{
				if(CommonUtils.checkString(ID)){
					this.discountService.updateDiscountRuleService(mr.pm);
				}else{
					mr.pm.put("ID", CommonUtils.uuid());	
					this.discountService.saveDiscountRuleService(mr.pm);
				}
				json.setSuccess(true);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("添加优惠打折规则异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加优惠打折规则异常");
		}
		return json;
	}
	
	@RequestMapping("/switch/rule")
	public ListRange switchsRule(HttpServletRequest request,HttpServletResponse response,String models){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				String isUse = jobject.getString("IS_USE");
				if(isUse.equals("1")){
					isUse = "0";
				}else{
					isUse = "1";
				}
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("ID", jobject.getString("ID"));
				params.put("IS_USE", isUse);
				this.discountService.updateDiscountRuleUseService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("启用/禁用优惠打折规则异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("启用/禁用优惠打折规则异常");
		}
		return json;
	}
	
	@RequestMapping("/save/product")
	public ListRange saveProduct(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("PRO_ID", mr.pm.get("PRO_ID"));
			this.discountService.delDiscountProductService(params);
			String[] DISCOUNT_RULE_ID = request.getParameterValues("DISCOUNT_RULE_ID");
			if(DISCOUNT_RULE_ID != null && DISCOUNT_RULE_ID.length > 0){
				for (String ID : DISCOUNT_RULE_ID) {
					mr.pm.put("ID", CommonUtils.uuid());
					//--------------------------------------
					mr.pm.put("PRO_ID", mr.pm.get("PRO_ID"));
					mr.pm.put("PRO_TYPE", mr.pm.get("PRO_TYPE"));
					mr.pm.put("DISCOUNT_ID", mr.pm.get("DISCOUNT_ID"));
					mr.pm.put("DISCOUNT_RULE_ID", ID);
					//--------------------------------------
					this.discountService.saveDiscountProductService(mr.pm);
				}
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("添加产品优惠打折异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加产品优惠打折异常");
		}
		return json;
	}
	
	@RequestMapping("/product")
	public ListRange product(HttpServletRequest request,HttpServletResponse response,MapRange mr, String proId){
		ListRange json = new ListRange();
		mr.pm.put("IS_USE", "0");
		mr.pm.put("LIMIT_DATE", "YES");
		mr.pm.put("PRO_ID", proId);
		List<Map<String, Object>> data = this.discountService.listProduceDiscountService(mr.pm);
		json.setData(data);
		json.setSuccess(true);
		return json;
	}
	
}
